# PPA Deploy

Deploy to PPA Launchpad

1. build application `gradle build`
2. extract gradle build `build/distributions/elephant_<version>.tar`
3. copy `bin` and `lib` to `distributions/debian/`
4. update debian changelog file `distributions/debian/debian/changelog`
5. create debian source `debuild -S`. You have to `cd distributions/debian` first
6. *Optional* sign changes `debsign -k <PGP_KEY_ID> file.changes`
7. upload to PPA `dput ppa:gipn/elephant elephant_<version>_source.changes`

> If you want to only create debian package, `.deb` file, use `debuild -uc -us`