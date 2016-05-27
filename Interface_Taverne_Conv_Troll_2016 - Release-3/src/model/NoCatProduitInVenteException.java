package model;


public class NoCatProduitInVenteException extends Exception{
    public NoCatProduitInVenteException(){
        super("This product has no categorie -> sale aborted");
    }
}
