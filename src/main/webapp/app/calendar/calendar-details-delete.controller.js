(function() {
    'use strict';

    angular
        .module('advertiserApp')
        .controller('CalendarDetailsDeleteController', CalendarDetailsDeleteController);

    CalendarDetailsDeleteController.$inject = ['$uibModalInstance', 'entity', 'Spot'];

    function CalendarDetailsDeleteController($uibModalInstance, entity, Spot) {
        var vm = this;

        vm.spot = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Spot.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
