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

    self.rawSources = rawSources;
    self.sources = [];
    self.currentSource = ko.observable();

    self.viewResults = ko.observable(viewResults);

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
        data : new SourceTransporter(self.currentSource(), self.viewResults())
      }
    });

    self.launch = function(form) {
      window.console.log('launch');

      $.ajax({
         type: form.method,
         url: form.action, // + '/validate',
         data: new SourceTransporter(self.currentSource(), self.viewResults()),
         success: function(result) {
            window.console.log('launch success');
            window.console.log(result);
            localStorage.removeItem(g.localStorageCurrentSource);
         },
         error: function(req, status, error) {
            window.console.log('launch error');
            window.console.log(status);
            window.console.log(error);
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
