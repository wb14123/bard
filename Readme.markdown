[![Build Status](https://travis-ci.org/wb14123/bard.svg)](https://travis-ci.org/wb14123/bard)

Bard is a wrapper of servlet. Makes it easier to write handlers.

**It is not stable now.**

Pre-requirements
---------------

* Java 8
* Maven 3

Features
---------------

* Use annotation to define filters, injectors and adapters.
* Very easy to use and extend, no configure file requires.
* Small code base, easy to understand.
* Auto generate API documents.

Working with Annotations
----------------

You can just write handler class and methods with annotations on it. It will run filters, injectors
and adapters with it.

For example, this is a handler that add two numbers:

```

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

It will auto get params from url query. You can see the whole source under
`src/main/java/org/binwang/bard/example/simple`.

Define Your Own Annotations
--------------

You can define your own annotations easily.

There are there kinds of annotations that could be defined:

* Filter: Filter HTTP request and response.
* Injector: Inject variables into handler method params.
* Adapter: Define which handler should be used to handle this HTTP request.

You should choose to extends one of the three classes above.

If you want the servlet auto find the annotations you just defined, use `@BindTo` on the implement
class. And create the servlet with `new Servlet(pkg1, pkg2`), which `pkg1` and `pkg2` are the package
names that contain the implementations.

**You can see the annotations defined in `src/main/java/org/binwang/bard/basic` for examples.**

Handle Errors
--------------

When there are exceptions occurs in the middleware or handler, the framework will put the exception into
`context.exception`, so that you can handle it and do clean up in the `After` actions of middleware.

In the basic package, we provide the filter `HandleErrors` combined with `ErrorCase` to handle the
exceptions that could be thrown by your handler or custom middleware. It will put the exception string
into the result, and auto add documents for you. See how to use it in
`src/main/java/org/binwang/bard/basic/injector/RequiredInjector` for example.


Examples
--------------

There are some examples under `src/main/java/org/binwang/bard/example`:

* simple: a simple example that adds two numbers.
* crud: a restful service that could create, read, update and delete user.

You could use maven to run the examples, run `simple` for example:

```
mvn compile
mvn exec:java -Dexec.mainClass="org.binwang.bard.example.simple.Main"
```

This will start a server on port 8080.

`curl http://localhost:8080/add?a=1&b=2` will get `3` as a result.

You can view [http://localhost:8080/api-doc](http://localhost:8080/api-doc)
to get the auto generated documents in JSON format.
