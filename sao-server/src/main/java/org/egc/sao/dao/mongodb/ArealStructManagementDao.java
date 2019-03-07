package org.egc.sao.dao.mongodb;

import org.bson.types.ObjectId;
import org.egc.sao.domain.ArealStructManagement;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArealStructManagementDao extends MongoRepository<ArealStructManagement, ObjectId> {
    List<ArealStructManagement> findAllBySubscenario(int subscenario);
}