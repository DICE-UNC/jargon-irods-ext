# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

### Added

### Changed

#### Factor out metalnx circular dependencies into abstracted services #5

Factor out generic concepts like stars/shares in a way that they can be used by other code, e.g. REST API or other clients. This corrects some circular dependencies in MetaLnx. metalnx core and services libraries are pulled out into stand-alone packages and then set as dependencies in Metalnx itself.

#### Add JWT config and issue support #9

Add ability to issue JWTs for logged-in users in order to access Metalnx associated microservices. This is initially to support pluggable search and notifications. This is for auth when Metalnx is acting as a client for other supporting microservices.

This change requires the addition of several properties in metalnx.properties...


### Removed

