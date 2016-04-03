(function () {
    angular.module('phone_book', [])
        .controller('phone_book_controller', ['$scope', '$http', function ($scope, $http) {
            $scope.query = '';
            $scope.records = [];
            $scope.getAllRecords = function () {
                $http.get('/records').success(
                    function (response) {
                        $scope.records = response;
                    })
            };
            //Search by substring
            $scope.search = function () {
                $http.get('/records', {
                    params: {people: $scope.query}
                }).success(
                    function (response) {
                        $scope.records = response;
                    })
            };
            $scope.init = function () {
                $scope.getAllRecords();
            };
        }]);
}());