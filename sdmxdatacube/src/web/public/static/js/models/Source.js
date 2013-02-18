define([
  'knockout'
], function(ko){
  'use strict';

  var Source = function(parent, title, uri, uriPattern, creator, project) {
    var self = this;

    // Parent is a JS object with properties title and uri.
    self.parent     = ko.observable(parent);
    self.title      = ko.observable(title);
    self.uri        = ko.observable(uri);
    self.uriPattern = ko.observable(uriPattern);
    self.creator    = ko.observable(creator);
    self.project    = ko.observable(project);
  };

  return Source;
});
