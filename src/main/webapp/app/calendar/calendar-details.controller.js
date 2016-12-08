(function() {
    'use strict';

    angular
        .module('advertiserApp')
        .controller('CalendarDetailsController', CalendarDetailsController);

    CalendarDetailsController.$inject = ['$scope', '$stateParams', '$uibModalInstance'];

    function CalendarDetailsController ($scope, $stateParams, $uibModalInstance) {
        var vm = this;
        vm.date = $stateParams.date;
        vm.hour = $stateParams.hour;
        vm.spots = $stateParams.spots;
        vm.campaigns = $stateParams.campaigns;
        vm.spotInfos;
        vm.clear = clear;
        vm.save = save;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.business.id !== null) {
                // Business.update(vm.business, onSaveSuccess, onSaveError);
            } else {
                // Business.save(vm.business, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }
    }
})();
