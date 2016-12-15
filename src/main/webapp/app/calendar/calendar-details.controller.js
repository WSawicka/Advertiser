(function() {
    'use strict';

    angular
        .module('advertiserApp')
        .controller('CalendarDetailsController', CalendarDetailsController);

    CalendarDetailsController.$inject = ['$scope', '$stateParams', '$uibModalInstance','entity', 'Campaign', 'SpotInfo', 'Spot'];

    function CalendarDetailsController ($scope, $stateParams, $uibModalInstance, entity, Campaign, SpotInfo, Spot) {
        var vm = this;
        vm.uibMI = $uibModalInstance;

        vm.newSpot = entity;
        vm.dateTime = $stateParams.dateTime;
        vm.dateJSON = $stateParams.dateJSON;
        vm.date = $stateParams.date;
        vm.hour = $stateParams.hour;
        vm.hourId = $stateParams.hourId;
        vm.dayId = $stateParams.dayId;

        vm.spots = [];
        vm.campaigns = [];
        vm.campaignsAll = [];
        vm.spotInfos = [];
        vm.spotInfosAll = [];

        vm.campaignsEdit = [];
        vm.campaignsAllEdit = [];
        vm.spotInfosEdit = [];
        vm.spotInfosAllEdit = [];

        vm.clear = clear;
        vm.save = save;
        vm.saveEdit = saveEdit;

        var selectedCampaign;
        var selectedSpotInfo;
        var selectedCampaignEdit;
        var selectedSpotInfoEdit;
        var isEditing = false;
        var spotToEdit;

        function clear () {
            $scope.$emit('advertiserApp:spotUpdate', null);
            $uibModalInstance.close();
        }

        function save() {
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
                Spot.save(vm.newSpot, onSaveSuccessNotClose, onSaveError);
            }
        }

        function saveEdit(){
            vm.isSavingEdit = true;
            spotToEdit.campaignDTO = selectedCampaignEdit;
            spotToEdit.campaignId = selectedCampaignEdit.id;
            spotToEdit.spotInfoId = selectedSpotInfoEdit.id;
            spotToEdit.spotName = selectedCampaignEdit.nameShort;
            Spot.update(spotToEdit, onSaveSuccess, onSaveError);
        }

        function onSaveSuccess (result) {
            $scope.$emit('advertiserApp:spotUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
            vm.isSavingEdit = false;
        }

        function onSaveSuccessNotClose (result) {
            vm.isSaving = false;
            vm.isSavingEdit = false;
            vm.spotInfos = [];
            vm.spotInfosEdit = [];
            vm.spotInfosAll = [];
            vm.spotInfosAllEdit = [];
            loadData();
            loadSpots();
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        $scope.show = function () {
            return !(isEditing == true && this.spot == spotToEdit);
        };

        function loadData(){
            vm.campaigns = Campaign.getAvailableCampaigns({dateTime: vm.dateJSON},
                function(resolve){
                    vm.campaignsEdit = resolve;
                    vm.campaignsAll = resolve;
                    vm.campaignsAllEdit = resolve;
                });
        }

        function loadSpots(){
            vm.spots = Spot.getAllSpotsByHourId({hourId: vm.hourId},
                function(resolve){
                    var set = "set";
                });
        }

        $(document).ready(function() {
            loadData();
            loadSpots();

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
                isEditing = true;
                spotToEdit = this.spot;
            };

            $scope.delete = function(){
                var spotToDelete = this.spot;
                if (!(vm.spots.length == spotToDelete.spotNumber)) {
                    for (var s in vm.spots) {
                        var spot = vm.spots[s];
                        if (spotToDelete.spotNumber < spot.spotNumber) {
                            spot.spotNumber--;
                            Spot.update(spot);
                        }
                    }
                }
                Spot.delete({id: spotToDelete.id}, function () {
                    loadData();
                    loadSpots();
                });
            };

            $('#edit_select_campaign').on('change', function () {
                vm.spotInfosEdit = [];
                var sel = $("#edit_select_campaign").val();
                if(sel != null && sel != ""){
                    selectedCampaignEdit = getCampaignWith(sel);
                    var campaignId = selectedCampaignEdit.id;
                    if (campaignId != null) {
                        vm.spotInfosEdit = SpotInfo.getAllSpotInfosIn({campaignId: campaignId},
                            function(resolve){
                                vm.spotInfosAllEdit = vm.spotInfosEdit;
                            });
                    }
                }
            });

            $('#edit_select_spotInfo').on('change', function(){
                selectedSpotInfoEdit = getSpotInfoWith($("#edit_select_spotInfo").val(), vm.spotInfosAllEdit);
            });

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
                selectedSpotInfo = getSpotInfoWith($("#select_spotInfo").val(), vm.spotInfosAll);
            });

            $scope.$on('eventName', function(event, data) { console.log(data); });
        });

        function getCampaignWith(name){
            for (var camp in vm.campaignsAll){
                var campaign = vm.campaignsAll[camp];
                if(campaign.name == name) return campaign;
            }
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
    }
})();
