(function() {
    'use strict';

    angular
        .module('advertiserApp')
        .controller('DayMySuffixDialogController', DayMySuffixDialogController);

    DayMySuffixDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Day', 'Hour', 'Week'];

    function DayMySuffixDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Day, Hour, Week) {
        var vm = this;

        vm.day = entity;
        vm.clear = clear;
        vm.save = save;
        vm.hours = Hour.query();
        vm.weeks = Week.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.day.id !== null) {
                Day.update(vm.day, onSaveSuccess, onSaveError);
            } else {
                Day.save(vm.day, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('advertiserApp:dayUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
