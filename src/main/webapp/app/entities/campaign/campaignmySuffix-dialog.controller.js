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
        vm.campaignstates = State.query({filter: 'campaign-is-null'});

        vm.colors = [];
        $.getJSON("/app/json/color_variables.json", function(result){
            vm.colors = result;
        });

        $q.all([vm.campaign.$promise, vm.campaignstates.$promise]).then(function() {
            if (!vm.campaign.campaignStateId) {
                return $q.reject();
            }
            return State.get({id : vm.campaign.campaignStateId}).$promise;
        }).then(function(campaignState) {
            vm.campaignstates.push(campaignState);
        });

        vm.businesses = Business.query({filter: 'campaign-is-null'});

        $q.all([vm.campaign.$promise, vm.businesses.$promise]).then(function() {
            if (!vm.campaign.businessId) {
                return $q.reject();
            }
            return Business.get({id : vm.campaign.businessId}).$promise;
        }).then(function(business) {
            vm.businesses.push(business);
        });

        vm.spots = Spot.query();
        vm.spotinfos = SpotInfo.query();
        vm.priceschedules = PriceSchedule.query();
        vm.reports = Report.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

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
