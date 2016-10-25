(function() {
    'use strict';

    angular
        .module('advertiserApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('reportmySuffix', {
            parent: 'entity',
            url: '/reportmySuffix',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'advertiserApp.report.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/report/reportsmySuffix.html',
                    controller: 'ReportMySuffixController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('report');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('reportmySuffix-detail', {
            parent: 'entity',
            url: '/reportmySuffix/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'advertiserApp.report.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/report/reportmySuffix-detail.html',
                    controller: 'ReportMySuffixDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('report');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Report', function($stateParams, Report) {
                    return Report.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'reportmySuffix',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('reportmySuffix-detail.edit', {
            parent: 'reportmySuffix-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/report/reportmySuffix-dialog.html',
                    controller: 'ReportMySuffixDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Report', function(Report) {
                            return Report.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('reportmySuffix.new', {
            parent: 'reportmySuffix',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/report/reportmySuffix-dialog.html',
                    controller: 'ReportMySuffixDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                startDate: null,
                                endDate: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('reportmySuffix', null, { reload: 'reportmySuffix' });
                }, function() {
                    $state.go('reportmySuffix');
                });
            }]
        })
        .state('reportmySuffix.edit', {
            parent: 'reportmySuffix',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/report/reportmySuffix-dialog.html',
                    controller: 'ReportMySuffixDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Report', function(Report) {
                            return Report.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('reportmySuffix', null, { reload: 'reportmySuffix' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('reportmySuffix.delete', {
            parent: 'reportmySuffix',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/report/reportmySuffix-delete-dialog.html',
                    controller: 'ReportMySuffixDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Report', function(Report) {
                            return Report.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('reportmySuffix', null, { reload: 'reportmySuffix' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
