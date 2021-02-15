package meli.challenge.mutants.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import meli.challenge.mutants.service.dto.DnaData;
import meli.challenge.mutants.service.dto.DnaStats;
import meli.challenge.mutants.service.errors.ErrorConstants;
import meli.challenge.mutants.service.errors.InvalidCharException;
import meli.challenge.mutants.service.errors.MatrixBadFormedException;
import meli.challenge.mutants.service.errors.MatrixBadFormedNxNException;
import meli.challenge.mutants.repository.DnaDataRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class MutantsAnalyzeService {
    private static final Pattern PATTERN = Pattern.compile("[ATCG]+", Pattern.CASE_INSENSITIVE);
    private final DnaDataRepository dnaDataRepository;
    private final RecursiveService recursiveService;

    public boolean isMutant(List<String> dna) {
        char[][] matrix = buildMatrixFromStringData(dna);
        boolean isMutant = recursiveService.isMutant(matrix);
        saveMutantInfo(dna, isMutant);
        return isMutant;
    }

    public DnaStats reporterDnaStats() {
        Optional<DnaStats> dnaStatsOpt = dnaDataRepository.reporterDnaStats().stream().findFirst();
        if (dnaStatsOpt.isPresent()){
            DnaStats dnaStats = dnaStatsOpt.get();
            if (dnaStats.getHumanSum() != 0){
                dnaStats.setRatio(BigDecimal.valueOf(dnaStats.getMutantSum()).divide(BigDecimal.valueOf(dnaStats.getHumanSum()),2,RoundingMode.HALF_EVEN));
            }
            return dnaStats;
        }
        return DnaStats.builder().build();
    }

    private void saveMutantInfo(List<String> dna, boolean isMutant) {
        DnaData dnaData = DnaData.builder()
                .id(String.join("", dna))
                .mutant(isMutant)
                .build();
        dnaDataRepository.save(dnaData);
    }

    public char[][] buildMatrixFromStringData(List<String> dna) {
        if (dna == null) throw new MatrixBadFormedException(ErrorConstants.MATRIX_BAD_FORMED_EXCEPTION);
        char[][] charMatrix = new char[dna.size()][dna.size()];
        try {
            for (int row = 0; row < dna.size(); row++) {
                if (!PATTERN.matcher(dna.get(row).toUpperCase()).matches()) {
                    log.error("Query invalid characters");
                    throw new InvalidCharException(ErrorConstants.INVALID_CHAR_EXCEPTION);
                }
                if (dna.get(row).length() != dna.size()) {
                    log.error("Query invalid matrix is not NxN");
                    throw new MatrixBadFormedNxNException(ErrorConstants.MATRIX_BAD_FORMED_NXN_EXCEPTION);
                }
                charMatrix[row] = dna.get(row).toCharArray();
            }
        } catch (IndexOutOfBoundsException ex) {
            log.info("Query invalid matrix is not NxN");
            throw new MatrixBadFormedException(ErrorConstants.MATRIX_BAD_FORMED_EXCEPTION);
        }
        return charMatrix;
    }
}
