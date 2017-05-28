-- phpMyAdmin SQL Dump
-- version 4.6.5.2
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: May 22, 2017 at 10:21 PM
-- Server version: 10.1.21-MariaDB
-- PHP Version: 5.6.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `nordicmotorhomes`
--
CREATE DATABASE IF NOT EXISTS `nordicmotorhomes` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
USE `nordicmotorhomes`;

-- --------------------------------------------------------

--
-- Table structure for table `customers`
--

CREATE TABLE `customers` (
  `id` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `cpr` varchar(255) NOT NULL,
  `address` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `phone` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

--
-- Dumping data for table `customers`
--

INSERT INTO `customers` (`id`, `name`, `cpr`, `address`, `email`, `phone`) VALUES
(0, 'null', 'null', 'null', 'null', 'null'),
(1, 'Hieu', '123456-7890', 'Lygten 37', 'hieu@dk.dk', '2141455'),
(2, 'Andrei', '123456-7891', 'Lygten 39', 'and@dk.dk', '234324'),
(3, 'Ersan', '123456-7892', 'Lygten 30', 'ers@dk.dk', '234325'),
(4, 'Rolandas', '123456-7893', 'Lgyen', 'rol@dk.dk', '234324');

-- --------------------------------------------------------

--
-- Table structure for table `extras`
--

CREATE TABLE `extras` (
  `id` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `quantity` int(11) NOT NULL,
  `price` double NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

--
-- Dumping data for table `extras`
--

INSERT INTO `extras` (`id`, `name`, `quantity`, `price`) VALUES
(1, 'Bike Rack', 3, 50),
(2, 'Bed linen', 2, 20),
(3, 'Child Seat', 0, 40),
(4, 'Picnic Table', 5, 40),
(5, 'Chair', 22, 20),
(6, 'Towel', 20, 10);

-- --------------------------------------------------------

--
-- Table structure for table `locations`
--

CREATE TABLE `locations` (
  `id` int(11) NOT NULL,
  `address` varchar(255) NOT NULL,
  `km` int(11) NOT NULL,
  `private` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

--
-- Dumping data for table `locations`
--

INSERT INTO `locations` (`id`, `address`, `km`, `private`) VALUES
(0, 'null', 0, 1),
(1, 'CPH', 0, 0),
(2, 'Norreport', 0, 0),
(3, 'Lyngby', 15, 0),
(4, 'Lygten 37', 10, 0);

-- --------------------------------------------------------

--
-- Table structure for table `maintenances`
--

CREATE TABLE `maintenances` (
  `id` int(11) NOT NULL,
  `car_id` int(11) NOT NULL,
  `repair_cost` double NOT NULL,
  `message` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- --------------------------------------------------------

--
-- Table structure for table `motorhomes`
--

CREATE TABLE `motorhomes` (
  `id` int(11) NOT NULL,
  `brand` varchar(255) NOT NULL,
  `model` varchar(255) NOT NULL,
  `capacity` int(11) NOT NULL,
  `price` double NOT NULL,
  `km_driven` int(11) NOT NULL,
  `cleaned` tinyint(1) NOT NULL,
  `broken` tinyint(1) NOT NULL,
  `reserved` tinyint(1) NOT NULL,
  `rented` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `motorhomes`
--

INSERT INTO `motorhomes` (`id`, `brand`, `model`, `capacity`, `price`, `km_driven`, `cleaned`, `broken`, `reserved`, `rented`) VALUES
(1, 'Toyota', 'V2', 5, 150, 9001, 1, 0, 0, 0),
(2, 'BMW', 'T2', 4, 200, 2001, 1, 0, 0, 0),
(3, 'Audi', 'A8', 6, 200, 600, 1, 0, 0, 0),
(4, 'Ford', 'MA4', 7, 160, 5000, 1, 0, 0, 0),
(5, 'Toyota', 'M183', 7, 190, 1500, 1, 0, 0, 0),
(6, 'Ford', 'FX09', 10, 520, 100, 1, 0, 0, 0);

-- --------------------------------------------------------

--
-- Table structure for table `rentals`
--

CREATE TABLE `rentals` (
  `id` int(11) NOT NULL,
  `customer_id` int(11) NOT NULL,
  `start_date` date NOT NULL,
  `end_date` date NOT NULL,
  `pickup` int(11) NOT NULL,
  `dropoff` int(11) NOT NULL,
  `price` double NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- --------------------------------------------------------

--
-- Table structure for table `rentals_extras`
--

CREATE TABLE `rentals_extras` (
  `id` int(11) NOT NULL,
  `rental_id` int(11) NOT NULL,
  `extra_id` int(11) NOT NULL,
  `quantity` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- --------------------------------------------------------

--
-- Table structure for table `rentals_motorhomes`
--

CREATE TABLE `rentals_motorhomes` (
  `id` int(11) NOT NULL,
  `rental_id` int(11) NOT NULL,
  `motorhome_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- --------------------------------------------------------

--
-- Table structure for table `reservations`
--

CREATE TABLE `reservations` (
  `id` int(11) NOT NULL,
  `customer_id` int(11) NOT NULL,
  `reservation_date` date NOT NULL,
  `start_date` date NOT NULL,
  `end_date` date NOT NULL,
  `pickup` int(11) NOT NULL,
  `dropoff` int(11) NOT NULL,
  `price` double NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- --------------------------------------------------------

--
-- Table structure for table `reservations_extras`
--

CREATE TABLE `reservations_extras` (
  `id` int(11) NOT NULL,
  `reservation_id` int(11) NOT NULL,
  `extra_id` int(11) NOT NULL,
  `quantity` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- --------------------------------------------------------

--
-- Table structure for table `reservations_motorhomes`
--

CREATE TABLE `reservations_motorhomes` (
  `id` int(11) NOT NULL,
  `reservation_id` int(11) NOT NULL,
  `motorhome_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `username` varchar(500) NOT NULL,
  `password` varchar(500) NOT NULL,
  `type` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `username`, `password`, `type`) VALUES
(1, 'salesstaff', '6acf10b8222fbc979eabfd8db63b2e0b', 'salesstaff'),
(3, 'bookkeper', '69825da5b50f8583765be93ca01e9651', 'bookkeper'),
(4, 'automecanic', 'b9b51327de5b4810e78d5a557059b2c7', 'automecanic'),
(7, 'cleaningstaff', 'ef81ac16cbfc2eaf47485198970bc8b3', 'cleaningstaff'),
(11, 'admin', '21232f297a57a5a743894a0e4a801fc3', 'admin');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `customers`
--
ALTER TABLE `customers`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `cpr` (`cpr`);

--
-- Indexes for table `extras`
--
ALTER TABLE `extras`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `locations`
--
ALTER TABLE `locations`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `maintenances`
--
ALTER TABLE `maintenances`
  ADD PRIMARY KEY (`id`),
  ADD KEY `car_id` (`car_id`);

--
-- Indexes for table `motorhomes`
--
ALTER TABLE `motorhomes`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `rentals`
--
ALTER TABLE `rentals`
  ADD PRIMARY KEY (`id`),
  ADD KEY `pickup` (`pickup`),
  ADD KEY `dropoff` (`dropoff`),
  ADD KEY `customer_id` (`customer_id`);

--
-- Indexes for table `rentals_extras`
--
ALTER TABLE `rentals_extras`
  ADD PRIMARY KEY (`id`),
  ADD KEY `rental_id` (`rental_id`),
  ADD KEY `extra_id` (`extra_id`);

--
-- Indexes for table `rentals_motorhomes`
--
ALTER TABLE `rentals_motorhomes`
  ADD PRIMARY KEY (`id`),
  ADD KEY `rental_id` (`rental_id`),
  ADD KEY `motorhome_id` (`motorhome_id`);

--
-- Indexes for table `reservations`
--
ALTER TABLE `reservations`
  ADD PRIMARY KEY (`id`),
  ADD KEY `pickup` (`pickup`),
  ADD KEY `dropoff` (`dropoff`),
  ADD KEY `customer_id` (`customer_id`);

--
-- Indexes for table `reservations_extras`
--
ALTER TABLE `reservations_extras`
  ADD PRIMARY KEY (`id`),
  ADD KEY `reservation_id` (`reservation_id`),
  ADD KEY `extra_id` (`extra_id`);

--
-- Indexes for table `reservations_motorhomes`
--
ALTER TABLE `reservations_motorhomes`
  ADD PRIMARY KEY (`id`),
  ADD KEY `reservation_id` (`reservation_id`),
  ADD KEY `motorhome_id` (`motorhome_id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `customers`
--
ALTER TABLE `customers`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;
--
-- AUTO_INCREMENT for table `extras`
--
ALTER TABLE `extras`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;
--
-- AUTO_INCREMENT for table `locations`
--
ALTER TABLE `locations`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;
--
-- AUTO_INCREMENT for table `maintenances`
--
ALTER TABLE `maintenances`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;
--
-- AUTO_INCREMENT for table `motorhomes`
--
ALTER TABLE `motorhomes`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;
--
-- AUTO_INCREMENT for table `rentals`
--
ALTER TABLE `rentals`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `rentals_extras`
--
ALTER TABLE `rentals_extras`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `rentals_motorhomes`
--
ALTER TABLE `rentals_motorhomes`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `reservations`
--
ALTER TABLE `reservations`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `reservations_extras`
--
ALTER TABLE `reservations_extras`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `reservations_motorhomes`
--
ALTER TABLE `reservations_motorhomes`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;
--
-- Constraints for dumped tables
--

--
-- Constraints for table `maintenances`
--
ALTER TABLE `maintenances`
  ADD CONSTRAINT `maintenances_ibfk_1` FOREIGN KEY (`car_id`) REFERENCES `motorhomes` (`id`);

--
-- Constraints for table `rentals`
--
ALTER TABLE `rentals`
  ADD CONSTRAINT `rentals_ibfk_1` FOREIGN KEY (`pickup`) REFERENCES `locations` (`id`),
  ADD CONSTRAINT `rentals_ibfk_2` FOREIGN KEY (`dropoff`) REFERENCES `locations` (`id`),
  ADD CONSTRAINT `rentals_ibfk_3` FOREIGN KEY (`customer_id`) REFERENCES `customers` (`id`);

--
-- Constraints for table `rentals_extras`
--
ALTER TABLE `rentals_extras`
  ADD CONSTRAINT `rentals_extras_ibfk_1` FOREIGN KEY (`rental_id`) REFERENCES `rentals` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `rentals_extras_ibfk_2` FOREIGN KEY (`extra_id`) REFERENCES `extras` (`id`);

--
-- Constraints for table `rentals_motorhomes`
--
ALTER TABLE `rentals_motorhomes`
  ADD CONSTRAINT `rentals_motorhomes_ibfk_1` FOREIGN KEY (`rental_id`) REFERENCES `rentals` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `rentals_motorhomes_ibfk_2` FOREIGN KEY (`motorhome_id`) REFERENCES `motorhomes` (`id`);

--
-- Constraints for table `reservations`
--
ALTER TABLE `reservations`
  ADD CONSTRAINT `reservations_ibfk_1` FOREIGN KEY (`pickup`) REFERENCES `locations` (`id`),
  ADD CONSTRAINT `reservations_ibfk_2` FOREIGN KEY (`dropoff`) REFERENCES `locations` (`id`),
  ADD CONSTRAINT `reservations_ibfk_3` FOREIGN KEY (`customer_id`) REFERENCES `customers` (`id`);

--
-- Constraints for table `reservations_extras`
--
ALTER TABLE `reservations_extras`
  ADD CONSTRAINT `reservations_extras_ibfk_1` FOREIGN KEY (`reservation_id`) REFERENCES `reservations` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `reservations_extras_ibfk_2` FOREIGN KEY (`extra_id`) REFERENCES `extras` (`id`);

--
-- Constraints for table `reservations_motorhomes`
--
ALTER TABLE `reservations_motorhomes`
  ADD CONSTRAINT `reservations_motorhomes_ibfk_1` FOREIGN KEY (`reservation_id`) REFERENCES `reservations` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `reservations_motorhomes_ibfk_2` FOREIGN KEY (`motorhome_id`) REFERENCES `motorhomes` (`id`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
