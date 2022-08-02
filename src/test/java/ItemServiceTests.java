import config.EaswaaqTestConfig;
import config.ItemServiceEndpoints;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.SQLOutput;

import static io.restassured.RestAssured.*;
import static io.restassured.http.Method.GET;
import static org.hamcrest.Matchers.equalTo;

public class ItemServiceTests extends EaswaaqTestConfig  {
    static String token = "eyJhbGciOiJFUzI1NiJ9.eyJzdWIiOiI5MDEiLCJpYXQiOjE2NTMzODU2ODYsImV4cCI6MTY1MzM4NTc0NiwicnQiOiI2NDZlYjlhOS04YmU4LTQ5NTktODNkMS00NGVkYTYyY2QyNGMiLCJydGV4cCI6MTY1ODU2OTY4NiwidXNlciI6IntcImlkXCI6OTAxLFwibG9naW5cIjpcIisyMDk2MDk1MTQ1OTlcIixcInBob25lTnVtYmVyXCI6e1wiY291bnRyeUNvZGVcIjoyMCxcIm5hdGlvbmFsTnVtYmVyXCI6OTYwOTUxNDU5OSxcIml0YWxpYW5MZWFkaW5nWmVyb1wiOmZhbHNlfSxcImVtYWlsXCI6XCJ0ZXN0ZXJAZ2Rlc2VtZW5hLnJ1XCIsXCJmdWxsTmFtZVwiOntcImZpcnN0TmFtZVwiOlwiVGVzdFwiLFwibGFzdE5hbWVcIjpcIlRlc3RcIixcIm1pZGRsZU5hbWVcIjpcIlRlc3RcIn0sXCJwcml2aWxlZ2VzXCI6Wy0xNDM4MzU5MTIxMzUxODAyODksMTM2MzY1MDg2OTkxXSxcImN1cnJlbnRQcm9maWxlXCI6XCJPUEVSQVRPUlwiLFwib3BlcmF0b3JQcm9maWxlXCI6e1wiaWRcIjoxMjQ3LFwic3RhdHVzXCI6XCJORVdcIn0sXCJibG9ja2VkXCI6ZmFsc2UsXCJ2ZXJpZmllZFwiOmZhbHNlLFwidGVybXNVc2VJZFwiOjB9In0.PWWYWTBSlaJ1nmQ3IdSTVeYM8e9GZ36wUURy0f-AIElO02D9NaJcF4aMYuS4e9sWXccwoTXMs6Yegz3dt9YZxg";

    @Before
    public void init() {
        given().auth().preemptive().basic("swagger", "bpc@password_swager");
    }

    @Test
    public void getProductTest() {
        given().
                pathParam("itemId", 83632).
                auth().preemptive().basic("swagger", "bpc@password_swager").
                get(ItemServiceEndpoints.ITEMS).
                then().log().all().
                body("value.title", equalTo("Autotest Coffee New"));
    }

    @Test
    public void getTagTest() {
        given().
                pathParam("tagId", 49).
                //   header("Authorization", "Bearer " + token)
                auth().preemptive().basic("swagger", "bpc@password_swager").
                queryParams("includeItems", "true").
                queryParams("includeCategories", "true").
                queryParams("includeSellers", "true").
                get(ItemServiceEndpoints.TAGS).
                then().log().all().
                body("value.id", equalTo(49)).
                body("value.title", equalTo("Don'tDeleteTestTag"));
    }


    @Test
    public void getSKUTest() {
        given().
                pathParam("skuId",111883).
                auth().preemptive().basic("swagger", "bpc@password_swager").
        //        header("Authorization", "Bearer " + token)
                log().all().
                when().get(ItemServiceEndpoints.SKU).
                then().
                log().all().
                body("value.id", equalTo(111883));
    }

    @Test
    public void getPromocodeTest() {
        given().
                pathParam("promocodeId",21).
                auth().preemptive().basic("swagger", "bpc@password_swager").
                get(ItemServiceEndpoints.PROMOCODES).
                then().log().all().
                body("value [0].id", equalTo(21)).
                body("value [0].code", equalTo("1010"));
    }


//    @Test
//    public void getTest() {
//
//        Response resp = given().header("Authorization", "Bearer "+token);
//        String responseBody = get(ItemServiceEndpoints.TAGS).asString();
//        System.out.println(responseBody);
//
//    }
}
