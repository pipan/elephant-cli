# PPA Deploy

Deploy to PPA Launchpad

1. build application `gradle build`
2. extract gradle build `distributions/elephant_0.0.1.tar`
3. copy `bin` and `lib` to `distributions/debian/`
4. update debian changelog file `distributions/debian/debian/changelog`
5. update debian version `distributions/debian/debian/control`
6. create debian source `debuild -S`
7. sign changes `debsign -k <PGP_KEY_ID> file.changes`
8. upload to PPA `dput ppa:gipn/elephant file.changes`

> If you want to only create debian package `.deb.` file use `debuild -uc -us`