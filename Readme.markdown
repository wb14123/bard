
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

Basic Structures
--------------

In web development, we write handlers to handle HTTP request: read the request, and write response to client. But some more structures could make things easier:

### Handler

### Filter

### Injector

### Adapter


Examples
--------------

See `src/main/java/org/binwang/bard/example` for examples.

How to run example:

```
mvn compile
mvn exec:java -Dexec.mainClass="org.binwang.bard.example.Main"
```
