import Pojo.Login;
import config.EaswaaqConnectionConfig;
import config.LogisticsServiceEndpoints;
import config.UserServiceEndpoints;
import config.category_markers.FullRegressTests;
import config.category_markers.SmokeTests;
import io.restassured.http.ContentType;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class LogisticsServiceTests extends EaswaaqConnectionConfig {
    static String token;
    static String loginOperator = "+209609514599";
    static String passwordOperator = "134509";
    static String profileTypeOperator =  "OPERATOR";
    static int sellerId = 1785;

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

    @Category({FullRegressTests.class, SmokeTests.class})
    @Test
    public void getSellerDeliveryMethodsTest() {
        given().
                header("Authorization", "Bearer " + token).
                queryParams("sellerId", sellerId).
                get(LogisticsServiceEndpoints.DELIVERY_METHODS).
                then().statusCode(200).log().all().
                body("value [0].selected", equalTo(true)).
                body("value [0].providerName", equalTo("ARAMEX")).
                body("value [1].selected", equalTo(false)).
                body("value [1].providerName", equalTo("DBS"));
    }
}
