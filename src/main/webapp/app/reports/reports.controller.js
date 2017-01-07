(function() {
    'use strict';

    angular
        .module('advertiserApp')
        .controller('ReportController', ReportController);

    ReportController.$inject = ['$scope', 'Principal', 'LoginService', 'entity', '$state', 'User', 'Campaign'];

    function ReportController($scope, Principal, LoginService, entity, $state, User, Campaign) {
        var vm = this;
        vm.authorities = ['ROLE_ADMIN'];

        vm.year = 2016;
        vm.campaigns = entity;
        vm.users = User.query();
        vm.report = Campaign.getReportGeneral({year: vm.year});
        vm.isLoaded = false;


        $scope.getUserLogin = function(){
            var userId = this.c.campaign.userId;
            for(var u in vm.users){
                var user = vm.users[u];
                if(user.id == userId)
                    return user.login;
            }
            vm.isLoaded = true;
        };
    }
})();
