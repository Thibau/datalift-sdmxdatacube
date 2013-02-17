define([
  'jquery',
  'knockout',
  'config/global',
  'models/Source'
], function($, ko, g, Source){
  'use strict';

  function findValue(array, uri, property) {
    var i = 0;
    var found = false;
    while (!found && i < array.length) {
      found = array[i]['uri'] === uri;
      i++;
    }

    return found ? array[i - 1][property] : '';
  }

  // our main view model
  var ViewModel = function(projectSources, viewResults) {
    var self = this;

    self.projectSources = projectSources;

    self.inputSource = ko.observable(self.projectSources[0]['uri']);

    self.outputSourceTitle = ko.computed(function() {
      return findValue(self.projectSources, self.inputSource(), 'outputTitle');
    });

    self.outputSourceURI = ko.computed(function() {
      return findValue(self.projectSources, self.inputSource(), 'outputURI');
    });

    self.uriPattern = ko.observable(self.projectSources[0]['uriPattern']);

    self.viewResults = ko.observable(viewResults);

    // internal computed observable that fires whenever anything changes in our todos
    ko.computed(function() {
      // store a clean copy to local storage, which also creates a dependency on the observableArray and all observables in each item
      //localStorage.setItem(g.localStorageItem, ko.toJSON(projectSources));
    }).extend({
       // save at most twice per second
      throttle: 500
    });

  };
  return ViewModel;
});
