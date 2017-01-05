(function() {
    'use strict';

    angular
        .module('advertiserApp')
        .controller('SpotMySuffixController', SpotMySuffixController);

    SpotMySuffixController.$inject = ['$scope', '$state', 'Spot'];

    function SpotMySuffixController ($scope, $state, Spot) {
        var vm = this;
        vm.authorities = ['ROLE_ADMIN'];

        vm.spots = [];

        loadAll();

        function loadAll() {
            Spot.query(function(result) {
                vm.spots = result;
            });
        }
    }
})();
