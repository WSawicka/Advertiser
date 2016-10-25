(function() {
    'use strict';

    angular
        .module('advertiserApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('businessmySuffix', {
            parent: 'entity',
            url: '/businessmySuffix',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'advertiserApp.business.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/business/businessesmySuffix.html',
                    controller: 'BusinessMySuffixController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('business');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('businessmySuffix-detail', {
            parent: 'entity',
            url: '/businessmySuffix/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'advertiserApp.business.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/business/businessmySuffix-detail.html',
                    controller: 'BusinessMySuffixDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('business');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Business', function($stateParams, Business) {
                    return Business.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'businessmySuffix',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('businessmySuffix-detail.edit', {
            parent: 'businessmySuffix-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/business/businessmySuffix-dialog.html',
                    controller: 'BusinessMySuffixDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Business', function(Business) {
                            return Business.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('businessmySuffix.new', {
            parent: 'businessmySuffix',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/business/businessmySuffix-dialog.html',
                    controller: 'BusinessMySuffixDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('businessmySuffix', null, { reload: 'businessmySuffix' });
                }, function() {
                    $state.go('businessmySuffix');
                });
            }]
        })
        .state('businessmySuffix.edit', {
            parent: 'businessmySuffix',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/business/businessmySuffix-dialog.html',
                    controller: 'BusinessMySuffixDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Business', function(Business) {
                            return Business.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('businessmySuffix', null, { reload: 'businessmySuffix' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('businessmySuffix.delete', {
            parent: 'businessmySuffix',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/business/businessmySuffix-delete-dialog.html',
                    controller: 'BusinessMySuffixDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Business', function(Business) {
                            return Business.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('businessmySuffix', null, { reload: 'businessmySuffix' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
