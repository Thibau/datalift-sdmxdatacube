function ConverterViewModel(projectSources, viewResults) {
  var self = this;

  self.projectSources = projectSources;


  self.inputSource = ko.observable(self.projectSources[0]['uri']);

  self.outputSourceTitle = ko.computed(function() {
    return findValue(self.projectSources, self.inputSource(), 'outputTitle');
  });

  self.outputSourceURI = ko.computed(function() {
    return findValue(self.projectSources, self.inputSource(), 'outputURI');
  });

  self.uriPattern = ko.observable(self.projectSources[0]['uriPattern']);

  self.viewResults = ko.observable(viewResults);
}

function findValue(array, uri, property) {
  var i = 0;
  var found = false;
  while (!found && i < array.length) {
    found = array[i]['uri'] === uri;
    i++;
  }

  return found ? array[i - 1][property] : '';
}
