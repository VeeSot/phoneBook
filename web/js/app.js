(function () {
    angular.module('phone_book', [])
        .controller('phone_book_controller', ['$scope', '$filter', '$http', function ($scope, $filter, $http) {
            $scope.query = '';
            $scope.records = [];
            $scope.newRecord = {};

            $scope.getAllRecords = function () {
                $http.get('/records').success(
                    function (response) {
                        $scope.records = $filter('orderBy')(response, 'people');
                    })
            };

            //Search by substring
            $scope.search = function () {
                $http.get('/records', {
                    params: {people: $scope.query}
                }).success(
                    function (response) {
                        $scope.records = $filter('orderBy')(response, 'people');
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
                    newRecord['recordId'] = response.data.id;
                    $scope.records.push(newRecord);
                    $scope.records = $filter('orderBy')($scope.records, 'people');
                    $scope.newRecord = {}
                }, function errorCallback(response) {
                    var data = response.data;
                    if (data.id == 0 && data.message == "Contact exist!") {
                        $.notify("Данная запись уже существует в справочнике", "error");
                    }
                    else {
                        console.log(response);
                        $.notify("Кажется что то поломалось.\nМы будем благодарны, если Вы сообщите об этом", "error");
                    }


                });

            };


            //Remove model on the server and local
            $scope.removeRecord = function (idx) {
                var recordToDelete = $scope.records[idx];
                $http.delete('/records/' + recordToDelete.recordId).then(function successCallback(response) {
                    if (response.status == 204) {
                        $scope.records.splice(idx, 1);//Выкинем из списка моделей
                        $.notify("Запись была успешно удалена из справочника", "success");
                    }
                }, function errorCallback(response) {
                    console.log(response);
                    $.notify("Кажется что то поломалось.\nМы будем благодарны, если Вы сообщите об этом", "error");
                });

            };

            $scope.init = function () {
                $scope.getAllRecords();
            };


        }]);
}());