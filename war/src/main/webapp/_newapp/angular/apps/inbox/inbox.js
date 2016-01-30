app.factory('inboxStorage', ['ngStore', function (ngStore) {
    return ngStore.model('inbox');
}]);

app.controller('Ctrl', ['$scope', function($scope) {
  $scope.folds = [
    {name: 'Inbox', filter:''},
    {name: 'Starred', filter:'starred'},
    {name: 'Sent', filter:'sent'},
    {name: 'Important', filter:'important'},
    {name: 'Draft', filter:'draft'},
    {name: 'Trash', filter:'trash'}
  ];

  $scope.labels = [
    {name: 'Angular', filter:'angular', color:'#23b7e5'},
    {name: 'Bootstrap', filter:'bootstrap', color:'#7266ba'},
    {name: 'Client', filter:'client', color:'#fad733'},
    {name: 'Work', filter:'work', color:'#27c24c'}
  ];

  $scope.addLabel = function(){
    $scope.labels.push(
      {
        name: $scope.newLabel.name,
        filter: angular.lowercase($scope.newLabel.name),
        color: '#ccc'
      }
    );
    $scope.newLabel.name = '';
  }

  $scope.labelClass = function(label) {
    return {
      'b-l-info': angular.lowercase(label) === 'angular',
      'b-l-primary': angular.lowercase(label) === 'bootstrap',
      'b-l-warning': angular.lowercase(label) === 'client',
      'b-l-success': angular.lowercase(label) === 'work'      
    };
  };

}]);

app.controller('ListCtrl', ['$scope', 'inboxStorage', '$stateParams', '$http', function($scope, inboxStorage, $stateParams, $http) {
  $scope.fold = $stateParams.fold;
  $scope.inboxes = inboxStorage.findAll();

  $scope.populateData = function(){
    $http.get('apps/inbox/inbox.json').then(function (resp) {
      $scope.inboxes = resp.data.inbox;
      $scope.inboxes.forEach(function (item) {
          inboxStorage.create(item);
      });
    });
  }

  // populate data
  if($scope.inboxes.length == 0){
    $scope.populateData();
  }

}]);

app.controller('DetailCtrl', ['$scope', 'inboxStorage', '$stateParams', '$state', function($scope, inboxStorage, $stateParams, $state) {
  $scope.item = inboxStorage.find($stateParams);
  $scope.labels = [
    {name: 'Angular', filter:'angular', color:'#23b7e5'},
    {name: 'Bootstrap', filter:'bootstrap', color:'#7266ba'},
    {name: 'Client', filter:'client', color:'#fad733'},
    {name: 'Work', filter:'work', color:'#27c24c'}
  ];
  $scope.removeItem = function(item){
    inboxStorage.destroy(item);
    $state.go('app.inbox.list');
  }
  $scope.updateItem = function(item, label){
    item.label = label;
    inboxStorage.update(item);
  }
}]);

app.controller('NewCtrl', ['$scope', function($scope) {
  $scope.inbox = {
    to: {},
    subject: '',
    content: ''
  }
  $scope.people = [
    { name: 'Adam',      email: 'adam@email.com',      age: 12, country: 'United States' },
    { name: 'Amalie',    email: 'amalie@email.com',    age: 12, country: 'Argentina' },
    { name: 'Estefanía', email: 'estefania@email.com', age: 21, country: 'Argentina' },
    { name: 'Adrian',    email: 'adrian@email.com',    age: 21, country: 'Ecuador' },
    { name: 'Wladimir',  email: 'wladimir@email.com',  age: 30, country: 'Ecuador' },
    { name: 'Samantha',  email: 'samantha@email.com',  age: 30, country: 'United States' },
    { name: 'Nicole',    email: 'nicole@email.com',    age: 43, country: 'Colombia' },
    { name: 'Natasha',   email: 'natasha@email.com',   age: 54, country: 'Ecuador' },
    { name: 'Michael',   email: 'michael@email.com',   age: 15, country: 'Colombia' },
    { name: 'Nicolás',   email: 'nicolas@email.com',    age: 43, country: 'Colombia' }
  ];
}]);

angular.module('app').directive('labelColor', function(){
  return function(scope, $el, attrs){
    $el.css({'color': attrs.color});
  }
});
