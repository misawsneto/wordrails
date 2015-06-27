package com.wordrails.api;

import org.joda.time.DateTime;

import com.wordrails.business.Station;

import retrofit.RestAdapter.LogLevel;
import retrofit.RetrofitError;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Test {

	public static void main(String[] args) throws IOException {
		WordRails wordRails = new WordRails(
				new MockConnectivityManager(true),
				new File("."),
				0,
				"http://demo.xarxlocal.com",
				"silvio",
				"silvio",
				LogLevel.FULL
		);

		wordRails.login();
		
		PersonDto person = wordRails.getPerson(2);
		System.out.println(person);
		
		NetworkDto network = wordRails.getNetwork(1);
		
		try{
		StationDto station = new StationDto();
		station.name = "Station 1";
		station.networks = new HashSet<String>();
		station.networks.add(wordRails.getSelf(network));
		station.visibility = Station.RESTRICTED_TO_NETWORKS;
		station.writable = true;
		station.main = false;
		
		wordRails.postStation(station);
		station = wordRails.getStation(station.id);
		System.out.println(station);
		}catch(RetrofitError e){
			System.out.println(e.getBodyAs(String.class));
		}
		
//		createPost(wordRails);

//		NetworkDto network = wordRails.getNetwork(1);
//
//		PersonDto person = wordRails.getPerson(3);
//		FavoriteDto favorite = new FavoriteDto();
//		favorite.post = postDto.getSelf();
//		favorite.person = person.getSelf();
//		
//		wordRails.postFavorite(favorite);

//		wordRails.findPostReadByPersonIdOrderByDate(personId)

//		PersonData data = wordRails.getInitialData();
//		System.out.println(data.person.name);
//		wordRails.logout();
//
//		data = wordRails.getInitialData();
//		System.out.println(data.person.name);
//		if(true)
//			return;
//
//		String phrase = "This is a phrase";
//
//		String[] splitPhrase = phrase.split("\\s+");
//
//		int limit = splitPhrase.length >= 100 ? 100 : splitPhrase.length;
//
//		if(true)
//			return;

//		WordRails wordRails = new WordRails(
//			new MockConnectivityManager(true),
//			new File("."), 
//			1024 * 1024, 
//			"http://localhost:8080", 
//			"wordrails", 
//			"wordrails", 
//			LogLevel.NONE
//		);
//
//		PersonDto person = wordRails.getPerson(1);
//		System.out.println(person);
//		
//		NetworkDto network = wordRails.getNetwork(1);
//		
//		try{
//		StationDto station = new StationDto();
//		station.name = "Station 1";
//		station.networks = new HashSet<String>();
//		station.networks.add(wordRails.getSelf(network));
//		station.visibility = Station.RESTRICTED_TO_NETWORKS;
//		station.writable = true;
//		wordRails.postStation(station);
//		station = wordRails.getStation(station.id);
//		System.out.println(station);
//		}catch(RetrofitError e){
//			System.out.println(e.getBodyAs(String.class));
//		}

//		NetworkDto network = new NetworkDto();
//		network.name = "Network 1";
//		wordRails.postNetwork(network);
//		network = wordRails.getNetwork(1);
//		System.out.println(network);
//		
//		stationRoleDto stationRole = new stationRoleDto();
//		stationRole.admin = true;
//		stationRole.network = wordRails.getSelf(network);
//		stationRole.person = wordRails.getSelf(person);
//		wordRails.poststationRole(stationRole);
//		stationRole = wordRails.getstationRole(stationRole.id);
//		System.out.println(stationRole);
//
//		StationPermissionsDto stationPermissions = new StationPermissionsDto();
//		stationPermissions.writePermission = true;
//		stationPermissions.readPermission = true;
//		stationPermissions.visibility = "UNRESTRICTED";
////		stationPermissions = wordRails.getStationPermissions(stationPermissions.id);
//		System.out.println(stationPermissions);
//
//		StationDto station = new StationDto();
//		station.name = "Station 1";
//		station.networks = new HashSet<String>();
//		station.networks.add(wordRails.getSelf(network));
//		wordRails.postStation(station);
//		station = wordRails.getStation(49);
//		System.out.println(station);
//		
//		FileDto file = new FileDto();
//		file.type = "I";
//		wordRails.postFile(file);
//		file = wordRails.getFile(file.id);
//		System.out.println(file);
//
//		wordRails.putFileContents(file.id, new TypedFile("image/jpeg", new java.io.File("Alice.jpg")));
//		
//		ImageDto image = new ImageDto();
//		image.original = wordRails.getSelf(file);
//		wordRails.postImage(image);
//		image = wordRails.getImage(image.id);
//		System.out.println(image);
//		
//		PostDto post = new PostDto();
//		post.title = "Post 1";
//		post.body = "Post 1";
//		post.scheduledDate = new DateTime(new Date().getTime()).plusMinutes(2).toDate();
//		post.author = wordRails.getSelf(person);
//		post.station = wordRails.getSelf(station);
//		wordRails.postPost(post);
//		post = wordRails.getPost(post.id);
//		System.out.println(post);
//		
//		wordRails.putPostFeaturedImage(post.id, wordRails.getSelf(image));
//		wordRails.putImagePost(image.id, wordRails.getSelf(post));
//
//		PostProjectionDto postProjection = wordRails.getPost_PostProjection(post.id);
//		for (ImageProjectionDto imageProjection : postProjection.images) {
//			System.out.println(imageProjection.small);
//			System.out.println(imageProjection.medium);
//			System.out.println(imageProjection.large);
//		}
	}

	private static void createPost(WordRails wordRails) {
		String person = wordRails.getSelf(wordRails.getPerson(2));
		String station = wordRails.getSelf(wordRails.getStation(49));

		PostDto post = new PostDto();
		post.title = "Post 1";
		post.body = "Post 1";
		post.scheduledDate = new DateTime(new Date().getTime()).plusMinutes(2).toDate();
		post.author = person;
		post.station = station;
		wordRails.postPost(post);
		post = wordRails.getPost(post.id);
		System.out.println(post);
	}

	public static void createDevEnv(WordRails wordRails) {

		TaxonomyDto localizacao = new TaxonomyDto();
		localizacao.name = "Localização";
		localizacao.type = "N";
		localizacao.id = wordRails.postTaxonomy(localizacao);
		localizacao = wordRails.getTaxonomy(localizacao.id);

		TaxonomyDto categoria = new TaxonomyDto();
		categoria.name = "Categoria";
		categoria.type = "N";
		categoria.id = wordRails.postTaxonomy(categoria);
		categoria = wordRails.getTaxonomy(categoria.id);

		NetworkDto network = new NetworkDto();
		network.name = "Rede Teste";
		network.id = wordRails.postNetwork(network);
		network = wordRails.getNetwork(network.id);

		wordRails.putTaxonomyOwningNetwork(localizacao.id, network.getSelf());
		wordRails.putTaxonomyOwningNetwork(categoria.id, network.getSelf());

		StationDto station1 = new StationDto();
		station1.name = "Estação 1";
		station1.networks = new HashSet<String>();
		station1.networks.add(network.getSelf());
		wordRails.postStation(station1);

		StationDto station2 = new StationDto();
		station2.name = "Estação 2";
		station2.networks = new HashSet<String>();
		station2.networks.add(network.getSelf());
		wordRails.postStation(station2);

		StationDto station3 = new StationDto();
		station3.name = "Estação 3";
		station3.networks = new HashSet<String>();
		station3.networks.add(network.getSelf());
		wordRails.postStation(station3);

		List<String> l = new ArrayList<String>();
		l.add(network.getSelf());

		for (StationDto s : wordRails.getStations(0, 10, null))
			wordRails.putStationNetworks(s.id, l);

		TermDto cat1 = new TermDto();
		cat1.name = "Categoria 1";
		cat1.taxonomy = categoria.getSelf();
		cat1.id = wordRails.postTerm(cat1);
		cat1 = wordRails.getTerm(cat1.id);
		TermDto cat2 = new TermDto();
		cat2.name = "Categoria 2";
		cat2.taxonomy = categoria.getSelf();
		cat2.id = wordRails.postTerm(cat2);
		cat2 = wordRails.getTerm(cat2.id);
		TermDto cat3 = new TermDto();
		cat3.name = "Categoria 3";
		cat3.taxonomy = categoria.getSelf();
		cat3.id = wordRails.postTerm(cat3);
		cat3 = wordRails.getTerm(cat3.id);
		TermDto cat1Child1 = new TermDto();
		cat1Child1.name = "Cat 1 Child 1";
		cat1Child1.taxonomy = categoria.getSelf();
		cat1Child1.id = wordRails.postTerm(cat1Child1);
		cat1Child1 = wordRails.getTerm(cat1Child1.id);
		TermDto cat1Child2 = new TermDto();
		cat1Child2.name = "Cat 1 Child 2";
		cat1Child2.taxonomy = categoria.getSelf();
		cat1Child2.id = wordRails.postTerm(cat1Child2);
		cat1Child2 = wordRails.getTerm(cat1Child2.id);
		TermDto cat1Child3 = new TermDto();
		cat1Child3.name = "Cat 1 Child 3";
		cat1Child3.taxonomy = categoria.getSelf();
		cat1Child3.id = wordRails.postTerm(cat1Child3);
		cat1Child3 = wordRails.getTerm(cat1Child3.id);
		TermDto cat1Child1Child1 = new TermDto();
		cat1Child1Child1.name = "Cat 1 Child 1 Child 1";
		cat1Child1Child1.taxonomy = categoria.getSelf();
		cat1Child1Child1.id = wordRails.postTerm(cat1Child1Child1);
		cat1Child1Child1 = wordRails.getTerm(cat1Child1Child1.id);
		TermDto cat1Child1Child2 = new TermDto();
		cat1Child1Child2.name = "Cat 1 Child 1 Child 2";
		cat1Child1Child2.taxonomy = categoria.getSelf();
		cat1Child1Child2.id = wordRails.postTerm(cat1Child1Child2);
		cat1Child1Child2 = wordRails.getTerm(cat1Child1Child2.id);
		wordRails.putTermParent(cat1Child1.id, cat1.getSelf());
		wordRails.putTermParent(cat1Child2.id, cat1.getSelf());
		wordRails.putTermParent(cat1Child3.id, cat1.getSelf());
		wordRails.putTermParent(cat1Child1Child1.id, cat1Child1.getSelf());
		wordRails.putTermParent(cat1Child1Child2.id, cat1Child1.getSelf());

		TermDto brasil = new TermDto();
		brasil.name = "Brasil";
		brasil.taxonomy = localizacao.getSelf();
		brasil.id = wordRails.postTerm(brasil);
		brasil = wordRails.getTerm(brasil.id);
		TermDto pernambuco = new TermDto();
		pernambuco.name = "Pernambuco";
		pernambuco.taxonomy = localizacao.getSelf();
		pernambuco.id = wordRails.postTerm(pernambuco);
		wordRails.putTermParent(pernambuco.id, brasil.getSelf());

		PersonDto person = wordRails.getPerson(1);
		List<StationDto> stas = wordRails.getStations(0, 50, null);
		List<TermDto> locs = wordRails.getTaxonomyTerms(1);
		List<TermDto> cats = wordRails.getTaxonomyTerms(2);
		for (StationDto s : stas) {
			for (int i = 0; i < 100; i++) {
				TermDto loc = locs.get(new Random().nextInt(locs.size()));
				TermDto cat = cats.get(new Random().nextInt(cats.size()));

				PostDto p = new PostDto();
				p.station = s.getSelf();
				p.author = person.getSelf();
				p.body = "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.";
				p.title = "post " + new Random().nextInt();
				p.id = wordRails.postPost(p);
				p = wordRails.getPost(p.id);

				List<String> terms = new ArrayList<String>();
				terms.add(loc.getSelf());
				terms.add(cat.getSelf());
				wordRails.putPostTerms(p.id, terms);

				//addImage("test.jpg", p.getSelf());
			}
		}
	}
}