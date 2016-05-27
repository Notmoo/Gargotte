package sample;


import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;


@SuppressWarnings("ALL")
public class Controller implements Initializable {

    private final ObservableList<Produit> listItemsMenu = FXCollections.observableArrayList();
    private final ObservableList<Produit> listItemsGalette = FXCollections.observableArrayList();
    private final ObservableList<Produit> listItemsMeal = FXCollections.observableArrayList();
    private final ObservableList<Produit> listItemsSnack = FXCollections.observableArrayList();
    private final ObservableList<Produit> listItemsDrink = FXCollections.observableArrayList();
    private final ObservableList<Produit> listItemsVentes = FXCollections.observableArrayList();
    private final ObservableList<Produit> listProductTableView = FXCollections.observableArrayList();
    private Model model;

    //-----------------------------------------------ONGLET "CAISSE"----------------------------------------------------
    //Récupération des différentes listes de produit
    @FXML
    private ListView<Produit> MenuListView;
    @FXML
    private ListView<Produit> GaletteListView;
    @FXML
    private ListView<Produit> MealListView;
    @FXML
    private ListView<Produit> SnackListView;
    @FXML
    private ListView<Produit> DrinkListView;

    //Récupération de la liste commande
    @FXML
    private ListView<Produit> BufferVenteListView;

    //Récupération du bouton de validation de la commande
    @FXML
    private Button ValidationButton;

    //Récupération des boutons de sélection du mode de paiement et du label les annonçant, et déclaration de la collection qui va les contenir
    @FXML
    private Label PaymentMethodSelectionLabel;
    private ArrayList<RadioButton> radioButtonList;
    @FXML
    private RadioButton LiquidePaymentMethodRadioButton;
    @FXML
    private RadioButton ChequePaymentMethodRadioButton;
    @FXML
    private RadioButton CompteAEPaymentMethodRadioButton;
    @FXML
    private RadioButton FreeGuestPaymentMethodRadioButton;
    @FXML
    private RadioButton FreeStaffPaymentMethodRadioButton;

    //Récupération du textField affichant le prix total de la commande
    @FXML
    private TextField PrixCommandeTextField;

    //Récupération des options contenues dans le menu
    @FXML
    private MenuItem MenuItemValidate;



    //-----------------------------------------------ONGLET "STOCK"-----------------------------------------------------
    //Récupoération du Tab contant le tableur
    @FXML
    private Tab TabPaneStock;

    //Récupération du tableur, ainsi que ses colonnes
    @FXML
    private TableView StockTableView;
    @FXML
    private TableColumn NomStockTableColumn;
    @FXML
    private TableColumn PrixStockTableColumn;
    @FXML
    private TableColumn StockNombreStockTableColumn;
    @FXML
    private TableColumn VenduNombreStockTableColumn;
    @FXML
    private TableColumn TypeStockTableColumn;



    //-----------------------------------------------ONGLET "MAJ STOCK"-------------------------------------------------
    //Récupération du Tab contenant le tableur
    @FXML
    private Tab TabPaneUpdateStock;

    //Récupération du tableur, ainsi que ses colonnes
    @FXML
    private TableView StockUpdateTableView;
    @FXML
    private TableColumn NomStockUpdateTableColumn;
    @FXML
    private TableColumn PrixStockUpdateTableColumn;
    @FXML
    private TableColumn StockNombreUpdateStockTableColumn;
    @FXML
    private TableColumn VenduNombreUpdateStockTableColumn;
    @FXML
    private TableColumn TypeStockUpdateTableColumn;

    //Récupération du champ de saisie du mot de passe, du bouton de validation du mot de passe
    //et du label affichant si le mot de passe est valide ou non
    @FXML
    private PasswordField PasswordField;
    @FXML
    private Button UnlockStockUpdateTableViewButton;
    @FXML
    private Label UnlockStockUpdateTableViewLabel;

