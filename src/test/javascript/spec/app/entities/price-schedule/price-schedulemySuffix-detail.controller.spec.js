'use strict';

describe('Controller Tests', function() {

    describe('PriceSchedule Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockPriceSchedule, MockPriceScheduleHour, MockCampaign;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockPriceSchedule = jasmine.createSpy('MockPriceSchedule');
            MockPriceScheduleHour = jasmine.createSpy('MockPriceScheduleHour');
            MockCampaign = jasmine.createSpy('MockCampaign');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'PriceSchedule': MockPriceSchedule,
                'PriceScheduleHour': MockPriceScheduleHour,
                'Campaign': MockCampaign
            };
            createController = function() {
                $injector.get('$controller')("PriceScheduleMySuffixDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'advertiserApp:priceScheduleUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
