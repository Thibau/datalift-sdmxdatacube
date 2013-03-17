define([
  'jquery',
  'knockout',
  'config/global'
], function ($, ko, g) {
  'use strict';

  /**
   * The Record prototype takes care of the history
   * of previously converted sources for our viewmodel.
   *
   * It contains an array of sources which is persisted in localStorage.
   */
  var Record = function () {
    var self = this;

    self.previousSources = ko.observableArray();

    /**
     * Initializes the record from localStorage.
     */
    self.initialize = function () {
      var localHistory = (typeof localStorage.getItem(g.localStorage.history) !== "undefined") && ko.utils.parseJson(localStorage.getItem(g.localStorage.history));
      self.previousSources(localHistory || []);
    };

    self.initialize();

    /**
     * Adds a source to the history, also verifying that
     * there aren't too many sources and slicing the array accordingly.
     * @param  {Source} source A Source object.
     */
    self.append = function (source) {
      self.previousSources.push(source);

      var nbSources = self.previousSources().length;
      // Slice the array if it is becoming too big.
      if (nbSources > g.maxHistorySources) {
        self.previousSources(self.previousSources().slice(nbSources - g.maxHistorySources, g.maxHistorySources));
      }
      localStorage.setItem(g.localStorage.history, ko.toJSON(self.previousSources()));
    };

  };

  return Record;
});
