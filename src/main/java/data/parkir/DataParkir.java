package data.parkir;

import data.parkir.dao.ParkirDAO;
import data.parkir.service.SpringUtil;
import data.parkir.view.FormAplikasiParkir;
import javax.swing.SwingUtilities;

public class DataParkir {

    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("   APLIKASI PARKIR (SPRING FRAMEWORK & HIBERNATE) ");
        System.out.println("   Dependency Injection & AOP       ");
        System.out.println("   Mahasiswa: Zydan Alfareza (51423046) - 4IA23    ");
        System.out.println("==================================================");

        ParkirDAO dao = SpringUtil.getParkirDAO();

        SwingUtilities.invokeLater(() -> {
            new FormAplikasiParkir(dao).setVisible(true);
        });
    }
}
