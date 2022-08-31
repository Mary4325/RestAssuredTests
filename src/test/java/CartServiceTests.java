import config.ArticleServiceEndpoints;
import config.CartServiceEndpoints;
import config.EaswaaqTestConfig;
import config.UserServiceEndpoints;
import config.category_markers.FullRegressTests;
import config.category_markers.SmokeTests;
import io.restassured.http.ContentType;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.io.File;
import java.util.Random;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class CartServiceTests extends EaswaaqTestConfig {
    static String token;

    static int buyerId = 1859;
    static int randomInt;

    @BeforeClass
    public static void getToken() {
        String operatorCreadentialsJson = """
                {"login": "+20223652567", 
                "password": "Qwe!2345",
                "profileType": "COMMON_BUYER"}""";
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
                "itemIds": [83633, 83632]}""".formatted(buyerId);

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
                body("value.itemIds", hasItems(83633, 83632));

        given().
                header("Authorization", "Bearer " + token).
                formParam("buyerId", buyerId).
                when().
                delete(CartServiceEndpoints.WISH).
                then().statusCode(200).log().all();
    }
}
