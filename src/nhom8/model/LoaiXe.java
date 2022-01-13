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
public class LoaiXe {
    String maLX;
    String maHX;
    String tenLoai;
    public LoaiXe()
    {}

    public LoaiXe(String maLX, String maHX, String tenLoai) {
        this.maLX = maLX;
        this.maHX = maHX;
        this.tenLoai = tenLoai;
    }

    @Override
    public String toString() {
        return this.tenLoai;
    }

    public String getMaLX() {
        return maLX;
    }

    public String getMaHX() {
        return maHX;
    }

    public String getTenLoai() {
        return tenLoai;
    }

    public void setMaLX(String maLX) {
        this.maLX = maLX;
    }

    public void setMaHX(String maHX) {
        this.maHX = maHX;
    }

    public void setTenLoai(String tenLoai) {
        this.tenLoai = tenLoai;
    }

    

   
    
    
}
