(function() {
    'use strict';

    angular
        .module('advertiserApp')
        .controller('HourMySuffixDeleteController',HourMySuffixDeleteController);

    HourMySuffixDeleteController.$inject = ['$uibModalInstance', 'entity', 'Hour'];

    function HourMySuffixDeleteController($uibModalInstance, entity, Hour) {
        var vm = this;

        vm.hour = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Hour.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
