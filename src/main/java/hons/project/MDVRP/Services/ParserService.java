package hons.project.MDVRP.Services;

import com.opencsv.CSVWriter;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


@Service
public class ParserService {

    File file;
    //The contents of the uploaded .csv are stored in records
    public List<List<String>> records = new ArrayList<>();
    String rootPath = (System.getProperty("user.dir")+"/src/main/upload/");
    int dimensions = 0;
    int normalised = 0;
    int evals = 0;

    public ParserService(){}

    public ParserService(File file){
        this.file = file;
    }

    //Populates the records List of lists
    public void parse(String fileName) throws IOException {

        fileName = rootPath + fileName;
        System.out.println(rootPath);
        System.out.println(fileName);

        BufferedReader br = new BufferedReader(new FileReader(fileName));
        String line;

        while ((line = br.readLine()) != null) {
            //Capturing these fields for later use
            String[] data = line.split(",");
            if (data[0].contains("Dimensions")){
                dimensions = Integer.parseInt(data[1]);
            } else if (data[0].contains("Normalised")){
                normalised = Integer.parseInt(data[1]);
            } else if (data[0].contains("evals")){
                evals = Integer.parseInt(data[1]);
            }

            //Removes the data that is not to be visualised - Dimensions, Normalised, evals, and so on
            List<String> parsedData = new ArrayList<String>();
            if (data.length > 2){
                for (int i = 2; i < dimensions+2; i++){
                    System.out.println("Adding: "+data[i]);
                    parsedData.add(data[i]);
                    //records.add(Arrays.asList(data));
                }
            }
            //Adds lines that are larger than 2
            if (data.length > 2){
                records.add(parsedData);
                System.out.println("New Array -------------");
            }

        }

        writeParsedFile();

    }

    //Prints the contents of the uploaded file
    public void printCSV(){
        for (List<String> row : records){
            for (String str : row){
                System.out.println(str);
            }
        }
    }

    //This method creates a .csv with uniform name, path, and formatting in order to make reading more consistent
    private void writeParsedFile() throws IOException {
        CSVWriter writer = new CSVWriter(new FileWriter(rootPath+"upload.csv"), ',');

        int count = 1;
        for (List<String> line : records){
            System.out.println("COUNT IS: "+count);
            writer.writeNext(line.toArray(new String[0]));
            count += 1;
        }
        writer.close();
    }
}
