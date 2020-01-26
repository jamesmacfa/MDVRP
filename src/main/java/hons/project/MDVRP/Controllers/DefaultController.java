package hons.project.MDVRP.Controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hons.project.MDVRP.Models.DefaultResponseModel;
import hons.project.MDVRP.Services.ParserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;


@Controller
public class DefaultController {
    Path filepath;
    @Autowired
    ParserService parserService;

    @GetMapping({"/", "/welcome"})
    public String welcome(Model model) {
        return "welcome";
    }

//    @RequestMapping("/csvraw")
//    @ResponseBody()
//    public List<List<String>> getCsv(Model model){
//
//        return parserService.records;
//    }

    @GetMapping("/csvraw")
    public ResponseEntity<Resource> downloadFile( HttpServletRequest request) throws FileNotFoundException {
        String rootPath = System.getProperty("user.dir");
        rootPath += "/src/main/upload";
        // Load file as Resource
        InputStreamResource resource = new InputStreamResource(new FileInputStream(rootPath+"/upload.csv"));

        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {

        }

        // Fallback to the default content type if type could not be determined
        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
    @GetMapping("/csvdemo")
    public String csvdemo(Model model) {return "csvdemo"; }

    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("files[]") MultipartFile file) {
        String rootPath = System.getProperty("user.dir");
        rootPath += "/src/main/upload";
        filepath = Paths.get(rootPath, file.getOriginalFilename());
        try (OutputStream os = Files.newOutputStream(filepath)) {
            os.write(file.getBytes());
            System.out.println(file.getOriginalFilename());
            parserService.parse(file.getOriginalFilename());
            parserService.printCSV();

        } catch (IOException e){

        }
        return "success";
    }
    @GetMapping("/success")
    public String successGet(){
        return "success";
    }
}

