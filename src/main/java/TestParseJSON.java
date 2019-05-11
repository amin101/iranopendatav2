
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Map;

public class TestParseJSON {

    private Gson gson;
    private String fileLocation = "";
//file location  = "C:\\Users\\User\\Desktop\\iranopendatav2\\src\\data\\dataset.json"

    public TestParseJSON(String fileLocation) {
        gson = new Gson();
        this.fileLocation = fileLocation;
    }

    public static void main(String[] args) {

        //  JsonElement ff = value.getAsJsonObject().get("data");

        //  System.out.println(ff.getAsJsonArray().get(0).getAsJsonArray().get(0).getAsJsonObject().get("key"));
    }

    public JsonElement getJsonElement() {

        FileReader myJSON = null;
        try {
            myJSON = new FileReader(fileLocation);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return gson.fromJson(myJSON, JsonElement.class);
    }

    public JsonArray getMetaData() {

        JsonArray metadata = getJsonElement().getAsJsonObject().get("metadata").getAsJsonArray();
        return metadata;
    }

    public JsonArray getData() {

        JsonArray data = getJsonElement().getAsJsonObject().get("data").getAsJsonArray();
        return data;
    }


}
