package org.apache.saltedpeanuts.handler;

import org.apache.saltedpeanuts.TestServer;
import org.apache.saltedpeanuts.model.Article;
import org.apache.saltedpeanuts.model.User;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ArticleHandlerTest extends TestServer {

    @Test
    public void testCreateArticle() throws Exception {
        User user =
            UserHandlerTester.signup("article-user-1" , "article-pass-1" , null, User.class);
        UserHandler.TokenResult token =
            UserHandlerTester.login("article-user-1" , "article-pass-1" ,
                UserHandler.TokenResult.class);
        Article article = ArticleHandlerTester.createArticle(token.token, "title" , "content" ,
            Article.class);
        assertNotNull(article.id);
        assertEquals(user.username, article.author.username);
        assertEquals("title" , article.title);
        assertEquals("content" , article.content);
    }

    @Test
    public void testGetArticle() throws Exception {
        UserHandlerTester.signup("article-user-2" , "article-pass-2" , null, User.class);
        UserHandler.TokenResult token =
            UserHandlerTester.login("article-user-2" , "article-pass-2" ,
                UserHandler.TokenResult.class);
        Article article1 = ArticleHandlerTester.createArticle(token.token, "title2" , "content2" ,
            Article.class);
        Article article2 = ArticleHandlerTester.getArticle(token.token, article1.id, Article.class);
        assertEquals(article1.id, article2.id);
        assertEquals(article1.title, article2.title);
        assertEquals(article1.content, article2.content);
        assertEquals(article1.author.id, article2.author.id);
    }

}
