define([
  'knockout',
  'validation',
  'config/global'
], function(ko, validation, g){
  'use strict';

  ko.validation.rules['ruleName'] = {
    validator: function () {
        return true;
    },
    message: 'This is not good.'
  };

  ko.validation.registerExtenders();
});
