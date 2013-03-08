define([
  'jquery',
  'knockout',
  'config/global',
  'bootstrap',
  'magicsuggest'
], function ($, ko, g) {
  'use strict';

  // A custom binding to handle the enter key (could go in a separate library)
  ko.bindingHandlers.enterKey = {
    init: function ( element, valueAccessor, allBindingsAccessor, data ) {
      var wrappedHandler, newValueAccessor;

      // wrap the handler with a check for the enter key
      wrappedHandler = function ( data, event ) {
        if ( event.keyCode === g.ENTER_KEY ) {
          valueAccessor().call( this, data, event );
        }
      };

      // create a valueAccessor with the options that we would want to pass to the event binding
      newValueAccessor = function () {
        return {
          keyup: wrappedHandler
        };
      };

      // call the real event binding's init function
      ko.bindingHandlers.event.init( element, newValueAccessor, allBindingsAccessor, data );
    }
  };

  // Wrapper to hasfocus that also selects text and applies focus async
  ko.bindingHandlers.selectAndFocus = {
    init: function ( element, valueAccessor, allBindingsAccessor ) {
      ko.bindingHandlers.hasfocus.init( element, valueAccessor, allBindingsAccessor );
      ko.utils.registerEventHandler( element, 'focus', function () {
        element.focus();
      } );
    },
    update: function ( element, valueAccessor ) {
      ko.utils.unwrapObservable( valueAccessor() ); // for dependency
      // ensure that element is visible before trying to focus
      setTimeout(function () {
        ko.bindingHandlers.hasfocus.update( element, valueAccessor );
      }, 0 );
    }
  };

  // Custom binding to trigger modals.
  ko.bindingHandlers['modal'] = {
    init: function (element) {
      $(element).modal({show : false});
    },
    update: function (element, valueAccessor) {
      var value = ko.utils.unwrapObservable(valueAccessor());

      if (value) {
        $(element).modal('show');
      } else {
        $(element).modal('hide');
      }
    }
  };

  // Custom binding to trigger tooltips.
  ko.bindingHandlers['tooltip'] = {
    init: function (element, valueAccessor) {
      var value = ko.utils.unwrapObservable(valueAccessor());
      $(element).tooltip({
        title: value
      });
    },
    update: function (element, valueAccessor) {
      var value = ko.utils.unwrapObservable(valueAccessor());
      $(element).tooltip({
        title: value
      });
    }
  };


  /**
   * Custom binding handler for Magic Suggest.
   * http://nicolasbize.github.com/magicsuggest/
   * This is currently used as a stub, but should be useful in the future.
   */
  ko.bindingHandlers['magicSuggest'] = {
    init: function (element, valueAccessor) {},
    update: function (element, valueAccessor) {
      //var value = ko.utils.unwrapObservable(valueAccessor());

      var values = [
        {type: "Schème", value: 'http://', name: "http://", desc: "Cette partie de l'URI spécifie le protocole utilisé pour accéder à la ressource."},
        {type: "Qualificateurs de format", value: 'www.', name: "www.", desc: "Pas de qualificateur de format — Sous-domaine WWW."},
        {type: "Qualificateurs de format", value: 'rdf.', name: "rdf.", desc: "Le qualificateur de format principal pour les données sémantiques publiées."},
        {type: "Qualificateurs de format", value: 'data.', name: "data.", desc: "Le qualificateur de format secondaire pour les données publiées."},
        {type: "Domaines principaux", value: 'insee.fr', name: "insee.fr", desc: "Domaine principal de l'INSEE."},
        {type: "Domaines principaux", value: 'ign.fr', name: "ign.fr", desc: "Domaine principal de l'IGN."},
        {type: "Domaines principaux", value: 'datalift.org', name: "datalift.org", desc: "Domaine principal de Datalift"},
        {type: "Éléments de contexte", value: '/rdf', name: "/rdf", desc: "Élément de contexte communément utilisé, sans valeur sémantique."},
        {type: "Éléments de contexte", value: '/def', name: "/def", desc: "Élément de contexte def pour indiquer l'URI d'un terme de vocabulaire. À utiliser avec un qualificateur de format."},
        {type: "Éléments de contexte", value: '/id', name: "/id", desc: "Élément de contexte id pour indiquer une URI destinée à identifier une ressource. À utiliser seul."},
        {type: "Propriétés SDMX", value: '{0}', name: "/{Maintainable Type}", desc: "Chemin spécifique aux éléments maintenables."},
        {type: "Propriétés SDMX", value: '{1}', name: "/{Identifiable Type}", desc: "Chemin spécifique aux élements identifiables."},
        {type: "Propriétés SDMX", value: '{2}', name: "/{Agency ID}", desc: "Agence de maintenance de l'objet SDMX original."},
        {type: "Propriétés SDMX", value: '{3}', name: "/{ID}", desc: "Identificateur unique de l'objet SDMX original."},
        {type: "Propriétés SDMX", value: '{4}', name: "/{Version}", desc: "Version de l'objet SDMX. Par défaut 1.0."},
        {type: "Propriétés SDMX", value: '{5}', name: "/{Child ID}", desc: "Identificateur des enfants de l'objet SDMX."}
      ];

      var options = {
        emptyText: 'Former avec les composants pré-fournis',
        groupBy: 'type',
        data: values,
        maxSelection: 10,
        maxDropHeight: 160,
        required : true,
        typeDelay: 200,
        useTabKey: true,
        renderer: function (v) {
          return '<p style="line-height:1">' + '<span class="text-info">' + v.name + '</span>' + ' &mdash; ' + v.desc + '</p>';
        }
      };

      var magic = $(element).magicSuggest(options);
      magic.addToSelection([values[0],values[2],values[4],values[8],values[10],values[13],values[14]]);
    }
  };
});
