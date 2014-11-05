package org.apache.saltedpeanuts.handler;

import org.apache.saltedpeanuts.GenericTester;
import org.apache.saltedpeanuts.model.Article;
import org.apache.saltedpeanuts.model.User;
import org.junit.Test;

import javax.servlet.ServletException;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ArticleHandlerTest extends GenericTester {

    @Test
    public void testCreateArticle() throws ServletException, IOException {
        User user = signUp("article-user-1", "article-pass-1", User.class);
        UserHandler.TokenResult token =
            login("article-user-1", "article-pass-1", UserHandler.TokenResult.class);
        Article article = createArticle(token.token, "title", "content", Article.class);
        assertNotNull(article.id);
        assertEquals(user.id, article.author.id);
        assertEquals("title", article.title);
        assertEquals("content", article.content);
    }

    @Test
    public void testGetArticle() throws ServletException, IOException {
        User user = signUp("article-user-2", "article-pass-2", User.class);
        UserHandler.TokenResult token =
            login("article-user-2", "article-pass-2", UserHandler.TokenResult.class);
        Article article1 = createArticle(token.token, "title2", "content2", Article.class);
        Article article2 = getArticle(token.token, article1.id, Article.class);
        assertEquals(article1.id, article2.id);
        assertEquals(article1.title, article2.title);
        assertEquals(article1.content, article2.content);
        assertEquals(article1.author.id, article2.author.id);
    }

}
