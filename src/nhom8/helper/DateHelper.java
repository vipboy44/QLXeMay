
package nhom8.helper;

import java.text.SimpleDateFormat;
import java.util.Date;


public class DateHelper {
    static final SimpleDateFormat DATE_FORMATER = new SimpleDateFormat("MM/dd/yyyy");
    
    /*
    Lấy thời gian hiện tại      
    return Date kết quả     
    */     
    public static Date now() {       
        return new Date();
    }
    
    /*
    Chuyển đổi String sang Date
    date là chuỗi cần chuyển.
    pattern là định dạng thời gian.
    return Date.
    */
    public static Date toDate(String date, String...pattern){
        try {
            if (pattern.length>0) {
                DATE_FORMATER.applyPattern(pattern[0]);
            }
            if (date==null) {
                return DateHelper.now();
            }
            return DATE_FORMATER.parse(date);
        } catch (Exception e) {
           // e.printStackTrace();
            throw new RuntimeException();
        }
    }
    /* Chuyển đổi từ Date sang String      
        date là Date cần chuyển đổi      
        pattern là định dạng thời gian      
         String kết quả      
    */
    public static String toString(Date date, String...pattern){
        if (pattern.length>0) {
            DATE_FORMATER.applyPattern(pattern[0]);
        }
        if (date==null) {
            date = DateHelper.now();
        }
        return DATE_FORMATER.format(date);        
    }
    
    
    /*
    Bổ sung số ngày vào thời gian.
    date thời gian hiện có.
    days số ngày cần bổ sung vào date.
    return Date kết quả.
    */
    public static Date addDays(Date date, int days){
        date.setTime(date.getTime()+days*24*60*60*1000);
        return date;
    }
    
    /*
    Bổ sung số ngày vào thời gian hiện tại.
    days số ngày cần bổ sung vào thời gian hiện tại.
    return Date kết quả.
    */
    public static Date add(int days){
        Date now = DateHelper.now();
        now.setTime(now.getTime()+days*24*60*60*1000);
        return now;
    }
   
//    public static boolean checkStringToDate(String date){
//        try {
//            Date date_real = DATE_FORMATER.parse(date);
//            System.out.println(DateHelper.toString(date_real));
//            return true;
//        } catch (ParseException ex) {
//            return false;
//        }
//    }
}
