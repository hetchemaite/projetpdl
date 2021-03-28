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

import java.awt.image.BufferedImage;
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

      long[] filedims = {(long) buffImg.getWidth(),(long) buffImg.getHeight()};

      fileContent = Files.readAllBytes(imgFile.getFile().toPath());
      //filetype = new org.springframework.http.MediaType(Files.probeContentType(imgFile.getFile().toPath()));
      filetype = new org.springframework.http.MediaType(Files.probeContentType(imgFile.getFile().toPath()).substring(6));

      Image img = new Image("test.jpg", fileContent, filedims, filetype);
      
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
  public void update(final Image img, final String[] params) throws FormatException,IOException {
    try {
      SCIFIOImgPlus<UnsignedByteType> imgbyte = ImageConverter.imageFromJPEGBytes(img.getData());
      Algo.increaseLuminosity(imgbyte, 30);
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
    
    System.out.println(images.remove(img.getId()));
    System.out.println(images);
  }

}
