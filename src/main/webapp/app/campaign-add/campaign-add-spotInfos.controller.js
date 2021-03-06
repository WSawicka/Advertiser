(function() {
    'use strict';

    angular
        .module('advertiserApp')
        .controller('CampaignAddSpotInfosController', CampaignAddSpotInfosController);

    CampaignAddSpotInfosController.$inject = ['$timeout', '$scope', '$state', '$stateParams','Campaign', 'SpotInfo', 'State'];

    function CampaignAddSpotInfosController ($timeout, $scope, $state, $stateParams, Campaign, SpotInfo, State) {
        var vm = this;
        vm.authorities = ['ROLE_ADMIN'];
        vm.campaign = $stateParams.campaign;
        // vm.campaignGot = Campaign.getCampaignByNameAndStartDate({name: vm.campaign.name, startDate: vm.campaign.startDate});

        vm.spotInfos = [];

        loadAll();

        function loadAll() {
            SpotInfo.getAllSpotInfosIn({campaignId: vm.campaign.id}, function(result) {
                vm.spotInfos = result;
            });
        }

        $scope.delete = function () {
            var spotInfoToDelete = this.spotInfo;
            SpotInfo.delete({id: spotInfoToDelete.id});
        }

    }
})();
