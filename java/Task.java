import java.util.HashMap;
import java.util.Map;

public class Task {
    private String name;
    private int totalHours;
    private Map<Integer, Integer> dailyWork = new HashMap<>(); //ключ - номер дня; значение - кол-во отработанных часов

    public Task(String name, int totalHours){
        if (totalHours < 1 || totalHours>16){
            throw new IllegalArgumentException("задача должна длиться от 1 до 16 часов!");
        }
        this.name = name;
        this.totalHours = totalHours;
    }

    public synchronized void addWorkedHours(int day, int hours){
        if (dailyWork.containsKey(day)) {
            dailyWork.put(day, dailyWork.get(day) + hours);
        } else{
            dailyWork.put(day, hours);
        }
    }

    public boolean isDone(){
        return getSpentHours() >= totalHours;
    }

    public int getSpentHours() {
        int total = 0;
        for (int hours : dailyWork.values()) {
            total += hours;
        }
        return total;
    }

    public int getRemainingHours() { //оставшиеся часы
        return Math.max(0, totalHours - getSpentHours());
    }

    public String getName() {
        return name;
    }
}
