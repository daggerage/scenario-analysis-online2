package org.egc.sao.dao.mongodb;

import org.egc.sao.domain.StructBMP;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StructBMPDao extends MongoRepository<StructBMP, String> {
    List<StructBMP> findAllBySubscenario(int subscenario);

    List<StructBMP> findAllById(int subscenario);
}