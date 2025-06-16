package mypackage.controllers;

import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import mypackage.MainApp;
import mypackage.model.DataBaseInteractor.*;
import mypackage.model.Investor;
import mypackage.model.InvestorNP;
import mypackage.model.InvestorLP;
import mypackage.view.util.ConfirmWindow;



public class ControllerSouscripteurs {

    @FXML
    private Label NameInvestor;
    @FXML
    private TableView<Investor> tableInvestors;
    @FXML
    private TableColumn<Investor, String> listInvestorName;
    @FXML
    private TableColumn<Investor, String> listInvestorId;

    @FXML
    private Button deleteButton;

    private MainApp mainApp;

    private ObservableList<Investor> listInvestor = FXCollections.observableArrayList();

    private ArrayList<Integer> ids = new ArrayList<>();

    private Investor selectedInvestor;

    public ControllerSouscripteurs() {
    }

    @FXML
    private void initialize() {
        // Add some sample data
        ids = InvestorInteractor.GetAllInvestorId();
        for (Integer id : ids) {
            if (InvestorInteractor.GetInvestorLP(id) != null){
                listInvestor.add(InvestorInteractor.GetInvestorLP(id));
            }
            if (InvestorInteractor.GetInvestorNP(id) != null){
                listInvestor.add(InvestorInteractor.GetInvestorNP(id));
            }
        }
        if (!listInvestor.isEmpty()) {
            System.out.println("Investors loaded: " + listInvestor.size());
            tableInvestors.setItems(listInvestor);
        }
        this.listInvestorName.setCellValueFactory(new PropertyValueFactory<Investor, String>("name"));
        this.listInvestorId.setCellValueFactory(new PropertyValueFactory<Investor, String>("id"));

        displayInvestor(null);
    
        tableInvestors.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> displayInvestor(newValue));
    }

    public ObservableList<Investor> getInvestors() {
        return listInvestor;
    }

    public ArrayList<Integer> GetIds() {
        return ids;
    }   

    private void displayInvestor(Investor investor) {
        this.selectedInvestor = investor;
        if (investor instanceof InvestorNP) {
            String name = ((InvestorNP) investor).getName();
            NameInvestor.setText(name);
        } else if (investor instanceof InvestorLP) {
            String name = ((InvestorLP) investor).getName();
            NameInvestor.setText(name);
        } else {
            // Clear the details if no person is selected
            NameInvestor.setText("");
        }
    }

    @FXML
    private void deleteInvestor(){
        if(selectedInvestor == null) {
            System.out.println("No Investor selected to delete.");
            return;
        }
        if (ConfirmWindow.confirmWindow()) {
            if (selectedInvestor instanceof InvestorLP) {
                System.out.println("Investor deleted: " + ((InvestorLP)selectedInvestor).getName());
                InvestorInteractor.DeleteInvestorLP(selectedInvestor.getId());
                listInvestor.remove(selectedInvestor);
            } else if (selectedInvestor instanceof InvestorNP) {
                System.out.println("Investor deleted: " + ((InvestorNP)selectedInvestor).getName());
                InvestorInteractor.DeleteInvestorNP(selectedInvestor.getId());
                listInvestor.remove(selectedInvestor);
            }
        } else {
            System.out.println("Deletion cancelled.");
            return;
        }
    }

    public void getMainApp() {
        if (mainApp != null) {
            System.out.println("MainApp is set.");
        } else {
            System.out.println("MainApp is not set.");
        }
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
}
