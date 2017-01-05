(function() {
    'use strict';

    angular
        .module('advertiserApp')
        .controller('StateMySuffixDeleteController',StateMySuffixDeleteController);

    StateMySuffixDeleteController.$inject = ['$uibModalInstance', 'entity', 'State'];

    function StateMySuffixDeleteController($uibModalInstance, entity, State) {
        var vm = this;
        vm.authorities = ['ROLE_ADMIN'];

        vm.state = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            State.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
