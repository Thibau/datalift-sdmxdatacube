# Datalift — SDMX2DataCube

Version: 0.8  
Date: 2013-03-23  
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

This module will (hopefully) soon be integrated as part of Datalift's main modules repository and further developed to better integrate with its conversion engine. Its interface will (hopefully) better answer the needs of its main users. A better integration of MagicSuggest is also necessary.

A note about integration with SDMXRDFParser is available [here (french)](https://github.com/Thibau/datalift-sdmxdatacube/wiki/Int%C3%A9gration-avec-SDMXRDFParser).

## External resources

### General

- http://datalift.org/
- http://sdmx.org/
- http://www.w3.org/TR/vocab-data-cube/
- https://webgate.ec.europa.eu/fpfis/mwikis/sdmx/
- http://eurostat.linked-statistics.org/
- http://csarven.ca/linked-sdmx-data

### Client

- http://twitter.github.com/bootstrap/
- http://nicolasbize.github.com/magicsuggest/
- http://knockoutjs.com/
- http://requirejs.org
- http://jquery.com/

### Server

- http://openrdf.org/
- http://www.sdmxsource.org/
- http://jersey.java.net/
- https://code.google.com/p/google-gson/
