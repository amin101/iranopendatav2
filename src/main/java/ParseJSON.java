
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

public class ParseJSON {

    private Gson gson;
    private String file = "";
//file location  = "C:\\Users\\User\\Desktop\\iranopendatav2\\src\\data\\dataset.json"

    public ParseJSON(String file) {
        gson = new Gson();
        this.file = file;
    }

    public static void main(String[] args) {

        //  JsonElement ff = value.getAsJsonObject().get("data");

        //  System.out.println(ff.getAsJsonArray().get(0).getAsJsonArray().get(0).getAsJsonObject().get("key"));
    }

    public JsonElement getJsonElement() {

//        FileReader myJSON = null;
//        try {
//            myJSON = new FileReader(file);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }


        return gson.fromJson(file, JsonElement.class);
    }

    public JsonArray getMetaData() {

        JsonArray metadata = getJsonElement().getAsJsonObject().get("metadata").getAsJsonArray();

        return metadata;
    }

    public JsonArray getData() {

        JsonArray data = getJsonElement().getAsJsonObject().get("data").getAsJsonArray();
        return data;
    }

    public String getResource() {

        String data = getJsonElement().getAsJsonObject().get("resource").getAsString();
        return data;
    }


}
