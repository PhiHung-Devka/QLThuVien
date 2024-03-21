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
public class DocGia {
    private String MaDocGia;
    private String HoTen;
    private String CCCD;
    private String Email;
    private String DiaChi;
    private Date NgayCap;
    private Date HanSD;
    private String Hinh;

    public DocGia(String MaDocGia, String HoTen, String CCCD, String Email, String DiaChi, Date NgayCap, Date HanSD, String Hinh) {
        this.MaDocGia = MaDocGia;
        this.HoTen = HoTen;
        this.CCCD = CCCD;
        this.Email = Email;
        this.DiaChi = DiaChi;
        this.NgayCap = NgayCap;
        this.HanSD = HanSD;
        this.Hinh = Hinh;
    }

    public String getDiaChi() {
        return DiaChi;
    }

    public void setDiaChi(String DiaChi) {
        this.DiaChi = DiaChi;
    }

    public void setMaDocGia(String MaDocGia) {
        this.MaDocGia = MaDocGia;
    }

    public void setHoTen(String HoTen) {
        this.HoTen = HoTen;
    }

    public void setCCCD(String CCCD) {
        this.CCCD = CCCD;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public void setNgayCap(Date NgayCap) {
        this.NgayCap = NgayCap;
    }

    public void setHanSD(Date HanSD) {
        this.HanSD = HanSD;
    }

    public void setHinh(String Hinh) {
        this.Hinh = Hinh;
    }

    public String getMaDocGia() {
        return MaDocGia;
    }

    public String getHoTen() {
        return HoTen;
    }

    public String getCCCD() {
        return CCCD;
    }

    public String getEmail() {
        return Email;
    }

    public Date getNgayCap() {
        return NgayCap;
    }

    public Date getHanSD() {
        return HanSD;
    }

    public String getHinh() {
        return Hinh;
    }
    
}
