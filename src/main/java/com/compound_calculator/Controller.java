package com.compound_calculator;

import com.compound_calculator.form.*;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;

/**
 * Controller class for the main view
 */
public class Controller {

    /**
     * FXML elements imported from index.fxml
     */
    @FXML
    private GridPane resultsSection;

    @FXML
    private Button clrBtn, calcBtn;
    @FXML
    private TableView<Row> tableView;
    @FXML
    private Pagination pagination;
    private Table table;
    private LineChart<Number, Number> lineChart;
    @FXML
    private VBox formContainer, graphContainer;
    @FXML
    private MenuBar menuBar;
    private Form form;
    @FXML
    private ToggleButton compoundTglBtn, presentValueTglBtn, inflationTglBtn;

    private static DecimalFormat dollarFormat = new DecimalFormat("0.00");

    /**
     * Initialize the view by adding the options to the frequency combo box,
     * setting the default value to "Monthly", and adding listeners to the
     * buttons.
     */
    @FXML
    public void initialize() {
        table = new Table(tableView, pagination);

        lineChart = Graph.getLineChart();
        addForm(1);
        //form = new CompoundForm(formGridPane);
        MenuBarUtils.initializeMenuBar(menuBar, table);


        // Add listeners to the buttons
        calcBtn.setOnAction(e -> calculate());
        clrBtn.setOnAction(e -> clear());
        ToggleGroup tgg = new ToggleGroup();
        compoundTglBtn.setToggleGroup(tgg);
        presentValueTglBtn.setToggleGroup(tgg);
        inflationTglBtn.setToggleGroup(tgg);
        //Add functionality to toggleButtons
        compoundTglBtn.setOnAction(e -> changeFormType(0));
        presentValueTglBtn.setOnAction(e -> changeFormType(1));
        inflationTglBtn.setOnAction(e -> changeFormType(2));
    }

    private void addForm(int type) {
        Form f;
        if (type == 0) {
            f = new CompoundForm();
            formContainer.getChildren().add(f);
        } else if (type == 1) {
            f = new PresentValueForm();
            formContainer.getChildren().add(new PresentValueForm());
        } else {
            f = new InflationForm();
            System.out.println("initiating inflation form");
            formContainer.getChildren().add(new InflationForm());
        }
        this.form = f;
    }


    private void changeFormType(int formType) {
        if (formContainer.getChildren().size() < 2) return;
        clear();
        formContainer.getChildren().remove(1);
        addForm(formType);
        for (Node n : formContainer.getChildren()) {
            System.out.println(n.toString());
        }
        resultsSection.setVisible(false);
    }

    /**
     * Clear the table and the input fields.
     * This method is called when the clear button is pressed.
     */
    public void clear() {
        //Set table to invisible and select the first option in the frequency combo box
        table.clear();
        if (!graphContainer.getChildren().isEmpty()) {
            Node n = graphContainer.getChildren().get(0);
            n.setVisible(false);
            graphContainer.getChildren().remove(n);
        }
        resultsSection.setVisible(false);
        if (form instanceof CompoundForm) {
            form.clear();
        } else if (form instanceof InflationForm) {
            InflationForm.clearForm();
        } else if (form instanceof PresentValueForm) {
            PresentValueForm.clearForm();
        }


    }

