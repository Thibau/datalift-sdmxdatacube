define({
  ENTER_KEY: 13,
  // Better than replace location.search, handles hashes too.
  remote : window.location.href.substring(0, window.location.href.indexOf('?')),
  path: {
    validate: '/validate'
  },
  localStorage: {
    current: 'sdmxdatacubeCurrentSource',
    history: 'sdmxdatacubeHistorySource'
  },
  maxHistorySources: 10,
  validationParameters : {
    insertMessages: false,
    decorateElement: true,
    errorElementClass: 'error'
  },
  timeBeforeRedirect : 4
});
