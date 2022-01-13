/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nhom8.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import nhom8.helper.JdbcHelper;
import nhom8.model.HangXe;
import nhom8.model.KhachHang;
import nhom8.model.LoaiXe;

/**
 *
 * @author COMPUTER
 */
public class HangVaLoaiXeDao {

    public void insert(HangXe model) {
        String sql = "INSERT INTO HangXe (MaHX, TenHX)VALUES( ?,  ?)";
        JdbcHelper.excuteUpdate(sql,
                model.getMaHX(),
                model.getTenHX());

    }

    public void update(HangXe model) {
        String sql = "UPDATE HangXe SET TenHX=? WHERE  MaHX =  ?";
        JdbcHelper.excuteUpdate(sql,
                model.getTenHX(),
                model.getMaHX());
    }

    public void delete(String idHX) {
        String sql = "DELETE FROM HangXe WHERE MaHX=?";
        JdbcHelper.excuteUpdate(sql, idHX);
    }

    public List<HangXe> select() {
        String sql = "SELECT * FROM HangXe";
        return select(sql);
    }

    public List<HangXe> selectByKeyword(String keyword) {
        String sql = "SELECT * FROM HangXe WHERE TenHX LIKE ?";
        return select(sql, "%" + keyword + "%");
    }

//    public List<KhachHang> selectByCourse(Integer makh) {
//        String sql = "SELECT * FROM KhachHang WHERE MaKH NOT IN (SELECT MaKH FROM HocVien WHERE MaKH=?)";
//        return select(sql, makh);
//    }
    public HangXe findById(String mahx) {
        String sql = "SELECT * FROM HangXe WHERE MaHX=?";
        List<HangXe> list = select(sql, mahx);
        return list.size() > 0 ? list.get(0) : null;
    }

    private List<HangXe> select(String sql, Object... args) {
        List<HangXe> list = new ArrayList<>();
        try {
            ResultSet rs = null;
            try {
                rs = JdbcHelper.executeQuery(sql, args);
                while (rs.next()) {
                    HangXe model = readFromResultSet(rs);
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

    private HangXe readFromResultSet(ResultSet rs) throws SQLException {
        HangXe model = new HangXe();
        model.setMaHX(rs.getString("MaHX"));
        model.setTenHX(rs.getString("TenHX"));

        return model;
    }
//-------------------------------------------------------------------//
    

}