    @FXML
    private void displayMoreInformation() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        if (this.form instanceof CompoundForm) {
            alert.setTitle("Compound Interest");
            alert.setHeaderText("How it works");
            alert.setContentText("Compound interest involves earning or paying interest on" +
                    " both the initial amount and the previously earned interest, resulting" +
                    " in the exponential growth of the total amount over time. To learn more," +
                    " visit https://en.wikipedia.org/wiki/Compound_interest");

        } else if (this.form instanceof InflationForm) {
            alert.setTitle("Inflation");
            alert.setHeaderText("How it works");
            alert.setContentText("Inflation is the continuous increase in prices, diminishing purchasing power," +
                    " influenced by factors such as demand spikes, supply disruptions, or excess money circulation." +
                    " To learn more, visit https://www.imf.org/en/Publications/fandd/issues/Series/Back-to-Basics/Inflation");
        } else if (this.form instanceof PresentValueForm) {
            alert.setTitle("Present Value");
            alert.setHeaderText("How it works");
            alert.setContentText("Present value is an economic concept that reflects the current worth of a sum of" +
                    " money or a series of future cash flows, taking into account the time value of money." +
                    " It recognizes that a given amount of money today is more valuable than the same" +
                    " amount in the future due to the potential for earning returns or interest." +
                    " To learn more, visit https://www.investopedia.com/terms/p/presentvalue.asp");
        }
        alert.showAndWait();
    }

    private void setCpdIntrResultsSection(@NotNull ObservableList<Row> data, double yearlyAddition) {
        //Calculate the total interest and capital
        final double initInv = data.get(0).getCapital();
        final int years = data.size() - 1;
        final double totCapital = data.get(data.size() - 1).getCapital();
        final double totInterest = totCapital - initInv - yearlyAddition * years;

        resultsSection.add(new Label("Total value of your investment"), 0, 0);
        resultsSection.add(new Label("Total interest earned"), 0, 1);
        resultsSection.add(new Label(totCapital + ""), 1, 0);
        resultsSection.add(new Label(totInterest + ""), 1, 1);

        resultsSection.setVisible(true);
    }

    private void setInflResultsSection(String infl, String yInfl) {
        resultsSection.add(new Label("Inflation rate"), 0, 0);
        resultsSection.add(new Label("Yearly inflation rate"), 0, 1);
        resultsSection.add(new Label(infl + "%"), 1, 0);
        resultsSection.add(new Label(yInfl + "%"), 1, 1);
        resultsSection.setVisible(true);
    }

    private void setPresValResultsSection(String presentValue, String lostToInflation) {
        resultsSection.add(new Label("Present value"), 0, 0);
        resultsSection.add(new Label(presentValue), 1, 0);
        resultsSection.add(new Label("Lost to inflation"), 0, 1);
        resultsSection.add(new Label(lostToInflation), 1, 1);
        resultsSection.setVisible(true);
    }

    private void clearResultsSection() {
        for (Node n : resultsSection.getChildren()) {
            n.setVisible(false);
        }
    }

    /**
     * Calculate the compound interest and display the results in the table.
     */
    public void calculate() {
        clearResultsSection();
        System.out.println(this.form.getClass());
        ObservableList<Row> data = form.getData();

        if (data == null) return;
        table.setData(data);

        if (this.form instanceof CompoundForm) {
            CompoundForm f = (CompoundForm) form;
            setCpdIntrResultsSection(data, f.getYearlyAddition());
        } else if (this.form instanceof InflationForm) {
            InflationForm infF = (InflationForm) form;
            setInflResultsSection(dollarFormat.format(infF.getInflRate()), dollarFormat.format(infF.getYearlyInflRate()));
        } else if (this.form instanceof PresentValueForm) {
            PresentValueForm pVF = (PresentValueForm) form;
            setPresValResultsSection(dollarFormat.format(pVF.getPresentValue()), dollarFormat.format(pVF.getLostToInflation()));
        }

        //Creates and adds new Line Chart with chosen data to appropriate VBox container named "graphContainer"
        if (!graphContainer.getChildren().isEmpty()) {
            //If a graph was already generated, and the user wishes to generate a new one,
            //everything is removed form graphContainer to allow a new graph to be added.
            //Since there can only be one chart at a time, we only need to remove element 0 form the container.
            //We set it to be invisible first, because otherwise the graphics don't update, and it stays on the screen
            //despite having been deleted.
            Node n = graphContainer.getChildren().get(0);
            n.setVisible(false);
            graphContainer.getChildren().remove(n);

        }
        addLineChart(data);

    }

    private void addLineChart(ObservableList<Row> data) {
        //the '0' in the line below makes sure of the fact that the graph is added to the top of the VBox
        lineChart = Graph.getLineChart(data);
        graphContainer.getChildren().add(0, lineChart);
    }

}
