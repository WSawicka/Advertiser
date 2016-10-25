(function() {
    'use strict';

    angular
        .module('advertiserApp')
        .controller('SpotMySuffixDialogController', SpotMySuffixDialogController);

    SpotMySuffixDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Spot', 'Hour', 'Campaign', 'SpotInfo'];

    function SpotMySuffixDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Spot, Hour, Campaign, SpotInfo) {
        var vm = this;

        vm.spot = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.hours = Hour.query();
        vm.campaigns = Campaign.query();
        vm.spotinfos = SpotInfo.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.spot.id !== null) {
                Spot.update(vm.spot, onSaveSuccess, onSaveError);
            } else {
                Spot.save(vm.spot, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('advertiserApp:spotUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.dateTime = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
