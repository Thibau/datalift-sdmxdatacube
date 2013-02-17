define([
  'knockout'
], function(ko){
  'use strict';

  var Source = function() {
    var parent = ko.observable();

    var title = ko.observable();
    var uri = ko.observable();

    var uriPattern = ko.observable();
  };

  return Source;
});
