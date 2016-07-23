package model;

import items.Produit;
import items.Vendable;
import items.Vente;
import org.jdom2.DataConversionException;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * Created by kwidz on 27/05/16.
 */
public class ModeleDAO {

    private static final boolean DEBUG = true; // true to display debugging syso lines, false otherwise

    private static final String dbFile = "taverne.xml"; // This dbFile have to be an XML file, it stores the database
    private static org.jdom2.Document document; //required to read the specified XML dbFile
    private static Element racine;  //contains the root of the specified XML dbFile

    private final ArrayList<items.Vente> bufferVentes;
    private final ArrayList<items.Vendable> dbProduits;


    public ModeleDAO() {
        bufferVentes = new ArrayList<>();
        dbProduits = new ArrayList<>();
        try {
            loadVentes();
            initDBProduits();
        } catch (Exception e) {
            if (DEBUG) {
                System.err.println("Error while loading Data from " + dbFile + " in Model's default constructor");
            }
        }
    }
    public ArrayList<items.Vendable> getDBProduits() {
        return dbProduits;
    }
    public ArrayList<Vendable> getDBProduitsWithoutMenu() {
        ArrayList<Vendable> array = new ArrayList<>();
        for(Vendable current : dbProduits){
            if(Produit.class.isInstance(current)){
                array.add(current);
            }
        }

        return array;
    }
    //Ajoute une vente dans bufferVentes, ou augmente le nombre de produit vendu si le produit est déjà dans la liste
    public void addVente(items.Vente newVente) {
        if (bufferVentes.size() != 0) {
            boolean found = false;

            for (items.Vente elem : bufferVentes) {
                if (elem.getProduit().equals(newVente.getProduit())) {
                    elem.increaseNb();
                    found = true;
                    if (DEBUG) {
                        System.out.println("Increased " + elem.getProduit().getNom() + " number from " + (elem.getNb() - 1) + " to " + elem.getNb());
                    }
                }
            }

            if (!found) {
                bufferVentes.add(newVente);
                if (DEBUG) {
                    System.out.println("Added " + newVente.getProduit().getNom() + " in bufferVente");
                }
            }
        } else {
            bufferVentes.add(newVente);
            if (DEBUG) {
                System.out.println("Added " + newVente.getProduit().getNom() + " in bufferVente");
            }
        }
    }

    //Sauvegarde chaque vente de bufferVente dans le fichie XML par appel successif de venteMenu(...) et ventePlat(...)
    public void saveVentes() {

        for (items.Vente temp : bufferVentes) {
            if(items.Menu.class.isInstance(temp.getProduit())){
                venteMenu(temp);
            }
            if(items.Produit.class.isInstance(temp.getProduit())){
                ventePlat(temp);
            }
        }

        try {
            XMLOutputter sortie = new XMLOutputter(Format.getPrettyFormat());
            sortie.output(document, new FileOutputStream(dbFile));
        } catch (IOException e) {
            System.err.println("Error while saving data in " + dbFile);
        }
        bufferVentes.clear();

    }

    private void venteMenu(items.Vente newVente) {

        List listMenu = racine.getChild("menus").getChildren("menu");
        Iterator itMenu = listMenu.iterator();
        boolean found = false;
        String nomMenu = newVente.getProduit().getNom();
        int nombre = newVente.getNb();

        //Comme le produit est un menu, on met à jour son nombre dispo
        newVente.getProduit().updateNombreStock();

        //Pour chaque élément de la liste et tant que l'on n'a pas trouvé le menu recherché
        while (itMenu.hasNext() && !found) {
            try {

                //On déclare un nouvel élément pour pouvoir lui appliquer les méthodes que l'on veut (getChild, ...)
                Element current = (Element) itMenu.next();

                //Si ce menu a le même nom que celui que l'on cherche
                if (current.getChild("nom").getText().equals(nomMenu)) {
                    //On incrémente l'attribut "vente", on décrémente le stock et on signifie la trouvaille
                    current.setAttribute("vente", Integer.toString(current.getAttribute("vente").getIntValue() + nombre));
                    current.setAttribute("stock", Integer.toString(Integer.parseInt(newVente.getProduit().getNombreStock()) - nombre));
                    for(items.Produit currentComposant : ((items.Menu) newVente.getProduit()).getComposants()){
                        removePlat(currentComposant, nombre);
                    }
                    if(DEBUG){System.out.println("Successfully updated number of ingredients of menu named "+newVente.getProduit().getNom());}
                    found = true;
                    if (DEBUG) {
                        System.out.println("vente found in XML DB named " + dbFile);
                    }
                }
            } catch (DataConversionException e) {
                System.err.println("Erreur dans la sauvegarde de la vente : menu");
            }
        }
    }

