import Pojo.Login;
import config.ArticleServiceEndpoints;
import config.EaswaaqConnectionConfig;
import config.UserServiceEndpoints;
import config.category_markers.FullRegressTests;
import config.category_markers.ServicesUpCheckTests;
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
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.lessThan;

public class ArticleServiceTests extends EaswaaqConnectionConfig {
    static String token;
    static int randomInt;
    static String loginOperator = "+209609514599";
    static String passwordOperator = "134509";
    static String profileTypeOperator =  "OPERATOR";
    static int articleId = 28;

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

    @BeforeClass
    public static void random(){
        Random randomGenerator = new Random();
        randomInt = randomGenerator.nextInt(1000); // get random number in the range of 0-1000
    }

    @Category({FullRegressTests.class, SmokeTests.class, ServicesUpCheckTests.class})
    @Test
    public void getArticleTest() {
        given().
                pathParam("articleId", articleId).
                header("Authorization", "Bearer " + token).
                get(ArticleServiceEndpoints.ARTICLE).
                then().statusCode(200).log().all().
                body("value.id", equalTo(articleId)).
                body("value.title", equalTo("AutotestTestArticle2022"));
    }

    @Category({FullRegressTests.class, SmokeTests.class})
    @Test
    public void createUpdateDeleteArticleTest() {
        String articleBodyJson = """
                {"title":"AutotestTestArticle2022","shortDescription":"AutotestTestArticle ",
                "html":"<p>AutotestTestArticle2022 AutotestTestArticle2022</p>",
                "categoryId":8,"lang":"EN","metaTitle":"TestArticle",
                "metaDescription":"TestArticle",
                "metaKeywords":"TestArticle"}""".formatted("AutotestArticle" + randomInt);

        int value = given().
                header("Authorization", "Bearer " + token).
                body(articleBodyJson).
                when().
                post(ArticleServiceEndpoints.ARTICLES).
                then().statusCode(200).log().all().
                body("success", equalTo(true)).
                extract().
                response().path("value");
        Assert.assertTrue(value > 0);
        String valueString = Integer.toString(value);

        given().
                header("Authorization", "Bearer " + token).
                pathParam("articleId", value).
                body(articleBodyJson).
                when().
                put(ArticleServiceEndpoints.ARTICLE).
                then().statusCode(200).log().all();

        given().  //publishing created article
                header("Authorization", "Bearer " + token).
                pathParam("articleId", valueString).
                body("\"PUBLISHED\"").
                when().
                patch(ArticleServiceEndpoints.ARTICLE_STATUS).
                then().statusCode(200).log().all();

        given().
                header("Authorization", "Bearer " + token).
                pathParam("articleId", value).
                when().
                delete(ArticleServiceEndpoints.ARTICLE).
                then().statusCode(200).log().all();
    }

    @Category({FullRegressTests.class, SmokeTests.class})
    @Test
    @Ignore
    public void addBigImageToArticleTest() {
        File bigUploadFile = new File("D:\\Users\\a.gordeeva\\Desktop\\Work\\TestPictures\\bigPicture.jpg");
        given().
                header("Authorization", "Bearer " + token).
                contentType("multipart/form-data").
                pathParam("articleId", articleId).
                multiPart("image", bigUploadFile).
                when().
                post(ArticleServiceEndpoints.ARTICLE_IMAGE).
                then().log().all().time(lessThan(7000L)).
                body("success", equalTo(false));
    }

    @Category({FullRegressTests.class, SmokeTests.class})
    @Test
    public void addImageToArticleTest() {
        File testUploadFile = new File("D:\\Users\\a.gordeeva\\Desktop\\Work\\TestPictures\\corgi.jpg");
        given().
                header("Authorization", "Bearer " + token).
                contentType("multipart/form-data").
                pathParam("articleId", articleId).
                multiPart("image", testUploadFile).
                when().
                post(ArticleServiceEndpoints.ARTICLE_IMAGE).
                then().statusCode(200).log().all().
                body("success", equalTo(true));
    }

    @Category({FullRegressTests.class, SmokeTests.class})
    @Test
    public void createUpdateHideCategoryTest() {
        String categoryBodyJson = """
                { "title": "AutotestCategory",
                "shortDescription": "AutotestCategory",
                "sortOrder": "%s", "alias": "AutotestCategory",
                "metaTitle": "AutotestCategory",
                "metaDescription": "AutotestCategory",
                "metaKeywords": "AutotestCategory",
                "itemMetaTitle": "Article meta title",
                "itemMetaDescription": "Article meta description",
                "itemMetaKeywords": "Article meta keywords",
                 "portalDeterminate": "COMMON"}""".formatted(randomInt);

        int value = given().
                header("Authorization", "Bearer " + token).
                body(categoryBodyJson).
                when().
                post(ArticleServiceEndpoints.CATEGORIES).
                then().statusCode(200).log().all().
                body("success", equalTo(true)).
                extract().
                response().path("value");
        Assert.assertTrue(value > 0);;

        given().
                header("Authorization", "Bearer " + token).
                pathParam("categoryId", value).
                body(categoryBodyJson).
                when().
                put(ArticleServiceEndpoints.CATEGORY).
                then().statusCode(200).log().all();

        given().  //updating article category status
                header("Authorization", "Bearer " + token).
                pathParam("categoryId", value).
                body("false").
                when().
                patch(ArticleServiceEndpoints.CATEGORY).
                then().statusCode(200).log().all();
    }
}
