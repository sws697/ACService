package org.example;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

public class ExcelExporter {
    private String mapToString(Map<LocalDateTime, Action> map) {
        return map.entrySet().stream()
                .map(entry -> entry.getKey().toString() + ": " + entry.getValue().toString())
                .collect(Collectors.joining(",\n "));
    }
    public void exportOrderToExcel(Order order, String filePath) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Order");

        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("ID");
        headerRow.createCell(1).setCellValue("Last Date");
        headerRow.createCell(2).setCellValue("Status");
        headerRow.createCell(3).setCellValue("Customer ID");
        headerRow.createCell(4).setCellValue("Customer Name");
        headerRow.createCell(5).setCellValue("Room ID");
        headerRow.createCell(6).setCellValue("Cost");
        Row dataRow = sheet.createRow(1);
        dataRow.createCell(0).setCellValue(order.getId());
        dataRow.createCell(1).setCellValue(order.getLastDate().toString());
        dataRow.createCell(2).setCellValue(order.getStatus().toString());
        dataRow.createCell(3).setCellValue(order.getCustomerId());
        dataRow.createCell(4).setCellValue(order.getCustomerName());
        dataRow.createCell(5).setCellValue(order.getRoomId());
        dataRow.createCell(6).setCellValue(order.serviceSlice.getCost());
        try (FileOutputStream outputStream = new FileOutputStream(filePath)) {
            workbook.write(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void exportOrderToExcelDetailed(Order order, String filePath) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Order");

        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("ID");
        headerRow.createCell(1).setCellValue("Last Date");
        headerRow.createCell(2).setCellValue("Status");
        headerRow.createCell(3).setCellValue("Customer ID");
        headerRow.createCell(4).setCellValue("Customer Name");
        headerRow.createCell(5).setCellValue("Room ID");
        headerRow.createCell(6).setCellValue("Actions");
        headerRow.createCell(7).setCellValue("Cost");
        Row dataRow = sheet.createRow(1);
        dataRow.createCell(0).setCellValue(order.getId());
        dataRow.createCell(1).setCellValue(order.getLastDate().toString());
        dataRow.createCell(2).setCellValue(order.getStatus().toString());
        dataRow.createCell(3).setCellValue(order.getCustomerId());
        dataRow.createCell(4).setCellValue(order.getCustomerName());
        dataRow.createCell(5).setCellValue(order.getRoomId());
        dataRow.createCell(6).setCellValue(mapToString(order.getActions()));
        dataRow.createCell(7).setCellValue(order.serviceSlice.getCost());
        try (FileOutputStream outputStream = new FileOutputStream(filePath)) {
            workbook.write(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
