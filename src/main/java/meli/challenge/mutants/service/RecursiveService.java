package meli.challenge.mutants.service;

import lombok.RequiredArgsConstructor;
import meli.challenge.mutants.service.dto.MatrixComparison;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecursiveService {
    private static final int sequencesNeeded = 2;
    private static final int letterSuccess = 4;

    public boolean isMutant(char[][] matrix) {
        int repetitions = 0;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                if (isValidDna(getMatrix(matrix, i, j))
                        && (++repetitions == sequencesNeeded)) return true;
            }
        }
        return false;
    }

    private MatrixComparison getMatrix(char[][] matrix, int i, int j) {
       return  MatrixComparison.builder()
                .matrix(matrix)
                .oldRow(i)
                .oldColumn(j)
                .rowPosition(i)
                .columnPosition(j)
                .quantityFound(0)
                .matrixLength(matrix.length)
                .letter(matrix[i][j])
                .build();
    }

    private boolean isValidDna(MatrixComparison matrix) {
        if (matrix.getQuantityFound() == letterSuccess) return true;
        if (!matrix.isValidCord() || !matrix.isSameLetter()) return false;
        if (matrix.getQuantityFound() > 0) {
            return isValidDna(matrix.getNextResult());
        } else {
            return isValidDna(matrix.getDownResult()) ||
                    isValidDna(matrix.getDownRightResult()) ||
                    isValidDna(matrix.getRightResult()) ||
                    isValidDna(matrix.getUpperRightResult());
        }
    }
}
