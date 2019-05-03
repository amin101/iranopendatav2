import com.sun.deploy.ref.AppRef;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
//import org.apache.jena.rdf.model.Resource;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CreateRDF {


    Model model;
    OntModel vocabularyModel;
    Map<String, String> FORMATS = new HashMap<>();

    public CreateRDF(String vocabularyDir, String resource, Map<String, String[]> rdfData) {
        //create Model

        FORMATS.put("Turtle", "ttl");
        FORMATS.put("N-Triples", "nt");
        FORMATS.put("N-Quads", "nq");
        FORMATS.put("TriG", "trig");
        FORMATS.put("RDF/XML", "rdf");
        FORMATS.put("JSON-LD", "jsonld");
        FORMATS.put("RDF Thrift", "trdf");
        FORMATS.put("RDF/JSON", "rj");
        FORMATS.put("TriX", "trix");

        vocabularyModel = ModelFactory.createOntologyModel();

        vocabularyModel.read(vocabularyDir);

        model = ModelFactory.createDefaultModel().
                setNsPrefix("IranOpenData", vocabularyModel.getNsPrefixURI(""));
        buildRdfModel(resource, rdfData);
    }

    private void buildRdfModel(String res, Map<String, String[]> rdfData) {
        //create resource
        Resource resource = model.createResource(res);


        for (Map.Entry<String, String[]> entry : rdfData.entrySet()) {

            Property property = vocabularyModel.getProperty(vocabularyModel.getNsPrefixURI("") + entry.getKey());
//            System.out.println(property.toString());
            if (property.isProperty()) {

                for (String val : entry.getValue()) {
                    //   System.out.println(val);
                    resource.addProperty(property, val);
                }

            }
        }
    }

    //Types are: "Turtle","N-Triples","N-Quads","TriG","RDF/XML","JSON-LD","RDF Thrift","RDF/JSON","TriX"

    public Map<String, String> saveType(String outputType, String appRoot) {
//        String dir = System.getProperty("catalina.base")+ File.separator+"webapps"+File.separator+"outputdata";
        Map result = new HashMap<String, String>();
        String dir = appRoot + File.separator + "outputdata";
        String fileName = dir + File.separator + "output_" + new Date().getTime() + "." + FORMATS.get(outputType);

        new File(dir).mkdirs();
//        System.out.println();
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
        //  return myFile.getAbsolutePath();
        result.put("url", myFile.getName());
        try {
            String content = new String(Files.readAllBytes(myFile.toPath()), "UTF-8");
            result.put("content", content);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("could not read file");
        }
        return result;

    }

}
