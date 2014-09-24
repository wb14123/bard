
[![Build Status](https://travis-ci.org/wb14123/bard.svg)](https://travis-ci.org/wb14123/bard)

Bard is a wrapper of servlet. Makes it easier to write handlers.

**It is not stable now.**

Why Another Web Framework
---------------

* Annotations in Java could makes code much cleaner.
* Current frameworks is not easy to extend.

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
@Path("/myapp")
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

It will auto get params from url query. You can see more examples later.

The annotations that just used is defined in basic package. You can define your own
annotations easily.

Define Your Own Annotations
--------------

There are there kinds of annotations that could be defined:

* Filter: Filter HTTP request and response.
* Injector: Inject variables into handler method params.
* Adapter: Define which handler should be used to handle this HTTP request.

### Basic

### Filter

You can see the annotations defined in `src/main/java/org/binwang/bard/basic` for examples.

Examples
--------------

See `src/main/java/org/binwang/bard/example` for the whole example.

How to run example:

```
mvn compile
mvn exec:java -Dexec.mainClass="org.binwang.bard.example.Main"
```

This will start a server on port 8080. You can view [http://localhost:8080/api-doc](http://localhost:8080/api-doc)
to get the auto generated documents in JSON format.
