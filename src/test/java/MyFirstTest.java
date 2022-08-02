import config.EaswaaqTestConfig;
import config.ItemServiceEndpoints;
import config.UserServiceEndpoints;
import org.junit.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;

public class MyFirstTest extends EaswaaqTestConfig {
    String accessToken =  "eyJhbGciOiJFUzI1NiJ9.eyJzdWIiOiI5MDEiLCJpYXQiOjE2NTkxMTIwMDMsImV4cCI6MTY1OTExMzIwMywicnQiOiJkZDNmYzIxZC0xMTY1LTQzNjUtODU4Ny1kMTZiYmE0ZTZiMDYiLCJydGV4cCI6MTY2NDI5NjAwMywidXNlciI6IntcImlkXCI6OTAxLFwibG9naW5cIjpcIisyMDk2MDk1MTQ1OTlcIixcInBob25lTnVtYmVyXCI6e1wiY291bnRyeUNvZGVcIjoyMCxcIm5hdGlvbmFsTnVtYmVyXCI6OTYwOTUxNDU5OSxcIml0YWxpYW5MZWFkaW5nWmVyb1wiOmZhbHNlfSxcImVtYWlsXCI6XCJ0ZXN0ZXJAZ2Rlc2VtZW5hLnJ1XCIsXCJmdWxsTmFtZVwiOntcImZpcnN0TmFtZVwiOlwiVGVzdFwiLFwibGFzdE5hbWVcIjpcIlRlc3RcIixcIm1pZGRsZU5hbWVcIjpcIlRlc3RcIn0sXCJwcml2aWxlZ2VzXCI6Wy0xNDQwNDcwMTgzNjc1MTY2NzMsMTM2MzU4Nzk1NTM1XSxcImN1cnJlbnRQcm9maWxlXCI6XCJPUEVSQVRPUlwiLFwib3BlcmF0b3JQcm9maWxlXCI6e1wiaWRcIjoxMjQ3LFwic3RhdHVzXCI6XCJORVdcIn0sXCJibG9ja2VkXCI6ZmFsc2UsXCJ2ZXJpZmllZFwiOmZhbHNlLFwidGVybXNVc2VJZFwiOjB9In0.0VBytpLwIO6WbaVS1JBHN1n7PbE_8O1AfsBFU1xAXjDxAb0gUGKE-LYriIomyAd4-O1gTL61QZZG7eerSuQ0Gw";
	String basicToken = "c3dhZ2dlcjpicGNAcGFzc3dvcmRfc3dhZ2Vy";
    String username = "+209609514599";
    String password =  "134509";
    String code;



    @Test
    public void myFirstTest() {
        given().
                pathParam("skuId",111883).
                header("Authorization", "Bearer " + accessToken)
                .log().all().
                when().get(ItemServiceEndpoints.SKU).
                then().
                log().all();
    }

    @Test
    public void oauth2Test() {
        given().
                when().
                get("/order-service/rest/api/external/merchantPaymentMethods?sellerId=1785").
                then().log().all().
                body("value [0].selected", equalTo("true"));
        //  String contentType = response
        //    System.out.println(contentType);

    }



    @Test
    public void getOperatorToken(){
        String operatorCreadentialsJson = "{\n" +
                "  \"login\": \"+209609514599\",\n" +
                "  \"password\": \"134509\",\n" +
                "  \"compressJwt\": true,\n" +
                "  \"profilerKey\": \"string\",\n" +
                "  \"profileType\": \"OPERATOR\"\n" +
                "}";
        given()
                .body(operatorCreadentialsJson).
        when()
                .post(UserServiceEndpoints.JWT_TOKEN).
        then();
    }





}