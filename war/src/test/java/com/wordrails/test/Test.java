package com.wordrails.test;

import com.wordrails.business.Image;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Test {

	public static void main(String[] args) throws IOException {
//		String rawJson = "{\"user\":\"trix_user\",\"password\":false,\"domain\":\"http:\\/\\/taqueo.pe.hu\\/wp-json\",\"token\":\"123999a2022ada6dbc4adc31dfceb987\",\"terms\":{\"category\":[{\"ID\":\"8\",\"name\":\"teste nova categoria\",\"slug\":\"teste-nova-categoria\",\"taxonomy\":\"category\",\"parent\":\"0\"},{\"ID\":\"1\",\"name\":\"Uncategorized\",\"slug\":\"uncategorized\",\"taxonomy\":\"category\",\"parent\":\"0\"}],\"post_tag\":[{\"ID\":\"14\",\"name\":\"312312\",\"slug\":\"312312\",\"taxonomy\":\"post_tag\",\"parent\":\"0\"},{\"ID\":\"13\",\"name\":\"dadasd\",\"slug\":\"dadasd\",\"taxonomy\":\"post_tag\",\"parent\":\"0\"},{\"ID\":\"11\",\"name\":\"dasdas\",\"slug\":\"dasdas\",\"taxonomy\":\"post_tag\",\"parent\":\"0\"},{\"ID\":\"18\",\"name\":\"dasdsa\",\"slug\":\"dasdsa\",\"taxonomy\":\"post_tag\",\"parent\":\"0\"},{\"ID\":\"12\",\"name\":\"ddddddddd\",\"slug\":\"ddddddddd\",\"taxonomy\":\"post_tag\",\"parent\":\"0\"},{\"ID\":\"15\",\"name\":\"dsaad\",\"slug\":\"dsaad\",\"taxonomy\":\"post_tag\",\"parent\":\"0\"},{\"ID\":\"16\",\"name\":\"dsads\",\"slug\":\"dsads\",\"taxonomy\":\"post_tag\",\"parent\":\"0\"},{\"ID\":\"9\",\"name\":\"tag1\",\"slug\":\"tag1\",\"taxonomy\":\"post_tag\",\"parent\":\"0\"},{\"ID\":\"10\",\"name\":\"tag2\",\"slug\":\"tag2\",\"taxonomy\":\"post_tag\",\"parent\":\"0\"},{\"ID\":\"4\",\"name\":\"teste1\",\"slug\":\"teste1\",\"taxonomy\":\"post_tag\",\"parent\":\"0\"},{\"ID\":\"5\",\"name\":\"teste2\",\"slug\":\"teste2\",\"taxonomy\":\"post_tag\",\"parent\":\"0\"}]}}";
//
//		ObjectMapper mapper = new ObjectMapper();
//		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//		WordpressConfig posts =

		String body = "<p class=\"\">Um temporal que caiu na tarde de ontem, em Petrolina, no Sertão do São Francisco (foto), deixou ruas, casas e espaços públicos alagados. Segundo o tenente do Corpo de Bombeiros da cidade, Allisson Careiro, a chuva começou por volta das 16h30 e seguiu até 17h30, com meia hora de forte intensidade.</p><p class=\"\">Cinco pessoas, sendo duas crianças, foram resgatadas de dentro de um carro encoberto pela água na Orla. O teto de uma loja do River Shopping desabou. Na última quarta-feira, outro temporal atingiu a cidade.</p><p class=\"\"><img class=\"fr-fin\" data-fr-image-preview=\"false\" alt=\"Image title\" src=\"/api/files/406/contents\" width=\"616\"></p><p class=\"\"><br></p><p class=\"\">A previsão, de acordo com o Laboratório de Meteorologia da Univasf, é de que a chuva se estenda até quinta-feira. A região está recebendo uma zona de convergência do Atlântico Sul, causando a mudança do clima.&nbsp;</p><p class=\"\">Leia mais...</p><p class=\"\"><a href=\"http://jconline.ne10.uol.com.br/canal/cidades/regional/noticia/2014/11/16/domingo-de-chuva-forte-em-petrolina-no-sertao-de-pernambuco-156377.php\" rel=\"nofollow\">http://jconline.ne10.uol.com.br/canal/cidades/regional/noticia/2014/11/16/domingo-de-chuva-forte-em-petrolina-no-sertao-de-pernambuco-156377.php</a><br></p><p class=\"\"><br></p><p class=\"\"><img class=\"fr-fin\" data-fr-image-preview=\"false\" alt=\"Image title\" src=\"/api/files/407/contents\" width=\"630\"></p><p class=\"\"><br></p>";
		Matcher m = Pattern.compile("/api/files/\\d+/contents").matcher(body);
		while (m.find()) {
			Integer id = Integer.valueOf(m.group().replace("/api/files/", "").replace("/contents", ""));
			System.out.println(id);
		}
	}
}
