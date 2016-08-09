package items;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Created by Guillaume on 29/04/2016.
 */
public abstract class Vendable {

    protected final SimpleStringProperty nom;
    protected SimpleStringProperty catNom;
    protected final SimpleDoubleProperty prix;
    protected final SimpleIntegerProperty nombreVendu;
    protected final SimpleIntegerProperty nombreStock;
    private final SimpleIntegerProperty nombreBufferVente;


    public Vendable(String name, double prix, String cat, int nombreStock, int nombreVendu){
        this.nom=new SimpleStringProperty(name);
        this.prix=new SimpleDoubleProperty(prix);
        this.catNom=new SimpleStringProperty(cat);

        this.nombreStock=new SimpleIntegerProperty(nombreStock);
        this.nombreVendu=new SimpleIntegerProperty(nombreVendu);
        this.nombreBufferVente = new SimpleIntegerProperty(0);
    }

    public String getPrix(){
        return Double.toString(this.prix.get()).format("%.2f",prix.get())+" €";
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
    public int getNombreBufferVente() {return nombreBufferVente.get();}
    public void setNombreVendu(int nb) { nombreVendu.set(nb); }
    public void setNombreVendu(String str) { nombreVendu.set(Integer.parseInt(str)); }
    public void setNombreStock(int nb) { nombreStock.set(nb); }
    public void setNombreStock(String str) { nombreStock.set(Integer.parseInt(str)); }
    public void setNombreBufferVente(int nb) {nombreBufferVente.set(nb);}
    public boolean testStock(){
        return nombreStock.get()>0;
    }

    public String toString(){

        String disponibilite = new String(" ");
        if(nombreStock.get()<=-1)
            disponibilite = disponibilite+"    (Aucun ingrédients trouvé)";
        else {
            if (nombreStock.get() == 0)
                disponibilite = disponibilite + "   (RUPTURE DE STOCK)";
            else {
                if (nombreStock.get() < 30)
                    disponibilite = disponibilite + "   (" + getNombreStock() + ")";
                else
                    disponibilite = disponibilite + "   (30+)";
            }
        }
        return nom.get()+disponibilite;
    }

    public void updateNombreStock(){}
}
