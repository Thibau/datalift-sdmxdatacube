define([
  'knockout',
  'config/global'
], function(ko, g){
  'use strict';

  /**
   * An application state prototype used to delegate
   * the view model's keeping track duty.
   * For example, it is used to manage modals to display
   * when async communications with the server are engaged.
   */
  var State = function() {
    var self = this;

    self.isProcessing = ko.observable(false);
    self.isConfirming = ko.observable(false);
    self.isReturning  = ko.observable(false);
    self.isError      = ko.observable(false);
    self.isSuccess    = ko.observable(false);
    self.globalError  = ko.observable();
    self.globalResult = ko.observable();

    self.launchingStart = function() {
      self.isProcessing(true);
      self.isConfirming(false);
      self.globalError(null);
      self.globalResult(null);
    };

    self.launchingSuccess = function(data) {
      self.isProcessing(false);
      self.isSuccess(true);
      self.globalResult(data);
    };

    self.launchingError = function(error) {
      self.isProcessing(false);
      self.isError(true);
      self.globalError(error);
    };

  };

  return State;
});
