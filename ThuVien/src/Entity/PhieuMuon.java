/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entity;

import java.util.Date;
import java.util.logging.Logger;

/**
 *
 * @author DIEUMY
 */
public class PhieuMuon {
    private String MaPhieuMuon;
    private String MaDocGia;
    private String MaSach;
    private String TenSach;
    private Date NgayMuon;
    private Date HanTra;

    public String getMaPhieuMuon() {
        return MaPhieuMuon;
    }

    public String getMaDocGia() {
        return MaDocGia;
    }

    public String getMaSach() {
        return MaSach;
    }

    public String getTenSach() {
        return TenSach;
    }

    public Date getNgayMuon() {
        return NgayMuon;
    }

    public Date getHanTra() {
        return HanTra;
    }

    public void setMaPhieuMuon(String MaPhieuMuon) {
        this.MaPhieuMuon = MaPhieuMuon;
    }

    public void setMaDocGia(String MaDocGia) {
        this.MaDocGia = MaDocGia;
    }

    public void setMaSach(String MaSach) {
        this.MaSach = MaSach;
    }

    public void setTenSach(String TenSach) {
        this.TenSach = TenSach;
    }

    public void setNgayMuon(Date NgayMuon) {
        this.NgayMuon = NgayMuon;
    }

    public void setHanTra(Date HanTra) {
        this.HanTra = HanTra;
    }

    public PhieuMuon(String MaPhieuMuon, String MaDocGia, String MaSach, String TenSach, Date NgayMuon, Date HanTra) {
        this.MaPhieuMuon = MaPhieuMuon;
        this.MaDocGia = MaDocGia;
        this.MaSach = MaSach;
        this.TenSach = TenSach;
        this.NgayMuon = NgayMuon;
        this.HanTra = HanTra;
    }
    
    
}
