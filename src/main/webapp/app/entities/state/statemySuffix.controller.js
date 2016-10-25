(function() {
    'use strict';

    angular
        .module('advertiserApp')
        .controller('StateMySuffixController', StateMySuffixController);

    StateMySuffixController.$inject = ['$scope', '$state', 'State'];

    function StateMySuffixController ($scope, $state, State) {
        var vm = this;
        
        vm.states = [];

        loadAll();

        function loadAll() {
            State.query(function(result) {
                vm.states = result;
            });
        }
    }
})();
