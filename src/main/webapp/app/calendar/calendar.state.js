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
                    authorities: ['ROLE_USER'],
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
                    date: '@date',
                    hour: '@hour',
                    spots: '@spots',
                    campaigns: 'campaigns'
                },
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/calendar/calendar-details.html',
                        controller: 'CalendarDetailsController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg'
                    }).result.then(function() {
                        $state.go('calendar', null, { reload: 'calendar' });
                    }, function() {
                        $state.go('calendar');
                    });
                }]
            })
            /*.state('calendar-details.edit', {
                parent: 'calendar-details',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/calendar/calendar-details-edit.html',
                        controller: 'CalendarDetailsEditController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: ['Spot', function(Spot) {
                                return Spot.get({id : $stateParams.id}).$promise;
                            }]
                        }
                    }).result.then(function() {
                        $state.go('calendar', {}, { reload: 'calendar' });
                    }, function() {
                        $state.go('calendar');
                    });
                }]
            })*/
    }
})();
