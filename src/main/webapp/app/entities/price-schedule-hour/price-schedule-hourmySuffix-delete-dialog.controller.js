(function() {
    'use strict';

    angular
        .module('advertiserApp')
        .controller('PriceScheduleHourMySuffixDeleteController',PriceScheduleHourMySuffixDeleteController);

    PriceScheduleHourMySuffixDeleteController.$inject = ['$uibModalInstance', 'entity', 'PriceScheduleHour'];

    function PriceScheduleHourMySuffixDeleteController($uibModalInstance, entity, PriceScheduleHour) {
        var vm = this;

        vm.priceScheduleHour = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            PriceScheduleHour.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
