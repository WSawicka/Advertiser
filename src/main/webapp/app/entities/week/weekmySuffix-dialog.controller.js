(function() {
    'use strict';

    angular
        .module('advertiserApp')
        .controller('WeekMySuffixDialogController', WeekMySuffixDialogController);

    WeekMySuffixDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Week', 'Day'];

    function WeekMySuffixDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Week, Day) {
        var vm = this;
        vm.authorities = ['ROLE_ADMIN'];

        vm.week = entity;
        vm.clear = clear;
        vm.save = save;
        vm.days = Day.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.week.id !== null) {
                Week.update(vm.week, onSaveSuccess, onSaveError);
            } else {
                Week.save(vm.week, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('advertiserApp:weekUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
