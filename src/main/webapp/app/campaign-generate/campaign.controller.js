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
        vm.business = [];

        $.getJSON("/app/json/color_variables.json", function(result){
            vm.colors = result;
        });


        function loadAll() {
            Spot.getCampaignsWithAmounts(function(result){
                vm.campaigns = result;
            });
            State.query(function(result) {
                vm.states = result;
            });
            Business.query(function(result) {
                vm.business = result;
            });
        }

        $scope.getCampaignStateNameFrom = function(){
            var id = this.c.campaign.campaignStateId;
            for(var s in vm.states){
                var state = vm.states[s];
                if(state.id == id)
                    return state.name.toLowerCase();
            }
        };

        $scope.getCampaignBusiness = function () {
            var id = this.c.campaign.businessId;
            for(var b in vm.business){
                var business = vm.business[b];
                if(business.id == id){
                    return business.name;
                }
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
