# Aplikasi Parkir Kendaraan (Desktop Java Swing + Spring Framework & Hibernate ORM)

Aplikasi desktop manajemen sistem parkir kendaraan (Motor & Mobil) berbasis Java Desktop Swing dengan integrasi **Spring Framework (IoC & Dependency Injection)** dan **Hibernate ORM** terhubung ke database MySQL. Dibuat untuk proyek Rekayasa Perangkat Lunak 2, Universitas Gunadarma.

**Identitas Mahasiswa:**
- **Nama:** Zydan Alfareza
- **NPM:** 51423046
- **Kelas:** 4IA23
- **Mata Kuliah:** Rekayasa Perangkat Lunak 2 (RPL 2)

---

## 🚀 Fitur Utama & Arsitektur

1. **Dependency Injection (DI) & Inversion of Control (IoC):**
   - **Constructor Injection:** `FormAplikasiParkir(ParkirDAO dao)` menerima objek DAO yang diinjeksikan langsung dari Spring.
   - **Setter Injection:** `setSessionFactory(SessionFactory sf)` di `ParkirDAOImpl` dan `setParkirDAO(ParkirDAO dao)` di Form.
   - **Class Interface:** Pemisahan kontrak abstraksi `ParkirDAO` (Interface) dan implementasi konkret `ParkirDAOImpl` (*Loose Coupling*).
2. **Spring IoC Container:**
   - Dikelola melalui file konfigurasi `applicationContext.xml` yang mengatur `dataSource`, `sessionFactory` (LocalSessionFactoryBean), dan bean `parkirDAO`.
3. **Hibernate ORM (Persistence):**
   - Pemetaan entitas class Java ke tabel MySQL `tb_parkir` menggunakan anotasi JPA (`@Entity`, `@Table`, `@Id`).
4. **Operasi CRUD Lengkap:**
   - **Create:** Simpan kendaraan masuk (Plat nomor & Jenis motor/mobil).
   - **Read:** Menampilkan daftar transaksi parkir di `JTable`.
   - **Update:** Proses kendaraan keluar dengan kalkulasi tarif progresif otomatis.
   - **Delete:** Hapus data transaksi parkir.

---

## 🛠️ Struktur Project (Layered Architecture)

```text
├── src/
│   └── main/
│       ├── java/
│       │   └── data/parkir/
│       │       ├── DataParkir.java             # Entry Point Utama Aplikasi
│       │       ├── model/                      # Layer 1: Model Entity
│       │       │   └── Parkir.java             # JPA Entity Model (Mapping tb_parkir)
│       │       ├── dao/                        # Layer 2: Akses Data (CRUD)
│       │       │   ├── ParkirDAO.java          # Interface Kontrak CRUD (Loose Coupling)
│       │       │   └── ParkirDAOImpl.java      # Implementasi Hibernate dengan Injeksi Spring
│       │       ├── service/                    # Layer 3: Logika Bisnis & Helper
│       │       │   ├── TarifParkir.java        # Rumus Hitung Tarif Progresif
│       │       │   ├── SpringUtil.java         # Helper Spring ApplicationContext
│       │       │   └── HibernateUtil.java      # Helper SessionFactory Hibernate
│       │       └── view/                       # Layer 4: Tampilan Pengguna
│       │           └── FormAplikasiParkir.java # GUI Swing dengan Constructor Injection
│       └── resources/
│           ├── applicationContext.xml          # Konfigurasi Spring Framework IoC Container
│           └── hibernate.cfg.xml               # Konfigurasi Koneksi JDBC & Dialek Hibernate
├── database_hibernate.sql                      # DDL Skema Tabel tb_parkir
│
├── pom.xml                             # Konfigurasi Apache Maven
├── README.md
└── .gitignore
```

---

## ⚙️ Cara Menjalankan

1. Pastikan database MySQL berjalan di port `3306` (misal via Laragon atau XAMPP).
2. Database yang digunakan adalah `db_mahasiswa` dengan tabel `tb_parkir`:
   ```sql
   SOURCE database_hibernate.sql;
   ```
3. Buka project **Data Parkir** di Apache NetBeans.
4. Jalankan class utama: **`data.parkir.DataParkir`**.
