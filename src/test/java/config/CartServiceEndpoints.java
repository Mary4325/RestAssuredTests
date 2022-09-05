package config;

public interface CartServiceEndpoints {
    String WISH = "cart-service/api/external/wish";
    String UPDATE_CART = "cart-service/api/external/cart/upsert";
    String CART = "cart-service/api/external/cart";
    String CHECKOUT = "cart-service/api/external/cart/checkout";
    String CONTACT_PERSON = "cart-service/api/external/cart/contactPerson";
    String PAYMENT_METHOD = "cart-service/api/external/cart/paymentMethod";
    String DELIVERY_COURIER = "cart-service/api/external/cart/delivery/courier";
    String UPDATE_COMMENT = "cart-service/api/external/cart/comment";
}
