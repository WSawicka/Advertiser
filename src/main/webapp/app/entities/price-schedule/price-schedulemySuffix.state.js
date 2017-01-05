(function() {
    'use strict';

    angular
        .module('advertiserApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('price-schedulemySuffix', {
            parent: 'entity',
            url: '/price-schedulemySuffix',
            data: {
                authorities: ['ROLE_ADMIN'],
                pageTitle: 'advertiserApp.priceSchedule.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/price-schedule/price-schedulesmySuffix.html',
                    controller: 'PriceScheduleMySuffixController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('priceSchedule');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('price-schedulemySuffix-detail', {
            parent: 'entity',
            url: '/price-schedulemySuffix/{id}',
            data: {
                authorities: ['ROLE_ADMIN'],
                pageTitle: 'advertiserApp.priceSchedule.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/price-schedule/price-schedulemySuffix-detail.html',
                    controller: 'PriceScheduleMySuffixDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('priceSchedule');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'PriceSchedule', function($stateParams, PriceSchedule) {
                    return PriceSchedule.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'price-schedulemySuffix',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('price-schedulemySuffix-detail.edit', {
            parent: 'price-schedulemySuffix-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/price-schedule/price-schedulemySuffix-dialog.html',
                    controller: 'PriceScheduleMySuffixDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['PriceSchedule', function(PriceSchedule) {
                            return PriceSchedule.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('price-schedulemySuffix.new', {
            parent: 'price-schedulemySuffix',
            url: '/new',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/price-schedule/price-schedulemySuffix-dialog.html',
                    controller: 'PriceScheduleMySuffixDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                startDate: null,
                                endDate: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('price-schedulemySuffix', null, { reload: 'price-schedulemySuffix' });
                }, function() {
                    $state.go('price-schedulemySuffix');
                });
            }]
        })
        .state('price-schedulemySuffix.edit', {
            parent: 'price-schedulemySuffix',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/price-schedule/price-schedulemySuffix-dialog.html',
                    controller: 'PriceScheduleMySuffixDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['PriceSchedule', function(PriceSchedule) {
                            return PriceSchedule.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('price-schedulemySuffix', null, { reload: 'price-schedulemySuffix' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('price-schedulemySuffix.delete', {
            parent: 'price-schedulemySuffix',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/price-schedule/price-schedulemySuffix-delete-dialog.html',
                    controller: 'PriceScheduleMySuffixDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['PriceSchedule', function(PriceSchedule) {
                            return PriceSchedule.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('price-schedulemySuffix', null, { reload: 'price-schedulemySuffix' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
