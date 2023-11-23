package com.compound_calculator.form;

import com.compound_calculator.Row;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.Test;

public class PresentValueFormTest {
    private ObservableList<Row> computePresentValueAnnuity(int n, double pMT, double r, double interval) {
        /*
            this is the formula:

            P= SUM(pMT/(1+r)^n)
            where:
            P= present value of annuity
            pMT= dollar amount each annuity payment
            r= discount/interest rate
            n= number of periods in which the payments will be made
         */
        ObservableList<Row> data = FXCollections.observableArrayList();
        double totalValue = 0;
        for (float i = 0; i <= n; i += interval) {
            double presentValue = pMT / Math.pow(1 + (r / 100.0f), i);
            totalValue += presentValue;
            data.add(new Row((int) i, presentValue));
        }
        return data;
    }

    @Test
    void computePresentValueAnnuityTest1() {
        ObservableList<Row> data = computePresentValueAnnuity(10, 100, 5, 1);
        assert data.size() == 11;
        assert data.get(0).getTime() == 0;
    }
}
