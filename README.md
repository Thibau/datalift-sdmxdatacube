# Datalift — SDMX2DataCube

Version: 0.8  
Date: 2013-03-08  
Authors: [Thibaud Colas](https://github.com/ThibWeb), [Thibaut Marmin](https://github.com/marminthibaut)  
Repository: [GitHub](https://github.com/Thibau/datalift-sdmxdatacube/)  
Documentation: [GitHub Wiki](https://github.com/Thibau/datalift-sdmxdatacube/wiki)  

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

SDMX2DataCube is a Datalift module, thus uses the [Sesame](http://openrdf.org/) triplestore, the Jersey JAX-RS implementation and [GSON](https://code.google.com/p/google-gson/).

This module is designed to feel snappy to use, the first request loads the whole interface and every subsequent call is full AJAJ.

The frontend is built with [Twitter Bootstrap](http://twitter.github.com/bootstrap/), [KnockoutJS](http://knockoutjs.com/) and [RequireJS](http://requirejs.org/). We use RequireJS to help organize the JavaScript code, separating it into modules.

The conversion engine relies on the SDMXRDFParser library, which is based on SDMXSource and [Spring](http://www.springsource.org/).

### Points of interest

* This module is built with strict mode enabled ("use strict").
* JSON exchanges format are defined with [JSON Schema](http://json-schema.org/).
* [Knockout Validation](https://github.com/ericmbarnard/Knockout-Validation) is used to manage form validation (client- and server-side).
* This module shares a small common code base (via inheritance) with the StringToURI module.
* Due to the integration of the SDMXRDFParser library, which requires Spring, this module is quite heavy (± 50Mo).

## What's the module's future

TODO

## External resources

TODO
