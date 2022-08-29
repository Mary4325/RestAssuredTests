import config.ArticleServiceEndpoints;
import config.CmsServiceEndpoints;
import config.EaswaaqTestConfig;
import config.UserServiceEndpoints;
import io.restassured.http.ContentType;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.util.Random;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class CmsServiceTests extends EaswaaqTestConfig {
    static String token;
    static int randomInt;

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

    @BeforeClass
    public static void random(){
        Random randomGenerator = new Random();
        randomInt = randomGenerator.nextInt(500); // get random number in the range of 0-500
    }

    @Test
    public void getElementTest() {
        given().
                pathParam("articleId", 8).
                header("Authorization", "Bearer " + token).
                get(ArticleServiceEndpoints.ARTICLE).
                then().statusCode(200).log().all().
                body("value.id", equalTo(8)).
                body("value.title", equalTo("Keyboards"));
    }

    @Test
    public void createUpdateBlockElementTest() {
        String articleBodyJson = """
                {"sortOrder":1,"pageLineBlockId":1353,"objectId":null,
                "staticBlock":true,"title":"%s",
                "metaUrl":{"name":"%s","url":"%s"},
                "metaImage":null,"additionalFields":{},
                "metaTitle":"%s", "metaDescription":"%s", "lang":"EN"}""".formatted("element " + randomInt,
                "element " + randomInt, "element " + randomInt, "element " + randomInt, "element " + randomInt);

        int value = given().
                header("Authorization", "Bearer " + token).
                body(articleBodyJson).
                when().
                post(CmsServiceEndpoints.ELEMENTS).
                then().statusCode(200).log().all().
                body("success", equalTo(true)).
                extract().
                response().path("value");
        Assert.assertTrue(value > 0);

        File testUploadFile = new File("D:\\Users\\a.gordeeva\\Desktop\\Work\\TestPictures\\corgi.jpg");
        given().
                header("Authorization", "Bearer " + token).
                contentType("multipart/form-data").
                pathParam("elementId", value).
                multiPart("image", testUploadFile).
                when().
                patch(CmsServiceEndpoints.ELEMENT_IMAGE).
                then().statusCode(200).log().all().
                body("success", equalTo(true));

//        given().
//                header("Authorization", "Bearer " + token).
//                pathParam("elementId", value).
//                when().
//                delete(CmsServiceEndpoints.ELEMENT).
//                then().statusCode(200).log().all();
    }
}
