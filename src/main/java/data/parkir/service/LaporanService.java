package data.parkir.service;

import data.parkir.model.Parkir;
import java.io.FileInputStream;
import java.io.InputStream;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;

public class LaporanService {

    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

    public static void cetakLaporan(List<Parkir> daftarParkir, int totalPendapatan) throws Exception {
        InputStream jrxmlStream = LaporanService.class.getResourceAsStream("/laporan_parkir.jrxml");
        if (jrxmlStream == null) {
            jrxmlStream = new FileInputStream("src/main/resources/laporan_parkir.jrxml");
        }

        List<ParkirReportItem> items = new ArrayList<>();
        for (Parkir p : daftarParkir) {
            items.add(new ParkirReportItem(
                p.getId(),
                p.getPlatNomor(),
                p.getJenis(),
                p.getJamMasuk() != null ? p.getJamMasuk().format(DTF) : "-",
                p.getJamKeluar() != null ? p.getJamKeluar().format(DTF) : "-",
                p.getTotalBayar() != null ? TarifParkir.rupiah(p.getTotalBayar()) : "Rp 0",
                p.getStatus()
            ));
        }

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("TOTAL_PENDAPATAN", TarifParkir.rupiah(totalPendapatan));

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(items);
        JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlStream);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
        JasperViewer.viewReport(jasperPrint, false);
    }

    public static class ParkirReportItem {
        private final Integer id;
        private final String platNomor;
        private final String jenis;
        private final String jamMasukStr;
        private final String jamKeluarStr;
        private final String totalBayarStr;
        private final String status;

        public ParkirReportItem(Integer id, String platNomor, String jenis,
                                String jamMasukStr, String jamKeluarStr,
                                String totalBayarStr, String status) {
            this.id = id;
            this.platNomor = platNomor;
            this.jenis = jenis;
            this.jamMasukStr = jamMasukStr;
            this.jamKeluarStr = jamKeluarStr;
            this.totalBayarStr = totalBayarStr;
            this.status = status;
        }

        public Integer getId() { return id; }
        public String getPlatNomor() { return platNomor; }
        public String getJenis() { return jenis; }
        public String getJamMasukStr() { return jamMasukStr; }
        public String getJamKeluarStr() { return jamKeluarStr; }
        public String getTotalBayarStr() { return totalBayarStr; }
        public String getStatus() { return status; }
    }
}
