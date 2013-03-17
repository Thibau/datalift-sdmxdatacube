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
      if (!viewResults) {
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
  };

  return State;
});
