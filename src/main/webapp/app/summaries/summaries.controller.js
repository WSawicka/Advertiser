(function() {
    'use strict';

    angular
        .module('advertiserApp')
        .controller('SummariesController', SummariesController);

    SummariesController.$inject = ['$scope', 'Principal', 'LoginService', '$state', 'Week', 'Day'];

    function SummariesController($scope, Principal, LoginService, $state, Week, Day) {
        var vm = this;
        vm.authorities = ['ROLE_ADMIN'];

        vm.months = [ {name:"January", value:0}, {name:"February", value:1}, {name:"March", value:2},
            {name:"April", value:3}, {name:"May", value:4}, {name:"June", value:5}, {name:"July", value:6},
            {name:"August", value:7}, {name:"September", value:8}, {name:"October", value:9},
            {name:"November", value:10}, {name:"December", value:11}];
        vm.year = 2017;
        vm.selectedMonth = vm.months[0];

        vm.showPerMonth = false;
        vm.campaignsInMonthData = [];
        vm.campaignsInMonthCategories = [];
        vm.spotsInMonthData = [];
        vm.spotsInMonthCategories = [];

        vm.showPerYear = false;
        vm.yearCategories = [];
        vm.campaignsInYearData = [];
        vm.spotsInYearData = [];
        var campaignYearIDs = [[],[],[],[],[],[],[],[],[],[],[],[]];

        $(function () {
            function createCampaignMonthChart() {
                $('#campaignsPerMonthContainer').highcharts({
                    chart: {type: 'line'},
                    title: {text: 'Campaigns in ' + vm.selectedMonth},
                    xAxis: { categories: vm.campaignsInMonthCategories },
                    yAxis: {min: 0, title: {text: 'Amount'}},
                    tooltip: {
                        headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
                        pointFormat: '<tr><td style="color:{series.color};padding:0"></td>' +
                        '<td style="padding:0"><b>{point.y:.1f}</b></td></tr>',
                        footerFormat: '</table>',
                        shared: true,
                        useHTML: true
                    },
                    plotOptions: {column: {pointPadding: 0.2, borderWidth: 0}},
                    series: [{ name: 'Campaigns', data: vm.campaignsInMonthData }]
                });
            }

            function createSpotMonthChart() {
                $('#spotsPerMonthContainer').highcharts({
                    chart: {type: 'line'},
                    title: {text: 'Spots in ' + vm.selectedMonth},
                    xAxis: { categories: vm.spotsInMonthCategories },
                    yAxis: {min: 0, title: {text: 'Amount'}},
                    tooltip: {
                        headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
                        pointFormat: '<tr><td style="color:{series.color};padding:0"></td>' +
                        '<td style="padding:0"><b>{point.y:.1f}</b></td></tr>',
                        footerFormat: '</table>',
                        shared: true,
                        useHTML: true
                    },
                    plotOptions: {column: {pointPadding: 0.2, borderWidth: 0}},
                    series: [{ name: 'Spots', data: vm.spotsInMonthData }]
                });
            }

            function createCampaignYearChart() {
                $('#campaignsPerYearContainer').highcharts({
                    chart: {type: 'line'},
                    title: {text: 'Campaigns in ' + vm.year},
                    xAxis: { categories: vm.yearCategories },
                    yAxis: {min: 0, title: {text: 'Amount'}},
                    tooltip: {
                        headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
                        pointFormat: '<tr><td style="color:{series.color};padding:0"></td>' +
                        '<td style="padding:0"><b>{point.y:.1f}</b></td></tr>',
                        footerFormat: '</table>',
                        shared: true,
                        useHTML: true
                    },
                    plotOptions: {column: {pointPadding: 0.2, borderWidth: 0}},
                    series: [{ name: 'Campaigns', data: vm.campaignsInYearData }]
                });
            }

            function createSpotYearChart() {
                $('#spotsPerYearContainer').highcharts({
                    chart: {type: 'line'},
                    title: {text: 'Spots in ' + vm.year},
                    xAxis: { categories: vm.yearCategories },
                    yAxis: {min: 0, title: {text: 'Amount'}},
                    tooltip: {
                        headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
                        pointFormat: '<tr><td style="color:{series.color};padding:0"></td>' +
                        '<td style="padding:0"><b>{point.y:.1f}</b></td></tr>',
                        footerFormat: '</table>',
                        shared: true,
                        useHTML: true
                    },
                    plotOptions: {column: {pointPadding: 0.2, borderWidth: 0}},
                    series: [{ name: 'Spots', data: vm.spotsInYearData }]
                });
            }

            function getCampaignAmountIn(day) {
                var campaignIds = [];
                for (var h in day.hours) {
                    var hour = day.hours[h];
                    for (var s in hour.spots) {
                        var spot = hour.spots[s];
                        if ($.inArray(spot.campaignId, campaignIds) == -1) {
                            campaignIds.push(spot.campaignId);
                        }
                    }
                }
                return campaignIds.length;
            }

            function setCampaignAmountIn(day, month) { //used for campaigns per year - to prevent counting duplicates
                for (var h in day.hours) {
                    var hour = day.hours[h];
                    for (var s in hour.spots) {
                        var spot = hour.spots[s];
                        if ($.inArray(spot.campaignId, campaignYearIDs[month]) == -1) {
                            campaignYearIDs[month].push(spot.campaignId);
                        }
                    }
                }
            }

            function getSpotAmountIn(day) {
                var spots = 0;
                for (var h in day.hours) {
                    var hour = day.hours[h];
                    for(var s in hour.spots)
                        spots++;
                }
                return spots;
            }

            $(document).ready(function () {
                $scope.setMonth = function () {
                    var d = moment.utc(0);
                    var startDate = moment(d).year(vm.year).month(vm.selectedMonth).date(1).toJSON();
                    var endDate = moment(startDate).endOf('month'); //2016-12-26T00:00:00+01:00
                    endDate = moment(endDate).utcOffset("+00:00").toJSON();
                    Day.getAllBetween({startDate: startDate, endDate: endDate},
                        function (result) {
                            var days = result;
                            for (var d in days) {
                                var day = days[d];
                                vm.campaignsInMonthCategories[d] = parseInt(d) + 1;
                                vm.campaignsInMonthData[d] = getCampaignAmountIn(day);
                                vm.spotsInMonthCategories[d] = parseInt(d) + 1;
                                vm.spotsInMonthData[d] = getSpotAmountIn(day);
                            }
                            createCampaignMonthChart();
                            createSpotMonthChart();
                            vm.showPerMonth = true;
                        });
                };

                $scope.setYear = function () {
                    var d = moment.utc(0);
                    var startDate = moment(d).year(vm.year).month(0).date(1).toJSON();
                    var endDate = moment(d).year(vm.year+1).month(0).date(1).toJSON();
                    endDate = moment(endDate).subtract(1, 'hours').toJSON();

                    Day.getAllBetween({startDate: startDate, endDate: endDate},
                        function (result) {
                            var days = result;

                            for (var m in vm.months){
                                var month = vm.months[m];
                                vm.yearCategories[m] = month.name;
                                vm.campaignsInYearData[m] = 0;
                                vm.spotsInYearData[m] = 0;
                            }

                            for (var d in days){
                                var day = days[d];
                                if (moment(day.dateTime).year() == vm.year) {
                                    var month = moment(day.dateTime).month();
                                    setCampaignAmountIn(day, month);
                                    var amountOfSpots = getSpotAmountIn(day);
                                    vm.spotsInYearData[month] += amountOfSpots;
                                }
                            }

                            for (var c in campaignYearIDs){
                                vm.campaignsInYearData[c] = campaignYearIDs[c].length;
                            }
                            createCampaignYearChart();
                            createSpotYearChart();
                            vm.showPerYear = true;
                        });
                };

                $scope.resetPerMonth = function () {
                    vm.showPerMonth = false;
                    $("#campaignsPerMonthContainer").empty();
                    $("#spotsPerMonthContainer").empty();
                    vm.campaignsInMonthCategories = [];
                    vm.campaignsInMonthData = [];
                    vm.spotsInMonthCategories = [];
                    vm.spotsInMonthData = [];
                };

                $scope.resetPerYear = function () {
                    vm.showPerYear = false;
                    $("#campaignsPerYearContainer").empty();
                    $("#spotsPerYearContainer").empty();
                    vm.campaignsInYearCategories = [];
                    vm.campaignsInYearData = [];
                    vm.spotsInYearCategories = [];
                    vm.spotsInYearData = [];
                };
            });
        });
    }
})();
