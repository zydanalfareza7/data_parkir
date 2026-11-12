package data.parkir.dao;

import data.parkir.model.Parkir;
import java.time.LocalDateTime;
import java.util.List;

public interface ParkirDAO {
    boolean simpan(Parkir p);
    boolean update(Parkir p);
    boolean hapus(int id);
    Parkir getById(int id);
    Parkir getByPlatAktif(String platNomor);
    List<Parkir> getAll();
    List<Parkir> getAktif();
    long hitungAktifByJenis(String jenis);
    List<Parkir> getByRentangTanggal(LocalDateTime awal, LocalDateTime akhir);
    int hitungTotalPendapatan();
}
