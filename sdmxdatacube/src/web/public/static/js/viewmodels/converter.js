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

  var ViewModel = function(defaultSources, viewResults) {
    var self = this;

    // Transform our array of Objects to an array of Sources.
    self.defaultSources = ko.observableArray(defaultSources.map(function (elt) {
      return new Source(elt.parent, elt.title, elt.uri, elt.uriPattern, elt.creator);
    }));

    console.log(defaultSources[0].parent.title);
    console.log(defaultSources[0].parent.uri);

    self.currentSource = ko.observable();

    self.viewResults = ko.observable(viewResults);

    // Internal computed observable that fires whenever anything changes.
    ko.computed(function() {
      // Store a clean copy to local storage.
      //localStorage.setItem(g.localStorageItem, ko.toJSON(projectSources));
    }).extend({
       // Save at most twice per second.
      throttle: 500
    });

  };
  return ViewModel;
});
