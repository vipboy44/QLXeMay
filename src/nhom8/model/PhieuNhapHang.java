/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nhom8.model;

import java.util.Date;
import nhom8.helper.DateHelper;

/**
 *
 * @author COMPUTER
 */
public class PhieuNhapHang {
    String maPN;
    String maNV;
    String nhaCungCap;
    Date ngayNhap = DateHelper.now() ;
    public PhieuNhapHang()
    {}

    public PhieuNhapHang(String maPN, String maNV, String nhaCungCap,Date ngayNhap) {
        this.maPN = maPN;
        this.maNV = maNV;
        this.nhaCungCap = nhaCungCap;
        this.ngayNhap = ngayNhap;
    }

    public String getMaPN() {
        return maPN;
    }

    public String getMaNV() {
        return maNV;
    }

    public String getNhaCungCap() {
        return nhaCungCap;
    }

    public Date getNgayNhap() {
        return ngayNhap;
    }

    public void setMaPN(String maPN) {
        this.maPN = maPN;
    }

    public void setMaNV(String maNV) {
        this.maNV = maNV;
    }

    public void setNhaCungCap(String nhaCungCap) {
        this.nhaCungCap = nhaCungCap;
    }

    public void setNgayNhap(Date ngayNhap) {
        this.ngayNhap = ngayNhap;
    }

    

   

    
    
}
