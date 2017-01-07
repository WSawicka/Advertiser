(function() {
    'use strict';
    angular
        .module('advertiserApp')
        .factory('Week', Week);

    Week.$inject = ['$resource'];

    function Week ($resource) {
        var resourceUrl =  'api/weeks/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' },
            'getWeekFromYear' : {
                method: 'GET',
                url: 'api/year/:year/weeks/:weekNumber',
                params: {
                    year: '@year',
                    weekNumber: '@weekNumber'
                }
            },
            'getSpotEvents' : {
                method: 'GET',
                isArray: true,
                url: 'api/events/year/:year/weeks/:weekNumber',
                params: {
                    year: '@year',
                    weekNumber: '@weekNumber'
                }
            }
        });
    }
})();
