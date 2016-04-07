app.controller('SettingsPostCtrl', ['$scope', '$log', '$timeout', '$mdDialog', '$state', 'FileUploader', 'TRIX', 'cfpLoadingBar', 'trixService', 'trix', '$http', '$mdToast', '$templateCache', '$location', '$interval', '$mdSidenav', '$translate', '$filter', '$localStorage', 'ngJcropConfig',
	function($scope ,  $log ,  $timeout ,  $mdDialog ,  $state ,  FileUploader ,  TRIX ,  cfpLoadingBar ,  trixService ,  trix ,  $http ,  $mdToast, $templateCache  , $location, $interval, $mdSidenav, $translate, $filter, $localStorage, ngJcropConfig){

	// ---------- scope initialization
	var lang = $translate.use();

	$scope.tags = [];

	$scope.landscape = true;

	$timeout(function(){
		$scope.openImageOptions = false;
	});

	ngJcropConfig.default.jcrop ={
	  aspectRatio: null,
	  bgColor: 'black',
	  bgOpacity: .4,
	  boxWidth: 400, 
	  boxHeight: 400
	};

	trix.getStations().success(function(stations){
		$scope.stations = stations;
		stations && (stations.length > 0) && $scope.stations.forEach(function(station){
			getTermTree(station)	
		})
	}).error(function(error){

	})

	function getTermTree(station){
		trix.getTermTree(null, station.categoriesTaxonomyId).success(function(categories){
			console.log(categories);
		});
	}

	// Must be [x, y, x2, y2, w, h]
   $scope.app.cropSelection = [100, 100, 200, 200];

	$scope.froalaOptions = {
		toolbarInline: false,
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
		  tabSize: 2,
		  fontSizeDefaultSelection: 18,
		// Set the image upload parameter.
        imageUploadParam: 'image_param',

        // Set the image upload URL.
        imageUploadURL: '/upload_image',

        // Additional upload params.
        imageUploadParams: {id: 'my_editor'},

        // Set request type.
        imageUploadMethod: 'POST',

        // Set max image size to 5MB.
        imageMaxSize: 8 * 1024 * 1024,

        // Allow to upload PNG and JPG.
        imageAllowedTypes: ['jpeg', 'jpg', 'png']
		}
    }

	$timeout(function(){
		$(".fr-box a").each(function(){
			if($(this).html() === 'Unlicensed Froala Editor')
				$(this).remove();
		});
	});

	// ---------- /scope initialization

	// sidenav toggle
	$scope.toggleOptions = buildToggler('more-options');

	function buildToggler(navID) {
      return function() {
        $mdSidenav(navID)
          .toggle()
      }
    }
    // -----------

    if($state.params.slug)
    	$scope.postState = 'Draft';

	// ---------- time config ---------- 
	function addMinutes(date, minutes) {
	    date = new Date(date.getTime() + minutes*60000);
	    return date;
	}

	function addDays(date, days) {
	    date.setDate(date.getDate() + days);
	    return date;
	}


	// --- post date
	$scope.postDateUpdateble = true;
	$scope.resetPostDate = function(){
		if(!$scope.postDateUpdateble)
			return;
		$scope.postDate = new Date();
		$scope.postDate.setSeconds(null);
		$scope.postDate.setMilliseconds(null);
	}

	$scope.changePostDate = function(date){
		$scope.postDateUpdateble = false;
	}
	// --- /post date
	
	// --- post schedule
	$scope.postScheduleUpdateble = true;
	$scope.resetScheduleDate = function(){
		if(!$scope.postScheduleUpdateble)
			return;
		$scope.postScheduleDate = new Date();
		$scope.postScheduleDate = addMinutes($scope.postScheduleDate, 15);
		$scope.postScheduleDate.setSeconds(null);
		$scope.postScheduleDate.setMilliseconds(null);
		$scope.postScheduleUpdateble = true;
	}
	// --- /post schedule

	// --- post delete
	$scope.postDeleteUpdateble = true;
	$scope.resetDeleteDate = function(){
		if(!$scope.postDeleteUpdateble)
			return;
		$scope.postDeleteDate = new Date();
		$scope.postDeleteDate = addDays($scope.postDeleteDate, 1);
		$scope.postDeleteDate.setSeconds(null);
		$scope.postDeleteDate.setMilliseconds(null);
		$scope.postDeleteUpdateble = true;
	}
	// --- /post delete

	// ---------- /time config ---------- 
	
	
	// --- slug
	$scope.customizedLink = {
		slug: ""
	}

	// --- slug

	// --- watch post change
	
	$scope.postObjectChanged = false;
	/**
	 * Watch post and set the page not to change if post was edited.
	 * The postObjectChanged flag is used here.
	 * @see $scope.$watch('postObjectChanged'
	 * @param  {[type]} true    [description]
	 */
	$scope.$watch('app.editingPost', function(newVal, oldVal) {
		if(oldVal && ('title' in oldVal) && ('body' in oldVal)){
			// post has been edited

			if(newVal.title !== oldVal.title || 
				newVal.body.stripHtml().replace(/(\r\n|\n|\r)/gm,"") !== oldVal.body.stripHtml().replace(/(\r\n|\n|\r)/gm,"")){

				// TODO: save draft

				// set post changed so $scope.watch can see postObjectChanged
				if(!$scope.postObjectChanged){
					$log.info('post changed, avoid page change...')
					$scope.postObjectChanged = true;
				}
			}
		}
	}, true);

	/**
	 * Watch value of postObjectChanged and set alert messages.
	 * @param  boolean newVal
	 */
	$scope.$watch('postObjectChanged', function(newVal, oldVal){
		if(newVal){
			window.onbeforeunload = function(){ return $filter('translate')('settings.post.messages.PAGE_CHANGE_ALERT') };
		}else{
			window.onbeforeunload = null;
		}
	});

	// --- /watch post change
	

	// --- clear post
	$scope.showClearPostDialog = function(event){
		// show term alert
		
		$mdDialog.show({
			controller: $scope.app.defaultDialog,
			templateUrl: 'clear-post-dialog.html',
			parent: angular.element(document.body),
			targetEvent: event,
			clickOutsideToClose:true
			// onComplete: function(){

			// }
		})
	}

	$scope.app.clearPostContent = function(){
		$scope.app.editingPost.title = '';
		$scope.app.editingPost.body = '';
		$mdDialog.cancel();
	}

	// /clear post
	
	// --- post info
	$scope.showPostInfoDialog = function(event){

		$mdDialog.show({
			controller: $scope.app.defaultDialog,
			template: 	postInfoTemplate,
			parent: angular.element(document.body),
			// targetEvent: event,
			clickOutsideToClose:false
			// onComplete: function(){

			// }
		})
	}

	$scope.app.checkState = function(state){
		if(!$scope.app.editingPost)
			return null;

		state = state ? state : $scope.app.editingPost.state;
		if(!state)
			return null;
		if(state == "PUBLISHED"){
			return 1;
		}else if(state == "DRAFT"){
			return 2;
		}else if(state == "SCHEDULED"){
			return 3;
		}else if(state == "TRASH"){
			return 4;
		}else{
			return 5;
		}
	}

	$scope.app.getStateText = function(state){
		if(!$scope.app.editingPost)
			return null;

		state = state ? state : $scope.app.editingPost.state;
		if(!state)
			return " - ";
		if(state == "PUBLISHED"){
			return $filter('translate')('settings.post.states.PUBLISHED');
		}else if(state == "DRAFT"){
			return $filter('translate')('settings.post.states.DRAFT');
		}else if(state == "SCHEDULED"){
			return $filter('translate')('settings.post.states.SCHEDULED');
		}else if(state == "TRASH"){
			return $filter('translate')('settings.post.states.TRASH');
		}else{
			return " - ";
		}
	}

	$scope.app.countBodyCharacters = function(){
		return $scope.app.editingPost.body.stripHtml().length
	}

	$scope.app.countTitleCharacters = function(){
		return $scope.app.editingPost.title.stripHtml().length
	}

	$scope.app.countTitleWords = function(){
		return countWords($scope.app.editingPost.title.stripHtml())
	}

	$scope.app.countBodyWords = function(){
		return countWords($scope.app.editingPost.body.stripHtml())
	}

	function countWords(str) {
		var regex = /\s+/gi;
		return str.trim().replace(regex, ' ').split(' ').length;
	}

	// --- /post info
	
	// ------------------- image uploader -------------

	var uploader = $scope.uploader = new FileUploader({
		url: TRIX.baseUrl + "/api/images/upload?imageType=POST"
	});

	$scope.uploadedImage = null;
	uploader.onAfterAddingFile = function(fileItem) {
		$scope.uploadedImage = null;
		uploader.uploadAll();
	};

	uploader.onSuccessItem = function(fileItem, response, status, headers) {
		if(response.filelink){
			$scope.featuredImage = $scope.uploadedImage = response;
			setPostFeaturedImage($scope.uploadedImage.hash)
			$mdToast.hide();
		}
	};

	uploader.onErrorItem = function(fileItem, response, status, headers) {
		if(status == 413)
			$scope.app.showErrorToast("A imagem não pode ser maior que 6MBs.");
		else
			$scope.app.showErrorToast("Não foi possível procesar a imagem. Por favor, tente mais tarde.");
	}

	$scope.clearImage = function(){ 
		$scope.uploadedImage = null;
		uploader.clearQueue();
		uploader.cancelAll()
		$scope.checkLandscape();
		$scope.postCtrl.imageHasChanged = true;
	}

	uploader.onProgressItem = function(fileItem, progress) {
		cfpLoadingBar.start();
		cfpLoadingBar.set(progress/10)
		if(progress == 100){
			cfpLoadingBar.complete()
			toastPromise = $mdToast.show(
				$mdToast.simple()
				.content('Processando...')
				.position('top right')
				.hideDelay(false)
				);
		}
	};

	$scope.postFeaturedImage = null
	var setPostFeaturedImage = function(hash){
		$scope.postFeaturedImage = $filter('imageLink')(hash, 'large')
	}


	// ------------------- end of image uploader -------------
	
	// --- image settings
	
	$scope.toggleLandscape = function(){
		$scope.landscape = !$scope.landscape
		$localStorage.landscape = $scope.landscape;
	}

	$scope.removeFeaturedImage = function(){
		$scope.featureImage = null;
		$scope.postFeaturedImage = null;
	}

	$scope.loadImageConfigFromStorage = function(){
		if ( angular.isDefined($localStorage.landscape) )
	    $scope.landscape = $localStorage.landscape;
	}

	$scope.showImageUploadDialog = function(){
		$("#image-buttom").trigger('click');
	}

	// load imageConfig
	$scope.loadImageConfigFromStorage();

	// --- /image settings

	// --- image crop
	$scope.showImageCropDialog = function(event){
		// show term alert
		
		$scope.app.postFeaturedImage = $scope.postFeaturedImage;

		$mdDialog.show({
			controller: $scope.app.defaultDialog,
			template: 	postCropTemplate,
			parent: angular.element(document.body)
		})
	}

	// --- /image crop
	// --- image focuspoint

	$scope.app.imgToolsProps = {
		width: 0,
		x: 50,
		y: 50,
		onMouseDown: function (e) {
      $scope.update(e);
      $scope.app.imgToolsProps.dragging = true;
    },
    onMouseMove: function (e) {
      if ($scope.app.imgToolsProps.dragging === true) {
        $scope.update(e);
      }
    },
		onMouseUp: function (e) {
      e.preventDefault();
      $scope.app.imgToolsProps.dragging = false;
    }
	}

	$scope.update = function (e) {
    e.preventDefault();
    var offset = $scope.offset(e.target);
    $scope.app.imgToolsProps.x = Math.round(((e.pageX - offset.left) / e.target.clientWidth) * 100);
    $scope.app.imgToolsProps.y = Math.round(((e.pageY - offset.top) / e.target.clientHeight) * 100);
  };
  
  $scope.offset = function (elm) {
    try { return elm.offset(); } catch (e) { }
    var body = document.documentElement || document.body;
    return {
        left: elm.getBoundingClientRect().left + (window.pageXOffset || body.scrollLeft),
        top: elm.getBoundingClientRect().top + (window.pageYOffset || body.scrollTop)
    };
  };

	var setImgToolsImageWidth = function(){
		var featuredImage = $('#img-stub');
		if(featuredImage.length)
		var w = $(featuredImage).width();
		var h = $(featuredImage).height();

		var isLandscape = true
		if(w > h)
			isLandscape = true;
		else
			isLandscape = false;

		if(isLandscape){
			$scope.app.imgToolsProps.height = (h * 400) / w
			$scope.app.imgToolsProps.width = 400;
		}else{
			$scope.app.imgToolsProps.width = (w * 400) / h
			$scope.app.imgToolsProps.height = 400;
		}
	}

	$interval(function(){
		setImgToolsImageWidth();
	}, 500);

	
	$scope.showImageFocuspointDialog = function(event){
		// show term alert
		
		$scope.app.postFeaturedImage = $scope.postFeaturedImage;

		$mdDialog.show({
			controller: $scope.app.defaultDialog,
			template: 	postFocuspointTemplate,
			parent: angular.element(document.body),
			targetEvent: event,
			clickOutsideToClose:false
			// onComplete: function(){

			// }
		})
	}
	// --- /image focuspoint
	
	var draftAutoSaveCheck = function(){
	}

	$interval(function(){
	}, 500);

	// --- auto save
	

	// --- /auto save

	// --- geolocation
	$scope.showGeoNotificationDialog = function(event){
		// show term alert
		
		$scope.app.postFeaturedImage = $scope.postFeaturedImage;

		$mdDialog.show({
			controller: $scope.app.defaultDialog,
			templateUrl: 	'geolocation-dialog.html',
			parent: angular.element(document.body),
			targetEvent: event,
			clickOutsideToClose:true
			// onComplete: function(){

			// }
		})
	}
	// --- /geolocation

	// --- save post as published
	$scope.savePostAsPublished = function(){

	}
	// --- /save post as published

	// --- save post as draft
	$scope.savePostAsDraft = function(){
		
	}
	// --- save post as draft

	// --- save post as scheduled
	$scope.savePostAsScheduled = function(){
		
	}
	// --- /save post as scheduled

	// --- save post as trash
	$scope.savePostAsTrash = function(){
		
	}
	// --- /save post as trash

	// --- save post as trash
	$scope.saveVersion = function(){
		
	}
	// --- /save post as trash

	// --- resize zoom class workaround
	
	$scope.zoomActive = true;
	$timeout(function(){
		$scope.zoomActive = false;
	}, 4000);

	// --- /resize zoom class workaround

	// ------------------- update term tree ---------------

	$scope.selectedStation = null;
	$scope.$watch('selectedStation', function(newVal){
		updateTermTree()
	})

	function updateTermTree(){
		if($scope.selectedStation)
			trix.getTermTree(null, $scope.selectedStation.categoriesTaxonomyId).success(function(response){
				$scope.termTree = response;
				selectTerms($scope.termTree, $scope.app.editingPost.terms)
			});
	}

	function selectTerms(terms, termList){
			if(!termList || !terms)
				return;
			var termIds = []
			termList.forEach(function(termItem, index){
				termIds.push(termItem.id)
			});

			terms && terms.forEach(function(term, index){
				if(termIds.indexOf(term.id) > -1)
					term.checked = true;
				selectTerms(term.children, termList)
			});
		}

		function getTermList(terms, retTerms){
			if(!retTerms)
				retTerms = []

			terms && terms.forEach(function(term, index){
				if(term.checked)
					retTerms.push(term)
				var ts = getTermList(term.children)
				ts.forEach(function(t){
					retTerms.push(t)
				});
			});
			return retTerms;
		}

	// ------------------- end of update term tree ---------------


	// --- mock and test
	var mockPostLoad = function(){
		$scope.app.editingPost = createPostStub();
		$scope.revisions = createVersions();

		var hash = $scope.app.editingPost.featuredImageHash ? $scope.app.editingPost.featuredImageHash : $scope.app.editingPost.featuredImage.originalHash
		setPostFeaturedImage(hash)
		$scope.featuredImage = $scope.app.editingPost.featuredImage
		$scope.landscape = $scope.app.editingPost.imageLandscape;
		$scope.customizedLink.slug = $scope.app.editingPost.slug;
	}

	var test = function(){
		mockPostLoad();
	}

	$timeout(function(){
		// test();
	}, 1000);

	settingsPostCtrl = $scope;
	// --- /mock & test
}]);

