package meli.challenge.mutants.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatrixComparison {
    private char[][] matrix;
    private int oldRow;
    private int oldColumn;
    private int rowPosition;
    private int columnPosition;
    private int quantityFound;
    private int matrixLength;
    private char letter;

    public boolean isValidCord() {
        return !(rowPosition < 0 || rowPosition >= matrixLength || columnPosition < 0 || columnPosition >= matrixLength);
    }
    public boolean isSameLetter() {
        return matrix[this.rowPosition][this.columnPosition] == this.letter;
    }

    public MatrixComparison getNextResult() {

        return MatrixComparison.builder()
                .matrix(this.getMatrix())
                .oldRow(this.getRowPosition())
                .oldColumn(this.getColumnPosition())
                .rowPosition(this.getRowPosition() + this.getRowPosition() - this.getOldRow())
                .columnPosition(this.getColumnPosition() + this.getColumnPosition() - this.getOldColumn())
                .quantityFound(this.getQuantityFound() + 1)
                .letter(this.getLetter())
                .matrixLength(this.getMatrixLength())
                .build();
    }

    public MatrixComparison getUpperRightResult() {
        return MatrixComparison.builder()
                .matrix(this.getMatrix())
                .oldRow(this.getOldRow())
                .oldColumn(this.getOldColumn())
                .rowPosition(this.getRowPosition() - 1)
                .columnPosition(this.getColumnPosition() + 1)
                .quantityFound(1)
                .letter(this.getLetter())
                .matrixLength(this.getMatrixLength())
                .build();
    }

    public MatrixComparison getRightResult() {
        return MatrixComparison.builder()
                .matrix(this.getMatrix())
                .oldRow(this.getOldRow())
                .oldColumn(this.getOldColumn())
                .rowPosition(this.getRowPosition())
                .columnPosition(this.getColumnPosition() + 1)
                .quantityFound(1)
                .letter(this.getLetter())
                .matrixLength(this.getMatrixLength())
                .build();
    }

    public MatrixComparison getDownRightResult() {
        return MatrixComparison.builder()
                .matrix(this.getMatrix())
                .oldRow(this.getOldRow())
                .oldColumn(this.getOldColumn())
                .rowPosition(this.getRowPosition() + 1)
                .columnPosition(this.getColumnPosition() + 1)
                .quantityFound(1)
                .letter(this.getLetter())
                .matrixLength(this.getMatrixLength())
                .build();
    }


    public MatrixComparison getDownResult() {
        return MatrixComparison.builder()
                .matrix(this.getMatrix())
                .oldRow(this.getOldRow())
                .oldColumn(this.getOldColumn())
                .rowPosition(this.getRowPosition() + 1)
                .columnPosition(this.getColumnPosition())
                .quantityFound(1)
                .letter(this.getLetter())
                .matrixLength(this.getMatrixLength())
                .build();
    }
}
