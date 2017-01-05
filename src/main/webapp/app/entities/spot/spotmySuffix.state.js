(function() {
    'use strict';

    angular
        .module('advertiserApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('spotmySuffix', {
            parent: 'entity',
            url: '/spotmySuffix',
            data: {
                authorities: ['ROLE_ADMIN'],
                pageTitle: 'advertiserApp.spot.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/spot/spotsmySuffix.html',
                    controller: 'SpotMySuffixController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('spot');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('spotmySuffix-detail', {
            parent: 'entity',
            url: '/spotmySuffix/{id}',
            data: {
                authorities: ['ROLE_ADMIN'],
                pageTitle: 'advertiserApp.spot.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/spot/spotmySuffix-detail.html',
                    controller: 'SpotMySuffixDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('spot');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Spot', function($stateParams, Spot) {
                    return Spot.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'spotmySuffix',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('spotmySuffix-detail.edit', {
            parent: 'spotmySuffix-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/spot/spotmySuffix-dialog.html',
                    controller: 'SpotMySuffixDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Spot', function(Spot) {
                            return Spot.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('spotmySuffix.new', {
            parent: 'spotmySuffix',
            url: '/new',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/spot/spotmySuffix-dialog.html',
                    controller: 'SpotMySuffixDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                dateTime: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('spotmySuffix', null, { reload: 'spotmySuffix' });
                }, function() {
                    $state.go('spotmySuffix');
                });
            }]
        })
        .state('spotmySuffix.edit', {
            parent: 'spotmySuffix',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/spot/spotmySuffix-dialog.html',
                    controller: 'SpotMySuffixDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Spot', function(Spot) {
                            return Spot.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('spotmySuffix', null, { reload: 'spotmySuffix' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('spotmySuffix.delete', {
            parent: 'spotmySuffix',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/spot/spotmySuffix-delete-dialog.html',
                    controller: 'SpotMySuffixDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Spot', function(Spot) {
                            return Spot.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('spotmySuffix', null, { reload: 'spotmySuffix' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
