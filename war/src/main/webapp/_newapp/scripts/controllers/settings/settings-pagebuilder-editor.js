app.controller('PageBuilderEditorCtrl', ['$scope', '$rootScope', '$log', '$timeout', '$mdDialog', '$state', 'FileUploader', 'TRIX', 'cfpLoadingBar', 'trixService', 'trix', '$http', '$mdToast', '$templateCache', '$location', '$interval', '$mdSidenav', '$translate', '$filter', '$localStorage',
	function($scope , $rootScope,  $log ,  $timeout ,  $mdDialog ,  $state ,  FileUploader ,  TRIX ,  cfpLoadingBar ,  trixService ,  trix ,  $http ,  $mdToast, $templateCache  , $location, $interval, $mdSidenav, $translate, $filter, $localStorage){

	//--- variables initialization
	// $scope.default
	//--- /variables initialization

	$scope.app.editingPage = {};

	$scope.getChildrenSectionsLength = function(section){
		if(section)
			return  Object.keys(section.children).length;
		else
			return [];
	}

	$scope.getChildrenSections = function(section){
		if(section)
			return  Object.keys(section.children).map(function (key) {
			    return section.children[key];
			})
		else
			return [];
	}

	$scope.getSections = function(sections){
		if(sections)
			return  Object.keys(sections).map(function (key) {
			    return sections[key];
			})
		else
				return [];
	}

	// --- mock % test
	var mockPageLoad = function(){
		$scope.app.editingPage = createPageStub();
		console.log($scope.app.editingPage.sections);
		console.log($scope.getSections($scope.app.editingPage.sections));
		console.log($scope.getChildrenSections($scope.getSections($scope.app.editingPage.sections)[0]));
	}

	var test = function(){
		mockPageLoad();
	}

	$timeout(function(){
		test();
	}, 1000);
	// --- /mock % test

	pageBuilderEditorCtrl = $scope;
}]);

var pageBuilderEditorCtrl = null;

