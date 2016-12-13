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
        var selectedHourId;

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

                eventClick: function(event) {
                    loadDataDetails(event.start);
                },

                dayClick: function(date, allDay, jsEvent, view){
                    loadDataDetails(date);
                },

                events: function () {
                    loadData();
                }
            });
            calendar.tab('show');
        });

        window.setTimeout(function() {
            calendar.fullCalendar('render');
        }, 50);

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
            vm.campaigns = Campaign.getAvailableCampaigns({dateTime: moment(date).toJSON()},
                function(resolve){
                    showDetails(date);
                });
        }

        function showDetails(dateTime){
            var dt = moment(dateTime);
            var day = dt.date();
            var dayObj = getDayBy(day);
            var date = dt.year() + "-" + (dt.month()+1) + "-" + dt.date();
            var hour = dt.hour();

            var spots = getSpotsIn(getDayBy(dt.date()), hour);
            var temp = getHourBy(hour, dayObj.hours);
            selectedHourId = temp.id;
            dateTime = moment(dateTime).subtract({'hour': 1});
            dateTime = moment(dateTime).toDate();
            goToDetailWindow(dateTime, date, hour, spots);
        }

        function goToDetailWindow(dateTime, date, hour, spots){
            $state.go('calendar-details',
                {'dateTime': dateTime,
                    'date': date,
                    'hour': hour,
                    'hourId': selectedHourId,
                    'spots' : spots,
                    'campaigns': vm.campaigns});
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
