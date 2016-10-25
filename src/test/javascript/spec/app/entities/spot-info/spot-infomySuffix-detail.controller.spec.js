'use strict';

describe('Controller Tests', function() {

    describe('SpotInfo Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockSpotInfo, MockSpot, MockCampaign;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockSpotInfo = jasmine.createSpy('MockSpotInfo');
            MockSpot = jasmine.createSpy('MockSpot');
            MockCampaign = jasmine.createSpy('MockCampaign');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'SpotInfo': MockSpotInfo,
                'Spot': MockSpot,
                'Campaign': MockCampaign
            };
            createController = function() {
                $injector.get('$controller')("SpotInfoMySuffixDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'advertiserApp:spotInfoUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