var settingsPostCtrl = null;

// --- TEST
/**
 * Post Stub
 * @return {Post} [mock]
 */
function createPostStub(){
	return {"title":"Foi o amor pela Rainha do Sul que tramou El Chapo Guzmán","featuredImageHash":"a1eb4d302ad9413be802d5d0bf119028","imageSmallHash":"44c8067b27a4a6a8d12ffdef6b34f5d8","imageMediumHash":"44c8067b27a4a6a8d12ffdef6b34f5d8","imageLargeHash":"a1eb4d302ad9413be802d5d0bf119028","categories":[{"id":1297,"name":"PODER"}],"tags":["sinaloa","el chapo","kate del castillo"],"terms":[{"termId":1297,"termName":"PODER","parentId":null,"taxonomyId":140,"imageHash":null,"children":null}],"sponsored":null,"date":1452799663000,"snippet":"Quem vê telenovelas mexicanas sabe que o amor tanto pode ser a salvação como uma perdição. E no caso do mais famoso barão do narcotráfico JoaquinEl Chapo Guzmán, um dos homens mais procurados da América do Norte, a paixão pela actriz Kate del Castillo acabou por revelar-se fatídica: foi a sua obsessão por ela, que o levou a organizar um encontro clandestino em plena selva para que pudessem conhecer-se melhor, que conduziu a polícia até ao seu esconderijo. A vistosa actriz de 43 anos, uma das mais famosas estrelas de cinema e televisão do México , chamou a atenção do","body":"<p>Quem vê telenovelas mexicanas sabe que o amor tanto pode ser a salvação como uma perdição. E no caso do mais famoso barão do narcotráfico Joaquin<em>El Chapo</em> Guzmán, um dos homens mais procurados da América do Norte, a paixão pela actriz Kate del Castillo acabou por revelar-se fatídica: foi a sua obsessão por ela, que o levou a organizar um encontro clandestino em plena selva para que pudessem conhecer-se melhor, que conduziu a polícia até ao seu esconderijo.\n</p><p>A vistosa actriz de 43 anos, uma das mais famosas estrelas de cinema e televisão do México , chamou a atenção do chefe do cartel de Sinaloa por causa de uma polémica mensagem que publicou no Twitter, no início de 2012, quando a guerra do Exército mexicano ao narcotráfico estava ao rubro. Era uma declaração política, mas que <em>El Chapo</em> terá interpretado como uma manifestação de afecto: dizia Kate que confiava mais nele do que no Governo, e como “seria maravilhoso se começasse a traficar com amor”.\n</p><p>Perante o impacto político das suas palavras, Castillo publicou uma série de justificações, incluindo que não estava a falar a sério, ou que a sua ironia e sarcasmo estavam a ser mal entendidas. Mas tomadas no seu todo, as suas declarações no Twitter constituem uma espécie de declaração – que se os perfis psicológicos de Joaquin Guzmán forem minimamente fidedignos, terá sido música para os ouvidos deste homem “sedutor, esplêndido e protector”, que apesar de ignorante e pouco interessado é “astuto e inteligente”, além de “compulsivo e tenaz”, que nunca desiste até conseguir o que quer.\n</p><p>As biografias de Kate também salientam a sua inteligência, a par da sua beleza e capacidade de trabalho. Filha de Éric del Castillo, uma das lendas da chamada época de ouro do cinema mexicano e estrela de telenovelas, Kate entrou para o negócio de família logo aos oito anos de idade. Nunca mais parou desde então, representando no teatro, cinema e também séries televisivas de enorme popularidade e audiência, tanto no México como nos Estados Unidos. Por várias vezes, interpretou o papel de mulheres ambiciosas, poderosas e temidas: na novela <em>A Rainha do Sul</em>, baseada no livro de Arturo Pérez-Reverte; na série <em>Erva</em> e na produção internacional <em>Donos do Paraíso</em> – todas estas protagonistas eram narcotraficantes.\n</p><p><em>El Chapo</em> estava detido na prisão de alta segurança de Altiplano quando iniciou contacto com a actriz. À sua mensagem inicial no Twitter respondeu com flores; depois iniciaram uma correspondência, ora manuscrita, ora por mensagens em código utilizando o telefone móvel do seu advogado, Andrés Granado, que se tornou mediador do diálogo entre os dois. Em Agosto de 2014 surgiu a hipótese de trabalharem juntos na produção de um <em>biopic</em>, alegadamente porque Guzmán lhe confessou o seu desejo de ver a sua história passada ao cinema.\n</p><p>A comunicação intensificou-se após a espectacular fuga da cadeia de El Chapo, em Julho de 2015, sempre usando o mesmo método, até que decidiu prescindir do intermediário: segundo revelam transcrições das mensagens, divulgadas pelo jornal <em>Milénio</em>, o narcotraficante fez chegar a Kate del Castillo o último modelo de telefone lançado pela Blackberry, para poderem falar directamente.\n</p><p>A conversa entre os dois nas mensagens entretanto publicadas é idêntica à de muitos casais apaixonados, sem referência a filmes ou negócios. No último capítulo do seu namoro electrónico, <em>El Chapo</em> já não esconde os seus sentimentos: convida-a para um encontro nas montanhas onde está escondido, e empenha-se para que tudo esteja a gosto da actriz – a proposta é para que passem três dias juntos. “Se trouxeres vinho, também bebo”, promete, informando-a que habitualmente não bebe, mas quando o faz prefere tequila e whisky.\n</p><p>A resposta de Kate é calorosa. Pode ser que nessa altura, como escreve o jornal <em>El País</em>, já tivesse cruzado definitivamente a linha que separa realidade e ficção, transmutando-se na sua personagem da <em>Rainha do Sul</em>, Teresa Mendoza, uma jovem inocente de Sinaloa que depois do assassinato do noivo se torna na líder implacável de um cartel da droga. “Fico muito comovida quando me dizes que vais cuidar de mim, nunca ninguém o fez por isso obrigada”, diz. “Não fazes ideia de como estou entusiasmada. E ansiosa por olhar-te nos olhos, em pessoa”, acrescenta.\n</p>","topper":"DROGAS","state":"DRAFT","readsCount":12,"bookmarksCount":0,"recommendsCount":0,"commentsCount":0,"readTime":8,"imageLandscape":true,"authorName":"Demo","authorUsername":"demo","authorId":51,"authorEmail":"demo@demo.com","authorTwitter":null,"authorCover":"2eeb3bc6887ab6a24168e9492b5dbdbf","authorProfilePicture":"bc3c2042f9c3474ccbebd9b8b40533c4","authorCoverMediumHash":"2eeb3bc6887ab6a24168e9492b5dbdbf","authorImageSmallHash":"3a10d25d632ec336bc31bc1e6f17f8e1","authorCoverUrl":null,"authorImageUrl":null,"stationName":"O MUNDO","stationId":11,"stationIdString":null,"slug":"foi-o-amor-pela-rainha-do-sul-que-tramou-el-chapo-guzman-n8r2q8ub","externalFeaturedImgUrl":null,"externalVideoUrl":null,"imageCaptionText":"Vários DVD's da A Rainha do Sul foram encontrados no esconderijo de Guzmán DR","imageCreditsText":null,"imageTitleText":null,"featuredVideoHash":null,"featuredAudioHash":null,"lat":null,"lng":null,"subheading":"Mensagens trocadas entre o perigoso narcotraficante, chefe do cartel Sinaloa, e a actriz mexicana Kate del Castillo, revelam um homem apaixonado.","scheduledDate":null,"notify":false,"id":6017,"postId":6017}
}