    private void removePlat(items.Produit plat, int nombre){
        boolean found = false;
        String nomPlat = plat.getNom();

        List listProduit = racine.getChild("types").getChildren("type");
        Iterator itProduit = listProduit.iterator();
        if (DEBUG) {
            System.out.println("listProduit done");
        }

        //pour chaque élément de la liste
        while (itProduit.hasNext() && !found) {
            //On place l'élément courant dans current, et on sort son prix et ses ventes effectuées
            Element currentProd = (Element) itProduit.next();

            List listPlat = currentProd.getChild("contenu").getChildren("plat");
            Iterator itPlat = listPlat.iterator();
            if (DEBUG) {
                System.out.println("listPlat done");
            }
            while (itPlat.hasNext() && !found) {
                try {

                    //On déclare un nouvel élément pour pouvoir lui appliquer les méthodes que l'on veut (getChild, ...)
                    Element current = (Element) itPlat.next();

                    //Si ce plat a le même nom que celui que l'on cherche
                    if (current.getText().equals(nomPlat)) {
                        //On décrémente l'attribut "stock" et on signifie la trouvaille
                        current.setAttribute("stock", Integer.toString(current.getAttribute("stock").getIntValue() - nombre));
                        found = true;
                        if (DEBUG) {
                            System.out.println("Product found in XML DB named" + dbFile + "\n---->succesfully removed " + nombre + " element in " + nomPlat);
                        }
                    }
                } catch (DataConversionException e) {
                    System.err.println("Erreur dans la sauvegarde de la vente : menu \n ------>erreur de délétion de composant de menu (DataConversionException)");
                }
            }
        }


    }

    private void ventePlat(items.Vente newVente) {

        boolean found = false;
        String nomPlat = newVente.getProduit().getNom();
        int nombre = newVente.getNb();

        List listProduit = racine.getChild("types").getChildren("type");
        Iterator itProduit = listProduit.iterator();
        if (DEBUG) {
            System.out.println("listProduit done");
        }

        //pour chaque élément de la liste
        while (itProduit.hasNext() && !found) {
            //On place l'élément courant dans current, et on sort son prix et ses ventes effectuées
            Element currentProd = (Element) itProduit.next();

            List listPlat = currentProd.getChild("contenu").getChildren("plat");
            Iterator itPlat = listPlat.iterator();
            if (DEBUG) {
                System.out.println("listPlat done");
            }
            while (itPlat.hasNext() && !found) {
                try {

                    //On déclare un nouvel élément pour pouvoir lui appliquer les méthodes que l'on veut (getChild, ...)
                    Element current = (Element) itPlat.next();

                    //Si ce plat a le même nom que celui que l'on cherche
                    if (current.getText().equals(nomPlat)) {
                        //On incrémente l'attribut "vente" et on signifie la trouvaille
                        current.setAttribute("vente", Integer.toString(current.getAttribute("vente").getIntValue() + nombre));
                        current.setAttribute("stock", Integer.toString(current.getAttribute("stock").getIntValue() - nombre));
                        found = true;
                        if (DEBUG) {
                            System.out.println("vente found in XML DB named" + dbFile);
                        }
                    }
                } catch (DataConversionException e) {
                    System.err.println("Erreur dans la sauvegarde de la vente : plat");
                }
            }
        }
    }

