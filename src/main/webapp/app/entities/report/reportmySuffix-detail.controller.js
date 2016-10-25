(function() {
    'use strict';

    angular
        .module('advertiserApp')
        .controller('ReportMySuffixDetailController', ReportMySuffixDetailController);

    ReportMySuffixDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Report', 'Campaign'];

    function ReportMySuffixDetailController($scope, $rootScope, $stateParams, previousState, entity, Report, Campaign) {
        var vm = this;

        vm.report = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('advertiserApp:reportUpdate', function(event, result) {
            vm.report = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
