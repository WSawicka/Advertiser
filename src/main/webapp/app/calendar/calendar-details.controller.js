(function() {
    'use strict';

    angular
        .module('advertiserApp')
        .controller('CalendarDetailsController', CalendarDetailsController);

    CalendarDetailsController.$inject = ['$scope', '$stateParams', '$uibModalInstance','entity', 'SpotInfo', 'Spot'];

    function CalendarDetailsController ($scope, $stateParams, $uibModalInstance, entity, SpotInfo, Spot) {
        var vm = this;

        vm.newSpot = entity;
        vm.dateTime = $stateParams.dateTime;
        vm.date = $stateParams.date;
        vm.hour = $stateParams.hour;
        vm.hourId = $stateParams.hourId;
        vm.spots = $stateParams.spots;
        vm.campaigns = $stateParams.campaigns;
        vm.campaignsAll = $stateParams.campaigns;
        vm.spotInfos = [];
        vm.spotInfosAll = [];

        vm.clear = clear;
        vm.save = save;

        var selectedCampaign;
        var selectedSpotInfo;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            vm.newSpot.id = null;
            vm.newSpot.campaignDTO = selectedCampaign;
            vm.newSpot.campaignId = selectedCampaign.id;
            vm.newSpot.dateTime = vm.dateTime;
            vm.newSpot.hourId = vm.hourId;
            vm.newSpot.spotInfoId = selectedSpotInfo.id;
            vm.newSpot.spotName = selectedCampaign.nameShort;
            vm.newSpot.spotNumber = vm.spots.length+1;
            if(vm.newSpot.id == null) {
                Spot.save(vm.newSpot, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('advertiserApp:spotUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveSuccessNotClose (result) {
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        function getCampaignWith(name){
            for (var camp in vm.campaignsAll){
                var campaign = vm.campaignsAll[camp];
                if(campaign.name == name) return campaign;
            }
        }

        function getSpotInfoWith(name){
            for(var si in vm.spotInfosAll){
                var spotInfo = vm.spotInfosAll[si];
                var stringName = spotInfo.producer + ", " + spotInfo.performer + ", " + spotInfo.length + "s";
                if(stringName == name) {
                    return spotInfo;
                }
            }
        }

        function getSpot(spotNumber, value) {
            for (var s in vm.spots){
                var spot = vm.spots[s];
                if(spot.spotNumber == spotNumber + value)
                    return spot;
            }
        }

        function getSpotOver(spot){
            return getSpot(spot.spotNumber, -1);
        }

        function getSpotBelow(spot){
            return getSpot(spot.spotNumber, 1);
        }

        $(document).ready(function() {
            $scope.moveUp = function(){
                var selected = this.spot;
                var over = getSpotOver(selected);
                selected.spotNumber--;
                over.spotNumber++;
                Spot.update(selected, onSaveSuccessNotClose, onSaveError);
                Spot.update(over, onSaveSuccessNotClose, onSaveError);
            };

            $scope.moveDown = function(){
                var selected = this.spot;
                var below = getSpotBelow(selected);
                selected.spotNumber++;
                below.spotNumber--;
                Spot.update(selected, onSaveSuccessNotClose, onSaveError);
                Spot.update(below, onSaveSuccessNotClose, onSaveError);
            };

            $scope.edit = function(){
                var spot = this.spot;
                //hide visible and show hidden
                /*var visibleBf = this.$parent.$parent.getElementsByClassName('edit-visibleBf');
                for (var el = 0; el < visibleBf.length; el ++) {
                    visibleBf[el].style.display = 'none';
                }
                var hiddenBf = this.$parent.$parent.getElementsByClassName('edit-hiddenBf');
                for (var el = 0; el < hiddenBf.length; el ++) {
                    hiddenBf[el].style.display = 'inline';
                }*/
            };

            $('#select_campaign').on('change', function() {
                vm.spotInfos = [];
                var sel = $("#select_campaign").val();
                if(sel != null && sel != ""){
                    selectedCampaign = getCampaignWith(sel);
                    var campaignId = selectedCampaign.id;
                    if (campaignId != null) {
                        vm.spotInfos = SpotInfo.getAllSpotInfosIn({campaignId: campaignId},
                            function(resolve){
                                vm.spotInfosAll = vm.spotInfos;
                            });
                    }
                }
            });
            $('#select_spotInfo').on('change', function(){
                selectedSpotInfo = getSpotInfoWith($("#select_spotInfo").val());
            });
        })
    }
})();
