/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nhom8.model;

import java.util.Date;

/**
 *
 * @author COMPUTER
 */
public class NhanVien {
    String maNV;
    String tenNV;
    String matKhau;
    boolean chucVu;
    boolean gioiTinh;
    String diaChi;
    String sDT;
    Date ngaySinh;
    String hinh;

    public NhanVien() {
    }

    public NhanVien(String maNV, String tenNV, String matKhau, boolean chucVu, boolean gioiTinh, String diaChi, String sDT, Date ngaySinh, String hinh) {
        this.maNV = maNV;
        this.tenNV = tenNV;
        this.matKhau = matKhau;
        this.chucVu = chucVu;
        this.gioiTinh = gioiTinh;
        this.diaChi = diaChi;
        this.sDT = sDT;
        this.ngaySinh = ngaySinh;
        this.hinh = hinh;
    }

    public String getMaNV() {
        return maNV;
    }

    public String getTenNV() {
        return tenNV;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public boolean isChucVu() {
        return chucVu;
    }

    public boolean isGioiTinh() {
        return gioiTinh;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public String getsDT() {
        return sDT;
    }

    public Date getNgaySinh() {
        return ngaySinh;
    }

    public String getHinh() {
        return hinh;
    }

    public void setMaNV(String maNV) {
        this.maNV = maNV;
    }

    public void setTenNV(String tenNV) {
        this.tenNV = tenNV;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }

    public void setChucVu(boolean chucVu) {
        this.chucVu = chucVu;
    }

    public void setGioiTinh(boolean gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public void setsDT(String sDT) {
        this.sDT = sDT;
    }

    public void setNgaySinh(Date ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public void setHinh(String hinh) {
        this.hinh = hinh;
    }
   
  


    
}
