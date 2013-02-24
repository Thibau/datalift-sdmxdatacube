define([
  'jquery',
  'knockout',
  'config/global',
  'models/Source',
  'models/SourceTransporter',
  'models/State',
  'validation'
], function($, ko, g, Source, SourceTransporter, State, validation){
  'use strict';

  /**
   * A view model which describes how our converter works,
   * what values it manages.
   * @param {Array} rawSources     An array which contains POJOs describing sources.
   * @param {Object} currentSource The value of the currentSource, could be null.
   * @param {Bool} viewResults     Tells whether or not the user wants to view results.
   */
  var ViewModel = function(rawSources, currentSource, viewResults) {
    var self = this;


    self.rawSources    = rawSources;
    self.sources       = [];
    self.currentSource = ko.observable();
    self.viewResults   = ko.observable(viewResults);
    self.state = new State();

    /*
    TODO :
    - Add restoring state with localStorage
    - Remote validation rule must be enforced
    - Rename state.global without global
    - Add extract display from SPARQL query when viewResult is false
    - Correct bug with viewResults and isRedirecting
    - Add history management
     */

    /**
     * Initializes the converter from the raw sources.
     * @param  {Object} currentSource A POJO which represents a source.
     */
    self.initialize = function(currentSource) {
      // Transform our array of Objects to an array of Sources.
      self.sources = self.rawSources.map(function (elt) {
        return new Source(elt.parent, elt.project, elt.title, elt.uri, elt.uriPattern, elt.creator, elt.created);
      });

      self.currentSource(currentSource || self.sources[0]);
    };

    self.initialize(currentSource);

    self.currentSource.extend({
      validObject : true,
      //validRemoteObject : {},
      remote : {
        onlyIf : function() {return self.currentSource.isModified() && self.currentSource.isValid();},
        params : {
          beforeSend : function(jqxhr, settings) {
            var parameterString = '';
            // Here, settings.data is already 'application/x-www-form-urlencoded'
            // Thus we need to append our URL encoded values to the string.
            $.each(new SourceTransporter(self.currentSource(), self.viewResults()), function(key, val) {
              parameterString += '&' + key + '=' + encodeURIComponent(val);
            });
            settings.data = parameterString.substring(1);
            // If not explicitely overriden here, content-type will be set to text/plain.
            jqxhr.setRequestHeader('Content-Type','application/x-www-form-urlencoded; charset=UTF-8');
          }
        }
      }
    });

    /**
     * Executes an AJAJ call to send a source to the server.
     * @param  {Object} form The form which was submitted.
     */
    self.launch = function(form) {
      self.state.launchingStart();

      $.ajax({
         type: form.method,
         url: form.action,
         data: new SourceTransporter(self.currentSource(), self.viewResults()),
         success: function(data, status, jqxhr) {
            self.state.launchingSuccess(jqxhr.getResponseHeader('Location'), self.viewResults());

            localStorage.removeItem(g.localStorageCurrentSource);
         },
         error: function(jqxhr, status, error) {
            self.state.launchingError(jqxhr.responseText);
         }
      });
    };

    /**
     * Resets the values of our sources by calling the initialization function again.
     * @return nothing.
     */
    self.reset = function() {
      self.initialize(null);
      localStorage.removeItem(g.localStorageCurrentSource);
    };

    // Internal computed observable that fires whenever anything changes.
    ko.computed(function() {
      // Store a clean copy to local storage.
      localStorage.setItem(g.localStorageCurrentSource, ko.toJSON(self.currentSource()));
    }).extend({
       // Save at most twice per second.
      throttle: 500
    });
  };
  return ViewModel;
});
