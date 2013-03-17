define([
  'jquery',
  'knockout',
  'config/global',
  'models/Source',
  'models/SourceTransporter',
  'models/State',
  'models/Record'
], function ($, ko, g, Source, SourceTransporter, State, Record) {
  'use strict';

  /**
   * A view model which describes how our converter works,
   * what values it manages.
   * @param {Array} rawSources     An array which contains POJOs describing sources.
   * @param {Bool} viewResults     Tells whether or not the user wants to view results.
   */
  var ConverterViewModel = function (rawSources, viewResults) {
    var self = this;


    self.rawSources    = rawSources;
    self.sources       = [];
    self.currentSource = ko.observable();
    self.viewResults   = ko.observable(viewResults);
    self.state         = new State();
    self.record        = new Record();

    /*
    TODO :
    - Add extract display from SPARQL query when viewResult is true (models/State.js/extract)
    - Add history management (models/Record.js)
    */

    /**
     * Initializes the converter from the raw sources.
     * @param  {Object} currentSource A POJO which represents a source.
     */
    self.initialize = function (fromLocal) {
      // Transform our array of Objects to an array of Sources.
      self.sources = self.rawSources.map(function (elt) {
        return new Source(elt.parent, elt.project, elt.title, elt.uri, elt.uriPattern, elt.creator, elt.created);
      });

      // First test if we want to use localStorage, then if there is something in it, finally retrieve the item.
      var localCurrentSource = fromLocal && (typeof localStorage.getItem(g.localStorage.current) !== "undefined") && ko.utils.parseJson(localStorage.getItem(g.localStorage.current));
      self.currentSource(localCurrentSource || self.sources[0]);
    };

    self.initialize(true);

    /**
     * Executes an AJAJ call to send a source to the server.
     * @param  {Object} form The form which was submitted.
     */
    self.launch = function (form) {
      self.state.launchingStart();

      $.ajax({
         type: form.method,
         url: form.action,
         data: new SourceTransporter(self.currentSource(), self.viewResults()),
         success: function (data, status, jqxhr) {
            self.state.launchingSuccess(jqxhr.getResponseHeader('Location'), self.viewResults());

            self.record.append(self.currentSource());
            localStorage.removeItem(g.localStorage.current);
         },
         error: function (jqxhr, status, error) {
            self.state.launchingError(jqxhr.responseText);
         }
      });
    };

    /**
     * Resets the values of our sources by calling the initialization function again.
     */
    self.reset = function () {
      self.initialize(false);
      localStorage.removeItem(g.localStorage.current);
    };

    // Internal computed observable that fires whenever anything changes.
    ko.computed(function () {
      // Store a clean copy to local storage.
      localStorage.setItem(g.localStorage.current, ko.toJSON(self.currentSource()));
    }).extend({
       // Save at most twice per second.
      throttle: 500
    });
  };

  return ConverterViewModel;
});
