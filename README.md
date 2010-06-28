gwt-persistence
===============

An GWT wrapper of the Javascript object-relational mapper library [persistence.js](http://github.com/zefhemel/persistencejs). It provides client-side object persistence capability to GWT applications, a feature similar to what Hibernate provides for GWT applications on the server-side.

Schema definition
-----------------

The schema is defined by declaring an interface for each entity. The interface needs to extend the Persistable marker interface. 