function createPageStub(){
	return {
    "title" : "page 1",
    "id" : 6,
    "sections" : {
    	"0" : {
    		"sectionType" : ".ContainerSection",
    		"pctSize": 100,
    		"children":{
	    		"0" : {
		        "sectionType" : ".QueryableListSection",
		        "pctSize": 100,
		        "id" : 7,
		        "title" : "Sec 1",
		        "properties" : {"type": "CAROUSEL"},
		        "isPageable" : false,
		        "size" : 3,
		        "mSize" : null,
		        "type" : "queryable_list",
		        "pageable" : false,
		        "msize" : null,
		        "blocks" : [
		        	{"title":"test 3","featuredImageHash":"717e1f628513c7b5eeb65a2a78599b25","imageSmallHash":null,"imageMediumHash":"7a592baccfaf0f03456cb817882353e7","imageLargeHash":"84873fa2322033e72fb7a7bee7de878d","categories":[{"id":1304,"name":"AMBIENTE"}],"tags":[],"terms":[{"termId":1304,"termName":"AMBIENTE","parentId":null,"taxonomyId":140,"imageHash":null,"children":null}],"sponsored":null,"date":1458159801000,"snippet":"Isso é um texto de testes...","body":null,"topper":null,"state":"PUBLISHED","readsCount":0,"bookmarksCount":0,"recommendsCount":0,"commentsCount":0,"readTime":0,"imageLandscape":true,"authorName":"Demo","authorUsername":"demo","authorId":51,"authorEmail":"demo@demo.com","authorTwitter":null,"authorCover":"2eeb3bc6887ab6a24168e9492b5dbdbf","authorProfilePicture":"bc3c2042f9c3474ccbebd9b8b40533c4","authorCoverMediumHash":"2eeb3bc6887ab6a24168e9492b5dbdbf","authorImageSmallHash":"3a10d25d632ec336bc31bc1e6f17f8e1","authorCoverUrl":null,"authorImageUrl":null,"stationName":"O MUNDO","stationId":11,"stationIdString":null,"slug":"test-3-emj4fnb8","externalFeaturedImgUrl":null,"externalVideoUrl":null,"imageCaptionText":null,"imageCreditsText":null,"imageTitleText":"12828489_962979120459669_7623045188689411920_o.jpg","featuredVideoHash":null,"featuredAudioHash":null,"lat":null,"lng":null,"subheading":null,"scheduledDate":null,"notify":true,"featuredImage":{"id":null,"title":null,"caption":null,"credits":null,"originalHash":"a1eb4d302ad9413be802d5d0bf119028"},"id":6910,"postId":6910},
		        	{"title":"test 2","featuredImageHash":null,"imageSmallHash":null,"imageMediumHash":null,"imageLargeHash":null,"categories":[{"id":1304,"name":"AMBIENTE"}],"tags":[],"terms":[{"termId":1304,"termName":"AMBIENTE","parentId":null,"taxonomyId":140,"imageHash":null,"children":null}],"sponsored":null,"date":1458159664000,"snippet":"...","body":null,"topper":null,"state":"PUBLISHED","readsCount":0,"bookmarksCount":0,"recommendsCount":0,"commentsCount":0,"readTime":0,"imageLandscape":true,"authorName":"Demo","authorUsername":"demo","authorId":51,"authorEmail":"demo@demo.com","authorTwitter":null,"authorCover":"2eeb3bc6887ab6a24168e9492b5dbdbf","authorProfilePicture":"bc3c2042f9c3474ccbebd9b8b40533c4","authorCoverMediumHash":"2eeb3bc6887ab6a24168e9492b5dbdbf","authorImageSmallHash":"3a10d25d632ec336bc31bc1e6f17f8e1","authorCoverUrl":null,"authorImageUrl":null,"stationName":"O MUNDO","stationId":11,"stationIdString":null,"slug":"test-2-eg83gspa","externalFeaturedImgUrl":null,"externalVideoUrl":null,"imageCaptionText":null,"imageCreditsText":null,"imageTitleText":null,"featuredVideoHash":null,"featuredAudioHash":null,"lat":null,"lng":null,"subheading":null,"scheduledDate":null,"notify":true,"featuredImage":null,"id":6909,"postId":6909},
		        	{"title":"Antigo guarda nazi julgado pela morte de 170 mil pessoas","featuredImageHash":"f277079ae75fbf5a1739fb238790f6bc","imageSmallHash":"6f1d3b33326a718f354a54ee8d5177de","imageMediumHash":"6f1d3b33326a718f354a54ee8d5177de","imageLargeHash":"f277079ae75fbf5a1739fb238790f6bc","categories":[{"id":1297,"name":"PODER"},{"id":1303,"name":"LÁ, CÁ e qualquer lugar"},{"id":1311,"name":"CONFLITOS"}],"tags":[],"terms":[{"termId":1311,"termName":"CONFLITOS","parentId":null,"taxonomyId":140,"imageHash":null,"children":null},{"termId":1303,"termName":"LÁ, CÁ e qualquer lugar","parentId":null,"taxonomyId":140,"imageHash":null,"children":null},{"termId":1297,"termName":"PODER","parentId":null,"taxonomyId":140,"imageHash":null,"children":null}],"sponsored":null,"date":1455213771000,
		        	"snippet":"Um antigo guarda de Auschwitz de 94 anos, Reinhold"+
							 "Hanning, vai ser julgado esta quinta-feira, na cidade de Detmold, na Alemanha, acusado de ser cúmplice"+
							 "no assassínio de pelo menos 170 mil pessoas. Este é o primeiro dos quatro julgamentos previstos para"+
							 "os próximos meses, estando todos os réus na casa dos 90 anos. Reinhold Hanning tinha 20 anos em 1942"+
							", altura em que começou a servir como guarda no campo de concentração de Auschwitz, na Polónia ocupada"+
							 "pelos nazis. A acusação afirma que Hanning recebia os prisioneiros judeus quando estes chegavam ao campo"+
							 "e os escoltava de seguida até às",
							 "body":null,"topper":"HOLOCAUSTO","state":"PUBLISHED","readsCount":10,"bookmarksCount":0,"recommendsCount":0,"commentsCount":0,"readTime":7,"imageLandscape":false,"authorName":"Demo","authorUsername":"demo","authorId":51,"authorEmail":"demo@demo.com","authorTwitter":null,"authorCover":"2eeb3bc6887ab6a24168e9492b5dbdbf","authorProfilePicture":"bc3c2042f9c3474ccbebd9b8b40533c4","authorCoverMediumHash":"2eeb3bc6887ab6a24168e9492b5dbdbf","authorImageSmallHash":"3a10d25d632ec336bc31bc1e6f17f8e1","authorCoverUrl":null,"authorImageUrl":null,"stationName":"O MUNDO","stationId":11,"stationIdString":null,"slug":"antigo-guarda-nazi-julgado-pela-morte-de-170-mil-pessoas-h4fsod1l","externalFeaturedImgUrl":null,"externalVideoUrl":null,"imageCaptionText":null,"imageCreditsText":null,"imageTitleText":null,"featuredVideoHash":null,"featuredAudioHash":null,"lat":null,"lng":null,"subheading":"Reinhold Hanning vai enfrentar testemunhas que sobreviveram ao horror dos campos de concentração.","scheduledDate":null,"notify":false,"featuredImage":{"id":null,"title":null,"caption":null,"credits":null,"originalHash":"f277079ae75fbf5a1739fb238790f6bc"},"id":6293,"postId":6293}
		        ]
		      }
		    }
		  },
    	"1" : {
    		"sectionType" : ".ContainerSection",
    		"pctSize": 100,
    		"children":{
	    		"0" : {
		        "sectionType" : ".QueryableListSection",
		        "pctSize": 50,
		        "id" : 7,
		        "title" : "Sec 1",
		        "properties" : {"type": "CARD"},
		        "isPageable" : false,
		        "size" : 10,
		        "mSize" : null,
		        "type" : "queryable_list",
		        "pageable" : false,
		        "msize" : null,
		        "blocks" : [ ]
		      },
		      "1" : {
		        "sectionType" : ".QueryableListSection",
		        "pctSize": 25,
		        "id" : 7,
		        "title" : "Sec 1",
		        "properties" : {"type": "LIST"},
		        "isPageable" : false,
		        "size" : 5,
		        "mSize" : null,
		        "type" : "queryable_list",
		        "pageable" : false,
		        "msize" : null,
		        "blocks" : [ ]
		      },
		      "2" : {
		        "sectionType" : ".QueryableListSection",
		        "pctSize": 25,
		        "id" : 7,
		        "title" : "Sec 1",
		        "properties" : {"type": "LIST"},
		        "isPageable" : false,
		        "size" : 5,
		        "mSize" : null,
		        "type" : "queryable_list",
		        "pageable" : false,
		        "msize" : null,
		        "blocks" : [ ]
		      }
		    }
    	}
    }
  }
}