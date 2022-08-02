package config;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.BeforeClass;

public class EaswaaqTestConfig {

    public static RequestSpecification easwaaqTest_requestSpec;
    public static ResponseSpecification easwaaqTest_responseSpec;

    static String token = "eyJhbGciOiJFUzI1NiJ9.eyJzdWIiOiI5MDEiLCJpYXQiOjE2NTkxMTIwMDMsImV4cCI6MTY1OTExMzIwMywicnQiOiJkZDNmYzIxZC0xMTY1LTQzNjUtODU4Ny1kMTZiYmE0ZTZiMDYiLCJydGV4cCI6MTY2NDI5NjAwMywidXNlciI6IntcImlkXCI6OTAxLFwibG9naW5cIjpcIisyMDk2MDk1MTQ1OTlcIixcInBob25lTnVtYmVyXCI6e1wiY291bnRyeUNvZGVcIjoyMCxcIm5hdGlvbmFsTnVtYmVyXCI6OTYwOTUxNDU5OSxcIml0YWxpYW5MZWFkaW5nWmVyb1wiOmZhbHNlfSxcImVtYWlsXCI6XCJ0ZXN0ZXJAZ2Rlc2VtZW5hLnJ1XCIsXCJmdWxsTmFtZVwiOntcImZpcnN0TmFtZVwiOlwiVGVzdFwiLFwibGFzdE5hbWVcIjpcIlRlc3RcIixcIm1pZGRsZU5hbWVcIjpcIlRlc3RcIn0sXCJwcml2aWxlZ2VzXCI6Wy0xNDQwNDcwMTgzNjc1MTY2NzMsMTM2MzU4Nzk1NTM1XSxcImN1cnJlbnRQcm9maWxlXCI6XCJPUEVSQVRPUlwiLFwib3BlcmF0b3JQcm9maWxlXCI6e1wiaWRcIjoxMjQ3LFwic3RhdHVzXCI6XCJORVdcIn0sXCJibG9ja2VkXCI6ZmFsc2UsXCJ2ZXJpZmllZFwiOmZhbHNlLFwidGVybXNVc2VJZFwiOjB9In0.0VBytpLwIO6WbaVS1JBHN1n7PbE_8O1AfsBFU1xAXjDxAb0gUGKE-LYriIomyAd4-O1gTL61QZZG7eerSuQ0Gw";

    @BeforeClass
    public static void setUp(){

        easwaaqTest_requestSpec = new RequestSpecBuilder()
                .setBaseUri("https://gateway.efinancetest.bpcmarketplace.com")
                .setPort(443)
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", "Bearer " + token)
                .addFilter(new RequestLoggingFilter())
                .addFilter(new ResponseLoggingFilter())
                .build();

        easwaaqTest_responseSpec = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .build();

        RestAssured.requestSpecification = easwaaqTest_requestSpec;
        RestAssured.responseSpecification = easwaaqTest_responseSpec;
    }
}
