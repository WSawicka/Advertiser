(function() {
    'use strict';

    angular
        .module('advertiserApp')
        .controller('PriceScheduleMySuffixDeleteController',PriceScheduleMySuffixDeleteController);

    PriceScheduleMySuffixDeleteController.$inject = ['$uibModalInstance', 'entity', 'PriceSchedule'];

    function PriceScheduleMySuffixDeleteController($uibModalInstance, entity, PriceSchedule) {
        var vm = this;
        vm.authorities = ['ROLE_ADMIN'];

        vm.priceSchedule = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            PriceSchedule.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
