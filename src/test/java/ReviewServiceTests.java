import Pojo.Login;
import config.EaswaaqConnectionConfig;
import config.ReviewServiceEndpoints;
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

public class ReviewServiceTests extends EaswaaqConnectionConfig {
    static String token;
    static String loginOperator = "+209609514599";
    static String passwordOperator = "134509";
    static String profileTypeOperator =  "OPERATOR";
    static int reviewId = 16;

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

    @Category({FullRegressTests.class, SmokeTests.class, ServicesUpCheckTests.class})
    @Test
    public void getReviewTest() {
        given().
                pathParam("reviewId", reviewId).
                header("Authorization", "Bearer " + token).
                get(ReviewServiceEndpoints.REVIEWS).
                then().statusCode(200).log().all().
                body("value.id", equalTo(reviewId)).
                body("value.securityDynamicInfo.orderId", equalTo(349));
    }
}
