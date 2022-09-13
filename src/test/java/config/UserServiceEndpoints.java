package config;

public interface UserServiceEndpoints {

    String JWT_TOKEN = "/user-service/api/external/jwtToken";
    String COMPANY = "user-service/api/external/companies/{companyId}";
    String COMPANIES = "user-service/api/external/companies";
    String MERCHANT = "user-service/api/external/legalSellers";
    String USERS = "/user-service/api/external/users/{userId}";
    String DELIVERY_ADDRESS = "user-service/api/external/deliveryAddress/{addressId}";
    String COMPANY_LOGO = "user-service/api/external/companies/{companyId}/logo";
    String EMPLOYEE = "user-service/api/external/legalSellers/{sellerId}/member";
}
