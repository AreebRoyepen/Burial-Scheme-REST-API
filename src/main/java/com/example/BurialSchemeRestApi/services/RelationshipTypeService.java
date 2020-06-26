package com.example.BurialSchemeRestApi.services;

import com.example.BurialSchemeRestApi.api.ResponseMessageList;
import com.example.BurialSchemeRestApi.enums.ResponseStatus;
import com.example.BurialSchemeRestApi.repositories.RelationshipRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class RelationshipTypeService {

    Logger logger = LoggerFactory.getLogger(RelationshipTypeService.class);
    RelationshipRepo relationshipRepo;

    public RelationshipTypeService(RelationshipRepo relationshipRepo) {
        this.relationshipRepo = relationshipRepo;
    }

    public ResponseMessageList all() {
        return ResponseMessageList.builder().data(relationshipRepo.findAll()).message(ResponseStatus.SUCCESS.name()).build();
    }

}
