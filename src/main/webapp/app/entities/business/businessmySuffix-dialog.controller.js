(function() {
    'use strict';

    angular
        .module('advertiserApp')
        .controller('BusinessMySuffixDialogController', BusinessMySuffixDialogController);

    BusinessMySuffixDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Business', 'Campaign'];

    function BusinessMySuffixDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Business, Campaign) {
        var vm = this;

        vm.business = entity;
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
            if (vm.business.id !== null) {
                Business.update(vm.business, onSaveSuccess, onSaveError);
            } else {
                Business.save(vm.business, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('advertiserApp:businessUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
