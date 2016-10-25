(function() {
    'use strict';

    angular
        .module('advertiserApp')
        .controller('PriceScheduleMySuffixDialogController', PriceScheduleMySuffixDialogController);

    PriceScheduleMySuffixDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'PriceSchedule', 'PriceScheduleHour', 'Campaign'];

    function PriceScheduleMySuffixDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, PriceSchedule, PriceScheduleHour, Campaign) {
        var vm = this;

        vm.priceSchedule = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.priceschedulehours = PriceScheduleHour.query();
        vm.campaigns = Campaign.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.priceSchedule.id !== null) {
                PriceSchedule.update(vm.priceSchedule, onSaveSuccess, onSaveError);
            } else {
                PriceSchedule.save(vm.priceSchedule, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('advertiserApp:priceScheduleUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.startDate = false;
        vm.datePickerOpenStatus.endDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
