(function() {
    'use strict';

    angular
        .module('advertiserApp')
        .controller('SpotInfoMySuffixDialogController', SpotInfoMySuffixDialogController);

    SpotInfoMySuffixDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'SpotInfo', 'Spot', 'Campaign'];

    function SpotInfoMySuffixDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, SpotInfo, Spot, Campaign) {
        var vm = this;

        vm.spotInfo = entity;
        vm.clear = clear;
        vm.save = save;
        vm.spots = Spot.query();
        vm.campaigns = Campaign.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.spotInfo.id !== null) {
                SpotInfo.update(vm.spotInfo, onSaveSuccess, onSaveError);
            } else {
                SpotInfo.save(vm.spotInfo, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('advertiserApp:spotInfoUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
