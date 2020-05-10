SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema gabiri
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema gabiri
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `gabiri` DEFAULT CHARACTER SET utf8 ;
USE `gabiri` ;

-- -----------------------------------------------------
-- Table `gabiri`.`Departments`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gabiri`.`Departments` (
  `department_id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `department_name` VARCHAR(45) NULL,
  PRIMARY KEY (`department_id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `gabiri`.`Employment_Types`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gabiri`.`Employment_Types` (
  `employment_type_id` TINYINT UNSIGNED NOT NULL,
  `employment_type` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`employment_type_id`),
  UNIQUE INDEX `idemployment_type_id_UNIQUE` (`employment_type_id` ASC) VISIBLE,
  UNIQUE INDEX `employment_type_UNIQUE` (`employment_type` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `gabiri`.`Positions`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gabiri`.`Positions` (
  `position_id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `position_title` VARCHAR(45) NOT NULL,
  `department_id` INT UNSIGNED NULL,
  `employment_type` TINYINT UNSIGNED NULL,
  `max_salary` MEDIUMINT UNSIGNED NULL,
  `max_wage` DECIMAL(5,2) NULL,
  `city` VARCHAR(20) NULL,
  `state_or_provence` VARCHAR(2) NULL,
  `start_date` DATE NULL,
  `date_created` DATE NULL,
  `date_filled` DATE NULL,
  `date_ended` DATE NULL,
  PRIMARY KEY (`position_id`),
  UNIQUE INDEX `position_id_UNIQUE` (`position_id` ASC) VISIBLE,
  INDEX `FK_positions_departments_idx` (`department_id` ASC) VISIBLE,
  INDEX `FK_positions_employment_types_idx` (`employment_type` ASC) VISIBLE,
  CONSTRAINT `FK_positions_departments`
    FOREIGN KEY (`department_id`)
    REFERENCES `gabiri`.`Departments` (`department_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_positions_employment_types`
    FOREIGN KEY (`employment_type`)
    REFERENCES `gabiri`.`Employment_Types` (`employment_type_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `gabiri`.`Applicants`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gabiri`.`Applicants` (
  `applicant_id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `first_name` VARCHAR(20) NOT NULL,
  `last_name` VARCHAR(30) NOT NULL,
  `email` VARCHAR(45) NULL,
  `cell_phone` VARCHAR(12) NULL,
  `work_phone` VARCHAR(12) NULL,
  `home_phone` VARCHAR(12) NULL,
  `address` VARCHAR(45) NULL,
  `city` VARCHAR(20) NULL,
  `state_or_provence` VARCHAR(2) NULL,
  `hidden_status` TINYINT(1) NULL DEFAULT 0,
  PRIMARY KEY (`applicant_id`),
  UNIQUE INDEX `applicant_id_UNIQUE` (`applicant_id` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `gabiri`.`Application_Status_Types`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gabiri`.`Application_Status_Types` (
  `status_id` TINYINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `status_description` VARCHAR(30) NULL,
  PRIMARY KEY (`status_id`),
  UNIQUE INDEX `status_id_UNIQUE` (`status_id` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `gabiri`.`Applications`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gabiri`.`Applications` (
  `applicant_id` INT UNSIGNED NOT NULL,
  `position_id` INT UNSIGNED NOT NULL,
  `status` TINYINT UNSIGNED NOT NULL,
  `date_applied` DATE NOT NULL,
  `rating` TINYINT NULL,
  INDEX `FK_applications_positions_idx` (`position_id` ASC) VISIBLE,
  INDEX `FK_applications_applicants_idx` (`applicant_id` ASC) VISIBLE,
  PRIMARY KEY (`applicant_id`, `position_id`),
  INDEX `FK_applications_statustypes_idx` (`status` ASC) VISIBLE,
  CONSTRAINT `FK_applications_positions`
    FOREIGN KEY (`position_id`)
    REFERENCES `gabiri`.`Positions` (`position_id`)
    ON DELETE CASCADE
    ON UPDATE RESTRICT,
  CONSTRAINT `FK_applications_applicants`
    FOREIGN KEY (`applicant_id`)
    REFERENCES `gabiri`.`Applicants` (`applicant_id`)
    ON DELETE CASCADE
    ON UPDATE RESTRICT,
  CONSTRAINT `FK_applications_statustypes`
    FOREIGN KEY (`status`)
    REFERENCES `gabiri`.`Application_Status_Types` (`status_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `gabiri`.`Documents`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gabiri`.`Documents` (
  `document_id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `applicant_id` INT UNSIGNED NOT NULL,
  `file_name` VARCHAR(255) NULL,
  `document_data` MEDIUMBLOB NOT NULL,
  PRIMARY KEY (`document_id`),
  UNIQUE INDEX `document_id_UNIQUE` (`document_id` ASC) VISIBLE,
  CONSTRAINT `FK_documents_applicants`
    FOREIGN KEY (`applicant_id`)
    REFERENCES `gabiri`.`Applicants` (`applicant_id`)
    ON DELETE CASCADE
    ON UPDATE RESTRICT)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;