(function() {
    'use strict';

    angular
        .module('advertiserApp')
        .controller('DayMySuffixDeleteController',DayMySuffixDeleteController);

    DayMySuffixDeleteController.$inject = ['$uibModalInstance', 'entity', 'Day'];

    function DayMySuffixDeleteController($uibModalInstance, entity, Day) {
        var vm = this;
        vm.authorities = ['ROLE_ADMIN'];

        vm.day = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Day.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
