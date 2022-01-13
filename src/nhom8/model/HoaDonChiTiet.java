/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nhom8.model;

/**
 *
 * @author COMPUTER
 */
public class HoaDonChiTiet {
    String maHD;
    String soKhung;
    double gia;
    public HoaDonChiTiet()
    {}

    public HoaDonChiTiet(String maHD, String soKhung, double gia) {
        this.maHD = maHD;
        this.soKhung = soKhung;
        this.gia = gia;
    }

    public String getMaHD() {
        return maHD;
    }

    public String getSoKhung() {
        return soKhung;
    }

    public double getGia() {
        return gia;
    }

    public void setMaHD(String maHD) {
        this.maHD = maHD;
    }

    public void setSoKhung(String soKhung) {
        this.soKhung = soKhung;
    }

    public void setGia(double gia) {
        this.gia = gia;
    }

    
}
