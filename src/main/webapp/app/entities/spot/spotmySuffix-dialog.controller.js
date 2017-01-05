(function() {
    'use strict';

    angular
        .module('advertiserApp')
        .controller('SpotMySuffixDialogController', SpotMySuffixDialogController);

    SpotMySuffixDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Spot', 'Week', 'Day', 'Hour', 'Campaign', 'SpotInfo'];

    function SpotMySuffixDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Spot, Week, Day, Hour, Campaign, SpotInfo) {
        var vm = this;
        vm.authorities = ['ROLE_ADMIN'];

        vm.spot = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.weeks = Week.query();
        vm.days = Day.query();
        vm.hours = Hour.query();
        vm.campaigns = Campaign.query();
        vm.spotinfos = SpotInfo.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            vm.spot.spotName = getCampaignBy(vm.spot.campaignId).nameShort;

            var hourId = getHourIdFrom(moment(vm.spot.dateTime));
            vm.spot.hourId = hourId;
            if (vm.spot.id !== null) {
                Spot.update(vm.spot, onSaveSuccess, onSaveError);
            } else {
                Spot.save(vm.spot, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('advertiserApp:spotUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.dateTime = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }

        function getCampaignBy(id){
            for(var campaign in vm.campaigns){
                var camp = vm.campaigns[campaign];
                if(camp.id == id) return camp;
            }
        }

        function getHourIdFrom(date){
            var dateMoment = moment(date);
            var weekID = getWeekIdBy(dateMoment.isoWeek());
            var d = dateMoment.date();
            var dayID = getDayIdBy(dateMoment.date(), weekID);
            for(var h in vm.hours){
                var hour = vm.hours[h];
                if(hour.number == dateMoment.hour() && hour.dayId == dayID){
                    return hour.id;
                }
            }
        }
        function getWeekIdBy(number){
            for(var w in vm.weeks){
                var week = vm.weeks[w];
                if(week.number == number){
                    return week.id;
                }
            }
        }
        function getDayIdBy(number, weekID){
            for(var d in vm.days){
                var day = vm.days[d];
                if(day.number == number && day.weekId == weekID){
                    return day.id;
                }
            }
        }
    }
})();
