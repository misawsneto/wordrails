app.factory('todoStorage', ['ngStore', function (ngStore) {
    return ngStore.model('todo');
}]);

app.controller('TodoCtrl', ['$scope', '$location', '$filter', 'todoStorage', function($scope, $location, $filter, todoStorage) {
    var todos = $scope.todos = todoStorage.findAll();

    $scope.newTodo = '';
    $scope.remainingCount = $filter('filter')(todos, {completed: false}).length;

    $scope.location = $location;

    $scope.$watch('location.path()', function (path) {
        $scope.statusFilter = { '/app/todo/active': {completed: false}, '/app/todo/completed': {completed: true} }[path];
    });

    $scope.$watch('remainingCount == 0', function (val) {
        $scope.allChecked = val;
    });

    $scope.addTodo = function () {
        var newTodo = $scope.newTodo.trim();
        if (newTodo.length === 0) {
            return;
        }

        var item = {
            id: todoStorage.nextId(),
            title: newTodo,
            completed: false
        };
        todos.push( todoStorage.create(item) );

        $scope.newTodo = '';
        $scope.remainingCount++;
    };

    $scope.editTodo = function (todo) {
        todo.editedTodo = true;
        // Clone the original todo to restore it on demand.
        $scope.originalTodo = angular.extend({}, todo);
    };

    $scope.doneEditing = function (todo) {
        todo.editedTodo = false;
        todo.title = todo.title.trim();

        if (!todo.title) {
            $scope.removeTodo(todo);
        }

        todoStorage.update(todo);
    };

    $scope.revertEditing = function (todo) {
        todos[todos.indexOf(todo)] = $scope.originalTodo;
        $scope.doneEditing($scope.originalTodo);
    };

    $scope.removeTodo = function (todo) {
        $scope.remainingCount -= todo.completed ? 0 : 1;
        todos.splice(todos.indexOf(todo), 1);
        todoStorage.destroy(todo);
    };

    $scope.todoCompleted = function (todo) {
        $scope.remainingCount += todo.completed ? -1 : 1;
        todoStorage.update(todo);
    };

    $scope.clearCompletedTodos = function () {
        todos.filter(function (todo) {
            if(todo.completed){
                todos.splice(todos.indexOf(todo), 1);
                todoStorage.destroy(todo);
            }
        });
    };

    $scope.markAll = function (completed) {
        todos.forEach(function (todo) {
            todo.completed = completed;
            todoStorage.update(todo);
        });
        $scope.remainingCount = !completed ? todos.length : 0;
    };
}]);

