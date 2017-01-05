(function() {
    'use strict';

    angular
        .module('advertiserApp')
        .controller('PriceScheduleMySuffixDetailController', PriceScheduleMySuffixDetailController);

    PriceScheduleMySuffixDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'PriceSchedule', 'PriceScheduleHour', 'Campaign'];

    function PriceScheduleMySuffixDetailController($scope, $rootScope, $stateParams, previousState, entity, PriceSchedule, PriceScheduleHour, Campaign) {
        var vm = this;
        vm.authorities = ['ROLE_ADMIN'];

        vm.priceSchedule = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('advertiserApp:priceScheduleUpdate', function(event, result) {
            vm.priceSchedule = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
