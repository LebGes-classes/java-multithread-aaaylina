import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.ArrayList;
import java.util.List;

public class Excel {
    private static final int DAYS = 7;
    private static final String INPUT_SHEET = "Сотрудники";
    private static final String OUTPUT_SHEET = "Итоги";

    public List<Employee> readEmployees(Workbook workbook) throws Exception {
        List<Employee> employees = new ArrayList<>();
        Sheet sheet = workbook.getSheet(INPUT_SHEET);

        if (sheet == null) {
            throw new RuntimeException("Вкладка 'Сотрудники' не найдена");
        }

        for (Row row : sheet) {
            if (row.getRowNum() == 0) continue;

            String name = row.getCell(0).getStringCellValue();
            List<Task> tasks = new ArrayList<>();

            for (int i = 1; i < row.getLastCellNum(); i += 2) {
                Cell taskCell = row.getCell(i);
                Cell hoursCell = row.getCell(i + 1);

                if (taskCell != null && hoursCell != null) {
                    tasks.add(new Task(taskCell.getStringCellValue(), (int) hoursCell.getNumericCellValue()));
                }
            }
            employees.add(new Employee(name, tasks));
        }
        return employees;
    }

    public void saveResults(Workbook workbook, List<Employee> employees) throws Exception{
        int sheetIndex = workbook.getSheetIndex(OUTPUT_SHEET);
        if(sheetIndex != -1){
            workbook.removeSheetAt(sheetIndex);
        }

        Sheet sheet = workbook.createSheet(OUTPUT_SHEET);

        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("Сотрудник");
        for(int day = 1; day <= DAYS; day++){
            header.createCell(day).setCellValue("День " + day);
        }
        header.createCell(DAYS + 1).setCellValue("Всего часов");

        int rowNum = 1;
        for(Employee emp : employees){
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(emp.getName());

            int total = 0;
            for(int day = 1; day <= DAYS; day++){
                int hours = emp.getWorkedHours(day);
                row.createCell(day).setCellValue(hours);
                total += hours;
            }
            row.createCell(DAYS+1).setCellValue(total);
        }
    }

}
