[![Build Status](https://travis-ci.org/wb14123/bard.svg)](https://travis-ci.org/wb14123/bard)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.bardframework/bard/badge.svg)](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22com.bardframework%22)

Bard is a web framework that is easy to use and extend.

**It is not stable now.**

Requirements
---------------

* Java 1.8
* Maven 3

Features
---------------

### Very Easy to Use

You can just write handlers like this:

``` java
@Produces("application/json")
public class SimpleHandler extends Handler {

    @GET
    @Path("/add")
    @Doc("Add two numbers")
    public int add(
        @QueryParam("a") @Required int a,
        @QueryParam("b") @Required int b) {
        return a + b;
    }
    
}
```

### Very Easy to Extend.

For example, you can define your custom injectors like this:

``` java
@BindTo(QueryParam.class)
public class QueryParamInjector extends Injector<QueryParam> {

    @Before
    public void getParams() {
        context.putCustom("param", annotation.value());
        String param = context.getRequest().getParameter(annotation.value());
        if (param == null) {
            context.setInjectorVariable(null);
            return;
        }
        TypeParser parser = TypeParser.newBuilder().build();
        context.setInjectorVariable(parser.parse(param, context.getInjectorVariableType()));
    }

    @Override
    public void generateDoc() {
        docParameter.name = annotation.value();
        docParameter.type = context.getInjectorVariableType();
        docParameter.belongs = "url";
    }
}
```

### Auto Generate Beautiful Document

No more code needed, once your handler is written, the API documents is right there for you.
The users could also try the APIs in the web UI. Isn't that cool?

See a [live demo](http://doc-ui.bardframework.com/?host=http:%2F%2Flinode.binwang.me:8082).

![bard-doc-ui](https://cloud.githubusercontent.com/assets/1906051/5196278/42b7f008-7566-11e4-822b-24eb6e3cb744.gif)

### Build A Self Contained Directory That Is Ready to Deploy

Just with one command `mvn package bard:standalone`, this framework will generate a self contained directory
which is ready to deploy. It contains directory `conf` to store config files, `bin` to store start up and shut
down scripts, `lib` to store library JARs and `log` to store all the logs for you.

Just run `bin/server.sh start | restart | stop`, you can control your server.

### Or Run In Your Favorite Servlet Container

If you want to build a WAR package in order to run in a servlet container, that is easy, too.
Just run `mvn package bard:war`, then you will get a WAR package.

Documents
--------------

See the [detailed documents](https://github.com/wb14123/bard/wiki).

Download
-------------

It is better to manage the dependency with maven:

``` xml
<dependency>
    <groupId>com.bardframework</groupId>
    <artifactId>bard-core</artifactId>
    <version>0.1.0</version>
</dependency>
<dependency>
    <groupId>com.bardframework</groupId>
    <artifactId>bard-basic</artifactId>
    <version>0.1.0</version>
</dependency>
<dependency>
    <groupId>com.bardframework</groupId>
    <artifactId>bard-util</artifactId>
    <version>0.1.0</version>
</dependency>
```

Java Doc
--------------

+ [bard-core](http://jenkins.bardframework.com/job/Bard%20Framework%20Javadoc%200.1/com.bardframework$bard-core/javadoc/)
+ [bard-basic](http://jenkins.bardframework.com/job/Bard%20Framework%20Javadoc%200.1/com.bardframework$bard-basic/javadoc/)
+ [bard-util](http://jenkins.bardframework.com/job/Bard%20Framework%20Javadoc%200.1/com.bardframework$bard-util/javadoc/)

License
--------------

This framework is licensed under [Apache 2.0](http://www.apache.org/licenses/LICENSE-2.0.txt).



