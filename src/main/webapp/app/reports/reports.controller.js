(function() {
    'use strict';

    angular
        .module('advertiserApp')
        .controller('ReportController', ReportController);

    ReportController.$inject = ['$scope', 'Principal', 'LoginService', '$state', 'Week', 'Day'];

    function ReportController($scope, Principal, LoginService, $state, Week, Day) {
        var vm = this;
        vm.authorities = ['ROLE_ADMIN'];

        vm.year = 2016;

        $(document).ready(function () {

        });

    }
})();
