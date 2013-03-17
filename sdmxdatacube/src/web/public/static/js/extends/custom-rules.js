define([
  'jquery',
  'knockout',
  'validation',
  'config/global'
], function ($, ko, validation, g) {
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

  /**
   * Aggregate validation of all the validated properties within an object
   * Parameter: true|false
   *
   */
  ko.validation.rules["validObject"] = {
    validator : function (obj, validate) {
      if (!obj || typeof obj !== "object") {
        throw "[validObject] Parameter must be an object";
      }

      return validate === (ko.validation.group(obj)().length === 0);
    },
    message : 'Every field must be valid.'
  };

  /**
   * Remote validation rule, allowing the server to seamlessly participate
   * in validating the values.
   *
   * Might come in handy : https://github.com/ericmbarnard/Knockout-Validation/pull/192
   */
  ko.validation.rules['remote'] = {
    async : true,
    validator : function (val, params, callback) {

      var defaults = {
          url : g.remote + g.path.validate,
          type : 'POST',
          accept : 'application/json',
          // The data needs to be set at execution time with beforeSend.
          data : {},
          success : function (data, status, jqxhr) {
            callback(data.valid);
          },
          error : function (jqxhr, status, error) {
            var result = JSON.parse(jqxhr.responseText);

            callback({
              isValid: result.isValid,
              message: result.global
            });
          }
      };

      var options = $.extend(defaults, params);

      $.ajax(options);

    },
    message : 'Remote validation failed'
  };

  ko.validation.registerExtenders();
});
