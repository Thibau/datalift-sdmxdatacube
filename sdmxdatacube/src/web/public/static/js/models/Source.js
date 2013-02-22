define([
  'knockout',
  'validation',
  'config/rules'
], function(ko, validation, rules){
  'use strict';

  /**
   * A Source is a dataset which comes either from a file, DB, endpoint.
   * Here it is mostly a not-yet-created DataCube source which
   * comes from a SDMX (XML) parent.
   * All of its properties are KO observables except parent.
   * @param {Object} parent     POJO with properties title and uri.
   * @param {Object} project    Which project is the source attached to. Properties title and uri.
   * @param {String} title      Label of the source. Unique. Generated by default.
   * @param {String} uri        URI to identify the source and its graph. Unique.
   * @param {String} uriPattern A pattern to be used by the URIs inside the source.
   * @param {String} creator    Who created the source.
   * @param {Date} created      When this source was created.
   */
  var Source = function(parent, project, title, uri, uriPattern, creator, created) {
    var self = this;

    // Parent is a JS object with properties title and uri.
    self.parent     = parent;
    // Project is also a JS object with properties title and uri.
    self.project    = project;
    self.title      = ko.observable(title).extend(rules.source.title);
    self.uri        = ko.observable(uri).extend(rules.source.uri);
    self.uriPattern = ko.observable(uriPattern).extend(rules.source.uriPattern);
    self.creator    = ko.observable(creator);
    self.created    = ko.observable(created);
  };

  return Source;
});
