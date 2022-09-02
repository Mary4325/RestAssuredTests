import Pojo.Login;
import config.CartServiceEndpoints;
import config.EaswaaqConnectionConfig;
import config.UserServiceEndpoints;
import config.category_markers.FullRegressTests;
import config.category_markers.SmokeTests;
import io.restassured.http.ContentType;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.Random;

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

    @Category({FullRegressTests.class, SmokeTests.class})
    @Test
    public void createGetDeleteWishlistTest() {
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
    public void AddProductsToCartTest() {
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
                then().statusCode(200).log().all().
                rootPath("value.sellers.1785.sku").
                body("", hasItems(hasEntry("skuId", 111883)), hasEntry("skuId", 111885));
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

}
