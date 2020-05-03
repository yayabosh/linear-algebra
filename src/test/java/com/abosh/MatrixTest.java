package com.abosh;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class MatrixTest {

    @Test
    public void testMatrixDefaultConstructor() {
        final Matrix m = new Matrix(3, 2);
        assertNotNull(m);
        assertEquals(3, m.getRows());
        assertEquals(2, m.getCols());
    }

    @Test
    public void testMatrixCopyConstructor() {
        final Matrix m = new Matrix(4, 5);
        final int[] values = new int[] { 60, 20, 30, 40, 50 };
        m.setRow(2, values);
        final Matrix copy = new Matrix(m);
        assertEquals(m, copy);
        assertEquals(4, m.getRows());
        assertEquals(5, m.getCols());
        assertArrayEquals(values, copy.getRow(2));
    }

    @Test
    public void testGLoadIdentityHappyPath() {
        final Matrix m = new Matrix(4, 4);
        final Matrix expectedIdentityMatrix = new Matrix(4, 4);
        int i = 0;
        for (int r = 0; r < expectedIdentityMatrix.getRows(); r++) {
            expectedIdentityMatrix.setValue(r, i, 1);
            i++;
        }
        m.gLoadIdentity();
        assertEquals(expectedIdentityMatrix, m);
    }

    @Test
    public void testGLoadIdentityThrowsIllegalArgumentException() {
        final Matrix unequalDimensions = new Matrix(4, 5);
        final IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class, () -> unequalDimensions.gLoadIdentity());
        assertEquals(
            "An identity matrix must be square, i.e. the number of rows and columns must be equal.", 
            thrown.getMessage());
    }

    @Test
    public void testSetRowHappyPath() {
        final int[] values = new int[] { 1, 2, 3, 4, 5 };
        final Matrix m = new Matrix(3, 5);
        m.setRow(2, values);
        assertArrayEquals(values, m.getRow(2));
    }

    @Test
    public void testSetRowThrowsIllegalArgumentException() {
        final int[] values = new int[] { 5, 6, 7, 2, 3 };
        final Matrix m = new Matrix(2, 1);
        final IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class, () -> m.setRow(1, values));
        assertEquals("There cannot be more values than columns in the matrix.", thrown.getMessage());
    }

    @Test
    public void setRowFromIndexHappyPath() {
        // given
        final int[] values = new int[] { 583, 24, 1134, 3, 78 };
        final Matrix m = new Matrix(4, 4);
        final int[] selectedValues = new int[] { 24, 1134, 3, 78 };
        // when
        m.setRowFromIndex(1, values, 1, 4);
        // then
        assertArrayEquals(selectedValues, m.getRow(1));
    }

    @Test
    public void setRowFromIndexThrowsIllegalArgumentExceptionRows() {
        final int[] values = new int[] { 123, 23, 54, 38, 34 };
        final Matrix m = new Matrix(23, 4);
        final IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class, () -> m.setRowFromIndex(24, values, 2, 3));
        // assertions on the thrown exception
        assertEquals("The specified row index is greater than the number of rows in the matrix.", thrown.getMessage());
    }

    @Test
    public void setRowFromIndexThrowsIllegalArgumentExceptionCols() {
        final int[] values = { 9, 4, 3 };
        final Matrix m = new Matrix(2, 2);
        final IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class, () -> m.setRowFromIndex(0, values, 0, 3));
        assertEquals("The number of components to be copied into the matrix exceed the matrix's bounds.", 
            thrown.getMessage());
    }

    @Test
    public void setRowFromIndexThrowsIllegalArgumentExceptionArrayBounds() {
        final int[] values = { 10, 443, 32, 43, 89 };
        final Matrix m = new Matrix(5, 7);
        final IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class, () -> m.setRowFromIndex(0, values, 2, 4));
        assertEquals("The number of components to be copied from the inputted array exceed its bounds.",
            thrown.getMessage());
    }

    @Test
    public void testSetValueHappyPath() {
        final Matrix m = new Matrix(3, 4);
        m.setValue(2, 3, 435);
        assertEquals(435, m.getValue(2, 3));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetValueThrowsIllegalArgumentException() throws Exception {
        final Matrix m = new Matrix(18, 15);
        m.setValue(19, 14, 2);
    }

    @Test
    public void testAdd() {
        final Matrix m = new Matrix(3, 2);
        final Matrix addend = new Matrix(3, 2);
        final int[] values = new int[] { 1, 1 };
        int r = 0;
        // populate two matrices with values[]
        for (; r < m.getRows(); r++) {
            m.setRow(r, values);
            addend.setRow(r, values);
        }
        // create new matrix to represent expected matrix after add() is called
        final Matrix sum = new Matrix(3, 2);
        final int[] summedValues = new int[] { 2, 2 };
        // populate summed matrix with expected summed values
        for (r = 0; r < sum.getRows(); r++)
            sum.setRow(r, summedValues);
        assertEquals(sum, m.add(addend));
    }

    @Test
    public void testAddInPlaceHappyPath() {
        final Matrix m = new Matrix(3, 2);
        final Matrix addend = new Matrix(3, 2);
        final int[] values = new int[] { 1, 1 };
        int r = 0;
        for (; r < m.getRows(); r++) {
            m.setRow(r, values);
            addend.setRow(r, values);
        }
        final Matrix sum = new Matrix(3, 2);
        final int[] summedValues = new int[] { 2, 2 };
        for (r = 0; r < sum.getRows(); r++)
            sum.setRow(r, summedValues);
        m.addInPlace(addend);
        assertEquals(sum, m);
    }

    @Test
    public void testAddInPlaceThrowsIllegalArgumentException() {
        final Matrix m = new Matrix(3, 3);
        final Matrix addend = new Matrix(2, 2);
        final IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class, () -> m.addInPlace(addend));
        assertEquals("Expected 3 rows and 3 columns. Received 2 rows and 2 columns.", thrown.getMessage());
    }

    @Test
    public void testSubtractThisMinusThat() {
        final Matrix m = new Matrix(3, 3);
        final Matrix subtrahend = new Matrix(3, 3);
        final int[] mValues = new int[] { 48, 48, 48 };
        final int[] subtrahendValues = new int[] { 10, 10, 10 };
        int r = 0;
        for (; r < m.getRows(); r++) {
            m.setRow(r, mValues);
            subtrahend.setRow(r, subtrahendValues);
        }
        final Matrix difference = new Matrix(3, 3);
        final int[] differenceValues = new int[] { 38, 38, 38 };
        for (r = 0; r < difference.getRows(); r++)
            difference.setRow(r, differenceValues);
        assertEquals(difference, m.subtract(subtrahend));
    }

    @Test
    public void testSubtractThatMinusThis() {
        final Matrix m = new Matrix(3, 3);
        final Matrix subtrahend = new Matrix(3, 3);
        final int[] mValues = new int[] { 44, 44, 44 };
        final int[] subtrahendValues = new int[] { 18, 18, 18 };
        int r = 0;
        for (; r < m.getRows(); r++) {
            m.setRow(r, mValues);
            subtrahend.setRow(r, subtrahendValues);
        }
        final Matrix difference = new Matrix(3, 3);
        final int[] differenceValues = new int[] { -26, -26, -26 };
        for (r = 0; r < difference.getRows(); r++)
            difference.setRow(r, differenceValues);
        assertEquals(difference, subtrahend.subtract(m));
    }

    @Test
    public void testSubtractInPlaceHappyPath() {
        final Matrix m = new Matrix(3, 3);
        final Matrix subtrahend = new Matrix(3, 3);
        final int[] mValues = new int[] { 30, 30, 30 };
        final int[] subtrahendValues = new int[] { 10, 10, 10 };
        int r = 0;
        for (; r < m.getRows(); r++) {
            m.setRow(r, mValues);
            subtrahend.setRow(r, subtrahendValues);
        }
        final Matrix difference = new Matrix(3, 3);
        final int[] differenceValues = new int[] { 20, 20, 20 };
        for (r = 0; r < difference.getRows(); r++)
            difference.setRow(r, differenceValues);
        m.subtractInPlace(subtrahend);
        assertEquals(difference, m);
    }

    @Test
    public void testSubtractInPlaceThrowsIllegalArgumentException() {
        final Matrix m = new Matrix(4, 3);
        final Matrix subtrahend = new Matrix(3, 3);
        final IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> m.subtractInPlace(subtrahend));
        assertEquals("Expected 4 rows and 3 columns. Received 3 rows and 3 columns.", thrown.getMessage());
    }

    @Test
    public void testMultiplyHappyPath() {
        final Matrix m = new Matrix(4, 3);
        final Matrix multiplicand = new Matrix(3, 2);
        final int[] mValues = new int[] { 2, 2, 2 };
        final int[] multiplicandValues = new int[] { 3, 3 };
        int r = 0;
        for (; r < m.getRows(); r++)
            m.setRow(r, mValues);
        for (r = 0; r < multiplicand.getRows(); r++)
            multiplicand.setRow(r, multiplicandValues);
        final Matrix product = new Matrix(m.getRows(), multiplicand.getCols());
        final int[] productValues = new int[] { 18, 18 };
        for (r = 0; r < product.getRows(); r++)
            product.setRow(r, productValues);
        assertEquals(product, m.multiply(multiplicand));
    }

    @Test
    public void testMultiplyThrowsIllegalArgumentException() {
        final Matrix m = new Matrix(3, 2);
        final Matrix multiplicand = new Matrix(3, 2);
        final IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> m.multiply(multiplicand));
        assertEquals("The number of columns in this matrix must equal the number of rows in the multiplicand matrix.",
                thrown.getMessage());
    }

    @Test
    public void testEqualsSameMemoryLocation() {
        final Matrix m = new Matrix(15, 24);
        assertTrue(m.equals(m));
    }

    @Test
    public void testEqualsSameMatrixValuesHappyPath() {
        final Matrix m = new Matrix(2, 3);
        final Matrix copy = new Matrix(2, 3);
        final int[] values = new int[] { 2, 3, 4 };
        for (int r = 0; r < m.getRows(); r++) {
            m.setRow(r, values);
            copy.setRow(r, values);
        }
        assertTrue(m.equals(copy));
    }

    @Test
    public void testEqualsUnequalDimensions() {
        final Matrix m = new Matrix(3, 2);
        final Matrix unequalDimensions = new Matrix(2, 3);
        assertFalse(m.equals(unequalDimensions));
    }

    @Test
    public void testEqualsUnequalMatrixValues() {
        final Matrix m = new Matrix(3, 2);
        final Matrix unequalValues = new Matrix(3, 2);
        final int[] values = { 1, 1 };
        for (int r = 0; r < m.getRows(); r++)
            m.setRow(r, values);
        assertFalse(m.equals(unequalValues));
    }

    @Test
    public void testEqualsIsNotInstanceOf() {
        final Matrix m = new Matrix(3, 5);
        final String definitelyAMatrix = new String("I am a matrix!");
        assertFalse(m.equals(definitelyAMatrix));
    }

    @Test
    public void testDimensionEqualityTrue() {
        final Matrix m = new Matrix(2, 3);
        final Matrix other = new Matrix(2, 3);
        assertTrue(m.dimensionEquality(other));
    }

    @Test
    public void testDimensionEqualityFalse() {
        final Matrix m = new Matrix(2, 3);
        final Matrix other = new Matrix(3, 3);
        assertFalse(m.dimensionEquality(other));
    }

    @Test
    public void testGetCol() {
        final Matrix m = new Matrix(5, 3);
        final int[] values = new int[] { 1, 2, 3 };
        for (int r = 0; r < m.getRows(); r++)
            m.setRow(r, values);
        final int[] expectedCol = new int[] { 2, 2, 2, 2, 2 };
        assertArrayEquals(expectedCol, m.getCol(1));
    }

    @Test
    public void testToString() {
        final Matrix identityMatrix = new Matrix(3, 3);
        identityMatrix.gLoadIdentity();
        assertEquals("[1, 0, 0]\n[0, 1, 0]\n[0, 0, 1]\n", identityMatrix.toString());
    }
}
