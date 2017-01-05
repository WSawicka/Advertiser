(function() {
    'use strict';

    angular
        .module('advertiserApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('campaign-add', {
                parent: 'app',
                url: '/campaign-add',
                data: {
                    authorities: ['ROLE_ADMIN'],
                    pageTitle: 'advertiserApp.campaign.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/campaign-add/campaign-add.html',
                        controller: 'CampaignAddController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    entity: function () {
                        return {
                            name: null,
                            nameShort: null,
                            product: null,
                            spotAmount: null,
                            startDate: null,
                            endDate: null,
                            color: null,
                            campaignState: null,
                            campaignBusiness: null,
                            priceSchedules: null,
                            id: null
                        };
                    },
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('campaign');
                        $translatePartialLoader.addPart('spotInfo');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('campaign-add-spotInfos', {
                parent: 'campaign-add',
                url: '/campaign-add/spotInfos',
                data: {
                    authorities: ['ROLE_ADMIN'],
                    pageTitle: 'advertiserApp.campaign.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/campaign-add/campaign-add-spotInfos.html',
                        controller: 'CampaignAddSpotInfosController',
                        controllerAs: 'vm'
                    }
                },
                params: {
                    campaign: '@campaign'
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('campaign');
                        $translatePartialLoader.addPart('spotInfo');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('campaign-add-spotInfos.new', {
                parent: 'campaign-add-spotInfos',
                url: '/new',
                data: {
                    authorities: ['ROLE_ADMIN']
                },
                params : {
                    campaign: '@campaign'
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/campaign-add/campaign-add-spotInfos-dialog.html',
                        controller: 'CampaignAddSpotInfosDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    length: null,
                                    filePath: null,
                                    producer: null,
                                    scenarioAuthor: null,
                                    soundAuthor: null,
                                    performer: null,
                                    music: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function() {
                        $state.go('campaign-add-spotInfos', null, { reload: 'campaign-add-spotInfos' });
                    }, function() {
                        $state.go('campaign-add-spotInfos');
                    });
                }]
            })
            .state('campaign-add-spotInfos.edit', {
                parent: 'campaign-add-spotInfos',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_ADMIN']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/campaign-add/campaign-add-spotInfos-dialog.html',
                        controller: 'CampaignAddSpotInfosDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: ['SpotInfo', function(SpotInfo) {
                                return SpotInfo.get({id : $stateParams.id}).$promise;
                            }]
                        }
                    }).result.then(function() {
                        $state.go('campaign-add-spotInfos', null, { reload: 'campaign-add-spotInfos' });
                    }, function() {
                        $state.go('campaign');
                    });
                }]
            })
    }
})();

//campaign-add-spotInfos-dialog
