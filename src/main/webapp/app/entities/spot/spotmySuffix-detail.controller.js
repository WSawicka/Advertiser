(function() {
    'use strict';

    angular
        .module('advertiserApp')
        .controller('SpotMySuffixDetailController', SpotMySuffixDetailController);

    SpotMySuffixDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Spot', 'Hour', 'Campaign', 'SpotInfo'];

    function SpotMySuffixDetailController($scope, $rootScope, $stateParams, previousState, entity, Spot, Hour, Campaign, SpotInfo) {
        var vm = this;
        vm.authorities = ['ROLE_ADMIN'];

        vm.spot = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('advertiserApp:spotUpdate', function(event, result) {
            vm.spot = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
