[![Build Status](https://travis-ci.org/wb14123/bard.svg)](https://travis-ci.org/wb14123/bard)

Bard is a wrapper of servlet. Makes it easier to write handlers.

**It is not stable now.**

Requirements
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
`bard-example/src/main/java/org/binwang/bard/example/simple`.

Some basic annotations are already defined under `bard-basic`. It defines some annotations in JAX-RS.
But it is not compatible with JAX-RS completely, since **the order of annotations matters**.


How the Annotations work
-------------------

There are three kinds of annotations in Bard:

* Adapter: Define which handler should be used to handle this HTTP request.
* Filter: Filter HTTP request and response.
* Injector: Inject variables into handler method params.

Here is how the annotations are run when a HTTP request comes:

### Run Adapters

Adapters specify which handler should handle the income request. Such as `@Path`, `@GET`, `@POST` are
adapters.

The framework will run adapters on the handler methods first, decide whether to use this method to
handle the request.

Adapters could be used both on class and method.

Adapters has match actions (methods annotated with `@Match`). The match actions will return `true` or
`false`. If one of the adapters' match actions returns `false`, the framework will skip this handler
and start to check whether the next handler should be used to handle the request.

If the adapter returns true,  after actions (methods annotated `@After`) will run in order to clean up
things.

If a class and a method in it both annotated with the same adapter, things will be different. I will
write documents about this later.

### Run Before Actions of Filters

Filters could filter the HTTP request or the response. Such as `@Produces` is filter.

The filter could be used both on class and method.

The filters will be run after all the adapters on the method returns true. Then filters' before actions
(methods annotated with @Before) will be run. The filter could do want it wants, such as write something
in `Context` and so on.


### Run Before Actions of Injectors

Injectors inject variables into handler method params. Such as `@PathParam`, `@PathParam` are injectors.

Injectors could be used on handler method's params and class public fields.

Injectors could get something from `context` in there before actions (methods defined with `@Before`),
and write them to `context.injectoVariable`. Then the framework will take the variable, use them as the
params and the invoke the handler method.

### Run the Handler Method

The here comes the handler method. Only the methods in Handler class with at least one adapter annotation
are considered as handler method. You can just return the result. Filters could handle the result for you.

### Run After Actions of Filters

Filters' after actions will be run in order to clean up things. `context.exception` will store the exception
thrown in previous steps. `context.result` will store the result returned by the handler. You can do something
with it, such as write them into the response.

### Run After Actions of Injectors

After run the handler method, injectors' after actions (methods annotated with `@After`) will be run, in order
to cleanup things. If there are any exceptions thrown by handler, `context.exception` will store it.

Things are done for now.

### Break the Chain

You may want the chain break if there are something you don't except. Such as if you are writing an injector
`@Requried` that checks whether the param is null, you may want to break the chain and return an error immediate while
the param is null.

When you want this, just throw an exception, then the chain will not go on. But the after actions of
already run filters and injectors,  will still run, in order to cleanup things. But `context.exception` will
be the exception that you just thrown.

You can use `@HandleErrors` as a helper filter to handle the exception you thrown. See the sections
below for details.

Define Your Own Annotations
--------------

You can define your own annotations easily. 

1. Define an annotation. `@DefinedAnnotation` for example.
2. Define an implement class. Extend one of these classes: `Adapter`, `Filter`, `Injector`.
Write match actions, before actions or after actions for it.
3. Bind them together. Use `@BindTo(DefinedAnnotation.class)` on the implement class.
4. Use the annotations. Create the servlet with `new Servlet(pkg1, pkg2`), which `pkg1` and `pkg2` are the package
names that contain the implementations.

**You can see the annotations defined in `bard-basic` for examples.**

Handle Errors
--------------

When there are exceptions occurs in the middleware or handler, the framework will put the exception into
`context.exception`, so that you can handle it and do clean up in the `After` actions of middleware.

In the basic package, we provide the filter `HandleErrors` combined with `ErrorCase` to handle the
exceptions that could be thrown by your handler or custom middleware. It will put the exception string
into the result, and auto add documents for you. You can see the usage in `bard-example`, with the
example `crud`.


Auto Generate Document
---------------

When the server is started, you can request `/api-doc` to get the document as JSON format. You could use
[bard-doc-ui](https://github.com/wb14123/bard-doc-ui) as a beautiful UI interface to display the document
on a web page.

`generateDoc` method in Adapter, Filter and Injector are used to auto generate documents.

Examples
--------------

There are some examples under `bard-example`:

* simple: a simple example that adds two numbers.
* crud: a restful service that could create, read, update and delete user.

You could use maven to run the examples, run `simple` for example:

```
mvn install
cd bard-example
mvn exec:java -Dexec.mainClass="org.binwang.bard.example.simple.Main"
```

This will start a server on port 8080.

`curl http://localhost:8080/add?a=1&b=2` will get `3` as a result.

You can view [http://localhost:8080/api-doc](http://localhost:8080/api-doc)
to get the auto generated documents in JSON format.
