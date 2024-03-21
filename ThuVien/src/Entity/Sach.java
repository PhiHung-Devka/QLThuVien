/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entity;

/**
 *
 * @author nguye
 */
public class Sach {

    private String maSach, tenSach;
    private int namXB, soLuong;
    private double giaSach;
    private String maLoaiSach, maXB, hinh;

    public Sach(String maSach, String tenSach, int namXB, int soLuong, double giaSach, String maLoaiSach, String maXB, String hinh) {
        this.maSach = maSach;
        this.tenSach = tenSach;
        this.namXB = namXB;
        this.soLuong = soLuong;
        this.giaSach = giaSach;
        this.maLoaiSach = maLoaiSach;
        this.maXB = maXB;
        this.hinh = hinh;
    }

    public String getHinh() {
        return hinh;
    }

    public void setHinh(String hinh) {
        this.hinh = hinh;
    }

    public Sach() {
    }

    public String getMaSach() {
        return maSach;
    }

    public void setMaSach(String maSach) {
        this.maSach = maSach;
    }

    public String getTenSach() {
        return tenSach;
    }

    public void setTenSach(String tenSach) {
        this.tenSach = tenSach;
    }

    public int getNamXB() {
        return namXB;
    }

    public void setNamXB(int namXB) {
        this.namXB = namXB;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public double getGiaSach() {
        return giaSach;
    }

    public void setGiaSach(double giaSach) {
        this.giaSach = giaSach;
    }

    public String getMaLoaiSach() {
        return maLoaiSach;
    }

    public void setMaLoaiSach(String maLoaiSach) {
        this.maLoaiSach = maLoaiSach;
    }

    public String getMaXB() {
        return maXB;
    }

    public void setMaXB(String maXB) {
        this.maXB = maXB;
    }
    
    
}
