package org.egc.sao.service;

import org.egc.sao.domain.ArealStructManagement;

import java.util.List;

public interface BMPService {
    List<ArealStructManagement> findAll();

    List<ArealStructManagement> findAllBySubscenario(int subscenario);
}
