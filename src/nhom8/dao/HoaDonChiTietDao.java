package nhom8.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import nhom8.helper.JdbcHelper;
import nhom8.model.HoaDonChiTiet;

public class HoaDonChiTietDao {

    public void insert(HoaDonChiTiet hdct) {
        String sql = "Insert into HoaDonChiTiet values(?,?,?)";
        JdbcHelper.excuteUpdate(sql, hdct.getMaHD(), hdct.getSoKhung(), hdct.getGia());
    }
    public  void deleteByMaHD(String maHD){
        String sql  = "Delete from HoaDonChiTiet where MaHD=?";
        JdbcHelper.excuteUpdate(sql, maHD);
    }
      public  void deleteBySoKhung(String soKhung){
        String sql  = "Delete from HoaDonChiTiet where SoKhung=?";
        JdbcHelper.excuteUpdate(sql, soKhung);
    }
    public List<HoaDonChiTiet> selectByMaHD(String maHD){
        String sql ="Select * from HoaDonChiTiet where MaHD=?";
        return  getListHoaDon(sql, maHD);
    }
    
    
    public List<HoaDonChiTiet> getListHoaDon(String sql, Object... args) {
        List<HoaDonChiTiet> list = new ArrayList<>();
        try {
            ResultSet rs = null;
            try {
                rs = JdbcHelper.executeQuery(sql, args);
                while (rs.next()) {
                    HoaDonChiTiet hdct = readHoaDonChiTietModel(rs);
                    list.add(hdct);
                }
            } finally {
                rs.getStatement().getConnection().close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    private HoaDonChiTiet readHoaDonChiTietModel(ResultSet rs) throws SQLException {
        HoaDonChiTiet hdct = new HoaDonChiTiet(rs.getString("MaHD"), rs.getString("SoKhung"),
                rs.getDouble("Gia"));
        return hdct;
    }
    
//    public static void main(String[] args) {
//        List<HoaDonChiTiet> list = new HoaDonChiTietDao().selectByMaHD("HD101");
//        System.out.println(list);
//    }
}
