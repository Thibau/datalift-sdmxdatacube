require.config({
  // Prod/Dev : baseUrl : 'http://localhost:1337/static/js',
  baseUrl : 'sdmxdatacube/static/js',
  // Require.js allows us to configure shortcut alias
  paths : {
    jquery : 'libs/jquery-1.9.1.min',
    bootstrap : 'libs/bootstrap-2.3.0.min',
    knockout : 'libs/knockout-2.2.1.min',
    validation : 'libs/knockout.validation-1.0.2.min',
    magicsuggest : 'libs/magicsuggest-1.2.3-min'
  },
  shim : {
    // Set dependencies.
    'bootstrap' : ['jquery'],
    'validation' : ['knockout'],
    'extends/custom-rules' : ['validation'],
    'magicsuggest' : ['jquery']
  }
});

require([
  'knockout',
  'config/global',
  'config/translation',
  'viewmodels/converter',
  'extends/handlers',
  'extends/native',
  'bootstrap',
  'validation'
], function (ko, g, i18n, ConverterViewModel, validation) {
  'use strict';
  /*
    Here it is time for some explanation.
    RequireJS modules define their dependencies explicitly,
    and we should here define a dependency to a config/parameters module
    which could contain the default values for our form, put there by Velocity (/ AJAX).
    But Datalift does not widely use RequireJS nor Knockout at the moment,
    so this isn't that bad to use such a half-assed solution.
    -- tl;dr;
    This is a "temporary" fix.
   */
  var parameters = window.inlineParameters;

  // Configure the validation to use parameters defined in global.
  ko.validation.configure(g.validationParameters);

  // Translate validation to current language.
  if (parameters.language === 'fr' || parameters.language === 'it' || parameters.language === 'es') {
    ko.validation.localize(i18n.validation[parameters.language]);
  }

  // Bind a new instance of our view model to the page.
  ko.applyBindings(new ConverterViewModel(parameters.defaults.sources, parameters.defaults.viewResults));
});
