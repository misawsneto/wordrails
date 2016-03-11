app.controller('SettingsPostCtrl', ['$scope', '$log', '$timeout', '$mdDialog', '$state', 'FileUploader', 'TRIX', 'cfpLoadingBar', 'trixService', 'trix', '$http', '$mdToast', '$templateCache', '$location', '$interval', '$mdSidenav', '$translate',
	function($scope ,  $log ,  $timeout ,  $mdDialog ,  $state ,  FileUploader ,  TRIX ,  cfpLoadingBar ,  trixService ,  trix ,  $http ,  $mdToast, $templateCache  , $location, $interval, $mdSidenav, $translate){

	$scope.content = '';

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
		  tabSize: 2,
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

	$scope.customizedLink = {
		slug: ""
	}

	var mockPostLoad = function(){
		$scope.editingPost = createPostStub();
		$scope.customizedLink.slug = $scope.editingPost.slug;
	}

	var test = function(){
		mockPostLoad();
	}

	test();

	// ---------- /time config ----------    

	settingsPostCtrl = $scope;
}]);

var settingsPostCtrl = null;

/**
 * Post Stub
 * @return {Post} [mock]
 */
function createPostStub(){
	return {"title":"Foi o amor pela Rainha do Sul que tramou El Chapo Guzmán","featuredImageHash":"a1eb4d302ad9413be802d5d0bf119028","imageSmallHash":"44c8067b27a4a6a8d12ffdef6b34f5d8","imageMediumHash":"44c8067b27a4a6a8d12ffdef6b34f5d8","imageLargeHash":"a1eb4d302ad9413be802d5d0bf119028","categories":[{"id":1297,"name":"PODER"}],"tags":["sinaloa","el chapo","kate del castillo"],"terms":[{"termId":1297,"termName":"PODER","parentId":null,"taxonomyId":140,"imageHash":null,"children":null}],"sponsored":null,"date":1452799663000,"snippet":"Quem vê telenovelas mexicanas sabe que o amor tanto pode ser a salvação como uma perdição. E no caso do mais famoso barão do narcotráfico JoaquinEl Chapo Guzmán, um dos homens mais procurados da América do Norte, a paixão pela actriz Kate del Castillo acabou por revelar-se fatídica: foi a sua obsessão por ela, que o levou a organizar um encontro clandestino em plena selva para que pudessem conhecer-se melhor, que conduziu a polícia até ao seu esconderijo. A vistosa actriz de 43 anos, uma das mais famosas estrelas de cinema e televisão do México , chamou a atenção do","body":"<p>Quem vê telenovelas mexicanas sabe que o amor tanto pode ser a salvação como uma perdição. E no caso do mais famoso barão do narcotráfico Joaquin<em>El Chapo</em> Guzmán, um dos homens mais procurados da América do Norte, a paixão pela actriz Kate del Castillo acabou por revelar-se fatídica: foi a sua obsessão por ela, que o levou a organizar um encontro clandestino em plena selva para que pudessem conhecer-se melhor, que conduziu a polícia até ao seu esconderijo.\n</p><p>A vistosa actriz de 43 anos, uma das mais famosas estrelas de cinema e televisão do México , chamou a atenção do chefe do cartel de Sinaloa por causa de uma polémica mensagem que publicou no Twitter, no início de 2012, quando a guerra do Exército mexicano ao narcotráfico estava ao rubro. Era uma declaração política, mas que <em>El Chapo</em> terá interpretado como uma manifestação de afecto: dizia Kate que confiava mais nele do que no Governo, e como “seria maravilhoso se começasse a traficar com amor”.\n</p><p>Perante o impacto político das suas palavras, Castillo publicou uma série de justificações, incluindo que não estava a falar a sério, ou que a sua ironia e sarcasmo estavam a ser mal entendidas. Mas tomadas no seu todo, as suas declarações no Twitter constituem uma espécie de declaração – que se os perfis psicológicos de Joaquin Guzmán forem minimamente fidedignos, terá sido música para os ouvidos deste homem “sedutor, esplêndido e protector”, que apesar de ignorante e pouco interessado é “astuto e inteligente”, além de “compulsivo e tenaz”, que nunca desiste até conseguir o que quer.\n</p><p>As biografias de Kate também salientam a sua inteligência, a par da sua beleza e capacidade de trabalho. Filha de Éric del Castillo, uma das lendas da chamada época de ouro do cinema mexicano e estrela de telenovelas, Kate entrou para o negócio de família logo aos oito anos de idade. Nunca mais parou desde então, representando no teatro, cinema e também séries televisivas de enorme popularidade e audiência, tanto no México como nos Estados Unidos. Por várias vezes, interpretou o papel de mulheres ambiciosas, poderosas e temidas: na novela <em>A Rainha do Sul</em>, baseada no livro de Arturo Pérez-Reverte; na série <em>Erva</em> e na produção internacional <em>Donos do Paraíso</em> – todas estas protagonistas eram narcotraficantes.\n</p><p><em>El Chapo</em> estava detido na prisão de alta segurança de Altiplano quando iniciou contacto com a actriz. À sua mensagem inicial no Twitter respondeu com flores; depois iniciaram uma correspondência, ora manuscrita, ora por mensagens em código utilizando o telefone móvel do seu advogado, Andrés Granado, que se tornou mediador do diálogo entre os dois. Em Agosto de 2014 surgiu a hipótese de trabalharem juntos na produção de um <em>biopic</em>, alegadamente porque Guzmán lhe confessou o seu desejo de ver a sua história passada ao cinema.\n</p><p>A comunicação intensificou-se após a espectacular fuga da cadeia de El Chapo, em Julho de 2015, sempre usando o mesmo método, até que decidiu prescindir do intermediário: segundo revelam transcrições das mensagens, divulgadas pelo jornal <em>Milénio</em>, o narcotraficante fez chegar a Kate del Castillo o último modelo de telefone lançado pela Blackberry, para poderem falar directamente.\n</p><p>A conversa entre os dois nas mensagens entretanto publicadas é idêntica à de muitos casais apaixonados, sem referência a filmes ou negócios. No último capítulo do seu namoro electrónico, <em>El Chapo</em> já não esconde os seus sentimentos: convida-a para um encontro nas montanhas onde está escondido, e empenha-se para que tudo esteja a gosto da actriz – a proposta é para que passem três dias juntos. “Se trouxeres vinho, também bebo”, promete, informando-a que habitualmente não bebe, mas quando o faz prefere tequila e whisky.\n</p><p>A resposta de Kate é calorosa. Pode ser que nessa altura, como escreve o jornal <em>El País</em>, já tivesse cruzado definitivamente a linha que separa realidade e ficção, transmutando-se na sua personagem da <em>Rainha do Sul</em>, Teresa Mendoza, uma jovem inocente de Sinaloa que depois do assassinato do noivo se torna na líder implacável de um cartel da droga. “Fico muito comovida quando me dizes que vais cuidar de mim, nunca ninguém o fez por isso obrigada”, diz. “Não fazes ideia de como estou entusiasmada. E ansiosa por olhar-te nos olhos, em pessoa”, acrescenta.\n</p>","topper":"DROGAS","state":"PUBLISHED","readsCount":12,"bookmarksCount":0,"recommendsCount":0,"commentsCount":0,"readTime":8,"imageLandscape":true,"authorName":"Demo","authorUsername":"demo","authorId":51,"authorEmail":"demo@demo.com","authorTwitter":null,"authorCover":"2eeb3bc6887ab6a24168e9492b5dbdbf","authorProfilePicture":"bc3c2042f9c3474ccbebd9b8b40533c4","authorCoverMediumHash":"2eeb3bc6887ab6a24168e9492b5dbdbf","authorImageSmallHash":"3a10d25d632ec336bc31bc1e6f17f8e1","authorCoverUrl":null,"authorImageUrl":null,"stationName":"O MUNDO","stationId":11,"stationIdString":null,"slug":"foi-o-amor-pela-rainha-do-sul-que-tramou-el-chapo-guzman-n8r2q8ub","externalFeaturedImgUrl":null,"externalVideoUrl":null,"imageCaptionText":"Vários DVD's da A Rainha do Sul foram encontrados no esconderijo de Guzmán DR","imageCreditsText":null,"imageTitleText":null,"featuredVideoHash":null,"featuredAudioHash":null,"lat":null,"lng":null,"subheading":"Mensagens trocadas entre o perigoso narcotraficante, chefe do cartel Sinaloa, e a actriz mexicana Kate del Castillo, revelam um homem apaixonado.","scheduledDate":null,"notify":false,"id":6017,"postId":6017}
}