package nhom8.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import nhom8.helper.JdbcHelper;
import nhom8.model.KhachHang;

public class KhachHangDao {

    public void insert(KhachHang model) {
        String sql = "INSERT INTO KhachHang (MaKH, TenKH, GioiTinh, DiaChi, DienThoai)VALUES( ?,  ?,  ?,  ?,  ?)";
        JdbcHelper.excuteUpdate(sql,
                model.getMaKH(),
                model.getTenKH(),
                model.isGioiTinh(),
                model.getDiaChi(),
                model.getSoDT());
    }

    public void update(KhachHang model) {
        String sql = "UPDATE KhachHang SET TenKH=?, GioiTinh=?, DiaChi=?, DienThoai=? WHERE  MaKH =  ?";
        JdbcHelper.excuteUpdate(sql,
                model.getTenKH(),
                model.isGioiTinh(),
                model.getDiaChi(),
                model.getSoDT(),
                model.getMaKH());
    }

    public void delete(String id) {
        String sql = "DELETE FROM KhachHang WHERE MaKH=?";
        JdbcHelper.excuteUpdate(sql, id);
    }

    public List<KhachHang> select() {
        String sql = "SELECT * FROM KhachHang";
        return select(sql);
    }

    public List<KhachHang> selectByKeyword(String keyword) {
        String sql = "SELECT * FROM KhachHang WHERE TenKH LIKE ?";
        return select(sql, "%" + keyword + "%");
    }

//    public List<KhachHang> selectByCourse(Integer makh) {
//        String sql = "SELECT * FROM KhachHang WHERE MaKH NOT IN (SELECT MaKH FROM HocVien WHERE MaKH=?)";
//        return select(sql, makh);
//    }
    public KhachHang findById(String manh) {
        String sql = "SELECT * FROM KhachHang WHERE MaKH=?";
        List<KhachHang> list = select(sql, manh);
        return list.size() > 0 ? list.get(0) : null;
    }

    private List<KhachHang> select(String sql, Object... args) {
        List<KhachHang> list = new ArrayList<>();
        try {
            ResultSet rs = null;
            try {
                rs = JdbcHelper.executeQuery(sql, args);
                while (rs.next()) {
                    KhachHang model = readFromResultSet(rs);
                    list.add(model);
                }
            } finally {
                rs.getStatement().getConnection().close();
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return list;
    }

    private KhachHang readFromResultSet(ResultSet rs) throws SQLException {
        KhachHang model = new KhachHang();
        model.setMaKH(rs.getString("MaKH"));
        model.setTenKH(rs.getString("TenKH"));
        model.setGioiTinh(rs.getBoolean("GioiTinh"));
        model.setDiaChi(rs.getString("DiaChi"));
        model.setSoDT(rs.getString("DienThoai"));

        return model;
    }
}
