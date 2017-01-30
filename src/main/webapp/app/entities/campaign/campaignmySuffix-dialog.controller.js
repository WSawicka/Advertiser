(function() {
    'use strict';

    angular
        .module('advertiserApp')
        .controller('CampaignMySuffixDialogController', CampaignMySuffixDialogController);

    CampaignMySuffixDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Campaign', 'State', 'Business', 'Spot', 'SpotInfo', 'PriceSchedule', 'Report'];

    function CampaignMySuffixDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Campaign, State, Business, Spot, SpotInfo, PriceSchedule, Report) {
        var vm = this;
        vm.authorities = ['ROLE_ADMIN'];

        vm.campaign = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;

        vm.colors = [];
        $.getJSON("/app/json/color_variables.json", function(result){
            vm.colors = result;
        });

        vm.spots = Spot.query();
        vm.spotinfos = SpotInfo.query();
        vm.states = Campaign.getAllCampaignStates();
        vm.businesses = Campaign.getAllCampaignBusinesses();
        vm.priceschedules = PriceSchedule.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function getColorValue(colorName){
            for(var i in vm.colors){
                var color = vm.colors[i];
                if(color.name.toLowerCase() == colorName.toLowerCase())
                    return color.value;
            }
        }

        function getUserIdBy(login){
            for(var u in vm.users){
                var user = vm.users[u];
                if(user.login == login)
                    return user.id;
            }
        }

        $scope.getColorHexFrom = function(){
            var name = vm.campaign.color;
            if(name != null)
                return getColorValue(name);
        };

        function getBusinessTitleWith(name){
            for(var b in vm.businesses){
                var business = vm.businesses[b];
                if(business.name == name)
                    return business.title;
            }
        }

        $scope.setColor = function () {
            var colorName = this;
            return getColorValue("red");
        };

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.campaign.id !== null) {
                Campaign.update(vm.campaign, onSaveSuccess, onSaveError);
            } else {
                Campaign.save(vm.campaign, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('advertiserApp:campaignUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.startDate = false;
        vm.datePickerOpenStatus.endDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
