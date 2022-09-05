package utils;
import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class AppHelper {

    public AppHelper() {
    }

    public boolean compareJson(String s1, String s2) {
        return JsonUtil.compareJson(s1, s2);
    }

    public String readFromFile(String filePath) {

        return FileUtil.readFromFile(filePath);
    }

    public String getJsonFieldValue(String json, String nodePath) {
        Object o = JsonUtil.getJsonFieldValue(json, nodePath);
        return o != null ? o.toString() : null;
    }
}
