(function() {
    'use strict';

    angular
        .module('advertiserApp')
        .controller('CampaignMySuffixDetailController', CampaignMySuffixDetailController);

    CampaignMySuffixDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Campaign', 'State', 'Business', 'Spot', 'SpotInfo', 'PriceSchedule', 'Report'];

    function CampaignMySuffixDetailController($scope, $rootScope, $stateParams, previousState, entity, Campaign, State, Business, Spot, SpotInfo, PriceSchedule, Report) {
        var vm = this;
        vm.authorities = ['ROLE_ADMIN'];

        vm.campaign = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('advertiserApp:campaignUpdate', function(event, result) {
            vm.campaign = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
