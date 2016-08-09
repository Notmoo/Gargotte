package model;


public class Vente {

    private final Produit produit;
    private int nombre;
    private final String paymentMethod;
    private final double prixTotal;

    //Constructeurs
    public Vente(Produit prod, int nombre, String paymentMethod){
        this.produit = prod;
        this.nombre = nombre;
        this.paymentMethod = paymentMethod;
        if(paymentMethod.equals("gratuit (invité)") || paymentMethod.equals("gratuit (staff)")){
            prixTotal=0;
        }else{
            prixTotal = produit.getPrixDouble()*nombre;
        }
    }

    //Accesseurs
    public int getNb(){
        return nombre;
    }
    public Produit getProduit(){
        return produit;
    }

    //Autres méthodes
    public void increaseNb(){nombre++;}
    //Renvoie une string contenant les infos de la vente, qui sera sauvegardée dans les logs
    public String toString(){
        return (produit.getNom()+"\t"+nombre+"\t"+paymentMethod+"\t"+(new Double(prixTotal).toString().format("%.2f",prixTotal)) + " €");
    }

}
