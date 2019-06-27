# knime-silicosit

KNIME nodes and example workflows for software made by [Silicos-it](http://silicos-it.be.s3-website-eu-west-1.amazonaws.com/index.html) like shape-it to align molecules based on shape and align-it to align molecules based on their pharmacophore.

[![Build Status Linux & OSX](https://travis-ci.org/3D-e-Chem/knime-silicos-it.svg?branch=master)](https://travis-ci.org/3D-e-Chem/knime-silicos-it)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=nl.esciencecenter.e3dchem.knime.silicosit%3Anl.esciencecenter.e3dchem.knime.silicosit&metric=alert_status)](https://sonarcloud.io/dashboard?id=nl.esciencecenter.e3dchem.knime.silicosit%3Anl.esciencecenter.e3dchem.knime.silicosit)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=nl.esciencecenter.e3dchem.knime.silicosit%3Anl.esciencecenter.e3dchem.knime.silicosit&metric=coverage)](https://sonarcloud.io/dashboard?id=nl.esciencecenter.e3dchem.knime.silicosit%3Anl.esciencecenter.e3dchem.knime.silicosit)
[![DOI](https://zenodo.org/badge/DOI/10.5281/zenodo.597793.svg)](https://doi.org/10.5281/zenodo.597793)

This project uses [Eclipse Tycho](https://www.eclipse.org/tycho/) to perform build steps.

# Installation

Requirements:

* KNIME, https://www.knime.org, version 4.0 or higher

Steps to get the Silicos-it KNIME node inside KNIME:

1. Goto Help > Install new software ... menu
2. Press add button
3. Fill text fields with `https://3d-e-chem.github.io/updates`
4. Select --all sites-- in `work with` pulldown
5. Select the node
6. Install software
7. Restart KNIME

# Usage

1. Create a new KNIME workflow.
2. Find node in Node navigator panel.
3. Drag node to workflow canvas.

# Examples

The `examples/` folder contains example KNIME workflows.

# Build

```
mvn verify
```

An Eclipse update site will be made in `p2/target/repository` directory.
The update site can be used to perform a local installation.

## Continuous Integration

Configuration files to run Continuous Integration builds on Linux (Travis-CI), OS X (Travis-CI) are present.

See `./.travis.yml` file how to trigger a Travis-CI build for every push or pull request.

# Development

Steps to get development environment setup based on https://github.com/knime/knime-sdk-setup#sdk-setup:

1. Install Java 8
2. Install Eclipse for [RCP and RAP developers](https://www.eclipse.org/downloads/packages/release/2018-12/r/eclipse-ide-rcp-and-rap-developers)
3. Configure Java 8 inside Eclipse Window > Preferences > Java > Installed JREs
4. Import this repo as an Existing Maven project
5. Activate target platform by going to Window > Preferences > Plug-in Development > Target Platform and check the `KNIME Analytics Platform (4.0) - nl.esciencecenter.e3dchem.knime.silicosit.targetplatform/KNIME-AP-4.0.target` target definition.

During import the Tycho Eclipse providers must be installed.

## Meta nodes

This plugin uses metanodes as it's public nodes. The are created in the following way:

1. The meta nodes are first created and tested inside the example workflows in the `examples/` directory.
2. The `name` and `customDescription` field inside `examples/**/workflow.knime` is filled.
3. The examples are fully run and committed
4. The meta nodes are internally completely reset, so we don't ship public nodes with example data in them.
5. The meta nodes from the example workflows are then copied to the `plugin/src/knime/` directory.
6. The meta nodes are added to the `plugin/plugin.xml` as PersistedMetaNode in the `org.knime.workbench.repository.metanode` extension.
7. The examples are checked-out to their fully run state.

## Tests

Tests for the node are in `tests/src` directory.
Tests can be executed with `mvn verify`, they will be run in a separate KNIME environment.
Test results will be written to `test/target/surefire-reports` directory.
Code coverage reports (html+xml) can be found in the `tests/target/jacoco/report/` directory.

There are no tests for the meta nodes as they are copied from the plugin to a workflow each time, which would make the test test itself. 

### Unit tests

Unit tests written in Junit4 format can be put in `tests/src/java`.

### Workflow tests

See https://github.com/3D-e-Chem/knime-testflow#3-add-test-workflow

## Speed up builds

Running mvn commands can take a long time as Tycho fetches indices of all p2 update sites.
This can be skipped by running maven offline using `mvn -o`.

# New release

1. Update versions in pom files with `mvn org.eclipse.tycho:tycho-versions-plugin:set-version -DnewVersion=<version>-SNAPSHOT` command.
2. Create package with `mvn package`, will create update site in `p2/target/repository`
3. Run tests with `mvn verify`
4. Optionally, test node by installing it in KNIME from a local update site
5. Append new release to an update site
  1. Make clone of an update site repo
  2. Append release to the update site with `mvn install -Dtarget.update.site=<path to update site>`
6. Commit and push changes in this repo and update site repo.
7. Create a GitHub release
8. Update Zenodo entry
  1. Correct authors
  2. Correct license
9. Make nodes available to 3D-e-Chem KNIME feature by following steps at https://github.com/3D-e-Chem/knime-node-collection#new-release

