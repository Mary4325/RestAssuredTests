import config.EaswaaqTestConfig;
import config.ReviewServiceEndpoints;
import config.UserServiceEndpoints;
import io.restassured.http.ContentType;
import org.junit.BeforeClass;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class ReviewServiceTests extends EaswaaqTestConfig {
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
    public void getReviewTest() {
        given().
                pathParam("reviewId", 16).
                header("Authorization", "Bearer " + token).
                get(ReviewServiceEndpoints.REVIEWS).
                then().statusCode(200).log().all().
                body("value.id", equalTo(16)).
                body("value.securityDynamicInfo.orderId", equalTo(349));
    }
}
