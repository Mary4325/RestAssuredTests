import Pojo.Login;
import config.*;
import config.category_markers.FullRegressTests;
import config.category_markers.ServicesUpCheckTests;
import config.category_markers.SmokeTests;
import io.restassured.http.ContentType;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.io.File;
import java.util.Random;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;

public class UserServiceTests extends EaswaaqConnectionConfig {
    static String token;
    static int randomInt;
    static String loginOperator = "+209609514599";
    static String passwordOperator = "134509";
    static String profileTypeOperator = "OPERATOR";
    static int buyerId = 1859;
    static int addressId = 852;
    static int companyId;
    static int employeeId;
    static int legalSellerId;
    static int stockAddressId;
    static String phoneNum;
    static String email;

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
    public static void random() {
        Random randomGenerator = new Random();
        randomInt = randomGenerator.nextInt(999); // get random number in the range of 0-999
        phoneNum = "221601" + randomInt;
        email = "\"testuser" + randomInt + "@gmail.com\"";
    }

    @Category({FullRegressTests.class, SmokeTests.class, ServicesUpCheckTests.class})
    @Test
    public void getUserTest() {
        given().
                pathParam("userId", 1218).
                header("Authorization", "Bearer " + token).
                queryParams("includeAdditionalFields", "true").
                get(UserServiceEndpoints.USERS).
                then().statusCode(200).log().all().
                body("value.id", equalTo(1218)).
                body("value.login", equalTo("+20222333444")).
                body("value.email", equalTo("test@gmail.com"));
    }

    @Category({FullRegressTests.class, SmokeTests.class})
    @Test
    public void getCompanyTest() {
        given().
                pathParam("companyId", 227).
                header("Authorization", "Bearer " + token).
                get(UserServiceEndpoints.COMPANY).
                then().statusCode(200).log().all().
                body("value.company.id", equalTo(227)).
                body("value.company.shortName", equalTo("Autotest Company"));
    }

    @Category({FullRegressTests.class, SmokeTests.class})
    @Test
    public void getUserDeliveryAddressTest() {
        given().
                pathParam("addressId", addressId).
                formParam("buyerId", buyerId).
                header("Authorization", "Bearer " + token).
                get(UserServiceEndpoints.DELIVERY_ADDRESS).
                then().statusCode(200).log().all().
                body("value.title", equalTo("MyAddress"));
    }

