(function() {
    'use strict';
    angular
        .module('advertiserApp')
        .factory('Spot', Spot);

    Spot.$inject = ['$resource', 'DateUtils'];

    function Spot ($resource, DateUtils) {
        var resourceUrl =  'api/spots/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.dateTime = DateUtils.convertDateTimeFromServer(data.dateTime);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
