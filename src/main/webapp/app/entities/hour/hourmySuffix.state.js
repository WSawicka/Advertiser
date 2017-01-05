(function() {
    'use strict';

    angular
        .module('advertiserApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('hourmySuffix', {
            parent: 'entity',
            url: '/hourmySuffix',
            data: {
                authorities: ['ROLE_ADMIN'],
                pageTitle: 'advertiserApp.hour.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/hour/hoursmySuffix.html',
                    controller: 'HourMySuffixController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('hour');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('hourmySuffix-detail', {
            parent: 'entity',
            url: '/hourmySuffix/{id}',
            data: {
                authorities: ['ROLE_ADMIN'],
                pageTitle: 'advertiserApp.hour.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/hour/hourmySuffix-detail.html',
                    controller: 'HourMySuffixDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('hour');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Hour', function($stateParams, Hour) {
                    return Hour.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'hourmySuffix',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('hourmySuffix-detail.edit', {
            parent: 'hourmySuffix-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/hour/hourmySuffix-dialog.html',
                    controller: 'HourMySuffixDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Hour', function(Hour) {
                            return Hour.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('hourmySuffix.new', {
            parent: 'hourmySuffix',
            url: '/new',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/hour/hourmySuffix-dialog.html',
                    controller: 'HourMySuffixDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                number: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('hourmySuffix', null, { reload: 'hourmySuffix' });
                }, function() {
                    $state.go('hourmySuffix');
                });
            }]
        })
        .state('hourmySuffix.edit', {
            parent: 'hourmySuffix',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/hour/hourmySuffix-dialog.html',
                    controller: 'HourMySuffixDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Hour', function(Hour) {
                            return Hour.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('hourmySuffix', null, { reload: 'hourmySuffix' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('hourmySuffix.delete', {
            parent: 'hourmySuffix',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/hour/hourmySuffix-delete-dialog.html',
                    controller: 'HourMySuffixDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Hour', function(Hour) {
                            return Hour.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('hourmySuffix', null, { reload: 'hourmySuffix' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
