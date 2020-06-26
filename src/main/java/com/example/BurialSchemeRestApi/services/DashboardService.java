package com.example.BurialSchemeRestApi.services;

import com.example.BurialSchemeRestApi.api.ResponseMessageObject;
import com.example.BurialSchemeRestApi.enums.ResponseStatus;
import com.example.BurialSchemeRestApi.util.UtilClass;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {

    UtilClass utilClass;


    public DashboardService(UtilClass utilClass) {
        this.utilClass = utilClass;
    }

    public ResponseMessageObject dashboard(){

        return ResponseMessageObject.builder().data(utilClass.getDashboardDetails()).message(ResponseStatus.SUCCESS.name()).build();

    }

}
