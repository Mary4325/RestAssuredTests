import config.EaswaaqTestConfig;
import config.ItemServiceEndpoints;
import config.UserServiceEndpoints;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import static io.restassured.RestAssured.*;
import static io.restassured.http.Method.GET;
import static org.hamcrest.Matchers.equalTo;

public class UserServiceTests extends EaswaaqTestConfig {
    static String token;

    @BeforeClass
    public static void getToken() {
        String operatorCreadentialsJson = """
                {"login": "+209609514599", 
                "password": "134509",
                "profileType": "OPERATOR"}""";
        token =
                given()
                        .contentType(ContentType.JSON)
                        .body(operatorCreadentialsJson)
                        .post(UserServiceEndpoints.JWT_TOKEN)
                        .then()
                        .statusCode(200)
                        .extract()
                        .response().path("value.token").toString();
    }

    @Test
    public void getBuyerTest() {
        given().
                pathParam("userId", 1218).
                header("Authorization", "Bearer " + token).
                queryParams("includeAdditionalFields", "true").
                get(UserServiceEndpoints.USERS).
                then().statusCode(200).log().all().
                body("value.id", equalTo(1218)).
                body("value.login", equalTo("+20222333444")).
                body("value.email", equalTo("test@gmail.com"));
    }

    @Test
    public void getCompanyTest()  {
        given().
                pathParam("companyId", 227).
                header("Authorization", "Bearer " + token).
                get(UserServiceEndpoints.COMPANIES).
                then().statusCode(200).log().all().
                body("value.company.id", equalTo(227)).
                body("value.company.shortName", equalTo("Autotest Company"));
    }
}