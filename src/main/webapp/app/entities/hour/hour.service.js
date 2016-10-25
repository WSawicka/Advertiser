(function() {
    'use strict';
    angular
        .module('advertiserApp')
        .factory('Hour', Hour);

    Hour.$inject = ['$resource'];

    function Hour ($resource) {
        var resourceUrl =  'api/hours/:id';

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
