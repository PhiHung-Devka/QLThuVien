/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package thuvien;

import Entity.DocGia;
import Entity.PhieuMuon;
import Entity.PhieuPhat;
import Entity.PhieuTra;
import Entity.Sach;
import Utils.JDBC;
import Utils.XDate;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author DIEUMY
 */
public class ThuVienJFrame extends javax.swing.JFrame {

	private ArrayList<PhieuPhat> listPhieuPhat = new ArrayList<>();
	private ArrayList<PhieuMuon> listPhieuMuon = new ArrayList<>();
	private ArrayList<DocGia> listDocGia = new ArrayList<>();
	private ArrayList<Sach> listSach = new ArrayList<>();
	private ArrayList<PhieuMuon> listPhieuMuonFull = new ArrayList<>();
	private ArrayList<PhieuTra> listPhieuTra = new ArrayList<>();
	private String Madg = "";
	private Connection conn = JDBC.getConnection("sa", "songlong", "QLThuVien_SOF102");
	private int index = -1;
	private DefaultTableModel model = new DefaultTableModel();
	private DefaultTableModel model1 = new DefaultTableModel();
	private File f1, f2, dir = new File("src\\Images");
	private String imageSave = "";

	/**
	 * Creates new form ThuVienJFrame
	 */
	public ThuVienJFrame() {
		initComponents();
		unit();
	}

	public ThuVienJFrame(String hoTen, String username) throws SQLException {
		initComponents();
		unit();
		lblTrangThai.setText(hoTen);
		txtMaNhanVien.setText(username);
	}

	void unit() {
		Image us = Toolkit.getDefaultToolkit().createImage("src\\Images\\logo-icon.png");
		this.setIconImage(us);
		setLocationRelativeTo(null);
		setTitle("NUCLEAR LIBRARY");
		new Timer(1000, new ActionListener() {
			SimpleDateFormat format = new SimpleDateFormat("hh:mm:ss a");

			@Override
			public void actionPerformed(ActionEvent e) {
				lblDongHo.setText(format.format(new Date()));
			}
		}).start();

		tblSachMuon.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tblThongTinSach.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		String[] cols = { "Mã PM", "Mã sách", "Tên sách", "Trạng thái" };
		model.setColumnIdentifiers(cols);
		tblSachMuon.setModel(model);
		clearDG();

		Sach();
		String[] cols1 = { "Mã sách", "Tên sách", "Số lượng", "Giá sách" };
		model1.setColumnIdentifiers(cols1);
		tblThongTinSach.setModel(model1);
	}

	void display(String m) {
		pnl.setVisible(false);
		pnlDocGia.setVisible(false);
		pnlLePhi.setVisible(false);
		pnlPhieuMuon.setVisible(false);
		pnlTaoThe.setVisible(false);
		pnlPhieuTraPhat.setVisible(false);
		switch (m) {
		case "pnl" -> pnl.setVisible(true);

		case "pnlDocGia" -> pnlDocGia.setVisible(true);

		case "pnlPhieuMuon" -> pnlPhieuMuon.setVisible(true);

		case "pnlPhieuTraPhat" -> pnlPhieuTraPhat.setVisible(true);

		case "pnlTaoThe" -> pnlTaoThe.setVisible(true);

		case "pnlLePhi" -> pnlLePhi.setVisible(true);

		}

	}

	void phieuMuon() {
		listPhieuMuon.clear();
		try {
			String sql = "select * from PHIEUMUON where MaPhieuMuon not in (select MaPhieuMuon from PHIEUTRA)";
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {
				String maPM = rs.getString(1);
				String maDG = rs.getString(2);
				String maSach = rs.getString(3);
				String tenSach = rs.getString(4);
				java.sql.Date ngayMuon = rs.getDate(5);
				java.sql.Date hanTra = rs.getDate(6);
				PhieuMuon pm = new PhieuMuon(maPM, maDG, maSach, tenSach, ngayMuon, hanTra);
				listPhieuMuon.add(pm);
			}
		} catch (SQLException ex) {
			Logger.getLogger(ThuVienJFrame.class.getName()).log(Level.SEVERE, null, ex);
		}

	}

