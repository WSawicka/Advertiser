(function() {
    'use strict';

    angular
        .module('advertiserApp')
        .controller('CampaignMySuffixController', CampaignMySuffixController);

    CampaignMySuffixController.$inject = ['$scope', '$state', 'Campaign'];

    function CampaignMySuffixController ($scope, $state, Campaign) {
        var vm = this;
        
        vm.campaigns = [];

        loadAll();

        function loadAll() {
            Campaign.query(function(result) {
                vm.campaigns = result;
            });
        }
    }
})();
