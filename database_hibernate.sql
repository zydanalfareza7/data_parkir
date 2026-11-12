-- ========================================================
-- Database Baru untuk Praktikum Java Hibernate (ACT)
-- Jalankan di phpMyAdmin Laragon: http://localhost/phpmyadmin
-- ========================================================

CREATE DATABASE IF NOT EXISTS `db_mahasiswa_hibernate`
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

USE `db_mahasiswa_hibernate`;

CREATE TABLE IF NOT EXISTS `tb_mhs` (
  `npm` VARCHAR(20) NOT NULL,
  `nama` VARCHAR(100) NOT NULL,
  `kelas` VARCHAR(20) NOT NULL,
  `jurusan` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`npm`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Data Awal Uji Coba (Dummy Data)
INSERT INTO `tb_mhs` (`npm`, `nama`, `kelas`, `jurusan`) VALUES
('51423046', 'Zydan Alfareza', '4IA23', 'Informatika'),
('50423001', 'Ahmad Fauzi', '4IA23', 'Informatika'),
('50423002', 'Siti Rahma', '4IA23', 'Sistem Informasi')
ON DUPLICATE KEY UPDATE `nama`=`nama`;
