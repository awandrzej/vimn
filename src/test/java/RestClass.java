
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.parser.ParseException;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
            JsonObject jo = element.getAsJsonObject();
            jo.addProperty("title", "Creating new JSON");
            jo.remove("last_activity_date");
            newJson += jo.toString();
            if ((i<arr.size()-1)) newJson += ",";
        }
        newJson += "],";
        newJson += "\n \"has_more\":"+jsonObject.getAsJsonPrimitive("has_more")+",";
        newJson += "\n \"quota_max\":"+jsonObject.getAsJsonPrimitive("quota_max")+",";
        newJson += "\n \"quota_remaining\":"+jsonObject.getAsJsonPrimitive("quota_remaining");
        newJson += "\n  }";


        for (int i = 0; i < arr.size(); i++) {
            JsonElement element = arr.get(i).getAsJsonObject();
            JsonObject jo = element.getAsJsonObject();
            JsonElement elementnew = "{\"tags\":[\"perl\",\"excel\",\"xlsx\"],\"owner\":{\"reputation\":96,\"user_id\":1957542,\"user_type\":\"registered\",\"accept_rate\":71,\"profile_image\":\"https://www.gravatar.com/avatar/ad7edfe578fee5a8db4dc34c3e57eae7?s=128&d=identicon&r=PG\",\"display_name\":\"MiSo\",\"link\":\"http://stackoverflow.com/users/1957542/miso\"},\"is_answered\":false,\"view_count\":2348,\"answer_count\":2,\"score\":5,\"creation_date\":1379605169,\"last_edit_date\":1424332336,\"question_id\":18899136,\"link\":\"http://stackoverflow.com/questions/18899136/modifying-xlsx-file-with-perl\",\"title\":\"BLA BLA\"}";
            }






/*

        ObjectMapper om = new ObjectMapper();
        try {
            Map<String, Object> m1 = (Map<String, Object>)(om.readValue(json, Map.class));
            Map<String, Object> m2 = (Map<String, Object>)(om.readValue(newJson, Map.class));
            System.out.println(m1);
            System.out.println(m2);
            System.out.println(m1.equals(m2));
        } catch (Exception e) {
            e.printStackTrace();
        }
*/


    }


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
