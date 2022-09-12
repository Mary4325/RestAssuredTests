import Pojo.Login;
import config.EaswaaqConnectionConfig;
import config.EventsLogServiceEndpoints;
import config.OrderServiceEndpoints;
import config.UserServiceEndpoints;
import config.category_markers.FullRegressTests;
import config.category_markers.ServicesUpCheckTests;
import config.category_markers.SmokeTests;
import io.restassured.http.ContentType;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class EventsLogServiceTests extends EaswaaqConnectionConfig {
    static String token;
    static String loginOperator = "+209609514599";
    static String passwordOperator = "134509";
    static String profileTypeOperator =  "OPERATOR";

    @BeforeClass
    public static void getToken() {
        Login loginInfo = new Login(loginOperator, passwordOperator, profileTypeOperator);
        token =
                given()
                        .contentType(ContentType.JSON)
                        .body(loginInfo)
                        .post(UserServiceEndpoints.JWT_TOKEN)
                        .then()
                        .statusCode(200)
                        .extract()
                        .response().path("value.token").toString();
    }

    @Category({FullRegressTests.class, SmokeTests.class, ServicesUpCheckTests.class})
    @Test
    public void getAuditTest() {
        Integer totalCount = given().
                queryParams("operators", true).
                queryParams("users", true).
                queryParams("page.num", 1).
                queryParams("page.size", 10).
                header("Authorization", "Bearer " + token).
                get(EventsLogServiceEndpoints.AUDIT).
                then().statusCode(200).log().all().
                body("success", equalTo(true)).
                extract().
                response().path("value.totalCount");
        Assert.assertTrue(totalCount > 270000);
    }
}
