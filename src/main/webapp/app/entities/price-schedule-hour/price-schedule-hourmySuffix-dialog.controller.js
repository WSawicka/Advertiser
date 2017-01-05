(function() {
    'use strict';

    angular
        .module('advertiserApp')
        .controller('PriceScheduleHourMySuffixDialogController', PriceScheduleHourMySuffixDialogController);

    PriceScheduleHourMySuffixDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'PriceScheduleHour', 'PriceSchedule'];

    function PriceScheduleHourMySuffixDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, PriceScheduleHour, PriceSchedule) {
        var vm = this;
        vm.authorities = ['ROLE_ADMIN'];

        vm.priceScheduleHour = entity;
        vm.clear = clear;
        vm.save = save;
        vm.priceschedules = PriceSchedule.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.priceScheduleHour.id !== null) {
                PriceScheduleHour.update(vm.priceScheduleHour, onSaveSuccess, onSaveError);
            } else {
                PriceScheduleHour.save(vm.priceScheduleHour, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('advertiserApp:priceScheduleHourUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