    //(Ré)Initialise la base de données des produits en la (re)chargeant depuis le fichier XML
    public void initDBProduits() {

        /*
        On vide la liste car cette méthode peut être appelée dans deux cas :
            - lancement du programme
            - actualisation de l'affichage du stock
        Dans le deuxième cas, on veux effacer et re-lire les données depuis la BDD
        */
        dbProduits.clear();

        //-------------------------------------------------Plats à l'unité-------------------------------------------------------------

        /*
            On créé une liste des balises type présentes dans le fichier xml, ainsi qu'un itérateur pour parcourir la liste en question.
         */
        List listProduit = racine.getChild("types").getChildren("type");
        Iterator itProduit = listProduit.iterator();
        if (DEBUG) {
            System.out.println("listProduit done");
        }

        //pour chaque élément de la liste
        while (itProduit.hasNext()) {
            try {
                //On place l'élément courant dans current, et on sort son prix et ses ventes effectuées
                Element currentProd = (Element) itProduit.next();

                /*
                    On créé une liste des plats de cette catégorie, avec un itérateur, et on la parcours pour afficher le nom,
                    qui l'a fait, le prix et le nombre vendu actuellement.
                 */
                List listPlat = currentProd.getChild("contenu").getChildren("plat");
                Iterator itPlat = listPlat.iterator();
                if (DEBUG) {
                    System.out.println("listPlat done");
                }
                while (itPlat.hasNext()) {
                    Element currentPlat = (Element) itPlat.next();
                    double prix = currentPlat.getAttribute("prix").getDoubleValue();
                    int stock = currentPlat.getAttribute("stock").getIntValue();
                    int vente = currentPlat.getAttribute("vente").getIntValue();
                    items.Produit thisPlat = new items.Produit(currentPlat.getText(), prix, currentProd.getChild("categorie").getText(), stock, vente);
                    dbProduits.add(thisPlat);

                    if (DEBUG) {
                        System.out.println("\t" + thisPlat.toString());
                    }
                }

            }
            // On catch les erreurs de conversion, qui peuvent être throw quand on récupère le prix et le nombre de ventes
            catch (DataConversionException e) {
                System.err.println("Erreur de conversion de données dans le while type de displayVentes()");
            }
        }

        if (DEBUG) {
            System.out.println("Display Product done");
        }

        /*
            On créé une liste des balises menu présentes dans le fichier XML, ainsi qu'un itérateur pour parcourir la liste en question.
         */
        //-------------------------------------------------Menus-----------------------------------------------------------------------
        List listMenu = racine.getChild("menus").getChildren("menu");
        Iterator itMenu = listMenu.iterator();
        if (DEBUG) {
            System.out.println("listMenu done");
        }
        //pour chaque élément de la liste
        while (itMenu.hasNext()) {

            try {
                //On place l'élément courant dans la var 'current', et on sort son prix, la qté dispo, ses ventes effectuées et on instancie la classe Menu avec ces données
                Element current = (Element) itMenu.next();
                double prix = current.getAttribute("prix").getDoubleValue();
                int stock = current.getAttribute("stock").getIntValue();
                int vente = current.getAttribute("vente").getIntValue();
                items.Menu thisMenu = new items.Menu(current.getChildText("nom"), prix, "menu", stock, vente, new ArrayList<>());


                //    On récupère les composants du menu et, pour chaque composant, on le cherche dans la bdd déjà créée (à ce stade,
                //    tous les prod. individuels ont été lu), et on ajoute les compo dans la liste si on les a trouvé dans la bdd.
                //    Si on a trouvé le produit, on signifie la trouvaille.


                boolean found = false;

                for(Element currentIngredient : current.getChild("contenu").getChildren("plat")){
                    if(DEBUG){System.out.println("new ingredient in menu : "+currentIngredient.getText()+ "\n");}
                    for(items.Vendable produit : dbProduits){
                        if(DEBUG){System.out.println("checking : "+produit.getNom());}
                        if(items.Produit.class.isInstance(produit) && currentIngredient.getText().equals(produit.getNom())) {
                            if(DEBUG){System.out.println("match");}
                            thisMenu.getComposants().add((items.Produit) produit);
                            found = true;
                        }
                    }

                    if(DEBUG) {
                        System.out.println("Recap of menu's ingredient");
                        for(items.Produit currentIngr : thisMenu.getComposants()){
                            System.out.println(" - "+currentIngr.toString());
                        }
                    }
                    if(!found && DEBUG) {
                        System.err.println("Ingredient '" + currentIngredient.getText() + "' of menu '" + current.getChildText("nom") + "' not found in bdd");
                    }else {
                        if (DEBUG) {
                            System.out.print("Ingredient '" + currentIngredient.getText() + "' of menu '" + current.getChildText("nom") + "' found in bdd");
                        }
                    }
                }
                thisMenu.updateNombreStock();
                dbProduits.add(thisMenu);

                if (DEBUG) {
                    System.out.println("\t" + thisMenu.toString());
                }

            }
            // On catch les erreurs de conversion, qui peuvent être throw quand on récupère le prix et le nombre de ventes
            catch (DataConversionException e) {
                System.err.println("Erreur de conversion de données dans le while menu de initDBProduits()");
            }
        }
        if (DEBUG) {
            System.out.println("Display Menu done");
        }
    }
    //Modifie l'un des attributs de la balise XML associée au Produit passé en paramètre
    public void updateAttributeValue(items.Vendable prod, int newStockValue, String attribute) {

        boolean found = false;
        String nomProduit = prod.getNom();

        if (items.Menu.class.isInstance(prod)) {

            List listMenu = racine.getChild("menus").getChildren("menu");
            Iterator itMenu = listMenu.iterator();

            //Pour chaque élément de la liste et tant que l'on n'a pas trouvé le menu recherché
            while (itMenu.hasNext() && !found) {
                //On déclare un nouvel élément pour pouvoir lui appliquer les méthodes que l'on veut (getChild, ...)
                Element current = (Element) itMenu.next();

                //Si ce menu a le même nom que celui que l'on cherche
                if (current.getChild("nom").getText().equals(nomProduit)) {
                    //On modifie l'attribut et on signifie la trouvaille
                    current.setAttribute(attribute, Integer.toString(newStockValue));
                    found = true;
                    if (DEBUG) {
                        System.out.println("product found in XML DB named " + dbFile + " || " + attribute + " updated : " + newStockValue);
                    }
                }
            }

        } else {

            List listProduit = racine.getChild("types").getChildren("type");
            Iterator itProduit = listProduit.iterator();
            if (DEBUG) {
                System.out.println("listProduit done");
            }

            //pour chaque élément de la liste
            while (itProduit.hasNext() && !found) {
                //On place l'élément courant dans current, et on sort son prix et ses ventes effectuées
                Element currentProd = (Element) itProduit.next();

                List listPlat = currentProd.getChild("contenu").getChildren("plat");
                Iterator itPlat = listPlat.iterator();
                if (DEBUG) {
                    System.out.println("listPlat done");
                }
                while (itPlat.hasNext() && !found) {
                    //On déclare un nouvel élément pour pouvoir lui appliquer les méthodes que l'on veut (getChild, ...)
                    Element current = (Element) itPlat.next();

                    //Si ce plat a le même nom que celui que l'on cherche
                    if (current.getText().equals(nomProduit)) {
                        //On incrémente l'attribut "vente" et on signifie la trouvaille
                        current.setAttribute(attribute, Integer.toString(newStockValue));
                        found = true;
                        if (DEBUG) {
                            System.out.println("product found in XML DB named " + dbFile + " || " + attribute + " updated : " + newStockValue);
                        }
                    }
                }
            }

        }

        if (found) {
            try {
                XMLOutputter sortie = new XMLOutputter(Format.getPrettyFormat());
                sortie.output(document, new FileOutputStream(dbFile));
                if (DEBUG) {
                    System.out.println("Data saved in dbFile");
                }
            } catch (Exception e) {
                System.err.println("Error while saving in dbFile");
            }
        }

    }

