(function() {
    'use strict';

    angular
        .module('advertiserApp')
        .controller('CampaignMySuffixController', CampaignMySuffixController);

    CampaignMySuffixController.$inject = ['$scope', '$state', 'Campaign', 'User'];

    function CampaignMySuffixController ($scope, $state, Campaign, User) {
        var vm = this;
        vm.authorities = ['ROLE_ADMIN'];

        vm.campaigns = [];
        vm.colors = [];
        vm.states = [];
        vm.businesses = [];
        vm.users = User.query();

        $.getJSON("/app/json/color_variables.json", function(result){
            vm.colors = result;
        });

        loadAll();

        function loadAll() {
            Campaign.query(function(result) {
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
            var title = this.campaign.campaignBusiness;
            for(var b in vm.businesses){
                var business = vm.businesses[b];
                if(title == business.title)
                    return business.name;
            }
        };

        $scope.getColorHexFrom = function(){
            var name = this.campaign.color;
            for(var i in vm.colors){
                var color = vm.colors[i];
                if(color.name.toLowerCase() == name.toLowerCase())
                    return color.value;
            }
        };

        $scope.getUserLogin = function(){
            var userId = this.campaign.userId;
            for(var u in vm.users){
                var user = vm.users[u];
                if(user.id == userId)
                    return user.login;
            }
        };
    }
})();
