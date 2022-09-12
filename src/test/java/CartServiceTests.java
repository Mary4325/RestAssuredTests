import Pojo.Login;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.CartServiceEndpoints;
import config.EaswaaqConnectionConfig;
import config.UserServiceEndpoints;
import config.category_markers.FullRegressTests;
import config.category_markers.ServicesUpCheckTests;
import config.category_markers.SmokeTests;
import io.restassured.http.ContentType;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import utils.AppHelper;
import utils.FileUtil;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.restassured.RestAssured.given;

import static org.hamcrest.Matchers.*;

public class CartServiceTests extends EaswaaqConnectionConfig {
    static String token;
    static int buyerId = 1859;
    static int item1 = 83632;
    static int item2 = 83633;
    static int skuId1 = 111883;
    static int skuId2 = 111885;
    static int sellerId = 1785;
    static String loginBuyer = "+20223652567";
    static String passwordBuyer = "Qwe!2345";
    static String profileTypeBuyer =  "COMMON_BUYER";
    static int randomInt;
    static String firstName =  "Autotest";
    private AppHelper helper;

    @BeforeClass
    public static void getToken() {
        Login loginInfo = new Login(loginBuyer, passwordBuyer, profileTypeBuyer);
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

    @BeforeClass
    public static void random(){
        Random randomGenerator = new Random();
        randomInt = randomGenerator.nextInt(1000); // get random number in the range of 0-1000
    }

    @Before
    public void initHelper() {
        helper = new AppHelper();
    }

    @Category({FullRegressTests.class, SmokeTests.class})
    @Test
    public void createGetClearWishlistTest() {
        String wishBodyJson = """
                {"buyerId": %s,
                "itemIds": [%s, %s]}""".formatted(buyerId, item1, item2);

        given().
                header("Authorization", "Bearer " + token).
                body(wishBodyJson).
                when().
                post(CartServiceEndpoints.WISH).
                then().statusCode(200).log().all().
                body("success", equalTo(true));

        given().
                formParam("buyerId", buyerId).
                header("Authorization", "Bearer " + token).
                get(CartServiceEndpoints.WISH).
                then().statusCode(200).log().all().
                body("value.itemIds", hasItems(item1, item2));

        String wishDeleteBodyJson = """
                {"buyerId": %s,
                "itemIds": [%s, %s],
                "removeAll": true}""".formatted(buyerId, item1, item2);

        given().
                header("Authorization", "Bearer " + token).
                body(wishDeleteBodyJson).
                when().
                delete(CartServiceEndpoints.WISH).
                then().statusCode(200).log().all();
    }

    @Category({FullRegressTests.class, SmokeTests.class})
    @Test
    public void addGetDeleteItemsToCartTest() {
        String cartBodyJson = """    
                {"buyerId": %s, "verifiedBuyer": true,
                "items": [{"skuId": %s,"count": 1},
                {"skuId": %s,"count": 2}]}""".formatted(buyerId, skuId1, skuId2);

        given().
                header("Authorization", "Bearer " + token).
                body(cartBodyJson).
                when().
                post(CartServiceEndpoints.UPDATE_CART).
                then().statusCode(200).log().all().
                body("value.buyerId", equalTo(buyerId));

        given().
                formParam("buyerId", buyerId).
                header("Authorization", "Bearer " + token).
                get(CartServiceEndpoints.CART).
                then().statusCode(200).log().all();
//               .rootPath("value.sellers.1785.sku").
//                body("", hasItems(hasEntry("skuId", 111883)), hasEntry("skuId", 111885));
//                body("value.buyerId", equalTo(buyerId)).
//                body("value.sellers.1785.sku", contains(111883, 111885)).extract().

        String emptyCartBodyJson = """
        {"buyerId": %s, "items": 
        [{"skuId": %s,"sellerId": %s}, {"skuId": %s,"sellerId": %s}],
        "removeAll": true}""".formatted(buyerId, skuId1, sellerId, skuId2, sellerId);

        given().
                header("Authorization", "Bearer " + token).
                body(emptyCartBodyJson).
                when().
                delete(CartServiceEndpoints.CART).
                then().statusCode(200).log().all();
             //   body("value.sellers.", equalTo("<{}>"));
    }

    @Category({FullRegressTests.class, SmokeTests.class})
    @Test
    public void CreateOrderTest() throws IOException {
        String cartBodyJson = """    
                {"buyerId": %s, "verifiedBuyer": true,
                "items": [{"skuId": %s,"count": 3}]}""".formatted(buyerId, skuId1);

        given().
                header("Authorization", "Bearer " + token).
                body(cartBodyJson).
                when().
                post(CartServiceEndpoints.UPDATE_CART).
                then().statusCode(200).log().all().
                body("value.buyerId", equalTo(buyerId));

        String contactPersonBodyJson = """
                {"buyerId": %s,
                  "contactPerson":
                    {"fullName": {"firstName": "Autotest", "lastName": "User"},
                    "phoneNumber": { "countryCode": 20,  "nationalNumber": 223652567,
                    "italianLeadingZero": false },
                    "email": "testuser939@gmail.com",
                    "recipientNotBuyer": false,
                    "allowEmailNotification": true,
                    "allowPhoneNotification": true }}""".formatted(buyerId);
        given().
                header("Authorization", "Bearer " + token).
                body(contactPersonBodyJson).
                when().
                put(CartServiceEndpoints.CONTACT_PERSON).
                then().statusCode(200).log().all().
                body("value.contactPerson.fullName.firstName", equalTo(firstName));

        String paymentMethodBodyJson = """
                {"paymentType": "CASH", "paymentPlanId": 4,
                "buyerId": %s }""".formatted(buyerId);

        given().
                header("Authorization", "Bearer " + token).
                body(paymentMethodBodyJson).
                when().
                put(CartServiceEndpoints.PAYMENT_METHOD).
                then().statusCode(200).log().all().
                body("value.payment.paymentMethod", equalTo("CASH"));

        String commentBodyJson = """
                {"sellerId": %s,
                  "buyerComment": "TestComment",
                  "systemComment": { "agreeToReplace": true },
                  "buyerId": %s}""".formatted(sellerId, buyerId);
        given().
                header("Authorization", "Bearer " + token).
                body(commentBodyJson).
                when().
                put(CartServiceEndpoints.UPDATE_COMMENT).
                then().statusCode(200).log().all();
       //         .body("value.sellers.buyerComment", equalTo("TestComment"));

        String addressBodyJson = helper.readFromFile("src/main/java/RequestBody/deliveryAddress.json");
        given().
                header("Authorization", "Bearer " + token).
                body(addressBodyJson).
                when().
                put(CartServiceEndpoints.DELIVERY_COURIER).
                then().statusCode(200).log().all();

        String checkoutBodyJson = """
                {"buyerId": %s,"buyerType": "COMMON_BUYER",
                "sellerId": %s,"currency": "EGP",
                "selectedSku": [%s]}""".formatted(buyerId, sellerId, skuId1);

        given().
                header("Authorization", "Bearer " + token).
                body(checkoutBodyJson).
                when().
                post(CartServiceEndpoints.CHECKOUT).
                then().statusCode(200).log().all();
    }

    @Category({ServicesUpCheckTests.class})
    @Test
    public void getCartTest() {
        given().
                formParam("buyerId", buyerId).
                header("Authorization", "Bearer " + token).
                get(CartServiceEndpoints.CART).
                then().statusCode(200).log().all();
//                .body("value.sellers", equalTo(sellerId));
    }

    @Test
    public void updateDeliveryCourierTest() {
        String addressBodyJson = helper.readFromFile("src/main/java/RequestBody/deliveryAddress.json");
        given().
                header("Authorization", "Bearer " + token).
                body(addressBodyJson).
                when().
                put(CartServiceEndpoints.DELIVERY_COURIER).
                then().statusCode(200).log().all();
    }
}
