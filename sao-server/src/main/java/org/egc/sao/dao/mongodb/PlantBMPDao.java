package org.egc.sao.dao.mongodb;

import org.egc.sao.domain.PlantBMP;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlantBMPDao extends MongoRepository<PlantBMP,String> {
}
