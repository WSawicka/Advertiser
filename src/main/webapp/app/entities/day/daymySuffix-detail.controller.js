(function() {
    'use strict';

    angular
        .module('advertiserApp')
        .controller('DayMySuffixDetailController', DayMySuffixDetailController);

    DayMySuffixDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Day', 'Hour', 'Week'];

    function DayMySuffixDetailController($scope, $rootScope, $stateParams, previousState, entity, Day, Hour, Week) {
        var vm = this;

        vm.day = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('advertiserApp:dayUpdate', function(event, result) {
            vm.day = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
