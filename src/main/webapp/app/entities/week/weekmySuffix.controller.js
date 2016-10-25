(function() {
    'use strict';

    angular
        .module('advertiserApp')
        .controller('WeekMySuffixController', WeekMySuffixController);

    WeekMySuffixController.$inject = ['$scope', '$state', 'Week'];

    function WeekMySuffixController ($scope, $state, Week) {
        var vm = this;
        
        vm.weeks = [];

        loadAll();

        function loadAll() {
            Week.query(function(result) {
                vm.weeks = result;
            });
        }
    }
})();
