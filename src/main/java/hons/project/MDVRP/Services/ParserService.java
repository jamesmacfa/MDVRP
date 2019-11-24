package hons.project.MDVRP.Services;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class ParserService {

    File file;
    public List<List<String>> records = new ArrayList<>();

    public ParserService(){}

    public ParserService(File file){
        this.file = file;
    }

    public void parse(String fileName) throws IOException {
        String rootPath = (System.getProperty("user.dir")+"/src/main/upload/");
        fileName = rootPath + fileName;
        System.out.println(rootPath);
        System.out.println(fileName);


        BufferedReader br = new BufferedReader(new FileReader(fileName));
        String line;
        while ((line = br.readLine()) != null) {
            String[] data = line.split(",");
            records.add(Arrays.asList(data));
        }


    }

    public void printCSV(){
        for (List<String> row : records){
            for (String str : row){
                System.out.println(str);
            }
        }
    }
}
