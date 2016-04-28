
import com.google.gson.*;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.config.SSLConfig;
import org.json.simple.parser.ParseException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.jayway.restassured.RestAssured.given;

/**
 * Created by Andrzej.Wudara on 2016-04-28.
 */
public class JSONClassTest {

    String json;
    JsonObject jsonObject;
    GetJSON getJSON;

    @Before()
    public void readJson() {
        GetJSON getJSON = new GetJSON();
        /*if address will not be accssible you can use getFileContent insetad of getFileContentURL*/
        //json = getJSON.getFileContent("json.json");
        json = getJSON.getFileContentURL();
        jsonObject = new JsonParser().parse(json).getAsJsonObject();
    }

    @Test

    public void duplicateJson() throws ParseException {
        printHeader("New json with titles CREATING NEW JSON for each of the items, remove last_activity_date, compare old and new json");
        String newJson = "{\"items\": [\n";
        JsonArray arr = jsonObject.getAsJsonArray("items");
        for (int i = 0; i < arr.size(); i++) {
            JsonElement element = arr.get(i).getAsJsonObject();
            JsonObject jObject = element.getAsJsonObject();
            jObject.addProperty("title", "Creating new JSON");
            jObject.remove("last_activity_date");
            newJson += jObject.toString();
            if ((i < arr.size() - 1)) newJson += ",";
        }
        newJson += "],";
        newJson += "\n \"has_more\":" + jsonObject.getAsJsonPrimitive("has_more") + ",";
        newJson += "\n \"quota_max\":" + jsonObject.getAsJsonPrimitive("quota_max") + ",";
        newJson += "\n \"quota_remaining\":" + jsonObject.getAsJsonPrimitive("quota_remaining");
        newJson += "\n  }";

        jsonObject = new JsonParser().parse(json).getAsJsonObject();
        arr = jsonObject.getAsJsonArray("items");
        JsonObject jsonObjectNew = new JsonParser().parse(newJson).getAsJsonObject();
        JsonArray arrNew = jsonObjectNew.getAsJsonArray("items");

        for (int i = 0; i < arr.size() ; i++) {
            JsonElement element = arr.get(i).getAsJsonObject();
            Gson gson = new Gson();
            String json = element.toString();

            Map<String, Object> map = new HashMap<String, Object>();
            map = (Map<String, Object>) gson.fromJson(json, map.getClass());
            map = (Map<String, Object>) gson.fromJson(json, map.getClass());

            for (Map.Entry<String, Object> entry : map.entrySet()) {
                JsonElement elementNew = arrNew.get(i).getAsJsonObject().get(entry.getKey());
                JsonElement elementOld = arr.get(i).getAsJsonObject().get(entry.getKey());

                String eOld = elementOld == null ? "no item" : elementOld.toString();
                String eNew = elementNew == null ? "no item" : elementNew.toString();

                if (!eNew.equalsIgnoreCase(eOld)) {
                    System.out.println("-----------------------------------------");
                    System.out.println("item:"+entry.getKey());

                    System.out.println("old Json value:"+eOld);
                    System.out.println("new Json value:"+eNew);

                }

            }
        }
    }

        @Test
        public void checkProfileImage() {
        printHeader("Each owner has a profile_image and link values: verify that these urls are valid");
        boolean testOk = true;
        JsonArray arr = jsonObject.getAsJsonArray("items");
        for (int i = 0; i < arr.size(); i++) {
            String element = arr.get(i).getAsJsonObject().get("owner").getAsJsonObject().get("profile_image").toString().replace("\"", "");
            System.out.println(element);
            /*rest-assured*/
            int statusCode =
                    given().config(RestAssured.config().sslConfig(
                            new SSLConfig().allowAllHostnames())).then().
                            get(element).then().extract().statusCode();
            //get(element).then().statusCode(200|403); - commented assertion for check all images
            System.out.print("---> statusCode: " + statusCode + "\n");
            if (statusCode != 200) testOk = false;
        }
        System.out.println("\n");
        Assert.assertTrue("All the profile images should response with status code: 200", testOk);
    }

    @Test

    public void removeTagsDuplicates() {
        printHeader("Retrieve all tags and save them to a list (duplicates should be removed");
        List<String> tags = new ArrayList<String>();
        ;
        JsonArray arr = jsonObject.getAsJsonArray("items");
        for (int i = 0; i < arr.size(); i++) {
            JsonArray elements = (JsonArray) arr.get(i).getAsJsonObject().get("tags");
            for (JsonElement element : elements) {
                if (!tags.toString().contains(element.getAsString())) tags.add(element.getAsString());
            }
        }
        System.out.println("tags without duplicates that should be removed:\n");
        for (String tag : tags) {
            System.out.println(tag);
        }
    }
    /*print test header*/

    public void printHeader(String title) {
        System.out.println("**************************************************");
        System.out.println(title);
        System.out.println("**************************************************");
    }


}