    //-----------------------------------------------ONGLET "MAJ STOCK"-------------------------------------------------
    //Récupération du Tab contenant les données chiffrées
    @FXML
    private Tab TabPaneStat;

    //récupération des différents label affichant les données
    @FXML
    private Label TotalGainStatLabel; //Montant total des ventes
    @FXML
    private Label TotalNumberStatTabLabel; //Nombre total de ventes
    @FXML
    private Label TotalFreeNumberStatTabLabel; //Nombre total de produits offert (staff et invités)
    @FXML
    private Label TotalFreeGainStatTabLabel; //Montant total des repas offert


    //--------------------------------------------------------CODE------------------------------------------------------

    //Méthode initialize, obligatoire pour la classe controller et se lance lors de la création de la VBox dans Main.java
    public void initialize(URL url, ResourceBundle rb) {

        //On initialise tout le modèle, y compris la lecture de la base de donnée et la récupération des produits
        model = new Model();

        //on initialise l'affichage des listes de l'onglet "Caisse"
        updateListView();

        // Association entre les listes et les éléments de l'onglet "Caisse"
        BufferVenteListView.setItems(listItemsVentes);
        GaletteListView.setItems(listItemsGalette);
        MealListView.setItems(listItemsMeal);
        SnackListView.setItems(listItemsSnack);
        DrinkListView.setItems(listItemsDrink);
        MenuListView.setItems(listItemsMenu);

        //Association entre les RadioButton et la liste
        radioButtonList = new ArrayList<>();
        radioButtonList.add(LiquidePaymentMethodRadioButton);
        radioButtonList.add(ChequePaymentMethodRadioButton);
        radioButtonList.add(CompteAEPaymentMethodRadioButton);
        radioButtonList.add(FreeGuestPaymentMethodRadioButton);
        radioButtonList.add(FreeStaffPaymentMethodRadioButton);


        //Association entre les listes et les éléments de l'onglet "Stock"
        NomStockTableColumn.setCellValueFactory( new PropertyValueFactory<Produit,String>("nom") );
        PrixStockTableColumn.setCellValueFactory( new PropertyValueFactory<Produit,String>("prix") );
        StockNombreStockTableColumn.setCellValueFactory( new PropertyValueFactory<Produit,Integer>("nombreStock") );
        VenduNombreStockTableColumn.setCellValueFactory( new PropertyValueFactory<Produit,Integer>("nombreVendu") );
        TypeStockTableColumn.setCellValueFactory( new PropertyValueFactory<Produit,String>("catNom") );

        //Association entre le tableur de l'onglet "Stock" et la liste des produits
        StockTableView.setItems(listProductTableView);


        //Association entre les listes et les éléments de l'onglet "MAJ Stock"
        NomStockUpdateTableColumn.setCellValueFactory( new PropertyValueFactory<Produit,String>("nom") );
        TypeStockUpdateTableColumn.setCellValueFactory( new PropertyValueFactory<Produit,String>("catNom") );
        PrixStockUpdateTableColumn.setCellValueFactory( new PropertyValueFactory<Produit,Float>("prix") );

        //Mise en place de la fonctionnalité de modification sur la colonne "nombre vente" du tableur + association entre la liste et cette colonne
        VenduNombreUpdateStockTableColumn.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<Produit, String>,ObservableValue<String>>() {
                    public ObservableValue<String> call(CellDataFeatures<Produit, String> p) {
                        // p.getValue() returns the Produit instance for a particular TableView row
                        return new SimpleStringProperty(p.getValue().getNombreVendu());
                    }
                }
        );
        VenduNombreUpdateStockTableColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        //Mise en place de la fonctionnalité de modification sur la colonne "nombre stock" du tableur + association entre la liste et cette colonne
        StockNombreUpdateStockTableColumn.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<Produit, String>,ObservableValue<String>>() {
                    public ObservableValue<String> call(CellDataFeatures<Produit, String> p) {
                        // p.getValue() returns the Produit instance for a particular TableView row
                        return new SimpleStringProperty(p.getValue().getNombreStock());
                    }
                }
        );
        StockNombreUpdateStockTableColumn.setCellFactory(TextFieldTableCell.forTableColumn());


        //Mise en place d'un listener sur la modification des cellules de la colonne "nombre vente" du tableur
        VenduNombreUpdateStockTableColumn.setOnEditCommit(
                new EventHandler<CellEditEvent<Produit, String>>() {
                    @Override
                    public void handle(CellEditEvent<Produit, String> event) {
                        model.getDao().updateAttributeValue(((Produit) event.getTableView().getItems().get(event.getTablePosition().getRow())), Integer.parseInt(event.getNewValue()), "vente");
                    }
                }
        );
        //Mise en place d'un listener sur la modification des cellules de la colonne "nombre stock" du tableur
        StockNombreUpdateStockTableColumn.setOnEditCommit(
                new EventHandler<CellEditEvent<Produit, String>>() {
                    @Override
                    public void handle(CellEditEvent<Produit, String> event) {
                        model.getDao().updateAttributeValue(((Produit) event.getTableView().getItems().get(event.getTablePosition().getRow())),Integer.parseInt(event.getNewValue()),"stock");
                    }
                }
        );

        //Association entre le tableur de l'onglet "MAJ Stock" et la liste des produits
        StockUpdateTableView.setItems(listProductTableView);

        //On inscrit l'action effectuable sur le bouton de validation de commande
        ValidationButton.setText("Validation");

        //On inscrit l'action sur le bouton de débloquage et on verrouille la fenetre de modification du stock
        UnlockStockUpdateTableViewButton.setText("Unlock");
        StockUpdateTableView.setDisable(true);

        //On met en place les différents raccourci clavier
        MenuItemValidate.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));


    }


    // Met à jour de l'affichage des onglets "Caisse", "Stock" et "MAJ Stock"
    @FXML
    private void updateStockDisplay(){
        //On relit les données de la base de donnée
        model.getDao().initDBProduits();
        updateListView();

        //On recharge les données
        listProductTableView.clear();
        listProductTableView.addAll(model.getDao().getDBProduits());

        //On remet les paramètres par défaut concernant le verrouillage de l'onglet "MAJ Stock" :
        // verrouillé, le bouton affichant "Unlock", avec le champ de saisie du mot de passe vide
        StockUpdateTableView.setDisable(true);
        UnlockStockUpdateTableViewButton.setText("Unlock");
        UnlockStockUpdateTableViewLabel.setText("");
        PasswordField.setDisable(false);
        PasswordField.setPromptText("enter password here");

    }

    // Si le stock le permet, sauvegarde la sélection de produit dans la DB puis remet à zéro la sélection. Sinon, ne fait rien
    private boolean saveSoldItem(){
        System.out.println("Début Sauvegarde Commande");
        String paymentMethod = getPaymentMethodSelection();

        for(Produit elem : listItemsVentes){
            model.addVente(new Vente(elem, 1, paymentMethod));
            System.out.println("+ une vente!");
        }

        if(model.isStockOK()){
            model.saveVentes();
            listItemsVentes.clear();
            updateListView();

            System.out.println("Fin de Sauvegarde Commande : Succès");
            return true;
        }
        else{
            model.getDao().clearVentes();
            System.out.println("Fin de Sauvegarde Commande : Echec (stock insuffisant)");
            return false;
        }


    }

    //Met à jour l'affichage du montant total de la sélection
    private void updateTextField(){
        double total=0;
        if(listItemsVentes.size()!=0) {
            for (Produit tempProd : listItemsVentes) {
                total += tempProd.getPrixDouble();
            }
            PrixCommandeTextField.setText((new Double(total).toString().format("%.2f",total)) + " €");
        }
    }

    //Met à jour l'affichage des listes de menus et produits dans l'onglet "Caisse"
    private void updateListView(){
        listItemsMenu.clear();
        listItemsGalette.clear();
        listItemsMeal.clear();
        listItemsSnack.clear();
        listItemsDrink.clear();

        for(Produit elem : model.getDao().getDBProduits()){
            try {
                switch (elem.getCatNom()) {
                    case "menu":
                        listItemsMenu.add(elem);
                        break;
                    case "Galette":
                        listItemsGalette.add(elem);
                        break;
                    case "Plat":
                        listItemsMeal.add(elem);
                        break;
                    case "Boisson":
                        listItemsDrink.add(elem);
                        break;
                    case "Snack":
                        listItemsSnack.add(elem);
                        break;
                    default:
                        throw new NoCatProduitInVenteException();
                }
            }catch(NoCatProduitInVenteException e){};
        }
    }

    //Vérifie que le mot de passe correspond et met à jour l'affichage en fonction de la situation
    @FXML
    private void handleUnlockButton(ActionEvent e){
        System.out.println("it works! ---> "+PasswordField.getText()+" / "+model.getAdminPassword());

        if(UnlockStockUpdateTableViewButton.getText()=="Unlock" && PasswordField.getText().equals(model.getAdminPassword())){
            StockUpdateTableView.setDisable(false);
            UnlockStockUpdateTableViewButton.setText("Lock");
            UnlockStockUpdateTableViewLabel.setText("ACCESS GRANTED");
            UnlockStockUpdateTableViewLabel.setTextFill(Color.rgb(21,117,84));
            PasswordField.setDisable(true);
        }else{
            if(UnlockStockUpdateTableViewButton.getText()=="Lock"){
                StockUpdateTableView.setDisable(true);
                UnlockStockUpdateTableViewButton.setText("Unlock");
                UnlockStockUpdateTableViewLabel.setText("");
                PasswordField.setDisable(false);
            }else{
                UnlockStockUpdateTableViewLabel.setText("ACCESS DENIED");
                UnlockStockUpdateTableViewLabel.setTextFill(Color.rgb(210,39,30));
            }
        }
        PasswordField.clear();
    }

    //Valide la commande
    private void validation(){
        //Remet le texte du label annonçant la sélection du type de paiement en noir
        PaymentMethodSelectionLabel.setTextFill(Color.BLACK);

        //enregistre la commande
        if(saveSoldItem()) {
            //Met a jour l'affichage du stock dans les onglets "Stock" et "MAJ Stock"
            updateStockDisplay();
            //Met a jour l'affichage des listes de produits dans l'onglet "Caisse"
            updateListView();
            //Remet à zéro la selection du type de paiement
            clearPaymentMethodSelection();
            //Met à jour l'affichage du prix total
            updateTextField();
        }
    }

    //Procède aux différents appel de validation et demande une confirmation pour les règlement par compte AE
    @FXML
    private void handleValidation(){

        //Si le type de payement a été choisii
        if(isPaymentMethodSelected()) {
            //Si la commande a déjà été confirmé une fois (dans le cas d'un règlement par compte AE
            if( ValidationButton.getText().equals("Confirmation")){
                ValidationButton.setText("Validation");
                validation();
            }else {
                //Si la commande est validée pour la première fois par compte AE
                if (getPaymentMethodSelection().equals("compte AE")) {
                    PaymentMethodSelectionLabel.setTextFill(Color.BLACK);
                    ValidationButton.setText("Confirmation");
                }else{
                    //Si la commande est validée pour la première fois, mais que le règlement ne se fait pas par compte AE
                    validation();
                }
            }
        }
        else{
            PaymentMethodSelectionLabel.setTextFill(Color.RED);
        }
    }

    //Listener de la liste des menus, onglet "Caisse"
    @FXML
    private void handleAddSelectionFromMenuList(){
        handleAddSelection(MenuListView);
    }

    //Listener de la liste des produits, onglet "Caisse"
    @FXML
    private void handleAddSelectionFromGaletteList(){
        handleAddSelection(GaletteListView);
    }

    //Listener de la liste des produits, onglet "Caisse"
    @FXML
    private void handleAddSelectionFromMealList(){
        handleAddSelection(MealListView);
    }

    //Listener de la liste des produits, onglet "Caisse"
    @FXML
    private void handleAddSelectionFromSnackList(){
        handleAddSelection(SnackListView);
    }

    //Listener de la liste des produits, onglet "Caisse"
    @FXML
    private void handleAddSelectionFromDrinkList(){
        handleAddSelection(DrinkListView);
    }

    //Ajoute l'élement sélectionné de la liste passée en paramètre
    private void handleAddSelection(ListView<Produit> listView){
        Produit currentProduct = listView.getSelectionModel().getSelectedItem();

        if(currentProduct.testStock()) {
            ValidationButton.setText("Validation");
            listItemsVentes.add(currentProduct);
            System.out.println("selection added to BufferVenteList");
            updateTextField();
            listView.getSelectionModel().clearSelection();
        }else{
            System.err.println("Erreur vente ("+currentProduct.getNom()+") : stock épuisé");
        }
    }

    //Listener de la liste répertoriant la commande en cours, onglet "Caisse"
    @FXML
    private void handleRemoveSelection(){
        ValidationButton.setText("Validation");
        listItemsVentes.remove(BufferVenteListView.getSelectionModel().getSelectedItem());
        System.out.println("selection removed from BufferVenteList");
        updateTextField();
        BufferVenteListView.getSelectionModel().clearSelection();
    }

    //Listeners des boutons de sélection du type de paiement, onglet "Caisse"
    //Ils permettent de désélectionner les autres boutons lorsqu'ils le deviennent
    @FXML
    private void liquidePaymentMethodSelected(){
        if(LiquidePaymentMethodRadioButton.isSelected()) {
            ChequePaymentMethodRadioButton.setSelected(false);
            CompteAEPaymentMethodRadioButton.setSelected(false);
            ValidationButton.setText("Validation");
        }
    }
    @FXML
    private void chequePaymentMethodSelected(){
        if(ChequePaymentMethodRadioButton.isSelected()) {
            LiquidePaymentMethodRadioButton.setSelected(false);
            CompteAEPaymentMethodRadioButton.setSelected(false);
            ValidationButton.setText("Validation");
        }
    }
    @FXML
    private void compteAEPaymentMethodSelected(){
        if(CompteAEPaymentMethodRadioButton.isSelected()) {
            LiquidePaymentMethodRadioButton.setSelected(false);
            ChequePaymentMethodRadioButton.setSelected(false);
            ValidationButton.setText("Validation");
        }
    }

    //Permet de remettre à son état initial la sélection du type de paiement
    private void clearPaymentMethodSelection(){
        for(RadioButton current : radioButtonList){
            current.setSelected(false);
        }
    }

    //Renvoie true si un type de paiement a été sélectionné, et false sinon
    private boolean isPaymentMethodSelected(){
        boolean selection = false;
        for(RadioButton current : radioButtonList){
            selection = selection || current.isSelected();
        }
        return selection;
    }

    //Renvoie une chaine de caractère correspondant au type de paiement choisi (suppose que le choix a déjà été fait)
    private String getPaymentMethodSelection(){
        if(LiquidePaymentMethodRadioButton.isSelected()){ return "liquide"; }
        if(ChequePaymentMethodRadioButton.isSelected()){ return "cheque"; }
        if(CompteAEPaymentMethodRadioButton.isSelected()){ return "compte AE"; }
        if(FreeGuestPaymentMethodRadioButton.isSelected()){ return "gratuit (invité)"; }
        return "gratuit (staff)";
    }
}
