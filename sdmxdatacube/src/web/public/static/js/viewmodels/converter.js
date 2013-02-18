define([
  'jquery',
  'knockout',
  'config/global',
  'models/Source',
  'models/SourceTransporter'
], function($, ko, g, Source, SourceTransporter){
  'use strict';

  var ViewModel = function(defaultSources, currentSource, viewResults) {
    var self = this;

    // Transform our array of Objects to an array of Sources.
    self.defaultSources = defaultSources.map(function (elt) {
      return new Source(elt.parent, elt.title, elt.uri, elt.uriPattern, elt.creator, elt.project);
    });

    self.currentSource = ko.observable(currentSource);

    self.viewResults = ko.observable(viewResults);

    self.launchConverter = function(form) {
      window.console.log('launch to ' + form.action);

      $.ajax({
         type: form.method,
         url: form.action,
         data: new SourceTransporter(self.currentSource(), self.viewResults()),
         success: function(result) {
            window.console.log('launch success');

            localStorage.removeItem(g.localStorageCurrentSource);
         },
         error: function(req, status, error) {
            window.console.log('launch error');
         }
      });
    };

    self.resetValues = function() {
      window.console.log('reset');
      //TODO self.currentSource(self.defaultSources[0]);
      localStorage.removeItem(g.localStorageCurrentSource);
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
