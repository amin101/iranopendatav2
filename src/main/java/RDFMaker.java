import com.google.gson.Gson;
import org.apache.jena.rdf.model.Model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public abstract class RDFMaker {

    Map<String, String> FORMATS = new HashMap<>();

    {
        FORMATS.put("Turtle", "ttl");
        FORMATS.put("N-Triples", "nt");
        FORMATS.put("N-Quads", "nq");
        FORMATS.put("TriG", "trig");
        FORMATS.put("RDF/XML", "rdf");
        FORMATS.put("JSON-LD", "jsonld");
        FORMATS.put("RDF Thrift", "trdf");
        FORMATS.put("RDF/JSON", "rj");
        FORMATS.put("TriX", "trix");
    }

    public abstract Model getModel();

    public String output(String outputType, String appRoot) {
        outputType = (outputType == null || outputType.isEmpty()) ? "RDF/XML" : outputType;
        Model model = getModel();
        Map result = new HashMap<String, String>();

        String dir = appRoot + File.separator + "outputdata";
        String fileName = dir + File.separator + "output_" + new Date().getTime() + "." + FORMATS.get(outputType);

        new File(dir).mkdirs();
        File myFile = new File(fileName);

        try {
            FileOutputStream output = new FileOutputStream(myFile);
            model.write(output, outputType);
            //write to cmd for test
            model.write(System.out, outputType);
            output.close();

        } catch (IOException e) {

        }
//        return fileName;
        result.put("url", myFile.getName());
        try {
            String content = new String(Files.readAllBytes(myFile.toPath()), "UTF-8");
            result.put("content", content);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("could not read file");
        }

        return new Gson().toJson(result);


    }
}
