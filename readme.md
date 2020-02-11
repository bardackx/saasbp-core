# SAASBP - Core

Software as a service boilerplate project; it was created to try a clean architecture approach as described in the book **Get Your Hands Dirty on Clean Architecture** by Tom Hombergs. And also to try unit testing with the mockito library.

## Core only, not a standalone project

This project is meant to be used by an *adapter* project. 

### An adapter project

An adapter project would be a project with controllers (or job, or message handler) classes that call the services from the **application.service** packages, data provider classes that implements the interfaces from the **application.port.out** packages, and configuration classes that wire all together.

For example this could be a Spring project.