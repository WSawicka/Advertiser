(function() {
    'use strict';

    angular
        .module('advertiserApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('summaries', {
                parent: 'app',
                url: '/summaries',
                data: {
                    authorities: ['ROLE_ADMIN'],
                    pageTitle: 'advertiserApp.campaign.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/summaries/summaries.html',
                        controller: 'SummariesController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('campaign');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
    }

})();
