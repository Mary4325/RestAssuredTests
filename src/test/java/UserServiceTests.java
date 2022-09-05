import Pojo.Login;
import config.EaswaaqConnectionConfig;
import config.UserServiceEndpoints;
import config.category_markers.FullRegressTests;
import config.category_markers.SmokeTests;
import io.restassured.http.ContentType;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;

public class UserServiceTests extends EaswaaqConnectionConfig {
    static String token;
    static String loginOperator = "+209609514599";
    static String passwordOperator = "134509";
    static String profileTypeOperator =  "OPERATOR";
    static int buyerId = 1859;
    static int addressId = 852;

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

    @Category({FullRegressTests.class, SmokeTests.class})
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

    @Category({FullRegressTests.class, SmokeTests.class})
    @Test
    public void getBuyerDeliveryAddressTest()  {
        given().
                pathParam("addressId", addressId).
                formParam("buyerId", buyerId).
                header("Authorization", "Bearer " + token).
                get(UserServiceEndpoints.DELIVERY_ADDRESS).
                then().statusCode(200).log().all().
                body("value.title", equalTo("MyAddress"));
    }
}