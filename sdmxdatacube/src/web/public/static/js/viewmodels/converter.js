define([
  'jquery',
  'knockout',
  'config/global',
  'models/Source'
], function($, ko, g, Source){
  'use strict';

  // function findValue(array, uri, property) {
  //   var i = 0;
  //   var found = false;
  //   while (!found && i < array.length) {
  //     found = array[i]['uri'] === uri;
  //     i++;
  //   }

  //   return found ? array[i - 1][property] : '';
  // }

  var ViewModel = function(defaultSources, currentSource, viewResults) {
    var self = this;

    // Transform our array of Objects to an array of Sources.
    self.defaultSources = ko.observableArray(defaultSources.map(function (elt) {
      return new Source(elt.parent, elt.title, elt.uri, elt.uriPattern, elt.creator, elt.project);
    }));

    self.currentSource = ko.observable(currentSource);

    self.viewResults = ko.observable(viewResults);

    // Internal computed observable that fires whenever anything changes.
    ko.computed(function() {
      // Store a clean copy to local storage.
      localStorage.setItem(g.localStorageCurrentSource, ko.toJSON(self.currentSource()));
    }).extend({
       // Save at most twice per second.
      throttle: 500
    });

    self.resetValues = function() {
      window.console.log('reset');
      localStorage.removeItem(g.localStorageCurrentSource);
    };

    self.launchConverter = function(data, event) {
      window.console.log('launch');

      var parameters = {};

      $.ajax({
         type: "POST",
         url: '/',
         data: parameters,
         success: function(result) {
            window.console.log('launch success');

            localStorage.removeItem(g.localStorageCurrentSource);
         },
         error: function(req, status, error) {
            window.console.log('launch error');
         }
      });
    };

  };
  return ViewModel;
});
