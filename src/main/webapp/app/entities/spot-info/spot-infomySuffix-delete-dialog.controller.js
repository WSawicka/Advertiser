(function() {
    'use strict';

    angular
        .module('advertiserApp')
        .controller('SpotInfoMySuffixDeleteController',SpotInfoMySuffixDeleteController);

    SpotInfoMySuffixDeleteController.$inject = ['$uibModalInstance', 'entity', 'SpotInfo'];

    function SpotInfoMySuffixDeleteController($uibModalInstance, entity, SpotInfo) {
        var vm = this;

        vm.spotInfo = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            SpotInfo.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
