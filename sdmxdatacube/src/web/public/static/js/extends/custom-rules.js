define([
  'knockout',
  'validation'
], function(ko, validation){
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
    validator: function (val, validate) {
      return !validate || !val || regex.uri.test(val);
    },
    message: 'Please enter a proper URI'
  };

  ko.validation.registerExtenders();
});
