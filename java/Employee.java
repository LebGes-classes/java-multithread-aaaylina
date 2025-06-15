import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Employee {
    private String name;
    private List<Task> tasks;
    private Map<Integer, Integer> idleTimeByDay = new HashMap<>(); // ключ - день; значение - часы простоя в данный день

    public Employee(String name, List<Task> tasks){
        this.name = name;
        this.tasks = tasks;
    }

    public void workDay(int day){
        int remainingHours = 8;

        for(Task task:tasks){
            if(task.isDone()) continue;

            int hoursToWork = Math.min(remainingHours, task.getRemainingHours());
            task.addWorkedHours(day, hoursToWork);
            remainingHours -= hoursToWork;

            if (remainingHours==0) break;
        }

        idleTimeByDay.put(day, remainingHours);
    }

    public int getIdleTime(int day) {
        return idleTimeByDay.getOrDefault(day, 0);
    }

    public int getWorkedHours(int day) {
        return 8 - getIdleTime(day);
    }

    public String getName() {
        return name;
    }
}
