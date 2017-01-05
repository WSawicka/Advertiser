(function() {
    'use strict';

    angular
        .module('advertiserApp')
        .controller('CampaignAddSpotInfosDialogController', CampaignAddSpotInfosDialogController);

    CampaignAddSpotInfosDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'SpotInfo', 'Spot'];

    function CampaignAddSpotInfosDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, SpotInfo, Spot) {
        var vm = this;

        vm.campaign = $stateParams.campaign;
        vm.spotInfo = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            vm.spotInfo.campaignId = vm.campaign.id;
            if (vm.spotInfo.id !== null) {
                SpotInfo.update(vm.spotInfo, onSaveSuccess, onSaveError);
            } else {
                SpotInfo.save(vm.spotInfo, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }
    }
})();
