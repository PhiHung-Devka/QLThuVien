/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
/**
 *
 * @author DIEUMY
 */
public class XDate {
    static final SimpleDateFormat DATE_FORMATER = new SimpleDateFormat("yyyy-MM-dd");

    public static String toString(Date date, String... pattern) {
        if (pattern.length > 0) {
            DATE_FORMATER.applyPattern(pattern[0]);
        }
        if (date == null) {
            date = new Date();
        }
        return DATE_FORMATER.format(date);
    }
    
    public static Date toDate(String date, String... pattern) {
        try {
            if (pattern.length > 0) {
                DATE_FORMATER.applyPattern(pattern[0]);
            
            }
            return DATE_FORMATER.parse(date);
        } catch (ParseException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    public static  int SoNgay(Date n1, Date n2){
        long mili1 = n1.getTime();
        long mili2 = n2.getTime();
        
        long mili = Math.abs(mili1-mili2);
        
        int songay = (int) (mili/(24*60*60*1000));
        return songay;
    }
}
