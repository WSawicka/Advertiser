(function() {
    'use strict';

    angular
        .module('advertiserApp')
        .controller('CampaignAddController', CampaignAddController);

    CampaignAddController.$inject = ['$timeout', '$scope', '$state', '$stateParams', 'entity', 'Campaign', 'State', 'Business', 'PriceSchedule'];

    function CampaignAddController ($timeout, $scope, $state, $stateParams, entity, Campaign, State, Business, PriceSchedule) {
        var vm = this;

        vm.campaign = entity;
        vm.cancel = cancel;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.saveSuccess = false;
        vm.showSaveErr = false;

        vm.states = Campaign.getAllCampaignStates();
        vm.businesses = Campaign.getAllCampaignBusinesses();
        vm.priceschedules = PriceSchedule.query();

        vm.datePickerOpenStatus.startDate = false;
        vm.datePickerOpenStatus.endDate = false;

        vm.campaignBusiness = null;

        vm.colors = [];
        $.getJSON("/app/json/color_variables.json", function(result){
            vm.colors = result;
        });

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }

        function getColorValue(colorName){
            for(var i in vm.colors){
                var color = vm.colors[i];
                if(color.name.toLowerCase() == colorName.toLowerCase())
                    return color.value;
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

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function cancel() {
            $state.go('home');
        }

        function save () {
            vm.showSaveErr = false;
            var startDate = moment(vm.campaign.startDate);
            var endDate = moment(vm.campaign.endDate);

            if(validate(startDate, endDate) == true) {
                if (moment(startDate).isSame(moment()))
                    vm.campaign.campaignState = "STARTED";
                else
                    vm.campaign.campaignState = "BEFORE";
                vm.campaign.campaignBusiness = getBusinessTitleWith(vm.campaignBusiness);
                Campaign.save(vm.campaign, onSaveSuccess, onSaveError);
            } else
                vm.showSaveErr = true;
        }

        function onSaveSuccess () {
            $scope.$emit('advertiserApp:campaignUpdate', vm.campaign);
            vm.isSaving = false;
            vm.saveSuccess = true;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        function validate(startDate, endDate) {
            var now = moment();
            if (moment(startDate).isBefore(now)){
                $('#startDateError').text('Too early!');
                return false;
            } else $('#startDateError').empty();
            if (moment(endDate).isSameOrBefore(startDate)){
                $('#endDateError').text("Too early!");
                return false;
            } else $('#endDateError').empty();

            if (startDate == null || startDate == "") return false;
            if (endDate == null || endDate == "") return false;
            if (vm.campaign.name == null || vm.campaign.name == "") return false;
            if (vm.campaign.nameShort == null || vm.campaign.nameShort == "") return false;
            if (vm.campaign.product == null || vm.campaign.product == "") return false;
            if (vm.campaign.spotAmount == null || vm.campaign.spotAmount == "") return false;
            if (vm.campaign.color == null || vm.campaign.color == "") return false;
            if (vm.campaignBusiness == null || vm.campaignBusiness == "") return false;
            return true;
        }
    }
})();