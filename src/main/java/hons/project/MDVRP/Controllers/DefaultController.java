package hons.project.MDVRP.Controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hons.project.MDVRP.Models.DefaultResponseModel;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


@Controller
public class DefaultController {
    @GetMapping({"/", "/welcome"})
    public String welcome(Model model) {
        return "welcome";
    }



    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("files[]") MultipartFile file) {
        String rootPath = System.getProperty("user.dir");
        rootPath += "/src/main/upload";
        Path filepath = Paths.get(rootPath, file.getOriginalFilename());
        try (OutputStream os = Files.newOutputStream(filepath)) {
            os.write(file.getBytes());
        } catch (IOException e){

        }
        return "success";
    }
    @GetMapping("/success")
    public String successGet(){
        return "success";
    }
}

