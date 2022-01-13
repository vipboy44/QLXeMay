
package nhom8.dao;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import nhom8.helper.JdbcHelper;


public class InDao {
        public List<Object[]> selectHoaDon(String maHD) {
        List<Object[]> list = new ArrayList<>();
        String sql = "{Call sp_printhoadon (?)}";

        try {
            ResultSet rs = null;
            try {
                rs = JdbcHelper.executeQuery(sql, maHD);
                while (rs.next()) {
                    Object[] row = {rs.getString("TenLoai"),
                        rs.getString("SoKhung"),
                        rs.getDouble("GiaBan"),
                        rs.getString("NhanVien"),
                        rs.getDate("NgayBan")};
                    list.add(row);
                }
            } finally {
                rs.getStatement().getConnection().close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
        
        
        
//    public static void main(String[] args) {
//        System.out.println(new InDao().printHoaDon("HD101"));
//    }
}
