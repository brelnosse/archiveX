-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Hôte : localhost
-- Généré le : dim. 15 fév. 2026 à 14:27
-- Version du serveur : 10.4.32-MariaDB
-- Version de PHP : 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `archives`
--

-- --------------------------------------------------------

--
-- Structure de la table `admin`
--

CREATE TABLE `admin` (
  `id` int(11) NOT NULL,
  `pseudo` varchar(100) NOT NULL,
  `email` varchar(150) NOT NULL,
  `mdp` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `admin`
--

INSERT INTO `admin` (`id`, `pseudo`, `email`, `mdp`) VALUES
(1, 'brel', 'nossebrel@gmail.com', 'admin123');

-- --------------------------------------------------------

--
-- Structure de la table `document`
--

CREATE TABLE `document` (
  `id` int(11) NOT NULL,
  `nomEtu` varchar(255) NOT NULL,
  `nomEn` varchar(255) NOT NULL,
  `file` varchar(500) NOT NULL,
  `dept` varchar(100) DEFAULT NULL,
  `filiere` varchar(100) DEFAULT NULL,
  `sujet` varchar(255) DEFAULT NULL,
  `domaine` varchar(150) DEFAULT NULL,
  `type` varchar(100) DEFAULT NULL,
  `date_s` date DEFAULT NULL,
  `date_a` date DEFAULT curdate()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `document`
--

INSERT INTO `document` (`id`, `nomEtu`, `nomEn`, `file`, `dept`, `filiere`, `sujet`, `domaine`, `type`, `date_s`, `date_a`) VALUES
(2, 'brel', 'brelle', 'src/upload/modal-verbs-2-obligation-necessity-prohibition-advice-british-english-student-B1-B2.pdf', '3iac', 'GL', 'développement logiciel', 'informatique', 'PROJET TUTOREE', '2026-02-20', '2026-02-15');

-- --------------------------------------------------------

--
-- Structure de la table `gestionnaire`
--

CREATE TABLE `gestionnaire` (
  `id` int(11) NOT NULL,
  `nom` varchar(100) NOT NULL,
  `prenom` varchar(100) NOT NULL,
  `email` varchar(150) NOT NULL,
  `code_conn` varchar(10) NOT NULL,
  `adminID` int(11) DEFAULT NULL,
  `date_ajout` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `gestionnaire`
--

INSERT INTO `gestionnaire` (`id`, `nom`, `prenom`, `email`, `code_conn`, `adminID`, `date_ajout`) VALUES
(1, 'brelle', 'nosse', 'brelnosse@gmail.com', 'KC81H', 1, '2026-02-15');

-- --------------------------------------------------------

--
-- Structure de la table `telechargement`
--

CREATE TABLE `telechargement` (
  `id_tel` int(11) NOT NULL,
  `id_file` int(11) NOT NULL,
  `date_tel` datetime DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Index pour les tables déchargées
--

--
-- Index pour la table `admin`
--
ALTER TABLE `admin`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `email` (`email`);

--
-- Index pour la table `document`
--
ALTER TABLE `document`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `file` (`file`);

--
-- Index pour la table `gestionnaire`
--
ALTER TABLE `gestionnaire`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `email` (`email`),
  ADD UNIQUE KEY `code_conn` (`code_conn`),
  ADD KEY `adminID` (`adminID`);

--
-- Index pour la table `telechargement`
--
ALTER TABLE `telechargement`
  ADD PRIMARY KEY (`id_tel`),
  ADD KEY `id_file` (`id_file`);

--
-- AUTO_INCREMENT pour les tables déchargées
--

--
-- AUTO_INCREMENT pour la table `admin`
--
ALTER TABLE `admin`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT pour la table `document`
--
ALTER TABLE `document`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT pour la table `gestionnaire`
--
ALTER TABLE `gestionnaire`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT pour la table `telechargement`
--
ALTER TABLE `telechargement`
  MODIFY `id_tel` int(11) NOT NULL AUTO_INCREMENT;

--
-- Contraintes pour les tables déchargées
--

--
-- Contraintes pour la table `gestionnaire`
--
ALTER TABLE `gestionnaire`
  ADD CONSTRAINT `gestionnaire_ibfk_1` FOREIGN KEY (`adminID`) REFERENCES `admin` (`id`) ON DELETE CASCADE;

--
-- Contraintes pour la table `telechargement`
--
ALTER TABLE `telechargement`
  ADD CONSTRAINT `telechargement_ibfk_1` FOREIGN KEY (`id_file`) REFERENCES `document` (`id`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
