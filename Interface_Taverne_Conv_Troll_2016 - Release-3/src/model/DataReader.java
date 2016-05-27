package model;

import java.io.*;
import java.util.Properties;


public class DataReader {

    private final String author = "Cess";
    private final String name = "Gargotte - Convention du Troll Penche";
    private final String date = "2015/2016";
    /*
        fileName : nom du fichier qui va être chargé
        successfulLoad : booleen, vaut 'true' si le chargement du fichier est arrivé à son terme, et 'false' sinon

        DEBUG : booleen, par défaut à 'false', à mettre sur 'true' pour afficher des valeurs utiles au debugging dans la console.
     */
    private String fileName;

    private boolean successfulLoad;
    private boolean successfulSave;

    private int numberSales;
    private int numberGifts;
    private double amountSales;
    private double amountGifts;

    private boolean DEBUG = true;

    /*
        Constructeur : nom du fichier cible en paramètre
     */
    public DataReader(){
        fileName = null;
        successfulSave = false;
        successfulLoad = false;
    }

    public DataReader(String fileName){
        this.fileName=fileName;
        successfulLoad = loadData();
        successfulSave=false;
    }

    /*
        Méthode loadData() : parcours le fichier pour lire les deux séparateur, la liste des uv ainsi que la matrice d'adjacence.
     */
    public boolean loadData(){
        Properties properties = new Properties();
        InputStream input;


        try {
            // on lie le fichier et on récupère les properties
            input = new FileInputStream(fileName);
            properties.load(input);


            numberGifts = Integer.parseInt(properties.getProperty("NombreTotalOffert"));
            numberSales = Integer.parseInt(properties.getProperty("NombreTotalVente"));
            amountGifts = Double.parseDouble(properties.getProperty("MontantTotalPertes"));
            amountSales = Double.parseDouble(properties.getProperty("MontantTotalBenef"));


        }
        catch(IOException e){
            e.printStackTrace();
            return false;
        }


        return true;
    }

    public boolean saveData(){

        /*
            Déclaration des champs sauvegardés dans le fichier
         */
        Properties properties = new Properties();
        properties.setProperty("MontantTotalBenef", new Double(amountSales).toString());
        properties.setProperty("MontantTotalPertes", new Double(amountGifts).toString());
        properties.setProperty("NombreTotalVente", new Integer(numberSales).toString());
        properties.setProperty("NombreTotalOffert", new Integer(numberGifts).toString());


        OutputStream output = null;
        File file = new File(fileName);

        try {
            output = new FileOutputStream(file);
            properties.store(output, getSignature());
            output.flush();
            successfulSave = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            successfulSave = false;
        } catch (IOException e) {
            e.printStackTrace();
            successfulSave = false;
        }

        try {
            if (output != null) {
                output.close();
                successfulSave = true;
            }
        } catch (IOException exception) {
            exception.printStackTrace();
            successfulSave = false;
        }

        return successfulSave;
    }

    /*
        La méthode generateNewFile permet de ... attention gros spoil ... générer une nouvelle fois le fichier!! #NoShitSherlock
        Les valeurs des properties sont mises à zéro
     */
    public void generateNewFile(String fileName)
    {
        Properties properties = new Properties();
        properties.setProperty("MontantTotalBenef", "0");
        properties.setProperty("MontantTotalPertes", "0");
        properties.setProperty("NombreTotalVente", "0");
        properties.setProperty("NombreTotalOffert", "0");

        OutputStream output = null;
        File file = new File(fileName);

        try {
            output = new FileOutputStream(file);
            properties.store(output, getSignature());
            output.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            if (output != null) {
                output.close();
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }

    }


    //Getters and Setters
    public void setNumberSales(int numberSales) {
        this.numberSales = numberSales;
    }
    public void setNumberGifts(int numberGifts) {
        this.numberGifts = numberGifts;
    }
    public void setAmountSales(double amountSales) {
        this.amountSales = amountSales;
    }
    public void setAmountGifts(double amountGifts) {
        this.amountGifts = amountGifts;
    }
    public int getNumberSales() {
        return numberSales;
    }
    public int getNumberGifts() {
        return numberGifts;
    }
    public double getAmountSales() {
        return amountSales;
    }
    public double getAmountGifts() {
        return amountGifts;
    }
    public boolean hasSuccessfullyLoaded() {
        return successfulLoad;
    }
    private String getSignature(){
        return (name+" - "+author+" - "+date);
    }
}
