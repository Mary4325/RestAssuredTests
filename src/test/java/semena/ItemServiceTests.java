package semena;


import config.ItemServiceEndpoints;
import config.UserServiceEndpoints;
import config.SemenaTestConfig;
import io.restassured.http.ContentType;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Random;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class ItemServiceTests extends SemenaTestConfig {
    static String token;
    static int randomInt;
    @BeforeClass
    public static void setUpVariables() {
        String operatorCreadentialsJson = """
                {"login": "tester@gdesemena.ru", 
                "password": "4GOlRMdZ3NKr",
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
        randomInt = randomGenerator.nextInt(1000); // get random number in the range of 0-1000
    }

    @Test
    @Ignore
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
    public void createProductTest() {
 String productBodyJson = """
         {"title": "Test product 04.08.2022",
                                           "categoryId": 2488,
                                           "legalSeller": true,
                                           "vat": 5.00,
                                           "shareRule": "DENY",
                                           "currency": "EGP",
                                           "sellerItemId": "5919",
                                           "description": "<p>Test product 04.08.2022</p>",
                                           "sellerId": 1785,
                                           "updateAdditionalFields": true,
                                           "itemType": "PHYSICAL",
                                           "digitalStoreId": 1,
                                           "metaTitle": "Test product",
                                           "metaDescription": "Test product",
                                           "metaKeywords": "Test product",
                                           "brand": "id": 65177,
                                           "countryId": 129874}""";
    Integer value = given().
            header("Authorization", "Bearer " + token).
            body(productBodyJson).
            when()
            .post(ItemServiceEndpoints.ITEMS).
            then().statusCode(200).log().all().
            body("success", equalTo(true)).
            extract().
            response().path("value");
        Assert.assertTrue(value > 0);
    }

    @Test
    @Ignore
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

    @Test
    @Ignore
    public void getSKUTest() {
        given().
                pathParam("skuId",111883).
                header("Authorization", "Bearer " + token).
                log().all().
                when().get(ItemServiceEndpoints.SKU).
                then().statusCode(200).log().all().
                body("value.id", equalTo(111883));
    }

    @Test
    public void getMarketingCampaignTest() {
        given().
                header("Authorization", "Bearer " + token).
                queryParams("page.size", "10").
                queryParams("page.num", "1").
                queryParams("id", "49").
                get(ItemServiceEndpoints.CAMPAIGNS).
                then().statusCode(200).log().all().
                body("value.data [0].id", equalTo(49)).
                body("value.data [0].title", equalTo("Акция"));
    }

    @Test
//   @Ignore
    public void createUpdateDeleteMarketingCampaignTest() {
        String campaignBodyJson = """
                {"title":"AutotestCompany",
                "description":"AutotestCompany",
                "active":true,
                "priority":2,
                "startedAt":"2022-08-01T00:00:00+03:00",
                "finishedAt":"2022-12-31T00:00:00+03:00"}""".formatted("TestCampaign"+ randomInt);
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
                then();

        given().
                header("Authorization", "Bearer " + token).
                pathParam("campaignId", value).
                when().
                delete(ItemServiceEndpoints.CAMPAIGN).
                then().statusCode(200).log().all();
    }

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

    @Test
    public void createBadgeTest() {
        String badgeBodyJson = """
               {"title":"%s",
               "color":{"red":66,"green":48,"blue":48,"alpha":1},
               "backgroundColor":{"red":31,"green":103,"blue":94,"alpha":1},
               "position":"UP_RIGHT",
               "templateId":4,
               "active":true,
               "startedAt":"2022-08-08T00:00:00+03:00",
               "finishedAt":"2022-08-31T00:00:00+03:00",
               "marketingCompanyId":"54"}""".formatted("Test Badge"+ randomInt);
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


    @Test
    @Ignore
    public void getPromocodeTest() {
        given().
                pathParam("promocodeId",21).
                header("Authorization", "Bearer " + token).
                get(ItemServiceEndpoints.PROMOCODE).
                then().statusCode(200).log().all().
                body("value [0].id", equalTo(21)).
                body("value [0].code", equalTo("1010"));
    }

    @Test
    @Ignore
    public void createUpdateAndDeletePromocodeTest() {
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
                when()
                .post(ItemServiceEndpoints.PROMOCODES).
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

