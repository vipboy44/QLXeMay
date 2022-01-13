
package nhom8.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import nhom8.helper.JdbcHelper;
import nhom8.model.PhieuNhapHang;

public class PhieuNhapHangDao {
    
    
    public void insert(PhieuNhapHang pnh){
        String sql = "Insert into PhieuNhapHang values(?,?,?,?)";
        JdbcHelper.excuteUpdate(sql, pnh.getMaPN(),pnh.getMaNV(),
                                pnh.getNhaCungCap(),pnh.getNgayNhap());
    }
     public void update(PhieuNhapHang pnh){
        String sql = "Update PhieuNhapHang set MaNV=?, NhaCungCap=?,NgayNhap=? where MaPN=?";
        JdbcHelper.excuteUpdate(sql,pnh.getMaNV(),
                                pnh.getNhaCungCap(),pnh.getNgayNhap(),pnh.getMaPN());
    }
    
    public void delete(String maPN){
        String sql = "Delete from PhieuNhapHang where MaPN=?";
        JdbcHelper.excuteUpdate(sql, maPN);
    }
    
    public PhieuNhapHang selectByMaPN(String maPN){
        String sql = "Select * from PhieuNhapHang where MaPN = ?";
        List<PhieuNhapHang> list = select(sql, maPN);
        return list.size()>0? list.get(0):null;
    }
    
    public List<PhieuNhapHang> selectAll(){
        String sql ="Select * from PhieuNhapHang";
         return select(sql);
    }
    
      public List<PhieuNhapHang> selectLikeMaPN(String maPN){
        String sql ="Select * from PhieuNhapHang where MaPN like ?";
        return  select(sql, "%"+maPN+"%");     
    }
    
    public List<PhieuNhapHang> select(String sql, Object...arg){
        List<PhieuNhapHang> list = new ArrayList<>();
        try {
            ResultSet rs = null;
            try {
                rs = JdbcHelper.executeQuery(sql, arg);
                while (rs.next()) {                    
                    PhieuNhapHang pnh = readPhieuNhapHang(rs);
                    list.add(pnh);
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
    
    
    public PhieuNhapHang readPhieuNhapHang(ResultSet rs) throws SQLException{
        PhieuNhapHang pnh = new PhieuNhapHang(rs.getString("MaPN"), rs.getString("MaNV"),
                            rs.getString("NhaCungCap"), rs.getDate("NgayNhap"));                           
       return pnh;
    }
}
