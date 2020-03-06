package scrapper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import server.Handler;
import user.*;

import java.io.IOException;

/**
 * Class to Parse HTML and import all data to database
 * @author tomas
 * 
 */
public class HtmlParser {
	

	/**
	 * Method to parse html and import to database
	 * @throws IOException error
	 */
	public void importar() throws IOException {
	   
	  //Class para transformar string em slug
	  Slug slug = new Slug();
	  
	  int numJogadores = 0, numEquipas = 0;
	  //Definos o url que queremos
	  String url = "http://www.zerozero.pt/edition_teams.php?id=125220";
	  
	  //Pegamos no HTML
      Document document = Jsoup.connect(url).get();
      
      //Obtemos as classes <div class="text">
      Elements textclass = document.getElementsByClass("zztable zzlist");
      Elements rows = textclass.select("tr");
      
      for (int i = 0; i < rows.size(); i++) {
    	  
    	  //Obtem o nome da equipa e o link
    	  Element td = rows.get(i).select("td").first();
    	  Element text = td.select("div.text").first();
    	  Element alink = text.select("a[href]").first();
    	  String nomeEquipa = alink.text();
    	  String link = alink.attr("href");
    	  
    	  //Vamos ao link da equipa
    	  String urlequipa = "http://www.zerozero.pt/" + link;
    	  Document doc = Jsoup.connect(urlequipa).get();
    	  
    	  System.out.println(nomeEquipa + " " + link);
    	  
    	  Clube c = new Clube(slug.makeSlug(nomeEquipa),nomeEquipa,slug.makeSlug(nomeEquipa),false,"Portuguesa",2000);
    	  String club_id = c.save();
			
    	  numEquipas++;
    	  Elements teamclass = doc.select("div#team_squad");
    	  Elements box = teamclass.select("div.innerbox");
    	  
    	  
    	  for (int j = 0; j < box.size(); j++) {
    		  Elements staffs = box.get(j).select("div.staff");
    		  String posicao = box.get(j).selectFirst("div.title").text();
    		  for (int k = 0; k < staffs.size(); k++) {
    			  String nome = staffs.get(k).select("div.text").first().select("a[href]").first().text();
    			  String numero = staffs.get(k).select("div.number").first().text();
    			  String idade = staffs.get(k).select("span").first().text();
  
    			  Player p = new Player(nome,slug.makeSlug(nome),slug.makeSlug(nome),false,club_id,idade.split(" ")[0],posicao);
    			  p.save();
    			  
    			  System.out.println(posicao + " " + nome + " " + numero + " " + idade);
    			  numJogadores++;
    		  }
    	  }
    	  
      }
      System.out.println("Jogadores importados:" + numJogadores);
      System.out.println("Equipas importados:" + numEquipas);
   }

}
