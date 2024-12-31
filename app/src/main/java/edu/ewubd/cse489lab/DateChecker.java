package edu.ewubd.cse489lab;

public class DateChecker {
    public static boolean isValidDate(String date) {
        if (!date.matches("\\d{2}-\\d{2}-\\d{4}")) {
            return false;
        }
        String[] parts = date.split("-");
        int day = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);
        int year = Integer.parseInt(parts[2]);
        if (month < 1 || month > 12) {
            return false;
        }
        if (day < 1 || day > 31) {
            return false;
        }
        // Check for February and leap year
        if (month == 2) {
            if (isLeapYear(year)) {
                return day <= 29;
            } else {
                return day <= 28;
            }
        }
        // Check for months with 30 days
        if (month == 4 || month == 6 || month == 9 || month == 11) {
            return day <= 30;
        } else {
            return true;
        }
    }

    private static boolean isLeapYear(int year) {
        if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
            return true;
        }
        return false;
    }
}
