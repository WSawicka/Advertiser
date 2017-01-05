(function() {
    'use strict';

    angular
        .module('advertiserApp')
        .controller('WeekMySuffixDetailController', WeekMySuffixDetailController);

    WeekMySuffixDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Week', 'Day'];

    function WeekMySuffixDetailController($scope, $rootScope, $stateParams, previousState, entity, Week, Day) {
        var vm = this;
        vm.authorities = ['ROLE_ADMIN'];

        vm.week = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('advertiserApp:weekUpdate', function(event, result) {
            vm.week = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
