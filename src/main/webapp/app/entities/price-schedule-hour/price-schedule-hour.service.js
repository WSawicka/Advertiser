(function() {
    'use strict';
    angular
        .module('advertiserApp')
        .factory('PriceScheduleHour', PriceScheduleHour);

    PriceScheduleHour.$inject = ['$resource'];

    function PriceScheduleHour ($resource) {
        var resourceUrl =  'api/price-schedule-hours/:id';

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
            'update': { method:'PUT' }
        });
    }
})();
