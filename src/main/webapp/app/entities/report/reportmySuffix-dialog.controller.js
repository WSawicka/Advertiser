(function() {
    'use strict';

    angular
        .module('advertiserApp')
        .controller('ReportMySuffixDialogController', ReportMySuffixDialogController);

    ReportMySuffixDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Report', 'Campaign'];

    function ReportMySuffixDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Report, Campaign) {
        var vm = this;
        vm.authorities = ['ROLE_ADMIN'];

        vm.report = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.campaigns = Campaign.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.report.id !== null) {
                Report.update(vm.report, onSaveSuccess, onSaveError);
            } else {
                Report.save(vm.report, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('advertiserApp:reportUpdate', result);
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
