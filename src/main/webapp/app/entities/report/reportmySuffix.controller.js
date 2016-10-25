(function() {
    'use strict';

    angular
        .module('advertiserApp')
        .controller('ReportMySuffixController', ReportMySuffixController);

    ReportMySuffixController.$inject = ['$scope', '$state', 'Report'];

    function ReportMySuffixController ($scope, $state, Report) {
        var vm = this;
        
        vm.reports = [];

        loadAll();

        function loadAll() {
            Report.query(function(result) {
                vm.reports = result;
            });
        }
    }
})();
