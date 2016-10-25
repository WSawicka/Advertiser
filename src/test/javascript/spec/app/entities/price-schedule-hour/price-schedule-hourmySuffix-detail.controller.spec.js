'use strict';

describe('Controller Tests', function() {

    describe('PriceScheduleHour Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockPriceScheduleHour, MockPriceSchedule;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockPriceScheduleHour = jasmine.createSpy('MockPriceScheduleHour');
            MockPriceSchedule = jasmine.createSpy('MockPriceSchedule');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'PriceScheduleHour': MockPriceScheduleHour,
                'PriceSchedule': MockPriceSchedule
            };
            createController = function() {
                $injector.get('$controller')("PriceScheduleHourMySuffixDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'advertiserApp:priceScheduleHourUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
