package org.example;

import org.apache.poi.ss.formula.functions.Column;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Collectors;

public class ExcelExporter {
    private static String mapToString(Map<LocalDateTime, Action> map) {
        return map.entrySet().stream()
                .map(entry -> entry.getKey().toString() + ": " + entry.getValue().toString())
                .collect(Collectors.joining(",\n "));
    }
    public static void exportOrderToExcel(Order order) {
        Workbook workbook = new XSSFWorkbook();
        String filePath = "Room"+order.getRoomId()+"Order" + order.getId() + ".xlsx";
        Sheet sheet = workbook.createSheet("Order");
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("ACID");
        headerRow.createCell(1).setCellValue("Customer ID");
        headerRow.createCell(2).setCellValue("Customer Name");
        headerRow.createCell(3).setCellValue("Room ID");
        headerRow.createCell(4).setCellValue("AcCost");
        Row dataRow = sheet.createRow(1);
        dataRow.createCell(0).setCellValue(order.getId());
        dataRow.createCell(1).setCellValue(order.getCustomerId());
        dataRow.createCell(2).setCellValue(order.getCustomerName());
        dataRow.createCell(3).setCellValue(order.getRoomId());
        dataRow.createCell(4).setCellValue(order.serviceSlice.getCost());
        try (FileOutputStream outputStream = new FileOutputStream(filePath)) {
            workbook.write(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void exportOrderToExcelDetailed(Order order) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Order");
        String filePath = "Room"+order.getRoomId()+"DetailedOrder" + order.getId() + ".xlsx";
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("ID");
        headerRow.createCell(1).setCellValue("Customer ID");
        headerRow.createCell(2).setCellValue("Customer Name");
        headerRow.createCell(3).setCellValue("Room ID");
        headerRow.createCell(4).setCellValue("Actions");
        headerRow.createCell(5).setCellValue("AcCost");
        Row dataRow = sheet.createRow(1);
        dataRow.createCell(0).setCellValue(order.getId());
        dataRow.createCell(1).setCellValue(order.getCustomerId());
        dataRow.createCell(2).setCellValue(order.getCustomerName());
        dataRow.createCell(3).setCellValue(order.getRoomId());
        dataRow.createCell(5).setCellValue(order.serviceSlice.getCost());
        int i=2;
        Iterator<Map.Entry<LocalDateTime, Action>> iterator = order.actions.entrySet().iterator();
        if(iterator.hasNext()){
            Map.Entry<LocalDateTime, Action> entry = iterator.next();
            dataRow.createCell(4).setCellValue(entry.getKey().toString() + ": " + entry.getValue().toString());
        }
        while (iterator.hasNext()) {
            Map.Entry<LocalDateTime, Action> entry = iterator.next();
            Row row = sheet.createRow(i++);
            row.createCell(4).setCellValue(entry.getKey().toString() + ": " + entry.getValue().toString());
        }
        try (FileOutputStream outputStream = new FileOutputStream(filePath)) {
            workbook.write(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void exportOrderToExcelAccom(Order order)
    {
        order.CheckInFee=order.CheckInDays*ACService.FeePerDayPerRoom.get(order.getRoomId());
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Order");
        String filePath = "Room"+order.getRoomId()+"AccomOrder" + order.getId() + ".xlsx";
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("OrderID");
        headerRow.createCell(1).setCellValue("CustomerID");
        headerRow.createCell(2).setCellValue("CustomerName");
        headerRow.createCell(3).setCellValue("RoomID");
        headerRow.createCell(4).setCellValue("CheckInDays");
        headerRow.createCell(5).setCellValue("CheckInFee");
        Row dataRow = sheet.createRow(1);
        dataRow.createCell(0).setCellValue(order.getId());
        dataRow.createCell(1).setCellValue(order.getCustomerId());
        dataRow.createCell(2).setCellValue(order.getCustomerName());
        dataRow.createCell(3).setCellValue(order.getRoomId());
        dataRow.createCell(4).setCellValue(order.getCheckInDays());
        dataRow.createCell(5).setCellValue(order.CheckInFee);
        try (FileOutputStream outputStream = new FileOutputStream(filePath)) {
            workbook.write(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