    @Category({FullRegressTests.class})
    @Test
    //Create Company, create legalSeller,set stockAddress(item-service),
    //set sellerDeliveryMethod = aramex (logistics-service), upload company's logo, create employee
    public void createCompanyLegalSellerEmployeeTest() {

        String companyBodyJson = """
                {"shortName":{"EN":"Autotest Company 2023"},"fullName":{"EN":"Autotest Company 2023"},"email":%s,
                "phone":{"countryCode":20,"nationalNumber":%s,"otpId":null,"confirmationCode":null},"siteUrl":"",
                "legalAddress":{"countryWithId":{"id":1,"country":{"iso2":"Egypt","title":"Egypt"}},"region":{"id":1,"title":"Alexandria"},
                "postalCode":12345,"city":"Alexandria","coordinates":null,"addressLine":"Zawya Abd El-Qader Amreya 1","district":"All areas of Alexandria","title":"MyAddress"},
                "regionId":38,"description":{"EN":"English"},"manager":{"firstName":"Test","middleName":null,"lastName":"User"},
                "managerEmail":%s,"managerPhone":{"countryCode":20,"nationalNumber":%s,"otpId":null,"confirmationCode":null},
                "twitter":null,"instagram":null,"whatsApp":null,"facebook":null,"additionalFields":{},"organizationTypeId":1,"companyProductTypes":["PHYSICAL"],
                "externalMerchantId":"","lang":"EN","channelId":null}""".formatted(email, phoneNum, email, phoneNum);
        companyId = given().
                header("Authorization", "Bearer " + token).
                body(companyBodyJson).
                when().
                post(UserServiceEndpoints.COMPANIES).
                then().statusCode(200).log().all().
                body("success", equalTo(true)).
                extract().response().path("value");
        Assert.assertTrue(companyId > 0);

        String makeMerchantBodyJson = """
                {"acceptsCurrency":"EGP","minNegotiationSum":1,"vatCoefficient":null,"minAdvancedPaymentSum":null,
                "tradeOption":false,"maxOrderPrice":null,"minOrderPrice":null,"metaTitle":"seller meta title",
                "metaDescription":"seller meta description","metaKeywords":"seller","companyId":%s}""".formatted(companyId);
        legalSellerId = given().
                header("Authorization", "Bearer " + token).
                body(makeMerchantBodyJson).
                when().
                post(UserServiceEndpoints.MERCHANT).
                then().statusCode(200).log().all().
                body("success", equalTo(true)).
                extract().response().path("value");
        Assert.assertTrue(companyId > 0);

        String deliveryMethodBodyJson = """
                {"sellerId":%s,"providerId":"svip-aramex"}""".formatted(legalSellerId);
        given().
                header("Authorization", "Bearer " + token).
                body(deliveryMethodBodyJson).
                when().
                post(LogisticsServiceEndpoints.DELIVERY_METHODS).
                then().statusCode(200).log().all().
                body("value", equalTo(true)).
                body("success", equalTo(true));

        String stockAddressBodyJson = """
                {"regionId":"1","address":{"countryWithId":{"id":1,"country":{"iso2":"Egypt","title":"Egypt"}},"region":{"id":1,"title":"Cairo"},
                "postalCode":11511,"city":"Attaba","coordinates":{"latitude":30.020224402349466,"longitude":31.333694458007816},
                "addressLine":"TestAddress","district":"Ataba ","title":"Cairo"},"defaultAddress":false,"sellerId":%s}""".formatted(legalSellerId);
        stockAddressId = given().
                header("Authorization", "Bearer " + token).
                body(stockAddressBodyJson).
                when().
                post(ItemServiceEndpoints.STOCK_ADDRESS).
                then().statusCode(200).log().all().
                body("success", equalTo(true)).
                extract().response().path("value");
        Assert.assertTrue(stockAddressId > 0);

        File companyLogoFile = new File("D:\\Users\\a.gordeeva\\Desktop\\Work\\TestPictures\\seller.jpg");
        given().
                header("Authorization", "Bearer " + token).
                contentType("multipart/form-data").
                pathParam("companyId", companyId).
                multiPart("image", companyLogoFile).
                multiPart("request", "{\"sortOrder\":1}", "application/json").
                when().
                patch(UserServiceEndpoints.COMPANY_LOGO).
                then().statusCode(200).log().all().
                body("success", equalTo(true));

        String employeeBodyJson = """
                {"email":%s,"fullName":{"firstName":"Autotest","lastName":"Employee","middleName":"User"},
                "phoneNumber":{"countryCode":20,"nationalNumber":%s,"otpId":null,"confirmationCode":null},"roles":[14]}""".formatted(email, phoneNum);
        employeeId = given().
                header("Authorization", "Bearer " + token).
                pathParam("sellerId", legalSellerId).
                body(employeeBodyJson).
                when().
                post(UserServiceEndpoints.EMPLOYEE).
                then().statusCode(200).log().all().
                body("success", equalTo(true)).
                extract().response().path("value");
        Assert.assertTrue(employeeId > 0);
    }

    @Category({FullRegressTests.class})
    @Test
    public void uploadLogoTest() {
        File companyLogoFile = new File("D:\\Users\\a.gordeeva\\Desktop\\Work\\TestPictures\\seller.jpg");
        given().
                header("Authorization", "Bearer " + token).
                contentType("multipart/form-data").
                pathParam("companyId", 245).
                multiPart("image", companyLogoFile).multiPart("request", "{\"sortOrder\":1}", "application/json").
                when().
                patch(UserServiceEndpoints.COMPANY_LOGO).
                then().statusCode(200).log().all().
                body("success", equalTo(true));
    }
}
