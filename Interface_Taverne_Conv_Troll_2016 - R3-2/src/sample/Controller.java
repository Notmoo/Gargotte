package sample;


import com.sun.javafx.scene.control.skin.TableViewSkinBase;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import model.*;
import items.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;


@SuppressWarnings("ALL")
public class Controller implements Initializable {

    private final ObservableList<items.Menu> listItemsMenu = FXCollections.observableArrayList();
    private final ObservableList<Produit> listItemsGalette = FXCollections.observableArrayList();
    private final ObservableList<Produit> listItemsMeal = FXCollections.observableArrayList();
    private final ObservableList<Produit> listItemsSnack = FXCollections.observableArrayList();
    private final ObservableList<Produit> listItemsDrink = FXCollections.observableArrayList();
    private final ObservableList<Vendable> listItemsVentes = FXCollections.observableArrayList();
    private final ObservableList<Vendable> listProductTableView = FXCollections.observableArrayList();
    private final ObservableList<Vendable> listProductWithoutMenuTableView = FXCollections.observableArrayList();
    private Model model;

    //----------------------------------Conteneurs d'onglets------------------------------------------------------------
    @FXML
    private TabPane MainTabPane;
    @FXML
    private  TabPane SelectTabPane;
    @FXML
    private VBox VBoxMain;




    //-----------------------------------------------ONGLET "CAISSE"----------------------------------------------------
    //récupération du Tab contenant le système de caisse
    @FXML
    private Tab TabPaneCaisse;

    //Récupération des différentes listes de produit
    @FXML
    private ListView<items.Menu> MenuListView;
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
    private TableView<Vendable> BufferVenteTableView;
    @FXML
    private TableColumn ProductVenteTableColumn;
    @FXML
    private TableColumn QuantiteVenteTableColumn;

    //Récupération du bouton de validation de la commande
    @FXML
    private Button ValidationButton;

    @FXML
    Menu MenuFile;

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
    //--------------------------------------------------------CODE------------------------------------------------------

