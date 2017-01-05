(function() {
    'use strict';

    angular
        .module('advertiserApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('campaign', {
                parent: 'app',
                url: '/campaign',
                data: {
                    authorities: ['ROLE_ADMIN'],
                    pageTitle: 'advertiserApp.campaign.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/campaign-generate/campaign.html',
                        controller: 'CampaignController',
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
            .state('campaign-client', {
            parent: 'app',
            url: '/campaign-client',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'advertiserApp.campaign.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/campaign-generate/campaign-client.html',
                    controller: 'CampaignClientController',
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
            .state('campaign-generate', {
                parent: 'app',
                url: '/campaign/generate',
                params: {
                    campaign: '@campaign',
                    amount: '@amount'
                },
                data: {
                    authorities: ['ROLE_ADMIN']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/campaign-generate/campaign-generate.html',
                        controller: 'CampaignGenerateController',
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
            });
    }

})();
