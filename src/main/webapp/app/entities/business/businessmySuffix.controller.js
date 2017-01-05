(function() {
    'use strict';

    angular
        .module('advertiserApp')
        .controller('BusinessMySuffixController', BusinessMySuffixController);

    BusinessMySuffixController.$inject = ['$scope', '$state', 'Business'];

    function BusinessMySuffixController ($scope, $state, Business) {
        var vm = this;
        vm.authorities = ['ROLE_ADMIN'];

        vm.businesses = [];

        loadAll();

        function loadAll() {
            Business.query(function(result) {
                vm.businesses = result;
            });
        }
    }
})();
