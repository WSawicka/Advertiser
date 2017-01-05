(function() {
    'use strict';

    angular
        .module('advertiserApp')
        .controller('CampaignController', CampaignController);

    CampaignController.$inject = ['$scope', '$state', 'Campaign', 'State', 'Business', 'Spot'];

    function CampaignController ($scope, $state, Campaign, State, Business, Spot) {
        var vm = this;

        loadAll();

        vm.campaigns = [];
        vm.colors = [];
        vm.states = [];
        vm.businesses = [];

        $.getJSON("/app/json/color_variables.json", function(result){
            vm.colors = result;
        });


        function loadAll() {
            Spot.getCampaignsWithAmounts(function(result){
                vm.campaigns = result;
            });
            Campaign.getAllCampaignStates(function(result){
                vm.states = result;
            });
            Campaign.getAllCampaignBusinesses(function(result){
                vm.businesses = result;
            });
        }

        $scope.getCampaignBusiness = function () {
            var title = this.c.campaign.campaignBusiness;
            for(var b in vm.businesses){
                var business = vm.businesses[b];
                if(title == business.title)
                    return business.name;
            }
        };

        $scope.getColorHexFrom = function(){
            var name = this.c.campaign.color;
            for(var i in vm.colors){
                var color = vm.colors[i];
                if(color.name.toLowerCase() == name.toLowerCase())
                    return color.value;
            }
        };
    }
})();
