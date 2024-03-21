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
public class PhieuTra {
    private String MaPhieuTra;
    private String MaPhieuMuon;
    private String MaSach;
    private Date NgayTra;

    public PhieuTra(String MaPhieuTra, String MaPhieuMuon, String MaSach, Date NgayTra) {
        this.MaPhieuTra = MaPhieuTra;
        this.MaPhieuMuon = MaPhieuMuon;
        this.MaSach = MaSach;
        this.NgayTra = NgayTra;
    }

    public String getMaPhieuTra() {
        return MaPhieuTra;
    }

    public String getMaPhieuMuon() {
        return MaPhieuMuon;
    }

    public String getMaSach() {
        return MaSach;
    }

    public Date getNgayTra() {
        return NgayTra;
    }

    public void setMaPhieuTra(String MaPhieuTra) {
        this.MaPhieuTra = MaPhieuTra;
    }

    public void setMaPhieuMuon(String MaPhieuMuon) {
        this.MaPhieuMuon = MaPhieuMuon;
    }

    public void setMaSach(String MaSach) {
        this.MaSach = MaSach;
    }

    public void setNgayTra(Date NgayTra) {
        this.NgayTra = NgayTra;
    }
    
    
}
