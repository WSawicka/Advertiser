(function() {
    'use strict';

    angular
        .module('advertiserApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('weekmySuffix', {
            parent: 'entity',
            url: '/weekmySuffix',
            data: {
                authorities: ['ROLE_ADMIN'],
                pageTitle: 'advertiserApp.week.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/week/weeksmySuffix.html',
                    controller: 'WeekMySuffixController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('week');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('weekmySuffix-detail', {
            parent: 'entity',
            url: '/weekmySuffix/{id}',
            data: {
                authorities: ['ROLE_ADMIN'],
                pageTitle: 'advertiserApp.week.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/week/weekmySuffix-detail.html',
                    controller: 'WeekMySuffixDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('week');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Week', function($stateParams, Week) {
                    return Week.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'weekmySuffix',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('weekmySuffix-detail.edit', {
            parent: 'weekmySuffix-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/week/weekmySuffix-dialog.html',
                    controller: 'WeekMySuffixDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Week', function(Week) {
                            return Week.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('weekmySuffix.new', {
            parent: 'weekmySuffix',
            url: '/new',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/week/weekmySuffix-dialog.html',
                    controller: 'WeekMySuffixDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                number: null,
                                year: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('weekmySuffix', null, { reload: 'weekmySuffix' });
                }, function() {
                    $state.go('weekmySuffix');
                });
            }]
        })
        .state('weekmySuffix.edit', {
            parent: 'weekmySuffix',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/week/weekmySuffix-dialog.html',
                    controller: 'WeekMySuffixDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Week', function(Week) {
                            return Week.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('weekmySuffix', null, { reload: 'weekmySuffix' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('weekmySuffix.delete', {
            parent: 'weekmySuffix',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/week/weekmySuffix-delete-dialog.html',
                    controller: 'WeekMySuffixDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Week', function(Week) {
                            return Week.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('weekmySuffix', null, { reload: 'weekmySuffix' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
