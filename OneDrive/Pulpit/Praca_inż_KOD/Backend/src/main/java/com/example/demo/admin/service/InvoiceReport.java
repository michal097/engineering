package com.example.demo.admin.service;

import com.example.demo.model.Invoice;
import com.example.demo.mongoRepo.InvoiceRepo;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Service
@Slf4j
public class InvoiceReport {

    private final InvoiceRepo invoiceRepo;
    @Getter
    private final String PATH = "src/main/resources/invoices.xlsx";

    @Autowired
    public InvoiceReport(InvoiceRepo invoiceRepo) {
        this.invoiceRepo = invoiceRepo;
    }

    public void generateReport(String from, String to) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Invoices report");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d-MM-yyy");
        List<Invoice> invoices = invoiceRepo.findAll()
                .stream()
                .filter(p -> LocalDate.parse(p.getInvoiceDate())
                        .isAfter(LocalDate.parse(from, formatter).minusDays(1)) && LocalDate.parse(p.getInvoiceDate()).isBefore(LocalDate.parse(to, formatter).plusDays(1)))
                .collect(Collectors.toList());
        int counter = 2;

        Map<String, Object[]> data = new TreeMap<>();
        data.put("1", new Object[]{"Invoice number",
                "Owner name",
                "Owner surname",
                "NIP",
                "Costs",
                "Bank account",
                "Online invoice",
                "Invoice date",
                "Client type"});

        for (Invoice invoice : invoices) {
            data.put(String.valueOf(counter),
                    new Object[]{
                            invoice.getFvNumber(),
                            invoice.getInvName(),
                            invoice.getInvSurname(),
                            invoice.getNIP(),
                            invoice.getCosts(),
                            invoice.getBankAccNumber(),
                            invoice.getInvoiceURL(),
                            invoice.getInvoiceDate(),
                            invoice.getClientType()});
            counter++;
        }

        Set<String> keySet = data.keySet();
        int rownum = 0;

        for (String key : keySet) {
            Row row = sheet.createRow(rownum++);
            Object[] objArray = data.get(key);

            int cellnum = 0;

            for (Object object : objArray) {

                Cell cell = row.createCell(cellnum++);
                if (object instanceof String)
                    cell.setCellValue((String) object);
                else if (object instanceof Integer)
                    cell.setCellValue((Integer) object);
                else if (object instanceof Double)
                    cell.setCellValue((Double) object);
            }
        }

        try (FileOutputStream fileOutputStream = new FileOutputStream(new File(PATH))) {
            for (int i = 0; i < 9; i++) {
                sheet.autoSizeColumn(i);
            }
            workbook.write(fileOutputStream);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
