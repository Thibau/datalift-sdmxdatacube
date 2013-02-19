define([
  'jquery',
  'knockout',
  'validation'
], function($, ko, validation){
  'use strict';

  var regex = {};
  regex.uri = /(http|ftp|https):\/\/[\w\-_]+([\w\-\.,@?\u005E=%&amp;:/~\+#]*[\w\-\@?^=%&amp;/~\+#])?/;
  //regex.url = /(http|ftp|https):\/\/[\w\-_]+(\.[\w\-_]+)+([\w\-\.,@?^=%&amp;:/~\+#]*[\w\-\@?^=%&amp;/~\+#])?/;
  // Regexes from http://snipplr.com/view/6889/regular-expressions-for-uri-validationparsing/
  //regex.uri = /^([a-z0-9+.-]+):(?://(?:((?:[a-z0-9-._~!$&'()*+,;=:]|%[0-9A-F]{2})*)@)?((?:[a-z0-9-._~!$&'()*+,;=]|%[0-9A-F]{2})*)(?::(\d*))?(/(?:[a-z0-9-._~!$&'()*+,;=:@/]|%[0-9A-F]{2})*)?|(/?(?:[a-z0-9-._~!$&'()*+,;=:@]|%[0-9A-F]{2})+(?:[a-z0-9-._~!$&'()*+,;=:@/]|%[0-9A-F]{2})*)?)(?:\?((?:[a-z0-9-._~!$&'()*+,;=:/?@]|%[0-9A-F]{2})*))?(?:#((?:[a-z0-9-._~!$&'()*+,;=:/?@]|%[0-9A-F]{2})*))?$/i;
  //regex.url = /^(https?):\/\/((?:[a-z0-9.-]|%[0-9A-F]{2}){3,})(?::(\d+))?((?:\/(?:[a-z0-9-._~!$&'()*+,;=:@]|%[0-9A-F]{2})*)*)(?:\?((?:[a-z0-9-._~!$&'()*+,;=:\/?@]|%[0-9A-F]{2})*))?(?:#((?:[a-z0-9-._~!$&'()*+,;=:\/?@]|%[0-9A-F]{2})*))?$/i;

  /**
   * Checks if a string is an URI
   * validator returns true if no validation, if val is empty or if val validates the regex.
   */
  ko.validation.rules['uri'] = {
    validator : function (val, validate) {
      return !validate || !val || regex.uri.test(val);
    },
    message : 'Please enter a proper URI'
  };

  /*
   * Aggregate validation of all the validated properties within an object
   * Parameter: true|false
   * Example:
   *
   * viewModel = {
   *    person: ko.observable({
   *       name: ko.observable().extend({ required: true }),
   *       age: ko.observable().extend({ min: 0, max: 120 })
   *    }.extend({ validObject: true })
   * }
  */
  ko.validation.rules["validObject"] = {
    validator : function (obj, bool) {
      if (!obj || typeof obj !== "object") {
        throw "[validObject] Parameter must be an object";
      }
      return bool === (ko.validation.group(obj)().length === 0);
    },
    message : "Every property of the object must validate to '{0}'"
  };

  ko.validation.rules['remote'] = {
    async : true,
    validator : function (val, params, callback) {
      var defaults = {
          url : 'http://localhost:8080/datalift/sdmxdatacube/validate',
          type : 'POST',
          success : callback,
          error : callback
      };

      var options = $.extend(defaults, params);

      $.ajax(options);
    },
    message : 'Server validation : false'
  };

  ko.validation.registerExtenders();
});
