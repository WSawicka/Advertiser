(function() {
    'use strict';
    angular
        .module('advertiserApp')
        .factory('PriceSchedule', PriceSchedule);

    PriceSchedule.$inject = ['$resource', 'DateUtils'];

    function PriceSchedule ($resource, DateUtils) {
        var resourceUrl =  'api/price-schedules/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.startDate = DateUtils.convertDateTimeFromServer(data.startDate);
                        data.endDate = DateUtils.convertDateTimeFromServer(data.endDate);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
