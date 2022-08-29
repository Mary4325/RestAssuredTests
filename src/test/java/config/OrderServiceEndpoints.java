package config;

public interface OrderServiceEndpoints {

    String ORDERS = "/order-service/rest/api/external/orders/{orderId}";
    String DISPUTES = "/order-service/rest/api/external/disputes";
    String PAYMENT_METHODS = "order-service/rest/api/external/merchantPaymentMethods";
}
