package pdl.backend;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;

import javax.imageio.ImageIO;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import io.scif.FormatException;

@RestController
public class ImageController {

  @Autowired
  private ObjectMapper mapper;

  private final ImageDao imageDao;

  @Autowired
  public ImageController(ImageDao imageDao) {
    this.imageDao = imageDao;
  }

  @RequestMapping(value = "/images/{id}", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
  public ResponseEntity<?> getImageAlgo(@PathVariable long id,
                                        @RequestParam(name="vignette", required = false) boolean isVignette,
                                        @RequestParam(name="algorithm", required = false) String algorithm,
                                        @RequestParam(name="gain", required = false) String gain,
                                        @RequestParam(name="filtersize", required = false) String filtersize,
                                        @RequestParam(name="teinte", required = false) String teinte,
                                        @RequestParam(name="filter", required = false) String filter) throws IOException, FormatException, BadArguments  {
    Optional<Image> imgFile = imageDao.retrieve(id);

    if (imgFile.isEmpty()) {
      //System.out.print("YOUHOU");
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    String[] params = {null,null,null};

    if (algorithm != null) {
      if (gain!=null && filtersize==null && filter == null && teinte==null) {
        params[0] = algorithm;
        params[1] = gain;
      } else if (gain==null && filtersize!=null && filter != null && teinte==null) {
        //System.out.println("c'est pas la");
        params[0] = algorithm;
        params[1] = filter;
        params[2] = filtersize;
      } else if (gain==null && filtersize==null && filter == null && teinte!=null) {
        params[0] = algorithm;
        params[1] = teinte;
      } else if (gain==null && filtersize==null && filter == null && teinte==null) {
        params[0] = algorithm;
      } else {
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
      }
      try {
        imageDao.update(imgFile.get(),params);

      } catch(FormatException a) {
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
      } catch(IOException e) {
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
      } catch(BadArguments message) {
        return ResponseEntity
                      .status(HttpStatus.INTERNAL_SERVER_ERROR)
                      .contentType(MediaType.TEXT_HTML)
                      .body(message);
      }
      return ResponseEntity
              .ok()
              .contentType(MediaType.IMAGE_JPEG)
              .body(imgFile.get().getData());

    } else if (gain==null && filtersize==null && filter == null && teinte==null){
      if (isVignette == true) {
        System.out.println(imgFile.get().getVignetteData());
        return ResponseEntity
                  .ok()
                  .contentType(MediaType.IMAGE_JPEG)
                  .body(imgFile.get().getVignetteData());
      }

      return ResponseEntity
                  .ok()
                  .contentType(MediaType.IMAGE_JPEG)
                  .body(imgFile.get().getData());
    }

    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);


  }


  @RequestMapping(value = "/images/{id}", method = RequestMethod.DELETE)
  public ResponseEntity<?> deleteImage(@PathVariable("id") long id) {
    //System.out.println(id);
    imageDao.delete(imageDao.retrieve(id).get());
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @RequestMapping(value = "/images/{id}", method = RequestMethod.POST)
  public ResponseEntity<?> addImage(@PathVariable("id") long id,
                                    @RequestParam("image") MultipartFile file) {

    try {
      System.out.println(file.getBytes().length);
      imageDao.changeVignette(file.getBytes(), Long.valueOf(id));
    } catch (final IOException e) {
      e.printStackTrace();
    } 
    return new ResponseEntity<>(HttpStatus.OK);

  }

  @RequestMapping(value = "/images", method = RequestMethod.POST)
  public ResponseEntity<?> addImage(@RequestParam("file") MultipartFile file,
                                    RedirectAttributes redirectAttributes) {
        try {
          final ClassPathResource imgFile = new ClassPathResource(file.getOriginalFilename());
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

          Image img = new Image(file.getOriginalFilename(),
                                file.getBytes(),
                                filedims,
                                new org.springframework.http.MediaType(file.getContentType().substring(6)),
                                vignetteData);

          imageDao.create(img);
        } catch (final IOException e) {
          e.printStackTrace();
        }
      return new ResponseEntity<>(HttpStatus.OK);
  }

  @RequestMapping(value = "/images", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
  @ResponseBody
  public ArrayNode getImageList() {
    ArrayNode nodes = mapper.createArrayNode();
    List<Image> imgs = imageDao.retrieveAll();
    //System.out.println(imgs);
    imgs.forEach(image -> {
      nodes.addObject().put("id", image.getId()).put("name", image.getName());
    });

    

    //System.out.println(nodes);
    return nodes;
  }

  
}