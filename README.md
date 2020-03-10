[![Build Status](https://travis-ci.org/bunpro-srs/BunproAndroidApp.svg?branch=master)](https://travis-ci.org/bunpro-srs/BunproAndroidApp)
[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=bunpro-srs&metric=reliability_rating)](https://sonarcloud.io/dashboard?id=bunpro-srs)
# Bunpro Android App

This is the Android application for the [Bunpro](https://bunpro.jp/) japanese grammar learning service.

## Testing (deprecated)

*Instrumentation tests were too costly to maintain, so they are no longer in use. The following should still work for future implementation, it was used with Bitrise CI tool.*

A few instrumentation tests had been created, although they are now discontinued. Instrumentation tests
launch the application like it should on an android device and test relevant features.

If you'd like to launch the instrumentation tests, you can type in your shell the following command to test everything:
"test_bunpro_login=*LOGIN* test_bunpro_password=*PASSWORD* ./gradlew connectedAndroidTest"
Or you can test only a specific class: "test_bunpro_login=*LOGIN* test_bunpro_password=*PASSWORD* ./gradlew connectedAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=bunpro.jp.bunproapp.LoginActivityTest"

## Contributing

The project use the [Github issues](https://github.com/bunpro-srs/BunproAndroidApp/issues) to track down the bugs and log upcoming features.
Feel free to report the bugs you discover; we are also open to pull requests.  

The project is currently suffering many bugs that need to be addressed.  
One of which is caused by updating the dependencies: one of those is causing the application to crash, so that's why I rolled back the versions to what they were before.  

## License

This app is released under the MIT License. See LICENSE for details.
