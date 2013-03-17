define(function () {
  'use strict';
  // trim polyfill
  if (!String.prototype.trim) {
    String.prototype.trim = function () {
      return this.replace( /^\s+|\s+$/g, '' );
    };
  }

  // forEach polyfill
  if (!Array.prototype.forEach) {
    Array.prototype.forEach = function (fn, scope) {
      for(var i = 0, len = this.length; i < len; ++i) {
        fn.call(scope || this, this[i], i, this);
      }
    };
  }

  if (!Array.prototype.every) {
    Array.prototype.every = function (fun /*, thisp */) {
      if (this == null) {
        throw new TypeError();
      }
      var t = Object(this);
      var len = t.length >>> 0;

      if (typeof fun != "function") {
        throw new TypeError();
      }
      var thisp = arguments[1];
      for (var i = 0; i < len; i++) {
        if (i in t && !fun.call(thisp, t[i], i, t)) {
          return false;
        }
      }
      return true;
    };
  }

  if (!Array.prototype.some) {
    Array.prototype.some = function (fun /*, thisp */ ) {
      if (this == null) {
        throw new TypeError();
      }
      var t = Object(this);
      var len = t.length >>> 0;
      if (typeof fun != "function") {
        throw new TypeError();
      }
      var thisp = arguments[1];
      for (var i = 0; i < len; i++) {
        if (i in t && fun.call(thisp, t[i], i, t)) {
          return true;
        }
      }
      return false;
    };
  }
});
