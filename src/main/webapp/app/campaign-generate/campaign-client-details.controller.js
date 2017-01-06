(function() {
    'use strict';

    angular
        .module('advertiserApp')
        .controller('CampaignClientDetailsController', CampaignClientDetailsController);

    CampaignClientDetailsController.$inject = ['$timeout', '$scope', '$state', '$stateParams', 'Spot', 'Campaign', 'State'];

    function CampaignClientDetailsController ($timeout, $scope, $state, $stateParams, Spot, Campaign, State) {
        var vm = this;
        vm.authorities = ['ROLE_USER'];
        var campaignId = $stateParams.campaignId;
        vm.campaign = Campaign.getCampaign({campaignId: campaignId}, function(){
            loadAll();
        });

        vm.data = [];

        function loadAll(){
            Spot.getCampaignsDaysSpotsOrdered({campaignId: vm.campaign.id}, function(result){
                vm.data = result;
            });
        }

    }
})();
