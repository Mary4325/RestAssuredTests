import Pojo.ItemStatus;
import Pojo.Login;
import config.EaswaaqConnectionConfig;
import config.ItemServiceEndpoints;
import config.UserServiceEndpoints;
import config.category_markers.FullRegressTests;
import config.category_markers.SmokeTests;
import io.restassured.http.ContentType;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.ContextConfiguration;

import java.util.Random;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;

//@ContextConfiguration
//@ActiveProfiles({"easwaaq_test", "semena_test"})

public class ItemServiceTests extends EaswaaqConnectionConfig {
    static String token;
    static int randomInt;
    static String loginOperator = "+209609514599";
    static String passwordOperator = "134509";
    static String profileTypeOperator =  "OPERATOR";

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

    @Category({FullRegressTests.class, SmokeTests.class})
    @Test
    public void getProductTest() {
        given().
                pathParam("itemId", 83632).
                header("Authorization", "Bearer " + token).
                get(ItemServiceEndpoints.ITEM).
                then().statusCode(200).log().all().
                body("value.title", equalTo("Autotest Coffee"));
    }

    @Test
    @Ignore
    public void changeProductStatusTest() {
        String value = "83632";
        ItemStatus itemstatus = new ItemStatus(new String []{value}, "PUBLISHED");

        given().
                header("Authorization", "Bearer " + token).
                body(itemstatus).
                put(ItemServiceEndpoints.ITEM_STATUS).
                then().statusCode(200).log().all().
                body("value", equalTo(true));
    }

    @Category({FullRegressTests.class, SmokeTests.class})
    @Test
    public void createUpdateDeleteProductTest() {
        String productBodyJson = """
                {"title":"AutotestProduct",
                "description":"<p>AutotestProduct 2022</p>",
                "categoryId":17,
                "sellerId":"1766",
                "sellerItemId":"%s",
                "lang":"EN","vat":5,"currency":"EGP",
                "itemType":"PHYSICAL","countryId":129874,
                "brandId":4,"shareRule":"DENY",
                "link":null,"checking":null,
                "metaTitle":"AutotestProduct","metaDescription":"AutotestProduct",
                "metaKeywords":"AutotestProduct",
                "legalSeller":true}""".formatted("88"+ randomInt);
        Integer value = given().
                header("Authorization", "Bearer " + token).
                body(productBodyJson).
                when().
                post(ItemServiceEndpoints.ITEMS).
                then().statusCode(200).log().all().
                body("success", equalTo(true)).
                extract().
                response().path("value");
        Assert.assertTrue(value > 0);

        String valueString =Integer.toString(value);

        given().
                header("Authorization", "Bearer " + token).
                pathParam("itemId", value).
                body(productBodyJson).
                when().
                put(ItemServiceEndpoints.ITEM).
                then().statusCode(200).log().all();

        ItemStatus statusPublished = new ItemStatus(new String []{valueString}, "PUBLISHED");

        given().  //here we are changing item status to Published
                header("Authorization", "Bearer " + token).
                body(statusPublished).
                put(ItemServiceEndpoints.ITEM_STATUS).
                then().statusCode(200).log().all().
                body("value", equalTo(true));

        ItemStatus statusDeleted = new ItemStatus(new String []{valueString}, "DELETED");

        given().  //here we are changing item status to Deleted
                header("Authorization", "Bearer " + token).
                body(statusDeleted).
                put(ItemServiceEndpoints.ITEM_STATUS).
                then().statusCode(200).log().all().
                body("value", equalTo(true));
    }

    @Category({FullRegressTests.class, SmokeTests.class})
    @Test
    public void getTagTest() {
        given().
                pathParam("tagId", 49).
                header("Authorization", "Bearer " + token).
                queryParams("includeItems", "true").
                queryParams("includeCategories", "true").
                queryParams("includeSellers", "true").
                get(ItemServiceEndpoints.TAGS).
                then().statusCode(200).log().all().
                body("value.id", equalTo(49)).
                body("value.title", equalTo("Don'tDeleteTestTag"));
    }

    @Category({FullRegressTests.class, SmokeTests.class})
    @Test
    public void getSKUTest() {
        given().
                pathParam("skuId",111883).
                header("Authorization", "Bearer " + token).
                log().all().
                when().get(ItemServiceEndpoints.SKU).
                then().statusCode(200).log().all().
                body("value.id", equalTo(111883));
    }

    @Category({FullRegressTests.class, SmokeTests.class})
    @Test
    public void createUpdateDeleteSKUTest() {
        String campaignBodyJson = """
                {"characteristics":{"enumsCharacteristics":{"31":[36]},"numericCharacteristics":{},
                "stringCharacteristics":{"17":{"EN":"no"},"19":{"EN":"white"},"67":{"EN":"123456"}}},
                "minOrderSize":1,"maxOrderSize":null,
                "packTypeId":63,"unitId":6,"weight":10,"originalPrice":200,
                "sellerSkuId":"%s","title":"box","stockAddressId":null,
                "quantity":5000,"sellerId":1785}""".formatted("456"+ randomInt);

        int value = given().
                header("Authorization", "Bearer " + token).
                pathParam("itemId", "83633").
                body(campaignBodyJson).
                when().
                post(ItemServiceEndpoints.SKUS).
                then().statusCode(200).log().all().
                body("success", equalTo(true)).
                extract().
                response().path("value");
        Assert.assertTrue(value > 0);

        given().
                header("Authorization", "Bearer " + token).
                pathParam("skuId", value).
                body(campaignBodyJson).
                when().
                put(ItemServiceEndpoints.SKU).
                then().statusCode(200).log().all();

        given().
                header("Authorization", "Bearer " + token).
                pathParam("skuId", value).
                when().
                delete(ItemServiceEndpoints.SKU).
                then().statusCode(200).log().all();
    }
    @Category({FullRegressTests.class, SmokeTests.class})
    @Test
    public void getMarketingCampaignTest() {
        given().
                header("Authorization", "Bearer " + token).
                queryParams("page.size", "10").
                queryParams("page.num", "1").
                queryParams("id", "10").
                get(ItemServiceEndpoints.CAMPAIGNS).
                then().statusCode(200).log().all().
                body("value.data [0].id", equalTo(10)).
                body("value.data [0].title", equalTo("Summer2022"));
    }

