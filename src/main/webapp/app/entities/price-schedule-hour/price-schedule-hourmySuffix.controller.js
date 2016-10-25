(function() {
    'use strict';

    angular
        .module('advertiserApp')
        .controller('PriceScheduleHourMySuffixController', PriceScheduleHourMySuffixController);

    PriceScheduleHourMySuffixController.$inject = ['$scope', '$state', 'PriceScheduleHour'];

    function PriceScheduleHourMySuffixController ($scope, $state, PriceScheduleHour) {
        var vm = this;
        
        vm.priceScheduleHours = [];

        loadAll();

        function loadAll() {
            PriceScheduleHour.query(function(result) {
                vm.priceScheduleHours = result;
            });
        }
    }
})();
