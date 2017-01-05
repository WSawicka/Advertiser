(function() {
    'use strict';

    angular
        .module('advertiserApp')
        .controller('CampaignMySuffixDeleteController',CampaignMySuffixDeleteController);

    CampaignMySuffixDeleteController.$inject = ['$uibModalInstance', 'entity', 'Campaign'];

    function CampaignMySuffixDeleteController($uibModalInstance, entity, Campaign) {
        var vm = this;
        vm.authorities = ['ROLE_ADMIN'];

        vm.campaign = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Campaign.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
