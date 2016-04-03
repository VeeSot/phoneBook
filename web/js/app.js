(function () {
    angular.module('phone_book', [])
        .controller('phone_book_controller', ['$scope', '$http', function ($scope, $http) {
            $scope.query = '';
            $scope.records = [];
            $scope.newRecord = {};
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
            //Add new model to server
            $scope.addNewRecord = function () {
                //Validation
                if (!$scope.newRecord.number) {
                    $.notify("Не указан номер телефона", "error");
                    return false;
                }
                if (!$scope.newRecord.people) {
                    $.notify("Не указано имя абонента", "error");
                    return false;
                }

                $http.post('/records', $scope.newRecord
                ).then(function successCallback(response) {
                    console.log(response);
                    $.notify("Запись успешно добавлена в справочник", "success");
                    var newRecord = $scope.newRecord;
                    newRecord['id'] = response.id;
                    $scope.records.push(newRecord);
                    $scope.newRecord = {}
                }, function errorCallback(response) {
                    var data = response.data;
                    if (data.id == 0 && data.message == "Contact exist!"){
                        $.notify("Данная запись уже существует в справочнике", "error");
                    }
                    else{
                        $.notify("Кажется что то поломалось.\nМы будем благодарны, если Вы сообщите об этом", "error");
                    }


                });

            };
            $scope.init = function () {
                $scope.getAllRecords();
            };
        }]);
}());