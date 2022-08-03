import config.EaswaaqTestConfig;
import config.LogisticsServiceEndpoints;
import config.OrderServiceEndpoints;
import config.UserServiceEndpoints;
import io.restassured.http.ContentType;
import org.junit.BeforeClass;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class LogisticsServiceTests extends EaswaaqTestConfig  {
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
    public void getSellerDeliveryMethodsTest() {
        given().
                header("Authorization", "Bearer " + token).
                queryParams("sellerId", 1785).
                get(LogisticsServiceEndpoints.DELIVERYMETHODS).
                then().statusCode(200).log().all().
                body("value [0].selected", equalTo(true)).
                body("value [0].providerName", equalTo("ARAMEX")).
                body("value [1].selected", equalTo(false)).
                body("value [1].providerName", equalTo("DBS"));
    }
}
