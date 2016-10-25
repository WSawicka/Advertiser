'use strict';

describe('Controller Tests', function() {

    describe('Campaign Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockCampaign, MockState, MockBusiness, MockSpot, MockSpotInfo, MockPriceSchedule, MockReport;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockCampaign = jasmine.createSpy('MockCampaign');
            MockState = jasmine.createSpy('MockState');
            MockBusiness = jasmine.createSpy('MockBusiness');
            MockSpot = jasmine.createSpy('MockSpot');
            MockSpotInfo = jasmine.createSpy('MockSpotInfo');
            MockPriceSchedule = jasmine.createSpy('MockPriceSchedule');
            MockReport = jasmine.createSpy('MockReport');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Campaign': MockCampaign,
                'State': MockState,
                'Business': MockBusiness,
                'Spot': MockSpot,
                'SpotInfo': MockSpotInfo,
                'PriceSchedule': MockPriceSchedule,
                'Report': MockReport
            };
            createController = function() {
                $injector.get('$controller')("CampaignMySuffixDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'advertiserApp:campaignUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
