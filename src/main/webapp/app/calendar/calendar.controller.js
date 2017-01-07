(function() {
    'use strict';

    angular
        .module('advertiserApp')
        .controller('CalendarController', CalendarController);

    CalendarController.$inject = ['$scope', '$state', '$cookies', '$stateParams', 'Spot', 'Week', 'Campaign'];

    function CalendarController ($scope, $state, $cookies, stateParams, Spot, Week, Campaign) {
        var vm = this;
        vm.authorities = ['ROLE_USER', 'ROLE_ADMIN'];

        var calendar = $('#calendar');
        vm.editMode = ($cookies.get('editMode') == "true");
        vm.dateNowCalendar = (($cookies.get('dateNowCalendar') + "") == null) ?
            moment() : ($cookies.get('dateNowCalendar') + "");

        vm.campaigns = [];
        vm.week = null;
        vm.days = [];
        vm.hours = [];

        vm.pickDate;
        vm.openCalendar = openCalendar;
        vm.datePickerOpenStatus = {};
        vm.datePickerOpenStatus.pickDate = false;

        var selectedHourId;
        var colors;

        $(document).ready(function() {
            calendar.fullCalendar({
                editable: false,
                eventDurationEditable: false,
                defaultView: 'agendaWeek',
                allDaySlot: false,
                height: 'auto',
                slotDuration: "00:60:00",
                defaultEventMinutes: '30:00',
                defaultTimedEventDuration: "00:60:00",
                groupByDateAndResource: true,
                displayEventTime: false,
                firstDay: '1',
                defaultDate: vm.dateNowCalendar,
                resources: [
                    { id: '1' , title: ' ' },
                    { id: '2' , title: ' ' },
                    { id: '3' , title: ' ' },
                    { id: '4' , title: ' ' },
                    { id: '5' , title: ' ' }
                ],

                viewRender: function (view, element) {
                    var date = calendar.fullCalendar('getDate');
                    $cookies.put('dateNowCalendar', date);
                    vm.dateNowCalendar = date;
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

            $scope.changeEditMode = function () {
                $cookies.put('editMode', vm.editMode);
            };

            $scope.goToDate = function () {
                var newDate = moment(vm.pickDate);
                $('#calendar').fullCalendar('gotoDate', newDate);
            }
        });

        window.setTimeout(function() {
            calendar.fullCalendar('render');
        }, 40);

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }

        function loadData() {
            var date = vm.dateNowCalendar;
            var weekNumber = moment(date).isoWeek();
            var year = moment(date).year();

            Week.getSpotEvents({weekNumber: weekNumber, year: year},
                function(result) {
                    for(var r in result){
                        var spot = result[r];
                        addEvent(spot.title, spot.start, spot.resourceId, spot.color);
                    }
                });

            vm.week = Week.getWeekFromYear({weekNumber: weekNumber, year: year},
                function(successData) {
                    var days = successData.days;
                    vm.days = days;
                    var hours = getHours(days);
                    vm.hours = hours;
            });
        }

        function loadDataDetails(dateTime){
            var dt = moment(dateTime);
            var day = dt.date();
            var dayObj = getDayBy(day);
            var date = dt.year() + "-" + (dt.month()+1) + "-" + dt.date();
            var hour = dt.hour();

            var temp = getHourBy(hour, dayObj.hours);
            selectedHourId = temp.id;

            var dateJSON = moment(dateTime).toJSON();
            dateTime = moment(dateTime).subtract({'hour': 1});
            dateTime = moment(dateTime).toDate();
            goToDetailWindow(dateTime, dateJSON, date, hour);
        }

        function goToDetailWindow(dateTime, dateJSON, date, hour){
            $state.go('calendar-details', {
                'dateTime': dateTime,
                'dateJSON': dateJSON,
                'date': date,
                'hour': hour,
                'hourId': selectedHourId
            });
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

        function getColorHexFrom(name){
            for(var i in colors){
                var color = colors[i];
                if(color.name.toLowerCase() == name.toLowerCase())
                    return color.value;
            }
        }

        function addEvent(title, start, idRes, color) {
            var eventObject = {
                title: title,
                start: start,
                resourceId: idRes,
                allDay: false,
                color: color
            };
            calendar.fullCalendar('renderEvent', eventObject);
        }

        $.getJSON("/app/json/color_variables.json", function(result){
            colors = result;
        });
    }
})();
