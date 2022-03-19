# PPA Deploy

Deploy to PPA Launchpad

1. update application version `build.gradle.kts`
2. build application `gradle build`
3. extract gradle build `build/distributions/elephant_<version>.tar`
4. copy `bin` and `lib` to `distributions/debian/`
5. update debian changelog file `distributions/debian/debian/changelog`
6. create debian source `debuild -S`. You have to `cd distributions/debian` first
7. *Optional* sign changes `debsign -k <PGP_KEY_ID> file.changes`
8. upload to PPA `dput ppa:gipn/elephant elephant_<version>_source.changes` from `distributions` directory. You have to `cd ../`.

> If you want to only create debian package, `.deb` file, use `debuild -uc -us`