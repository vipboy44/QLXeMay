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
public class HangXe {
    String maHX;
    String tenHX;

    public HangXe(String maHX, String tenHX) {
        this.maHX = maHX;
        this.tenHX = tenHX;
    }

    public HangXe() {
       
    }

    public String getMaHX() {
        return maHX;
    }

    public String getTenHX() {
        return tenHX;
    }

    public void setMaHX(String maHX) {
        this.maHX = maHX;
    }

    public void setTenHX(String tenHX) {
        this.tenHX = tenHX;
    }
    

    
    
}
