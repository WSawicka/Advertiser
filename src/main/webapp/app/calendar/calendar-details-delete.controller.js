(function() {
    'use strict';

    angular
        .module('advertiserApp')
        .controller('CalendarDetailsDeleteController', CalendarDetailsDeleteController);

    CalendarDetailsDeleteController.$inject = ['$uibModalInstance', '$stateParams', 'entity', 'Spot'];

    function CalendarDetailsDeleteController($uibModalInstance, $stateParams, entity, Spot) {
        var vm = this;
        var uibMIparent = $stateParams.uibMI;

        var spots = [];
        spots = $stateParams.spots;
        vm.spot = entity;
        var wasLast = (vm.spot.spotNumber == spots.length);
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function getSpot(id){
            for(var s in spots){
                var spot = spots[s];
                if (spot.id == id)
                    return spot;
            }
        }

        function confirmDelete (id) {
            if (!wasLast) {
                var spotToDelete = getSpot(id);
                for (var s in spots) {
                    var spot = spots[s];
                    if (spotToDelete.spotNumber < spot.spotNumber) {
                        spot.spotNumber--;
                        Spot.update(spot);
                    }
                }
            }
            Spot.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                    //uibMIparent.close();
                });
        }
    }
})();
