
import com.google.gson.*;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.config.SSLConfig;
import org.apache.commons.io.FileUtils;
import org.json.simple.parser.ParseException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.jayway.restassured.RestAssured.get;
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
    /*
    Generate a new json that will be equal to the given one except for the title, that will be „CREATING NEW JSON” for each of the items.*//*
    */
    public void duplicateJson() throws ParseException {

        String newJson = "{\"items\": [\n";

        JsonArray arr = jsonObject.getAsJsonArray("items");
        for (int i = 0; i < arr.size(); i++) {
            JsonElement element = arr.get(i).getAsJsonObject();
            JsonObject jo = element.getAsJsonObject();
            jo.addProperty("title", "Creating new JSON");
            jo.remove("last_activity_date");
            newJson += jo.toString();
            if ((i < arr.size() - 1)) newJson += ",";
        }
        newJson += "],";
        newJson += "\n \"has_more\":" + jsonObject.getAsJsonPrimitive("has_more") + ",";
        newJson += "\n \"quota_max\":" + jsonObject.getAsJsonPrimitive("quota_max") + ",";
        newJson += "\n \"quota_remaining\":" + jsonObject.getAsJsonPrimitive("quota_remaining");
        newJson += "\n  }";

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
        System.out.print("tags without duplicates that should be removed:");
        System.out.println(tags.toString());

    }


    /*print test header*/

    public void printHeader(String title) {
        System.out.println("**************************************************");
        System.out.println(title);
        System.out.println("**************************************************");
    }


}