    //Charge les données depuis le fichier XML et stocke la balise root dans la variable 'racine'
    private static void loadVentes() throws JDOMException, IOException {

        SAXBuilder sxb = new SAXBuilder();
        document = sxb.build(new File(dbFile));
        racine = document.getRootElement();
        if (DEBUG) {
            System.out.println("loading done");
        }
    }

    public boolean isStockOK() {
        boolean stockOK = true;

        ArrayList<items.Vente> list = new ArrayList<>();

        for(items.Vente currentVente : bufferVentes){
            currentVente.getProduit().updateNombreStock();
            if(currentVente.getProduit().getClass().equals(items.Menu.class)){
                if(DEBUG) System.out.println("-----------Menu Found------------");
                items.Menu currentMenu = (items.Menu) currentVente.getProduit();
                for(items.Produit currentComposant : currentMenu.getComposants()) {
                    if (list.size() <= 0) {
                        list.add(new items.Vente(currentComposant, currentVente.getNb(), currentVente.getPaymentMethod()));
                    } else {
                        boolean found = false;
                        for (items.Vente currentListItem : list) {
                            if (currentComposant.equals(currentListItem.getProduit())) {
                                currentListItem.setNb(currentListItem.getNb() + currentVente.getNb());
                                found = true;
                            }//end if products match

                            if (found) break;
                        }//End for each item in list
                        if (!found)
                            list.add(new items.Vente(currentComposant, currentVente.getNb(), currentVente.getPaymentMethod()));
                    }//End if-else list is empty
                }//End for each composant of currentMenu
            }else{
                if(list.size()<=0){
                    list.add(currentVente);
                }else{
                    boolean found = false;
                    for (items.Vente currentListItem : list) {
                        if(currentVente.getProduit().equals(currentListItem.getProduit())){
                            currentListItem.addNb(currentVente.getNb());
                            found = true;
                        }//end IF

                        if(found) break;
                    }
                    if(!found) list.add(currentVente);
                }//End if-else "size is zero"
            }//End if-else "Vendable is a menu"
        }//end for currentVente in bufferVente

        if(DEBUG){
            System.out.println("<### BufferVente : recap start ###>");
            for(Vente currentVente : list){
                System.out.println("\t - "+currentVente.toString());
            }
            System.out.println("<### BufferVente : recap end   ###>");
        }

        for(items.Vente currentVente : list){
            if(currentVente.getNb() > Integer.parseInt(dbProduits.get(dbProduits.indexOf(currentVente.getProduit())).getNombreStock())){
                stockOK = false;
            }
        }
        if(DEBUG){
            if(stockOK) System.out.println("<### STOCK OK ###>");
            else  System.out.println("<### STOCK KO ###>");
        }
        return stockOK;
    }

    //Efface le contenu de bufferVente
    public void clearVentes(){
        bufferVentes.clear();
    }
}
