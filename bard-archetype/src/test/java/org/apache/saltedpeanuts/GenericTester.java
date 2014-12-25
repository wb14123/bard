package org.apache.saltedpeanuts;

import com.bardframework.bard.util.server.BardServer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import java.io.IOException;

public class GenericTester {
    protected static String host;
    private static BardServer server = new BardServer(SimpleServlet.class);
    protected ObjectMapper mapper = new ObjectMapper();

    @BeforeClass
    public static void runBeforeClass() throws Exception {
        host = server.startForTest();
    }

    @AfterClass
    public static void runAfterClass() throws Exception {
        server.stop();
        server = new BardServer(SimpleServlet.class);
    }

    // user handler
    protected <T> T signUp(String username, String password, Class<T> t)
        throws UnirestException, IOException {
        String content = Unirest.get(host + "/user/signup")
            .queryString("username", username)
            .queryString("password", password)
            .asString().getBody();
        return mapper.readValue(content, t);
    }

    protected <T> T login(String username, String password, Class<T> t)
        throws IOException, UnirestException {
        String content = Unirest.get(host + "/user/login")
            .queryString("username", username)
            .queryString("password", password)
            .asString().getBody();
        return mapper.readValue(content, t);
    }

    protected <T> T info(String token, Class<T> t)
        throws IOException, UnirestException {
        String content = Unirest.get(host + "/user/info")
            .header("auth-token", token)
            .asString().getBody();
        return mapper.readValue(content, t);
    }

    // article handler
    protected <T> T createArticle(String token, String title, String content, Class<T> t)
        throws IOException, UnirestException {
        String c = Unirest.put(host + "/article")
            .header("auth-token", token)
            .field("title", title)
            .field("content", content)
            .asString().getBody();
        return mapper.readValue(c, t);
    }

    protected <T> T getArticle(String token, String id, Class<T> t)
        throws IOException, UnirestException {
        String c = Unirest.get(host + "/article/{id}")
            .header("auth-token", token)
            .routeParam("id", id)
            .asString().getBody();
        return mapper.readValue(c, t);

    }
}
