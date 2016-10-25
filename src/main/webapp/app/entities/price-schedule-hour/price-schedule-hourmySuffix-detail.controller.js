(function() {
    'use strict';

    angular
        .module('advertiserApp')
        .controller('PriceScheduleHourMySuffixDetailController', PriceScheduleHourMySuffixDetailController);

    PriceScheduleHourMySuffixDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'PriceScheduleHour', 'PriceSchedule'];

    function PriceScheduleHourMySuffixDetailController($scope, $rootScope, $stateParams, previousState, entity, PriceScheduleHour, PriceSchedule) {
        var vm = this;

        vm.priceScheduleHour = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('advertiserApp:priceScheduleHourUpdate', function(event, result) {
            vm.priceScheduleHour = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
