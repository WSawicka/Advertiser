(function() {
    'use strict';

    angular
        .module('advertiserApp')
        .controller('HourMySuffixDetailController', HourMySuffixDetailController);

    HourMySuffixDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Hour', 'Spot', 'Day'];

    function HourMySuffixDetailController($scope, $rootScope, $stateParams, previousState, entity, Hour, Spot, Day) {
        var vm = this;
        vm.authorities = ['ROLE_ADMIN'];

        vm.hour = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('advertiserApp:hourUpdate', function(event, result) {
            vm.hour = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
