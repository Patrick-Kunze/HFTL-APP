package bkmi.de.hftl_app.help;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Klasse um die News von der HftL-Seite zu laden
 */
public class NewsResolver {

    Document doc;
    ArrayList<HftlEvent> termine;

    /*
    * Konstruktor initialisiert die übergebene URL als Document
     */
    public NewsResolver(String url) {
        getHtmlAsDoc(url);
    }

    private boolean getHtmlAsDoc(String url) {
        try {
            doc = Jsoup.connect(url).get();
            return true;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ----- Funktionen für das NewsFragment -----

    /*
    * Erzeugt eine Liste mit den nächsten Terminen von der Hftl-Startseite
     */
    private void getHftlEvent(){
        termine= new ArrayList<>();

        Elements elements = doc.getElementsByClass("news-list-item");
        for(int i=0; i<elements.size(); i++){
            HftlEvent termin = new HftlEvent();
            termin.time= elements.get(i).child(0).ownText();
            termin.url="https://www.hft-leipzig.de/"+elements.get(i).child(1).child(0).attr("href");
            termin.text=elements.get(i).child(1).child(0).ownText();
            termine.add(termin);
        }
    }

    /*
    * Erzeugt eine String-Array mit allen Terminen
     */
    public String[] getTermineStringArray(){

        getHftlEvent();
        if(termine.size()==0) return null; //Falls keine Termine anstehen

        String[] s = new String[termine.size()+1];
        s[0]="Die aktuellen News sind:";   // Für die ListView das erste Element,

        int i = 1;
        for(HftlEvent termin:termine ){
            s[i++]= "Am " + termin.time + ":\n" + termin.text + "\nFür mehr Infos antippen." ;
        }

        return s;
    }

    /*
    * Gibt die URL des Termines an Position "position" zurück, wird für die Detailanzeige benötigt.
     */
    public String getUrlAsString(int position){
        return termine.get(position).url;
    }

    /* ----- Funktion für die NewsClickedActivty -----
    *   Sucht nach den Details auf der HfTl-Seite und gibt sie als String-Array zurück
     */
    public String[] getDetailsStringArray(){
        String[] s=new String[6];
        Elements elements = doc.getElementsByClass("news-single-item");

        s[0]=elements.get(0).child(1).text()+"\n";   //Überschrift
        s[1]=elements.get(0).child(2).text()+"\n";   //Subhead
        s[2]=elements.get(0).child(0).text();//Zeit
        s[3]=elements.get(0).child(3).text();   //Text

        return s;
    }

}
