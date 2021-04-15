package pdl.backend;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.imageio.ImageIO;

import net.imglib2.Cursor;
import net.imglib2.RandomAccess;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.img.array.ArrayImgs;
import io.scif.FormatException;
import io.scif.SCIFIO;
import io.scif.gui.BufferedImageReader;
import io.scif.img.ImgIOException;
import io.scif.img.ImgOpener;
import io.scif.img.ImgSaver;
import io.scif.img.SCIFIOImgPlus;
import net.imglib2.type.numeric.integer.UnsignedByteType;
import net.imglib2.exception.IncompatibleTypeException;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Repository;

@Repository
public class ImageDao implements Dao<Image> {

  private final Map<Long, Image> images = new HashMap<>();
  public ImageDao() {
    // placez une image test.jpg dans le dossier "src/main/resources" du projet
    final ClassPathResource imgFile = new ClassPathResource("test.jpg");
    byte[] fileContent;
    org.springframework.http.MediaType filetype;

    try {

      BufferedImage buffImg = ImageIO.read(imgFile.getFile());

      int[] filedims = {buffImg.getWidth(),buffImg.getHeight()};
      float[] vignetteMaxDims = {110,50};
      int[] vignetteDims = {0,0};

      if (filedims[0]/vignetteMaxDims[0] > filedims[1]/vignetteMaxDims[1]) {
        vignetteDims[0]= (int) vignetteMaxDims[0];
        vignetteDims[1] = (int) (filedims[1] * (vignetteMaxDims[0]/filedims[0]));
      } else {
        vignetteDims[1]=(int) vignetteMaxDims[1];
        System.out.println(filedims[0]);
        System.out.println(vignetteMaxDims[1]/filedims[1]);
        vignetteDims[0] = (int) (filedims[0] * (vignetteMaxDims[1]/filedims[1]));
      }
      System.out.println("" + vignetteDims[0]+"    "+ vignetteDims[1]);

      BufferedImage vignette = new BufferedImage((int) vignetteDims[0],(int) vignetteDims[1] , buffImg.getType());

      Graphics2D g = vignette.createGraphics();

      g.drawImage(buffImg, 0, 0,(int) vignetteDims[0],(int) vignetteDims[1], null);
      g.dispose();
      
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      ImageIO.write(vignette, Files.probeContentType(imgFile.getFile().toPath()).substring(6), baos);
      byte[] vignetteData = baos.toByteArray();

     

      fileContent = Files.readAllBytes(imgFile.getFile().toPath());

      filetype = new org.springframework.http.MediaType(Files.probeContentType(imgFile.getFile().toPath()).substring(6));


      Image img = new Image("test.jpg", fileContent, filedims, filetype, vignetteData);

      images.put(img.getId(), img);
    } catch (final IOException e) {
      e.printStackTrace();
    }
  }

  
  @Override
  public Optional<Image> retrieve(final long id) {
    if (images.get(id) == null) {
      return Optional.empty();
    }
    return Optional.of(images.get(id));
  }

  public void changeVignette(final byte[] data, long id) {
    System.out.println("id :" + id + " data : " + data.length);
    images.get(id).setVignetteData(data);
  }

  @Override
  public List<Image> retrieveAll() {
    List<Image> imgs = new ArrayList<>();
    images.forEach((id, img) -> imgs.add(img));
    return imgs;
  }

  @Override
  public void create(final Image img) {

    images.put(img.getId(), img);
  }


  @Override
  public void update(final Image img, final String[] params) throws FormatException,IOException,BadArguments {
    //System.out.println(img.getVignetteData());
    try {
      SCIFIOImgPlus<UnsignedByteType> imgbyte = ImageConverter.imageFromJPEGBytes(img.getData());
      switch (params[0]) {
        case "increaseLuminosity":

          //System.out.println(params[1] + "     " + params[2]);
          if (params[1]!= null && params[2]==null) {

            Algo.increaseLuminosity(imgbyte, Integer.valueOf(params[1]));
          } else {

            throw new BadArguments(params[0] + "prend un seul argument");
          }
          break;

        case "histogram":

        if (params[1]== null && params[2]==null) {
          Algo.EgalisationHistogramme(imgbyte);
        } else {
          throw new BadArguments(params[0] + "ne prend pas d'arguments");
        }
        break;

        case "coloredFilter":

        if (params[1]!= null && params[2]==null) {
          int s = Integer.valueOf(params[1]);
          if (s < 360 && s >= 0) {
            Algo.Teinte(imgbyte, s);
          } else {
            throw new BadArguments(params[1] + " n'est pas une valeur valide pour " + params [0]);
          }
        } else {
          throw new BadArguments(params[0] + "prend un seul argument");
        }
        break;

        case "blurryFilter":

          if (params[1]!= null && params[2]!=null) {
            if (params[1] == "moyen") {
              Algo.meanFilter(imgbyte,imgbyte, Integer.valueOf(params[2]));
            } else {
              Algo.FiltreGaussien(imgbyte, imgbyte, Integer.valueOf(params[2]));
            }
          } else {
            
            throw new BadArguments(params[0] + "prend 2 arguments");
          }
        break;
        
        case "borderFilter":
        if (params[1]== null && params[2]==null) {
          SCIFIOImgPlus<UnsignedByteType> imgbyte2 = ImageConverter.imageFromJPEGBytes(img.getData());
          Algo.BorderFilter(imgbyte, imgbyte2);
        } else {
          throw new BadArguments(params[0] + "ne prend pas d'arguments");
        }
        break;

        default:
          throw new BadArguments(params[0] + "n'existe pas");
      }

      byte[] newImage = ImageConverter.imageToJPEGBytes(imgbyte);
      img.setData(newImage);
    } catch(FormatException a) {
      throw a;
    } catch(IOException e) {
      throw e;
    }

  }

  @Override
  public void delete(final Image img) {
    
    images.remove(img.getId());
    //System.out.println(images);
  }

}
