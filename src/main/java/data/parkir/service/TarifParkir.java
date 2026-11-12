package data.parkir.service;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Locale;

public final class TarifParkir {

    public static final int MOTOR_JAM_PERTAMA = 3000;
    public static final int MOTOR_JAM_BERIKUT = 2000;
    public static final int MOTOR_MAKS_HARIAN = 15000;

    public static final int MOBIL_JAM_PERTAMA = 6000;
    public static final int MOBIL_JAM_BERIKUT = 3000;
    public static final int MOBIL_MAKS_HARIAN = 30000;

    private static final DecimalFormat RUPIAH =
        new DecimalFormat("#,###", DecimalFormatSymbols.getInstance(Locale.forLanguageTag("id-ID")));

    private TarifParkir() {
    }

    public static boolean isMotor(String jenis) {
        return "Motor".equalsIgnoreCase(jenis);
    }

    public static long hitungJam(LocalDateTime masuk, LocalDateTime keluar) {
        long menit = Math.max(0, Duration.between(masuk, keluar).toMinutes());
        return Math.max(1, (long) Math.ceil(menit / 60.0));
    }

    public static int hitungTarif(String jenis, LocalDateTime masuk, LocalDateTime keluar) {
        if (masuk == null || keluar == null) return 0;
        long totalJam = hitungJam(masuk, keluar);
        long hari = totalJam / 24;
        long sisaJam = totalJam % 24;

        int maksHarian = isMotor(jenis) ? MOTOR_MAKS_HARIAN : MOBIL_MAKS_HARIAN;
        return (int) (hari * maksHarian) + tarifSebagianHari(jenis, sisaJam);
    }

    private static int tarifSebagianHari(String jenis, long jam) {
        if (jam <= 0) return 0;
        int tarif;
        int maks;
        if (isMotor(jenis)) {
            tarif = MOTOR_JAM_PERTAMA + (int) ((jam - 1) * MOTOR_JAM_BERIKUT);
            maks = MOTOR_MAKS_HARIAN;
        } else {
            tarif = MOBIL_JAM_PERTAMA + (int) ((jam - 1) * MOBIL_JAM_BERIKUT);
            maks = MOBIL_MAKS_HARIAN;
        }
        return Math.min(tarif, maks);
    }

    public static String rupiah(long nominal) {
        return "Rp" + RUPIAH.format(nominal);
    }
}
