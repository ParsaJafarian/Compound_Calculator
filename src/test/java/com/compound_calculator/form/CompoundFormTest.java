package com.compound_calculator.form;

import com.compound_calculator.Row;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class FormTest {

    /**
     * Compound Data is compared to
     * <br>
     * <a href="https://www.getsmarteraboutmoney.ca/calculators/compound-interest-calculator/">
     * Get Smarter About Money Calculator
     * </a>
     */
    @Test
    void getCompoundDataTest1() {
        ObservableList<Row> data = CompoundForm.computeCompoundInterest(2000, 20, 0.03, 1, 1000);

        assert data.size() == 21;
        assert data.get(0).getTime() == 0;
        assert data.get(0).getCapital() == 2000;

        final double finalCapital = data.get(data.size() - 1).getCapital();
        final double expectedFinalCapital = 30482.60;

        // The final capital should be within 1 dollar of the expected final capital
        assertTrue(Math.abs(finalCapital - expectedFinalCapital) < 1, "Final capital: " + finalCapital + " Expected final capital: " + expectedFinalCapital);
    }

    @Test
    void getCompoundDataTest2() {
        ObservableList<Row> data = CompoundForm.computeCompoundInterest(1000, 10, 0.05, 12, 100);

        assert data.size() == 11;
        assert data.get(0).getTime() == 0;
        assert data.get(0).getCapital() == 1000;

        final double finalCapital = data.get(data.size() - 1).getCapital();
        final double expectedFinalCapital = 2911.64;

        // The final capital should be within 1 dollar of the expected final capital
        assertTrue(Math.abs(finalCapital - expectedFinalCapital) < 1, "Final capital: " + finalCapital + " Expected final capital: " + expectedFinalCapital);
    }
}
