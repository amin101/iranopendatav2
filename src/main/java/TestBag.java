import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.apache.jena.rdf.model.Bag;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;

public class TestBag {
    static String personURI = "http://somewhere/JohnSmith";
    static String fullName = "John Smith";
    private static String URI = "http://www.ontologyportal.org/SUMO.owl#Building";
    Gson gson = new Gson();
    Model model;
    TestParseJSON parseJSON;
    private Bag bag;


    public TestBag(String fileLocation) {
        model = ModelFactory.createDefaultModel();
        bag = model.createBag();
        parseJSON = new TestParseJSON(fileLocation);
    }

    public static void main(String[] args) {
        TestBag testBag = new TestBag("C:\\Users\\User\\Desktop\\iranopendatav2\\src\\data\\dataset.json");
        testBag.addBagMetaData();
        testBag.addPrimaryMetadata("http://www.ontologyportal.org/SUMO.owl#Building");
        testBag.bagOut();
    }

    public Bag addBagMetaData() {
        JsonArray metadata = parseJSON.getMetaData();
        metadata.forEach(x -> {
            String key = String.valueOf(x.getAsJsonObject().get("key"));
            String value = String.valueOf(x.getAsJsonObject().get("value"));

            bag.addProperty(model.createProperty(key), value);
        });
        return bag;
    }

    public Bag addPrimaryMetadata(String resource) {
        JsonArray data = parseJSON.getData();

        for (int i = 0; i < data.size(); i++) {
            JsonArray dataArray = data.get(i).getAsJsonArray();
            Resource myResource = model.createResource(resource);

            for (int j = 0; j < dataArray.size(); j++) {
                JsonObject dataObject = dataArray.get(j).getAsJsonObject();
                myResource.addProperty(
                        model.createProperty(dataObject.get("key").getAsString()),
                        dataObject.get("value").getAsString()
                );

            }

            bag.add(myResource);
        }
        return bag;
    }

    public Model bagOut() {
        return model.write(System.out);
    }


}
