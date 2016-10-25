(function() {
    'use strict';

    angular
        .module('advertiserApp')
        .controller('BusinessMySuffixDeleteController',BusinessMySuffixDeleteController);

    BusinessMySuffixDeleteController.$inject = ['$uibModalInstance', 'entity', 'Business'];

    function BusinessMySuffixDeleteController($uibModalInstance, entity, Business) {
        var vm = this;

        vm.business = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Business.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
