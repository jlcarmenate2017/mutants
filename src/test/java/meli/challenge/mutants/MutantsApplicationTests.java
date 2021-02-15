package meli.challenge.mutants;

import meli.challenge.mutants.service.errors.ExceptionTranslator;
import meli.challenge.mutants.service.MutantsAnalyzeService;
import meli.challenge.mutants.web.rest.MutantResource;
import meli.challenge.mutants.service.dto.RequestMutant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.IOException;
import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("dev")
class MutantsApplicationTests {

    @Autowired
    private MutantsAnalyzeService mutantsAnalyzeService;

    private MockMvc restMutantMockMvc;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @BeforeEach
    public void setup() {
        MutantResource mutantResource = new MutantResource(mutantsAnalyzeService);
        this.restMutantMockMvc = MockMvcBuilders.standaloneSetup(mutantResource)
                .setControllerAdvice(exceptionTranslator)
                .setMessageConverters(jacksonMessageConverter)
                .build();
    }

    @Test
    void test01IsHumanDNA() throws Exception {
        RequestMutant requestMutant = RequestMutant.builder().dna(Arrays.asList("ACCTATC", "CTCACTT", "ACGCTAT", "ACCTACC", "CAATTCC", "CACCAAT", "CAACAAT")).build();
        this.restMutantMockMvc.perform(
                mutantRequest(requestMutant)
        ).andExpect(status().isForbidden());
    }

    @Test
    void test01IsMutantDNA() throws Exception {
        RequestMutant requestMutant = RequestMutant.builder().dna(Arrays.asList(
                "ATGCGA",
                "CAGTGC",
                "TTATGT",
                "AGAAGG",
                "CCCCTA",
                "TCACTG")).build();
        this.restMutantMockMvc.perform(
                mutantRequest(requestMutant)
        ).andExpect(status().isOk());
    }

    @Test
    void test02IsMutantDNA() throws Exception {
        RequestMutant requestMutant = RequestMutant.builder().dna(Arrays.asList("AAAAGA", "CAGTCC", "TTATGT", "AGAAGG", "ACACTA", "TCACTG")).build();
        this.restMutantMockMvc.perform(
                mutantRequest(requestMutant)
        ).andExpect(status().isOk());
    }
    @Test
    void test03IsMutantDNA() throws Exception {
        RequestMutant requestMutant = RequestMutant.builder().dna(Arrays.asList(
                "ATGCGA",
                "CAGTAC",
                "TCATGT",
                "AGCAGG",
                "CACCTA",
                "TCACTG")).build();

        this.restMutantMockMvc.perform(
                mutantRequest(requestMutant)
        ).andExpect(status().isOk());
    }
    @Test
    void test04IsMutantDNA() throws Exception {
        RequestMutant requestMutant = RequestMutant.builder().dna(Arrays.asList(
                "ATGAGA",
                "CCATAC",
                "TACTCT",
                "AGCCGG",
                "CACCTA",
                "TCACTG")).build();

        this.restMutantMockMvc.perform(
                mutantRequest(requestMutant)
        ).andExpect(status().isOk());
    }

    @Test
    void test01MatrixInvalid() throws Exception {
        RequestMutant requestMutant = RequestMutant.builder().dna(Arrays.asList("AAAAGA", "CAGTC", "TTATG", "AGAAG", "ACACTA", "TCACTG")).build();
        this.restMutantMockMvc.perform(
                mutantRequest(requestMutant)
        )
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value("MATRIX BAD FORMED NXN EXCEPTION"))
                .andExpect(jsonPath("$.title").value("MATRIX BAD FORMED NXN EXCEPTION"))
                .andExpect(jsonPath("$.detail").value("MATRIX BAD FORMED NXN EXCEPTION"));
    }

    @Test
    void test02MatrixInvalid() throws Exception {
        RequestMutant requestMutant = RequestMutant.builder().dna(Arrays.asList("AAWAGA", "CAGTCW", "TTATGA", "AGAAGZ", "ACACTA", "TCACTG")).build();
        this.restMutantMockMvc.perform(
                mutantRequest(requestMutant)
        )
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value("INVALID CHAR IN THE MATRIX"))
                .andExpect(jsonPath("$.title").value("INVALID CHAR IN THE MATRIX"))
                .andExpect(jsonPath("$.detail").value("INVALID CHAR IN THE MATRIX"));
    }

    @Test
    void test03MatrixInvalid() throws Exception {
        RequestMutant requestMutant = RequestMutant.builder().dna(Arrays.asList("AAAAGA", "CAGTCA", "TTATGA", "AGAAGZ", "ACACTA")).build();
        this.restMutantMockMvc.perform(
                mutantRequest(requestMutant)
        )
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value("MATRIX BAD FORMED NXN EXCEPTION"))
                .andExpect(jsonPath("$.title").value("MATRIX BAD FORMED NXN EXCEPTION"))
                .andExpect(jsonPath("$.detail").value("MATRIX BAD FORMED NXN EXCEPTION"));
    }

    @Test
    void test01getStats() throws Exception {
        this.restMutantMockMvc.perform(
                statsRequest()
        ).andExpect(status().isOk());
    }

    private MockHttpServletRequestBuilder statsRequest() {
        return get("/stats")
                .contentType(MediaType.APPLICATION_JSON);
    }


    private MockHttpServletRequestBuilder mutantRequest(RequestMutant requestMutant) throws IOException {
        return post("/mutant")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(requestMutant));
    }

}