    @Category({FullRegressTests.class, SmokeTests.class})
    @Test
    public void createUpdateDeleteMarketingCampaignTest() {
        String campaignBodyJson = """
                {"title": "%s",
                "description":"Test2022",
                "active":true,
                "priority":2,
                "startedAt":"2022-08-07T00:00:00+03:00",
                "finishedAt":"2022-08-31T00:00:00+03:00"}""".formatted("TestCampaign"+ randomInt);
        int value = given().
                header("Authorization", "Bearer " + token).
                body(campaignBodyJson).
                when().
                post(ItemServiceEndpoints.CAMPAIGNS).
                then().statusCode(200).log().all().
                body("success", equalTo(true)).
                extract().
                response().path("value");
        Assert.assertTrue(value > 0);

        given().
                header("Authorization", "Bearer " + token).
                pathParam("campaignId", value).
                body(campaignBodyJson).
                when().
                put(ItemServiceEndpoints.CAMPAIGN).
                then().statusCode(200).log().all();

        given().
                header("Authorization", "Bearer " + token).
                pathParam("campaignId", value).
                when().
                delete(ItemServiceEndpoints.CAMPAIGN).
                then().statusCode(200).log().all();
    }

    @Category({FullRegressTests.class, SmokeTests.class})
    @Test
    public void getBadgeTest() {
        given().
                header("Authorization", "Bearer " + token).
                queryParams("page.size", "10").
                queryParams("page.num", "1").
                queryParams("id", "7").
                queryParams("includeItems", "true").
                get(ItemServiceEndpoints.BADGES).
                then().statusCode(200).log().all().
                body("value.data [0].id", equalTo(7)).
                body("value.data [0].marketingCompanyId", equalTo(10)).
                body("value.data [0].title", equalTo("Summer2022 Badge"));
    }

    @Category({FullRegressTests.class, SmokeTests.class})
    @Test
    public void createUpdateDeleteBadgeTest() {
        String badgeBodyJson = """
               {"title":"%s",
               "color":{"red":16,"green":23,"blue":60,"alpha":1},
               "backgroundColor":{"red":248,"green":232,"blue":68,"alpha":1},
               "position":"DOWN_RIGHT",
               "templateId":1, "active":true,
               "startedAt":"2022-08-01T00:00:00+03:00",
               "finishedAt":"2022-08-31T00:00:00+03:00",
               "marketingCompanyId":"10"}""".formatted("Test Badge"+ randomInt);
        int value = given().
                header("Authorization", "Bearer " + token).
                body(badgeBodyJson).
                when().
                post(ItemServiceEndpoints.BADGES).
                then().statusCode(200).log().all().
                body("success", equalTo(true)).
                extract().
                response().path("value");
        Assert.assertTrue(value > 0);

        given().
                header("Authorization", "Bearer " + token).
                pathParam("badgeId", value).
                body(badgeBodyJson).
                when().
                put(ItemServiceEndpoints.BADGE).
                then();

        given().
                header("Authorization", "Bearer " + token).
                pathParam("badgeId", value).
                when().
                delete(ItemServiceEndpoints.BADGE).
                then().statusCode(200).log().all();
    }

    @Category({FullRegressTests.class, SmokeTests.class})
    @Test
    public void getPromocodeTest() {
        given().
                pathParam("promocodeId",21).
                header("Authorization", "Bearer " + token).
                get(ItemServiceEndpoints.PROMOCODE).
                then().statusCode(200).log().all().
                body("value [0].id", equalTo(21)).
                body("value [0].code", equalTo("1010"));
    }

    @Category({FullRegressTests.class, SmokeTests.class})
    @Test
    public void createUpdateDeletePromocodeTest() {
        String promocodeBodyJson = """
                {"code": "%s",
                "type": "PERCENT",
                "minOrderPrice": 50,
                "maxDiscount": 400,
                "percent": 10,
                "hint": "test promocode",
                "rules": {},
                "active": true,
                "buyerOnlyOnce": true}""".formatted("promocode"+ randomInt);
        Integer value = given().
                header("Authorization", "Bearer " + token).
                body(promocodeBodyJson).
                when().
                post(ItemServiceEndpoints.PROMOCODES).
                then().statusCode(200).log().all().
                body("success", equalTo(true)).
                extract().
                response().path("value");
        Assert.assertTrue(value > 0);

        given().
                header("Authorization", "Bearer " + token).
                pathParam("promocodeId", value).
                body(promocodeBodyJson).
                when().
                put(ItemServiceEndpoints.PROMOCODE).
                then().statusCode(200).log().all();

        given().
                header("Authorization", "Bearer " + token).
                pathParam("promocodeId", value).
                when()
                .delete(ItemServiceEndpoints.PROMOCODE).
                then().statusCode(200).log().all();
        }
    }

