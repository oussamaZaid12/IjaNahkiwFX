-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: May 07, 2024 at 12:45 PM
-- Server version: 10.4.28-MariaDB
-- PHP Version: 8.0.28

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `ijanahkiw2`
--

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `id` int(11) NOT NULL,
  `email` varchar(180) NOT NULL,
  `roles` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '(DC2Type:json)' CHECK (json_valid(`roles`)),
  `password` varchar(255) NOT NULL,
  `nom` varchar(255) NOT NULL,
  `prenom` varchar(255) NOT NULL,
  `age` int(11) DEFAULT NULL,
  `is_banned` tinyint(1) DEFAULT NULL,
  `is_verified` tinyint(1) DEFAULT NULL,
  `profile_picture` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`id`, `email`, `roles`, `password`, `nom`, `prenom`, `age`, `is_banned`, `is_verified`, `profile_picture`) VALUES
(1, 'raed@gmail.com', '[\"ROLE_ADMIN\"]', '$2y$13$fRzgyVgf3HFds2/FWvNHLOiJy17v0h0/DFyyA6Ndr4efxc/tYnihW', 'raedd', 'yengui', 22, NULL, NULL, 'Capturedcran2023-07-03161645-65e997b162d27.png'),
(2, 'salma@gmail.com', '[\"ROLE_PATIENT\"]', '$2a$12$obClIYorkYEPeoYN1yq/buMxGXCMHC5ySB63akmlSPK9cXesKA73K', 'salma', '12345678', 20, 0, NULL, 'avatar.png'),
(3, 'brahemraed6@gmail.com', '[\"ROLE_THERAPEUTE\"]', '$2a$10$njpP6QnwZKmDsUTdedeJ.OhB5V.oCTwwQ/WG7T8UFnYMG3iSWTEkS', 'raed', 'raedraed', 20, NULL, NULL, 'avatar.png'),
(4, 'aziz@gmail.com', '[\"ROLE_ADMIN\"]', '$2a$10$7HCP1qQuz74wOYf7MvuVOeIbkMM2wkluTYaRCMBfSxF2IRwYEia8.', 'aziz', 'raedraed', 20, NULL, NULL, 'avatar.png'),
(5, 'mohamed@gmail.com', '[\"ROLE_THERAPEUTE\"]', '$2a$10$4LJ68Z72NM9ocAxMuPnxce0R0dYignd4C9EsteEhhdBUh4.eGISAK', 'mohamed', 'raedraed', 23, NULL, NULL, 'avatar.png');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UNIQ_8D93D649E7927C74` (`email`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `user`
--
ALTER TABLE `user`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
