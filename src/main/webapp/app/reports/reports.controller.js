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


        $scope.getUserLogin = function(){
            var userId = this.c.campaign.userId;
            for(var u in vm.users){
                var user = vm.users[u];
                if(user.id == userId)
                    return user.login;
            }
        };

        $scope.generatePDF = function () {
            html2canvas(document.getElementById('reportBody'), {
                onrendered: function (canvas) {
                    var data = canvas.toDataURL();
                    var year = vm.year;
                    var docDefinition = {
                        content: [{
                            image: data,
                            width: 500
                        }]
                    };
                    pdfMake.createPdf(docDefinition).download("Report_" + moment().toJSON().toString() + ".pdf");
                }
            });
        }
    }
})();