    //Méthode initialize, obligatoire pour la classe controller et se lance lors de la création de la VBox dans Main.java
    public void initialize(URL url, ResourceBundle rb) {

        //On initialise tout le modèle, y compris la lecture de la base de donnée et la récupération des produits
        model = new Model();


        //on initialise l'affichage des listes de l'onglet "Caisse"
        updateListView();

        // Association entre les listes et les éléments de l'onglet "Caisse"
        ProductVenteTableColumn.setCellValueFactory(new PropertyValueFactory<Produit, String>("nom"));
        QuantiteVenteTableColumn.setCellValueFactory(new PropertyValueFactory<Produit, Integer>("nombreBufferVente"));
        BufferVenteTableView.setItems(listItemsVentes);
        GaletteListView.setItems(listItemsGalette);
        MealListView.setItems(listItemsMeal);
        SnackListView.setItems(listItemsSnack);
        DrinkListView.setItems(listItemsDrink);
        MenuListView.setItems(listItemsMenu);

        initListViewColorDisplay(GaletteListView, MealListView, SnackListView, DrinkListView);

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
        initTableViewColorDisplay(StockTableView);

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
        StockUpdateTableView.setItems(listProductWithoutMenuTableView);
        initTableViewColorDisplay(StockUpdateTableView);

        //On inscrit l'action effectuable sur le bouton de validation de commande
        ValidationButton.setText("Validation");

        //On inscrit l'action sur le bouton de débloquage et on verrouille la fenetre de modification du stock
        UnlockStockUpdateTableViewButton.setText("Unlock");
        StockUpdateTableView.setDisable(true);

        //On met en place les différents raccourci clavier
        MenuItemValidate.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));
        MenuItemValidate.setAccelerator(new KeyCodeCombination(KeyCode.ENTER, KeyCombination.CONTROL_DOWN));


        final KeyCombination keyComb1 = new KeyCodeCombination(KeyCode.A,
                KeyCombination.CONTROL_DOWN);
        final KeyCombination keyComb2= new KeyCodeCombination(KeyCode.A,
                KeyCombination.SHIFT_DOWN);
        final KeyCombination keyComb3= new KeyCodeCombination(KeyCode.Z,
                KeyCombination.CONTROL_DOWN);
        final KeyCombination keyComb4= new KeyCodeCombination(KeyCode.W,
                KeyCombination.CONTROL_DOWN);

        TabPaneCaisse.setOnSelectionChanged(new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        LiquidePaymentMethodRadioButton.requestFocus();
                    }
                });
            }
        });
        MainTabPane.addEventHandler(KeyEvent.KEY_RELEASED, new EventHandler() {
            @Override
            public void handle(Event event) {
                /*if (keyComb1.match((KeyEvent)event)) {
                    if(MainTabPane.getSelectionModel().isSelected(3))
                        MainTabPane.getSelectionModel().select(0);
                    else
                        MainTabPane.getSelectionModel().selectNext();
                }
                if (keyComb2.match((KeyEvent) event)) {

                    if (SelectTabPane.getSelectionModel().isSelected(4))
                        SelectTabPane.getSelectionModel().select(0);
                    else
                        SelectTabPane.getSelectionModel().selectNext();
                }
                if (keyComb3.match((KeyEvent) event)) {

                    if (SelectTabPane.getSelectionModel().isSelected(0))
                        handleRemoveKeyboardSelection(MenuListView);
                    if (SelectTabPane.getSelectionModel().isSelected(1))
                        handleRemoveKeyboardSelection(GaletteListView);
                    if (SelectTabPane.getSelectionModel().isSelected(2))
                        handleRemoveKeyboardSelection(MealListView);
                    if (SelectTabPane.getSelectionModel().isSelected(3))
                        handleRemoveKeyboardSelection(SnackListView);
                    if (SelectTabPane.getSelectionModel().isSelected(4))
                        handleRemoveKeyboardSelection(DrinkListView);
                }
                if (keyComb4.match((KeyEvent) event)) {

                    System.out.println("passed");
                    if (SelectTabPane.getSelectionModel().isSelected(0))
                        handleAddSelection(MenuListView, true);
                    if (SelectTabPane.getSelectionModel().isSelected(1))
                        handleAddSelection(GaletteListView, true);
                    if (SelectTabPane.getSelectionModel().isSelected(2))
                        handleAddSelection(MealListView, true);
                    if (SelectTabPane.getSelectionModel().isSelected(3))
                        handleAddSelection(SnackListView, true);
                    if (SelectTabPane.getSelectionModel().isSelected(4))
                        handleAddSelection(DrinkListView, true);
                   // handleAddSelection();
                }*/
                

            }
        });

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                LiquidePaymentMethodRadioButton.requestFocus();
            }
        });





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

        listProductWithoutMenuTableView.clear();
        listProductWithoutMenuTableView.addAll(model.getDao().getDBProduitsWithoutMenu());

        //On force la réinitialisation de l'affichage des tableurs des onglets Stock et MAJ stock
        StockTableView.getProperties().put(TableViewSkinBase.RECREATE, Boolean.TRUE);
        StockUpdateTableView.getProperties().put(TableViewSkinBase.RECREATE, Boolean.TRUE);

        //On remet les paramètres par défaut concernant le verrouillage de l'onglet "MAJ Stock" :
        // verrouillé, le bouton affichant "Unlock", avec le champ de saisie du mot de passe vide
        StockUpdateTableView.setDisable(true);
        UnlockStockUpdateTableViewButton.setText("Unlock");
        UnlockStockUpdateTableViewLabel.setText("");
        PasswordField.setDisable(false);
        PasswordField.setPromptText("enter password here");
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                PasswordField.requestFocus();
            }
        });
        PasswordField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode().equals(KeyCode.ENTER))
                {
                    handleUnlockButton(new ActionEvent());
                }
            }
        });

    }

    // Si le stock le permet, sauvegarde la sélection de produit dans la DB puis remet à zéro la sélection. Sinon, ne fait rien
    private boolean saveSoldItem(){
        System.out.println("Début Sauvegarde Commande");
        String paymentMethod = getPaymentMethodSelection();

        for(Vendable elem : listItemsVentes){
            model.addVente(new Vente(elem, elem.getNombreBufferVente(), paymentMethod));
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
            for (Vendable tempProd : listItemsVentes) {
                total += tempProd.getPrixDouble() * tempProd.getNombreBufferVente();
            }
            PrixCommandeTextField.setText((new Double(total).toString().format("%.2f",total)) + " €");
        }else{
            PrixCommandeTextField.setText("0 €");
        }
    }

    //Met à jour l'affichage des listes de menus et produits dans l'onglet "Caisse"
    private void updateListView(){
        listItemsMenu.clear();
        listItemsGalette.clear();
        listItemsMeal.clear();
        listItemsSnack.clear();
        listItemsDrink.clear();

        for(Vendable elem : model.getDao().getDBProduits()){
            try {
                if(items.Menu.class.isInstance(elem)){
                    listItemsMenu.add((items.Menu)elem);
                }
                switch (elem.getCatNom()) {
                    case "Galette":
                        listItemsGalette.add((Produit) elem);
                        break;
                    case "Plat":
                        listItemsMeal.add((Produit) elem);
                        break;
                    case "Boisson":
                        listItemsDrink.add((Produit) elem);
                        break;
                    case "Snack":
                        listItemsSnack.add((Produit) elem);
                        break;
                    default:
                        throw new NoCatProduitInVenteException();
                }
            }catch(NoCatProduitInVenteException e){};
        }
    }

    @FXML
    private void StockTableViewMouseClickHandler(){
        StockTableView.getSelectionModel().clearSelection();
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

    @FXML
    private void handleClose(){
        Platform.exit();
    }

    //Listener de la liste des menus, onglet "Caisse"
    @FXML
    private void handleAddSelectionFromMenuList(){
        handleAddMenuSelection(MenuListView, false);

    }

    //Listener de la liste des produits, onglet "Caisse"
    @FXML
    private void handleAddSelectionFromGaletteList(){
        handleAddSelection(GaletteListView, false);
    }

    //Listener de la liste des produits, onglet "Caisse"
    @FXML
    private void handleAddSelectionFromMealList(){
        handleAddSelection(MealListView, false);
    }

    //Listener de la liste des produits, onglet "Caisse"
    @FXML
    private void handleAddSelectionFromSnackList(){
        handleAddSelection(SnackListView, false);
    }

    //Listener de la liste des produits, onglet "Caisse"
    @FXML
    private void handleAddSelectionFromDrinkList(){
        handleAddSelection(DrinkListView, false);
    }

    //Ajoute l'élement sélectionné de la liste passée en paramètre
    private void handleAddSelection(ListView<Produit> listView, boolean isKeyboardShortcut){
        Produit currentProduct = listView.getSelectionModel().getSelectedItem();

        if(currentProduct.testStock()) {
            ValidationButton.setText("Validation");
            boolean listContains = false;
            int index = 0;
            for(Vendable elem:listItemsVentes) {
                if(elem.getNom() == currentProduct.getNom()) {
                    listContains = true;
                    break;
                }
                index++;
            }
            if(listContains) {
                if(listItemsVentes.get(index).getNombreBufferVente() < Integer.parseInt(listItemsVentes.get(index).getNombreStock())) {
                    listItemsVentes.get(index).setNombreBufferVente(listItemsVentes.get(index).getNombreBufferVente() + 1);
                    listItemsVentes.set(index, listItemsVentes.get(index));
                    System.out.println("incrementing the quantity");
                } else {
                    System.out.println("no more quantity");
                }
            } else {
                listItemsVentes.add(currentProduct);
                listItemsVentes.get(index).setNombreBufferVente(1);
                System.out.println("selection added to BufferVenteTable");
            }
            updateTextField();
        }else{
            System.err.println("Erreur vente ("+currentProduct.getNom()+") : stock épuisé");
        }
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                LiquidePaymentMethodRadioButton.requestFocus();
            }
        });
    }

    //Ajoute l'élement sélectionné de la liste passée en paramètre
    private void handleAddMenuSelection(ListView<items.Menu> listView, boolean isKeyboardShortcut){
        items.Menu currentProduct = listView.getSelectionModel().getSelectedItem();

        if(currentProduct.testStock()) {
            ValidationButton.setText("Validation");
            boolean listContains = false;
            int index = 0;
            for(Vendable elem:listItemsVentes) {
                if(elem.getNom() == currentProduct.getNom()) {
                    listContains = true;
                    break;
                }
                index++;
            }
            if(listContains) {
                items.Menu item = (items.Menu) listItemsVentes.get(index);
                if(listItemsVentes.get(index).getNombreBufferVente() < Integer.parseInt(listItemsVentes.get(index).getNombreStock())) {
                    boolean components_quantity_ok = true;

                    for(items.Produit current : item.getComposants()){
                        if( ( current.getNombreBufferVente()+item.getNombreBufferVente() ) >= Integer.parseInt(current.getNombreStock())){
                            components_quantity_ok = false;
                        }
                        if(!components_quantity_ok) break;
                    }

                    if(components_quantity_ok) {
                        listItemsVentes.get(index).setNombreBufferVente(listItemsVentes.get(index).getNombreBufferVente() + 1);
                        listItemsVentes.set(index, listItemsVentes.get(index));
                        System.out.println("incrementing the quantity");
                    }else{
                        System.out.println("no more quantity");
                    }
                } else {
                    System.out.println("no more quantity");
                }
            } else {
                listItemsVentes.add(currentProduct);
                listItemsVentes.get(index).setNombreBufferVente(1);
                System.out.println("selection added to BufferVenteTable");
            }
            updateTextField();
        }else{
            System.err.println("Erreur vente ("+currentProduct.getNom()+") : stock épuisé");
        }
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                LiquidePaymentMethodRadioButton.requestFocus();
            }
        });
    }

    //Listener de la liste répertoriant la commande en cours, onglet "Caisse"
    @FXML
    private void handleRemoveSelection(){
        ValidationButton.setText("Validation");

        Vendable currentProduct = BufferVenteTableView.getSelectionModel().getSelectedItem();
        boolean listContains;
        int index = 0;
        for(Vendable elem:listItemsVentes) {
            if(elem.getNom() == currentProduct.getNom()) {
                listContains = true;
                break;
            }
            index++;
        }

        if(listItemsVentes.get(index).getNombreBufferVente() > 1) {
            listItemsVentes.get(index).setNombreBufferVente(listItemsVentes.get(index).getNombreBufferVente() - 1);
            listItemsVentes.set(index, listItemsVentes.get(index));
            System.out.println("incrementing the quantity");
        }else{
            listItemsVentes.remove(BufferVenteTableView.getSelectionModel().getSelectedItem());
        }
        System.out.println("selection removed from BufferVenteList");
        updateTextField();
        BufferVenteTableView.getSelectionModel().clearSelection();
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                LiquidePaymentMethodRadioButton.requestFocus();
            }
        });
    }

    private void handleRemoveKeyboardSelection(ListView<Produit> listView){
        ValidationButton.setText("Validation");

        Produit currentProduct = listView.getSelectionModel().getSelectedItem();
        boolean listContains;
        int index = 0;
        for(Vendable elem:listItemsVentes) {
            if(elem.getNom() == currentProduct.getNom()) {
                listContains = true;
                break;
            }
            index++;
        }

        if(listItemsVentes.get(index).getNombreBufferVente() > 1) {
            listItemsVentes.get(index).setNombreBufferVente(listItemsVentes.get(index).getNombreBufferVente() - 1);
            listItemsVentes.set(index, listItemsVentes.get(index));
            System.out.println("incrementing the quantity");
        }else{
            listItemsVentes.remove(index);
        }


        System.out.println("selection removed from BufferVenteList");
        updateTextField();
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                LiquidePaymentMethodRadioButton.requestFocus();
            }
        });
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

    private void initListViewColorDisplay(ListView... listViews) {
        for (ListView listView : listViews) {
            listView.setCellFactory(lv -> {
                return new ListCell<Produit>() {

                    @Override
                    public void updateItem(Produit item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null) {
                            if (Integer.parseInt(item.getNombreStock()) <= 0) {
                                setId("Rupture");
                                //setStyle("-fx-background-color: #FF9595; -fx-font-fill: black");
                            } else {
                                if (Integer.parseInt(item.getNombreStock()) > 0 && Integer.parseInt(item.getNombreStock()) < 30) {
                                    setId("Faible");
                                    //setStyle("-fx-background-color: #FFFFCC; -fx-font-fill: black");
                                }else{
                                    setId("Haut");
                                    //setStyle("-fx-background-color: #CCFFCC; -fx-font-fill: black");
                                }
                            }
                            setText(item.toString());
                        } else {
                            setStyle(null);
                        }
                    }
                };
            });
        }
    }

    private void initTableViewColorDisplay(TableView... tableViews) {
        for(TableView tableView : tableViews) {
            tableView.setRowFactory(new Callback<TableView<Vendable>, TableRow<Vendable>>() {
                @Override
                public TableRow<Vendable> call(TableView<Vendable> tableView) {
                    final TableRow<Vendable> row = new TableRow<Vendable>() {
                        @Override
                        protected void updateItem(Vendable item, boolean empty) {
                            super.updateItem(item, empty);
                            if (item != null) {
                                if (Integer.parseInt(item.getNombreStock()) <= 0) {
                                    setId("Rupture");
                                } else {
                                    if (Integer.parseInt(item.getNombreStock()) > 0 && Integer.parseInt(item.getNombreStock()) < 30) {
                                        setId("Faible");
                                    } else {
                                        setId("Haut");
                                    }
                                }
                                setText(item.toString());
                            } else {
                                setStyle(null);
                            }
                        }
                    };
                    return row;
                }
            });

            tableView.addEventFilter(ScrollEvent.ANY, new EventHandler<ScrollEvent>() {
                @Override
                public void handle(ScrollEvent scrollEvent) {
                    System.out.println("Scrolled.");
                    tableView.getProperties().put(TableViewSkinBase.RECREATE, Boolean.TRUE);
                }
            });
        }
    }


    public void handleHelp(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Convention du troll");
        alert.setHeaderText("Aide raccourcis clavier");
        alert.setContentText("ctrl+[A] : change l'onglet actif\n" +
                "shift+[A] : change les onglets de type de produits \n" +
                "ctrl+[S] ou ctrl+[ENTER]  : valide une commande\n" +
                "ctrl+[Z] pour supprimer un produit dans la commande\n"+
                "ctrl+[W] pour ajouter un produit dans la commande\n"+
                "Notons qu'il est aussi possible de naviguer dans\n" +
                " les moyens de paiements à l'aide des flèches.\n");

        alert.setResizable(true);
        alert.getDialogPane().setPrefSize(500, 300);

        alert.show();
    }
}
