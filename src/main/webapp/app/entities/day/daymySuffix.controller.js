(function() {
    'use strict';

    angular
        .module('advertiserApp')
        .controller('DayMySuffixController', DayMySuffixController);

    DayMySuffixController.$inject = ['$scope', '$state', 'Day'];

    function DayMySuffixController ($scope, $state, Day) {
        var vm = this;
        vm.authorities = ['ROLE_ADMIN'];

        vm.days = [];

        loadAll();

        function loadAll() {
            Day.query(function(result) {
                vm.days = result;
            });
        }
    }
})();
