(function() {
    'use strict';

    angular
        .module('advertiserApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('spot-infomySuffix', {
            parent: 'entity',
            url: '/spot-infomySuffix',
            data: {
                authorities: ['ROLE_ADMIN'],
                pageTitle: 'advertiserApp.spotInfo.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/spot-info/spot-infosmySuffix.html',
                    controller: 'SpotInfoMySuffixController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('spotInfo');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('spot-infomySuffix-detail', {
            parent: 'entity',
            url: '/spot-infomySuffix/{id}',
            data: {
                authorities: ['ROLE_ADMIN'],
                pageTitle: 'advertiserApp.spotInfo.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/spot-info/spot-infomySuffix-detail.html',
                    controller: 'SpotInfoMySuffixDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('spotInfo');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'SpotInfo', function($stateParams, SpotInfo) {
                    return SpotInfo.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'spot-infomySuffix',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('spot-infomySuffix-detail.edit', {
            parent: 'spot-infomySuffix-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/spot-info/spot-infomySuffix-dialog.html',
                    controller: 'SpotInfoMySuffixDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['SpotInfo', function(SpotInfo) {
                            return SpotInfo.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('spot-infomySuffix.new', {
            parent: 'spot-infomySuffix',
            url: '/new',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/spot-info/spot-infomySuffix-dialog.html',
                    controller: 'SpotInfoMySuffixDialogController',
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
                    $state.go('spot-infomySuffix', null, { reload: 'spot-infomySuffix' });
                }, function() {
                    $state.go('spot-infomySuffix');
                });
            }]
        })
        .state('spot-infomySuffix.edit', {
            parent: 'spot-infomySuffix',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/spot-info/spot-infomySuffix-dialog.html',
                    controller: 'SpotInfoMySuffixDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['SpotInfo', function(SpotInfo) {
                            return SpotInfo.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('spot-infomySuffix', null, { reload: 'spot-infomySuffix' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('spot-infomySuffix.delete', {
            parent: 'spot-infomySuffix',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/spot-info/spot-infomySuffix-delete-dialog.html',
                    controller: 'SpotInfoMySuffixDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['SpotInfo', function(SpotInfo) {
                            return SpotInfo.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('spot-infomySuffix', null, { reload: 'spot-infomySuffix' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
