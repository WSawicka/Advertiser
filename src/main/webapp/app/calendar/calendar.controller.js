(function() {
    'use strict';

    angular
        .module('advertiserApp')
        .controller('CalendarController', CalendarController);

    CalendarController.$inject = ['$scope', '$state', 'Spot', 'Week', 'Campaign'];

    function CalendarController ($scope, $state, Spot, Week, Campaign) {
        var vm = this;
        var calendar = $('#calendar');

        vm.campaigns = [];
        vm.week;
        vm.days;
        vm.hours;

        $(document).ready(function() {
            calendar.fullCalendar({
                editable: true,
                eventDurationEditable: false,
                defaultView: 'agendaWeek',
                allDaySlot: false,
                slotDuration: "00:60:00",
                defaultEventMinutes: '30:00',
                defaultTimedEventDuration: "00:60:00",
                groupByDateAndResource: true,
                displayEventTime: false,
                firstDay: '1',
                resources: [
                    { id: '1' , title: ' ' },
                    { id: '2' , title: ' ' },
                    { id: '3' , title: ' ' },
                    { id: '4' , title: ' ' },
                    { id: '5' , title: ' ' }
                ],

                viewAfterAllRender: function (view, element) {
                    loadData();
                },

                eventClick: function(date) {
                    loadDataDetails(date);
                },

                dayClick: function(date, allDay, jsEvent, view){
                    loadDataDetails(date);
                },

                events: function () {
                    loadData();
                }
            });
        });

        function loadData() {
            calendar.fullCalendar('option', 'contentHeight', 580);
            var date = calendar.fullCalendar('getDate');
            var weekNumber = moment(date).isoWeek();
            var year = moment(date).year();

            vm.week = Week.getWeekFromYear({weekNumber: weekNumber, year: year},
                function(successData) {
                    var days = successData.days;
                    vm.days = days;
                    var hours = getHours(days);
                    vm.hours = hours;
                    var spots = getSpots(hours);
                    for(var spot in spots){
                        var s = spots[spot];
                        var date = s.dateTime.replace("T", " ");
                        var number = s.spotNumber;
                        addEvent(s.spotName, date, number);
                    }
                });
        }

        function loadDataDetails(date){
            var dateTime = moment(date).subtract('1', 'hours').add('1', 'months').toJSON();
            vm.campaigns = Campaign.getAvailableCampaigns({dateTime: d},
                function(resolve){
                    showDetails(dateTime);
                });
        }

        function showDetails(dateTime){
            var date = dateTime.getFullYear()+'-'+dateTime.getMonth()+'-'+dateTime.getDate();
            var hour = dateTime.getHours();

            var day = getDayBy(dateTime.getDate());
            var spots = getSpotsIn(day, hour);

            while (spots.length < 5) {
                var spot = new Spot();
                spots.push(spot);
            }
            $state.go('calendar-details', {'spots' : spots, 'date': date, 'hour': hour});
        }

        function getHours(days){
            var hours = [];
            for(var day in days) {
                var d = days[day];
                var temp = hours;
                hours = temp.concat(d.hours);
            }
            return hours;
        }

        function getSpots(hours) {
            var spots = [];
            for(var hour in hours){
                var h = hours[hour];
                var temp = spots;
                spots = temp.concat(h.spots);
            }
            return spots;
        }

        function getSpotsIn(day, hour){
            var hours = day.hours;
            var h = getHourBy(hour, hours);
            return h.spots;
        }

        function getHourBy(hourNumber, hours){
            for(var hour in hours){
                var h = hours[hour];
                if(h.number == hourNumber){
                    return h;
                }
            }
        }

        function getDayBy(dayNumber){
            for(var day in vm.days){
                var d = vm.days[day];
                if (d.number == dayNumber){
                    return d;
                }
            }
        }

        function addEvent(title, start, idRes) {
            var eventObject = {
                title: title,
                start: start,
                resourceId: idRes,
                allDay: false
            };
            calendar.fullCalendar('renderEvent', eventObject);
        }
    }
})();
