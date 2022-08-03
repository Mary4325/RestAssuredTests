import config.EaswaaqTestConfig;
import config.ItemServiceEndpoints;
import config.OrderServiceEndpoints;
import config.UserServiceEndpoints;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.http.Method.GET;
import static org.hamcrest.Matchers.equalTo;

public class OrderServiceTests extends EaswaaqTestConfig  {
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
    public void getOrderTest() {
        given().
                pathParam("orderId", 409).
                header("Authorization", "Bearer " + token).
                get(OrderServiceEndpoints.ORDERS).
                then().log().all().
                body("value.id", equalTo(409)).
                body("value.unitedOrderId", equalTo(380));
    }

    @Test
    public void getMerchantPaymentMethodsTest() {
        given().
                header("Authorization", "Bearer " + token).
                queryParams("sellerId", 1785).
                get(OrderServiceEndpoints.PAYMENTMETHODS).
                then().statusCode(200).log().all().
                body("value [0].selected", equalTo(true)).
                body("value [0].paymentMethod", equalTo("CASH")).
                body("value [1].selected", equalTo(true)).
                body("value [1].paymentMethod", equalTo("CARDS"));
    }
}
