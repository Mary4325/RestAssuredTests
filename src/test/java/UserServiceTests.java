import Pojo.Login;
import config.EaswaaqConnectionConfig;
import config.ItemServiceEndpoints;
import config.UserServiceEndpoints;
import config.category_markers.FullRegressTests;
import config.category_markers.ServicesUpCheckTests;
import config.category_markers.SmokeTests;
import io.restassured.http.ContentType;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.Random;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;

public class UserServiceTests extends EaswaaqConnectionConfig {
    static String token;
    static int randomInt;
    static String loginOperator = "+209609514599";
    static String passwordOperator = "134509";
    static String profileTypeOperator =  "OPERATOR";
    static int buyerId = 1859;
    static int addressId = 852;
    static int companyId;
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
    public static void random(){
        Random randomGenerator = new Random();
        randomInt = randomGenerator.nextInt(1000); // get random number in the range of 0-1000

        phoneNum = ("2 2365" + randomInt);
        email= "testuser"+ randomInt +"@gmail.com";
    }
//    @BeforeClass
//    public static void getRandomEmailPhone(){
//        phoneNum = ("2 2365" + randomInt);
//        email= "testuser"+ randomInt +"@gmail.com";
//    }

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
    public void getCompanyTest()  {
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
    public void getUserDeliveryAddressTest()  {
        given().
                pathParam("addressId", addressId).
                formParam("buyerId", buyerId).
                header("Authorization", "Bearer " + token).
                get(UserServiceEndpoints.DELIVERY_ADDRESS).
                then().statusCode(200).log().all().
                body("value.title", equalTo("MyAddress"));
    }
    @Category({FullRegressTests.class, SmokeTests.class})
    @Test
    public void createCompanyAddressTest()  {

        String companyBodyJson = """
                {"shortName":{"EN":"Autotest Company 2023"},"fullName":{"EN":"Autotest Company 2023},"email":%s,
                "phone":{"countryCode":20,"nationalNumber":%s,"otpId":null,"confirmationCode":null},"siteUrl":"",
                "legalAddress":{"countryWithId":{"id":1,"country":{"iso2":"Egypt","title":"Egypt"}},"region":{"id":1,"title":"Alexandria"},
                "postalCode":12345,"city":"Alexandria","coordinates":null,"addressLine":"Zawya Abd El-Qader Amreya 1","
                district":"All areas of Alexandria","title":"MyAddress"},"regionId":38,"description":{"EN":"English"},"manager":{"firstName":"Test","middleName":null,"lastName":"User"},
                "managerEmail":%s","managerPhone":{"countryCode":20,"nationalNumber":%s,"otpId":null,"confirmationCode":null},
                "twitter":null,"instagram":null,"whatsApp":null,"facebook":null,"additionalFields":{},"organizationTypeId":1,"companyProductTypes":["PHYSICAL"],
                "externalMerchantId":"","lang":"EN","channelId":null}""".formatted(email, phoneNum,email, phoneNum );
        companyId = given().
                header("Authorization", "Bearer " + token).
                body(companyBodyJson).
                when().
                post(UserServiceEndpoints.COMPANIES).
                then().statusCode(200).log().all().
                body("success", equalTo(true)).
                extract().
                response().path("value");
        Assert.assertTrue(companyId > 0);
    }

}