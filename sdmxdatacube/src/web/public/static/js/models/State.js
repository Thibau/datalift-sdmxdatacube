define([
  'jquery',
  'knockout',
  'config/global'
], function($, ko, g){
  'use strict';

  /**
   * An application state prototype used to delegate
   * the view model's keeping track duty.
   * For example, it is used to manage modals to display
   * when async communications with the server are engaged.
   */
  var State = function() {
    var self = this;

    self.isProcessing  = ko.observable(false);
    self.isConfirming  = ko.observable(false);
    self.isRedirecting = ko.observable(false);
    self.timeLeft      = ko.observable(0);
    self.isError       = ko.observable(false);
    self.isSuccess     = ko.observable(false);
    self.globalError   = ko.observable();
    self.globalResult  = ko.observable();
    self.globalExtract = ko.observable();

    self.launchingStart = function() {
      self.isProcessing(true);
      self.isConfirming(false);
      self.globalError(null);
      self.globalResult(null);
    };

    self.launchingSuccess = function(location, viewResults) {
      self.isProcessing(false);
      self.isSuccess(true);
      self.globalResult(location);
      if (viewResults) {
        self.redirectingStart(location);
      }
      else {
        self.retrieveExtract(location);
      }
    };

    self.launchingError = function(error) {
      self.isProcessing(false);
      self.isError(true);
      self.globalError(error);
    };

    // Redirect to location after timeBeforeRedirect milliseconds.
    self.redirectingStart = function(location) {
      self.isRedirecting(true);
      self.timeLeft(g.timeBeforeRedirect);

      // Update the timer every 1s.
      setInterval(function() {
        self.timeLeft(self.timeLeft() - 1);
        if (self.timeLeft() === 0 && self.isRedirecting()) {
          self.isRedirecting(false);
          window.location.href = location;
        }
      },
      1000);
    };

    self.retrieveExtract = function(location) {

    };

  };

  return State;
});
