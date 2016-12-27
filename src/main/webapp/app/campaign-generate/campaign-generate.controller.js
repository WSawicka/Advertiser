(function() {
    'use strict';

    angular
        .module('advertiserApp')
        .controller('CampaignGenerateController', CampaignGenerateController);

    CampaignGenerateController.$inject = ['$scope', '$stateParams', 'Campaign', 'State', 'Spot', 'SpotInfo', 'Day'];

    function CampaignGenerateController ($scope, $stateParams, Campaign, State, Spot, SpotInfo, Day) {
        var vm = this;

        vm.campaign = $stateParams.campaign;
        vm.amount = $stateParams.amount;
        vm.isPossible = false;
        vm.spotInfos = [];
        vm.spotInfosAll = [];
        vm.spotsPerDay = 8;  //default value
        vm.daysBetween = [];

        vm.night = false;
        vm.morning = true;
        vm.day = true;
        vm.evening = false;

        vm.hoursPreferred = [];

        vm.allAvailableHours = 0;
        vm.allAvailablePreferredHours = 0;
        vm.spotsInHoursPreferred = 0;
        vm.spotsInOtherHours = 0;

        loadData();

        function loadData() {
            Day.getAllBetween({startDate: vm.campaign.startDate, endDate: vm.campaign.endDate},
                function(result){
                    vm.daysBetween = result;
            });
            vm.spotInfosAll = SpotInfo.getAllSpotInfosIn({campaignId: vm.campaign.id},
                function(resolve){
                    vm.spotInfos = resolve;
            });
        }

        function getSpotInfoWith(name, spotInfoList){
            for(var si in spotInfoList){
                var spotInfo = spotInfoList[si];
                var stringName = spotInfo.producer + ", " + spotInfo.performer + ", " + spotInfo.length + "s";
                if(stringName == name) {
                    return spotInfo;
                }
            }
        }

        $(document).ready(function() {
            $scope.checkIfPossible = function () {
                vm.hoursPreferred = [];
                if(vm.night) vm.hoursPreferred.push([0], [1], [2], [3], [4], [5]);
                if(vm.morning) vm.hoursPreferred.push([6], [7], [8], [9], [10], [11]);
                if(vm.day) vm.hoursPreferred.push([12], [13], [14], [15], [16], [17]);
                if(vm.evening) vm.hoursPreferred.push([18], [19], [20], [21], [22], [23]);

                Campaign.checkIfPossible({hoursPreferred: vm.hoursPreferred, startDate: vm.campaign.startDate,
                        endDate: vm.campaign.endDate}, function (result) {
                    vm.allAvailableHours = result[0];
                    vm.allAvailablePreferredHours = result[1];
                    if (vm.allAvailableHours >= vm.amount)
                        vm.isPossible = true;
                    if (vm.allAvailablePreferredHours >= vm.amount)
                        vm.spotsInHoursPreferred = vm.amount;
                    else {
                        vm.spotsInHoursPreferred = vm.allAvailablePreferredHours;
                        vm.spotsInOtherHours = vm.campaign.spotAmount - vm.amount - vm.spotsInHoursPreferred;
                    }
                });
            };

            $scope.generate = function () {
                var toGenerate = vm.campaign.spotAmount - vm.amount;
                var spotInfoId = getSpotInfoWith(vm.spotInfos, vm.spotInfosAll).id;
                Campaign.generateSpotsInCampaign({id: vm.campaign.id, toGenerate: toGenerate, spotInfoId: spotInfoId,
                        spotsLimit: vm.spotsPerDay, hoursPreferred: vm.hoursPreferred},
                    function (result) {
                        for(var res in result) {
                            var spot = result[res];
                            if (spot.hasOwnProperty("id"))
                                Spot.save(spot);
                        }
                    });
            }
        });
    }
})();
