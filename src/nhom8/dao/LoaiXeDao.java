package nhom8.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import nhom8.helper.JdbcHelper;
import nhom8.model.LoaiXe;

public class LoaiXeDao {
     public void insert(LoaiXe model) {
        String sql = "INSERT INTO LoaiXe (MaLX, MaHX,Tenloai)VALUES( ?,  ?, ?)";
        JdbcHelper.excuteUpdate(sql,
                model.getMaLX(),
                model.getMaHX(),
                model.getTenLoai());

    }

    public void update(LoaiXe model) {
        String sql = "UPDATE LoaiXe SET MaHX=?,Tenloai=? WHERE  MaLX =  ?";
        JdbcHelper.excuteUpdate(sql,
                model.getMaHX(),
                model.getTenLoai(),
                model.getMaLX());
    }

    public void delete(String idLX) {
        String sql = "DELETE FROM LoaiXe WHERE MaLX=?";
        JdbcHelper.excuteUpdate(sql, idLX);
    }
      
    public List<LoaiXe> select() {
       String sql = "Select * from LoaiXe";
       return select(sql);
    }

    public LoaiXe selectByID(String MaLX){
        String sql = "Select * from LoaiXe where MaLX=?";
        List<LoaiXe> list = select(sql, MaLX);
        return list.size()>0?list.get(0):null;
    }
    
      public List<LoaiXe> selectByKeyword(String keyword) {
        String sql = "SELECT * FROM LoaiXe WHERE Tenloai LIKE ?";
        return select(sql, "%" + keyword + "%");
    }
    
    public List<LoaiXe> select(String sql, Object... args) {
        List<LoaiXe> list = new ArrayList<>();
        try {
            ResultSet rs = null;
            try {
                rs = JdbcHelper.executeQuery(sql, args);
                while (rs.next()) {
                    LoaiXe lx = readLoaiXe(rs);
                    list.add(lx);
                }
            } finally {
                rs.getStatement().getConnection().close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            //throw new RuntimeException();
        }
        return list;
    }

    public LoaiXe readLoaiXe(ResultSet rs) throws SQLException {
        LoaiXe loaiXe = new LoaiXe(rs.getString("MaLX"),
                rs.getString("MaHX"),
                rs.getString("Tenloai"));
        return loaiXe;
    }
    
    
}
