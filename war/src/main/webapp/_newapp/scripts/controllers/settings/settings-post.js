app.controller('SettingsPostCtrl', ['$scope', '$log', '$timeout', '$mdDialog', '$state', 'FileUploader', 'TRIX', 'cfpLoadingBar', 'trixService', 'trix', '$http', '$mdToast', '$templateCache', '$location', '$interval',
	function($scope ,  $log ,  $timeout ,  $mdDialog ,  $state ,  FileUploader ,  TRIX ,  cfpLoadingBar ,  trixService ,  trix ,  $http ,  $mdToast, $templateCache  , $location, $interval){

	$scope.content = '';

	$scope.mediaOptionsOpen = false;

	$scope.froalaOptions = {
 		toolbarInline: false,
      placeholderText: 'Enter Text Here',
      heightMin: 200,
      fontSizeDefaultSelection: '18',
      codeMirror: true,
      codeMirrorOptions: {
		  indentWithTabs: true,
		  lineNumbers: true,
		  lineWrapping: true,
		  mode: 'text/html',
		  tabMode: 'indent',
		  tabSize: 2
		}
    }

	$timeout(function(){
		$(".fr-box a").each(function(){
			if($(this).html() === 'Unlicensed Froala Editor')
				$(this).remove();
		});
	});
}]);