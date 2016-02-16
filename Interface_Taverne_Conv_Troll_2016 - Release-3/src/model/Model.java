package model;

import java.io.*;
import org.jdom2.*;
import org.jdom2.input.*;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;



public class Model {


    //Specify the filepath in these strings
    private static final String dbFile = "taverne.xml"; // This dbFile have to be an XML file, it stores the database
    private static final String logFilePath = "records.log";
    private static final String statFilePath = "licorne.stat";
    private static File logFile; //This logFile have to be a txt file, it will store the sale's logs

    private static org.jdom2.Document document; //required to read the specified XML dbFile
    private static Element racine;  //contains the root of the specified XML dbFile

    private DataReader dr; //DataReader instance which will load and save stats about sales into the file specified in parameters

    private static final boolean DEBUG = true; // true to display debugging syso lines, false otherwise

    private final ArrayList<Vente> bufferVentes;
    private final ArrayList<Produit> dbProduits;

    private int numberSales;
    private double amountSales;
    private int numberGifts;
    private double amountGifts;

    private final String adminPassword;

    //Constructeurs
    public Model() {
        adminPassword = "licorne"; // Admin password, required for stock update via software

        logFile = new File(logFilePath);

        bufferVentes = new ArrayList<>();
        dbProduits = new ArrayList<>();

        try {
            loadVentes();
            initDBProduits();
            loadStats();
        } catch (Exception e) {
            if (DEBUG) {
                System.err.println("Error while loading Data from " + dbFile + " in Model's default constructor");
            }
        }


    }

    //Accesseurs

    public int getNumberSales() {
        return numberSales;
    }
    public double getAmountSales() {
        return amountSales;
    }
    public int getNumberGifts() {
        return numberGifts;
    }
    public double getAmountGifts() {
        return amountGifts;
    }
    public ArrayList<Produit> getDBProduits() {
        return dbProduits;
    }
    public String getAdminPassword() {
        return adminPassword;
    }

    //Autres méthodes

