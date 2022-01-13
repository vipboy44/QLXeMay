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
public class HoaDon {
    String maHD;
    String maNV;
    String maKH;
    Date ngayBan = DateHelper.now(); ;
    public HoaDon()
    {}

    public HoaDon(String maHD, String maNV, String maKH, Date ngayBan) {
        this.maHD = maHD;
        this.maNV = maNV;
        this.maKH = maKH;
        this.ngayBan = ngayBan;
    }



    public String getMaHD() {
        return maHD;
    }

    public String getMaNV() {
        return maNV;
    }

    public String getMaKH() {
        return maKH;
    }

    public Date getNgayBan() {
        return ngayBan;
    }

    public void setMaHD(String maHD) {
        this.maHD = maHD;
    }

    public void setMaNV(String maNV) {
        this.maNV = maNV;
    }

    public void setMaKH(String maKH) {
        this.maKH = maKH;
    }

    public void setNgayBan(Date ngayBan) {
        this.ngayBan = ngayBan;
    }

   
    
}
