import config.ArticleServiceEndpoints;
import config.CmsServiceEndpoints;
import config.EaswaaqConnectionConfig;
import config.UserServiceEndpoints;
import config.category_markers.FullRegressTests;
import config.category_markers.ServicesUpCheckTests;
import io.restassured.http.ContentType;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.io.File;
import java.util.Random;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class CmsServiceTests extends EaswaaqConnectionConfig {
    static String token;
    static int randomInt;

    public String metaDescription = "meta description meta description meta description meta description meta description meta description" +
            " meta description meta description meta description meta description meta description meta description meta description";

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

    @Category({FullRegressTests.class, ServicesUpCheckTests.class})
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



    @Category({FullRegressTests.class})
    @Test
    public void createUpdateBlockElementTest() {
        String newBlockBodyJson = """
                {"sortOrder":1,"pageLineBlockId":1353,"objectId":null,
                "staticBlock":true,"title":"%s",
                "metaUrl":{"name":"%s","url":"%s"},
                "metaImage":null,"additionalFields":{},
                "metaTitle":"%s", "metaDescription":"%s", "lang":"EN"}""".formatted("element " + randomInt,
                "element " + randomInt, "element " + randomInt, "element " + randomInt, "element " + randomInt);

        int value = given().
                header("Authorization", "Bearer " + token).
                body(newBlockBodyJson).
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

        given().
                header("Authorization", "Bearer " + token).
                pathParam("elementId", value).
                when().
                delete(CmsServiceEndpoints.ELEMENT).
                then().statusCode(200).log().all();
    }

    @Category({FullRegressTests.class})
    @Test
    public void createLineTest() {
        String lineBodyJson = """
                {"sizeId":2,"type":"USER","pageContentId":1,"sortOrder":15,
                "backgroundCss":{"bgColor":{"red":233,"green":249,"blue":220,"alpha":1}},
                "status":"PUBLISHED","title":"AutotestLine"}""";
        int lineId = given().
                header("Authorization", "Bearer " + token).
                body(lineBodyJson).
                when().
                post(CmsServiceEndpoints.LINES).
                then().statusCode(200).log().all().
                body("success", equalTo(true)).
                extract().
                response().path("value");
        Assert.assertTrue(lineId > 0);

        String blockJson = """
                {"pageContentLineId":%s,"block":{"regionIds":null,"id":null,"parentId":null,"staticBlock":true,"blockTypeId":3,"title":"FeaturedCategoryFreshList",
                "metaUrl":{"name":"FeaturedCategoryFreshList","url":"www.google.com"},"elementTemplateId":null,"elementType":"CUSTOM","additionalFields":{},
            "templateId":33,"lgCol":12,"mdCol":12,"smCol":12,"xsCol":12,"sortOrder":2,"lang":"EN",
                "metaTitle":"FeaturedCategoryFreshList meta title",
                "metaDescription":"FeaturedCategoryFreshList meta description","parentBlock":true},"lang":"EN"}""".formatted(lineId);
        int parentBlockId = given().
                header("Authorization", "Bearer " + token).
                body(blockJson).
                when().
                post(CmsServiceEndpoints.BLOCKS).
                then().statusCode(200).log().all().
                body("success", equalTo(true)).
                extract().
                response().path("value");
        Assert.assertTrue(parentBlockId > 0);

        String blockBodyJson = """
                {"pageContentLineId":%s,"block":{"regionIds":null,"id":null,"parentId":%s,"staticBlock":true,"blockTypeId":3,"title":"FeaturedCategoryFreshList",
                "metaUrl":{"name":"FeaturedCategoryFreshList","url":"www.google.com"},"elementTemplateId":null,"elementType":"CUSTOM","additionalFields":{},
                "templateId":33,"lgCol":12,"mdCol":12,"smCol":12,"xsCol":12,"sortOrder":1,"lang":"EN",
                "metaTitle":"FeaturedCategoryFreshList meta title","metaDescription":"FeaturedCategoryFreshList meta description"},"lang":"EN"}""".formatted(lineId, parentBlockId);
        int blockId = given().
                header("Authorization", "Bearer " + token).
                body(blockBodyJson).
                when().
                post(CmsServiceEndpoints.BLOCKS).
                then().statusCode(200).log().all().
                body("success", equalTo(true)).
                extract().
                response().path("value");
        Assert.assertTrue(blockId > 0);





//        String blockBodyJson = """
//                {"pageContentLineId":%s,"block":{"regionIds":null,"id":null,"parentId":null,"staticBlock":true,"blockTypeId":4,"title":"TwoColumnInfo",
//                "metaUrl":{"name":"TwoColumnInfo meta url","url":"www.google.com"},"elementTemplateId":null,"elementType":"CUSTOM","additionalFields":{},
//                "templateId":17,"lgCol":12,"mdCol":12,"smCol":12,"xsCol":12,
//                "sortOrder":0,"lang":"EN","metaTitle":"TwoColumnInfo meta title",
//                "metaDescription": %s,"parentBlock":true},"lang":"EN"}""".formatted(lineId, metaDescription);
//        int blockId = given().
//                header("Authorization", "Bearer " + token).
//                body(blockBodyJson).
//                when().
//                post(CmsServiceEndpoints.BLOCKS).
//                then().statusCode(200).log().all().
//                body("success", equalTo(true)).
//                extract().
//                response().path("value");
//        Assert.assertTrue(blockId > 0);



    }
}
