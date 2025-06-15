import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WorkiWorki {
    private static final String FILE_NAME = "work_tracker.xlsx";
    public static void main(String[] args) {
        Excel excel = new Excel();

        try{
            Workbook workbook;
            try (FileInputStream file = new FileInputStream(FILE_NAME)) {
                workbook = new XSSFWorkbook(file);
            } catch (Exception e) {
                workbook = new XSSFWorkbook();

                Sheet inputSheet = workbook.createSheet("Сотрудники");
                Row header = inputSheet.createRow(0);
                header.createCell(0).setCellValue("Сотрудник");
                header.createCell(1).setCellValue("Задача1");
                header.createCell(2).setCellValue("Часы1");
            }

            List<Employee> employees = excel.readEmployees(workbook);

            for(int day = 1; day<=7; day++){
                System.out.println("\n--- День " + day + " ---");

                List<Thread> threads = new ArrayList<>();
                for (Employee emp : employees) {
                    final int currentDay = day;
                    Thread thread = new Thread(() -> {
                        emp.workDay(currentDay);
                        System.out.printf("%s: отработано %d ч, простой %d ч%n",
                                emp.getName(),
                                emp.getWorkedHours(currentDay),
                                emp.getIdleTime(currentDay));
                    });
                    threads.add(thread);
                    thread.start();
                }

                for (Thread thread : threads) {
                    thread.join();
                }
            }
            excel.saveResults(workbook, employees);

            try (FileOutputStream out = new FileOutputStream(FILE_NAME)) {
                workbook.write(out);
            }

            System.out.println("\nРезультаты сохранены в файл " + FILE_NAME);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
