(function() {
    'use strict';

    angular
        .module('advertiserApp')
        .controller('CampaignController', CampaignController);

    CampaignController.$inject = ['$scope', '$state', 'User', 'Campaign'];

    function CampaignController ($scope, $state, User, Campaign) {
        var vm = this;
        vm.authorities = ['ROLE_ADMIN'];

        loadAll();

        vm.campaigns = [];
        vm.colors = [];
        vm.states = [];
        vm.businesses = [];
        vm.users = User.query();

        $.getJSON("/app/json/color_variables.json", function(result){
            vm.colors = result;
        });


        function loadAll() {
            Campaign.getCampaignsWithAmounts(function(result){
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

        $scope.getUserLogin = function(){
            var userId = this.c.campaign.userId;
            for(var u in vm.users){
                var user = vm.users[u];
                if(user.id == userId)
                    return user.login;
            }
        };
    }
})();
