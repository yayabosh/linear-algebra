package com.abosh;

import java.util.Arrays;

/**
 * @author Abosh Upadhyaya
 */
public class Matrix {
    // private data fields
    private final double[][] matrix;
    private final int rows;
    private final int cols;

    /**
     * default constructor. initializes all values in matrix to 0.
     * 
     * @param rows number of rows in matrix
     * @param cols number of columns in matrix
     */
    public Matrix(final int rows, final int cols) {
        this.matrix = new double[rows][cols];
        this.rows = rows;
        this.cols = cols;
    }

    /**
     * constructs a copy of an inputted matrix.
     * 
     * @param m matrix to be copied
     */
    public Matrix(final Matrix copy) {
        this(copy.getRows(), copy.getCols());
        for (int r = 0; r < rows; r++)
            System.arraycopy(copy.matrix[r], 0, this.matrix[r], 0, cols);
    }

    /**
     * replaces the current matrix with the identity matrix, preserving its current dimensions and resetting the matrix
     * to a default state.
     */
    public void gLoadIdentity() {
        if (this.rows != this.cols)
            throw new IllegalArgumentException(
                "An identity matrix must be square, i.e. the number of rows and columns must be equal.");
        // where the 1 will be located
        int i = 0;
        for (int r = 0; r < this.rows; r++) {
            for (int c = 0; c < this.cols; c++) {
                if (c == i)
                    matrix[r][i] = 1.0;
                else
                    matrix[r][c] = 0.0;
            }
            i++;
        }
    }

    /**
     * sets values for a row in the matrix starting at the beginning of the row.
     * 
     * @param r      row index
     * @param values values to set
     */
    public void setRow(final int r, final double[] values) {
        if (values.length > cols)
            throw new IllegalArgumentException("There cannot be more values than columns in the matrix.");
        setRowFromIndex(r, values, 0, values.length);
    }

    /**
     * sets values for a row in the matrix starting at a specific index.
     * 
     * @param r      row index
     * @param values values to set
     * @param i      index to start copying from in values[]
     * @param len    total number of components to be copied from values[]
     */
    public void setRowFromIndex(final int r, final double[] values, final int i, final int len) {
        if (r >= rows)
            throw new IllegalArgumentException(
                "The specified row index is greater than the number of rows in the matrix.");
        if (len > cols)
            throw new IllegalArgumentException(
                "The number of components to be copied into the matrix exceed the matrix's bounds.");
        if (i + len > values.length)
            throw new IllegalArgumentException(
                "The number of components to be copied from the inputted array exceed its bounds.");
        System.arraycopy(values, i, matrix[r], 0, len);
    }

    /**
     * sets the value of one position in the matrix.
     * 
     * @param r     row index
     * @param c     column index
     * @param value value to set
     */
    public void setValue(final int r, final int c, final double value) {
        if (r >= rows || c >= cols)
            throw new IllegalArgumentException(
                "The specified location exceeds the matrix's row or column bounds.");
        matrix[r][c] = value;
    }

    /**
     * returns a summed matrix of an inputted matrix with the current matrix object.
     * 
     * @param addend matrix to be added to current matrix.
     * @return a summed matrix
     */
    public Matrix add(final Matrix addend) {
        // reconstructs addend so it can be returned once summed
        final Matrix sum = new Matrix(addend);
        sum.addInPlace(this);
        return sum;
    }

    /**
     * modifies current matrix by adding new matrix to it.
     * 
     * @param addend matrix to be added to current matrix.
     */
    public void addInPlace(final Matrix addend) {
        if (!dimensionEquality(addend))
            throw new IllegalArgumentException(
                    String.format("Expected %d rows and %d columns. Received %d rows and %d columns.",
                            this.getRows(), this.getCols(), addend.getRows(), addend.getRows()));
        // add the matrices
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                matrix[r][c] += addend.getValue(r, c);
            }
        }
    }

    /**
     * subtracts an inputted matrix from the current matrix object, returning a new subtracted matrix.
     * @param subtrahend matrix to subtract
     * @return a subtracted matrix
     */
    public Matrix subtract(final Matrix subtrahend) {
        // reconstructs subtrahend so a new matrix can be returned
        final Matrix difference = new Matrix(this);
        difference.subtractInPlace(subtrahend);
        return difference;
    }

    /**
     * subtracts an inputted matrix from the current matrix object.
     * @param subtrahend matrix to subtract
     */
    public void subtractInPlace(final Matrix subtrahend) {
        // matrix rows and columns must be equivalent for matrix subtraction
        if (!dimensionEquality(subtrahend))
            throw new IllegalArgumentException(
                    String.format("Expected %d rows and %d columns. Received %d rows and %d columns.",
                            this.getRows(), this.getCols(), subtrahend.getRows(), subtrahend.getRows()));

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                this.matrix[r][c] -= subtrahend.getValue(r, c);
            }
        }
    }

    /**
     * multiplies two matrices together.
     * 
     * @param multiplicand matrix to multiply this.matrix by
     * @return a new multiplied matrix
     */
    public Matrix multiply(final Matrix multiplicand) {
        if (this.cols != multiplicand.getRows())
            throw new IllegalArgumentException(
                "The number of columns in this matrix must equal the number of rows in the multiplicand matrix.");

        final Matrix product = new Matrix(this.rows, multiplicand.getCols());
        for (int r = 0; r < product.getRows(); r++) {
            for (int c = 0; c < product.getCols(); c++) {
                for (int i = 0, j = 0; i < this.cols && j < multiplicand.getRows(); i++, j++)
                    product.matrix[r][c] += this.matrix[r][i] * multiplicand.getValue(j, c);
            }
        }
        return product;
    }

    @Override
    public boolean equals(final Object anObject) {
        // compares memory locations
        if (this == anObject)
            return true;
        // compares matrix values
        if (anObject instanceof Matrix) {
            final Matrix anotherMatrix = (Matrix) anObject;
            if (!dimensionEquality(anotherMatrix))
                return false;
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    if (matrix[r][c] != anotherMatrix.getValue(r, c))
                        return false;
                }
            }
            return true;
        }
        return false;
    }

    /**
     * tests row and column equality between an inputted matrix and the current matrix object.
     * @param m an inputted matrix
     * @return if both the matrices' dimensions are equal
     */
    public boolean dimensionEquality(final Matrix m) {
        return m.getRows() == this.getRows() && m.getCols() == this.getCols();
    }

    /**
     * returns a value at specified indices in the matrix.
     * 
     * @param r row index
     * @param c column index
     * @return a value in the matrix
     */
    public double getValue(final int r, final int c) {
        return this.matrix[r][c];
    }

    /**
     * returns a specified row in the matrix.
     * @param r row index
     * @return the specified row
     */
    public double[] getRow(final int r) {
        return matrix[r];
    }

    /**
     * returns a specified column in the matrix as a horizontal array.
     * @param c column index
     * @return the specified column
     */
    public double[] getCol(final int c) {
        final double[] col = new double[rows];
        for (int r = 0; r < rows; r++)
            col[r] = matrix[r][c];
        return col;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    /**
     * Returns a string representation of the contents of the specified matrix. The string representation consists of a
     * list of the matrix's elements enclosed in square brackets. Adjacent elements are separated by a comma followed by
     * a space.
     * @return a string representation of the matrix
     */
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        for (final double[] row : this.matrix) {
            builder.append(Arrays.toString(row));
            builder.append("\n");
        }
        return builder.toString();
    }
}
