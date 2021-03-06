(function() {
    'use strict';

    angular
        .module('advertiserApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('calendar', {
                parent: 'app',
                url: '/calendarView',
                data: {
                    authorities: ['ROLE_ADMIN', 'ROLE_INSPECTOR']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/calendar/calendar.html',
                        controller: 'CalendarController',
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
            .state('calendar-details', {
                parent: 'calendar',
                url: '/details/{date}/{hour}',
                params: {
                    dateTime: '@dateTime',
                    dateJSON: '@dateJSON',
                    date: '@date',
                    hour: '@hour',
                    hourId: '@hourId'
                },
                data: {
                    authorities: ['ROLE_ADMIN', 'ROLE_INSPECTOR']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/calendar/calendar-details.html',
                        controller: 'CalendarDetailsController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    campaignDTO: null,
                                    campaignId: null,
                                    dateTime: null,
                                    spotInfoId: null,
                                    spotName: null,
                                    spotNumber: null
                                };
                            }
                        }
                    }).result.then(function() {
                        $state.go('calendar', null, { reload: 'calendar' });
                    }, function() {
                        $state.go('calendar');
                    });
                }]
            })
            .state('calendar-details.delete', {
                parent: 'calendar-details',
                url: '/{id}/delete',
                params : {
                    spots: '@spots',
                    uibMI: '@uibMI'
                },
                data: {
                    authorities: ['ROLE_ADMIN']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/calendar/calendar-details-delete.html',
                        controller: 'CalendarDetailsDeleteController',
                        controllerAs: 'vm',
                        size: 'md',
                        resolve: {
                            entity: ['Spot', function(Spot) {
                                return Spot.get({id : $stateParams.id}).$promise;
                            }]
                        }
                    }).result.then(function() {
                        $state.go('calendar-details', null, { reload: 'calendar-details' });
                    }, function() {
                        $state.go('calendar-details');
                    });
                }]
            })
    }
})();
