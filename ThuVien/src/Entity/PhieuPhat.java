/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entity;

import java.util.Date;

/**
 *
 * @author DIEUMY
 */
public class PhieuPhat {
    private String MaPhieuPhat;
    private String MaPhieuTra;
    private String MaSach;
    private Date NgayPhat;
    private String LyDoPhat;
    private Double PhiPhat;
    private String MaNV;

    public PhieuPhat(String MaPhieuPhat, String MaPhieuTra, String MaSach, Date NgayPhat, String LyDoPhat, Double PhiPhat, String MaNV) {
        this.MaPhieuPhat = MaPhieuPhat;
        this.MaPhieuTra = MaPhieuTra;
        this.MaSach = MaSach;
        this.NgayPhat = NgayPhat;
        this.LyDoPhat = LyDoPhat;
        this.PhiPhat = PhiPhat;
        this.MaNV = MaNV;
    }

    public String getMaPhieuPhat() {
        return MaPhieuPhat;
    }

    public String getMaPhieuTra() {
        return MaPhieuTra;
    }

    public String getMaSach() {
        return MaSach;
    }

    public Date getNgayPhat() {
        return NgayPhat;
    }

    public String getLyDoPhat() {
        return LyDoPhat;
    }

    public Double getPhiPhat() {
        return PhiPhat;
    }

    public String getMaNV() {
        return MaNV;
    }

    public void setMaPhieuPhat(String MaPhieuPhat) {
        this.MaPhieuPhat = MaPhieuPhat;
    }

    public void setMaPhieuTra(String MaPhieuTra) {
        this.MaPhieuTra = MaPhieuTra;
    }

    public void setMaSach(String MaSach) {
        this.MaSach = MaSach;
    }

    public void setNgayPhat(Date NgayPhat) {
        this.NgayPhat = NgayPhat;
    }

    public void setLyDoPhat(String LyDoPhat) {
        this.LyDoPhat = LyDoPhat;
    }

    public void setPhiPhat(Double PhiPhat) {
        this.PhiPhat = PhiPhat;
    }

    public void setMaNV(String MaNV) {
        this.MaNV = MaNV;
    }
    
    
}
