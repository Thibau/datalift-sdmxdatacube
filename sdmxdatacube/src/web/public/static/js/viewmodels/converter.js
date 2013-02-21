define([
  'jquery',
  'knockout',
  'config/global',
  'models/Source',
  'models/SourceTransporter',
  'validation'
], function($, ko, g, Source, SourceTransporter, validation){
  'use strict';

  var ViewModel = function(rawSources, currentSource, viewResults) {
    var self = this;


    self.rawSources    = rawSources;
    self.sources       = [];
    self.currentSource = ko.observable();
    self.viewResults   = ko.observable(viewResults);

    self.state = {
      isProcessing     : ko.observable(false),
      isConfirming     : ko.observable(false),
      isViewingErrors  : ko.observable(false),
      errors           : ko.observableArray()
    };

    /*
    TODO :
    - Add comments for state
    - Add comments for launch
    - Add restoring state with localStorage
    - Divide source.project into title, uri
    - Write and use tooltip and popover bindings
    - Remove observable on project
    - Add comments to ViewModel
    - Verify how localStorage is kept updated
    - Handle progress bar
     */
    // http://localhost:8080/datalift/sparql/describe?uri=http%3A%2F%2Flocalhost%3A8080%2Fdatalift%2Fproject%2Ftest1%2Fsource%2Fagequinquennal6809-donnees-21-qb&type=Graph&default-graph=internal&max=500

    /**
     * Initializes the converter from the raw sources.
     * @param  {Object} currentSource A POJO which represents a source.
     */
    self.initialize = function(currentSource) {
      // Transform our array of Objects to an array of Sources.
      self.sources = self.rawSources.map(function (elt) {
        return new Source(elt.parent, elt.title, elt.uri, elt.uriPattern, elt.creator, elt.created, elt.project);
      });

      self.currentSource(currentSource || self.sources[0]);
    };

    self.initialize(currentSource);

    self.currentSource.extend({
      validObject : true,
      remote : {
        // TODO Update data with value of currentSource at call time.
        data : new SourceTransporter(self.currentSource(), self.viewResults())
      }
    });


    self.launch = function(form) {
      window.console.log('launch');

      self.state.isProcessing(true);
      self.state.isConfirming(false);
      self.state.errors.removeAll();

      $.ajax({
         type: form.method,
         url: form.action, // + '/validate',
         data: new SourceTransporter(self.currentSource(), self.viewResults()),
         success: function(data, status, jqxhr) {
            window.console.log('launch success');
            window.console.log(data);
            localStorage.removeItem(g.localStorageCurrentSource);
         },
         error: function(jqxhr, status, error) {
            self.state.isProcessing(false);
            self.state.isViewingErrors(true);
            self.state.errors.push(jqxhr.responseText);

            window.console.log('launch error');
            window.console.log(jqxhr.responseText);
            window.console.log(JSON.parse(jqxhr.responseText));
         },
         complete: function () {

         }
      });
    };

    /**
     * Resets the values of our sources by calling the initialization function again.
     * @return nothing.
     */
    self.reset = function() {
      self.initialize(null);
      localStorage.setItem(g.localStorageCurrentSource, ko.toJSON(self.currentSource()));
    };

    // Internal computed observable that fires whenever anything changes.
    ko.computed(function() {
      // Store a clean copy to local storage.
      localStorage.setItem(g.localStorageCurrentSource, ko.toJSON(self.currentSource()));
    }).extend({
       // Save at most twice per second.
      throttle: 500
    });
  };
  return ViewModel;
});
