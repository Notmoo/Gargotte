package items;


public class Vente {

    private final Vendable produit;
    private int nombre;
    private final String paymentMethod;
    private final double prixTotal;

    //Constructeurs
    public Vente(Vendable prod, int nombre, String paymentMethod){
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
    public void addNb(int nb) { this.nombre += nb; }
    public void setNb(int nb) { this.nombre = nb; }
    public Vendable getProduit(){
        return produit;
    }
    public String getPaymentMethod() { return paymentMethod; }

    //Autres méthodes
    public void increaseNb(){nombre++;}
    //Renvoie une string contenant les infos de la vente, qui sera sauvegardée dans les logs
    public String toString(){
        return (produit.getNom()+"\t"+nombre+"\t"+paymentMethod+"\t"+(new Double(prixTotal).toString().format("%.2f",prixTotal)) + " €");
    }

}
