package com.example.BurialSchemeRestApi.services;

import com.example.BurialSchemeRestApi.api.ResponseMessageList;
import com.example.BurialSchemeRestApi.enums.ResponseStatus;
import com.example.BurialSchemeRestApi.repositories.RoleRepo;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    RoleRepo roleRepo;

    public RoleService(RoleRepo roleRepo) {
        this.roleRepo = roleRepo;
    }

    public ResponseMessageList roles() {
        return ResponseMessageList.builder().data(roleRepo.findAll()).message(ResponseStatus.SUCCESS.name()).build();
    }
}
