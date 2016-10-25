(function() {
    'use strict';

    angular
        .module('advertiserApp')
        .controller('SpotMySuffixDeleteController',SpotMySuffixDeleteController);

    SpotMySuffixDeleteController.$inject = ['$uibModalInstance', 'entity', 'Spot'];

    function SpotMySuffixDeleteController($uibModalInstance, entity, Spot) {
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
