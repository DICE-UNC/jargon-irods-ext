
Jargon Extensions API for irods-ext


# Project: Jargon Extensions API - IRODS-EXT
### Date: 07/05/2018
### Release Version: 4.3.1.0-SNAPSHOT
### git tag:

## News

Extended support in Jargon for advanced iRODS mid-tier capabilities using the IRODS-EXT database.  This package is an implementation of the
original jargon-extensions reference implementation. The prior version was associated with the cloud browser, this follow-on implements the
same extension interfaces to track MetaLnx, and will attempt to provide a common API that bridges several projects, giving out-of-the-box baseline functionality.

## Requirements

* Jargon depends on Java 1.8+
* Jargon is built using Apache Maven2, see POM for dependencies
* Jargon supports iRODS 3.0 through iRODS 3.3.1 community, as well as iRODS 4.2.2 consortium

## Libraries

Jargon-core uses Maven for dependency management.  See the pom.xml file for references to various dependencies.

Note that the following bug and feature requests are logged in GitHub with related commit information [[https://github.com/DICE-UNC/jargon/issues]]

## Changes

#### Factor out metalnx circular dependencies into abstracted services #5

Factor out generic concepts like stars/shares in a way that they can be used by other code, e.g. REST API or other clients. This corrects some circular dependencies in MetaLnx. metalnx core and services libraries are pulled out into stand-alone packages and then set as dependencies in Metalnx itself.
