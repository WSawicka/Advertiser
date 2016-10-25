(function() {
    'use strict';
    angular
        .module('advertiserApp')
        .factory('SpotInfo', SpotInfo);

    SpotInfo.$inject = ['$resource'];

    function SpotInfo ($resource) {
        var resourceUrl =  'api/spot-infos/:id';

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
