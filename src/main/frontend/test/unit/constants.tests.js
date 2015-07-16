/*global angular */

'use strict';

describe('Unit: Constants da aplicação', function() {

  var constants;

  beforeEach(function() {
    // instantiate the app module
    angular.mock.module('app');

    // mock the directive
    angular.mock.inject(function(AppSettings) {
      constants = AppSettings;
    });
  });

  it('A variÁvel deve existir', function() {
    expect(constants).toBeDefined();
  });


});