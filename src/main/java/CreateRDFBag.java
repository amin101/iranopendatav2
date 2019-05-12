import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.apache.jena.rdf.model.*;

public class CreateRDFBag extends RDFMaker {

    private Model model;
    private ParseJSON parseJSON;
    private Bag bag;
    private String resource;

    public CreateRDFBag(String file) {
        model = ModelFactory.createDefaultModel();
        bag = model.createBag();
//        System.out.println(getLocation("data/dataset.json"));
        parseJSON = new ParseJSON(file);
        resource = getResource();
    }

    public static void main(String[] args) {
        CreateRDFBag CreateRDFBag = new CreateRDFBag("data/dataset.json");
        CreateRDFBag.addBagMetaData();
        CreateRDFBag.addPrimaryMetadata(CreateRDFBag.getResource());
        // CreateRDFBag.bagOut();
        CreateRDFBag.output("", "");
    }

    private String getLocation(String location) {
        return getClass().getProtectionDomain().getCodeSource().getLocation().getPath() + location;
    }

    public String getResource() {

        if (resource == null) {
            resource = parseJSON.getResource();
        }
        return resource;
    }

    public Bag addBagMetaData() {
        JsonArray metadata = parseJSON.getMetaData();
        metadata.forEach(x -> {
            String key = x.getAsJsonObject().get("key").getAsString();
            String value = x.getAsJsonObject().get("value").getAsString();
//            String a = "amin is here";
//            System.out.println(a+key);
            bag.addProperty(model.createProperty(key), value);
        });
        return bag;
    }

    public Bag addPrimaryMetadata(String resource) {
        JsonArray data = parseJSON.getData();

        for (int i = 0; i < data.size(); i++) {
            JsonArray dataArray = data.get(i).getAsJsonArray();
            Resource myResource = model.createResource(resource + "_" + i);

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
        return model.write(System.out, "RDF/XML");

    }


    @Override
    public Model getModel() {
        return model;
    }
}
