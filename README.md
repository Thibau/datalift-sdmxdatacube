# Datalift — SDMX2DataCube

Version: 0.7
Date: 2013-03-08
Authors: [Thibaud Colas](https://github.com/ThibWeb), [Thibaut Marmin](https://github.com/marminthibaut)
Repository: [GitHub](https://github.com/Thibau/datalift-sdmxdatacube/)

---------------------------------------------------------------------

## tl;dr

SDMX2DataCube is a [Datalift](http://datalift.org/) module which converts [SDMX](http://sdmx.org/) data to RDF using the [DataCube](http://www.w3.org/TR/vocab-data-cube/) ontology.

It was created to fulfill the needs of the Datalift project to work with statistical linked data by two students during a two weeks scholar project.

## How to use it

SDMX2DataCube shouldn't need anything else than Datalift's source code to be compiled, all of the other dependencies being uploaded in its repository.

SDMX2DataCube specifically needs access to Datalift's `core`, `framework` and `incubator` module folders . Those dependencies will be automatically found if the `sdmxdatacube` folder is placed at the root of Datalift's source code, or can be otherwise resolved by placing symbolic links inside `datalift-sdmxdatacube`:

```
  ln -s /Path/to/Datalift/core/ core
  ln -s /Path/to/Datalift/framework/ framework
  ln -s /Path/to/Datalift/incubator/ incubator
```

Please note that in order for the compilation step to execute, the Datalift core and framework must have already been compiled, including the `incubator/query` module.

Once compiled, the module can be used by placing its jar (`sdmxdatacube/dist/sdmxdatacube.jar`) inside Datalift's `modules` directory (`DATALIFT_HOME/modules`), and restarting Datalift.

This process can be automated by using one of the two build scripts : `reload_module.sh` and `live-build.xml` (developer-centric and rather opinionated).

## How is it built

TODO

## What's the module's future

TODO

## External resources

TODO
