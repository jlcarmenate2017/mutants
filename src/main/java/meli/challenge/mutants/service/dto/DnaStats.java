package meli.challenge.mutants.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DnaStats {
    @JsonIgnore
    private String id;
    @Builder.Default
    @JsonProperty("count_mutant_dna")
    private int mutantSum = 0;
    @Builder.Default
    @JsonProperty("count_human_dna")
    private int humanSum = 0;
    @Builder.Default
    private BigDecimal ratio = BigDecimal.ZERO;
}
