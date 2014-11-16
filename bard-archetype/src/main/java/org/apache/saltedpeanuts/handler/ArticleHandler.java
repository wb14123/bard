package org.apache.saltedpeanuts.handler;


import com.bardframework.bard.basic.marker.Doc;
import com.bardframework.bard.basic.marker.EscapeHTML;
import com.bardframework.bard.basic.marker.Required;
import com.bardframework.bard.core.Handler;
import com.bardframework.bard.util.db.marker.DBSession;
import com.bardframework.bard.util.user.marker.LoginUser;
import org.apache.saltedpeanuts.model.Article;
import org.apache.saltedpeanuts.model.User;
import org.hibernate.Session;

import javax.ws.rs.*;


@Path("/article")
public class ArticleHandler extends Handler {
    @DBSession public Session dbSession;
    @LoginUser @Required("Auth token error") public String userId;

    @PUT
    @Path("/")
    @Doc("Create an article")
    public Article createArticle(
        @FormParam("title") @Required String title,
        @FormParam("content") @EscapeHTML @Required String content
    ) {
        Article article = new Article();
        article.author = new User(userId);
        article.title = title;
        article.content = content;
        dbSession.save(article);
        return article;
    }

    @GET
    @Path("/{id}")
    @Doc("Get an article by id")
    public Article getArticle(@PathParam("id") @Required String articleId) {
        Article article = new Article();
        dbSession.load(article, articleId);
        return article;
    }
}
