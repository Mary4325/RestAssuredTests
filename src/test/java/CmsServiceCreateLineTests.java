import config.ArticleServiceEndpoints;
import config.CmsServiceEndpoints;
import config.EaswaaqConnectionConfig;
import config.UserServiceEndpoints;
import config.category_markers.FullRegressTests;
import io.restassured.http.ContentType;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.io.File;
import java.util.Random;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class CmsServiceCreateLineTests extends EaswaaqConnectionConfig {
    static String token;
    static int randomInt;
    static int lineId;

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
    public static void random() {
        Random randomGenerator = new Random();
        randomInt = randomGenerator.nextInt(1000); // get random number in the range of 0-1000
    }

    @BeforeClass
    public static void createLine() {
        String lineBodyJson = """
                {"sizeId":2,"type":"USER","pageContentId":1,"sortOrder":15,
                "backgroundCss":{"bgColor":{"red":233,"green":249,"blue":220,"alpha":1}},
                "status":"PUBLISHED","title":"AutotestLine"}""";
        lineId = given().
                header("Authorization", "Bearer " + token).
                body(lineBodyJson).
                when().
                post(CmsServiceEndpoints.LINES).
                then().statusCode(200).log().all().
                body("success", equalTo(true)).
                extract().response().path("value");
        Assert.assertTrue(lineId > 0);
    }


    @Category({FullRegressTests.class})
    @Test
    public void addCustomBlockFeaturedCategoryFreshListTest() {
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

        for (int i = 0; i <= 5; i++) {

            String elementBodyJson = """
                    {"sortOrder":1,"pageLineBlockId":%s,"objectId":null,
                    "staticBlock":true,"title":"%s",
                    "metaUrl":{"name":"%s","url":"%s"},
                    "metaImage":null,"additionalFields":{},
                    "metaTitle":"%s", "metaDescription":"%s", "lang":"EN"}""".formatted(blockId, "element " + randomInt,
                    "element " + randomInt, "element " + randomInt, "element " + randomInt, "element " + randomInt);

            int elementId = given().
                    header("Authorization", "Bearer " + token).
                    body(elementBodyJson).
                    when().
                    post(CmsServiceEndpoints.ELEMENTS).
                    then().statusCode(200).log().all().
                    body("success", equalTo(true)).
                    extract().
                    response().path("value");
            Assert.assertTrue(elementId > 0);

            File testUploadFile = new File("D:\\Users\\a.gordeeva\\Desktop\\Work\\TestPictures\\corgi.jpg");
            given().
                    header("Authorization", "Bearer " + token).
                    contentType("multipart/form-data").
                    pathParam("elementId", elementId).
                    multiPart("image", testUploadFile).
                    when().
                    patch(CmsServiceEndpoints.ELEMENT_IMAGE).
                    then().statusCode(200).log().all().
                    body("success", equalTo(true));
            randomInt += 1;
        }
    }

    @Category({FullRegressTests.class})
    @Test
    public void addCustomBlockFeaturedCategoryListTest() {
        String blockJson = """
                {"pageContentLineId":%s,"block":{"regionIds":null,"id":null,"parentId":null,"staticBlock":true,"blockTypeId":3,"title":"FeaturedCategoryList",
                "metaUrl":{"name":"FeaturedCategoryList","url":"www.google.com"},"elementTemplateId":null,"elementType":"CUSTOM","additionalFields":{},
                "templateId":14,"lgCol":12,"mdCol":12,"smCol":12,"xsCol":12,"sortOrder":2,"lang":"EN",
                "metaTitle":"FeaturedCategoryList meta title",
                "metaDescription":"FeaturedCategoryList meta description","parentBlock":true},"lang":"EN"}""".formatted(lineId);
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
                {"pageContentLineId":%s,"block":{"regionIds":null,"id":null,"parentId":%s,"staticBlock":true,"blockTypeId":3,"title":"FeaturedCategoryList",
                "metaUrl":{"name":"FeaturedCategoryList","url":"www.google.com"},"elementTemplateId":null,"elementType":"CUSTOM","additionalFields":{},
                "templateId":14,"lgCol":12,"mdCol":12,"smCol":12,"xsCol":12,"sortOrder":1,"lang":"EN",
                "metaTitle":"FeaturedCategoryList meta title","metaDescription":"FeaturedCategoryList meta description"},"lang":"EN"}""".formatted(lineId, parentBlockId);
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

        for (int i = 0; i <= 5; i++) {

            String elementBodyJson = """
                    {"sortOrder":1,"pageLineBlockId":%s,"objectId":null,
                    "staticBlock":true,"title":"%s",
                    "metaUrl":{"name":"%s","url":"%s"},
                    "metaImage":null,"additionalFields":{},
                    "metaTitle":"%s", "metaDescription":"%s", "lang":"EN"}""".formatted(blockId, "element " + randomInt,
                    "element " + randomInt, "element " + randomInt, "element " + randomInt, "element " + randomInt);

            int elementId = given().
                    header("Authorization", "Bearer " + token).
                    body(elementBodyJson).
                    when().
                    post(CmsServiceEndpoints.ELEMENTS).
                    then().statusCode(200).log().all().
                    body("success", equalTo(true)).
                    extract().
                    response().path("value");
            Assert.assertTrue(elementId > 0);

            File testUploadFile = new File("D:\\Users\\a.gordeeva\\Desktop\\Work\\TestPictures\\corgi.jpg");
            given().
                    header("Authorization", "Bearer " + token).
                    contentType("multipart/form-data").
                    pathParam("elementId", elementId).
                    multiPart("image", testUploadFile).
                    when().
                    patch(CmsServiceEndpoints.ELEMENT_IMAGE).
                    then().statusCode(200).log().all().
                    body("success", equalTo(true));
            randomInt += 1;
        }
    }

    @Category({FullRegressTests.class})
    @Test
    public void addCustomBlockAdvantagesHorizontalTest() {
        String blockJson = """
                {"pageContentLineId":%s,"block":{"regionIds":null,"id":null,"parentId":null,"staticBlock":true,"blockTypeId":3,"title":"AdvantagesHorizontal",
                "metaUrl":{"name":"AdvantagesHorizontal","url":"www.google.com"},"elementTemplateId":null,"elementType":"CUSTOM","additionalFields":{},
                "templateId":13,"lgCol":12,"mdCol":12,"smCol":12,"xsCol":12,"sortOrder":2,"lang":"EN",
                "metaTitle":"AdvantagesHorizontal meta title",
                "metaDescription":"AdvantagesHorizontal meta description","parentBlock":true},"lang":"EN"}""".formatted(lineId);
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
                {"pageContentLineId":%s,"block":{"regionIds":null,"id":null,"parentId":%s,"staticBlock":true,"blockTypeId":3,"title":"AdvantagesHorizontal",
                "metaUrl":{"name":"AdvantagesHorizontal","url":"www.google.com"},"elementTemplateId":null,"elementType":"CUSTOM","additionalFields":{},
                "templateId":13,"lgCol":12,"mdCol":12,"smCol":12,"xsCol":12,"sortOrder":1,"lang":"EN",
                "metaTitle":"AdvantagesHorizontal meta title","metaDescription":"AdvantagesHorizontal meta description"},"lang":"EN"}""".formatted(lineId, parentBlockId);
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

        for (int i = 0; i <= 3; i++) {

            String elementBodyJson = """
                    {"sortOrder":1,"pageLineBlockId":%s,"objectId":null,
                    "staticBlock":true,"title":"%s",
                    "metaUrl":{"name":"%s","url":"%s"},
                    "metaImage":null,"additionalFields":{},
                    "metaTitle":"%s", "metaDescription":"%s", "lang":"EN"}""".formatted(blockId, "element " + randomInt,
                    "element " + randomInt, "element " + randomInt, "element " + randomInt, "element " + randomInt);

            int elementId = given().
                    header("Authorization", "Bearer " + token).
                    body(elementBodyJson).
                    when().
                    post(CmsServiceEndpoints.ELEMENTS).
                    then().statusCode(200).log().all().
                    body("success", equalTo(true)).
                    extract().
                    response().path("value");
            Assert.assertTrue(elementId > 0);

            File testUploadFile = new File("D:\\Users\\a.gordeeva\\Desktop\\Work\\TestPictures\\apples.jpg");
            given().
                    header("Authorization", "Bearer " + token).
                    contentType("multipart/form-data").
                    pathParam("elementId", elementId).
                    multiPart("image", testUploadFile).
                    when().
                    patch(CmsServiceEndpoints.ELEMENT_IMAGE).
                    then().statusCode(200).log().all().
                    body("success", equalTo(true));
            randomInt += 1;
        }
    }

    @Category({FullRegressTests.class})
    @Test
    public void addCustomBlockAdvantagesTest() {
        String blockJson = """
                {"pageContentLineId":%s,"block":{"regionIds":null,"id":null,"parentId":null,"staticBlock":true,"blockTypeId":3,"title":"Advantages",
                "metaUrl":{"name":"Advantages","url":"www.google.com"},"elementTemplateId":null,"elementType":"CUSTOM","additionalFields":{},
                "templateId":1,"lgCol":12,"mdCol":12,"smCol":12,"xsCol":12,"sortOrder":2,"lang":"EN",
                "metaTitle":"Advantages meta title", "metaDescription":"Advantages meta description","parentBlock":true},"lang":"EN"}""".formatted(lineId);
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
                {"pageContentLineId":%s,"block":{"regionIds":null,"id":null,"parentId":%s,"staticBlock":true,"blockTypeId":3,"title":"Advantages",
                "metaUrl":{"name":"Advantages","url":"www.google.com"},"elementTemplateId":null,"elementType":"CUSTOM","additionalFields":{},
                "templateId":1,"lgCol":12,"mdCol":12,"smCol":12,"xsCol":12,"sortOrder":1,"lang":"EN",
                "metaTitle":"Advantages meta title","metaDescription":"Advantages meta description"},"lang":"EN"}""".formatted(lineId, parentBlockId);
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

        for (int i = 0; i <= 5; i++) {

            String elementBodyJson = """
                    {"sortOrder":1,"pageLineBlockId":%s,"objectId":null,
                    "staticBlock":true,"title":"%s",
                    "metaUrl":{"name":"%s","url":"%s"},
                    "metaImage":null,"additionalFields":{},
                    "metaTitle":"%s", "metaDescription":"%s", "lang":"EN"}""".formatted(blockId, "element " + randomInt,
                    "element " + randomInt, "element " + randomInt, "element " + randomInt, "element " + randomInt);

            int elementId = given().
                    header("Authorization", "Bearer " + token).
                    body(elementBodyJson).
                    when().
                    post(CmsServiceEndpoints.ELEMENTS).
                    then().statusCode(200).log().all().
                    body("success", equalTo(true)).
                    extract().
                    response().path("value");
            Assert.assertTrue(elementId > 0);

            File testUploadFile = new File("D:\\Users\\a.gordeeva\\Desktop\\Work\\TestPictures\\lemon.jpg");
            given().
                    header("Authorization", "Bearer " + token).
                    contentType("multipart/form-data").
                    pathParam("elementId", elementId).
                    multiPart("image", testUploadFile).
                    when().
                    patch(CmsServiceEndpoints.ELEMENT_IMAGE).
                    then().statusCode(200).log().all().
                    body("success", equalTo(true));
            randomInt += 1;
        }
    }

    @Category({FullRegressTests.class})
    @Test
    public void addCustomBlockPopularBrandsTest() {
        String blockJson = """
                {"pageContentLineId":%s,"block":{"regionIds":null,"id":null,"parentId":null,"staticBlock":true,"blockTypeId":3,"title":"PopularBrands",
                "metaUrl":{"name":"PopularBrands","url":"www.google.com"},"elementTemplateId":null,"elementType":"CUSTOM","additionalFields":{},
                "templateId":21,"lgCol":12,"mdCol":12,"smCol":12,"xsCol":12,"sortOrder":2,"lang":"EN",
                "metaTitle":"PopularBrands meta title", "metaDescription":"PopularBrands meta description","parentBlock":true},"lang":"EN"}""".formatted(lineId);
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
                {"pageContentLineId":%s,"block":{"regionIds":null,"id":null,"parentId":%s,"staticBlock":true,"blockTypeId":3,"title":"PopularBrands",
                "metaUrl":{"name":"PopularBrands","url":"www.google.com"},"elementTemplateId":null,"elementType":"CUSTOM","additionalFields":{},
                "templateId":21,"lgCol":12,"mdCol":12,"smCol":12,"xsCol":12,"sortOrder":1,"lang":"EN",
                "metaTitle":"PopularBrands meta title","metaDescription":"PopularBrands meta description"},"lang":"EN"}""".formatted(lineId, parentBlockId);
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

        for (int i = 0; i <= 10; i++) {

            String elementBodyJson = """
                    {"sortOrder":1,"pageLineBlockId":%s,"objectId":null,
                    "staticBlock":true,"title":"%s",
                    "metaUrl":{"name":"%s","url":"%s"},
                    "metaImage":null,"additionalFields":{},
                    "metaTitle":"%s", "metaDescription":"%s", "lang":"EN"}""".formatted(blockId, "element " + randomInt,
                    "element " + randomInt, "element " + randomInt, "element " + randomInt, "element " + randomInt);

            int elementId = given().
                    header("Authorization", "Bearer " + token).
                    body(elementBodyJson).
                    when().
                    post(CmsServiceEndpoints.ELEMENTS).
                    then().statusCode(200).log().all().
                    body("success", equalTo(true)).
                    extract().
                    response().path("value");
            Assert.assertTrue(elementId > 0);

            File testUploadFile = new File("D:\\Users\\a.gordeeva\\Desktop\\Work\\TestPictures\\lemon.jpg");
            given().
                    header("Authorization", "Bearer " + token).
                    contentType("multipart/form-data").
                    pathParam("elementId", elementId).
                    multiPart("image", testUploadFile).
                    when().
                    patch(CmsServiceEndpoints.ELEMENT_IMAGE).
                    then().statusCode(200).log().all().
                    body("success", equalTo(true));
            randomInt += 1;
        }
    }

    @Test
    @Ignore
    public void deleteLine() {
        given().
                header("Authorization", "Bearer " + token).
                pathParam("lineId", lineId).
                when().post(CmsServiceEndpoints.LINE).
                then().statusCode(200).log().all().
                body("success", equalTo(true));
    }
}
