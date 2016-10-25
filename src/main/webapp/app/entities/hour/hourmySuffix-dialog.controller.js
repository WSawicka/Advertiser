(function() {
    'use strict';

    angular
        .module('advertiserApp')
        .controller('HourMySuffixDialogController', HourMySuffixDialogController);

    HourMySuffixDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Hour', 'Spot', 'Day'];

    function HourMySuffixDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Hour, Spot, Day) {
        var vm = this;

        vm.hour = entity;
        vm.clear = clear;
        vm.save = save;
        vm.spots = Spot.query();
        vm.days = Day.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.hour.id !== null) {
                Hour.update(vm.hour, onSaveSuccess, onSaveError);
            } else {
                Hour.save(vm.hour, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('advertiserApp:hourUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
