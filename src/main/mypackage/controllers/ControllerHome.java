package mypackage.controllers;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.collections.FXCollections;

import mypackage.MainApp;
import mypackage.model.Obligation;
import mypackage.model.DataBaseInteractor.ObligationInteractor;
import mypackage.view.util.CouponWindow;

public class ControllerHome {
    @FXML
    private Label totalCapitauxLabel;
    @FXML
    private Label totalObligationsRunningLabel;
    @FXML
    private Label totalInvestorsLabel;
    @FXML
    private Label rateWeightedAverageLabel;
    @FXML
    private Label remainDurationAverageLabel;
    @FXML
    private Label lastUpdateLabel;

    @FXML
    private ListView<String[]> couponListView = new ListView<>();
    ObservableList<String[]> couponListViewItems = FXCollections.observableArrayList(); //String[] : "[0] = date, [1] = amount, [2] = obligationName"

    private MainApp mainApp;

    public ControllerHome() {
    }

    @FXML
    private void initialize() {
        
        displayObligationsResume();

        displayCoupon();

    }

    private void displayObligationsResume() {
        ArrayList<Integer> obligationsId = ObligationInteractor.GetAllObligationsId();

        Long totalCapitaux = 0l;
        Integer totalObligationsRunning = 0;
        Integer totalInvestors = 0;
        Long rateWeightedAverage = 0l;
        Long remainDurationDayAverage = 0l;

        for (Integer obligationId : obligationsId) {
            Obligation obligation = ObligationInteractor.GetObligation(obligationId);
            if (obligation != null) {
                if(LocalDate.now().isBefore(LocalDate.parse(obligation.getStartDate()).plusMonths(obligation.getDurationMonths()))) {
                    totalCapitaux += obligation.getCapital();
                    totalObligationsRunning += 1;
                    totalInvestors += obligation.getInvestors().size();
                    rateWeightedAverage += (obligation.getRate()[0] + obligation.getRate()[1]) * obligation.getCapital();
                    remainDurationDayAverage += obligation.getCapital() * ChronoUnit.DAYS.between(
                        LocalDate.now(),
                        LocalDate.parse(obligation.getStartDate()).plusMonths(obligation.getDurationMonths()));
                }
            } else {
                System.err.println("Obligation with ID " + obligationId + " not found.");
            }
        }
        if (totalObligationsRunning > 0) {
            rateWeightedAverage /= totalCapitaux;
            remainDurationDayAverage /= totalCapitaux;
        }
        Integer remainMonths = (int) (remainDurationDayAverage / 30);
        Integer remainDays = (int) (remainDurationDayAverage % 30);

        totalCapitauxLabel.setText(String.valueOf(totalCapitaux));
        totalObligationsRunningLabel.setText(String.valueOf(totalObligationsRunning));
        totalInvestorsLabel.setText(String.valueOf(totalInvestors));
        rateWeightedAverageLabel.setText(String.valueOf(rateWeightedAverage) + " %");
        remainDurationAverageLabel.setText(String.valueOf(remainMonths + " mois " + remainDays + " jours"));
    }

    private void getCoupons() {
        ArrayList<Integer> ObligIds = ObligationInteractor.GetAllObligationsId();
        for (Integer id : ObligIds) {
            Obligation obligation = ObligationInteractor.GetObligation(id);
            int period = 0;
            if(obligation.getPeriodicity() != null) {
                if(obligation.getPeriodicity().equals("Mensuelle")) {
                    period = 1;
                } else if(obligation.getPeriodicity().equals("Trimestrielle")) {
                    period = 3;
                } else if(obligation.getPeriodicity().equals("Semestrielle")) {
                    period = 6;
                } else if(obligation.getPeriodicity().equals("Annuelle")) {
                    period = 12;
                } else {
                    System.err.println("Unknown periodicity: " + obligation.getPeriodicity());
                    continue;
                }
            } else {
                System.err.println("Periodicity not set for obligation ID: " + id);
                continue;
            }
            if(obligation.getRate()[0] != 0){
                for(int i = 0; i < obligation.getDurationMonths(); i += period) {
                    LocalDate couponDate = LocalDate.parse(obligation.getStartDate()).plusMonths(i);
                    String[] coupon = new String[3];
                    coupon[0] = couponDate.toString();
                    coupon[1] = String.valueOf(obligation.getRate()[0] * obligation.getCapital() / 100);
                    coupon[2] = obligation.getName();
                    couponListViewItems.add(coupon);
                }
            }
            if(obligation.getRate()[1] != 0) {
                    LocalDate couponDate = LocalDate.parse(obligation.getStartDate()).plusMonths(obligation.getDurationMonths());
                    String[] coupon = new String[3];
                    coupon[0] = couponDate.toString();
                    coupon[1] = String.valueOf(obligation.getRate()[1] * obligation.getCapital() / 100);
                    coupon[2] = obligation.getName();
                    couponListViewItems.add(coupon);
                }
        }
    }

    @FXML
    private void displayCoupon() {
        getCoupons();
        couponListViewItems.sort((a, b) -> {
            LocalDate dateA = LocalDate.parse(a[0]);
            LocalDate dateB = LocalDate.parse(b[0]);
            return dateA.compareTo(dateB);
        });
        couponListView.setItems(couponListViewItems);
        couponListView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(String[] item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(null);
                Label  dateLabel = new Label();
                Label amountLabel = new Label();
                Label obligationNameLabel = new Label();
                Button buttonOuvrir = new Button("Ouvrir");
                AnchorPane content = new AnchorPane(dateLabel, amountLabel, obligationNameLabel, buttonOuvrir);
                AnchorPane.setRightAnchor(buttonOuvrir, 0.0);
                AnchorPane.setLeftAnchor(dateLabel, 0.0);
                AnchorPane.setLeftAnchor(amountLabel, 80.0);
                AnchorPane.setLeftAnchor(obligationNameLabel, 150.0);

                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    dateLabel.setText(item[0]);
                    amountLabel.setText(" " + item[1] +  " €");
                    obligationNameLabel.setText(" " + item[2] +  " ");
                    buttonOuvrir.setOnAction(event -> {
                    //CouponWindow.show(item);
                    });
                }
                setGraphic(content);
            }
        });
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
