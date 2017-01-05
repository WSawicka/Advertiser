(function() {
    'use strict';

    angular
        .module('advertiserApp')
        .controller('SpotInfoMySuffixController', SpotInfoMySuffixController);

    SpotInfoMySuffixController.$inject = ['$scope', '$state', 'SpotInfo'];

    function SpotInfoMySuffixController ($scope, $state, SpotInfo) {
        var vm = this;
        vm.authorities = ['ROLE_ADMIN'];

        vm.spotInfos = [];

        loadAll();

        function loadAll() {
            SpotInfo.query(function(result) {
                vm.spotInfos = result;
            });
        }
    }
})();
