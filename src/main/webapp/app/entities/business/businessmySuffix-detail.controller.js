(function() {
    'use strict';

    angular
        .module('advertiserApp')
        .controller('BusinessMySuffixDetailController', BusinessMySuffixDetailController);

    BusinessMySuffixDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Business', 'Campaign'];

    function BusinessMySuffixDetailController($scope, $rootScope, $stateParams, previousState, entity, Business, Campaign) {
        var vm = this;

        vm.business = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('advertiserApp:businessUpdate', function(event, result) {
            vm.business = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
