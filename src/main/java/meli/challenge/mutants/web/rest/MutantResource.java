package meli.challenge.mutants.web.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import meli.challenge.mutants.service.MutantsAnalyzeService;
import meli.challenge.mutants.service.dto.RequestMutant;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RequiredArgsConstructor
@RestController
public class MutantResource {
    private final MutantsAnalyzeService mutantsAnalyzeService;
    @PostMapping(value = "/mutant", consumes = "application/json")
    public ResponseEntity mutant(@RequestBody RequestMutant data) {
        log.info("Query verify mutant started with {} ",data.getDna());
        if (mutantsAnalyzeService.isMutant(data.getDna())){
            log.info("Query is mutant");
            return new ResponseEntity<>(HttpStatus.OK);
        }
        log.error("Query is not mutant");
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @GetMapping(value = "/stats")
    public ResponseEntity stats() {
        log.info("Check stats of mutants {} ");
        return ResponseEntity.ok().body(mutantsAnalyzeService.reporterDnaStats());
    }
}
