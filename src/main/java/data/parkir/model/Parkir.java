package data.parkir.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.*;

@Entity
@Table(name = "tb_parkir")
public class Parkir implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "plat_nomor", nullable = false, length = 20)
    private String platNomor;

    @Column(name = "jenis", nullable = false, length = 20)
    private String jenis;

    @Column(name = "jam_masuk", nullable = false)
    private LocalDateTime jamMasuk;

    @Column(name = "jam_keluar")
    private LocalDateTime jamKeluar;

    @Column(name = "total_bayar")
    private Integer totalBayar;

    @Column(name = "status", length = 20)
    private String status;

    @Column(name = "petugas", length = 50)
    private String petugas;

    @Column(name = "metode_bayar", length = 20)
    private String metodeBayar;

    @Column(name = "uang_bayar")
    private Integer uangBayar;

    @Column(name = "kembalian")
    private Integer kembalian;

    @Column(name = "denda")
    private Integer denda;

    @Column(name = "keterangan", length = 50)
    private String keterangan;

    public Parkir() {
    }

    public Parkir(String platNomor, String jenis, LocalDateTime jamMasuk, String petugas) {
        this.platNomor = platNomor;
        this.jenis = jenis;
        this.jamMasuk = jamMasuk;
        this.status = "Parkir";
        this.totalBayar = 0;
        this.denda = 0;
        this.petugas = petugas;
    }

    public boolean isMasihParkir() {
        return "Parkir".equalsIgnoreCase(status) || jamKeluar == null;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getPlatNomor() { return platNomor; }
    public void setPlatNomor(String platNomor) { this.platNomor = platNomor; }

    public String getJenis() { return jenis; }
    public void setJenis(String jenis) { this.jenis = jenis; }

    public LocalDateTime getJamMasuk() { return jamMasuk; }
    public void setJamMasuk(LocalDateTime jamMasuk) { this.jamMasuk = jamMasuk; }

    public LocalDateTime getJamKeluar() { return jamKeluar; }
    public void setJamKeluar(LocalDateTime jamKeluar) { this.jamKeluar = jamKeluar; }

    public Integer getTotalBayar() { return totalBayar; }
    public void setTotalBayar(Integer totalBayar) { this.totalBayar = totalBayar; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getPetugas() { return petugas; }
    public void setPetugas(String petugas) { this.petugas = petugas; }

    public String getMetodeBayar() { return metodeBayar; }
    public void setMetodeBayar(String metodeBayar) { this.metodeBayar = metodeBayar; }

    public Integer getUangBayar() { return uangBayar; }
    public void setUangBayar(Integer uangBayar) { this.uangBayar = uangBayar; }

    public Integer getKembalian() { return kembalian; }
    public void setKembalian(Integer kembalian) { this.kembalian = kembalian; }

    public Integer getDenda() { return denda; }
    public void setDenda(Integer denda) { this.denda = denda; }

    public String getKeterangan() { return keterangan; }
    public void setKeterangan(String keterangan) { this.keterangan = keterangan; }
}
