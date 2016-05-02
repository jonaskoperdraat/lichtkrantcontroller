//inject angular file upload directives and services.
var app = angular.module('lichtkrantControllerApp', ['ngFileUpload']);

app.controller('lichtkrantControllerCtrl', ['$scope', 'Upload', '$timeout', '$http', function ($scope, Upload, $timeout, $http) {

    $http.get('/info').
        success(function(data) {
            $scope.VERSION = data["applicationVersion"];
        });

    $scope.nextPointerIndex = 1;

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
                    if (currentPage < data.pages.length - 1) {
                        data.pages[currentPage + 1].isNext = true;
                        nextPointerIndex = currentPage + 1;
                    }
                    if (currentPage == data.pages.length - 1) {
                        data.pages[currentPage].isNext = true;
                        nextPointerIndex = currentPage;
                    }
                    $scope.showData = data;
                    $scope.moveNextPointerTo(nextPointerIndex);
                    // $scope.$apply();
//                     $scope.scrollToNextPointer();
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
        $scope.nextPointerIndex = Math.min($scope.showData.pages.length-1, Math.max(0, index));
        // Traverse the pages, removing the current 'isNext' variable and setting 'isNext' on the correct page.
        for(var i = 0; i < $scope.showData.pages.length; i++) {
            delete $scope.showData.pages[i].isNext;
            $scope.showData.pages[i].isNext = i == $scope.nextPointerIndex;
        }
        $scope.scrollToNextPointer();
    };

    $scope.scrollToNextPointer = function() {
        //var paddingpx = $(window).height() / 2;
        //$('.well.next').animatescroll({padding : paddingpx + 'px'});
        // $('.well.next').animatescroll();
        $timeout(
            $('.well.next').animatescroll({padding: Math.floor($(window).height()/2 - $('.well.next').height())})
        )
    };


    $scope.moveNextPointerBackward = function() {
        $scope.moveNextPointerTo($scope.nextPointerIndex - 1)
    };

    $scope.moveNextPointerForward = function() {
        $scope.moveNextPointerTo($scope.nextPointerIndex + 1)
    };

    $scope.$on('keydown', function(onEvent, keypressEvent) {
        console.log('received keydown notification');
        switch (keypressEvent.which) {
            case 13 :
                // console.log('Handling enter...');
                $scope.goToPage($scope.nextPointerIndex);
                keypressEvent.preventDefault();
                break;
            case 38: // Up
            case 75: // K
                // console.log('Handling up arrow...');
                $scope.moveNextPointerBackward();
                keypressEvent.preventDefault();
                break;
            case 40: // Down
            case 74: // J
                // console.log('Handling down arrow');
                $scope.moveNextPointerForward();
                keypressEvent.preventDefault();
            default:
                break;
        }
        $scope.$apply();
    });

    (function() {
        $scope.loadShow();
    })()

}]);

app.directive('keyListener', [
    '$document',
    '$rootScope',
    function ($document, $rootScope) {
        return {
            restrict: 'A',
            link: function () {
                $document.bind('keydown', function (e) {
                    console.log('Got keydown:', e.which);
                    $rootScope.$broadcast('keydown', e)
                });
            }
        };
    }
]);

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