import config.EaswaaqTestConfig;
import config.ItemServiceEndpoints;
import config.UserServiceEndpoints;
import io.restassured.http.ContentType;
import org.junit.BeforeClass;
import org.junit.Test;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;

public class ItemServiceTests extends EaswaaqTestConfig  {
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
    public void getProductTest() {
        given().
                pathParam("itemId", 83632).
                header("Authorization", "Bearer " + token).
                get(ItemServiceEndpoints.ITEMS).
                then().statusCode(200).log().all().
                body("value.title", equalTo("Autotest Coffee New"));
    }

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

    @Test
    public void getPromocodeTest() {
        given().
                pathParam("promocodeId",21).
                header("Authorization", "Bearer " + token).
                get(ItemServiceEndpoints.PROMOCODES).
                then().statusCode(200).log().all().
                body("value [0].id", equalTo(21)).
                body("value [0].code", equalTo("1010"));
    }
}
