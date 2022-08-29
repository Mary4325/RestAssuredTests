package config;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.BeforeClass;

public class SemenaTestConfig {

    public static RequestSpecification easwaaqTest_requestSpec;
    public static ResponseSpecification easwaaqTest_responseSpec;


    @BeforeClass
    public static void setUp(){

        easwaaqTest_requestSpec = new RequestSpecBuilder()
                .setBaseUri("https://gateway.semenatest.bpcmarketplace.com/")
                .setPort(443)
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
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
