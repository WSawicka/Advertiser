(function() {
    'use strict';

    angular
        .module('advertiserApp')
        .controller('CampaignClientController', CampaignClientController);

    CampaignClientController.$inject = ['Principal', 'Auth', '$scope', '$state', 'User', 'Campaign', 'State', 'Business', 'Spot'];

    function CampaignClientController (Principal, Auth, $scope, $state, User, Campaign, State, Business, Spot) {
        var vm = this;
        vm.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        vm.auth = Principal.identity();
        vm.user;

        setUser();

        vm.campaigns = [];
        vm.states = [];

        function setUser() {
            Principal.identity().then(function(account) {
                var login = account.login;
                User.get({login: login}, function(result){
                    vm.user = result;
                    loadAll();
                });
            });
        }

        function loadAll(){
            Spot.getCampaignsOfUserWithAmounts({userId: vm.user.id}, function(result){
                vm.campaigns = result;
            });
            Campaign.getAllCampaignStates(function(result){
                vm.states = result;
            });
        }

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
