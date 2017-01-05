(function() {
    'use strict';

    angular
        .module('advertiserApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('campaignmySuffix', {
            parent: 'entity',
            url: '/campaignmySuffix',
            data: {
                authorities: ['ROLE_ADMIN'],
                pageTitle: 'advertiserApp.campaign.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/campaign/campaignsmySuffix.html',
                    controller: 'CampaignMySuffixController',
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
        .state('campaignmySuffix-detail', {
            parent: 'entity',
            url: '/campaignmySuffix/{id}',
            data: {
                authorities: ['ROLE_ADMIN'],
                pageTitle: 'advertiserApp.campaign.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/campaign/campaignmySuffix-detail.html',
                    controller: 'CampaignMySuffixDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('campaign');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Campaign', function($stateParams, Campaign) {
                    return Campaign.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'campaignmySuffix',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('campaignmySuffix-detail.edit', {
            parent: 'campaignmySuffix-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/campaign/campaignmySuffix-dialog.html',
                    controller: 'CampaignMySuffixDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Campaign', function(Campaign) {
                            return Campaign.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('campaignmySuffix.new', {
            parent: 'campaignmySuffix',
            url: '/new',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/campaign/campaignmySuffix-dialog.html',
                    controller: 'CampaignMySuffixDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                nameShort: null,
                                product: null,
                                spotAmount: null,
                                startDate: null,
                                endDate: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('campaignmySuffix', null, { reload: 'campaignmySuffix' });
                }, function() {
                    $state.go('campaignmySuffix');
                });
            }]
        })
        .state('campaignmySuffix.edit', {
            parent: 'campaignmySuffix',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/campaign/campaignmySuffix-dialog.html',
                    controller: 'CampaignMySuffixDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Campaign', function(Campaign) {
                            return Campaign.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('campaignmySuffix', null, { reload: 'campaignmySuffix' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('campaignmySuffix.delete', {
            parent: 'campaignmySuffix',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/campaign/campaignmySuffix-delete-dialog.html',
                    controller: 'CampaignMySuffixDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Campaign', function(Campaign) {
                            return Campaign.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('campaignmySuffix', null, { reload: 'campaignmySuffix' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
