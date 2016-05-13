package model;

import java.io.Serializable;
import java.util.Calendar;

public class LogLine implements Serializable{

    /*
   timeStamp : objet de classe Calendar, contient l'heure à laquelle la commande a été validée
   vente : objet de classe Vente, contient le Produit vendu, le nombre d'objet vendu ainsi que la méthode de paiement
     */
    private Calendar timeStamp;
    private Vente vente;



    public LogLine(Vente vente){
        this.vente = vente;
        //Calendar.getInstance() créé un nouvel objet Calendar, avec l'heure actuelle du système (UT)
        timeStamp = Calendar.getInstance();
    }

    //Renvoie une chaine de caractères contenant le temps et les différentes informations de la vente
    public String toString(){
        return (getTimeString()+"\t"+vente.toString());
    }

    //Renvoie l'heure de la vente sous la forme <HH:MM:SS>
    private String getTimeString(){
        return "<"+timeStamp.get(Calendar.HOUR_OF_DAY)+":"+timeStamp.get(Calendar.MINUTE)+":"+timeStamp.get(Calendar.SECOND)+">";
    }
}
