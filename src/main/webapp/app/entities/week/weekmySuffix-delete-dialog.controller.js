(function() {
    'use strict';

    angular
        .module('advertiserApp')
        .controller('WeekMySuffixDeleteController',WeekMySuffixDeleteController);

    WeekMySuffixDeleteController.$inject = ['$uibModalInstance', 'entity', 'Week'];

    function WeekMySuffixDeleteController($uibModalInstance, entity, Week) {
        var vm = this;
        vm.authorities = ['ROLE_ADMIN'];

        vm.week = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Week.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
