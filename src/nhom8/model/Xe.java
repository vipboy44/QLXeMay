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
public class Xe {
    String soKhung;
    String maPN;
    String maLX;
    String soMay;
    String mauXe;
    String phanKhoi;
    double giaNhap;
    double giaBan;
    String anh;
    int trangThai;
    public Xe()
    {}

    public Xe(String soKhung, String maPN, String maLX, String soMay, String mauXe, String phanKhoi, double giaNhap, double giaBan, String anh, int trangThai) {
        this.soKhung = soKhung;
        this.maPN = maPN;
        this.maLX = maLX;
        this.soMay = soMay;
        this.mauXe = mauXe;
        this.phanKhoi = phanKhoi;
        this.giaNhap = giaNhap;
        this.giaBan = giaBan;
        this.anh = anh;
        this.trangThai = trangThai;
    }

    public String getSoKhung() {
        return soKhung;
    }

    public String getMaPN() {
        return maPN;
    }

    public String getMaLX() {
        return maLX;
    }

    public String getSoMay() {
        return soMay;
    }

    public String getMauXe() {
        return mauXe;
    }

    public String getPhanKhoi() {
        return phanKhoi;
    }

    public double getGiaNhap() {
        return giaNhap;
    }

    public double getGiaBan() {
        return giaBan;
    }

    public String getAnh() {
        return anh;
    }

    public int getTrangThai() {
        return trangThai;
    }

    public void setSoKhung(String soKhung) {
        this.soKhung = soKhung;
    }

    public void setMaPN(String maPN) {
        this.maPN = maPN;
    }

    public void setMaLX(String maLX) {
        this.maLX = maLX;
    }

    public void setSoMay(String soMay) {
        this.soMay = soMay;
    }

    public void setMauXe(String mauXe) {
        this.mauXe = mauXe;
    }

    public void setPhanKhoi(String phanKhoi) {
        this.phanKhoi = phanKhoi;
    }

    public void setGiaNhap(double giaNhap) {
        this.giaNhap = giaNhap;
    }

    public void setGiaBan(double giaBan) {
        this.giaBan = giaBan;
    }

    public void setAnh(String anh) {
        this.anh = anh;
    }

    public void setTrangThai(int trangThai) {
        this.trangThai = trangThai;
    }

    
    

    
   
    
}
