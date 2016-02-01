app.factory('noteStorage', ['ngStore', function (ngStore) {
    return ngStore.model('note');
}]);

app.controller('NoteCtrl', ['$scope', '$location', '$filter', 'ngStore', 'noteStorage', function($scope, $location, $filter, ngStore, noteStorage) {
    $scope.items = noteStorage.findAll();
    $scope.newItem = '';

    $scope.addItem = function () {
        var newItem = $scope.newItem.trim();
        if (newItem.length === 0) {
            return;
        }
        var item = {
          id: noteStorage.nextId(),
          title: newItem,
          color: 'bg-white',
          date: Date.now()
        };
        $scope.items.push(noteStorage.create(item));
        $scope.newItem = '';
    };
}]);

app.controller('NoteItemCtrl', ['$scope', 'noteStorage', '$state', '$stateParams', function($scope, noteStorage, $state, $stateParams) {
    $scope.colors = ['bg-white', 'indigo', 'pink', 'red', 'amber', 'blue', 'green'];
    $scope.item = noteStorage.find($stateParams);
    $scope.$watch("item", function() {
      noteStorage.update($scope.item);
    }, true);

    $scope.removeItem = function(item){
      noteStorage.destroy(item);
      $state.go('app.note.list');
    }
}]);
