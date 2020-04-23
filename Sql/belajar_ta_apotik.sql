-- phpMyAdmin SQL Dump
-- version 3.1.3.1
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Jan 31, 2014 at 10:00 PM
-- Server version: 5.1.33
-- PHP Version: 5.2.9

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `belajar_ta_apotik`
--

-- --------------------------------------------------------

--
-- Table structure for table `medicine`
--

CREATE TABLE IF NOT EXISTS `medicine` (
  `id_obat` int(11) NOT NULL AUTO_INCREMENT,
  `kode_obat` varchar(20) NOT NULL,
  `nama_obat` varchar(100) NOT NULL,
  `kategori_obat` varchar(15) NOT NULL,
  `jenis_obat` varchar(20) NOT NULL,
  `merek_obat` varchar(100) NOT NULL,
  `harga_beli_obat` decimal(10,0) NOT NULL,
  `harga_jual_obat` decimal(10,0) NOT NULL,
  `jumlah_obat` int(3) NOT NULL,
  `tanggal_masuk` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `expired` date NOT NULL,
  PRIMARY KEY (`id_obat`),
  UNIQUE KEY `kode_obat` (`kode_obat`),
  UNIQUE KEY `kode_obat_2` (`kode_obat`),
  UNIQUE KEY `kode_obat_3` (`kode_obat`),
  UNIQUE KEY `kode_obat_4` (`kode_obat`),
  UNIQUE KEY `kode_obat_5` (`kode_obat`),
  UNIQUE KEY `kode_obat_7` (`kode_obat`),
  UNIQUE KEY `id_obat` (`id_obat`),
  UNIQUE KEY `kode_obat_8` (`kode_obat`),
  KEY `kode_obat_6` (`kode_obat`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=26 ;

--
-- Dumping data for table `medicine`
--

INSERT INTO `medicine` (`id_obat`, `kode_obat`, `nama_obat`, `kategori_obat`, `jenis_obat`, `merek_obat`, `harga_beli_obat`, `harga_jual_obat`, `jumlah_obat`, `tanggal_masuk`, `expired`) VALUES
(1, 'SF01', 'Amoxcilin 100gr Generik', 'Obat Dalam', 'Tablet', 'Amoxcilin 100gr', 8000, 10000, 13, '2014-01-25 21:48:09', '2014-01-25'),
(2, 'SF02', 'Farsien 50 gr', 'Obat Luar', 'Kapsul', 'Farsien 50 gr', 6000, 8000, 82, '2014-01-25 21:52:11', '2014-01-25'),
(3, 'SF03', 'Broadamx 500 mg', 'Obat Dalam', 'Kaplet', 'Amoksisilina Trihidrat 500mg', 12500, 15000, 90, '2014-01-26 00:40:19', '2016-01-21'),
(4, 'SF04', 'Flutamol', 'Obat Dalam', 'Kaplet', 'Paraxetamol PCG', 5000, 6500, 12, '2014-01-26 00:40:48', '2016-01-21'),
(5, 'SF05', 'Sana Flu', 'Obat Dalam', 'Kaplet', 'Paracetamol P HCI', 5000, 6500, 48, '2014-01-26 00:40:32', '2015-01-08'),
(6, 'SF06', 'Pi Kang Shuang 10gr', 'Obat Luar', 'Salep', 'Pi Kang Shuang', 5000, 9000, 97, '2014-01-25 22:14:35', '2016-01-21'),
(8, 'SF08', 'Sana Flu10gr', 'Obat Dalam', 'Kaplet', 'Sana Flu', 15000, 20000, 60, '2014-01-26 00:29:00', '2015-01-20'),
(21, 'SF10', 'Flutamol 110gr', 'Obat Dalam', 'Kaplet', 'Flutamol ', 5000, 7000, 12, '2014-01-31 20:27:48', '2014-01-31'),
(24, 'SF12', 'Flutamol 100gr', 'Obat Dalam', 'Kaplet', 'Flutamol ', 5000, 7000, 12, '2014-01-31 20:43:23', '2014-01-31'),
(25, 'SF13', 'Amoxlin 200gr', 'Obat Dalam', 'Pil', 'Amoxilin', 20000, 25000, 25, '2014-01-31 20:59:15', '2014-01-31');

-- --------------------------------------------------------

--
-- Table structure for table `penjualan`
--

CREATE TABLE IF NOT EXISTS `penjualan` (
  `id_jual` int(5) NOT NULL AUTO_INCREMENT,
  `kode_transaksi` int(10) NOT NULL,
  `kode_obat` varchar(10) NOT NULL,
  `nama_obat` varchar(100) NOT NULL,
  `merek_obat` varchar(100) NOT NULL,
  `harga_jual` int(11) NOT NULL,
  `jumlah_beli` int(11) NOT NULL,
  `total_harga` int(11) NOT NULL,
  `tanggal_transaksi` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_jual`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=190 ;

--
-- Dumping data for table `penjualan`
--

INSERT INTO `penjualan` (`id_jual`, `kode_transaksi`, `kode_obat`, `nama_obat`, `merek_obat`, `harga_jual`, `jumlah_beli`, `total_harga`, `tanggal_transaksi`) VALUES
(140, 2, 'SF04', 'zzz', 'r', 6500, 1, 6500, '2014-01-30 01:25:51'),
(142, 34313, 'SF05', 'aa', 'r', 6500, 2, 13000, '2014-01-30 01:43:36'),
(143, 32532535, 'SF05', 'aa', 'r', 6500, 2, 13000, '2014-01-30 01:45:41'),
(144, 42525, 'SF01', 'Amoxcilin 100gr Generik', 'Amoxcilin 100gr', 10000, 2, 20000, '2014-01-30 01:49:25'),
(145, 1324636, 'SF03', 'ggq', 'we', 15000, 1, 15000, '2014-01-30 01:56:13'),
(146, 3144, '1', 'SF01', 'Tablet', 8000, 3, 24000, '2014-01-30 20:01:00'),
(147, 3144, '2', 'SF02', 'Kapsul', 6000, 2, 12000, '2014-01-30 20:01:21'),
(148, 123, 'SF01', 'Amoxcilin 100gr Generik', 'Amoxcilin 100gr', 10000, 1, 10000, '2014-01-30 20:03:34'),
(149, 12424, 'SF01', 'Amoxcilin 100gr Generik', 'Amoxcilin 100gr', 10000, 1, 10000, '2014-01-31 06:24:08'),
(151, 144, 'SF01', 'Amoxcilin 100gr Generik', 'Amoxcilin 100gr', 10000, 1, 10000, '2014-01-31 13:57:50'),
(154, 15, 'SF01', 'Amoxcilin 100gr Generik', 'Amoxcilin 100gr', 10000, 3, 30000, '2014-01-31 15:29:16'),
(157, 555550, 'SF01', 'Amoxcilin 100gr Generik', 'Amoxcilin 100gr', 10000, 2, 20000, '2014-01-31 16:09:35'),
(158, 555550, 'SF02', 'Farsien 50 gr', 'Farsien 50 gr', 8000, 2, 16000, '2014-01-31 16:09:47'),
(159, 1221, 'SF05', 'Sana Flu', 'Paracetamol P HCI', 6500, 11, 71500, '2014-01-31 16:29:08'),
(160, 1221, 'SF02', 'Farsien 50 gr', 'Farsien 50 gr', 8000, 1, 8000, '2014-01-31 16:29:15'),
(166, 12133, 'SF01', 'Amoxcilin 100gr Generik', 'Amoxcilin 100gr', 10000, 1, 10000, '2014-01-31 19:22:23'),
(172, 3, 'SF06', 'Pi Kang Shuang 10gr', 'Pi Kang Shuang', 9000, 2, 18000, '2014-01-31 19:50:35'),
(173, 1, 'SF05', 'Sana Flu', 'Paracetamol P HCI', 6500, 2, 13000, '2014-01-31 19:50:44'),
(174, 10, 'SF02', 'Farsien 50 gr', 'Farsien 50 gr', 8000, 2, 16000, '2014-01-31 19:50:52'),
(175, 1234567, 'SF01', 'Amoxcilin 100gr Generik', 'Amoxcilin 100gr', 10000, 2, 20000, '2014-01-31 20:28:46'),
(176, 1234567, 'SF03', 'Broadamx 500 mg', 'Amoksisilina Trihidrat 500mg', 15000, 2, 30000, '2014-01-31 20:28:58'),
(177, 122, 'SF04', 'Flutamol', 'Paraxetamol PCG', 6500, 2, 13000, '2014-01-31 20:30:57'),
(178, 1112, 'SF01', 'Amoxcilin 100gr Generik', 'Amoxcilin 100gr', 10000, 3, 30000, '2014-01-31 20:44:15'),
(179, 1112, 'SF04', 'Flutamol', 'Paraxetamol PCG', 6500, 1, 6500, '2014-01-31 20:44:26'),
(180, 221, 'SF02', 'Farsien 50 gr', 'Farsien 50 gr', 8000, 2, 16000, '2014-01-31 20:46:11'),
(181, 21212, 'SF01', 'Amoxcilin 100gr Generik', 'Amoxcilin 100gr', 10000, 2, 20000, '2014-01-31 21:00:56'),
(182, 21212, 'SF02', 'Farsien 50 gr', 'Farsien 50 gr', 8000, 2, 16000, '2014-01-31 21:01:10'),
(183, 13131, 'SF01', 'Amoxcilin 100gr Generik', 'Amoxcilin 100gr', 10000, 4, 40000, '2014-01-31 21:03:14'),
(185, 4531, 'SF01', 'Amoxcilin 100gr Generik', 'Amoxcilin 100gr', 10000, 3, 30000, '2014-01-31 21:35:50'),
(186, 4531, 'SF03', 'Broadamx 500 mg', 'Amoksisilina Trihidrat 500mg', 15000, 2, 30000, '2014-01-31 21:35:57'),
(187, 4531, '8', 'SF08', 'Kaplet', 15000, 2, 30000, '2014-01-31 21:37:51'),
(188, 1412, 'SF02', 'Farsien 50 gr', 'Farsien 50 gr', 8000, 3, 24000, '2014-01-31 21:46:33'),
(189, 1412, 'SF02', 'Farsien 50 gr', 'Farsien 50 gr', 8000, 3, 24000, '2014-01-31 21:46:37');

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE IF NOT EXISTS `user` (
  `id_user` int(11) NOT NULL AUTO_INCREMENT,
  `nama` varchar(100) NOT NULL,
  `jenis_kelamin` varchar(15) NOT NULL,
  `username` varchar(20) NOT NULL,
  `password` varchar(20) NOT NULL,
  `akses` varchar(15) NOT NULL,
  `alamat` text NOT NULL,
  `email` varchar(50) NOT NULL,
  `no_hp` varchar(12) NOT NULL,
  PRIMARY KEY (`id_user`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=20 ;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`id_user`, `nama`, `jenis_kelamin`, `username`, `password`, `akses`, `alamat`, `email`, `no_hp`) VALUES
(10, 'Mhd Syarif', 'Laki-Laki', 'syarif', 'syarif', 'Admin', 'Pekanbaru', 'mhdsyarif.ms@gmail.com', '085365048772'),
(11, 'linda', 'Perempuan', 'linda', 'linda', 'Kasir', '', '', ''),
(18, 'admin', 'Laki-Laki', 'admin', 'admin', 'Admin', '', '', ''),
(19, 'kasir', 'Perempuan', 'kasir', 'kasir', 'Kasir', '', '', '');
