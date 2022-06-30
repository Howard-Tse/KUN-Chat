package com.practice.chat.utils;

import com.practice.chat.dbconnector.DBHelper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class NumberUtil {
    private static String intArrayToString(int[] num) {
        String str = "";
        for(int i = 0; i < num.length; ++i) {
            str += num[i];
        }
        return str;
    }

    private static boolean isNumberRepeat(String number) throws SQLException {
        DBHelper helper = DBHelper.getInstance();

        String sql = """
                select *
                from TB_User
                where Number = '%s'
                """;
        sql = sql.formatted(number);

        try (ResultSet set = helper.getStatement().executeQuery(sql)) {
            return set.next();
        }
    }

    public static String generateNewNumber() throws SQLException {
        String newNumber;
        int[] num = new int[10];

        do {
            for (int i = 0; i < 10; ++i) {
                if (i == 0)
                    num[i] = (int) (Math.random() * 9) + 1;
                else
                    num[i] = (int) (Math.random() * 10);
            }
            newNumber = intArrayToString(num);
        } while (isNumberRepeat(newNumber));

        return newNumber;
    }
}