	void docGia() {
		try {
			String sql = "select MaDocGia, CONCAT(Ho,' ',Ten) as 'HoTen', CCCD, "
					+ "Email, DiaChi, NgayCap, HanSD, Hinh from DOCGIA ";
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {
				String maDG = rs.getString(1);
				String hoTen = rs.getString(2);
				String cccd = rs.getString(3);
				String email = rs.getString(4);
				String diachi = rs.getString(5);
				java.sql.Date ngayCap = rs.getDate(6);
				java.sql.Date hanSD = rs.getDate(7);
				String hinh = rs.getString(8);
				DocGia dg = new DocGia(maDG, hoTen, cccd, email, diachi, ngayCap, hanSD, hinh);
				listDocGia.add(dg);
			}
		} catch (SQLException ex) {
			Logger.getLogger(ThuVienJFrame.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	void clearDG() {
		lblMaDG.setText("");
		lblHoTen.setText("");
		lblCCCD.setText("");
		lblEmail.setText("");
		lblNgayCap.setText("");
		lblHan.setText("");
		lblHinh2.setIcon(ResizeImage("src\\Images\\logo-icon.png"));
		btnLapPM.setEnabled(false);
		btnLapPhieuTra.setEnabled(false);
	}

	void findDocGia() {
		int m = 0;
		phieuMuon();
		tblSachMuon.setModel(model);
		model.setRowCount(0);
		for (int i = 0; i < listPhieuMuon.size(); i++) {
			if (listPhieuMuon.get(i).getMaDocGia().equalsIgnoreCase(txtFindDocgia.getText())) {
				// trạng thái
				Date date = new Date();
				Boolean tt;
				if (date.after(listPhieuMuon.get(i).getHanTra())) {
					tt = false;
				} else {
					tt = true;
				}
				model.addRow(new Object[] { listPhieuMuon.get(i).getMaPhieuMuon(), listPhieuMuon.get(i).getMaSach(),
						listPhieuMuon.get(i).getTenSach(), tt });

				// Mã đọc giả lập phiếu mượn
				Madg = txtFindDocgia.getText();
			}
		}
		// Doc gia
		docGia();
		for (DocGia dg : listDocGia) {
			if (dg.getMaDocGia().equalsIgnoreCase(txtFindDocgia.getText())) {
				m = 1;
				lblMaDG.setText(dg.getMaDocGia());
				lblHoTen.setText(dg.getHoTen());
				lblCCCD.setText(dg.getCCCD());
				lblEmail.setText(dg.getEmail());
				lblNgayCap.setText(XDate.toString(dg.getNgayCap()));
				lblHan.setText(XDate.toString(dg.getHanSD()));
				ImageIcon ic = new ImageIcon(dg.getHinh());
//            chinh hinh vua voi label
				Image scaledImage = ic.getImage().getScaledInstance(lblAnh.getWidth(), lblAnh.getHeight(),
						Image.SCALE_SMOOTH);
				ic.setImage(scaledImage); // Cập nhật hình ảnh mới đã co giãn cho ImageIcon
				lblHinh2.setText("");
				lblHinh2.setHorizontalAlignment(JLabel.CENTER);
				lblHinh2.setVerticalAlignment(JLabel.CENTER);
				lblHinh2.setIcon(ic);
			}
		}
		if (tblSachMuon.getSelectedRowCount() < 3) {
			btnLapPM.setEnabled(true);
		} else {
			btnLapPM.setEnabled(false);
		}
		if (txtFindDocgia.getText().equals("")) {
			clearDG();
			m = 1;
		}
		if (m == 0) {
			JOptionPane.showMessageDialog(this, "Không tìm thấy đọc giả");
		}
	}

	void fullPhieuMuon() {
		listPhieuMuonFull.clear();
		try {
			String sql = "select * from PHIEUMUON";
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {
				String maPM = rs.getString(1);
				String maDG = rs.getString(2);
				String maSach = rs.getString(3);
				String tenSach = rs.getString(4);
				java.sql.Date ngayMuon = rs.getDate(5);
				java.sql.Date hanTra = rs.getDate(6);
				PhieuMuon pm = new PhieuMuon(maPM, maDG, maSach, tenSach, ngayMuon, hanTra);
				listPhieuMuonFull.add(pm);
			}
		} catch (SQLException ex) {
			Logger.getLogger(ThuVienJFrame.class.getName()).log(Level.SEVERE, null, ex);
		}

	}

	public ImageIcon ResizeImage(String ImagePath) {
		ImageIcon myImage = new ImageIcon(ImagePath);
		Image img = myImage.getImage();
		Image newImg = img.getScaledInstance(lblHinh2.getWidth(), lblHinh2.getHeight(), Image.SCALE_SMOOTH);
		lblHinh2.setHorizontalAlignment(lblHinh2.CENTER);
		lblHinh2.setVerticalAlignment(lblHinh2.CENTER);
		ImageIcon image = new ImageIcon(newImg);
		return image;
	}

	void Sach() {
		listSach.clear();
		try {
			String sql = "select * from SACH ";
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {
				String MaSach = rs.getString(1);
				String TenSach = rs.getString(2);
				int NamSX = rs.getInt(3);
				int SoLuong = rs.getInt(4);
				double Gia = rs.getInt(5);
				String MaLoai = rs.getString(6);
				String NXB = rs.getString(7);
				String hinh = rs.getString(8);
				Sach sach = new Sach(MaSach, TenSach, NamSX, SoLuong, Gia, MaLoai, NXB, hinh);
				listSach.add(sach);
			}
		} catch (SQLException ex) {
			Logger.getLogger(ThuVienJFrame.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	void findSach() {
		int m = 0;
		Sach();
		phieuMuon();
		model1.setRowCount(0);
		tblThongTinSach.setModel(model1);
		for (Sach sa : listSach) {
			if (txtSearch1.getText().equalsIgnoreCase(sa.getMaSach())) {
				model1.addRow(new Object[] { sa.getMaSach(), sa.getTenSach(), sa.getSoLuong(), sa.getGiaSach() });
				m = 1;
			}
		}

		if (txtSearch1.getText().equals("")) {
			m = 1;
		}
		try {
			index = 0;
			tblThongTinSach.setRowSelectionInterval(index, index);
		} catch (Exception e) {
		}

		if (m == 0) {
			JOptionPane.showMessageDialog(this, "Không tìm thấy sách!!!");
		}
	}

	void lapPhieuMuon() {
		fullPhieuMuon();
		txtMaSach.setText((String) tblThongTinSach.getValueAt(0, 0));
		txtTenSach.setText((String) tblThongTinSach.getValueAt(0, 1));
		txtGia.setText(tblThongTinSach.getValueAt(0, 3).toString());
		Date date = new Date();
// lấy thời điểm bây giờ:
		Calendar c = Calendar.getInstance();
// cộng thêm 7 ngày:
		c.add(Calendar.DAY_OF_YEAR, 7);

// Muốn chuyển qua lớp khác như Date chẳng hạn:
		Date d = c.getTime();
		txtNgayMuon.setText(XDate.toString(date));
		txtNgayTra.setText(XDate.toString(d));

		// Mã đọc giả lập phiếu mượn
		Madg = txtFindDocgia.getText();
		txtMaDG.setText(Madg);

		String pm = listPhieuMuonFull.get(listPhieuMuonFull.size() - 1).getMaPhieuMuon();
		String substring = pm.substring(2);
		int phm = Integer.parseInt(substring) + 1;
		if (phm < 100) {
			txtMaPhieuMuon.setText("PM0" + phm);
		} else {
			txtMaPhieuMuon.setText("PM" + phm);
		}

	}

	void LuuPhieuMuon() {
		try {
			String sql = "INSERT INTO PHIEUMUON VALUES (?, ?, ?, ?, ?, ?)";
			PreparedStatement pst = conn.prepareStatement(sql);
			pst.setString(1, txtMaPhieuMuon.getText());
			pst.setString(2, txtMaDG.getText());
			pst.setString(3, txtMaSach.getText());
			pst.setString(4, txtTenSach.getText());
			pst.setString(5, txtNgayMuon.getText());
			pst.setString(6, txtNgayTra.getText());
			int k = pst.executeUpdate();
			if (k > 0) {
				JOptionPane.showMessageDialog(this, "Lập phiếu mượn thành công!!!");
				for (Sach s : listSach) {
					if (txtSearch1.getText().equalsIgnoreCase(s.getMaSach())) {
						String updateSach = "update Sach set SoLuong =  SoLuong - 1 " + "where MaSach = '"
								+ s.getMaSach() + "'";
						Statement st = conn.createStatement();
						int soDong = st.executeUpdate(updateSach);
						phieuMuon();
					}
				}
			}
		} catch (SQLException ex) {
			Logger.getLogger(ThuVienJFrame.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	void clearPM() {
		txtMaPhieuMuon.setText("");
		txtMaSach.setText("");
		txtTenSach.setText("");
		txtGia.setText("");
		txtNgayMuon.setText("");
		txtNgayTra.setText("");
		btnOkPhieu.setEnabled(false);

	}

	void PhieuTra() {
		listPhieuTra.clear();
		try {
			String sql = "select * from PhieuTra ";
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {
				String MaPhieuTra = rs.getString(1);
				String MaPhieuMuon = rs.getString(2);
				String MaSach = rs.getString(3);
				java.sql.Date NgayTra = rs.getDate(4);

				PhieuTra pt = new PhieuTra(MaPhieuTra, MaPhieuMuon, MaSach, NgayTra);
				listPhieuTra.add(pt);
			}
		} catch (SQLException ex) {
			Logger.getLogger(ThuVienJFrame.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	void lapPhieuTra() {
		PhieuTra();
		String pt = listPhieuTra.get(listPhieuTra.size() - 1).getMaPhieuTra();
		String substring = pt.substring(2);
		int phm = Integer.parseInt(substring) + 1;
		if (phm < 100) {
			txtMaPhieuTra5.setText("PT0" + phm);
		} else {
			txtMaPhieuTra5.setText("PT" + phm);
		}
		index = tblSachMuon.getSelectedRow();
		String mapm = (String) tblSachMuon.getValueAt(index, 0);
		try {
			String sql = "select * from phieumuon where MaPhieuMuon = " + "'" + mapm + "'";
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {
				txtMaPhieuMuon4.setText(rs.getString(1));
				txtMasach5.setText(rs.getString(3));
				txtTenSach4.setText(rs.getString(4));
				txtNgayMuon4.setText(rs.getString(5));

			}
		} catch (SQLException ex) {
			Logger.getLogger(ThuVienJFrame.class.getName()).log(Level.SEVERE, null, ex);
		}
		Date date = new Date();
		txtNgayTra4.setText(XDate.toString(date));
	}

	void luuPhieuTra() {
		try {
			String sql = "INSERT INTO PHIEUTRA VALUES (?, ?, ?, ?)";
			PreparedStatement pst = conn.prepareStatement(sql);
			pst.setString(1, txtMaPhieuTra5.getText());
			pst.setString(2, txtMaPhieuMuon4.getText());
			pst.setString(3, txtMasach5.getText());
			pst.setString(4, txtNgayTra4.getText());
			int k = pst.executeUpdate();
			if (k > 0) {
				JOptionPane.showMessageDialog(this, "Lập phiếu trả thành công!!!");
			}
		} catch (SQLException ex) {
			Logger.getLogger(ThuVienJFrame.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	void PhieuPhat() {
		listPhieuPhat.clear();
		try {
			String sql = "select * from PhieuPhat";
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {
				String MaPhieuPhat = rs.getString(1);
				String MaPhieuTra = rs.getString(2);
				String MaSach = rs.getString(3);
				java.sql.Date NgayPhat = rs.getDate(4);
				String LyDoPhat = rs.getString(5);
				Double PhiPhat = rs.getDouble(6);
				String MaNV = rs.getString(7);

				PhieuPhat pp = new PhieuPhat(MaPhieuPhat, MaPhieuTra, MaSach, NgayPhat, LyDoPhat, PhiPhat, MaNV);
				listPhieuPhat.add(pp);
			}
		} catch (SQLException ex) {
			Logger.getLogger(ThuVienJFrame.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	void lapPhieuPhat() {
		PhieuPhat();
		String pp = listPhieuPhat.get(listPhieuPhat.size() - 1).getMaPhieuPhat();
		String substring = pp.substring(2);
		int phm = Integer.parseInt(substring) + 1;
		if (phm < 100) {
			txtMaphieuphat.setText("PP0" + phm);
		} else {
			txtMaphieuphat.setText("PP" + phm);
		}
		txtMaphieutra2.setText(txtMaPhieuTra5.getText());
		txtMasach2.setText(txtMasach5.getText());
		txtNgayPhat.setText(txtNgayTra4.getText());
	}

	void luuPhieuPhat() {
		try {
			String sql = "INSERT INTO PHIEUPHAT VALUES (?, ?, ?, ?, ?, ?, ?)";
			PreparedStatement pst = conn.prepareStatement(sql);
			pst.setString(1, txtMaphieuphat.getText());
			pst.setString(2, txtMaphieutra2.getText());
			pst.setString(3, txtMasach2.getText());
			pst.setString(4, txtNgayPhat.getText());
			pst.setString(5, (String) cboLyDo.getSelectedItem());
			pst.setString(6, txtPhi.getText());
			pst.setString(7, txtMaNhanVien.getText());
			int k = pst.executeUpdate();
			if (k > 0) {
				JOptionPane.showMessageDialog(this, "Lập phiếu phạt thành công!!!");
			}
		} catch (SQLException ex) {
			Logger.getLogger(ThuVienJFrame.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public boolean checkNull() {
		String formatMADG = "DG[0-9]{3}";
		String ccdFormat = "0\\d{11}";
		String phoneFormat = "0\\d{9,10}";
		String emailFormat = "\\w+@\\w+(\\.\\w+){1,2}";
		String dateFormat = "yyyy-MM-dd";
		if (txtMaDocGia.getText().equals("")) {
			JOptionPane.showMessageDialog(this, "Bạn chưa nhập mã đọc giả!");
			return true;
		} else if (!txtMaDocGia.getText().matches(formatMADG)) {
			JOptionPane.showMessageDialog(this, "Mã đọc giả định dạng là DG... !");
			return true;
		}
		docGia();
		for (DocGia dg : listDocGia) {
			if (txtMaDocGia.getText().equalsIgnoreCase(dg.getMaDocGia())) {
				JOptionPane.showMessageDialog(this, "Mã đọc giả đã tồn tại!");
				return true;
			}
		}
		if (txtTenDocGia.getText().equals("")) {
			JOptionPane.showMessageDialog(this, "Bạn chưa nhập tên đọc giả!");
			return true;
		} else if (txtCCCD.getText().equals("")) {
			JOptionPane.showMessageDialog(this, "Bạn chưa nhập CCCD!");
			return true;
		} else if (!txtCCCD.getText().matches(ccdFormat)) {
			JOptionPane.showMessageDialog(this, "CCCD Không đúng định dạng");
			return true;
		} else if (txtSDT.getText().equals("")) {
			JOptionPane.showMessageDialog(this, "Bạn chưa nhập số điện thoại!");
			return true;
		} else if (!txtSDT.getText().matches(phoneFormat)) {
			JOptionPane.showMessageDialog(this, "Số điện thoại bạn nhập không đúng định dạng!");
			return true;
		} else if (txtEmail.getText().equals("")) {
			JOptionPane.showMessageDialog(this, "Bạn chưa nhập Email");
			return true;
		} else if (!txtEmail.getText().matches(emailFormat)) {
			JOptionPane.showMessageDialog(this, "Email không đúng định dạng!");
			return true;
		} else if (txtDiaChi.getText().equals("")) {
			JOptionPane.showMessageDialog(this, "Bạn chưa nhập Địa chỉ!");
			return true;
		} else if (txtHanSuDung.getText().equals("")) {
			JOptionPane.showMessageDialog(this, "Bạn chưa nhập hạn sử dụng!");
			return true;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		sdf.setLenient(false); // Tắt chế độ linh hoạt
		try {
			sdf.parse(txtHanSuDung.getText());
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Hạn sử dụng bạn nhập chưa đúng định dạng!");
			return true;
		}
		if (imageSave.equals("")) {
			JOptionPane.showMessageDialog(this, "Bạn chưa chọn ảnh!");
			return true;
		}
		return false;
	}

	public void taoThe() throws SQLException {
		if (checkNull()) {
			return;
		}
		String hoDG = null, tenDG = null;
		int lastIndex = txtTenDocGia.getText().lastIndexOf(' ');
		if (lastIndex != -1) {
			// Sử dụng phương thức substring() để lấy phần của chuỗi từ vị trí cuối cùng đến
			// hết
			tenDG = txtTenDocGia.getText().substring(lastIndex + 1);
			hoDG = txtTenDocGia.getText().substring(0, lastIndex);
		}
//        String insertSQL = " insert into DOCGIA values\n"
//                + "  ('" + txtMaDocGia.getText() + "',N'" + hoDG + "',N'" + tenDG + "',"
//                + "'" + txtCCCD.getText() + "','" + txtEmail.getText() + "',N'" + txtDiaChi.getText() + "',"
//                + "'" + txtNgayCap.getText() + "','" + txtHanSuDung.getText() + "','" + imageSave + "');";
//        Statement st = conn.createStatement();
		String sql = "insert into DOCGIA values (?, ? ,?, ?, ?, ?, ?, ?, ?)";
		PreparedStatement pst = conn.prepareStatement(sql);
		pst.setString(1, txtMaDocGia.getText());
		pst.setString(2, hoDG);
		pst.setString(3, tenDG);
		pst.setString(4, txtCCCD.getText());
		pst.setString(5, txtEmail.getText());
		pst.setString(6, txtDiaChi.getText());
		pst.setString(7, txtNgayCap.getText());
		pst.setString(8, txtHanSuDung.getText());
		pst.setString(9, imageSave);
		int soDong = pst.executeUpdate();
		if (soDong > 0) {
			copyImage();
			JOptionPane.showMessageDialog(this, "Đã thêm thành công!");
		} else {
			JOptionPane.showMessageDialog(this, "Đã thêm thất bại");
		}
	}

	public void copyImage() throws SQLException {
		if (!dir.exists()) {// nếu folder image chưa tồn tại
			dir.mkdirs();
		}

		f2 = new File(imageSave);
		try {
			Files.copy(f1.toPath(), f2.toPath()); // Sao chép tệp tin ảnh
		} catch (IOException ex) {
			Logger.getLogger(QuanLyJFrame.class.getName()).log(Level.SEVERE, null, ex);
		}

//        String Iname = f2.getAbsolutePath();
//        ImageIcon ic = new ImageIcon(Iname);
////            chinh hinh vua voi label
//        Image scaledImage = ic.getImage().getScaledInstance(pnlanh.getWidth(), pnlanh.getHeight(), Image.SCALE_SMOOTH);
//        ic.setImage(scaledImage); // Cập nhật hình ảnh mới đã co giãn cho ImageIcon
//        lblAnh.setText("");
//        lblAnh.setHorizontalAlignment(JLabel.CENTER);
//        lblAnh.setVerticalAlignment(JLabel.CENTER);
//        lblAnh.setIcon(ic);
	}

	public void cancelTaoThe() {
		txtMaDocGia.setText("");
		txtTenDocGia.setText("");
		txtCCCD.setText("");
		txtSDT.setText("");
		txtEmail.setText("");
		txtDiaChi.setText("");
		txtHanSuDung.setText("");
		lblAnh.setIcon(null);
		imageSave = "";
	}

	public void findLePhi() {
		docGia();
		for (DocGia dg : listDocGia) {
			if (txtSearch.getText().equalsIgnoreCase(dg.getMaDocGia())) {
				lblMaDocGia.setText(dg.getMaDocGia());
				lblHovaTen.setText(dg.getHoTen());
				lblCanCuocCD.setText(dg.getCCCD());
				lblDiaChi.setText(dg.getDiaChi());
				lblEmailLePhi.setText(dg.getEmail());
				lblNgayCapLePhi.setText(String.valueOf(dg.getNgayCap()));
				lblGiaTriDen.setText(String.valueOf(dg.getHanSD()));
				ImageIcon ic = new ImageIcon(dg.getHinh());
//            chinh hinh vua voi label
				Image scaledImage = ic.getImage().getScaledInstance(lblAnh.getWidth(), lblAnh.getHeight(),
						Image.SCALE_SMOOTH);
				ic.setImage(scaledImage); // Cập nhật hình ảnh mới đã co giãn cho ImageIcon
				lblHinhLePhi.setText("");
				lblHinhLePhi.setHorizontalAlignment(JLabel.CENTER);
				lblHinhLePhi.setVerticalAlignment(JLabel.CENTER);
				lblHinhLePhi.setIcon(ic);

				lblMaThe.setText(dg.getMaDocGia());
				txtNgayCap1.setText(String.valueOf(dg.getNgayCap()));
				txtNgayHetHan.setText(String.valueOf(dg.getHanSD()));
			}
		}
	}

	public void giaHan() {

	}

	/**
	 * This method is called from within the cpnlPhieuMuononstructor to initialize
	 * the form. WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
	// <editor-fold defaultstate="collapsed" desc="Generated
	// Code">//GEN-BEGIN:initComponents
	private void initComponents() {

		tbaCongCu = new javax.swing.JToolBar();
		btnDocGia = new javax.swing.JButton();
		btnLePhi = new javax.swing.JButton();
		btnTaoThe = new javax.swing.JButton();
		jSeparator2 = new javax.swing.JToolBar.Separator();
		btnDangXuat = new javax.swing.JButton();
		pnlTrangThai = new javax.swing.JPanel();
		lblDongHo = new javax.swing.JLabel();
		lblTrangThai = new javax.swing.JLabel();
		jLayeredPane1 = new javax.swing.JLayeredPane();
		pnl = new javax.swing.JPanel();
		jLabel91 = new javax.swing.JLabel();
		pnlDocGia = new javax.swing.JPanel();
		jPanel8 = new javax.swing.JPanel();
		txtFindDocgia = new javax.swing.JTextField();
		btnFindDocGia = new javax.swing.JButton();
		jPanel10 = new javax.swing.JPanel();
		jScrollPane1 = new javax.swing.JScrollPane();
		tblSachMuon = new javax.swing.JTable();
		jCheckBox1 = new javax.swing.JCheckBox();
		jCheckBox2 = new javax.swing.JCheckBox();
		btnLapPhieuTra = new javax.swing.JButton();
		btnLapPM = new javax.swing.JButton();
		jPanel13 = new javax.swing.JPanel();
		jLabel18 = new javax.swing.JLabel();
		lblMaDG = new javax.swing.JLabel();
		jLabel52 = new javax.swing.JLabel();
		lblHoTen = new javax.swing.JLabel();
		jLabel54 = new javax.swing.JLabel();
		lblCCCD = new javax.swing.JLabel();
		lblEmail = new javax.swing.JLabel();
		jLabel59 = new javax.swing.JLabel();
		jLabel60 = new javax.swing.JLabel();
		lblNgayCap = new javax.swing.JLabel();
		lblHan = new javax.swing.JLabel();
		jLabel63 = new javax.swing.JLabel();
		lblHinh2 = new javax.swing.JLabel();
		pnlLePhi = new javax.swing.JPanel();
		txtNgayCap1 = new javax.swing.JTextField();
		lblMaThe = new javax.swing.JLabel();
		jLabel20 = new javax.swing.JLabel();
		jLabel21 = new javax.swing.JLabel();
		jLabel22 = new javax.swing.JLabel();
		txtNgayHetHan = new javax.swing.JTextField();
		txtLePhi = new javax.swing.JTextField();
		jLabel23 = new javax.swing.JLabel();
		jLabel24 = new javax.swing.JLabel();
		txtGiaHan = new javax.swing.JTextField();
		jPanel1 = new javax.swing.JPanel();
		txtSearch = new javax.swing.JTextField();
		btnSearch = new javax.swing.JButton();
		btnThem = new javax.swing.JButton();
		btnHuy = new javax.swing.JButton();
		jPanel12 = new javax.swing.JPanel();
		jLabel16 = new javax.swing.JLabel();
		lblMaDocGia = new javax.swing.JLabel();
		jLabel39 = new javax.swing.JLabel();
		lblHovaTen = new javax.swing.JLabel();
		jLabel41 = new javax.swing.JLabel();
		lblCanCuocCD = new javax.swing.JLabel();
		jLabel43 = new javax.swing.JLabel();
		lblDiaChi = new javax.swing.JLabel();
		lblEmailLePhi = new javax.swing.JLabel();
		jLabel46 = new javax.swing.JLabel();
		jLabel47 = new javax.swing.JLabel();
		lblNgayCapLePhi = new javax.swing.JLabel();
		lblGiaTriDen = new javax.swing.JLabel();
		jLabel50 = new javax.swing.JLabel();
		lblHinhLePhi = new javax.swing.JLabel();
		pnlTaoThe = new javax.swing.JPanel();
		pnlTaoThe1 = new javax.swing.JPanel();
		jLabel1 = new javax.swing.JLabel();
		jLabel2 = new javax.swing.JLabel();
		jLabel3 = new javax.swing.JLabel();
		jLabel5 = new javax.swing.JLabel();
		jLabel6 = new javax.swing.JLabel();
		jLabel7 = new javax.swing.JLabel();
		txtTenDocGia = new javax.swing.JTextField();
		txtMaDocGia = new javax.swing.JTextField();
		txtCCCD = new javax.swing.JTextField();
		txtEmail = new javax.swing.JTextField();
		txtNgayCap = new javax.swing.JTextField();
		txtHanSuDung = new javax.swing.JTextField();
		txtSDT = new javax.swing.JTextField();
		jLabel8 = new javax.swing.JLabel();
		pnlanh = new javax.swing.JPanel();
		lblAnh = new javax.swing.JLabel();
		btnTaoTheDocGia = new javax.swing.JButton();
		btnCanceltaoThedg = new javax.swing.JButton();
		jLabel4 = new javax.swing.JLabel();
		txtDiaChi = new javax.swing.JTextField();
		pnlPhieuMuon = new javax.swing.JPanel();
		jPanel2 = new javax.swing.JPanel();
		jScrollPane2 = new javax.swing.JScrollPane();
		tblThongTinSach = new javax.swing.JTable();
		btnCancelsach = new javax.swing.JButton();
		btnOksach = new javax.swing.JButton();
		jPanel3 = new javax.swing.JPanel();
		txtSearch1 = new javax.swing.JTextField();
		btnTim = new javax.swing.JButton();
		jPanel4 = new javax.swing.JPanel();
		jLabel9 = new javax.swing.JLabel();
		jLabel10 = new javax.swing.JLabel();
		jLabel11 = new javax.swing.JLabel();
		jLabel12 = new javax.swing.JLabel();
		jLabel13 = new javax.swing.JLabel();
		jLabel14 = new javax.swing.JLabel();
		jLabel15 = new javax.swing.JLabel();
		txtMaPhieuMuon = new javax.swing.JTextField();
		txtMaDG = new javax.swing.JTextField();
		txtMaSach = new javax.swing.JTextField();
		txtTenSach = new javax.swing.JTextField();
		txtGia = new javax.swing.JTextField();
		txtNgayMuon = new javax.swing.JTextField();
		txtNgayTra = new javax.swing.JTextField();
		btnOkPhieu = new javax.swing.JButton();
		btnCancelPhieu = new javax.swing.JButton();
		jLabel19 = new javax.swing.JLabel();
		pnlPhieuTraPhat = new javax.swing.JPanel();
		pnlPTra4 = new javax.swing.JPanel();
		jLabel85 = new javax.swing.JLabel();
		jLabel86 = new javax.swing.JLabel();
		jLabel87 = new javax.swing.JLabel();
		jLabel88 = new javax.swing.JLabel();
		jLabel89 = new javax.swing.JLabel();
		jLabel90 = new javax.swing.JLabel();
		txtMaPhieuTra5 = new javax.swing.JTextField();
		txtMaPhieuMuon4 = new javax.swing.JTextField();
		txtMasach5 = new javax.swing.JTextField();
		txtTenSach4 = new javax.swing.JTextField();
		txtNgayMuon4 = new javax.swing.JTextField();
		txtNgayTra4 = new javax.swing.JTextField();
		lblCanhBaoQuaHan = new javax.swing.JLabel();
		btnLapPhieuPhat4 = new javax.swing.JButton();
		btnTraSach4 = new javax.swing.JButton();
		tbnCancelPhat1 = new javax.swing.JButton();
		pnlPPhat = new javax.swing.JPanel();
		jLabel31 = new javax.swing.JLabel();
		jLabel32 = new javax.swing.JLabel();
		jLabel33 = new javax.swing.JLabel();
		jLabel34 = new javax.swing.JLabel();
		jLabel35 = new javax.swing.JLabel();
		jLabel36 = new javax.swing.JLabel();
		jLabel37 = new javax.swing.JLabel();
		txtMaphieuphat = new javax.swing.JTextField();
		txtMaphieutra2 = new javax.swing.JTextField();
		txtMasach2 = new javax.swing.JTextField();
		txtNgayPhat = new javax.swing.JTextField();
		cboLyDo = new javax.swing.JComboBox<>();
		txtPhi = new javax.swing.JTextField();
		txtMaNhanVien = new javax.swing.JTextField();
		tbnCancelPhat = new javax.swing.JButton();
		btnOkphat = new javax.swing.JButton();

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowOpened(java.awt.event.WindowEvent evt) {
				formWindowOpened(evt);
			}
		});

		tbaCongCu.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
		tbaCongCu.setRollover(true);
		tbaCongCu.setMargin(new java.awt.Insets(0, 15, 0, 15));

		btnDocGia.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/User group.png"))); // NOI18N
		btnDocGia.setText("  Đọc giả  ");
		btnDocGia.setFocusable(false);
		btnDocGia.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		btnDocGia.setMargin(new java.awt.Insets(2, 10, 2, 10));
		btnDocGia.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
		btnDocGia.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnDocGiaActionPerformed(evt);
			}
		});
		tbaCongCu.add(btnDocGia);

		btnLePhi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/Money.png"))); // NOI18N
		btnLePhi.setText("   Lệ phí    ");
		btnLePhi.setFocusable(false);
		btnLePhi.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		btnLePhi.setMargin(new java.awt.Insets(2, 10, 2, 10));
		btnLePhi.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
		btnLePhi.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnLePhiActionPerformed(evt);
			}
		});
		tbaCongCu.add(btnLePhi);

		btnTaoThe.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/Clipboard.png"))); // NOI18N
		btnTaoThe.setText("Tạo thẻ TV");
		btnTaoThe.setFocusable(false);
		btnTaoThe.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		btnTaoThe.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
		btnTaoThe.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnTaoTheActionPerformed(evt);
			}
		});
		tbaCongCu.add(btnTaoThe);
		tbaCongCu.add(jSeparator2);

		btnDangXuat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/Exit.png"))); // NOI18N
		btnDangXuat.setText("Đăng xuất");
		btnDangXuat.setFocusable(false);
		btnDangXuat.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		btnDangXuat.setMargin(new java.awt.Insets(2, 10, 2, 10));
		btnDangXuat.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
		tbaCongCu.add(btnDangXuat);

		btnDangXuat.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				int se = JOptionPane.showConfirmDialog(null, "Bạn có muốn thoát khỏi ứng dụng không?",
						"Hệ thống quản lý đào tạo", JOptionPane.YES_NO_OPTION);
				if (se == JOptionPane.YES_OPTION) {
					System.exit(0);
				}
			}
		});

		pnlTrangThai.setBorder(javax.swing.BorderFactory.createEtchedBorder());

		lblDongHo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/Alarm.png"))); // NOI18N
		lblDongHo.setText("09:07:47 AM");

		lblTrangThai.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/User.png"))); // NOI18N
		lblTrangThai.setText("Tên nhân viên");

		javax.swing.GroupLayout pnlTrangThaiLayout = new javax.swing.GroupLayout(pnlTrangThai);
		pnlTrangThai.setLayout(pnlTrangThaiLayout);
		pnlTrangThaiLayout
				.setHorizontalGroup(pnlTrangThaiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
								pnlTrangThaiLayout.createSequentialGroup().addContainerGap().addComponent(lblTrangThai)
										.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED,
												javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(lblDongHo).addContainerGap()));
		pnlTrangThaiLayout
				.setVerticalGroup(pnlTrangThaiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(pnlTrangThaiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(lblDongHo).addComponent(lblTrangThai)));

		jLabel91.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		jLabel91.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/logo1-removebg-preview.png"))); // NOI18N

		javax.swing.GroupLayout pnlLayout = new javax.swing.GroupLayout(pnl);
		pnl.setLayout(pnlLayout);
		pnlLayout.setHorizontalGroup(pnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				javax.swing.GroupLayout.Alignment.TRAILING,
				pnlLayout.createSequentialGroup().addContainerGap()
						.addComponent(jLabel91, javax.swing.GroupLayout.DEFAULT_SIZE, 836, Short.MAX_VALUE)
						.addContainerGap()));
		pnlLayout
				.setVerticalGroup(
						pnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addGroup(pnlLayout.createSequentialGroup()
										.addComponent(jLabel91, javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addGap(0, 0, 0)));

		jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Tìm kiếm",
				javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.ABOVE_TOP,
				new java.awt.Font("Dialog", 1, 12), new java.awt.Color(69, 139, 0))); // NOI18N

		txtFindDocgia.setText("DG001");

		btnFindDocGia.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/Search.png"))); // NOI18N
		btnFindDocGia.setText("Tìm");
		btnFindDocGia.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnFindDocGiaActionPerformed(evt);
			}
		});

		javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
		jPanel8.setLayout(jPanel8Layout);
		jPanel8Layout.setHorizontalGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel8Layout.createSequentialGroup().addContainerGap().addComponent(txtFindDocgia)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
						.addComponent(btnFindDocGia).addContainerGap()));
		jPanel8Layout.setVerticalGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
						.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
								.addComponent(txtFindDocgia)
								.addComponent(btnFindDocGia, javax.swing.GroupLayout.Alignment.TRAILING))
						.addContainerGap()));

		jPanel10.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Sách đã mượn",
				javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.ABOVE_TOP,
				new java.awt.Font("Dialog", 1, 12), new java.awt.Color(69, 139, 0))); // NOI18N

		tblSachMuon.setModel(new javax.swing.table.DefaultTableModel(
				new Object[][] { { null, null, null, null }, { null, null, null, null }, { null, null, null, null } },
				new String[] { "Mã PM", "Mã Sách", "Tên Sách", "Trạng thái" }) {
			boolean[] canEdit = new boolean[] { false, false, false, false };

			public boolean isCellEditable(int rowIndex, int columnIndex) {
				return canEdit[columnIndex];
			}
		});
		tblSachMuon.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				tblSachMuonMouseClicked(evt);
			}

			public void mouseReleased(java.awt.event.MouseEvent evt) {
				tblSachMuonMouseReleased(evt);
			}
		});
		jScrollPane1.setViewportView(tblSachMuon);
		if (tblSachMuon.getColumnModel().getColumnCount() > 0) {
			tblSachMuon.getColumnModel().getColumn(0).setResizable(false);
			tblSachMuon.getColumnModel().getColumn(1).setResizable(false);
			tblSachMuon.getColumnModel().getColumn(2).setResizable(false);
			tblSachMuon.getColumnModel().getColumn(3).setResizable(false);
		}

		jCheckBox1.setSelected(true);
		jCheckBox1.setText("Còn hạn");
		jCheckBox1.setEnabled(false);

		jCheckBox2.setText("Hết hạn");
		jCheckBox2.setEnabled(false);

		btnLapPhieuTra.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/Notes.png"))); // NOI18N
		btnLapPhieuTra.setText("Lập phiếu trả");
		btnLapPhieuTra.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnLapPhieuTraActionPerformed(evt);
			}
		});

		btnLapPM.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/Remove from basket.png"))); // NOI18N
		btnLapPM.setText("Lập phiếu mượn");
		btnLapPM.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnLapPMActionPerformed(evt);
			}
		});

		javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
		jPanel10.setLayout(jPanel10Layout);
		jPanel10Layout.setHorizontalGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel10Layout.createSequentialGroup().addContainerGap().addGroup(jPanel10Layout
						.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
						.addGroup(jPanel10Layout.createSequentialGroup()
								.addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
										.addComponent(jCheckBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 82,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addComponent(jCheckBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 82,
												javax.swing.GroupLayout.PREFERRED_SIZE))
								.addGap(0, 0, Short.MAX_VALUE)))
						.addContainerGap())
				.addGroup(jPanel10Layout.createSequentialGroup().addGap(44, 44, 44).addComponent(btnLapPhieuTra)
						.addGap(31, 31, 31).addComponent(btnLapPM).addContainerGap(56, Short.MAX_VALUE)));
		jPanel10Layout.setVerticalGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
						.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 97,
								javax.swing.GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jCheckBox1)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jCheckBox2)
						.addGap(18, 18, 18)
						.addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(btnLapPhieuTra).addComponent(btnLapPM))
						.addGap(81, 81, 81)));

		jPanel13.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Thông tin đọc giả",
				javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.ABOVE_TOP,
				new java.awt.Font("Dialog", 1, 12), new java.awt.Color(69, 139, 0))); // NOI18N

		jLabel18.setText("Mã đọc giả:");

		lblMaDG.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
		lblMaDG.setForeground(new java.awt.Color(69, 139, 0));
		lblMaDG.setText("DG123456");

		jLabel52.setText("Họ và tên:");

		lblHoTen.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
		lblHoTen.setForeground(new java.awt.Color(69, 139, 0));
		lblHoTen.setText("DG123456");

		jLabel54.setText("CCCD:");

		lblCCCD.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
		lblCCCD.setForeground(new java.awt.Color(69, 139, 0));
		lblCCCD.setText("DG123456");

		lblEmail.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
		lblEmail.setForeground(new java.awt.Color(69, 139, 0));
		lblEmail.setText("DG123456");

		jLabel59.setText("Email:");

		jLabel60.setText("Ngày cấp:");

		lblNgayCap.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
		lblNgayCap.setForeground(new java.awt.Color(69, 139, 0));
		lblNgayCap.setText("DG123456");

		lblHan.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
		lblHan.setForeground(new java.awt.Color(69, 139, 0));
		lblHan.setText("DG123456");

		jLabel63.setText("Giá trị đến:");

		lblHinh2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

		javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
		jPanel13.setLayout(jPanel13Layout);
		jPanel13Layout.setHorizontalGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel13Layout.createSequentialGroup().addContainerGap()
						.addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addGroup(jPanel13Layout.createSequentialGroup().addComponent(jLabel18)
										.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
										.addComponent(lblMaDG, javax.swing.GroupLayout.PREFERRED_SIZE, 88,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
								.addGroup(jPanel13Layout.createSequentialGroup().addGap(6, 6, 6).addGroup(jPanel13Layout
										.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
										.addGroup(jPanel13Layout.createSequentialGroup().addGap(16, 16, 16)
												.addGroup(jPanel13Layout
														.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
														.addGroup(jPanel13Layout.createSequentialGroup()
																.addComponent(jLabel59)
																.addPreferredGap(
																		javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
																.addComponent(
																		lblEmail,
																		javax.swing.GroupLayout.PREFERRED_SIZE, 184,
																		javax.swing.GroupLayout.PREFERRED_SIZE))
														.addGroup(jPanel13Layout.createSequentialGroup()
																.addComponent(jLabel54)
																.addPreferredGap(
																		javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
																.addComponent(lblCCCD,
																		javax.swing.GroupLayout.PREFERRED_SIZE, 184,
																		javax.swing.GroupLayout.PREFERRED_SIZE))))
										.addGroup(jPanel13Layout.createSequentialGroup().addComponent(jLabel60)
												.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
												.addGroup(jPanel13Layout
														.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
														.addGroup(jPanel13Layout.createSequentialGroup()
																.addComponent(lblHinh2,
																		javax.swing.GroupLayout.PREFERRED_SIZE, 193,
																		javax.swing.GroupLayout.PREFERRED_SIZE)
																.addGap(0, 0, Short.MAX_VALUE))
														.addGroup(jPanel13Layout.createSequentialGroup()
																.addComponent(lblNgayCap,
																		javax.swing.GroupLayout.PREFERRED_SIZE, 85,
																		javax.swing.GroupLayout.PREFERRED_SIZE)
																.addPreferredGap(
																		javax.swing.LayoutStyle.ComponentPlacement.RELATED,
																		36, Short.MAX_VALUE)
																.addComponent(jLabel63)
																.addPreferredGap(
																		javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
																.addComponent(lblHan,
																		javax.swing.GroupLayout.PREFERRED_SIZE, 130,
																		javax.swing.GroupLayout.PREFERRED_SIZE))))
										.addGroup(jPanel13Layout.createSequentialGroup().addComponent(jLabel52)
												.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
												.addComponent(lblHoTen, javax.swing.GroupLayout.DEFAULT_SIZE,
														javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))));
		jPanel13Layout.setVerticalGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel13Layout.createSequentialGroup().addContainerGap()
						.addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(jLabel18).addComponent(lblMaDG))
						.addGap(17, 17, 17)
						.addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(jLabel52).addComponent(lblHoTen))
						.addGap(18, 18, 18)
						.addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(jLabel54).addComponent(lblCCCD))
						.addGap(18, 18, 18)
						.addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(jLabel59).addComponent(lblEmail))
						.addGap(18, 18, 18)
						.addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(jLabel60).addComponent(lblNgayCap).addComponent(jLabel63)
								.addComponent(lblHan, javax.swing.GroupLayout.PREFERRED_SIZE, 15,
										javax.swing.GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
						.addComponent(lblHinh2, javax.swing.GroupLayout.DEFAULT_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addContainerGap()));

		javax.swing.GroupLayout pnlDocGiaLayout = new javax.swing.GroupLayout(pnlDocGia);
		pnlDocGia.setLayout(pnlDocGiaLayout);
		pnlDocGiaLayout.setHorizontalGroup(pnlDocGiaLayout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(pnlDocGiaLayout.createSequentialGroup().addGap(0, 0, 0)
						.addGroup(pnlDocGiaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
								.addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
						.addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		pnlDocGiaLayout.setVerticalGroup(pnlDocGiaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(pnlDocGiaLayout.createSequentialGroup().addContainerGap().addGroup(pnlDocGiaLayout
						.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addGroup(pnlDocGiaLayout.createSequentialGroup()
								.addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
						.addContainerGap()));

		txtNgayCap1.setEditable(false);

		lblMaThe.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
		lblMaThe.setForeground(new java.awt.Color(39, 139, 0));
		lblMaThe.setText("jLabel6");

		jLabel20.setText("Mã thẻ:");

		jLabel21.setText("Ngày cấp:");

		jLabel22.setText("Ngày hết hạn:");

		txtNgayHetHan.setEditable(false);

		txtLePhi.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				txtLePhiActionPerformed(evt);
			}
		});

		jLabel23.setText("Lệ phí:");

		jLabel24.setText("Gia hạn đến ngày:");

		jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(),
				"Tìm kiếm", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
				javax.swing.border.TitledBorder.ABOVE_TOP, new java.awt.Font("Dialog", 1, 12),
				new java.awt.Color(69, 139, 0))); // NOI18N

		txtSearch.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
		txtSearch.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				txtSearchActionPerformed(evt);
			}
		});

		btnSearch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/Search.png"))); // NOI18N
		btnSearch.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnSearchActionPerformed(evt);
			}
		});

		javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
		jPanel1.setLayout(jPanel1Layout);
		jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel1Layout.createSequentialGroup()
						.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 231,
								javax.swing.GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(btnSearch)
						.addGap(12, 12, 12)));
		jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel1Layout.createSequentialGroup().addGap(10, 10, 10)
						.addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
								.addComponent(txtSearch)
								.addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
						.addContainerGap()));

		btnThem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/Accept.png"))); // NOI18N
		btnThem.setText("Gia hạn");
		btnThem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnThemActionPerformed(evt);
			}
		});

		btnHuy.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/Delete.png"))); // NOI18N
		btnHuy.setText("Hủy");
		btnHuy.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnHuyActionPerformed(evt);
			}
		});

		jPanel12.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Thông tin đọc giả",
				javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.ABOVE_TOP,
				new java.awt.Font("Dialog", 1, 12), new java.awt.Color(69, 139, 0))); // NOI18N

		jLabel16.setText("Mã đọc giả:");

		lblMaDocGia.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
		lblMaDocGia.setForeground(new java.awt.Color(69, 139, 0));
		lblMaDocGia.setText("DG123456");

		jLabel39.setText("Họ và tên:");

		lblHovaTen.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
		lblHovaTen.setForeground(new java.awt.Color(69, 139, 0));
		lblHovaTen.setText("DG123456");

		jLabel41.setText("CCCD:");

		lblCanCuocCD.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
		lblCanCuocCD.setForeground(new java.awt.Color(69, 139, 0));
		lblCanCuocCD.setText("DG123456");

		jLabel43.setText("Địa chỉ:");

		lblDiaChi.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
		lblDiaChi.setForeground(new java.awt.Color(69, 139, 0));
		lblDiaChi.setText("DG123456");

		lblEmailLePhi.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
		lblEmailLePhi.setForeground(new java.awt.Color(69, 139, 0));
		lblEmailLePhi.setText("DG123456");

		jLabel46.setText("Email:");

		jLabel47.setText("Ngày cấp:");

		lblNgayCapLePhi.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
		lblNgayCapLePhi.setForeground(new java.awt.Color(69, 139, 0));
		lblNgayCapLePhi.setText("DG123456");

		lblGiaTriDen.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
		lblGiaTriDen.setForeground(new java.awt.Color(69, 139, 0));
		lblGiaTriDen.setText("DG123456");

		jLabel50.setText("Giá trị đến:");

		lblHinhLePhi.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

		javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
		jPanel12.setLayout(jPanel12Layout);
		jPanel12Layout.setHorizontalGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel12Layout.createSequentialGroup().addContainerGap().addGroup(jPanel12Layout
						.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(jPanel12Layout.createSequentialGroup().addComponent(jLabel16)
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
								.addComponent(lblMaDocGia, javax.swing.GroupLayout.PREFERRED_SIZE, 115,
										javax.swing.GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED,
										javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(jLabel39)
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
								.addComponent(lblHovaTen, javax.swing.GroupLayout.PREFERRED_SIZE, 133,
										javax.swing.GroupLayout.PREFERRED_SIZE))
						.addGroup(jPanel12Layout.createSequentialGroup().addGap(6, 6, 6).addGroup(jPanel12Layout
								.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addGroup(jPanel12Layout.createSequentialGroup().addGap(13, 13, 13)
										.addGroup(jPanel12Layout
												.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
												.addGroup(jPanel12Layout.createSequentialGroup().addComponent(jLabel46)
														.addPreferredGap(
																javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
														.addComponent(lblEmailLePhi,
																javax.swing.GroupLayout.PREFERRED_SIZE, 184,
																javax.swing.GroupLayout.PREFERRED_SIZE))
												.addGroup(jPanel12Layout.createSequentialGroup().addComponent(jLabel43)
														.addPreferredGap(
																javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
														.addComponent(lblDiaChi, javax.swing.GroupLayout.PREFERRED_SIZE,
																184, javax.swing.GroupLayout.PREFERRED_SIZE))
												.addGroup(jPanel12Layout.createSequentialGroup().addComponent(jLabel41)
														.addPreferredGap(
																javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
														.addComponent(lblCanCuocCD,
																javax.swing.GroupLayout.PREFERRED_SIZE, 184,
																javax.swing.GroupLayout.PREFERRED_SIZE))))
								.addGroup(jPanel12Layout.createSequentialGroup().addComponent(jLabel47)
										.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
										.addGroup(jPanel12Layout
												.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
												.addGroup(jPanel12Layout.createSequentialGroup()
														.addComponent(lblNgayCapLePhi,
																javax.swing.GroupLayout.PREFERRED_SIZE, 85,
																javax.swing.GroupLayout.PREFERRED_SIZE)
														.addPreferredGap(
																javax.swing.LayoutStyle.ComponentPlacement.RELATED,
																javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
														.addComponent(jLabel50))
												.addComponent(lblHinhLePhi, javax.swing.GroupLayout.DEFAULT_SIZE,
														javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
										.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
										.addComponent(lblGiaTriDen, javax.swing.GroupLayout.PREFERRED_SIZE, 130,
												javax.swing.GroupLayout.PREFERRED_SIZE)))))
						.addContainerGap()));
		jPanel12Layout.setVerticalGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel12Layout.createSequentialGroup().addContainerGap()
						.addGroup(jPanel12Layout
								.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel16)
								.addComponent(lblMaDocGia).addComponent(jLabel39).addComponent(lblHovaTen))
						.addGap(18, 18, 18)
						.addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(jLabel41).addComponent(lblCanCuocCD))
						.addGap(18, 18, 18)
						.addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(jLabel43).addComponent(lblDiaChi))
						.addGap(18, 18, 18)
						.addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(jLabel46).addComponent(lblEmailLePhi))
						.addGap(18, 18, 18)
						.addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(jLabel47).addComponent(lblNgayCapLePhi).addComponent(jLabel50)
								.addComponent(lblGiaTriDen, javax.swing.GroupLayout.PREFERRED_SIZE, 15,
										javax.swing.GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
						.addComponent(lblHinhLePhi, javax.swing.GroupLayout.PREFERRED_SIZE, 220,
								javax.swing.GroupLayout.PREFERRED_SIZE)
						.addContainerGap(17, Short.MAX_VALUE)));

		javax.swing.GroupLayout pnlLePhiLayout = new javax.swing.GroupLayout(pnlLePhi);
		pnlLePhi.setLayout(pnlLePhiLayout);
		pnlLePhiLayout.setHorizontalGroup(pnlLePhiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(pnlLePhiLayout.createSequentialGroup().addContainerGap()
						.addGroup(pnlLePhiLayout
								.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
								.addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
								.addGroup(
										javax.swing.GroupLayout.Alignment.TRAILING, pnlLePhiLayout
												.createSequentialGroup().addGap(48, 48, 48).addComponent(btnThem)
												.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED,
														javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
												.addComponent(btnHuy))
								.addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlLePhiLayout
										.createSequentialGroup()
										.addGroup(pnlLePhiLayout
												.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
												.addComponent(jLabel23).addComponent(jLabel24).addComponent(jLabel22)
												.addComponent(jLabel21).addComponent(jLabel20))
										.addGap(18, 18, 18)
										.addGroup(pnlLePhiLayout
												.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
												.addComponent(txtLePhi, javax.swing.GroupLayout.Alignment.LEADING)
												.addComponent(txtNgayHetHan, javax.swing.GroupLayout.Alignment.LEADING)
												.addComponent(txtNgayCap1, javax.swing.GroupLayout.Alignment.LEADING)
												.addComponent(txtGiaHan, javax.swing.GroupLayout.Alignment.LEADING)
												.addComponent(lblMaThe, javax.swing.GroupLayout.Alignment.LEADING,
														javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE))))
						.addGap(18, 18, 18).addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addContainerGap()));
		pnlLePhiLayout.setVerticalGroup(pnlLePhiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(pnlLePhiLayout.createSequentialGroup().addContainerGap().addGroup(pnlLePhiLayout
						.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addGroup(pnlLePhiLayout.createSequentialGroup()
								.addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
								.addGap(25, 25, 25)
								.addGroup(pnlLePhiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
										.addComponent(jLabel20).addComponent(lblMaThe))
								.addGap(25, 25, 25)
								.addGroup(pnlLePhiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
										.addComponent(jLabel21).addComponent(txtNgayCap1,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.PREFERRED_SIZE))
								.addGap(25, 25, 25)
								.addGroup(pnlLePhiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
										.addComponent(jLabel22).addComponent(txtNgayHetHan,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.PREFERRED_SIZE))
								.addGap(25, 25, 25)
								.addGroup(pnlLePhiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
										.addComponent(jLabel23).addComponent(txtLePhi,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.PREFERRED_SIZE))
								.addGap(25, 25, 25)
								.addGroup(pnlLePhiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
										.addComponent(jLabel24).addComponent(txtGiaHan,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.PREFERRED_SIZE))
								.addGap(32, 32, 32)
								.addGroup(pnlLePhiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
										.addComponent(btnThem).addComponent(btnHuy))
								.addGap(0, 27, Short.MAX_VALUE)))
						.addContainerGap()));

		pnlTaoThe1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Thông tin thẻ",
				javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.ABOVE_TOP,
				new java.awt.Font("Segoe UI", 1, 12), new java.awt.Color(69, 167, 0))); // NOI18N

		jLabel1.setText("Mã đọc giả:");

		jLabel2.setText("Họ tên:");

		jLabel3.setText("CCCD:");

		jLabel5.setText("Email:");

		jLabel6.setText("Ngày cấp:");

		jLabel7.setText("Hạn sử dụng:");

		txtNgayCap.setEditable(false);

		txtHanSuDung.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				txtHanSuDungActionPerformed(evt);
			}
		});

		jLabel8.setText("SDT:");

		pnlanh.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

		lblAnh.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mousePressed(java.awt.event.MouseEvent evt) {
				lblAnhMousePressed(evt);
			}
		});

		javax.swing.GroupLayout pnlanhLayout = new javax.swing.GroupLayout(pnlanh);
		pnlanh.setLayout(pnlanhLayout);
		pnlanhLayout.setHorizontalGroup(pnlanhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addComponent(lblAnh, javax.swing.GroupLayout.DEFAULT_SIZE, 223, Short.MAX_VALUE));
		pnlanhLayout.setVerticalGroup(pnlanhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addComponent(lblAnh, javax.swing.GroupLayout.DEFAULT_SIZE, 226, Short.MAX_VALUE));

		btnTaoTheDocGia.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/Add.png"))); // NOI18N
		btnTaoTheDocGia.setText("Tạo thẻ");
		btnTaoTheDocGia.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnTaoTheDocGiaActionPerformed(evt);
			}
		});

		btnCanceltaoThedg.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/Delete.png"))); // NOI18N
		btnCanceltaoThedg.setText("Cancel");
		btnCanceltaoThedg.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnCanceltaoThedgActionPerformed(evt);
			}
		});

		jLabel4.setText("Địa chỉ: ");

		javax.swing.GroupLayout pnlTaoThe1Layout = new javax.swing.GroupLayout(pnlTaoThe1);
		pnlTaoThe1.setLayout(pnlTaoThe1Layout);
		pnlTaoThe1Layout.setHorizontalGroup(pnlTaoThe1Layout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(pnlTaoThe1Layout.createSequentialGroup().addContainerGap(90, Short.MAX_VALUE)
						.addGroup(pnlTaoThe1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addGroup(pnlTaoThe1Layout
										.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
										.addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 71,
												javax.swing.GroupLayout.PREFERRED_SIZE))
								.addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 50,
										javax.swing.GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(pnlTaoThe1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addComponent(txtHanSuDung, javax.swing.GroupLayout.PREFERRED_SIZE, 179,
										javax.swing.GroupLayout.PREFERRED_SIZE)
								.addComponent(txtMaDocGia, javax.swing.GroupLayout.PREFERRED_SIZE, 179,
										javax.swing.GroupLayout.PREFERRED_SIZE)
								.addGroup(pnlTaoThe1Layout.createSequentialGroup()
										.addGroup(pnlTaoThe1Layout
												.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
												.addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 179,
														javax.swing.GroupLayout.PREFERRED_SIZE)
												.addComponent(txtSDT, javax.swing.GroupLayout.PREFERRED_SIZE, 179,
														javax.swing.GroupLayout.PREFERRED_SIZE)
												.addComponent(txtCCCD, javax.swing.GroupLayout.PREFERRED_SIZE, 179,
														javax.swing.GroupLayout.PREFERRED_SIZE)
												.addComponent(txtTenDocGia, javax.swing.GroupLayout.PREFERRED_SIZE, 179,
														javax.swing.GroupLayout.PREFERRED_SIZE)
												.addComponent(txtDiaChi, javax.swing.GroupLayout.PREFERRED_SIZE, 179,
														javax.swing.GroupLayout.PREFERRED_SIZE)
												.addComponent(txtNgayCap, javax.swing.GroupLayout.PREFERRED_SIZE, 179,
														javax.swing.GroupLayout.PREFERRED_SIZE))
										.addGap(55, 55, 55).addComponent(pnlanh, javax.swing.GroupLayout.PREFERRED_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.PREFERRED_SIZE)))
						.addGap(43, 43, 43))
				.addGroup(pnlTaoThe1Layout.createSequentialGroup().addGap(230, 230, 230).addComponent(btnCanceltaoThedg)
						.addGap(57, 57, 57).addComponent(btnTaoTheDocGia)
						.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		pnlTaoThe1Layout.setVerticalGroup(pnlTaoThe1Layout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(pnlTaoThe1Layout.createSequentialGroup().addContainerGap(19, Short.MAX_VALUE)
						.addGroup(pnlTaoThe1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(jLabel1).addComponent(txtMaDocGia, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
						.addGroup(pnlTaoThe1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addGroup(pnlTaoThe1Layout.createSequentialGroup().addGap(18, 18, 18)
										.addGroup(pnlTaoThe1Layout
												.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
												.addComponent(txtTenDocGia, javax.swing.GroupLayout.PREFERRED_SIZE,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														javax.swing.GroupLayout.PREFERRED_SIZE)
												.addComponent(jLabel2))
										.addGap(18, 18, 18)
										.addGroup(pnlTaoThe1Layout
												.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
												.addComponent(jLabel3)
												.addComponent(txtCCCD, javax.swing.GroupLayout.PREFERRED_SIZE,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														javax.swing.GroupLayout.PREFERRED_SIZE))
										.addGap(18, 18, 18)
										.addGroup(pnlTaoThe1Layout
												.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
												.addComponent(jLabel8).addComponent(txtSDT,
														javax.swing.GroupLayout.PREFERRED_SIZE,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														javax.swing.GroupLayout.PREFERRED_SIZE))
										.addGap(18, 18, 18)
										.addGroup(pnlTaoThe1Layout
												.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
												.addComponent(jLabel5).addComponent(txtEmail,
														javax.swing.GroupLayout.PREFERRED_SIZE,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														javax.swing.GroupLayout.PREFERRED_SIZE))
										.addGap(18, 18, 18)
										.addGroup(pnlTaoThe1Layout
												.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
												.addComponent(jLabel4).addComponent(txtDiaChi,
														javax.swing.GroupLayout.PREFERRED_SIZE,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														javax.swing.GroupLayout.PREFERRED_SIZE))
										.addGap(18, 18, 18)
										.addGroup(pnlTaoThe1Layout
												.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
												.addComponent(txtNgayCap, javax.swing.GroupLayout.PREFERRED_SIZE,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														javax.swing.GroupLayout.PREFERRED_SIZE)
												.addComponent(jLabel6)))
								.addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
										pnlTaoThe1Layout.createSequentialGroup().addGap(10, 10, 10).addComponent(pnlanh,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.PREFERRED_SIZE)))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(pnlTaoThe1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(jLabel7).addComponent(txtHanSuDung,
										javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.PREFERRED_SIZE))
						.addGap(18, 18, 18)
						.addGroup(pnlTaoThe1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(btnCanceltaoThedg).addComponent(btnTaoTheDocGia))
						.addGap(17, 17, 17)));

		javax.swing.GroupLayout pnlTaoTheLayout = new javax.swing.GroupLayout(pnlTaoThe);
		pnlTaoThe.setLayout(pnlTaoTheLayout);
		pnlTaoTheLayout.setHorizontalGroup(
				pnlTaoTheLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
						javax.swing.GroupLayout.Alignment.TRAILING,
						pnlTaoTheLayout.createSequentialGroup().addContainerGap(91, Short.MAX_VALUE)
								.addComponent(pnlTaoThe1, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
								.addGap(70, 70, 70)));
		pnlTaoTheLayout.setVerticalGroup(pnlTaoTheLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(pnlTaoTheLayout.createSequentialGroup().addGap(21, 21, 21)
						.addComponent(pnlTaoThe1, javax.swing.GroupLayout.PREFERRED_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addContainerGap(23, Short.MAX_VALUE)));

		jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Thông tin sách",
				javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION,
				new java.awt.Font("Segoe UI", 1, 12), new java.awt.Color(69, 167, 0))); // NOI18N

		tblThongTinSach
				.setModel(new javax.swing.table.DefaultTableModel(
						new Object[][] { { null, null, null, null }, { null, null, null, null },
								{ null, null, null, null }, { null, null, null, null } },
						new String[] { "Tên sách", "Mã sách", "Số lượng", "Giá" }) {
					boolean[] canEdit = new boolean[] { false, false, true, true };

					public boolean isCellEditable(int rowIndex, int columnIndex) {
						return canEdit[columnIndex];
					}
				});
		tblThongTinSach.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseReleased(java.awt.event.MouseEvent evt) {
				tblThongTinSachMouseReleased(evt);
			}
		});
		jScrollPane2.setViewportView(tblThongTinSach);

		btnCancelsach.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/Delete.png"))); // NOI18N
		btnCancelsach.setText("Cancel");
		btnCancelsach.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnCancelsachActionPerformed(evt);
			}
		});

		btnOksach.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/Accept.png"))); // NOI18N
		btnOksach.setText("Ok");
		btnOksach.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnOksachActionPerformed(evt);
			}
		});

		javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
		jPanel2.setLayout(jPanel2Layout);
		jPanel2Layout.setHorizontalGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel2Layout.createSequentialGroup().addGap(53, 53, 53).addComponent(btnCancelsach)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 141, Short.MAX_VALUE)
						.addComponent(btnOksach, javax.swing.GroupLayout.PREFERRED_SIZE, 83,
								javax.swing.GroupLayout.PREFERRED_SIZE)
						.addGap(71, 71, 71))
				.addGroup(jPanel2Layout.createSequentialGroup().addContainerGap()
						.addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
						.addContainerGap()));
		jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel2Layout.createSequentialGroup()
						.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 166,
								javax.swing.GroupLayout.PREFERRED_SIZE)
						.addGap(18, 18, 18)
						.addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(btnCancelsach, javax.swing.GroupLayout.PREFERRED_SIZE, 35,
										javax.swing.GroupLayout.PREFERRED_SIZE)
								.addComponent(btnOksach, javax.swing.GroupLayout.PREFERRED_SIZE, 35,
										javax.swing.GroupLayout.PREFERRED_SIZE))
						.addContainerGap()));

		jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Tìm kiếm",
				javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION,
				new java.awt.Font("Segoe UI", 1, 12), new java.awt.Color(69, 167, 0))); // NOI18N

		txtSearch1.setText("s001");
		txtSearch1.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(java.awt.event.KeyEvent evt) {
				txtSearch1KeyReleased(evt);
			}
		});

		btnTim.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/Search.png"))); // NOI18N
		btnTim.setText("Tìm");
		btnTim.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnTimActionPerformed(evt);
			}
		});

		javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
		jPanel3.setLayout(jPanel3Layout);
		jPanel3Layout.setHorizontalGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel3Layout.createSequentialGroup().addContainerGap().addComponent(txtSearch1)
						.addGap(18, 18, 18).addComponent(btnTim).addContainerGap()));
		jPanel3Layout.setVerticalGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
						jPanel3Layout.createSequentialGroup().addContainerGap(12, Short.MAX_VALUE)
								.addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
										.addComponent(txtSearch1, javax.swing.GroupLayout.PREFERRED_SIZE, 30,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addComponent(btnTim))
								.addContainerGap()));

		jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Thông tin phiếu mượn",
				javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION,
				new java.awt.Font("Segoe UI", 1, 12), new java.awt.Color(69, 167, 0))); // NOI18N

		jLabel9.setText("Mã phiếu:");

		jLabel10.setText("Mã đọc giả:");

		jLabel11.setText("Mã sách:");

		jLabel12.setText("Tên sách:");

		jLabel13.setText("Giá sách:");

		jLabel14.setText("Ngày mượn:");

		jLabel15.setText("Trả hạn:");

		txtMaPhieuMuon.setEditable(false);

		txtMaDG.setEditable(false);

		txtMaSach.setEditable(false);

		txtTenSach.setEditable(false);

		txtGia.setEditable(false);

		txtNgayMuon.setEditable(false);

		txtNgayTra.setEditable(false);

		btnOkPhieu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/Accept.png"))); // NOI18N
		btnOkPhieu.setText("Ok");
		btnOkPhieu.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnOkPhieuActionPerformed(evt);
			}
		});

		btnCancelPhieu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/Left.png"))); // NOI18N
		btnCancelPhieu.setText("Quay lại");
		btnCancelPhieu.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnCancelPhieuActionPerformed(evt);
			}
		});

		javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
		jPanel4.setLayout(jPanel4Layout);
		jPanel4Layout.setHorizontalGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel4Layout.createSequentialGroup().addGap(34, 34, 34)
						.addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
								.addGroup(jPanel4Layout.createSequentialGroup()
										.addGroup(jPanel4Layout
												.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
												.addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE,
														javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
												.addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE,
														javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
												.addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE,
														javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
												.addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE,
														javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
												.addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE,
														javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
												.addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE,
														javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
												.addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 74,
														javax.swing.GroupLayout.PREFERRED_SIZE))
										.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addGroup(jPanel4Layout
												.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
												.addComponent(txtMaPhieuMuon).addComponent(txtMaDG)
												.addComponent(txtMaSach).addComponent(txtTenSach).addComponent(txtGia)
												.addComponent(txtNgayMuon).addComponent(txtNgayTra,
														javax.swing.GroupLayout.PREFERRED_SIZE, 182,
														javax.swing.GroupLayout.PREFERRED_SIZE)))
								.addGroup(javax.swing.GroupLayout.Alignment.LEADING,
										jPanel4Layout.createSequentialGroup().addGap(24, 24, 24)
												.addComponent(btnCancelPhieu)
												.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED,
														javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
												.addComponent(btnOkPhieu, javax.swing.GroupLayout.PREFERRED_SIZE, 83,
														javax.swing.GroupLayout.PREFERRED_SIZE)
												.addGap(17, 17, 17)))
						.addContainerGap(45, Short.MAX_VALUE)));
		jPanel4Layout.setVerticalGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel4Layout.createSequentialGroup().addGap(13, 13, 13)
						.addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(jLabel9)
								.addComponent(txtMaPhieuMuon, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
						.addGap(18, 18, 18)
						.addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(jLabel10).addComponent(txtMaDG, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
						.addGap(18, 18, 18)
						.addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(jLabel11).addComponent(txtMaSach, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
						.addGap(18, 18, 18)
						.addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(jLabel12).addComponent(txtTenSach, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
						.addGap(18, 18, 18)
						.addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(jLabel13).addComponent(txtGia, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
						.addGap(18, 18, 18)
						.addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(jLabel14)
								.addComponent(txtNgayMuon, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
						.addGap(18, 18, 18)
						.addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(jLabel15).addComponent(txtNgayTra, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
						.addGap(18, 18, 18)
						.addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(btnOkPhieu, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(btnCancelPhieu))
						.addContainerGap()));

		jLabel19.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
		jLabel19.setForeground(new java.awt.Color(39, 169, 0));
		jLabel19.setText("PHIẾU MƯỢN");

		javax.swing.GroupLayout pnlPhieuMuonLayout = new javax.swing.GroupLayout(pnlPhieuMuon);
		pnlPhieuMuon.setLayout(pnlPhieuMuonLayout);
		pnlPhieuMuonLayout
				.setHorizontalGroup(pnlPhieuMuonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(pnlPhieuMuonLayout.createSequentialGroup().addContainerGap()
								.addGroup(pnlPhieuMuonLayout
										.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
										.addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
								.addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addContainerGap())
						.addGroup(
								pnlPhieuMuonLayout.createSequentialGroup().addGap(359, 359, 359).addComponent(jLabel19)
										.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		pnlPhieuMuonLayout
				.setVerticalGroup(pnlPhieuMuonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlPhieuMuonLayout.createSequentialGroup()
								.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(jLabel19).addGap(18, 18, 18)
								.addGroup(pnlPhieuMuonLayout
										.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
										.addGroup(pnlPhieuMuonLayout.createSequentialGroup()
												.addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														javax.swing.GroupLayout.PREFERRED_SIZE)
												.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
												.addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														javax.swing.GroupLayout.PREFERRED_SIZE))
										.addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
								.addContainerGap()));

		pnlPTra4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Phiếu trả",
				javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.ABOVE_TOP,
				new java.awt.Font("Dialog", 1, 14), new java.awt.Color(69, 167, 0))); // NOI18N

		jLabel85.setText("Mã phiếu trả:");

		jLabel86.setText("Mã phiếu mượn: ");

		jLabel87.setText("Mã sách: ");

		jLabel88.setText("Tên sách:");

		jLabel89.setText("Ngày mượn:");

		jLabel90.setText("Ngày trả: ");

		txtMaPhieuTra5.setEditable(false);

		txtMaPhieuMuon4.setEditable(false);

		txtMasach5.setEditable(false);

		txtTenSach4.setEditable(false);

		txtNgayMuon4.setEditable(false);

		txtNgayTra4.setEditable(false);

		btnLapPhieuPhat4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/Problem.png"))); // NOI18N
		btnLapPhieuPhat4.setText("Lập phiếu phạt");
		btnLapPhieuPhat4.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnLapPhieuPhat4ActionPerformed(evt);
			}
		});

		btnTraSach4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/Refresh.png"))); // NOI18N
		btnTraSach4.setText("Trả sách");
		btnTraSach4.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnTraSach4ActionPerformed(evt);
			}
		});

		tbnCancelPhat1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/Delete.png"))); // NOI18N
		tbnCancelPhat1.setText("Cancel");
		tbnCancelPhat1.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				tbnCancelPhat1ActionPerformed(evt);
			}
		});

		javax.swing.GroupLayout pnlPTra4Layout = new javax.swing.GroupLayout(pnlPTra4);
		pnlPTra4.setLayout(pnlPTra4Layout);
		pnlPTra4Layout.setHorizontalGroup(pnlPTra4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
						pnlPTra4Layout.createSequentialGroup().addGap(24, 24, 24).addComponent(btnTraSach4)
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 61,
										Short.MAX_VALUE)
								.addComponent(btnLapPhieuPhat4).addGap(25, 25, 25))
				.addGroup(pnlPTra4Layout.createSequentialGroup().addContainerGap()
						.addGroup(pnlPTra4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
								.addComponent(jLabel85, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(jLabel86, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(jLabel87, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(jLabel88, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(jLabel89, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(jLabel90, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(pnlPTra4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addGroup(pnlPTra4Layout.createSequentialGroup().addComponent(lblCanhBaoQuaHan)
										.addGap(0, 0, Short.MAX_VALUE))
								.addComponent(txtMaPhieuTra5).addComponent(txtMaPhieuMuon4).addComponent(txtMasach5)
								.addComponent(txtTenSach4).addComponent(txtNgayMuon4).addComponent(txtNgayTra4))
						.addContainerGap())
				.addGroup(pnlPTra4Layout.createSequentialGroup().addGap(114, 114, 114).addComponent(tbnCancelPhat1)
						.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		pnlPTra4Layout.setVerticalGroup(pnlPTra4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(pnlPTra4Layout.createSequentialGroup().addContainerGap()
						.addGroup(pnlPTra4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(jLabel85)
								.addComponent(txtMaPhieuTra5, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
						.addGap(18, 18, 18)
						.addGroup(pnlPTra4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(jLabel86)
								.addComponent(txtMaPhieuMuon4, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
						.addGap(18, 18, 18)
						.addGroup(pnlPTra4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(jLabel87).addComponent(txtMasach5, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
						.addGap(18, 18, 18)
						.addGroup(pnlPTra4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(jLabel88)
								.addComponent(txtTenSach4, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
						.addGap(18, 18, 18)
						.addGroup(pnlPTra4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(jLabel89)
								.addComponent(txtNgayMuon4, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
						.addGap(18, 18, 18)
						.addGroup(pnlPTra4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(jLabel90).addComponent(txtNgayTra4,
										javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
						.addComponent(lblCanhBaoQuaHan).addGap(18, 18, 18)
						.addGroup(pnlPTra4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(btnTraSach4).addComponent(btnLapPhieuPhat4))
						.addGap(18, 18, 18).addComponent(tbnCancelPhat1).addContainerGap(34, Short.MAX_VALUE)));

		pnlPPhat.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Phiếu phạt",
				javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.ABOVE_TOP,
				new java.awt.Font("Dialog", 1, 14), new java.awt.Color(69, 167, 0))); // NOI18N

		jLabel31.setText("Mã phiếu phạt:");

		jLabel32.setText("Mã phiếu trả:");

		jLabel33.setText("Mã sách:");

		jLabel34.setText("Ngày phạt:");

		jLabel35.setText("Lý do phạt: ");

		jLabel36.setText("Phí phạt:");

		jLabel37.setText("Mã nhân viên: ");

		txtMaphieuphat.setEditable(false);

		txtMaphieutra2.setEditable(false);

		txtMasach2.setEditable(false);

		txtNgayPhat.setEditable(false);

		cboLyDo.setModel(new javax.swing.DefaultComboBoxModel<>(
				new String[] { "Chọn lý do phạt", "Mất sách (100%)", "Hỏng sách (70%)", "Trễ hạn (10k/1 ngày)" }));
		cboLyDo.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				cboLyDoActionPerformed(evt);
			}
		});

		txtPhi.setEditable(false);

		txtMaNhanVien.setEditable(false);

		tbnCancelPhat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/Delete.png"))); // NOI18N
		tbnCancelPhat.setText("Cancel");
		tbnCancelPhat.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				tbnCancelPhatActionPerformed(evt);
			}
		});

		btnOkphat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/Accept.png"))); // NOI18N
		btnOkphat.setText("Ok");
		btnOkphat.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnOkphatActionPerformed(evt);
			}
		});

		javax.swing.GroupLayout pnlPPhatLayout = new javax.swing.GroupLayout(pnlPPhat);
		pnlPPhat.setLayout(pnlPPhatLayout);
		pnlPPhatLayout.setHorizontalGroup(pnlPPhatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(pnlPPhatLayout.createSequentialGroup().addContainerGap()
						.addGroup(pnlPPhatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addGroup(pnlPPhatLayout.createSequentialGroup()
										.addGroup(pnlPPhatLayout
												.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
												.addComponent(jLabel37, javax.swing.GroupLayout.DEFAULT_SIZE,
														javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
												.addComponent(jLabel31, javax.swing.GroupLayout.DEFAULT_SIZE,
														javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
												.addComponent(jLabel32, javax.swing.GroupLayout.DEFAULT_SIZE,
														javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
												.addComponent(jLabel34, javax.swing.GroupLayout.DEFAULT_SIZE,
														javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
												.addComponent(jLabel33, javax.swing.GroupLayout.DEFAULT_SIZE,
														javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
												.addComponent(jLabel35, javax.swing.GroupLayout.DEFAULT_SIZE,
														javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
												.addComponent(jLabel36, javax.swing.GroupLayout.DEFAULT_SIZE,
														javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
										.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
										.addGroup(pnlPPhatLayout
												.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
												.addComponent(txtMaphieuphat).addComponent(txtMaphieutra2)
												.addComponent(txtMasach2).addComponent(txtNgayPhat)
												.addComponent(cboLyDo, 0, 276, Short.MAX_VALUE).addComponent(txtPhi)
												.addComponent(txtMaNhanVien)))
								.addGroup(pnlPPhatLayout.createSequentialGroup().addGap(58, 58, 58)
										.addComponent(tbnCancelPhat).addGap(59, 59, 59)
										.addComponent(btnOkphat, javax.swing.GroupLayout.PREFERRED_SIZE, 83,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addGap(0, 0, Short.MAX_VALUE)))
						.addContainerGap()));
		pnlPPhatLayout.setVerticalGroup(pnlPPhatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(pnlPPhatLayout.createSequentialGroup().addContainerGap()
						.addGroup(pnlPPhatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(jLabel31)
								.addComponent(txtMaphieuphat, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
						.addGap(18, 18, 18)
						.addGroup(pnlPPhatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(jLabel32)
								.addComponent(txtMaphieutra2, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
						.addGap(18, 18, 18)
						.addGroup(pnlPPhatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(jLabel33).addComponent(txtMasach2, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
						.addGap(18, 18, 18)
						.addGroup(pnlPPhatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(jLabel34)
								.addComponent(txtNgayPhat, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
						.addGap(18, 18, 18)
						.addGroup(pnlPPhatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(jLabel35).addComponent(cboLyDo, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
						.addGap(18, 18, 18)
						.addGroup(pnlPPhatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(jLabel36).addComponent(txtPhi, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
						.addGap(18, 18, 18)
						.addGroup(pnlPPhatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(jLabel37).addComponent(txtMaNhanVien,
										javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED,
								javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addGroup(pnlPPhatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(tbnCancelPhat).addComponent(btnOkphat))
						.addGap(36, 36, 36)));

		javax.swing.GroupLayout pnlPhieuTraPhatLayout = new javax.swing.GroupLayout(pnlPhieuTraPhat);
		pnlPhieuTraPhat.setLayout(pnlPhieuTraPhatLayout);
		pnlPhieuTraPhatLayout
				.setHorizontalGroup(pnlPhieuTraPhatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(pnlPhieuTraPhatLayout.createSequentialGroup().addGap(31, 31, 31)
								.addComponent(pnlPTra4, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
								.addGap(18, 18, 18)
								.addComponent(pnlPPhat, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
								.addContainerGap(31, Short.MAX_VALUE)));
		pnlPhieuTraPhatLayout.setVerticalGroup(pnlPhieuTraPhatLayout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(pnlPhieuTraPhatLayout.createSequentialGroup().addContainerGap()
						.addGroup(pnlPhieuTraPhatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addComponent(pnlPTra4, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(pnlPPhat, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
						.addContainerGap()));

		jLayeredPane1.setLayer(pnl, javax.swing.JLayeredPane.DEFAULT_LAYER);
		jLayeredPane1.setLayer(pnlDocGia, javax.swing.JLayeredPane.DEFAULT_LAYER);
		jLayeredPane1.setLayer(pnlLePhi, javax.swing.JLayeredPane.DEFAULT_LAYER);
		jLayeredPane1.setLayer(pnlTaoThe, javax.swing.JLayeredPane.DEFAULT_LAYER);
		jLayeredPane1.setLayer(pnlPhieuMuon, javax.swing.JLayeredPane.DEFAULT_LAYER);
		jLayeredPane1.setLayer(pnlPhieuTraPhat, javax.swing.JLayeredPane.DEFAULT_LAYER);

		javax.swing.GroupLayout jLayeredPane1Layout = new javax.swing.GroupLayout(jLayeredPane1);
		jLayeredPane1.setLayout(jLayeredPane1Layout);
		jLayeredPane1Layout
				.setHorizontalGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addComponent(pnl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
								Short.MAX_VALUE)
						.addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addComponent(pnlDocGia, javax.swing.GroupLayout.Alignment.TRAILING,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE))
						.addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addComponent(pnlLePhi, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
						.addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addComponent(pnlTaoThe, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
						.addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addComponent(pnlPhieuMuon, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
						.addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addComponent(pnlPhieuTraPhat, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		jLayeredPane1Layout
				.setVerticalGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addComponent(pnl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
								Short.MAX_VALUE)
						.addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addComponent(pnlDocGia, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
						.addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addComponent(pnlLePhi, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
						.addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addComponent(pnlTaoThe, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
						.addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addComponent(pnlPhieuMuon, javax.swing.GroupLayout.Alignment.TRAILING,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE))
						.addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addComponent(pnlPhieuTraPhat, javax.swing.GroupLayout.Alignment.TRAILING,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE)));

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addContainerGap()
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
								.addComponent(jLayeredPane1)
								.addComponent(tbaCongCu, javax.swing.GroupLayout.Alignment.LEADING,
										javax.swing.GroupLayout.DEFAULT_SIZE, 848, Short.MAX_VALUE)
								.addComponent(pnlTrangThai, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
						.addContainerGap()));
		layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup().addContainerGap()
						.addComponent(tbaCongCu, javax.swing.GroupLayout.PREFERRED_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addGap(0, 0, 0).addComponent(jLayeredPane1).addGap(0, 0, 0)
						.addComponent(pnlTrangThai, javax.swing.GroupLayout.PREFERRED_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addContainerGap()));

		pack();
	}// </editor-fold>//GEN-END:initComponents

	private void btnDocGiaActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnDocGiaActionPerformed
		display("pnlDocGia");

	}// GEN-LAST:event_btnDocGiaActionPerformed

	private void formWindowOpened(java.awt.event.WindowEvent evt) {// GEN-FIRST:event_formWindowOpened
		display("pnl");
	}// GEN-LAST:event_formWindowOpened

	private void txtSearchActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtSearchActionPerformed
		// TODO add your handling code here:
	}// GEN-LAST:event_txtSearchActionPerformed

	private void btnLePhiActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnLePhiActionPerformed
		display("pnlLePhi");
	}// GEN-LAST:event_btnLePhiActionPerformed

	private void btnTaoTheActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnTaoTheActionPerformed
		display("pnlTaoThe");
		LocalDateTime currentDateTime = LocalDateTime.now();
		LocalDate currentDate = currentDateTime.toLocalDate();
		txtNgayCap.setText(String.valueOf(currentDate));
	}// GEN-LAST:event_btnTaoTheActionPerformed

	private void btnHuyActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnHuyActionPerformed
		// TODO add your handling code here:
	}// GEN-LAST:event_btnHuyActionPerformed

	private void btnCancelsachActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnCancelsachActionPerformed
//        tblThongTinSach.removeRowSelectionInterval(index, index);
		clearPM();
		btnOkPhieu.setEnabled(false);
		btnOksach.setEnabled(false);
		txtSearch1.setText("");
		listSach.clear();
		findSach();
	}// GEN-LAST:event_btnCancelsachActionPerformed

	private void btnOksachActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnOksachActionPerformed
		lapPhieuMuon();
		btnOkPhieu.setEnabled(true);
	}// GEN-LAST:event_btnOksachActionPerformed

	private void btnOkPhieuActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnOkPhieuActionPerformed
		LuuPhieuMuon();
	}// GEN-LAST:event_btnOkPhieuActionPerformed

	private void btnCancelPhieuActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnCancelPhieuActionPerformed
		display("pnlDocGia");
		clearPM();
		btnOkPhieu.setEnabled(false);
		btnOksach.setEnabled(false);
		txtSearch1.setText("");
		listSach.clear();
		findSach();
	}// GEN-LAST:event_btnCancelPhieuActionPerformed

	private void btnLapPMActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnLapPMActionPerformed
		display("pnlPhieuMuon");
		clearPM();
		txtMaDG.setText(txtFindDocgia.getText());
		btnOkPhieu.setEnabled(false);
		btnOksach.setEnabled(false);
	}// GEN-LAST:event_btnLapPMActionPerformed

	private void btnLapPhieuTraActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnLapPhieuTraActionPerformed
		display("pnlPhieuTraPhat");
		lapPhieuTra();
		btnLapPhieuPhat4.setEnabled(false);///////////////////////////////////////////////////////////////////////

		tbnCancelPhat.setEnabled(false);
		btnOkphat.setEnabled(false);
	}// GEN-LAST:event_btnLapPhieuTraActionPerformed

	private void tbnCancelPhatActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_tbnCancelPhatActionPerformed
		txtMaphieuphat.setText("");
		txtMaphieutra2.setText("");
		txtMasach2.setText("");
		txtNgayPhat.setText("");
		cboLyDo.setSelectedIndex(0);
		txtPhi.setText("");
		txtMaNhanVien.setText("");
	}// GEN-LAST:event_tbnCancelPhatActionPerformed

	private void btnOkphatActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnOkphatActionPerformed
		luuPhieuPhat();
	}// GEN-LAST:event_btnOkphatActionPerformed

	private void tbnCancelPhat1ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_tbnCancelPhat1ActionPerformed
		txtMaPhieuTra5.setText("");
		txtMaPhieuMuon4.setText("");
		txtMasach5.setText("");
		txtTenSach4.setText("");
		txtNgayMuon4.setText("");
		txtNgayTra4.setText("");
		display("pnlDocGia");
		btnLapPhieuTra.setEnabled(false);
		tbnCancelPhat.setEnabled(false);
		btnOkphat.setEnabled(false);
		findDocGia();
		if (tblSachMuon.getRowCount() < 3) {
			btnLapPM.setEnabled(true);
		}
	}// GEN-LAST:event_tbnCancelPhat1ActionPerformed

	private void btnFindDocGiaActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnFindDocGiaActionPerformed
		findDocGia();
		btnLapPhieuTra.setEnabled(false);
	}// GEN-LAST:event_btnFindDocGiaActionPerformed

	private void tblSachMuonMouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_tblSachMuonMouseClicked

		btnLapPhieuTra.setEnabled(true);

	}// GEN-LAST:event_tblSachMuonMouseClicked

	private void btnTimActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnTimActionPerformed
		findSach();
		btnCancelsach.setEnabled(true);
		btnOksach.setEnabled(true);
	}// GEN-LAST:event_btnTimActionPerformed

	private void txtSearch1KeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_txtSearch1KeyReleased

	}// GEN-LAST:event_txtSearch1KeyReleased

	private void tblSachMuonMouseReleased(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_tblSachMuonMouseReleased
		if (this.tblSachMuon.getCellEditor() != null)
			this.tblSachMuon.getCellEditor().cancelCellEditing();
	}// GEN-LAST:event_tblSachMuonMouseReleased

	private void tblThongTinSachMouseReleased(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_tblThongTinSachMouseReleased
		if (this.tblThongTinSach.getCellEditor() != null)
			this.tblThongTinSach.getCellEditor().cancelCellEditing();
	}// GEN-LAST:event_tblThongTinSachMouseReleased

	private void btnTraSach4ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnTraSach4ActionPerformed
		luuPhieuTra();
		btnLapPhieuPhat4.setEnabled(true);
	}// GEN-LAST:event_btnTraSach4ActionPerformed

	private void btnLapPhieuPhat4ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnLapPhieuPhat4ActionPerformed
		tbnCancelPhat.setEnabled(true);
		btnOkphat.setEnabled(true);

		lapPhieuPhat();
	}// GEN-LAST:event_btnLapPhieuPhat4ActionPerformed

	private void cboLyDoActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cboLyDoActionPerformed

		Double gia = 0.0;
		Double phi = 0.0;
		for (Sach sa : listSach) {
			if (sa.getMaSach().equalsIgnoreCase(txtMasach5.getText())) {
				gia = sa.getGiaSach();
			}
		}
		Date n1 = new Date();
		Date n2 = XDate.toDate(txtNgayMuon4.getText());
		double n = XDate.SoNgay(n1, n2);
		if (cboLyDo.getSelectedIndex() == 1) {
			phi = gia;
		} else if (cboLyDo.getSelectedIndex() == 2) {
			phi = 0.7 * gia;
		} else if (cboLyDo.getSelectedIndex() == 3) {
			phi = 10000 * n;
		} else {
			JOptionPane.showMessageDialog(this, "Vui lòng chọn lý do phạt");
		}
		txtPhi.setText(Double.toString(phi));

	}// GEN-LAST:event_cboLyDoActionPerformed

	private void btnTaoTheDocGiaActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnTaoTheDocGiaActionPerformed
		try {
			taoThe();
		} catch (SQLException ex) {
			Logger.getLogger(ThuVienJFrame.class.getName()).log(Level.SEVERE, null, ex);
		}
	}// GEN-LAST:event_btnTaoTheDocGiaActionPerformed

	private void lblAnhMousePressed(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_lblAnhMousePressed
		JFileChooser jc = new JFileChooser();
		jc.setCurrentDirectory(new File("D://"));
		int result = jc.showOpenDialog(this);
		if (result == jc.APPROVE_OPTION) {
			f1 = jc.getSelectedFile();
			imageSave = dir + "\\" + f1.getName();
			String iName = f1.getAbsolutePath();
			ImageIcon ic = new ImageIcon(iName);
			Image scaledImage = ic.getImage().getScaledInstance(lblAnh.getWidth(), lblAnh.getHeight(),
					Image.SCALE_SMOOTH);
			ic.setImage(scaledImage); // Cập nhật hình ảnh mới đã co giãn cho ImageIcon
			lblAnh.setText("");
			lblAnh.setHorizontalAlignment(JLabel.CENTER);
			lblAnh.setVerticalAlignment(JLabel.CENTER);
			lblAnh.setIcon(ic);
		}
	}// GEN-LAST:event_lblAnhMousePressed

	private void btnCanceltaoThedgActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnCanceltaoThedgActionPerformed
		cancelTaoThe();
	}// GEN-LAST:event_btnCanceltaoThedgActionPerformed

	private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnSearchActionPerformed
		findLePhi();
	}// GEN-LAST:event_btnSearchActionPerformed

	private void txtHanSuDungActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtHanSuDungActionPerformed
		// TODO add your handling code here:
	}// GEN-LAST:event_txtHanSuDungActionPerformed

	private void btnThemActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnThemActionPerformed
		giaHan();
	}// GEN-LAST:event_btnThemActionPerformed

	private void txtLePhiActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtLePhiActionPerformed
		// TODO add your handling code here:
	}// GEN-LAST:event_txtLePhiActionPerformed

	/**
	 * @param args the command line arguments
	 */
	public static void main(String args[]) {
		/* Set the Nimbus look and feel */
		// <editor-fold defaultstate="collapsed" desc=" Look and feel setting code
		// (optional) ">
		/*
		 * If Nimbus (introduced in Java SE 6) is not available, stay with the default
		 * look and feel. For details see
		 * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
		 */
		try {
			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
				if ("Windows".equals(info.getName())) {
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;

				}
			}
		} catch (ClassNotFoundException ex) {
			java.util.logging.Logger.getLogger(ThuVienJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null,
					ex);

		} catch (InstantiationException ex) {
			java.util.logging.Logger.getLogger(ThuVienJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null,
					ex);

		} catch (IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(ThuVienJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null,
					ex);

		} catch (javax.swing.UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(ThuVienJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null,
					ex);
		}
		// </editor-fold>
		// </editor-fold>

		/* Create and display the form */
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				new ThuVienJFrame().setVisible(true);
			}
		});
	}

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JButton btnCancelPhieu;
	private javax.swing.JButton btnCancelsach;
	private javax.swing.JButton btnCanceltaoThedg;
	private javax.swing.JButton btnDangXuat;
	private javax.swing.JButton btnDocGia;
	private javax.swing.JButton btnFindDocGia;
	private javax.swing.JButton btnHuy;
	private javax.swing.JButton btnLapPM;
	private javax.swing.JButton btnLapPhieuPhat4;
	private javax.swing.JButton btnLapPhieuTra;
	private javax.swing.JButton btnLePhi;
	private javax.swing.JButton btnOkPhieu;
	private javax.swing.JButton btnOkphat;
	private javax.swing.JButton btnOksach;
	private javax.swing.JButton btnSearch;
	private javax.swing.JButton btnTaoThe;
	private javax.swing.JButton btnTaoTheDocGia;
	private javax.swing.JButton btnThem;
	private javax.swing.JButton btnTim;
	private javax.swing.JButton btnTraSach4;
	private javax.swing.JComboBox<String> cboLyDo;
	private javax.swing.JCheckBox jCheckBox1;
	private javax.swing.JCheckBox jCheckBox2;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JLabel jLabel10;
	private javax.swing.JLabel jLabel11;
	private javax.swing.JLabel jLabel12;
	private javax.swing.JLabel jLabel13;
	private javax.swing.JLabel jLabel14;
	private javax.swing.JLabel jLabel15;
	private javax.swing.JLabel jLabel16;
	private javax.swing.JLabel jLabel18;
	private javax.swing.JLabel jLabel19;
	private javax.swing.JLabel jLabel2;
	private javax.swing.JLabel jLabel20;
	private javax.swing.JLabel jLabel21;
	private javax.swing.JLabel jLabel22;
	private javax.swing.JLabel jLabel23;
	private javax.swing.JLabel jLabel24;
	private javax.swing.JLabel jLabel3;
	private javax.swing.JLabel jLabel31;
	private javax.swing.JLabel jLabel32;
	private javax.swing.JLabel jLabel33;
	private javax.swing.JLabel jLabel34;
	private javax.swing.JLabel jLabel35;
	private javax.swing.JLabel jLabel36;
	private javax.swing.JLabel jLabel37;
	private javax.swing.JLabel jLabel39;
	private javax.swing.JLabel jLabel4;
	private javax.swing.JLabel jLabel41;
	private javax.swing.JLabel jLabel43;
	private javax.swing.JLabel jLabel46;
	private javax.swing.JLabel jLabel47;
	private javax.swing.JLabel jLabel5;
	private javax.swing.JLabel jLabel50;
	private javax.swing.JLabel jLabel52;
	private javax.swing.JLabel jLabel54;
	private javax.swing.JLabel jLabel59;
	private javax.swing.JLabel jLabel6;
	private javax.swing.JLabel jLabel60;
	private javax.swing.JLabel jLabel63;
	private javax.swing.JLabel jLabel7;
	private javax.swing.JLabel jLabel8;
	private javax.swing.JLabel jLabel85;
	private javax.swing.JLabel jLabel86;
	private javax.swing.JLabel jLabel87;
	private javax.swing.JLabel jLabel88;
	private javax.swing.JLabel jLabel89;
	private javax.swing.JLabel jLabel9;
	private javax.swing.JLabel jLabel90;
	private javax.swing.JLabel jLabel91;
	private javax.swing.JLayeredPane jLayeredPane1;
	private javax.swing.JPanel jPanel1;
	private javax.swing.JPanel jPanel10;
	private javax.swing.JPanel jPanel12;
	private javax.swing.JPanel jPanel13;
	private javax.swing.JPanel jPanel2;
	private javax.swing.JPanel jPanel3;
	private javax.swing.JPanel jPanel4;
	private javax.swing.JPanel jPanel8;
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JScrollPane jScrollPane2;
	private javax.swing.JToolBar.Separator jSeparator2;
	private javax.swing.JLabel lblAnh;
	private javax.swing.JLabel lblCCCD;
	private javax.swing.JLabel lblCanCuocCD;
	private javax.swing.JLabel lblCanhBaoQuaHan;
	private javax.swing.JLabel lblDiaChi;
	private javax.swing.JLabel lblDongHo;
	private javax.swing.JLabel lblEmail;
	private javax.swing.JLabel lblEmailLePhi;
	private javax.swing.JLabel lblGiaTriDen;
	private javax.swing.JLabel lblHan;
	private javax.swing.JLabel lblHinh2;
	private javax.swing.JLabel lblHinhLePhi;
	private javax.swing.JLabel lblHoTen;
	private javax.swing.JLabel lblHovaTen;
	private javax.swing.JLabel lblMaDG;
	private javax.swing.JLabel lblMaDocGia;
	private javax.swing.JLabel lblMaThe;
	private javax.swing.JLabel lblNgayCap;
	private javax.swing.JLabel lblNgayCapLePhi;
	private javax.swing.JLabel lblTrangThai;
	private javax.swing.JPanel pnl;
	private javax.swing.JPanel pnlDocGia;
	private javax.swing.JPanel pnlLePhi;
	private javax.swing.JPanel pnlPPhat;
	private javax.swing.JPanel pnlPTra4;
	private javax.swing.JPanel pnlPhieuMuon;
	private javax.swing.JPanel pnlPhieuTraPhat;
	private javax.swing.JPanel pnlTaoThe;
	private javax.swing.JPanel pnlTaoThe1;
	private javax.swing.JPanel pnlTrangThai;
	private javax.swing.JPanel pnlanh;
	private javax.swing.JToolBar tbaCongCu;
	private javax.swing.JTable tblSachMuon;
	private javax.swing.JTable tblThongTinSach;
	private javax.swing.JButton tbnCancelPhat;
	private javax.swing.JButton tbnCancelPhat1;
	private javax.swing.JTextField txtCCCD;
	private javax.swing.JTextField txtDiaChi;
	private javax.swing.JTextField txtEmail;
	private javax.swing.JTextField txtFindDocgia;
	private javax.swing.JTextField txtGia;
	private javax.swing.JTextField txtGiaHan;
	private javax.swing.JTextField txtHanSuDung;
	private javax.swing.JTextField txtLePhi;
	private javax.swing.JTextField txtMaDG;
	private javax.swing.JTextField txtMaDocGia;
	private javax.swing.JTextField txtMaNhanVien;
	private javax.swing.JTextField txtMaPhieuMuon;
	private javax.swing.JTextField txtMaPhieuMuon4;
	private javax.swing.JTextField txtMaPhieuTra5;
	private javax.swing.JTextField txtMaSach;
	private javax.swing.JTextField txtMaphieuphat;
	private javax.swing.JTextField txtMaphieutra2;
	private javax.swing.JTextField txtMasach2;
	private javax.swing.JTextField txtMasach5;
	private javax.swing.JTextField txtNgayCap;
	private javax.swing.JTextField txtNgayCap1;
	private javax.swing.JTextField txtNgayHetHan;
	private javax.swing.JTextField txtNgayMuon;
	private javax.swing.JTextField txtNgayMuon4;
	private javax.swing.JTextField txtNgayPhat;
	private javax.swing.JTextField txtNgayTra;
	private javax.swing.JTextField txtNgayTra4;
	private javax.swing.JTextField txtPhi;
	private javax.swing.JTextField txtSDT;
	private javax.swing.JTextField txtSearch;
	private javax.swing.JTextField txtSearch1;
	private javax.swing.JTextField txtTenDocGia;
	private javax.swing.JTextField txtTenSach;
	private javax.swing.JTextField txtTenSach4;
	// End of variables declaration//GEN-END:variables
}
