package nhom8.helper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JdbcHelper {

    private static String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    private static String dburl = "jdbc:sqlserver://localhost;database=DuAn1_Nhom8";
    private static String username = "sa";
    private static String password = "songlong";

    /**
     * driver
     */
    static {
        try {
            Class.forName(driver);//nạp kết nối jdbc
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
            //throw new RuntimeException(ex); 
        }
    }

    /*
     * Xây dựng  PreparedStatement 
     *  sql là câu lệnh SQL chứa có thể chứa tham số. Nó có thể là một lời gọi thủ tục lưu 
     *  args là danh sách các giá trị được cung cấp cho các tham số trong câu lệnh sql 
     * return PreparedStatement tạo được 
     * throws java.sql.SQLException lỗi sai cú pháp 
     */
    public static PreparedStatement preparedStatement(String sql, Object... args) throws SQLException {
        Connection connection = DriverManager.getConnection(dburl, username, password);
        PreparedStatement pstm = null;
        if (sql.trim().startsWith("{")) {//câu lệnh sql bắt đầu { 
            pstm = connection.prepareCall(sql);
        } else {
            pstm = connection.prepareStatement(sql);
        }
        for (int i = 0; i < args.length; i++) {
            pstm.setObject(i + 1, args[i]);    //.giống như pstm.setString(1,value).
        }
        return pstm;
    }

    /*
     * Thực hiện câu lệnh SQL thao tác (INSERT, UPDATE, DELETE) hoặc thủ tục lưu thao tác dữ liệu     
     * @param sql là câu lệnh SQL chứa có thể chứa tham số. Nó có thể là một lời gọi thủ tục lưu    
     * @param args là danh sách các giá trị được cung cấp cho các tham số trong câu lệnh sql 
     */
    public static void excuteUpdate(String sql, Object... args) {
        try {
            PreparedStatement stmt = preparedStatement(sql, args);
            try {
                stmt.executeUpdate();
            } finally {
                stmt.getConnection().close();
            }
        } catch (SQLException e) {          
            throw new RuntimeException();
        }
    }

    /*
     * Thực hiện câu lệnh SQL truy vấn (SELECT) hoặc thủ tục lưu truy vấn dữ liệu 
     * sql là câu lệnh SQL chứa có thể chứa tham số. Nó có thể là một lời gọi thủ tục lưu 
     *  args là danh sách các giá trị được cung cấp cho các tham số trong câu lệnh sql 
     */
    public static ResultSet executeQuery(String sql, Object... args) {
        try {
            PreparedStatement stmt = preparedStatement(sql, args);
            return stmt.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
