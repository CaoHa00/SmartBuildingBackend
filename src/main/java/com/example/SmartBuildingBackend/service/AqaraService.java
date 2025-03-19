package com.example.SmartBuildingBackend.service;

import org.springframework.stereotype.Service;
import java.util.Map;

@Service
public interface AqaraService {
    String sendRequestToAqara(Map<String, Object> requestBody) throws Exception;
}

    

    

