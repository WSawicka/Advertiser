'use strict';

describe('Controller Tests', function() {

    describe('Spot Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockSpot, MockHour, MockCampaign, MockSpotInfo;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockSpot = jasmine.createSpy('MockSpot');
            MockHour = jasmine.createSpy('MockHour');
            MockCampaign = jasmine.createSpy('MockCampaign');
            MockSpotInfo = jasmine.createSpy('MockSpotInfo');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Spot': MockSpot,
                'Hour': MockHour,
                'Campaign': MockCampaign,
                'SpotInfo': MockSpotInfo
            };
            createController = function() {
                $injector.get('$controller')("SpotMySuffixDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'advertiserApp:spotUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
