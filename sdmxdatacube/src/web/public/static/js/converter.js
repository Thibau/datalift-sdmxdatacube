function ConverterViewModel(projectSources, viewResults) {
  var self = this;

  self.projectSources = projectSources;
  self.inputSource = ko.observable();

  self.outputSourceName = ko.observable();
  self.outputSourceURI = ko.observable();

  self.uriFormat = ko.observable();

  self.viewResults = ko.observable(viewResults);
}
