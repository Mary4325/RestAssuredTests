package config;

public interface OrderServiceEndpoints {

    String ORDERS = "/order-service/rest/api/external/orders/{orderId}";
    String PAYMENTMETHODS = "order-service/rest/api/external/merchantPaymentMethods";
}
