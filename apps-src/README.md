# Generating a makefile

In order to generate a new makefile for an application or update an existing one, we can call `abc make <root dir of the application>`. This is gonna generate a new makefile under the root directory of the application. It uses the template that is located under [scripts/make-template.make](https://gitlab.infosun.fim.uni-passau.de/gambi/action-based-test-carving/-/tree/master/scripts/make-template.make). The template is the right file to do updates in order for the changes to be propagated to other apps, too. 


## Makefile properties
`abc make ...` expects an `abc-apk-config` to exist under the root directory of an application. This config file contains all app-specific data which make needs to know, e.g., path to the built apks or package name. To create one for another application, refer to [apps-src/BasicCalculator/abc-apk-config](https://gitlab.infosun.fim.uni-passau.de/gambi/action-based-test-carving/-/blob/master/apps-src/BasicCalculator/abc-apk-config)