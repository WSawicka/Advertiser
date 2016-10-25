(function() {
    'use strict';

    angular
        .module('advertiserApp')
        .controller('SpotInfoMySuffixDetailController', SpotInfoMySuffixDetailController);

    SpotInfoMySuffixDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'SpotInfo', 'Spot', 'Campaign'];

    function SpotInfoMySuffixDetailController($scope, $rootScope, $stateParams, previousState, entity, SpotInfo, Spot, Campaign) {
        var vm = this;

        vm.spotInfo = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('advertiserApp:spotInfoUpdate', function(event, result) {
            vm.spotInfo = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