    //Ajoute une vente dans bufferVentes, ou augmente le nombre de produit vendu si le produit est déjà dans la liste
    public void addVente(Vente newVente) {
        if (bufferVentes.size() != 0) {
            boolean found = false;

            for (Vente elem : bufferVentes) {
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
    //Charge les données depuis le fichier XML et stocke la balise root dans la variable 'racine'
    private static void loadVentes() throws JDOMException, IOException {

        SAXBuilder sxb = new SAXBuilder();
        document = sxb.build(new File(Model.dbFile));
        racine = document.getRootElement();
        if (DEBUG) {
            System.out.println("loading done");
        }
    }
    //Sauvegarde chaque vente de bufferVente dans le fichie XML par appel successif de venteMenu(...) et ventePlat(...)
    public void saveVentes() {

        for (Vente temp : bufferVentes) {
            switch (temp.getProduit().getCatNom()) {
                case "menu":
                    venteMenu(temp);
                    break;
                default:
                    ventePlat(temp);
                    break;
            }
        }

        try {
            XMLOutputter sortie = new XMLOutputter(Format.getPrettyFormat());
            sortie.output(document, new FileOutputStream(Model.dbFile));
        } catch (IOException e) {
            System.err.println("Error while saving data in " + dbFile);
        }
        bufferVentes.clear();

    }
    private void venteMenu(Vente newVente) {

        List listMenu = racine.getChild("menus").getChildren("menu");
        Iterator itMenu = listMenu.iterator();
        boolean found = false;
        String nomMenu = newVente.getProduit().getNom();
        int nombre = newVente.getNb();

        //Pour chaque élément de la liste et tant que l'on n'a pas trouvé le menu recherché
        while (itMenu.hasNext() && !found) {
            try {

                //On déclare un nouvel élément pour pouvoir lui appliquer les méthodes que l'on veut (getChild, ...)
                Element current = (Element) itMenu.next();

                //Si ce menu a le même nom que celui que l'on cherche
                if (current.getChild("nom").getText().equals(nomMenu)) {
                    //On incrémente l'attribut "vente" et on signifie la trouvaille
                    current.setAttribute("vente", Integer.toString(current.getAttribute("vente").getIntValue() + nombre));
                    current.setAttribute("stock", Integer.toString(current.getAttribute("stock").getIntValue() - nombre));
                    found = true;
                    if (DEBUG) {
                        System.out.println("vente found in XML DB named " + Model.dbFile);
                    }
                    saveLog(new LogLine(newVente));
                    saveStats(newVente);
                }
            } catch (DataConversionException e) {
                System.err.println("Erreur dans la sauvegarde de la vente : menu");
            }
        }
    }
    private void ventePlat(Vente newVente) {

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
                            System.out.println("vente found in XML DB named" + Model.dbFile);
                        }
                        saveLog(new LogLine(newVente));
                        saveStats(newVente);
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
                //On place l'élément courant dans current, et on sort son prix et ses ventes effectuées
                Element current = (Element) itMenu.next();
                double prix = current.getAttribute("prix").getDoubleValue();
                int stock = current.getAttribute("stock").getIntValue();
                int vente = current.getAttribute("vente").getIntValue();
                Produit thisMenu = new Produit(current.getChildText("nom"), prix, "menu", stock, vente);
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
                    Produit thisPlat = new Produit(currentPlat.getText(), prix, currentProd.getChild("categorie").getText(), stock, vente);
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
    }
    //Modifie l'un des attributs de la balise XML associée au Produit passé en paramètre
    public void updateAttributeValue(Produit prod, int newStockValue, String attribute) {

        boolean found = false;
        String nomProduit = prod.getNom();

        if (prod.getCatNom().equals("menu")) {

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
                        System.out.println("product found in XML DB named " + Model.dbFile + " || " + attribute + " updated : " + newStockValue);
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
                            System.out.println("product found in XML DB named " + Model.dbFile + " || " + attribute + " updated : " + newStockValue);
                        }
                    }
                }
            }

        }

        if (found) {
            try {
                XMLOutputter sortie = new XMLOutputter(Format.getPrettyFormat());
                sortie.output(document, new FileOutputStream(Model.dbFile));
                if (DEBUG) {
                    System.out.println("Data saved in dbFile");
                }
            } catch (Exception e) {
                System.err.println("Error while saving in dbFile");
            }
        }

    }
    //Ajoute une ligne dans le fichier des logs de ventes
    private void saveLog(LogLine ll) {
        try {
            //Opening a FileWriter instance, which will handle the log writing
            final FileWriter writer = new FileWriter(logFile, true);

            try {
                //Each LogLine is writen in the logFile
                writer.write(ll.toString() + System.getProperty("line.separator"));
            } finally { //Closing the FileWriter instance, no matter what
                writer.close();
            }
        } catch (IOException e) {
            System.err.println("Error while saving logs in " + logFilePath);
        }

    }
    //Renvoie true si le stock contient tous les produits présent dans bufferVentes dans les quantités demandées, false sinon
    public boolean isStockOK() {
        boolean stockOK = true;

        for(Vente currentVente : bufferVentes){
            if(currentVente.getNb() > Integer.parseInt(dbProduits.get(dbProduits.indexOf(currentVente.getProduit())).getNombreStock())){
                stockOK = false;
            }
        }
        return stockOK;
    }
    //Efface le contenu de bufferVente
    public void clearVentes(){
        bufferVentes.clear();
    }
    //(Re)Charge les statistiques contenues dans le fichier .stat, et stoque les valeurs dans des attributs de Model
    private void loadStats(){
        dr = new DataReader(statFilePath);
        numberGifts = dr.getNumberGifts();
        amountGifts = dr.getAmountGifts();
        numberSales = dr.getNumberSales();
        amountSales = dr.getAmountSales();
        System.out.println(numberGifts+"  "+numberSales+"  "+amountGifts+"  "+amountSales);
    }
    //Sauvegarde les statistiques dans le fichier .stat
    private void saveStats(Vente newVente){
        if(dr!=null){
            if(newVente.getPrixTotal()==0){
                numberGifts += newVente.getNb();
                amountGifts += newVente.getNb()*newVente.getProduit().getPrixDouble();

                dr.setAmountGifts(amountGifts);
                dr.setNumberGifts(numberGifts);
            }else{
                numberSales += newVente.getNb();
                amountSales += newVente.getPrixTotal();

                dr.setAmountSales(amountSales);
                dr.setNumberSales(numberSales);
            }

            dr.saveData();
        }else{
            dr.loadData();
            saveStats(newVente);
            System.err.println("Previous DataReader was null --> It is now loaded");
        }
    }

    // DEBUGGING METHODS
    public void displayFileContent() {

        /*
            On créé une liste des balises menu présentes dans le fichier XML, ainsi qu'un itérateur pour parcourir la liste en question.
         */
        System.out.println("-------------------------------------------------Menus-----------------------------------------------------------------------");
        System.out.println("");
        List listMenu = racine.getChild("menus").getChildren("menu");
        Iterator itMenu = listMenu.iterator();
        if (DEBUG) {
            System.out.println("listMenu done");
        }
        //pour chaque élément de la liste
        while (itMenu.hasNext()) {

            try {
                //On place l'élément courant dans current, et on sort son prix et ses ventes effectuées
                Element current = (Element) itMenu.next();
                int nbVentes = current.getAttribute("vente").getIntValue();
                double prix = current.getAttribute("prix").getDoubleValue();

                //On affiche le tout
                System.out.println(current.getChild("nom").getText());
                System.out.println("  Ventes : " + nbVentes + " || Prix : " + prix + " €");

            }
            // On catch les erreurs de conversion, qui peuvent être throw quand on récupère le prix et le nombre de ventes
            catch (DataConversionException e) {
                System.err.println("Erreur de conversion de données dans le while menu de displayVentes()");
            }
        }
        if (DEBUG) {
            System.out.println("DisplayVentes done");
        }

        System.out.println("");
        System.out.println("-------------------------------------------------Plats à l'unité-------------------------------------------------------------");
        System.out.println("");
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

                //On affiche le nom du produit, càd de la catégorie(galettes bretonnes, boissons, etc...)
                System.out.println("Catégorie : " + currentProd.getChild("nom").getText() + " || " + currentProd.getChild("conception").getText());

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
                    int nbVentes = currentPlat.getAttribute("vente").getIntValue();
                    double prix = currentPlat.getAttribute("prix").getDoubleValue();
                    System.out.println("\t\t* " + currentPlat.getText());
                    System.out.println("\t\t\tVentes : " + nbVentes + " || Prix : " + prix + " €");
                }

            }
            // On catch les erreurs de conversion, qui peuvent être throw quand on récupère le prix et le nombre de ventes
            catch (DataConversionException e) {
                System.err.println("Erreur de conversion de données dans le while type de displayVentes()");
            }
        }


    }
    public void displayBufferVente() {
        for (Vente temp : bufferVentes) {
            System.out.println(temp.getProduit().getNom());
            System.out.println("prix : " + temp.getProduit().getPrix() + " || nombre : " + temp.getNb());
        }
    }
}
