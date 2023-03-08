final class Date {
    private final int year;
    public final int day;
    private final int month;

    public Date(int day, int month, int year) {
        this.year = year;
        this.day = day;
        this.month = month;
    } 

    public final int nextMonth() {
        return (month + 1) % 12;
    }
}