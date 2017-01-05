(function() {
    'use strict';

    angular
        .module('advertiserApp')
        .controller('PriceScheduleMySuffixController', PriceScheduleMySuffixController);

    PriceScheduleMySuffixController.$inject = ['$scope', '$state', 'PriceSchedule'];

    function PriceScheduleMySuffixController ($scope, $state, PriceSchedule) {
        var vm = this;
        vm.authorities = ['ROLE_ADMIN'];

        vm.priceSchedules = [];

        loadAll();

        function loadAll() {
            PriceSchedule.query(function(result) {
                vm.priceSchedules = result;
            });
        }
    }
})();
