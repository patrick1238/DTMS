# 01 create database
# create database:
CREATE SCHEMA IF NOT EXISTS `linfo_open_db` ;
# 02 create user & grant rights
GRANT ALL PRIVILEGES ON linfo_open_db.* TO 'linfo_user'@'%' IDENTIFIED BY 'l1nf0_pwd';

# 03 create tables
CREATE TABLE IF NOT EXISTS `linfo_open_db`.`contact_person` (
  `idcontact_person` INT UNSIGNED NOT NULL,
  `title` VARCHAR(45) NULL,
  `forename` VARCHAR(45) NULL,
  `surname` VARCHAR(45) NULL,
  `email` VARCHAR(45) NULL,
  `phone` VARCHAR(45) NULL,
  PRIMARY KEY (`idcontact_person`))
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `linfo_open_db`.`clinic` (
  `idclinic` INT UNSIGNED NOT NULL,
  `name` VARCHAR(100) NULL,
  `city` VARCHAR(25) NULL,
  `street` VARCHAR(45) NULL,
  `zipcode` VARCHAR(10) NULL,
  PRIMARY KEY (`idclinic`))
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `linfo_open_db`.`contact_for_clinic` (
  `fkey_cfc_clinic` INT UNSIGNED NOT NULL,
  `fkey_cfc_contact` INT UNSIGNED NOT NULL,
  `notes` VARCHAR(400) NULL DEFAULT 'Allgemeiner Ansprechpartner',
  PRIMARY KEY (`fkey_cfc_clinic`, `fkey_cfc_contact`),
  INDEX `contact_idx` (`fkey_cfc_contact` ASC),
  CONSTRAINT `fkey_cfc_contact`
    FOREIGN KEY (`fkey_cfc_contact`)
    REFERENCES `linfo_open_db`.`contact_person` (`idcontact_person`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fkey_cfc_clinic`
    FOREIGN KEY (`fkey_cfc_clinic`)
    REFERENCES `linfo_open_db`.`clinic` (`idclinic`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `linfo_open_db`.`clinic_case` (
  `idcase` INT UNSIGNED NOT NULL,
  `case_number` VARCHAR(45) NOT NULL,
  `fkey_case_clinic` INT UNSIGNED NOT NULL,
  `case_entry_date` DATE NULL,
  `case_diagnosis` VARCHAR(100) NULL,
  `case_submitter` INT UNSIGNED NULL,
  PRIMARY KEY (`idcase`),
  INDEX `clinic_idx` (`fkey_case_clinic` ASC),
  INDEX `fkey_case_submitter_idx` (`case_submitter` ASC),
  CONSTRAINT `fkey_case_clinic`
    FOREIGN KEY (`fkey_case_clinic`)
    REFERENCES `linfo_open_db`.`clinic` (`idclinic`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fkey_case_submitter`
    FOREIGN KEY (`case_submitter`)
    REFERENCES `linfo_open_db`.`submitter` (`idsubmitter`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `linfo_open_db`.`contact_person` (
  `idcontact_person` INT UNSIGNED NOT NULL,
  `title` VARCHAR(45) NULL,
  `forename` VARCHAR(45) NULL,
  `surname` VARCHAR(45) NULL,
  `email` VARCHAR(45) NULL,
  `phone` VARCHAR(45) NULL,
  PRIMARY KEY (`idcontact_person`))
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `linfo_open_db`.`service_definition` (
  `idservice_definition` INT UNSIGNED NOT NULL,
  `name` VARCHAR(100) NULL,
  `description` VARCHAR(400) NULL,
  `parent_definition_id` INT UNSIGNED NULL DEFAULT NULL,
  PRIMARY KEY (`idservice_definition`),
  INDEX `parentDefinition_idx` (`parent_definition_id` ASC),
  CONSTRAINT `parent_definition`
    FOREIGN KEY (`parent_definition_id`)
    REFERENCES `linfo_open_db`.`service_definition` (`idservice_definition`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `linfo_open_db`.`metadata_value` (
  `metadata_value_id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `value_type` VARCHAR(50) NOT NULL DEFAULT 'integer',
  `meta_key` VARCHAR(50) NULL,
  `depricated` TINYINT NULL DEFAULT 0,
  `unit` VARCHAR(10) NULL,
  PRIMARY KEY (`metadata_value_id`))
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `linfo_open_db`.`metadata_defaults` (
  `fkey_service_definition` INT UNSIGNED NOT NULL,
  `fkey_default_value` INT UNSIGNED NOT NULL,
  INDEX `service_definition_idx` (`fkey_service_definition` ASC),
  PRIMARY KEY (`fkey_service_definition`, `fkey_default_value`),
  INDEX `value_idx` (`fkey_default_value` ASC),
  UNIQUE INDEX `unique_idx` (`fkey_service_definition` ASC, `fkey_default_value` ASC),
  CONSTRAINT `service_definition`
    FOREIGN KEY (`fkey_service_definition`)
    REFERENCES `linfo_open_db`.`service_definition` (`idservice_definition`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `value`
    FOREIGN KEY (`fkey_default_value`)
    REFERENCES `linfo_open_db`.`metadata_value` (`metadata_value_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `linfo_open_db`.`service` (
  `idservice` INT UNSIGNED NOT NULL,
  `fkey_case` INT UNSIGNED NULL,
  `fkey_service_definition` INT UNSIGNED NULL,
  PRIMARY KEY (`idservice`),
  INDEX `case_idx` (`fkey_case` ASC),
  INDEX `definition_idx` (`fkey_service_definition` ASC),
  CONSTRAINT `case`
    FOREIGN KEY (`fkey_case`)
    REFERENCES `linfo_open_db`.`clinic_case` (`idcase`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `definition`
    FOREIGN KEY (`fkey_service_definition`)
    REFERENCES `linfo_open_db`.`service_definition` (`idservice_definition`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `linfo_open_db`.`metadata_string` (
  `fkey_service` INT UNSIGNED NOT NULL,
  `meta_key` VARCHAR(50) NOT NULL,
  `meta_value` VARCHAR(100) NULL,
  INDEX `service_idx` (`fkey_service` ASC),
  PRIMARY KEY (`fkey_service`, `meta_key`),
  CONSTRAINT `service`
    FOREIGN KEY (`fkey_service`)
    REFERENCES `linfo_open_db`.`service` (`idservice`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `linfo_open_db`.`metadata_int` (
  `fkey_service` INT UNSIGNED NOT NULL,
  `meta_key` VARCHAR(30) NOT NULL,
  `meta_value` INT NULL,
  INDEX `service_idx` (`fkey_service` ASC),
  PRIMARY KEY (`fkey_service`, `meta_key`),
  CONSTRAINT `service1`
    FOREIGN KEY (`fkey_service`)
    REFERENCES `linfo_open_db`.`service` (`idservice`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `linfo_open_db`.`metadata_text` (
  `fkey_service` INT UNSIGNED NOT NULL,
  `meta_key` VARCHAR(50) NOT NULL,
  `meta_value` VARCHAR(1000) NULL,
  INDEX `service_idx` (`fkey_service` ASC),
  PRIMARY KEY (`fkey_service`, `meta_key`),
  CONSTRAINT `service0`
    FOREIGN KEY (`fkey_service`)
    REFERENCES `linfo_open_db`.`service` (`idservice`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `linfo_open_db`.`metadata_double` (
  `fkey_service` INT UNSIGNED NOT NULL,
  `meta_key` VARCHAR(50) NOT NULL,
  `meta_value` DOUBLE NULL,
  INDEX `service_idx` (`fkey_service` ASC),
  PRIMARY KEY (`fkey_service`, `meta_key`),
  CONSTRAINT `service2`
    FOREIGN KEY (`fkey_service`)
    REFERENCES `linfo_open_db`.`service` (`idservice`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `linfo_open_db`.`metadata_url` (
  `fkey_service` INT UNSIGNED NOT NULL,
  `meta_key` VARCHAR(50) NOT NULL,
  `meta_value` VARCHAR(200) NOT NULL,
  INDEX `service_idx` (`fkey_service` ASC),
  PRIMARY KEY (`fkey_service`, `meta_key`),
  CONSTRAINT `service3`
    FOREIGN KEY (`fkey_service`)
    REFERENCES `linfo_open_db`.`service` (`idservice`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

# user accounts and logging:
CREATE TABLE IF NOT EXISTS `linfo_open_db`.`submitter` (
  `idsubmitter` INT UNSIGNED NOT NULL,
  `surname` VARCHAR(45) NULL,
  `forename` VARCHAR(45) NULL,
  `title` VARCHAR(45) NULL,
  `login` VARCHAR(45) NULL,
  `password` VARCHAR(45) NULL,
  PRIMARY KEY (`idsubmitter`))
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `linfo_open_db`.`logs` (
  `idlog` INT UNSIGNED NOT NULL,
  `timestamp` DATE NOT NULL,
  `affected_table` VARCHAR(45) NOT NULL DEFAULT 'None',
  `message` VARCHAR(400) NOT NULL DEFAULT 'Create/Edit of table',
  `fkey_submitter` INT UNSIGNED NULL,
  PRIMARY KEY (`idlog`),
  INDEX `submitter_idx` (`fkey_submitter` ASC),
  CONSTRAINT `submitter`
    FOREIGN KEY (`fkey_submitter`)
    REFERENCES `linfo_open_db`.`submitter` (`idsubmitter`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

# 04 insert data

# SUBMITTER
INSERT INTO `linfo_open_db`.`submitter` (`idsubmitter`, `surname`, `forename`, `title`, `login`, `password`) VALUES (2, 'Root', 'Root', 'Root', 'admin', 'H0dgkin');

# CLINIC
INSERT INTO `linfo_open_db`.`clinic` (`idclinic`, `name`, `city`, `street`, `zipcode`) VALUES (1, 'Uniklinkum Frankfurt', 'Frankfurt am Main', 'Theodor-Stern-Kai 7', '60590');
INSERT INTO `linfo_open_db`.`clinic` (`idclinic`, `name`, `city`, `street`, `zipcode`) VALUES (1, 'Uniklinkum Innsbruck', 'Innsbruck', 'not set', '00000');

#METADATA VALUES
INSERT INTO `linfo_open_db`.`metadata_value` (`metadata_value_id`, `value_type`, `meta_key`, `depricated`, `unit`) VALUES (1, 'string', 'Capture date', 0, NULL);
INSERT INTO `linfo_open_db`.`metadata_value` (`metadata_value_id`, `value_type`, `meta_key`, `depricated`, `unit`) VALUES (2, 'text', 'Comment', 0, NULL);
INSERT INTO `linfo_open_db`.`metadata_value` (`metadata_value_id`, `value_type`, `meta_key`, `depricated`, `unit`) VALUES (3, 'string', 'Red', 0, NULL);
INSERT INTO `linfo_open_db`.`metadata_value` (`metadata_value_id`, `value_type`, `meta_key`, `depricated`, `unit`) VALUES (4, 'string', 'Blue', 0, NULL);
INSERT INTO `linfo_open_db`.`metadata_value` (`metadata_value_id`, `value_type`, `meta_key`, `depricated`, `unit`) VALUES (5, 'int', 'Rating', 0, NULL);
INSERT INTO `linfo_open_db`.`metadata_value` (`metadata_value_id`, `value_type`, `meta_key`, `depricated`, `unit`) VALUES (6, 'int', 'Block', 0, NULL);
INSERT INTO `linfo_open_db`.`metadata_value` (`metadata_value_id`, `value_type`, `meta_key`, `depricated`, `unit`) VALUES (7, 'double', 'Fraction', 0, NULL);
INSERT INTO `linfo_open_db`.`metadata_value` (`metadata_value_id`, `value_type`, `meta_key`, `depricated`, `unit`) VALUES (8, 'url', 'Cellgraph url', 0, NULL);
INSERT INTO `linfo_open_db`.`metadata_value` (`metadata_value_id`, `value_type`, `meta_key`, `depricated`, `unit`) VALUES (9, 'url', 'File url', 0, NULL);
INSERT INTO `linfo_open_db`.`metadata_value` (`metadata_value_id`, `value_type`, `meta_key`, `depricated`, `unit`) VALUES (10, 'text', 'Green', 0, NULL);
INSERT INTO `linfo_open_db`.`metadata_value` (`metadata_value_id`, `value_type`, `meta_key`, `depricated`, `unit`) VALUES (11, 'int', 'Tiles', 0, NULL);
INSERT INTO `linfo_open_db`.`metadata_value` (`metadata_value_id`, `value_type`, `meta_key`, `depricated`, `unit`) VALUES (12, 'double', 'Magnification', 0, NULL);
INSERT INTO `linfo_open_db`.`metadata_value` (`metadata_value_id`, `value_type`, `meta_key`, `depricated`, `unit`) VALUES (13, 'text', 'Annotation', 0, NULL);
INSERT INTO `linfo_open_db`.`metadata_value` (`metadata_value_id`, `value_type`, `meta_key`, `depricated`, `unit`) VALUES (14, 'url', 'Overview url', 0, NULL);
INSERT INTO `linfo_open_db`.`metadata_value` (`metadata_value_id`, `value_type`, `meta_key`, `depricated`, `unit`) VALUES (15, 'url', 'Imaris url', 0, NULL);
INSERT INTO `linfo_open_db`.`metadata_value` (`metadata_value_id`, `value_type`, `meta_key`, `depricated`, `unit`) VALUES (16, 'url', 'Forward reads url', 0, NULL);
INSERT INTO `linfo_open_db`.`metadata_value` (`metadata_value_id`, `value_type`, `meta_key`, `depricated`, `unit`) VALUES (17, 'url', 'Backward reads url', 0, NULL);
INSERT INTO `linfo_open_db`.`metadata_value` (`metadata_value_id`, `value_type`, `meta_key`, `depricated`, `unit`) VALUES (18, 'url', 'BAM url', 0, NULL);
INSERT INTO `linfo_open_db`.`metadata_value` (`metadata_value_id`, `value_type`, `meta_key`, `depricated`, `unit`) VALUES (19, 'url', 'Variants url', 0, NULL);
INSERT INTO `linfo_open_db`.`metadata_value` (`metadata_value_id`, `value_type`, `meta_key`, `depricated`, `unit`) VALUES (20, 'url', 'Green channel url', 0, NULL);
INSERT INTO `linfo_open_db`.`metadata_value` (`metadata_value_id`, `value_type`, `meta_key`, `depricated`, `unit`) VALUES (21, 'url', 'Red channel url', 0, NULL);

#SERVICE DEFINITIONS
INSERT INTO `linfo_open_db`.`service_definition` (`idservice_definition`, `name`, `description`, `parent_definition_id`) VALUES (1, 'Service', 'General service', NULL);
INSERT INTO `linfo_open_db`.`service_definition` (`idservice_definition`, `name`, `description`, `parent_definition_id`) VALUES (2, 'Image', 'Digital scan of a tissue section', 1);
INSERT INTO `linfo_open_db`.`service_definition` (`idservice_definition`, `name`, `description`, `parent_definition_id`) VALUES (3, '2D Image', '2D scan of a tissue section, e.g. a WSI', 2);
INSERT INTO `linfo_open_db`.`service_definition` (`idservice_definition`, `name`, `description`, `parent_definition_id`) VALUES (4, '3D Image', '3D scan of a tissue section', 2);
INSERT INTO `linfo_open_db`.`service_definition` (`idservice_definition`, `name`, `description`, `parent_definition_id`) VALUES (5, '4D Image', 'Time lapsed data of a tissue section', 2);
INSERT INTO `linfo_open_db`.`service_definition` (`idservice_definition`, `name`, `description`, `parent_definition_id`) VALUES (6, 'Genomics', 'NGS data - raw sequences, alignment, and variant calls', 1);
INSERT INTO `linfo_open_db`.`service_definition` (`idservice_definition`, `name`, `description`, `parent_definition_id`) VALUES (7, 'Methylation', 'Methylation data', 1);

#SERVICE METADATA DEFAULTS
INSERT INTO `linfo_open_db`.`metadata_defaults` (`fkey_service_definition`, `fkey_default_value`) VALUES (1, 1);
INSERT INTO `linfo_open_db`.`metadata_defaults` (`fkey_service_definition`, `fkey_default_value`) VALUES (1, 2);
INSERT INTO `linfo_open_db`.`metadata_defaults` (`fkey_service_definition`, `fkey_default_value`) VALUES (2, 3);
INSERT INTO `linfo_open_db`.`metadata_defaults` (`fkey_service_definition`, `fkey_default_value`) VALUES (2, 4);
INSERT INTO `linfo_open_db`.`metadata_defaults` (`fkey_service_definition`, `fkey_default_value`) VALUES (2, 5);
INSERT INTO `linfo_open_db`.`metadata_defaults` (`fkey_service_definition`, `fkey_default_value`) VALUES (2, 9);
INSERT INTO `linfo_open_db`.`metadata_defaults` (`fkey_service_definition`, `fkey_default_value`) VALUES (3, 6);
INSERT INTO `linfo_open_db`.`metadata_defaults` (`fkey_service_definition`, `fkey_default_value`) VALUES (3, 7);
INSERT INTO `linfo_open_db`.`metadata_defaults` (`fkey_service_definition`, `fkey_default_value`) VALUES (3, 8);
INSERT INTO `linfo_open_db`.`metadata_defaults` (`fkey_service_definition`, `fkey_default_value`) VALUES (4, 10);
INSERT INTO `linfo_open_db`.`metadata_defaults` (`fkey_service_definition`, `fkey_default_value`) VALUES (4, 11);
INSERT INTO `linfo_open_db`.`metadata_defaults` (`fkey_service_definition`, `fkey_default_value`) VALUES (4, 12);
INSERT INTO `linfo_open_db`.`metadata_defaults` (`fkey_service_definition`, `fkey_default_value`) VALUES (4, 13);
INSERT INTO `linfo_open_db`.`metadata_defaults` (`fkey_service_definition`, `fkey_default_value`) VALUES (4, 14);
INSERT INTO `linfo_open_db`.`metadata_defaults` (`fkey_service_definition`, `fkey_default_value`) VALUES (4, 15);
INSERT INTO `linfo_open_db`.`metadata_defaults` (`fkey_service_definition`, `fkey_default_value`) VALUES (5, 10);
INSERT INTO `linfo_open_db`.`metadata_defaults` (`fkey_service_definition`, `fkey_default_value`) VALUES (5, 11);
INSERT INTO `linfo_open_db`.`metadata_defaults` (`fkey_service_definition`, `fkey_default_value`) VALUES (5, 12);
INSERT INTO `linfo_open_db`.`metadata_defaults` (`fkey_service_definition`, `fkey_default_value`) VALUES (5, 13);
INSERT INTO `linfo_open_db`.`metadata_defaults` (`fkey_service_definition`, `fkey_default_value`) VALUES (5, 14);
INSERT INTO `linfo_open_db`.`metadata_defaults` (`fkey_service_definition`, `fkey_default_value`) VALUES (5, 15);
INSERT INTO `linfo_open_db`.`metadata_defaults` (`fkey_service_definition`, `fkey_default_value`) VALUES (6, 16);
INSERT INTO `linfo_open_db`.`metadata_defaults` (`fkey_service_definition`, `fkey_default_value`) VALUES (6, 17);
INSERT INTO `linfo_open_db`.`metadata_defaults` (`fkey_service_definition`, `fkey_default_value`) VALUES (6, 18);
INSERT INTO `linfo_open_db`.`metadata_defaults` (`fkey_service_definition`, `fkey_default_value`) VALUES (6, 19);
INSERT INTO `linfo_open_db`.`metadata_defaults` (`fkey_service_definition`, `fkey_default_value`) VALUES (7, 20);
INSERT INTO `linfo_open_db`.`metadata_defaults` (`fkey_service_definition`, `fkey_default_value`) VALUES (7, 21);