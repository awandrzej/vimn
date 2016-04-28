import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

import static com.jayway.restassured.RestAssured.get;

/**
 * Created by Andrzej on 28.04.2016.
 */
public class GetJSON {
    static final String REST_DATA_LOCATION = "src/test/resources/";
    static final String JSON_URL = "https://api.stackexchange.com/2.2/search?order=desc&amp;sort=activity&intitle=perl&site=stackoverflow";


    public String getFileContent(String path) {
        try {
            return FileUtils.readFileToString(new File(REST_DATA_LOCATION + path));
        } catch (IOException ex) {
            throw new IllegalArgumentException(ex);
        }
    }


    public String getFileContentURL() {
        return get(JSON_URL).asString();
    }
}
