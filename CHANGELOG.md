# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

### Added

### Changed

#### Add JWT config and issue support #9

Add ability to issue JWTs for logged-in users in order to access Metalnx associated microservices. This is initially to support pluggable search and notifications. This is for auth when Metalnx is acting as a client for other supporting microservices.

This change requires the addition of several properties in metalnx.properties...



### Removed

