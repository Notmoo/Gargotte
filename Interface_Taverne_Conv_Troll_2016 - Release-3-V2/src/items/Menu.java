package items;

import java.util.ArrayList;

/**
 * Created by Guillaume on 29/04/2016.
 */
public class Menu extends Vendable {

    private ArrayList<Produit> produits;

    public Menu(String name, double prix, String cat, int nombreStock, int nombreVendu){
        super(name, prix, cat, nombreStock, nombreVendu);
        produits = new ArrayList<>();
    }

    public Menu(String name, double prix, String cat, int nombreStock, int nombreVendu, ArrayList<Produit> list){
        super(name, prix, cat, nombreStock, nombreVendu);
        produits = list;
    }

    public void updateNombreStock(){
        int minNombreStock = -1;

        for(Produit current : produits){
            if(minNombreStock==-1){
                minNombreStock = Integer.parseInt(current.getNombreStock());
            }else{
                minNombreStock = (minNombreStock>Integer.parseInt(current.getNombreStock())? Integer.parseInt(current.getNombreStock()) : minNombreStock);
            }
        }

        this.setNombreStock(minNombreStock);
    }

    public boolean testStock(){
        updateNombreStock();
        if(nombreStock.get()<=0){
            return false;
        }else{
            return true;
        }
    }

    public ArrayList<Produit> getComposants(){return produits;}
}
