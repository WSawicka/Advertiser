(function() {
    'use strict';

    angular
        .module('advertiserApp')
        .controller('StateMySuffixDetailController', StateMySuffixDetailController);

    StateMySuffixDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'State', 'Campaign'];

    function StateMySuffixDetailController($scope, $rootScope, $stateParams, previousState, entity, State, Campaign) {
        var vm = this;
        vm.authorities = ['ROLE_ADMIN'];

        vm.state = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('advertiserApp:stateUpdate', function(event, result) {
            vm.state = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
