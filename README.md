Eclipse GlassFish
=================

Eclipse GlassFish is a compatible implementation of Jakarta EE.

Building
--------

Prerequisites:

* JDK8+
* Maven 3.0.3+

Run the full build:

`mvn clean install`

Locate the Zip distributions:
- appserver/distributions/glassfish/target/glassfish.zip
- appserver/distributions/web/target/web.zip

Locate staged distributions:
- appserver/distributions/glassfish/target/stage
- appserver/distributions/web/target/stage

Testing
--------
Running GlassFish QuickLook tests:

`mvn -f appserver/tests/quicklook/pom.xml test -Dglassfish.home={fullpath}/appserver/distributions/glassfish/target/stage/glassfish6/glassfish`

For more details, see [QuickLook_Test_Instructions](https://github.com/eclipse-ee4j/glassfish/blob/master/appserver/tests/quicklook/QuickLook_Test_Instructions.html)

Starting GlassFish
------------------

`glassfish6/bin/asadmin start-domain`
`glassfish6/bin/asadmin start-domain --verbose --debug` to run with logging in a console window

To access the administration console use http://127.0.0.1:4848 

Stopping GlassFish
------------------

`glassfish6/bin/asadmin stop-domain`

Jenkins
-------
Eclipse Jenkins instance for the project is here https://ci.eclipse.org/glassfish/

FOr further  information see the project website https://eclipse-ee4j.github.io/glassfish/

