define([
  'jquery',
  'knockout',
  'config/global'
], function ($, ko, g) {
  'use strict';

  /**
   * An application state prototype used to delegate
   * the view model's keeping track duty.
   * For example, it is used to manage modals to display
   * when async communications with the server are engaged.
   */
  var State = function () {
    var self = this;

    // Those are the states of the converter at a given time.
    self.isProcessing  = ko.observable(false);
    self.isConfirming  = ko.observable(false);
    self.isRedirecting = ko.observable(false);
    self.timeLeft      = ko.observable(0);
    self.isError       = ko.observable(false);
    self.isSuccess     = ko.observable(false);
    self.error         = ko.observable();
    self.result        = ko.observable();
    self.extract       = ko.observable();

    self.launchingStart = function () {
      self.isProcessing(true);
      self.isConfirming(false);
      self.error(null);
      self.result(null);
    };

    self.launchingSuccess = function (location, viewResults) {
      self.isProcessing(false);
      self.isSuccess(true);
      self.result(location);
      if (viewResults) {
        // self.retrieveExtract(location);
      }
      else {
        self.redirectingStart(location);
      }
    };

    self.launchingError = function (error) {
      self.isProcessing(false);
      self.isError(true);
      self.error(error);
    };

    // Redirect to location after timeBeforeRedirect milliseconds.
    self.redirectingStart = function (location) {
      self.isRedirecting(true);
      self.timeLeft(g.timeBeforeRedirect);

      // Update the timer every 1s.
      setInterval(function () {
        self.timeLeft(self.timeLeft() - 1);
        if (self.timeLeft() === 0 && self.isRedirecting()) {
          self.isRedirecting(false);
          window.location.href = location;
        }
      },
      1000);
    };

    // TODO.
    // self.retrieveExtract = function (location) {
    //   $.getJSON('http://localhost:8080/datalift/sparql?default-graph-uri=internal&query=SELECT%20*%20FROM%20%3Chttp%3A%2F%2Flocalhost%3A8080%2Fdatalift%2Fproject%2Ftest1%2Fsource%2Fagequinquennal6809-donnees-21-qb%3E%20WHERE%20{%20%20%20%3Fs%20%3Fp%20%3Fo%20.%20}%20LIMIT%2010&format=json&grid=true',
    //   function (data) {
    //     self.extract(data);
    //     console.log(self.extract().head);
    //   });
    // };
    //
    // http://localhost:8080/datalift/sparql?default-graph-uri=internal&query=SELECT%20*%20FROM%20%3Chttp%3A%2F%2Flocalhost%3A8080%2Fdatalift%2Fproject%2Ftest1%2Fsource%2Fagequinquennal6809-donnees-21-qb%3E%20WHERE%20{%20%20%20%3Fs%20%3Fp%20%3Fo%20.%20}%20LIMIT%2010&format=json&grid=true
    // Returns
    // {"head": [ "s", "p", "o" ], "rows": [{"s": "<a href=\"http:\/\/localhost:8080\/datalift\/sparql\/describe?uri=http%3A%2F%2Fwww.recshop.fake%2Fcd%2FEmpire+Burlesque&default-graph=internal\">http:\/\/www.recshop.fake\/cd\/Empire Burlesque<\/a>", "p": "<a href=\"http:\/\/localhost:8080\/datalift\/sparql\/describe?uri=http%3A%2F%2Fwww.recshop.fake%2Fcd%23artist&default-graph=internal\">http:\/\/www.recshop.fake\/cd#artist<\/a>", "o": "\"Bob Dylan\""}, {"s": "<a href=\"http:\/\/localhost:8080\/datalift\/sparql\/describe?uri=http%3A%2F%2Fwww.recshop.fake%2Fcd%2FEmpire+Burlesque&default-graph=internal\">http:\/\/www.recshop.fake\/cd\/Empire Burlesque<\/a>", "p": "<a href=\"http:\/\/localhost:8080\/datalift\/sparql\/describe?uri=http%3A%2F%2Fwww.recshop.fake%2Fcd%23country&default-graph=internal\">http:\/\/www.recshop.fake\/cd#country<\/a>", "o": "\"USA\""}, {"s": "<a href=\"http:\/\/localhost:8080\/datalift\/sparql\/describe?uri=http%3A%2F%2Fwww.recshop.fake%2Fcd%2FEmpire+Burlesque&default-graph=internal\">http:\/\/www.recshop.fake\/cd\/Empire Burlesque<\/a>", "p": "<a href=\"http:\/\/localhost:8080\/datalift\/sparql\/describe?uri=http%3A%2F%2Fwww.recshop.fake%2Fcd%23company&default-graph=internal\">http:\/\/www.recshop.fake\/cd#company<\/a>", "o": "\"Columbia\""}, {"s": "<a href=\"http:\/\/localhost:8080\/datalift\/sparql\/describe?uri=http%3A%2F%2Fwww.recshop.fake%2Fcd%2FEmpire+Burlesque&default-graph=internal\">http:\/\/www.recshop.fake\/cd\/Empire Burlesque<\/a>", "p": "<a href=\"http:\/\/localhost:8080\/datalift\/sparql\/describe?uri=http%3A%2F%2Fwww.recshop.fake%2Fcd%23price&default-graph=internal\">http:\/\/www.recshop.fake\/cd#price<\/a>", "o": "\"10.90\""}, {"s": "<a href=\"http:\/\/localhost:8080\/datalift\/sparql\/describe?uri=http%3A%2F%2Fwww.recshop.fake%2Fcd%2FEmpire+Burlesque&default-graph=internal\">http:\/\/www.recshop.fake\/cd\/Empire Burlesque<\/a>", "p": "<a href=\"http:\/\/localhost:8080\/datalift\/sparql\/describe?uri=http%3A%2F%2Fwww.recshop.fake%2Fcd%23year&default-graph=internal\">http:\/\/www.recshop.fake\/cd#year<\/a>", "o": "\"1985\""}, {"s": "<a href=\"http:\/\/localhost:8080\/datalift\/sparql\/describe?uri=http%3A%2F%2Fwww.recshop.fake%2Fcd%2FHide+your+heart&default-graph=internal\">http:\/\/www.recshop.fake\/cd\/Hide your heart<\/a>", "p": "<a href=\"http:\/\/localhost:8080\/datalift\/sparql\/describe?uri=http%3A%2F%2Fwww.recshop.fake%2Fcd%23artist&default-graph=internal\">http:\/\/www.recshop.fake\/cd#artist<\/a>", "o": "\"Bonnie Tyler\""}, {"s": "<a href=\"http:\/\/localhost:8080\/datalift\/sparql\/describe?uri=http%3A%2F%2Fwww.recshop.fake%2Fcd%2FHide+your+heart&default-graph=internal\">http:\/\/www.recshop.fake\/cd\/Hide your heart<\/a>", "p": "<a href=\"http:\/\/localhost:8080\/datalift\/sparql\/describe?uri=http%3A%2F%2Fwww.recshop.fake%2Fcd%23country&default-graph=internal\">http:\/\/www.recshop.fake\/cd#country<\/a>", "o": "\"UK\""}, {"s": "<a href=\"http:\/\/localhost:8080\/datalift\/sparql\/describe?uri=http%3A%2F%2Fwww.recshop.fake%2Fcd%2FHide+your+heart&default-graph=internal\">http:\/\/www.recshop.fake\/cd\/Hide your heart<\/a>", "p": "<a href=\"http:\/\/localhost:8080\/datalift\/sparql\/describe?uri=http%3A%2F%2Fwww.recshop.fake%2Fcd%23company&default-graph=internal\">http:\/\/www.recshop.fake\/cd#company<\/a>", "o": "\"CBS Records\""}, {"s": "<a href=\"http:\/\/localhost:8080\/datalift\/sparql\/describe?uri=http%3A%2F%2Fwww.recshop.fake%2Fcd%2FHide+your+heart&default-graph=internal\">http:\/\/www.recshop.fake\/cd\/Hide your heart<\/a>", "p": "<a href=\"http:\/\/localhost:8080\/datalift\/sparql\/describe?uri=http%3A%2F%2Fwww.recshop.fake%2Fcd%23price&default-graph=internal\">http:\/\/www.recshop.fake\/cd#price<\/a>", "o": "\"9.90\""}, {"s": "<a href=\"http:\/\/localhost:8080\/datalift\/sparql\/describe?uri=http%3A%2F%2Fwww.recshop.fake%2Fcd%2FHide+your+heart&default-graph=internal\">http:\/\/www.recshop.fake\/cd\/Hide your heart<\/a>", "p": "<a href=\"http:\/\/localhost:8080\/datalift\/sparql\/describe?uri=http%3A%2F%2Fwww.recshop.fake%2Fcd%23year&default-graph=internal\">http:\/\/www.recshop.fake\/cd#year<\/a>", "o": "\"1988\""}]}


    // <table class="table table-striped table-condensed table-hover table-bordered">
    //   <caption>Extrait des résultats</caption>
    //   <thead></thead>
    //   <tbody>

    //   </tbody>
    // </table>
  };

  return State;
});
