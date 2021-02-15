package meli.challenge.mutants.repository;

import meli.challenge.mutants.service.dto.DnaData;
import meli.challenge.mutants.service.dto.DnaStats;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DnaDataRepository extends MongoRepository<DnaData,String> {
    @Aggregation(pipeline = {
            "{ $project: { mutantSum: { $cond: { if:  { $eq: [ $mutant, true ] }, then:1 , else: 0 } }, humanSum: { $cond: { if:  { $eq: [ $mutant, false ] }, then:1 , else: 0 } } }  }",
            "{ $group: {_id: 0,mutantSum: { $sum: $mutantSum },humanSum: { $sum: $humanSum }} }"
    })
    List<DnaStats> reporterDnaStats();
}
