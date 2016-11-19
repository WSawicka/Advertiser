(function() {
    'use strict';

    angular
        .module('advertiserApp')
        .controller('CalendarDetailsController', CalendarDetailsController);

    CalendarDetailsController.$inject = ['$scope', '$stateParams', '$uibModalInstance'];

    function CalendarDetailsController ($scope, $stateParams, $uibModalInstance) {
        var vm = this;
        vm.spots = $stateParams.spots;
        vm.date = $stateParams.date;
        vm.hour = $stateParams.hour;
        vm.clear = clear;
        vm.save = save;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
        }
    }
})();
