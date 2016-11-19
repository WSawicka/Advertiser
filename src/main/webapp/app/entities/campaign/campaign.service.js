(function() {
    'use strict';
    angular
        .module('advertiserApp')
        .factory('Campaign', Campaign);

    Campaign.$inject = ['$resource', 'DateUtils'];

    function Campaign ($resource, DateUtils) {
        var resourceUrl =  'api/campaigns/:id';

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
            'update': { method:'PUT' },
            'getAvailableCampaigns': {
                method: 'GET',
                isArray: true,
                url: 'api/campaignsBefore/dateTime/:dateTime',
                params: {
                    dateTime: '@dateTime'
                }
            }
        });
    }
})();
