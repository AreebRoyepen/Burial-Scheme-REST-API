package com.example.BurialSchemeRestApi.reports;

import com.example.BurialSchemeRestApi.models.Claim;
import com.example.BurialSchemeRestApi.models.Member;
import com.example.BurialSchemeRestApi.util.UtilClass;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ReportGenerator {

    UtilClass utilClass;

    public ReportGenerator(UtilClass utilClass) {
        this.utilClass = utilClass;
    }

    public byte[] generateStatement(Member member, List list) throws FileNotFoundException, JRException {

        File file = ResourceUtils.getFile("classpath:Statement.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(list);

        Map<String, Object> parameters = new HashMap();
        parameters.put("memberName", member.getName() + " " + member.getSurname());
        parameters.put("address1", member.getAddress());
        parameters.put("address2", member.getArea());
        parameters.put("address3", member.getPostalCode());
        parameters.put("joinDate", utilClass.formatDate(member.getDOE()));
        parameters.put("memberNo",Long.toString(member.getID()));

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,parameters, dataSource);

        byte [] bytes = JasperExportManager.exportReportToPdf(jasperPrint);

        return bytes;

    }

    public byte[] generateClaimCSV(List<Claim> claims) throws IOException {
        String CSV_SEPARATOR = ",";

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(stream, "UTF-8"));

        for (Claim claim : claims){

            StringBuffer oneLine = new StringBuffer();
            oneLine.append(claim.getBurialPlace());
            oneLine.append(CSV_SEPARATOR);
            oneLine.append(claim.getBuriedDate());
            oneLine.append(CSV_SEPARATOR);
            oneLine.append(claim.getClaimDate());
            oneLine.append(CSV_SEPARATOR);
            oneLine.append(claim.getDeathDate());
            bw.write(oneLine.toString());
            bw.newLine();
        }
        bw.flush();
        bw.close();

        // To convert it to a byte[] - simply use
        final byte[] bytes = stream.toByteArray();

        return bytes;
    }

    public byte[] generateMemberCSV(List<Member> members) throws IOException {
        String CSV_SEPARATOR = ",";

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(stream, "UTF-8"));
        for (Member member : members){

            StringBuffer oneLine = new StringBuffer();
            oneLine.append(member.getName());
            oneLine.append(CSV_SEPARATOR);
            oneLine.append(member.getSurname());
            oneLine.append(CSV_SEPARATOR);
            oneLine.append(member.getDOB());
            oneLine.append(CSV_SEPARATOR);
            oneLine.append(member.getDOE());
            oneLine.append(CSV_SEPARATOR);
            oneLine.append(member.getAddress());
            oneLine.append(CSV_SEPARATOR);
            oneLine.append(member.getArea());
            oneLine.append(CSV_SEPARATOR);
            oneLine.append(member.getCellNumber());
            oneLine.append(CSV_SEPARATOR);
            oneLine.append(member.getHomeNumber());
            oneLine.append(CSV_SEPARATOR);
            oneLine.append(member.getWorkNumber());
            oneLine.append(CSV_SEPARATOR);
            bw.write(oneLine.toString());
            bw.newLine();
        }
        bw.flush();
        bw.close();

        // To convert it to a byte[] - simply use
        final byte[] bytes = stream.toByteArray();

        return bytes;
    }

}
