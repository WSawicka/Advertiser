(function() {
    'use strict';

    angular
        .module('advertiserApp')
        .controller('CampaignClientController', CampaignClientController);

    CampaignClientController.$inject = ['Principal', 'Auth', '$scope', '$state', 'Campaign', 'State', 'Business', 'Spot'];

    function CampaignClientController (Principal, Auth, $scope, $state, Campaign, State, Business, Spot) {
        var vm = this;
        vm.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        vm.auth = Principal.identity();
        vm.login;

        Principal.identity().then(function(account) {
            vm.login = account;
        });

        loadAll();
        //TODO: wyświetlać tylko kampanie użytkownika - nowe okno?

        vm.user;
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
