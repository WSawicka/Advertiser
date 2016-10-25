(function() {
    'use strict';
    angular
        .module('advertiserApp')
        .factory('Business', Business);

    Business.$inject = ['$resource'];

    function Business ($resource) {
        var resourceUrl =  'api/businesses/:id';

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
