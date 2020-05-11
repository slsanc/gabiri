-- -----------------------------------------------------
-- Data for table `gabiri`.`Employment_Types`
-- -----------------------------------------------------
START TRANSACTION;
USE `gabiri`;
INSERT IGNORE INTO `gabiri`.`Employment_Types` (`employment_type_id`, `employment_type`) VALUES (1, 'Full Time Salaried');
INSERT IGNORE INTO `gabiri`.`Employment_Types` (`employment_type_id`, `employment_type`) VALUES (2, 'Full Time Wage-Based');
INSERT IGNORE INTO `gabiri`.`Employment_Types` (`employment_type_id`, `employment_type`) VALUES (3, 'Part Time Wage-Based');
INSERT IGNORE INTO `gabiri`.`Employment_Types` (`employment_type_id`, `employment_type`) VALUES (4, 'Independent Contractor');
INSERT IGNORE INTO `gabiri`.`Employment_Types` (`employment_type_id`, `employment_type`) VALUES (5, 'Freelance');
INSERT IGNORE INTO `gabiri`.`Employment_Types` (`employment_type_id`, `employment_type`) VALUES (6, 'Other');

COMMIT;


-- -----------------------------------------------------
-- Data for table `gabiri`.`Application_Status_Types`
-- -----------------------------------------------------
START TRANSACTION;
USE `gabiri`;
INSERT IGNORE INTO `gabiri`.`Application_Status_Types` (`status_id`, `status_description`) VALUES (1, 'Under Consideration');
INSERT IGNORE INTO `gabiri`.`Application_Status_Types` (`status_id`, `status_description`) VALUES (2, 'No Longer Under Consideration');
INSERT IGNORE INTO `gabiri`.`Application_Status_Types` (`status_id`, `status_description`) VALUES (3, 'Hired');
INSERT IGNORE INTO `gabiri`.`Application_Status_Types` (`status_id`, `status_description`) VALUES (4, 'Hired, Position Completed or Terminated');

COMMIT;

