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
import nhom8.model.Xe;

/**
 *
 * @author Asus
 */
public class XeDao {

    public void nhapXe(Xe xe) {
        String sql = "Insert into Xe(SoKhung, MaPN , MaLX, SoMay, MauXe, PhanKhoi, GiaNhap, GiaBan, Anh, TrangThai) "
                + " values(?,?,?,?,?,?,?,?,?,?)";
        JdbcHelper.excuteUpdate(sql, xe.getSoKhung(), xe.getMaPN(), xe.getMaLX(),
                xe.getSoMay(), xe.getMauXe(), xe.getPhanKhoi(),
                xe.getGiaNhap(), xe.getGiaBan(), xe.getAnh(), xe.getTrangThai());
    }

    public void updateXeNhap(Xe xe) {
        String sql = "Update Xe set MaLX=?, SoMay=?, MauXe=?, PhanKhoi=?, GiaNhap=?, Anh =?"
                + " Where SoKhung=?";
        JdbcHelper.excuteUpdate(sql, xe.getMaLX(),
                xe.getSoMay(), xe.getMauXe(), xe.getPhanKhoi(),
                xe.getGiaNhap(), xe.getAnh(), xe.getSoKhung());
    }

    public void delete(String soKhung) {
        String sql = "Delete from Xe Where SoKhung =?";
        JdbcHelper.excuteUpdate(sql, soKhung);
    }

//    public void update(Xe model) {
//        String sql = "UPDATE Xe SET MaPN=?, MaLX=?, SoMay=?, MauXe=?, PhanKhoi=?, GiaNhap=?, GiaBan=?, Anh=?, TrangThai=? WHERE SoKhung=?";
//        JdbcHelper.excuteUpdate(sql,
//                model.getMaPN(),
//                model.getMaLX(),
//                model.getSoMay(),
//                model.getMauXe(),
//                model.getPhanKhoi(),
//                model.getGiaNhap(),
//                model.getGiaBan(),
//                model.getAnh(),
//                model.getTrangThai(),
//                model.getSoKhung()
//        );
//    }
    public void update(Xe model) {
        String sql = "{Call sp_updateXe (?,?,?,?,?,?,?,?,?,?)}";
        JdbcHelper.excuteUpdate(sql,
                model.getSoKhung(),
                model.getMaPN(),
                model.getMaLX(),
                model.getSoMay(),
                model.getMauXe(),
                model.getPhanKhoi(),
                model.getGiaNhap(),
                model.getGiaBan(),
                model.getAnh(),
                model.getTrangThai()
        );
    }

    public List<Xe> selectByMaPN(String maPN) {
        String sql = "Select *  from Xe where MaPN =?";
        return select(sql, maPN);
    }

    public void updateTrangThai(int trangthai, String sokhung) {
        String sql = "Update Xe SET TrangThai = ? where SoKhung =?";
        JdbcHelper.excuteUpdate(sql, trangthai, sokhung);
    }

    public List<Xe> select(String sql, Object... args) {
        List<Xe> list = new ArrayList<>();
        try {
            ResultSet rs = null;
            try {
                rs = JdbcHelper.executeQuery(sql, args);
                while (rs.next()) {
                    Xe xemay = readHoaDonModel(rs);
                    list.add(xemay);
                }
            } finally {
                rs.getStatement().getConnection().close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            //throw new RuntimeException();
        }
        return list;
    }

    public List<Xe> selectByKeyword(String keyword) {
        String sql = "SELECT * FROM XE WHERE SoKhung LIKE ?";
        return select(sql, "%" + keyword + "%");
    }

    public Xe findById(String SK) {
        String sql = "SELECT * FROM Xe WHERE SoKhung=?";
        List<Xe> list = select(sql, SK);
        return list.size() > 0 ? list.get(0) : null;
    }

    public Xe findXeBySoKhung_1(String soKhung) {
        Xe x = null;
        String sql = "select * from xe where SoKhung=?";
        try {
            ResultSet rs = JdbcHelper.executeQuery(sql, soKhung);
            while (rs.next()) {
                x = readHoaDonModel(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // throw new RuntimeException();
        }
        return x;
    }

    private Xe readHoaDonModel(ResultSet rs) throws SQLException {
        Xe x = new Xe(rs.getString("SoKhung"), rs.getString("MaPN"), rs.getString("MaLX"),
                rs.getString("SoMay"), rs.getString("MauXe"), rs.getString("PhanKhoi"),
                rs.getDouble("GiaNhap"), rs.getDouble("GiaBan"), rs.getString("Anh"),
                rs.getInt("TrangThai"));
        return x;
    }
//    public static void main(String[] args) {
//        List<Xe> list = new XeDao().selectByMaPN("PN001");
//        System.out.println(list);
//    }
}
