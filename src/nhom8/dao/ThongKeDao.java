package nhom8.dao;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import nhom8.helper.JdbcHelper;

public class ThongKeDao {

    public List<Object[]> doanhThuNam(String year) {
        List<Object[]> list = new ArrayList<>();
        String sql = "{Call sp_doanhthunam (?)}";

        try {
            ResultSet rs = null;
            try {
                rs = JdbcHelper.executeQuery(sql, year);
                while (rs.next()) {
                    Object[] row = {rs.getString("TenLoai"),
                        rs.getString("MaLX"),
                        rs.getString("SoLuong"),
                        rs.getDouble("DoanhThu"),
                        rs.getDouble("LoiNhuan")};
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

    public List<Object[]> doanhThuThang(String year, String month) {
        List<Object[]> list = new ArrayList<>();
        String sql = "{Call sp_doanhthuthang (?,?)}";
        try {
            ResultSet rs = null;
            try {
                rs = JdbcHelper.executeQuery(sql, year, month);
                while (rs.next()) {
                    Object[] row = {rs.getString("TenLoai"),
                        rs.getString("MaLX"),
                        rs.getString("SoLuong"),
                        rs.getDouble("DoanhThu"),
                        rs.getDouble("LoiNhuan")};
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
}
