package com.wordrails.business;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.wordrails.WordrailsService;
import com.wordrails.test.AbstractTest;
import com.wordrails.util.WordpressParsedContent;
import com.wordrails.util.WordrailsUtil;

public class PostEventHandlerTest extends AbstractTest {
	
	@Autowired PostEventHandler postEventHandler;
	@Autowired TermEventHandler termEventHandler;
	@Autowired WordrailsService wordrailsService;
	
	@Test
	public void test() throws Exception {
		String content = "<div class='text-black pt-serif blue ng-binding' ng-style='app.customStyle.secondaryFont' bind-html-unsafe='post.body' style='font-family: 'PT Serif', sans-serif;'>[caption id='attachment_66378' align='alignnone' width='770']<a href='http://cockpitblogs.ne10.com.br/torcedor/wp-content/uploads/2015/04/marcelo-770.jpg'><img class='size-full wp-image-66378' src='http://cockpitblogs.ne10.com.br/torcedor/wp-content/uploads/2015/04/marcelo-770.jpg' alt='Foto: Divulgação/FPF' width='770' height='416'></a> Foto: Divulgação/FPF[/caption] Marcelo de Lima Henrique é quem vai comandar o primeiro jogo da decisão entre Salgueiro e Santa Cruz, no Cornélio de Barros, na próxima quarta-feira, às 22h. Ele será auxiliado por Clóvis Amaral e Fernanda Colombo. Já a volta terá Emerson Sobral como árbitro principal ao lado de Albert Júnior e Elan Vieira. O segundo jogo será no domingo, às 16h, no Arruda. A Federação Pernambucana também definiu quem apita os jogos entre Sport e Central. Sebastião Rufino Filho comanda a primeira partida, no Lacerdão, enquanto Giorgio Wilton o segundo, na Ilha do Retiro.</div>";
//		WordpressParsedContent wpc = WordrailsUtil.extractImageFromContent(content);
		WordpressParsedContent wpc = wordrailsService.extractImageFromContent(content);
		System.out.println(wpc);
	}
}