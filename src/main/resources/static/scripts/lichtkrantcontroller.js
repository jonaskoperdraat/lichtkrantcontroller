//inject angular file upload directives and services.
var app = angular.module('lichtkrantControllerApp', ['ngFileUpload']);

app.controller('lichtkrantControllerCtrl', ['$scope', 'Upload', '$timeout', '$http', function ($scope, Upload, $timeout, $http) {

    $scope.uploadShow = function(file) {
        file.upload = Upload.upload({
            url: '/show/open',
            data: {file: file},
        });

        file.upload.then(function (response) {
            $timeout(function () {
                file.result = response.data;
                $('.dropdown.open .dropdown-toggle').dropdown('toggle');
                $scope.loadShow();
            });
        }, function (response) {
            if (response.status > 0)
                $scope.errorMsg = response.status + ': ' + response.data;
        }, function (evt) {
            // Math.min is to fix IE which reports 200% sometimes
            file.progress = Math.min(100, parseInt(100.0 * evt.loaded / evt.total));
        });
    };

    $scope.loadShow = function() {
        $http.get('/show').
            success(function(data) {
                if (data.uuid != null) {
                    var currentPage = data.status.currentPage;
                    if (currentPage < data.pages.length)
                        data.pages[currentPage].isCurrent = true;
                    if (currentPage < data.pages.length - 1)
                        data.pages[currentPage + 1].isNext = true;
                    if (currentPage ==   data.pages.length - 1)
                        data.pages[currentPage].isNext = true;
                    $scope.showData = data;
                }
            });
    };

    $scope.goToPage = function(index) {
        console.log('goToPage(' + index + ')');
        $http.get('/show/goToPage/' + index).
            success(function(data) {
                $scope.loadShow();
            });
            // TODO: use ShowStatus object that is being returned to update FE instead of re-loading entire show.
            // TODO: handle error situations.
    };
    
    $scope.moveNextPointerTo = function(index) {
        console.log('moveNextPointerTo(' + index + ')');
        // Prevent the index going out of bounds.
        index = Math.min($scope.showData.pages.length-1, Math.max(0, index));
        // Traverse the pages, removing the current 'isNext' variable and setting 'isNext' on the correct page.
        for(var i = 0; i < $scope.showData.pages.length; i++) {
            delete $scope.showData.pages[i].isNext;
            $scope.showData.pages[i].isNext = i == index;
        }
    };

    (function() {
        $scope.loadShow();
    })()

}]);

$(document).on('change', '.btn-file :file', function() {
    var input = $(this),
        numFiles = input.get(0).files ? input.get(0).files.length : 1,
        label = input.val().replace(/\\/g, '/').replace(/.*\//, '');
    input.trigger('fileselect', [numFiles, label]);
});

$(document).ready( function() {
    $('.btn-file :file').on('fileselect', function(event, numFiles, label) {

        var input = $(this).parents('.input-group').find(':text'),
            log = numFiles > 1 ? numFiles + ' files selected' : label;

        if( input.length ) {
            input.val(log);
        } else {
            if( log ) alert(log);
        }

    });
});