function createVersions(){
	 var revisions = [{
			"date": 1458079387000,
			"tag": "",
			"payload": createPostStub()
		},
		{
			"date": 1458067270000,
			"tag": "",
			"payload": createPostStub()
		},
		{
			"date": 1458052152000,
			"tag": "",
			"payload": createPostStub()
		}]

		return revisions;
}
// --- /TEST

	var postInfoTemplate = '<md-dialog class="splash-dialog">'+
		'<div class="pos-fix top-0 right-0 p">'+
			'<md-button class="md-icon-button md-icon-button-lg" ng-click="app.cancelDialog();"><i class="mdi mdi2-close i-40"></i></md-button>'+
		'</div>'+
		'<div class="content">'+
			'<div class="row">'+
			  '<div class="m-b-sm"><code>/{{app.editingPost.slug}}</code></div>'+
			  '<div class="h3 font-bold m-b-md">{{app.editingPost.title}}</div>'+
			  '<div class="text-md m-b-sm">'+
			    '<strong>Status:</strong>'+
			    '<span class="text-u-c m-l-sm">'+
			    	'{{app.getStateText()}}'+
		    	'</span>'+
			  '</div>'+
			  '<div class="text-md m-b-sm">'+
			    '<strong>Data de publicação:</strong>'+
			    '<span class="text-u-c m-l-sm">{{app.editingPost.date | fromNow2}}</span>'+
			  '</div>'+
			  '<div class="text-md m-b-sm" ng-if="app.editingPost.createdAt">'+
			    '<strong>Criado em:</strong>'+
			    '<span class="text-u-c m-l-sm">{{app.editingPost.createdAt | fromNow2}}</span>'+
			  '</div>'+
			  '<div class="text-md m-b-sm" ng-if="app.editingPost.updatedAt">'+
			    '<strong>Atualizado em:</strong>'+
			    '<span class="text-u-c m-l-sm">{{app.editingPost.updatedAt | fromNow2}}</span>'+
			  '</div>'+
			  '<div class="text-md m-b-sm" ng-if="app.editingPost.body">'+
			    '<strong>Título:</strong>'+
			    '<span class="m-l-sm m-r-sm">{{app.countTitleWords()}} palavras</span> | <span class="m-l-sm">{{app.countTitleCharacters()}} caracteres</span>'+
			  '</div>'+
			  '<div class="text-md m-b-sm" ng-if="app.editingPost.body">'+
			    '<strong>Texto:</strong>'+
			    '<span class="m-l-sm m-r-sm">{{app.countBodyWords()}} palavras</span> | <span class="m-l-sm">{{app.countBodyCharacters()}} caracteres</span>'+
			  '</div>'+
			'</div>'+
		'</div>'+
	'</md-dialog>';

	var postCropTemplate = '<md-dialog class="splash-dialog">'+
		'<div class="pos-fix top-0 right-0 p w-full">'+
			'<md-button class="md-icon-button md-icon-button-lg pull-right" ng-click="app.cancelDialog();"><i class="mdi mdi2-close i-40"></i></md-button>'+
			'<div translate="settings.post.CROP_IMAGE_DESC" class="m-h-auto text-center w-full text-md p font-500"></div>'+
		'</div>'+
		'<div class="block-800 min-h-400 pos-rlt min-h-400" style="width:100%">'+
			'<div class="pos-abt w-full top-0 bottom-0 left-0">'+
			 '<div class="hbox">'+
			    '<div class="col text-md font-500">'+
			      '<div class="vbox">'+
			        '<div class="row-row">'+
			          '<div class="cell">'+
			            '<div class="cell-inner">'+
			            	'<div class="center-img-container m-h-auto o-f-h">'+
											'<div ng-jcrop="app.postFeaturedImage" selection="app.cropSelection"></div>'+
										'</div>'+
			            '</div>'+
			          '</div>'+
			        '</div>'+
			      '</div>'+
			    '</div>'+
		    '</div>'+
	    '</div>'+
    '</div>'+
		'<div class="text-center p-t">'+
			'<md-button ng-click="app.cancelDialog();" class="m-0">'+
				'{{\'titles.CANCEL\' | translate}}'+
			'</md-button>'+
			'<md-button ng-click="app.clearPostContent();" class="m-0">'+
				'{{\'titles.SAVE\' | translate}}'+
			'</md-button>'+
		'</div>'+
	'</md-dialog>';

	var postFocuspointTemplate = '<md-dialog class="splash-dialog">'+
		'<div class="pos-fix top-0 right-0 p w-full">'+
			'<md-button class="md-icon-button md-icon-button-lg pull-right" ng-click="app.cancelDialog();"><i class="mdi mdi2-close i-40"></i></md-button>'+
			'<div translate="settings.post.FOCUS_IMAGE_DESC" class="m-h-auto text-center w-full text-md p font-500"></div>'+
		'</div>'+
		'<div class="block-800 min-h-400 pos-rlt min-h-400" style="width:100%">'+
			'<div class="block-800">'+
			'<div class="pos-abt w-full top-0 bottom-0 left-0">'+
			  '<div class="hbox">'+
			    '<div class="col text-md font-500">'+
			      '<div class="vbox">'+
			        '<div class="row-row">'+
			          '<div class="cell">'+
			            '<div class="cell-inner">'+
			            	'<div class="focus-point center-img-container pull-right o-f-h">'+
				            	'<div ng-style="{\'width\': app.imgToolsProps.width + \'px\', \'height\': app.imgToolsProps.height + \'px\', \'background-image\': \'url(\' + app.postFeaturedImage + \')\', \'background-position\': \'50% 50%\'}" '+
				            	'class="bg-cover center-img md-whiteframe-z2 focus-area" '+
				            	'ng-mousemove="app.imgToolsProps.onMouseMove($event)" ng-mousedown="app.imgToolsProps.onMouseDown($event)" ng-mouseup="app.imgToolsProps.onMouseUp($event)">'+
				            		'<span class="target lfy-focuspoint-button" ng-style="{\'left\': app.imgToolsProps.x + \'%\', \'top\': app.imgToolsProps.y + \'%\'}"></span>'+
				            	'</div>'+
										'</div>'+
			            '</div>'+
			          '</div>'+
			        '</div>'+
			      '</div>'+
			    '</div>'+
			    '<div class="col w-xxl text-md font-500">'+
			      '<div class="vbox">'+
			        '<div class="row-row">'+
			          '<div class="cell">'+
			            '<div class="cell-inner" style="background-image: url(/images/device-stubs.png); background-position: center bottom; background-repeat: no-repeat;">'+
			            	'<figure class="pos-abt bg-cover" id="focuspoint-tablet" '+ 
			            		'style=" width: 82px;'+
									    'height: 124px;'+
									    'border-radius: 3px;'+
									    'top: 30px;'+
									    'left: 107px;"'+
									    'ng-style="{\'background-image\': \'url(\' + app.postFeaturedImage+ \')\', \'background-position\': app.imgToolsProps.x + \'% \' + app.imgToolsProps.y + \'%\'}"></figure>'+
										'<figure class="pos-abt bg-cover" id="focuspoint-mobile" '+
											'style="width: 100px;'+
									    'height: 43px;'+
									    'border-radius: 3px;'+
									    'top: 183px;'+
									    'left: 104px;"'+
									    'ng-style="{\'background-image\': \'url(\' + app.postFeaturedImage+ \')\', \'background-position\': app.imgToolsProps.x + \'% \' + app.imgToolsProps.y + \'%\'}"></figure>'+
										'<figure class="pos-abt bg-cover" id="focuspoint-laptop" '+
											'style="width: 124px;'+
									    'height: 85px;'+
									    'border-radius: 3px;'+
									    'top: 248px;'+
									    'left: 94px;"'+
									    'ng-style="{\'background-image\': \'url(\' + app.postFeaturedImage+ \')\', \'background-position\': app.imgToolsProps.x + \'% \' + app.imgToolsProps.y + \'%\'}"></figure>'+
			            '</div>'+
			          '</div>'+
			        '</div>'+
			      '</div>'+
			    '</div>'+
		    '</div>'+
	    '</div>'+
	    '</div>'+
    '</div>'+
		'<div class="text-center p-t">'+
			'<md-button ng-click="app.cancelDialog();" class="m-0">'+
				'{{\'titles.CANCEL\' | translate}}'+
			'</md-button>'+
			'<md-button ng-click="app.clearPostContent();" class="m-0">'+
				'{{\'titles.SAVE\' | translate}}'+
			'</md-button>'+
		'</div>'+
	'</md-dialog>';