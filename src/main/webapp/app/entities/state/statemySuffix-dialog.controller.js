(function() {
    'use strict';

    angular
        .module('advertiserApp')
        .controller('StateMySuffixDialogController', StateMySuffixDialogController);

    StateMySuffixDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'State', 'Campaign'];

    function StateMySuffixDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, State, Campaign) {
        var vm = this;

        vm.state = entity;
        vm.clear = clear;
        vm.save = save;
        vm.campaigns = Campaign.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.state.id !== null) {
                State.update(vm.state, onSaveSuccess, onSaveError);
            } else {
                State.save(vm.state, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('advertiserApp:stateUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
