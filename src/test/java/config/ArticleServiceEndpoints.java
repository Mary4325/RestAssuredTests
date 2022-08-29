package config;

public interface ArticleServiceEndpoints {

    String ARTICLES = "article-service/api/external/articles";
    String ARTICLE = "article-service/api/external/articles/{articleId}";
    String ARTICLE_STATUS = "article-service/api/external/articles/{articleId}/status";
    String ARTICLE_IMAGE = "article-service/api/external/articles/{articleId}/image";
    String CATEGORIES = "article-service/api/external/categories";
    String CATEGORY = "article-service/api/external/categories/{categoryId}";
}
