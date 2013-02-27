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
        minLength: 5,
        uri : true
      },
      uriPattern : {
        required: true,
        minLength: 3
      }
    }
  };
});


