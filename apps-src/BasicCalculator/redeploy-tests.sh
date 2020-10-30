 #!/bin/bash
 ./gradlew assembleAndroidTest
 mv ./app/build/outputs/apk/androidTest/debug/app-debug-androidTest.apk app-debug-androidTest.apk 
 abc sign-apk app-debug-androidTest.apk 
 abc install-apk app-debug-androidTest.apk 
