(function() {
    'use strict';

    angular
        .module('advertiserApp')
        .controller('CalendarController', CalendarController);

    CalendarController.$inject = ['$scope', '$state'];

    function CalendarController ($scope, $state) {
        var vm = this;

        $(document).ready(function() {
            $('#calendar').fullCalendar({
                editable: true,
                defaultView: 'agendaWeek',
                allDaySlot: false,
                slotEventOverlap: false,
                firstDay: '1'
            })
        });
    }
})();
