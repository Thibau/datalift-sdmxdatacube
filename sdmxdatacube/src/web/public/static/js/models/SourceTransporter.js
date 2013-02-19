define([
  'knockout'
], function(ko){
  'use strict';

  /**
   * Transforms a KO source into a POJO
   * using the exchange format defined by the server.
   * @param  {Source} koSource    A Source with Knockout bindings.
   * @param  {Bool}   viewResults Parameter for our form.
   * @return {Object}             An object using the server format.
   */
  var SourceTransporter =  function(koSource, viewResults) {
    var source = ko.toJS(koSource);

    var message = {
      projectId        : source.project,
      inputSourceUri   : source.parent.uri,
      outputSourceName : source.title,
      outputSourceUri  : source.uri,
      viewResults      : viewResults
    };

    /*
      A transporter is a fictional teleportation machine used in the Star Trek universe.
      Transporters convert a person or object into an energy pattern (a process called dematerialization),
      then "beam" it to a target, where it is reconverted into matter (rematerialization).
     */

    return message;
  };

  return SourceTransporter;
});