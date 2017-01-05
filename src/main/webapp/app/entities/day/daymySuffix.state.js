(function() {
    'use strict';

    angular
        .module('advertiserApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('daymySuffix', {
            parent: 'entity',
            url: '/daymySuffix',
            data: {
                authorities: ['ROLE_ADMIN'],
                pageTitle: 'advertiserApp.day.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/day/daysmySuffix.html',
                    controller: 'DayMySuffixController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('day');
                    $translatePartialLoader.addPart('dayName');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('daymySuffix-detail', {
            parent: 'entity',
            url: '/daymySuffix/{id}',
            data: {
                authorities: ['ROLE_ADMIN'],
                pageTitle: 'advertiserApp.day.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/day/daymySuffix-detail.html',
                    controller: 'DayMySuffixDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('day');
                    $translatePartialLoader.addPart('dayName');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Day', function($stateParams, Day) {
                    return Day.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'daymySuffix',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('daymySuffix-detail.edit', {
            parent: 'daymySuffix-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/day/daymySuffix-dialog.html',
                    controller: 'DayMySuffixDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Day', function(Day) {
                            return Day.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('daymySuffix.new', {
            parent: 'daymySuffix',
            url: '/new',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/day/daymySuffix-dialog.html',
                    controller: 'DayMySuffixDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                number: null,
                                dayName: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('daymySuffix', null, { reload: 'daymySuffix' });
                }, function() {
                    $state.go('daymySuffix');
                });
            }]
        })
        .state('daymySuffix.edit', {
            parent: 'daymySuffix',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/day/daymySuffix-dialog.html',
                    controller: 'DayMySuffixDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Day', function(Day) {
                            return Day.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('daymySuffix', null, { reload: 'daymySuffix' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('daymySuffix.delete', {
            parent: 'daymySuffix',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/day/daymySuffix-delete-dialog.html',
                    controller: 'DayMySuffixDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Day', function(Day) {
                            return Day.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('daymySuffix', null, { reload: 'daymySuffix' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
