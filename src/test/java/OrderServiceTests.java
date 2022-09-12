import Pojo.Login;
import config.EaswaaqConnectionConfig;
import config.OrderServiceEndpoints;
import config.UserServiceEndpoints;
import config.category_markers.FullRegressTests;
import config.category_markers.ServicesUpCheckTests;
import config.category_markers.SmokeTests;
import io.restassured.http.ContentType;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class OrderServiceTests extends EaswaaqConnectionConfig {
    static String token;
    static String loginOperator = "+209609514599";
    static String passwordOperator = "134509";
    static String profileTypeOperator =  "OPERATOR";

    static int sellerId = 1785;
    static int orderId = 1099;
    static int unitedOrderId= 1063;

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
    @Category({FullRegressTests.class, ServicesUpCheckTests.class})
    @Test
    public void getDisputeTest() {
        given().
                queryParams("id", 40).
                queryParams("page.num", 1).
                queryParams("page.size", 10).
                header("Authorization", "Bearer " + token).
                get(OrderServiceEndpoints.DISPUTES).
                then().log().all().
                body("value.data [0].orderId", equalTo(orderId)).
                body("value.data [0].disputeReasonTitle", equalTo("The Ordered Item Never Arrived"));
    }

    @Category({FullRegressTests.class, SmokeTests.class})
    @Test
    public void getOrderTest() {
        given().
                pathParams("orderId", orderId).
                header("Authorization", "Bearer " + token).
                get(OrderServiceEndpoints.ORDERS).
                then().log().all().
                body("value.id", equalTo(orderId)).
                body("value.unitedOrderId", equalTo(unitedOrderId));
    }

    @Category({FullRegressTests.class, SmokeTests.class})
    @Test
    public void getMerchantPaymentMethodsTest() {
        given().
                header("Authorization", "Bearer " + token).
                queryParams("sellerId", sellerId).
                get(OrderServiceEndpoints.PAYMENT_METHODS).
                then().statusCode(200).log().all().
                body("value [0].selected", equalTo(true)).
                body("value [0].paymentMethod", equalTo("CASH")).
                body("value [1].selected", equalTo(true)).
                body("value [1].paymentMethod", equalTo("CARDS"));
    }
}
