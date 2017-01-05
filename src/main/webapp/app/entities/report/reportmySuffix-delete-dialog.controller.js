(function() {
    'use strict';

    angular
        .module('advertiserApp')
        .controller('ReportMySuffixDeleteController',ReportMySuffixDeleteController);

    ReportMySuffixDeleteController.$inject = ['$uibModalInstance', 'entity', 'Report'];

    function ReportMySuffixDeleteController($uibModalInstance, entity, Report) {
        var vm = this;
        vm.authorities = ['ROLE_ADMIN'];

        vm.report = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Report.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
