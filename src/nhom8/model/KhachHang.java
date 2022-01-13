
package nhom8.model;


public class KhachHang {
    String maKH;
    String tenKH;
    boolean gioiTinh;
    String diaChi;
    String soDT;

    public KhachHang(String maKH, String tenKH, boolean gioiTinh, String diaChi, String soDT) {
        this.maKH = maKH;
        this.tenKH = tenKH;
        this.gioiTinh = gioiTinh;
        this.diaChi = diaChi;
        this.soDT = soDT;
    }

    public KhachHang() {
    }

    public String getMaKH() {
        return maKH;
    }

    public String getTenKH() {
        return tenKH;
    }

    public boolean isGioiTinh() {
        return gioiTinh;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public String getSoDT() {
        return soDT;
    }

    public void setMaKH(String maKH) {
        this.maKH = maKH;
    }

    public void setTenKH(String tenKH) {
        this.tenKH = tenKH;
    }

    public void setGioiTinh(boolean gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public void setSoDT(String soDT) {
        this.soDT = soDT;
    }

    
}
