// Require.js allows us to configure shortcut alias
require.config({
  baseUrl : 'http://localhost:1337/static/js',
  paths: {
    jquery: 'libs/jquery-1.9.1.min',
    bootstrap: 'libs/bootstrap-2.3.0.min',
    knockout: 'libs/knockout-2.2.1.min'
  },
  shim: {
      // Set bootstrap dependencies
      'bootstrap' : ['jquery']
  }
});

require([
  'knockout',
  'config/global',
  'viewmodels/converter',
  'extends/handlers',
  'extends/native'
], function(ko, g, ConverterViewModel){
  /*
    Here it is time for some explanation.
    RequireJS modules define their dependencies explicitly,
    and we should here define a dependency to a config/defaults module
    which could contain the default values for our form, put there by Velocity (/ AJAX).
    But Datalift does not widely use RequireJS nor Knockout at the moment,
    so this isn't that bad to use such a half-assed solution.
    -- tl;dr;
    This is a "temporary" fix.
   */
  //'use strict';
  var defaults = inlineDefaults;

  // Check local storage for values.
  var currentSource = ko.utils.parseJson(localStorage.getItem(g.localStorageCurrentSource));

  // Bind a new instance of our view model to the page.
  ko.applyBindings( new ConverterViewModel(defaults.sources, currentSource || null, defaults.viewResults));
});
