(function() {
    'use strict';

    angular
        .module('advertiserApp')
        .controller('CampaignGenerateController', CampaignGenerateController);

    CampaignGenerateController.$inject = ['$scope', '$state', 'Campaign', 'Spot'];

    function CampaignGenerateController ($scope, $state, Campaign, State, Business, Spot) {
        var vm = this;

    }
});
