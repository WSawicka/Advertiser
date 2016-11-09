(function() {
    'use strict';

    angular
        .module('advertiserApp')
        .controller('CalendarController', CalendarController);

    CalendarController.$inject = ['$scope', '$state', 'Spot', 'Week'];

    function CalendarController ($scope, $state, Spot, Week) {
        var vm = this;
        var calendar = $('#calendar');

        vm.week;

        $(document).ready(function() {
            calendar.fullCalendar({
                editable: true,
                defaultView: 'agendaWeek',
                allDaySlot: false,
                slotDuration: "00:60:00",
                defaultEventMinutes: '30:00',
                defaultTimedEventDuration: "00:60:00",
                groupByDateAndResource: true,
                displayEventTime: false,
                firstDay: '1',

                viewAfterAllRender: function (view, element) {
                    loadData();
                },

                eventClick: function(date) {
                    alert('Clicked on: ' + moment(date).toDate());
                    $(this).css('background-color', 'red');
                },

                dayClick: function(date, allDay, jsEvent, view){
                    alert('Clicked on the slot: ' + date);
                },

                resources: [
                    { id: '1' , title: ' ' },
                    { id: '2' , title: ' ' },
                    { id: '3' , title: ' ' },
                    { id: '4' , title: ' ' },
                    { id: '5' , title: ' ' }
                ],

                events: function () {
                    loadData();
                }
            });
            calendar.fullCalendar('option', 'contentHeight', 580);
        });

        function loadData() {
            var date = calendar.fullCalendar('getDate');
            var weekNumber = moment(date).isoWeek();
            var year = moment(date).year();

            vm.week = Week.getWeekFromYear({weekNumber: weekNumber, year: year},
                function(successData) {
                    var days = successData.days;
                    var hours = getHours(days);
                    var spots = getSpots(hours);
                    for(var spot in spots){
                        var s = spots[spot];
                        var date = s.dateTime.replace("T", " ");
                        addEvent(s.campaignDTO.nameShort, date, 1);
                    }
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
