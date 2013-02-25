define({
  ENTER_KEY: 13,
  // Better than replace location.search, handles hashes too.
  remoteValidatorURL : window.location.href.substring(0, window.location.href.indexOf('?')) + '/validate',
  localStorageCurrentSource: 'localStorageCurrentSource',
  localStorageHistorySources: 'localStorageHistorySources',
  maxHistorySources: 10,
  validationParameters : {
    insertMessages: false,
    decorateElement: true,
    errorElementClass: 'error'
  },
  timeBeforeRedirect : 4
});
