(function() {
    'use strict';

    angular
        .module('advertiserApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('price-schedule-hourmySuffix', {
            parent: 'entity',
            url: '/price-schedule-hourmySuffix',
            data: {
                authorities: ['ROLE_ADMIN'],
                pageTitle: 'advertiserApp.priceScheduleHour.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/price-schedule-hour/price-schedule-hoursmySuffix.html',
                    controller: 'PriceScheduleHourMySuffixController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('priceScheduleHour');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('price-schedule-hourmySuffix-detail', {
            parent: 'entity',
            url: '/price-schedule-hourmySuffix/{id}',
            data: {
                authorities: ['ROLE_ADMIN'],
                pageTitle: 'advertiserApp.priceScheduleHour.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/price-schedule-hour/price-schedule-hourmySuffix-detail.html',
                    controller: 'PriceScheduleHourMySuffixDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('priceScheduleHour');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'PriceScheduleHour', function($stateParams, PriceScheduleHour) {
                    return PriceScheduleHour.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'price-schedule-hourmySuffix',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('price-schedule-hourmySuffix-detail.edit', {
            parent: 'price-schedule-hourmySuffix-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/price-schedule-hour/price-schedule-hourmySuffix-dialog.html',
                    controller: 'PriceScheduleHourMySuffixDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['PriceScheduleHour', function(PriceScheduleHour) {
                            return PriceScheduleHour.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('price-schedule-hourmySuffix.new', {
            parent: 'price-schedule-hourmySuffix',
            url: '/new',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/price-schedule-hour/price-schedule-hourmySuffix-dialog.html',
                    controller: 'PriceScheduleHourMySuffixDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                hour: null,
                                price: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('price-schedule-hourmySuffix', null, { reload: 'price-schedule-hourmySuffix' });
                }, function() {
                    $state.go('price-schedule-hourmySuffix');
                });
            }]
        })
        .state('price-schedule-hourmySuffix.edit', {
            parent: 'price-schedule-hourmySuffix',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/price-schedule-hour/price-schedule-hourmySuffix-dialog.html',
                    controller: 'PriceScheduleHourMySuffixDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['PriceScheduleHour', function(PriceScheduleHour) {
                            return PriceScheduleHour.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('price-schedule-hourmySuffix', null, { reload: 'price-schedule-hourmySuffix' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('price-schedule-hourmySuffix.delete', {
            parent: 'price-schedule-hourmySuffix',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/price-schedule-hour/price-schedule-hourmySuffix-delete-dialog.html',
                    controller: 'PriceScheduleHourMySuffixDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['PriceScheduleHour', function(PriceScheduleHour) {
                            return PriceScheduleHour.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('price-schedule-hourmySuffix', null, { reload: 'price-schedule-hourmySuffix' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
