(function() {
    'use strict';

    angular
        .module('advertiserApp')
        .controller('CampaignMySuffixController', CampaignMySuffixController);

    CampaignMySuffixController.$inject = ['$scope', '$state', 'Campaign'];

    function CampaignMySuffixController ($scope, $state, Campaign) {
        var vm = this;
        vm.authorities = ['ROLE_ADMIN'];

        vm.campaigns = [];
        vm.colors = [];
        $.getJSON("/app/json/color_variables.json", function(result){
            vm.colors = result;
        });

        loadAll();

        function loadAll() {
            Campaign.query(function(result) {
                vm.campaigns = result;
            });
        }

        $scope.getColorHexFrom = function(){
            var name = this.campaign.color;
            for(var i in vm.colors){
                var color = vm.colors[i];
                if(color.name.toLowerCase() == name.toLowerCase())
                    return color.value;
            }
        }
    }
})();
