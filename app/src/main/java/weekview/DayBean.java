package weekview;

public class DayBean {
    private int day;
    private int month;
    private int year;
    // 是否为当前月
    private boolean currentMonth;
    // 是否为今天
    private boolean currentDay;
    private boolean hastask;
    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public boolean isCurrentMonth() {
        return currentMonth;
    }

    public void setCurrentMonth(boolean currentMonth) {
        this.currentMonth = currentMonth;
    }

    public boolean isCurrentDay() {
        return currentDay;
    }

    public void setCurrentDay(boolean currentDay) {
        this.currentDay = currentDay;
    }

    public boolean isHastask() {
        return hastask;
    }

    public void setHastask(boolean hastask) {

        this.hastask = hastask;
    }
}
