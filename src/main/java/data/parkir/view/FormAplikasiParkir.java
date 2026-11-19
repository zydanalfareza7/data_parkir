package data.parkir.view;

import data.parkir.dao.ParkirDAO;
import data.parkir.model.Parkir;
import data.parkir.service.LaporanService;
import data.parkir.service.SpringUtil;
import data.parkir.service.TarifParkir;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class FormAplikasiParkir extends JFrame {

    private ParkirDAO dao;
    private final JTextField txtPlat;
    private final JComboBox<String> cmbJenis;
    private final JTable tabelParkir;
    private final DefaultTableModel tableModel;
    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

    public FormAplikasiParkir(ParkirDAO dao) {
        this.dao = (dao != null) ? dao : SpringUtil.getParkirDAO();

        setTitle("Aplikasi Parkir (Spring & Hibernate) - 4IA23");
        setSize(850, 520);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel panelHeader = new JPanel();
        panelHeader.setBackground(new Color(30, 58, 138));
        panelHeader.setBorder(new EmptyBorder(12, 15, 12, 15));
        JLabel lblTitle = new JLabel("SISTEM MANAJEMEN PARKIR KENDARAAN");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(Color.WHITE);
        panelHeader.add(lblTitle);
        add(panelHeader, BorderLayout.NORTH);

        JPanel panelInput = new JPanel(new GridLayout(3, 2, 10, 10));
        panelInput.setBorder(BorderFactory.createTitledBorder("Input Kendaraan Masuk"));

        panelInput.add(new JLabel("Plat Nomor Kendaraan:"));
        txtPlat = new JTextField();
        panelInput.add(txtPlat);

        panelInput.add(new JLabel("Jenis Kendaraan:"));
        cmbJenis = new JComboBox<>(new String[]{"Motor", "Mobil"});
        panelInput.add(cmbJenis);

        JButton btnMasuk = new JButton("1. Kendaraan Masuk (Simpan)");
        btnMasuk.setBackground(new Color(16, 185, 129));
        btnMasuk.setForeground(Color.WHITE);
        btnMasuk.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnMasuk.addActionListener(e -> aksiMasuk());
        panelInput.add(btnMasuk);

        JButton btnReset = new JButton("Reset Form");
        btnReset.addActionListener(e -> resetForm());
        panelInput.add(btnReset);

        String[] kolom = {"ID", "Plat Nomor", "Jenis", "Jam Masuk", "Jam Keluar", "Total Bayar", "Status"};
        tableModel = new DefaultTableModel(kolom, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tabelParkir = new JTable(tableModel);
        tabelParkir.setRowHeight(24);
        JScrollPane scrollPane = new JScrollPane(tabelParkir);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Daftar Transaksi Parkir"));

        JPanel panelTengah = new JPanel(new BorderLayout(10, 10));
        panelTengah.setBorder(new EmptyBorder(10, 15, 10, 15));
        panelTengah.add(panelInput, BorderLayout.NORTH);
        panelTengah.add(scrollPane, BorderLayout.CENTER);
        add(panelTengah, BorderLayout.CENTER);

        JPanel panelBawah = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        panelBawah.setBorder(new EmptyBorder(0, 15, 10, 15));

        JButton btnKeluar = new JButton("2. Kendaraan Keluar & Bayar");
        btnKeluar.setBackground(new Color(234, 88, 12));
        btnKeluar.setForeground(Color.WHITE);
        btnKeluar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnKeluar.addActionListener(e -> aksiKeluar());
        panelBawah.add(btnKeluar);

        JButton btnHapus = new JButton("3. Hapus Data");
        btnHapus.setBackground(new Color(225, 29, 72));
        btnHapus.setForeground(Color.WHITE);
        btnHapus.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnHapus.addActionListener(e -> aksiHapus());
        panelBawah.add(btnHapus);

        JButton btnCetak = new JButton("4. Cetak Laporan");
        btnCetak.setBackground(new Color(37, 99, 235));
        btnCetak.setForeground(Color.WHITE);
        btnCetak.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnCetak.addActionListener(e -> aksiCetakLaporan());
        panelBawah.add(btnCetak);

        JButton btnRefresh = new JButton("Refresh Tabel");
        btnRefresh.addActionListener(e -> muatData());
        panelBawah.add(btnRefresh);

        add(panelBawah, BorderLayout.SOUTH);
        muatData();
    }

    public FormAplikasiParkir() {
        this(SpringUtil.getParkirDAO());
    }

    public void setParkirDAO(ParkirDAO dao) {
        this.dao = dao;
        muatData();
    }

    private void aksiMasuk() {
        String plat = txtPlat.getText().trim().toUpperCase();
        String jenis = (String) cmbJenis.getSelectedItem();

        if (plat.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Plat nomor wajib diisi!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (dao.getByPlatAktif(plat) != null) {
            JOptionPane.showMessageDialog(this, "Kendaraan dengan plat " + plat + " masih berada di area parkir!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Parkir p = new Parkir(plat, jenis, LocalDateTime.now(), "zydan");
        if (dao.simpan(p)) {
            JOptionPane.showMessageDialog(this, "Kendaraan " + plat + " (" + jenis + ") berhasil masuk parkir!");
            resetForm();
            muatData();
        } else {
            JOptionPane.showMessageDialog(this, "Gagal menyimpan data kendaraan!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void aksiKeluar() {
        int baris = tabelParkir.getSelectedRow();
        if (baris == -1) {
            JOptionPane.showMessageDialog(this, "Pilih kendaraan di tabel yang ingin keluar!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) tableModel.getValueAt(baris, 0);
        Parkir p = dao.getById(id);
        if (p == null || !p.isMasihParkir()) {
            JOptionPane.showMessageDialog(this, "Kendaraan sudah selesai parkir!", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        LocalDateTime now = LocalDateTime.now();
        int tarif = TarifParkir.hitungTarif(p.getJenis(), p.getJamMasuk(), now);

        String inputUang = JOptionPane.showInputDialog(this,
            "Plat Nomor : " + p.getPlatNomor() + " (" + p.getJenis() + ")\n" +
            "Jam Masuk  : " + p.getJamMasuk().format(dtf) + "\n" +
            "Jam Keluar : " + now.format(dtf) + "\n" +
            "Total Tarif: " + TarifParkir.rupiah(tarif) + "\n\n" +
            "Masukkan Jumlah Uang Bayar:",
            tarif);

        if (inputUang == null || inputUang.trim().isEmpty()) return;

        int uang = 0;
        try {
            uang = Integer.parseInt(inputUang.replaceAll("[^0-9]", ""));
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Nominal tidak valid!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (uang < tarif) {
            JOptionPane.showMessageDialog(this, "Uang pembayaran kurang!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int kembalian = uang - tarif;
        p.setJamKeluar(now);
        p.setTotalBayar(tarif);
        p.setUangBayar(uang);
        p.setKembalian(kembalian);
        p.setMetodeBayar("Tunai");
        p.setStatus("Selesai");

        if (dao.update(p)) {
            JOptionPane.showMessageDialog(this,
                "Pembayaran Berhasil!\n" +
                "Total Tagihan : " + TarifParkir.rupiah(tarif) + "\n" +
                "Uang Diterima : " + TarifParkir.rupiah(uang) + "\n" +
                "Kembalian     : " + TarifParkir.rupiah(kembalian),
                "Transaksi Selesai", JOptionPane.INFORMATION_MESSAGE);
            resetForm();
            muatData();
        } else {
            JOptionPane.showMessageDialog(this, "Gagal mengupdate transaksi keluar!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void aksiHapus() {
        int baris = tabelParkir.getSelectedRow();
        if (baris == -1) {
            JOptionPane.showMessageDialog(this, "Pilih baris di tabel yang ingin dihapus!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) tableModel.getValueAt(baris, 0);
        int konfirm = JOptionPane.showConfirmDialog(this, "Yakin ingin menghapus data transaksi ID: " + id + "?",
            "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);

        if (konfirm == JOptionPane.YES_OPTION) {
            if (dao.hapus(id)) {
                JOptionPane.showMessageDialog(this, "Data berhasil dihapus!");
                resetForm();
                muatData();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal menghapus data!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void aksiCetakLaporan() {
        try {
            List<Parkir> data = dao.getAll();
            int total = dao.hitungTotalPendapatan();
            LaporanService.cetakLaporan(data, total);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Gagal mencetak laporan JasperReports:\n" + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void muatData() {
        tableModel.setRowCount(0);
        List<Parkir> list = dao.getAll();
        for (Parkir p : list) {
            tableModel.addRow(new Object[]{
                p.getId(),
                p.getPlatNomor(),
                p.getJenis(),
                p.getJamMasuk() != null ? p.getJamMasuk().format(dtf) : "-",
                p.getJamKeluar() != null ? p.getJamKeluar().format(dtf) : "-",
                p.getTotalBayar() != null ? TarifParkir.rupiah(p.getTotalBayar()) : "Rp 0",
                p.getStatus()
            });
        }
    }

    private void resetForm() {
        txtPlat.setText("");
        cmbJenis.setSelectedIndex(0);
        tabelParkir.clearSelection();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FormAplikasiParkir().setVisible(true));
    }
}
