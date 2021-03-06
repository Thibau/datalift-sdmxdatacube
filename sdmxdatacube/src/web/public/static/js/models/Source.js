define([
  'jquery',
  'knockout',
  'validation',
  'config/rules',
  'config/global',
  'models/SourceTransporter',
  'extends/custom-rules'
], function ($, ko, validation, rules, g, SourceTransporter) {
  'use strict';

  /**
   * A Source is a dataset which comes either from a file, DB, endpoint.
   * Here it is mostly a not-yet-created DataCube source which
   * comes from a SDMX (XML) parent.
   * All of its properties are KO observables except parent and project.
   * @param {Object} parent     POJO with properties title and uri.
   * @param {Object} project    Which project is the source attached to. Properties title and uri.
   * @param {String} title      Label of the source. Unique. Generated by default.
   * @param {String} uri        URI to identify the source and its graph. Unique.
   * @param {String} uriPattern A pattern to be used by the URIs inside the source.
   * @param {String} creator    Who created the source.
   * @param {Date} created      When this source was created.
   */
  var Source = function (parent, project, title, uri, uriPattern, creator, created) {
    var self = this;

    // Parent is a JS object with properties title and uri.
    // Project is also a JS object with properties title and uri.
    self.parent     = parent;
    self.project    = project;
    self.title      = ko.observable(title);
    self.uri        = ko.observable(uri);
    self.uriPattern = ko.observable(uriPattern);
    self.creator    = ko.observable(creator);
    self.created    = ko.observable(created);

    var validated = ['title', 'uri', 'uriPattern'];

    /*
      Those function are all kind of stubs for a knockout validation object.
      This is a bit hacky, but still seems the best way to remotely validate each field.
      To perform it the right way, one would have to extend self with validObject,
      thus meaning that each validatable inside self must be valid.
      But this only works for sync rules, whereas remote validation is async by nature.
     */
    var generateRules = function (field) {
      // Client rules, then server rules.
      self[field].extend(rules.source[field]);
      self[field].extend({
        remote : {
          // Only remote validate if all fields are completed here and at least one of them has been modified.
          // TODO Beware the end of this line.
          // The problem is : isModified returns the status of the item since its initialization value, not since its last value.
          onlyIf : function () {return self[field]() && self[field].isModified() && (self[field]() !== ko.utils.parseJson(localStorage.getItem(g.localStorage.current))[field]);},
          params: {
            beforeSend : function (jqxhr, settings) {
              var parameterString = '';
              // Here, settings.data is already 'application/x-www-form-urlencoded'
              // Thus we need to append our URL encoded values to the string.
              $.each(new SourceTransporter(ko.toJS(self), true), function (key, val) {
                parameterString += '&' + key + '=' + encodeURIComponent(val);
              });
              settings.data = parameterString.substring(1);
              // If not explicitely overriden here, content-type will be set to text/plain.
              jqxhr.setRequestHeader('Content-Type','application/x-www-form-urlencoded; charset=UTF-8');
            }
          }
        }
      });
    };

    // title, uri and uriPattern have validation rules.
    validated.forEach(generateRules);

    self.isValid = function () {
      return validated.every(function (elt) {return self[elt].isValid();});
    };

    self.isValidating = function () {
      return validated.some(function (elt) {return self[elt].isValidating();});
    };
  };

  return Source;
});
