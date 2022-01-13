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
import nhom8.model.NhanVien;

/**
 *
 * @author COMPUTER
 */
public class NhanVienDao {
    public void insert(NhanVien model) {
        String sql = "INSERT INTO NhanVien (MaNV, TenNV, MatKhau, ChucVu, GioiTinh, DiaChi, DienThoai, NgaySinh, Hinh) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try{
        JdbcHelper.excuteUpdate(sql,
                model.getMaNV(),
                model.getTenNV(),
                model.getMatKhau(),
                model.isChucVu(),
                model.isGioiTinh(),
                model.getDiaChi(),
                model.getsDT(),
                model.getNgaySinh(),
                model.getHinh());
        } catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public void update(NhanVien model) {
        String sql = "UPDATE NhanVien SET TenNV=?, MatKhau=?, ChucVu=?, GioiTinh=?, DiaChi=?, DienThoai=?, NgaySinh=?, Hinh=? WHERE MaNV=?";
        JdbcHelper.excuteUpdate(sql,
                model.getTenNV(),
                model.getMatKhau(),
                model.isChucVu(),
                model.isGioiTinh(),
                model.getDiaChi(),
                model.getsDT(),
                model.getNgaySinh(),
                model.getHinh(),
                model.getMaNV());
    }
//    public void updateNV(NhanVien nvmodel) {
//        String sql = "UPDATE NhanVien SET TenNV=?, MatKhau=?, ChucVu=? WHERE MaNV=?";
//        JdbcHelper.excuteUpdate(sql,
//                nvmodel.getTenNV(),
//                nvmodel.getMatKhau(),
//                nvmodel.isChucVu(),
//                nvmodel.getMaNV());
//    }

    public void delete(String MaNV) {
        String sql = "DELETE FROM NhanVien WHERE MaNV=?";
        JdbcHelper.excuteUpdate(sql, MaNV);
    }

    public List<NhanVien> select() {
        String sql = "SELECT * FROM NhanVien";
        return select(sql);
    }

    public NhanVien findById(String manv) {
        String sql = "SELECT * FROM NhanVien WHERE MaNV=?";
        List<NhanVien> list = select(sql, manv);
        return list.size() > 0 ? list.get(0) : null;
    }

    private List<NhanVien> select(String sql, Object... args) {
        List<NhanVien> list = new ArrayList<>();
        try {
            ResultSet rs = null;
            try {
                rs = JdbcHelper.executeQuery(sql, args);
                while (rs.next()) {
                    NhanVien model = readFromResultSet(rs);
                    list.add(model);
                }
            } finally {
                rs.getStatement().getConnection().close();
            }
        } catch (SQLException ex) {
             ex.printStackTrace();
           // throw new RuntimeException(ex);
        }
        return list;
    }

    private NhanVien readFromResultSet(ResultSet rs) throws SQLException {
        NhanVien model = new NhanVien();
        model.setMaNV(rs.getString("MaNV"));
        model.setTenNV(rs.getString("TenNV"));
        model.setMatKhau(rs.getString("MatKhau"));
        model.setChucVu(rs.getBoolean("ChucVu"));
        model.setGioiTinh(rs.getBoolean("Gioitinh"));
        model.setDiaChi(rs.getString("DiaChi"));
        model.setsDT(rs.getString("DienThoai"));
        model.setNgaySinh(rs.getDate("NgaySinh"));
        model.setHinh(rs.getString("Hinh"));
        
       
        return model;
    }
     public List<NhanVien> selectByKeyword(String keyword) {
        String sql = "SELECT * FROM NhanVien WHERE TenNV LIKE ?";
        return select(sql, "%" + keyword + "%");
    }
    
}
