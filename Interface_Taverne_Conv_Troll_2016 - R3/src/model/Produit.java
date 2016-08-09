package model;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;


public class Produit {
    private final SimpleStringProperty nom;
    private SimpleStringProperty catNom;
    private final SimpleDoubleProperty prix;
    private final SimpleIntegerProperty nombreVendu;
    private final SimpleIntegerProperty nombreStock;

    public Produit(String name, double prix, String cat, int nombreStock, int nombreVendu){
        this.nom=new SimpleStringProperty(name);
        this.prix=new SimpleDoubleProperty(prix);
        this.catNom=new SimpleStringProperty(cat);

        this.nombreStock=new SimpleIntegerProperty(nombreStock);
        this.nombreVendu=new SimpleIntegerProperty(nombreVendu);
    }

    public String getPrix(){
        return Double.toString(prix.get()).format("%.2f",prix.get())+" €";
    }
    public String getPrixWE() { return Double.toString(prix.get()).format("%.2f",prix.get()); }
    public double getPrixDouble() { return prix.get(); }
    public String getNom(){
        return nom.get();
    }
    public String getCatNom(){
        return catNom.get();
    }
    public String getNombreVendu() {
        return Integer.toString(nombreVendu.get());
    }
    public String getNombreStock() {
        return Integer.toString(nombreStock.get());
    }
    public void setNombreVendu(int nb) { nombreVendu.set(nb); }
    public void setNombreVendu(String str) { nombreVendu.set(Integer.parseInt(str)); }
    public void setNombreStock(int nb) { nombreStock.set(nb); }
    public void setNombreStock(String str) { nombreStock.set(Integer.parseInt(str)); }

    public boolean testStock(){
        return nombreStock.get()>0;
    }

    public String toString(){

        String disponibilite = new String(" ");
        if(nombreStock.get()<=0)
            disponibilite = disponibilite+"   (RUPTURE DE STOCK)";
        else{
            if(nombreStock.get()<30)
                disponibilite = disponibilite+"   ("+getNombreStock()+")";
            else
                disponibilite = disponibilite+"   (30+)";
        }

        return nom.get()+disponibilite;
    }
}
