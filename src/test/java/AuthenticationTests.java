import config.EaswaaqTestConfig;
import config.UserServiceEndpoints;
import io.restassured.http.ContentType;
import org.junit.Assert;
import org.junit.Test;
import static io.restassured.RestAssured.*;

public class AuthenticationTests extends EaswaaqTestConfig{

    @Test
    public void getOperatorToken() {
        String operatorCreadentialsJson = """
                {"login": "+209609514599", 
                "password": "134509",
                "profileType": "OPERATOR"}""";
        String token =
                given()
                        .contentType(ContentType.JSON)
                        .body(operatorCreadentialsJson)
                        .post(UserServiceEndpoints.JWT_TOKEN)
                        .then()
                        .statusCode(200)
                        .extract()
                        .response().path("value.token").toString();
        Assert.assertTrue(token !=null);
        System.out.println("Token is: " + token);
    }

    @Test
    public void getMerchantToken() {
        String merchantCreadentialsJson = """
                {"login": "+2033970008", 
                "password": "Qwe!2345",
                "profileType": "LEGAL_SELLER"}""";
        String token =
                given()
                        .contentType(ContentType.JSON)
                        .body(merchantCreadentialsJson)
                        .post(UserServiceEndpoints.JWT_TOKEN)
                        .then()
                        .statusCode(200)
                        .extract()
                        .response().path("value.token").toString();
        Assert.assertTrue(token !=null);
        System.out.println("Token is: " + token);
    }

    @Test
    public void getBuyerToken() {
        String buyerCreadentialsJson = """
                {"login": "+20223651235", 
                "password": "Qwe!2345",
                "profileType": "COMMON_BUYER"}""";
        String token =
                given()
                        .contentType(ContentType.JSON)
                        .body(buyerCreadentialsJson)
                        .post(UserServiceEndpoints.JWT_TOKEN)
                        .then()
                        .statusCode(200)
                        .extract()
                        .response().path("value.token").toString();
        Assert.assertTrue(token !=null);
        System.out.println("Token is: " + token);
    }
}
