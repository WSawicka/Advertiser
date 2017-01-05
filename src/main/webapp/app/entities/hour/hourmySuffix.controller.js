(function() {
    'use strict';

    angular
        .module('advertiserApp')
        .controller('HourMySuffixController', HourMySuffixController);

    HourMySuffixController.$inject = ['$scope', '$state', 'Hour'];

    function HourMySuffixController ($scope, $state, Hour) {
        var vm = this;
        vm.authorities = ['ROLE_ADMIN'];

        vm.hours = [];

        loadAll();

        function loadAll() {
            Hour.query(function(result) {
                vm.hours = result;
            });
        }
    }
})();
