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
            },
            'checkIfPossible' : {
                method : 'GET',
                isArray: true,
                url : 'api/generate/campaign/check/:hoursPreferred/:startDate/:endDate',
                params : {
                    hoursPreferred: '@hoursPreferred',
                    startDate: '@startDate',
                    endDate: '@endDate'
                }
            },
            'generateSpotsInCampaign' : {
                method : 'GET',
                isArray: true,
                url : 'api/generate/campaign/:generationForm/:id/:toGenerate/:spotInfoId/:spotsLimit/:hoursPreferred/:peaks',
                params : {
                    generationForm: '@generationForm',
                    id: '@id',
                    toGenerate: '@toGenerate',
                    spotInfoId: '@spotInfoId',
                    spotsLimit: '@spotsLimit',
                    hoursPreferred: '@hoursPreferred',
                    peaks: '@peaks'
                }
            },
            'getAllCampaignStates' : {
                method: 'GET',
                isArray: true,
                url: 'api/campaignStates'
            },
            'getAllCampaignBusinesses' : {
                method: 'GET',
                isArray: true,
                url: 'api/campaignBusinesses'
            },
            'getCampaignsWithAmounts': {
                method: 'GET',
                isArray: true,
                url: 'api/campaigns/all/withAmounts'
            },
            'getCampaignsWithAmountsOfYear': {
                method: 'GET',
                isArray: true,
                url: 'api/:year/campaigns/all/withAmounts',
                params: {
                    year: '@year'
                }
            },
            'getCampaignsWithAmountsAndPricesOfYear': {
                method: 'GET',
                isArray: true,
                url: 'api/:year/campaigns/all/withAmounts/withPrice',
                params: {
                    year: '@year'
                }
            },
            'getCampaign':{
                method: 'GET',
                url: 'api/campaign/:campaignId',
                params: {
                    campaignId: '@campaignId'
                }
            },
            'getReportGeneral': {
                method: 'GET',
                url: 'api/report/:year',
                params: {
                    year: '@year'
                }
            }

        });
    }
})();
