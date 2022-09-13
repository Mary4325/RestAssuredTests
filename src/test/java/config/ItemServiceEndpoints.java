package config;

public interface ItemServiceEndpoints {

    String ITEM = "/item-service/api/external/items/{itemId}";
    String ITEMS = "/item-service/api/external/items";
    String ITEM_STATUS = "/item-service/api/external/items/status";
    String SKU = "/item-service/api/external/sku/{skuId}";
    String SKUS = "/item-service/api/external/items/{itemId}/sku";
    String PROMOCODE = "/item-service/api/external/promocodes/{promocodeId}";
    String PROMOCODES = "/item-service/api/external/promocodes";
    String TAGS = "/item-service/api/external/tags/{tagId}";
    String CAMPAIGNS = "/item-service/api/external/marketingCompanies";
    String CAMPAIGN = "/item-service/api/external/marketingCompanies/{campaignId}";
    String BADGES = "item-service/api/external/badges";
    String BADGE = "item-service/api/external/badges/{badgeId}";
    String STOCK_ADDRESS = "item-service/api/external/stockAddress";
}
