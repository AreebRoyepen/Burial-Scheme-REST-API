package com.example.BurialSchemeRestApi.services;

import com.example.BurialSchemeRestApi.models.Member;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportService {

    public byte[] report(Member member, List list) throws FileNotFoundException, JRException {

        File file = ResourceUtils.getFile("classpath:Statement.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(list);

        Map<String, Object> parameters = new HashMap();
        parameters.put("memberName", member.getName() + " " + member.getSurname());
        parameters.put("address1", member.getAddress());
        parameters.put("address2", member.getArea());
        //todo postal code
        parameters.put("address3", "7460");
        parameters.put("joinDate", member.getDOE().toString());
        parameters.put("memberNo",Long.toString(member.getID()));

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,parameters, dataSource);

        byte [] bytes = JasperExportManager.exportReportToPdf(jasperPrint);

        return bytes;

    }

}
