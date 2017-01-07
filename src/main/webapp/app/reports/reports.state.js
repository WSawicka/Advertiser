(function() {
    'use strict';

    angular
        .module('advertiserApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('reports', {
                parent: 'app',
                url: '/reports/{year}',
                data: {
                    authorities: ['ROLE_ADMIN'],
                    pageTitle: 'advertiserApp.campaign.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/reports/reports.html',
                        controller: 'ReportController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('campaign');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Campaign', function($stateParams, Campaign) {
                        return Campaign.getCampaignsWithAmountsAndPricesOfYear({year : $stateParams.year}).$promise;
                    }]
                }
            })
    }
})();
