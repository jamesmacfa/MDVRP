package hons.project.MDVRP.Services;

import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class ParserService {
    File file;
    public ParserService(File file){
        this.file = file;
    }

    
}
