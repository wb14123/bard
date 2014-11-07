[![Build Status](https://travis-ci.org/wb14123/bard.svg)](https://travis-ci.org/wb14123/bard)

Bard is a web framework that is easy to use and extend.

**It is not stable now.**

Requirements
---------------

* Java 1.8
* Maven 3

Features
---------------

### Use annotation to define filters, injectors and adapters.

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

### Very easy to extend.

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

No more code needed, once your handler is written, the API document is right there for you.

See a [live demo](http://doc-ui.bardframework.com/).

![Doc UI Screenshot](https://cloud.githubusercontent.com/assets/1906051/4930305/5925bd98-6562-11e4-957d-e3ec17656f06.png)


### Run In Your Favorite Servlet Container

Bard could build a WAR package for you so that you could run it in your favorite serlvet container.

You can also run it as a standalone server, too.

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
    <version>0.0.1-SNAPSHOT</version>
</dependency>
<dependency>
    <groupId>com.bardframework</groupId>
    <artifactId>bard-basic</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
<dependency>
    <groupId>com.bardframework</groupId>
    <artifactId>bard-util</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```

Java Doc
--------------

[bard-core](http://jenkins.bardframework.com/job/Bard%20Framework%20Javadoc/com.bardframework$bard-core/javadoc/)
[bard-basic](http://jenkins.bardframework.com/job/Bard%20Framework%20Javadoc/com.bardframework$bard-basic/javadoc/)
[bard-util](http://jenkins.bardframework.com/job/Bard%20Framework%20Javadoc/com.bardframework$bard-util/javadoc/)

License
--------------

This framework is licensed under [Apache 2.0](http://www.apache.org/licenses/LICENSE-2.0.txt).



