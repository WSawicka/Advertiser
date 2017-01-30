(function() {
    'use strict';

    angular
        .module('advertiserApp')
        .controller('CampaignGenerateController', CampaignGenerateController);

    CampaignGenerateController.$inject = ['$scope', '$state', '$stateParams', 'Campaign', 'State', 'Spot', 'SpotInfo', 'Day'];

    function CampaignGenerateController ($scope, $state, $stateParams, Campaign, State, Spot, SpotInfo, Day) {
        var vm = this;
        vm.authorities = ['ROLE_ADMIN'];

        vm.campaign = $stateParams.campaign;
        vm.amount = $stateParams.amount;
        vm.isPossible = false;
        vm.spotInfos = [];
        vm.spotInfosAll = [];
        vm.notSelectedSpotInfo = false;

        vm.spotsPerDay = 12;  //default value
        vm.daysBetween = [];

        vm.night = false;
        vm.morning = true;
        vm.day = true;
        vm.evening = false;

        vm.hoursPreferred = [];
        vm.peaks = [5, 7, 15, 18];
        vm.withPeaks = false;

        vm.allSpotsToGenerate = vm.campaign.spotAmount - vm.amount;
        vm.spotsInHoursPreferred = 0;
        vm.allPreferredHours = 0; // info from db
        vm.spotsInOtherHours = 0;

        var spotGenerated = 0;
        vm.generationTypeSelected = "default";
        vm.generationType = [{name: "zwykły", value: "default"}, {name: "losowy", value: "xorshift"}];

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
            $scope.changed = function () {
                vm.isPossible = false;
                vm.notSelectedSpotInfo = false;
            };

            $scope.checkIfPossible = function () {
                vm.hoursPreferred = [];
                if (vm.night) vm.hoursPreferred.push([0], [1], [2], [3], [4], [5]);
                if (vm.morning) vm.hoursPreferred.push([6], [7], [8], [9], [10], [11]);
                if (vm.day) vm.hoursPreferred.push([12], [13], [14], [15], [16], [17]);
                if (vm.evening) vm.hoursPreferred.push([18], [19], [20], [21], [22], [23]);

                if (Array.isArray(vm.spotInfos)) {
                    vm.notSelectedSpotInfo = true;
                    return;
                }
                else
                    vm.notSelectedSpotInfo = false;

                Campaign.checkIfPossible({hoursPreferred: vm.hoursPreferred, startDate: vm.campaign.startDate,
                        endDate: vm.campaign.endDate}, function (result) {
                    vm.spotsPerDay = parseInt(document.getElementById("spotsPerDay").value, 10);
                    var allHours = result[0]; // possible hours between the dates
                    vm.allPreferredHours = result[0]; // possible & preferred hours between the dates

                    //check if it's possible to sets spots accordingly to spots per day limit
                    var amountOfHoursPref = vm.hoursPreferred.length;
                    var amountOfDays = moment(vm.campaign.endDate).diff(vm.campaign.startDate, 'days');

                    var morePlaceThanSpotsToGenerate = (vm.spotsPerDay > amountOfHoursPref) ?
                        amountOfHoursPref * amountOfDays >= vm.allSpotsToGenerate : vm.spotsPerDay * amountOfDays >= vm.allSpotsToGenerate;
                    var possibleToSetSpotsInOtherHours = vm.spotsPerDay > amountOfHoursPref;
                    if (allHours >= vm.allSpotsToGenerate) {
                        if (morePlaceThanSpotsToGenerate) { //no need for other hours
                            vm.spotsInHoursPreferred = vm.allSpotsToGenerate;
                            vm.spotsInOtherHours = 0;
                            vm.isPossible = true;
                        } else if (!morePlaceThanSpotsToGenerate && possibleToSetSpotsInOtherHours) {//need for other hours, but possible insert
                            vm.spotsInHoursPreferred = amountOfHoursPref * amountOfDays;
                            vm.spotsInOtherHours = (vm.spotsPerDay - amountOfHoursPref) * amountOfDays;
                            vm.isPossible = true;
                        } else // not possible to set other hours - not possible to generate all amount of spots
                            alert("Woops! Error.\nNot enough hours.\nChange preferred hours, amounts of spots per day or campaign dates.");
                    } else
                        alert("Woops! Error.\nNot enough available hours between those days.\nChange campaign dates.");
                });
            };

            $scope.generate = function () {
                generateSpots();
            };

            function generateSpots() {
                var toGenerate = [vm.spotsInHoursPreferred, vm.spotsInOtherHours];
                var spotInfoId = getSpotInfoWith(vm.spotInfos, vm.spotInfosAll).id;
                var generationForm = vm.generationTypeSelected;

                if (!vm.withPeaks)
                    vm.peaks = [11];

                Campaign.generateSpotsInCampaign({generationForm: generationForm, id: vm.campaign.id,
                        toGenerate: toGenerate, spotInfoId: spotInfoId,
                        spotsLimit: vm.spotsPerDay, hoursPreferred: vm.hoursPreferred, peaks: vm.peaks},
                    function (result) {
                        $.when($.ajax(saveSpots(result))).then(function () {
                            alert(spotGenerated + " spots was generated.");
                        });

                        $state.go('campaign');
                    });
            }

            function saveSpots(spots) {
                for(var s in spots) {
                    var spot = spots[s];
                    if (spot.hasOwnProperty("id")) {
                        Spot.save(spot);
                        spotGenerated++;
                    }
                }
            }

        });
    }
})();
