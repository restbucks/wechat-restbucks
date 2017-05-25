package org.restbucks.wechat.bff.wechat.messaging;

import static java.util.Collections.singletonList;

import java.util.List;
import java.util.stream.Stream;
import lombok.Builder;
import lombok.Value;
import org.restbucks.wechat.bff.wechat.messaging.NewsMessage.Article.ArticleBuilder;

@Value
public class NewsMessage {

    private String recipient;

    private List<Article> articles;

    public NewsMessage(String recipient, ArticleBuilder article) {
        this(recipient, singletonList(article.build()));
    }

    public NewsMessage(String recipient, Article article) {
        this(recipient, singletonList(article));
    }

    public NewsMessage(String recipient, List<Article> articles) {
        this.recipient = recipient;
        this.articles = articles;
    }

    public Stream<Article> articles() {
        return articles.stream();
    }

    @Value
    @Builder
    public static class Article {

        private String title;
        private String description;
        private String url;
        private String image;


    }
}


