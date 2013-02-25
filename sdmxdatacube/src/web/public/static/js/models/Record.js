define([
  'jquery',
  'knockout',
  'config/global'
], function($, ko, g){
  'use strict';

  var Record = function() {
    var self = this;

    self.previousSources = ko.observableArray();

    self.initialize = function() {
      var localHistory = ko.utils.parseJson(localStorage.getItem(g.localStorageHistorySources));
      self.previousSources(localHistory || []);
    };

    self.initialize();

    self.append = function(source) {
      self.previousSources.push(source);

      var nbSources = self.previousSources().length;
      // Slice the array if it is becoming too big.
      if (nbSources > g.maxHistorySources) {
        self.previousSources(self.previousSources().slice(nbSources - g.maxHistorySources, g.maxHistorySources));
      }
      localStorage.setItem(g.localStorageHistorySources, ko.toJSON(self.previousSources()));
    };

  };

  return Record;
});
