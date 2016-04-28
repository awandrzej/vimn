
import com.google.gson.*;
import org.apache.commons.io.FileUtils;
import org.json.JSONString;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.jayway.restassured.RestAssured.get;

/**
 * Created by Andrzej.Wudara on 2016-04-28.
 */
public class RestClass {

    static final String REST_DATA_LOCATION = "src/test/resources/";
    String json;
    JsonObject jsonObject;
    @Before()
    public void readJson(){
        json = getFileContent("json.json");
        jsonObject = new JsonParser().parse(json).getAsJsonObject();
    }




    @Test
    /*
    Generate a new json that will be equal to the given one except for the title, that will be „CREATING NEW JSON” for each of the items.*//*
    */
    public void checkProfileImage() throws ParseException {

        String newJson = "{\"items\": [\n";

        JsonArray arr = jsonObject.getAsJsonArray("items");
        for (int i = 0; i < arr.size(); i++) {
            JsonElement element = arr.get(i).getAsJsonObject();
            //System.out.println(element.toString());
            JsonObject jo = element.getAsJsonObject();
            jo.addProperty("title", "Creating new JSON");
            jo.remove("last_activity_date");
        }

        newJson += "],";
        jsonObject.getAsJsonPrimitive("has_more");
        newJson += "\n \"has_more\":";
        newJson += "\n \"quota_max\":";
        newJson += "\n \"quota_remaining\":";
        newJson += "\n  }";

        System.out.println(newJson);

    }


//    ],
//            "has_more": true,
//            "quota_max": 300,
//            "quota_remaining": 299




//    @Test
     //Each owner has a profile_image and link values: verify that these urls are valid
//    public void checkProfileImage(){
//        JsonArray arr = jsonObject.getAsJsonArray("items");
//        for (int i = 0; i < arr.size(); i++) {
//            JsonElement element = arr.get(i).getAsJsonObject().get("owner").getAsJsonObject().get("profile_image");
//            System.out.println(element);
              //rest-assured
//            get("https://raw.githubusercontent.com/plu/JPSimulatorHacks/master/Data/test.png").then().statusCode(200);
//        }
//    }

//    @Test
/*
    Retrieve all tags and save them to a list (duplicates should be removed)
*/
    public void removeTagsDuplicates(){

        List<String> tags = new ArrayList<String>();;
        JsonArray arr = jsonObject.getAsJsonArray("items");
        for (int i = 0; i < arr.size(); i++) {
            JsonArray elements = (JsonArray) arr.get(i).getAsJsonObject().get("tags");
            for (JsonElement element:elements){
                if(!tags.toString().contains(element.getAsString()))    tags.add(element.getAsString());
            }
        }
        System.out.print("tags without duplicates that should be removed:");
        System.out.println(tags.toString());

    }




    public String getFileContent(String path) {
        try {
            return FileUtils.readFileToString(new File(REST_DATA_LOCATION + path));
        } catch (IOException ex) {
            throw new IllegalArgumentException(ex);
        }
    }

}
