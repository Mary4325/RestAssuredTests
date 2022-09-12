import Pojo.Login;
import config.EaswaaqConnectionConfig;
import config.ReportServiceEndpoints;
import config.UserServiceEndpoints;
import config.category_markers.FullRegressTests;
import config.category_markers.ServicesUpCheckTests;
import config.category_markers.SmokeTests;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.groovy.util.Arrays;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class ReportServiceTests extends EaswaaqConnectionConfig {
    static String token;
    static String loginOperator = "+209609514599";
    static String passwordOperator = "134509";
    static String profileTypeOperator = "OPERATOR";

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

    @Category({FullRegressTests.class, SmokeTests.class, ServicesUpCheckTests.class})
    @Test
    public void getReportsTest() {
        given().
                header("Authorization", "Bearer " + token).
                get(ReportServiceEndpoints.REPORTS).
                then().statusCode(200).log().all().
                body("success", equalTo(true));
    }

    @Category({FullRegressTests.class, SmokeTests.class})
    @Test
    public void getSalesReportTest() {
        given().
                header("Authorization", "Bearer " + token).
                pathParam("reportId", 1).
                get(ReportServiceEndpoints.REPORT).
                then().statusCode(200).log().all().
                body("success", equalTo(true)).
                body("value.name", equalTo("Sales Data"));
    }

    @Category({FullRegressTests.class, SmokeTests.class})
    @Test
    public void getProductsDataTest() {
        given().
                header("Authorization", "Bearer " + token).
                pathParam("reportId", 2).
                get(ReportServiceEndpoints.REPORT).
                then().statusCode(200).log().all().
                body("success", equalTo(true)).
                body("value.name", equalTo("Products Data"));
    }

    @Category({FullRegressTests.class, SmokeTests.class})
    @Test
    public void getOrdersDataTest() {
        given().
                header("Authorization", "Bearer " + token).
                pathParam("reportId", 3).
                get(ReportServiceEndpoints.REPORT).
                then().statusCode(200).log().all().
                body("success", equalTo(true)).
                body("value.name", equalTo("Orders Data - Summarized"));

        given().
                header("Authorization", "Bearer " + token).
                pathParam("reportId", 4).
                get(ReportServiceEndpoints.REPORT).
                then().statusCode(200).log().all().
                body("success", equalTo(true)).
                body("value.name", equalTo("Orders Data - Detailed"));

        given().
                header("Authorization", "Bearer " + token).
                pathParam("reportId", 5).
                get(ReportServiceEndpoints.REPORT).
                then().statusCode(200).log().all().
                body("success", equalTo(true)).
                body("value.name", equalTo("Orders Settlement Status"));
    }

    @Category({FullRegressTests.class, SmokeTests.class})
    @Test
    public void getClientsCompaniesEmployeeDeliveryDataTest() {
        given().
                header("Authorization", "Bearer " + token).
                pathParam("reportId", 6).
                get(ReportServiceEndpoints.REPORT).
                then().statusCode(200).log().all().
                body("success", equalTo(true)).
                body("value.name", equalTo("Clients Data"));

        given().
                header("Authorization", "Bearer " + token).
                pathParam("reportId", 7).
                get(ReportServiceEndpoints.REPORT).
                then().statusCode(200).log().all().
                body("success", equalTo(true)).
                body("value.name", equalTo("Companies Data"));

        given().
                header("Authorization", "Bearer " + token).
                pathParam("reportId", 8).
                get(ReportServiceEndpoints.REPORT).
                then().statusCode(200).log().all().
                body("success", equalTo(true)).
                body("value.name", equalTo("Employee - Company Data"));

        given().
                header("Authorization", "Bearer " + token).
                pathParam("reportId", 9).
                get(ReportServiceEndpoints.REPORT).
                then().statusCode(200).log().all().
                body("success", equalTo(true)).
                body("value.name", equalTo("Delivery Data"));
    }

    @Category({FullRegressTests.class, SmokeTests.class})
    @Test
    public void downloadProductsReportTest() {
        String contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
        LocalDateTime startDate = LocalDateTime.now().minusMonths(6);

        Response response = RestAssured.given().
                        header("Authorization", "Bearer " + token).
                        pathParam("reportId", "products").
                  //    queryParams("startDate", startDate.toString()).
                        queryParams("page.num", 1).
                        queryParams("page.size", 1000).
                        when().
                        get(ReportServiceEndpoints.REPORT).
                        andReturn().then().statusCode(200).log().all().extract().response();

        Assert.assertTrue(response.body().asByteArray().length > 0);
        Assert.assertEquals(response.getHeader("Content-Type"), contentType);
        }

    @Category({FullRegressTests.class, SmokeTests.class})
    @Test
    public void downloadSalesReportTest() {
        String contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

        Response response = RestAssured.given().
                header("Authorization", "Bearer " + token).
                pathParam("reportId", "sales").
                queryParams("page.num", 1).
                queryParams("page.size", 1000).
                when().
                get(ReportServiceEndpoints.REPORT).
                andReturn().then().statusCode(200).log().all().extract().response();

        Assert.assertTrue(response.body().asByteArray().length > 0);
        Assert.assertEquals(response.getHeader("Content-Type"), contentType);
    }

    @Category({FullRegressTests.class, SmokeTests.class})
    @Test
    public void downloadOrdersSummarizedReportTest() {
        String contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

        Response response = RestAssured.given().
                header("Authorization", "Bearer " + token).
                pathParam("reportId", "ordersSummarized").
                queryParams("page.num", 1).
                queryParams("page.size", 1000).
                when().
                get(ReportServiceEndpoints.REPORT).
                andReturn().then().statusCode(200).log().all().extract().response();

        Assert.assertTrue(response.body().asByteArray().length > 0);
        Assert.assertEquals(response.getHeader("Content-Type"), contentType);
    }

    @Category({FullRegressTests.class, SmokeTests.class})
    @Test
    public void downloadOrdersDetailedReportTest() {
        String contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

        Response response = RestAssured.given().
                header("Authorization", "Bearer " + token).
                pathParam("reportId", "ordersDetailed").
                queryParams("page.num", 1).
                queryParams("page.size", 1000).
                when().
                get(ReportServiceEndpoints.REPORT).
                andReturn().then().statusCode(200).log().all().extract().response();

        Assert.assertTrue(response.body().asByteArray().length > 0);
        Assert.assertEquals(response.getHeader("Content-Type"), contentType);
    }

    @Category({FullRegressTests.class, SmokeTests.class})
    @Test
    public void downloadClientsReportTest() {
        String contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

        Response response = RestAssured.given().
                header("Authorization", "Bearer " + token).
                pathParam("reportId", "clients").
                queryParams("page.num", 1).
                queryParams("page.size", 1000).
                when().
                get(ReportServiceEndpoints.REPORT).
                andReturn().then().statusCode(200).log().all().extract().response();

        Assert.assertTrue(response.body().asByteArray().length > 0);
        Assert.assertEquals(response.getHeader("Content-Type"), contentType);
    }

    @Category({FullRegressTests.class})
    @Test
    public void downloadCompaniesReportTest() {
        String contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

        Response response = RestAssured.given().
                header("Authorization", "Bearer " + token).
                pathParam("reportId", "companies").
                queryParams("page.num", 1).
                queryParams("page.size", 1000).
                when().
                get(ReportServiceEndpoints.REPORT).
                andReturn().then().statusCode(200).log().all().extract().response();

        Assert.assertTrue(response.body().asByteArray().length > 0);
        Assert.assertEquals(response.getHeader("Content-Type"), contentType);
    }

    @Category({FullRegressTests.class})
    @Test
    public void downloadEmployeesReportTest() {
        String contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

        Response response = RestAssured.given().
                header("Authorization", "Bearer " + token).
                pathParam("reportId", "employees").
                queryParams("page.num", 1).
                queryParams("page.size", 1000).
                when().
                get(ReportServiceEndpoints.REPORT).
                andReturn().then().statusCode(200).log().all().extract().response();

        Assert.assertTrue(response.body().asByteArray().length > 0);
        Assert.assertEquals(response.getHeader("Content-Type"), contentType);
    }

    @Category({FullRegressTests.class})
    @Test
    public void downloadDeliveryReportTest() {
        String contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

        Response response = RestAssured.given().
                header("Authorization", "Bearer " + token).
                pathParam("reportId", "delivery").
                queryParams("page.num", 1).
                queryParams("page.size", 1000).
                when().
                get(ReportServiceEndpoints.REPORT).
                andReturn().then().statusCode(200).log().all().extract().response();

        Assert.assertTrue(response.body().asByteArray().length > 0);
        Assert.assertEquals(response.getHeader("Content-Type"), contentType);
    }
}

