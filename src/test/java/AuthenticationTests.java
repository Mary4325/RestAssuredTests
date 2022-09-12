import Pojo.Login;
import config.EaswaaqConnectionConfig;
import config.UserServiceEndpoints;
import config.category_markers.FullRegressTests;
import config.category_markers.ServicesUpCheckTests;
import config.category_markers.SmokeTests;
import io.restassured.http.ContentType;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import static io.restassured.RestAssured.*;

public class AuthenticationTests extends EaswaaqConnectionConfig {
    static String loginOperator = "+209609514599";
    static String passwordOperator = "134509";
    static String profileTypeOperator =  "OPERATOR";
    static String loginMerchant = "+2033970008";
    static String passwordMerchant = "Qwe!2345";
    static String profileTypeMerchant =  "LEGAL_SELLER";
    static String loginBuyer = "+20223652567";
    static String passwordBuyer = "Qwe!2345";
    static String profileTypeBuyer =  "COMMON_BUYER";

    @Category({FullRegressTests.class, SmokeTests.class, ServicesUpCheckTests.class})
    @Test
    public void getOperatorToken() {
        Login loginInfo = new Login(loginOperator, passwordOperator, profileTypeOperator);
        String token =
                given()
                        .contentType(ContentType.JSON)
                        .body(loginInfo)
                        .post(UserServiceEndpoints.JWT_TOKEN)
                        .then()
                        .statusCode(200)
                        .extract()
                        .response().path("value.token").toString();
        Assert.assertTrue(token !=null);
        System.out.println("Token is: " + token);
    }

    @Category({FullRegressTests.class, SmokeTests.class})
    @Test
    public void getMerchantToken() {
        Login loginInfo = new Login(loginMerchant, passwordMerchant, profileTypeMerchant);
        String token =
                given()
                        .contentType(ContentType.JSON)
                        .body(loginInfo)
                        .post(UserServiceEndpoints.JWT_TOKEN)
                        .then()
                        .statusCode(200)
                        .extract()
                        .response().path("value.token").toString();
        Assert.assertTrue(token !=null);
        System.out.println("Token is: " + token);
    }

    @Category({FullRegressTests.class, SmokeTests.class})
    @Test
    public void getBuyerToken() {
        Login loginInfo = new Login(loginBuyer, passwordBuyer, profileTypeBuyer);
        String token =
                given()
                        .contentType(ContentType.JSON)
                        .body(loginInfo)
                        .post(UserServiceEndpoints.JWT_TOKEN)
                        .then()
                        .statusCode(200)
                        .extract()
                        .response().path("value.token").toString();
        Assert.assertTrue(token !=null);
        System.out.println("Token is: " + token);
    }
}
