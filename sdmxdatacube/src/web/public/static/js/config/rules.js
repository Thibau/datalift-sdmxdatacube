define([
  'knockout',
  'validation',
  'extends/custom-rules'
], function(ko, validation) {
  return {
    source : {
      title : {
      required: true,
      minLength: 3
      },
      uri : {
        required: true,
        minLength: 3
      },
      uriPattern : {
        required: true,
        minLength: 3
      },
      project : {
        required: true
      }
    }
  };
});


