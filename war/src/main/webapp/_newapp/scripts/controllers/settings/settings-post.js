app.controller('SettingsPostCtrl', ['$scope', '$log', '$timeout', '$mdDialog', '$state', 'FileUploader', 'TRIX', 'cfpLoadingBar', 'trixService', 'trix', '$http', '$mdToast', '$templateCache', '$location', '$interval', '$mdSidenav', '$translate',
	function($scope ,  $log ,  $timeout ,  $mdDialog ,  $state ,  FileUploader ,  TRIX ,  cfpLoadingBar ,  trixService ,  trix ,  $http ,  $mdToast, $templateCache  , $location, $interval, $mdSidenav, $translate){

	$scope.content = '';

	$scope.mediaOptionsOpen = false;

	var lang = $translate.use();

	$scope.tags = [];

	$scope.froalaOptions = {
		toolbarInline: false,
      placeholderText: 'Enter Text Here',
      heightMin: 200,
      fontSizeDefaultSelection: '18',
      codeMirror: true,
      language: (lang == 'en' ? 'en_gb' : lang == 'pt' ? 'pt_br' : null),
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

	// sidenav toggle
	$scope.toggleOptions = buildToggler('more-options');

	function buildToggler(navID) {
      return function() {
        $mdSidenav(navID)
          .toggle()
      }
    }
    // -----------
}]);