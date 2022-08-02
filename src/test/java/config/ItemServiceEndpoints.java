package config;

public interface ItemServiceEndpoints {

    String ITEMS = "/item-service/api/external/items/{itemId}";
    String SKU = "/item-service/api/external/sku/{skuId}";
    String PROMOCODES = "/item-service/api/external/promocodes/{promocodeId}";
    String TAGS = "/item-service/api/external/tags/{tagId}";
}
