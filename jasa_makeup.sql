-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: Oct 15, 2026 at 09:53 AM
-- Server version: 8.0.30
-- PHP Version: 8.1.10

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `jasa_makeup`
--

-- --------------------------------------------------------

--
-- Table structure for table `pelanggan`
--

CREATE TABLE `pelanggan` (
  `id_pelanggan` int NOT NULL,
  `nama_pelanggan` varchar(100) NOT NULL,
  `alamat` varchar(150) NOT NULL,
  `noHP` varchar(12) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `pelanggan`
--

INSERT INTO `pelanggan` (`id_pelanggan`, `nama_pelanggan`, `alamat`, `noHP`) VALUES
(1, 'SZA', 'Jl.Tarmidi No.01 Samarinda', '089876543211'),
(3, 'SZA', 'Jl.Tarmidi No.01 Samarinda', '089876543211'),
(4, 'SZA', 'Jl.Tarmidi No.01 Samarinda', '089876543211'),
(5, 'SZA', 'Jl.Tarmidi No.01 Samarinda', '089876543211'),
(10, 'SZA', 'Jl. Tarmidi No.14', '081389583784'),
(11, 'SZA', 'Jl. Tarmidi No.14', '081389583784'),
(12, 'SZA', 'Jl. Tarmidi No.14', '081389583784'),
(13, 'SZA', 'Jl. Tarmidi No.14', '081389583784'),
(14, 'Madison Br', 'Jl. Pramuka', '081389583784');

-- --------------------------------------------------------

--
-- Table structure for table `pemesanan`
--

CREATE TABLE `pemesanan` (
  `id_pemesanan` varchar(5) NOT NULL,
  `id_pelanggan` int NOT NULL,
  `tanggal_pemesanan` date NOT NULL,
  `jenis_makeup` varchar(25) NOT NULL,
  `total_harga` int NOT NULL,
  `status_pembayaran` varchar(10) DEFAULT 'Pending',
  `jumlah_orang_dirias` int DEFAULT NULL,
  `ada_retouch_kit` tinyint(1) DEFAULT NULL,
  `jumlah_sesi_rias` int DEFAULT NULL,
  `include_sanggul` tinyint(1) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `pemesanan`
--

INSERT INTO `pemesanan` (`id_pemesanan`, `id_pelanggan`, `tanggal_pemesanan`, `jenis_makeup`, `total_harga`, `status_pembayaran`, `jumlah_orang_dirias`, `ada_retouch_kit`, `jumlah_sesi_rias`, `include_sanggul`) VALUES
('P01', 1, '2026-09-20', 'Make Up Wisuda', 300000, 'Lunas', 1, 1, NULL, NULL),
('P02', 14, '2027-12-12', 'Make Up Pengantin', 1080000, 'Pending', NULL, NULL, 2, 1);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `pelanggan`
--
ALTER TABLE `pelanggan`
  ADD PRIMARY KEY (`id_pelanggan`);

--
-- Indexes for table `pemesanan`
--
ALTER TABLE `pemesanan`
  ADD PRIMARY KEY (`id_pemesanan`),
  ADD KEY `id_pelanggan` (`id_pelanggan`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `pelanggan`
--
ALTER TABLE `pelanggan`
  MODIFY `id_pelanggan` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `pemesanan`
--
ALTER TABLE `pemesanan`
  ADD CONSTRAINT `pemesanan_ibfk_1` FOREIGN KEY (`id_pelanggan`) REFERENCES `pelanggan` (`id_pelanggan`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
