package model;

import java.io.*;



public class Model {


    //Specify the filepath in these strings
    private static final String logFilePath = "records.log";
    private static File logFile; //This logFile have to be a txt file, it will store the sale's logs
    private ModeleDAO dao;

    private static final boolean DEBUG = true; // true to display debugging syso lines, false otherwise



    private final String adminPassword;

    //Constructeurs
    public Model() {
        adminPassword = "licorne"; // Admin password, required for stock update via software

        logFile = new File(logFilePath);

        dao=new ModeleDAO();


    }

    //Accesseurs
    public String getAdminPassword() {
        return adminPassword;
    }

    //Ajoute une vente dans bufferVentes, ou augmente le nombre de produit vendu si le produit est déjà dans la liste
    public void addVente(Vente newVente) {
        dao.addVente(newVente);
        saveLog(new LogLine(newVente));
    }

    //Sauvegarde chaque vente de bufferVente dans le fichie XML par appel successif de venteMenu(...) et ventePlat(...)
    public void saveVentes() {

        dao.saveVentes();
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
        return dao.isStockOK();
    }


    public ModeleDAO getDao() {
        return dao;
    }
}
