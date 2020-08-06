# CLINIC
INSERT INTO `linfo_open_db`.`clinic` (`idclinic`, `name`, `city`, `street`, `zipcode`) VALUES (3, 'Musterklinik', 'Einstadt', 'Zweistr. 23', '65734');

#SUBMITTERS
INSERT INTO `linfo_open_db`.`submitter` (`idsubmitter`, `surname`, `forename`, `title`, `login`, `password`) VALUES (2, 'Mustermann', 'Max', 'Herr', 'guest', '123456');
INSERT INTO `linfo_open_db`.`submitter` (`idsubmitter`, `surname`, `forename`, `title`, `login`, `password`) VALUES (3, 'Musterfrau', 'Miriam', 'Frau', 'guest2', '654321');

# CLINIC_CASE
INSERT INTO `linfo_open_db`.`clinic_case` (`idcase`, `case_number`, `fkey_case_clinic`, `case_entry_date`, `case_diagnosis`, `fkey_case_submitter`) VALUES (1, 'H1001-19', 1, '2017-03-23', 'cHL (MC)', 1);
INSERT INTO `linfo_open_db`.`clinic_case` (`idcase`, `case_number`, `fkey_case_clinic`, `case_entry_date`, `case_diagnosis`, `fkey_case_submitter`) VALUES (2, 'H1321-19', 1, '2017-06-01', 'cHL (MC)', 2);
INSERT INTO `linfo_open_db`.`clinic_case` (`idcase`, `case_number`, `fkey_case_clinic`, `case_entry_date`, `case_diagnosis`, `fkey_case_submitter`) VALUES (3, 'K2233-18', 1, '2017-08-13', 'cHL (NS)', 3);
INSERT INTO `linfo_open_db`.`clinic_case` (`idcase`, `case_number`, `fkey_case_clinic`, `case_entry_date`, `case_diagnosis`, `fkey_case_submitter`) VALUES (4, 'K1234-19', 1, '2017-12-24', 'Lymphadenitis', 2);
INSERT INTO `linfo_open_db`.`clinic_case` (`idcase`, `case_number`, `fkey_case_clinic`, `case_entry_date`, `case_diagnosis`, `fkey_case_submitter`) VALUES (5, 'H121-17', 1, '2018-01-03', 'cHL (NS)', 2);
INSERT INTO `linfo_open_db`.`clinic_case` (`idcase`, `case_number`, `fkey_case_clinic`, `case_entry_date`, `case_diagnosis`, `fkey_case_submitter`) VALUES (6, 'H979-17', 1, '2018-03-08', 'cHL (MC)', 3);
INSERT INTO `linfo_open_db`.`clinic_case` (`idcase`, `case_number`, `fkey_case_clinic`, `case_entry_date`, `case_diagnosis`, `fkey_case_submitter`) VALUES (7, 'K385-17', 2, '2018-07-30', 'Lymphadenitis', 3);
INSERT INTO `linfo_open_db`.`clinic_case` (`idcase`, `case_number`, `fkey_case_clinic`, `case_entry_date`, `case_diagnosis`, `fkey_case_submitter`) VALUES (8, 'K3543-19', 2, '2019-09-11', 'cHL (MC)', 2);
INSERT INTO `linfo_open_db`.`clinic_case` (`idcase`, `case_number`, `fkey_case_clinic`, `case_entry_date`, `case_diagnosis`, `fkey_case_submitter`) VALUES (9, 'K322-18', 2, '2019-12-04', 'cHL (NS)', 2);
INSERT INTO `linfo_open_db`.`clinic_case` (`idcase`, `case_number`, `fkey_case_clinic`, `case_entry_date`, `case_diagnosis`, `fkey_case_submitter`) VALUES (10, 'H1001-18', 2, '2020-01-23', 'cHL (NS)', 3);


#CONTACT_PERSON
INSERT INTO `linfo_open_db`.`contact_person` (`idcontact_person`, `title`, `forename`, `surname`, `email`, `phone`) VALUES (1, 'Dr.', 'Johann', 'Johanson', 'jomail@nomail.com', '93459 93458');
INSERT INTO `linfo_open_db`.`contact_person` (`idcontact_person`, `title`, `forename`, `surname`, `email`, `phone`) VALUES (2, 'Prof. Dr.', 'Gregor', 'Gregowitz', 'grewitz@nomail.com', '854 85834');
INSERT INTO `linfo_open_db`.`contact_person` (`idcontact_person`, `title`, `forename`, `surname`, `email`, `phone`) VALUES (3, 'Dr.', 'Frauke', 'Kunze', 'fraunze@nomail.com', '949 34222');
INSERT INTO `linfo_open_db`.`contact_person` (`idcontact_person`, `title`, `forename`, `surname`, `email`, `phone`) VALUES (4, 'Herr', 'Martin', 'Müller', 'martini@fakemail.de', '85 3424254');
INSERT INTO `linfo_open_db`.`contact_person` (`idcontact_person`, `title`, `forename`, `surname`, `email`, `phone`) VALUES (5, 'Frau', 'Ulrike', 'Meier', 'ulme@fakemail.de', '+40 169 348919');
INSERT INTO `linfo_open_db`.`contact_person` (`idcontact_person`, `title`, `forename`, `surname`, `email`, `phone`) VALUES (6, 'Frau', 'Pia', 'Schulze', 'schulze@mustermail.org', '+49 170 238848');

# METADATA VALUES
# standard values are set in initialize script

#SERVICE DEFINITIONS
# standard service definitions are set in initialize script

#SERVICE METADATA DEFAULTS
# service definition defaults are set in initialize script

# SERVICE
INSERT INTO `linfo_open_db`.`service` (`idservice`, `fkey_case`, `fkey_service_definition`) VALUES (1, 1, 3);
INSERT INTO `linfo_open_db`.`service` (`idservice`, `fkey_case`, `fkey_service_definition`) VALUES (2, 1, 4);
INSERT INTO `linfo_open_db`.`service` (`idservice`, `fkey_case`, `fkey_service_definition`) VALUES (3, 2, 3);
INSERT INTO `linfo_open_db`.`service` (`idservice`, `fkey_case`, `fkey_service_definition`) VALUES (4, 2, 5);

# CLINIC_CONTACTS
INSERT INTO `linfo_open_db`.`contact_for_clinic` (`fkey_cfc_clinic`, `fkey_cfc_contact`, `notes`) VALUES (1, 3, 'Allgemeiner Ansprechpartner und so');
INSERT INTO `linfo_open_db`.`contact_for_clinic` (`fkey_cfc_clinic`, `fkey_cfc_contact`, `notes`) VALUES (2, 1, 'Nur Putzangestellte/r hat keine Ahnung von nix');

#METADATA STRING
INSERT INTO `linfo_open_db`.`metadata_string` (`fkey_service`, `meta_key`, `meta_value`) VALUES (1, 'Capture date', '2020-01-01 15:00 MEZ');
INSERT INTO `linfo_open_db`.`metadata_string` (`fkey_service`, `meta_key`, `meta_value`) VALUES (2, 'Capture date', '2020-01-05 15:23 MEZ');
INSERT INTO `linfo_open_db`.`metadata_string` (`fkey_service`, `meta_key`, `meta_value`) VALUES (3, 'Capture date', '2020-02-14 15:10 MEZ');
INSERT INTO `linfo_open_db`.`metadata_string` (`fkey_service`, `meta_key`, `meta_value`) VALUES (4, 'Capture date', '2020-03-08 15:45 MEZ');


#METADATA TEXT
INSERT INTO `linfo_open_db`.`metadata_text` (`fkey_service`, `meta_key`, `meta_value`) VALUES (1, 'Comment', 'Testservice-1');
INSERT INTO `linfo_open_db`.`metadata_text` (`fkey_service`, `meta_key`, `meta_value`) VALUES (2, 'Comment', 'Testservice-2');
INSERT INTO `linfo_open_db`.`metadata_text` (`fkey_service`, `meta_key`, `meta_value`) VALUES (3, 'Comment', 'Testservice-3');
INSERT INTO `linfo_open_db`.`metadata_text` (`fkey_service`, `meta_key`, `meta_value`) VALUES (4, 'Comment', 'Testservice-4');
INSERT INTO `linfo_open_db`.`metadata_text` (`fkey_service`, `meta_key`, `meta_value`) VALUES (1, 'Blue', 'hematoxilin');
INSERT INTO `linfo_open_db`.`metadata_text` (`fkey_service`, `meta_key`, `meta_value`) VALUES (2, 'Blue', 'DAPI');
INSERT INTO `linfo_open_db`.`metadata_text` (`fkey_service`, `meta_key`, `meta_value`) VALUES (3, 'Blue', 'hematoxilin');
INSERT INTO `linfo_open_db`.`metadata_text` (`fkey_service`, `meta_key`, `meta_value`) VALUES (4, 'Blue', 'DAPI');
INSERT INTO `linfo_open_db`.`metadata_text` (`fkey_service`, `meta_key`, `meta_value`) VALUES (1, 'Red', 'CD30');
INSERT INTO `linfo_open_db`.`metadata_text` (`fkey_service`, `meta_key`, `meta_value`) VALUES (2, 'Red', 'CD4');
INSERT INTO `linfo_open_db`.`metadata_text` (`fkey_service`, `meta_key`, `meta_value`) VALUES (3, 'Red', 'CD20');
INSERT INTO `linfo_open_db`.`metadata_text` (`fkey_service`, `meta_key`, `meta_value`) VALUES (4, 'Red', 'Actin');
INSERT INTO `linfo_open_db`.`metadata_text` (`fkey_service`, `meta_key`, `meta_value`) VALUES (4, 'Green', 'CD20');


#METADATA INT
INSERT INTO `linfo_open_db`.`metadata_int` (`fkey_service`, `meta_key`, `meta_value`) VALUES (1, 'Block', 1);
INSERT INTO `linfo_open_db`.`metadata_int` (`fkey_service`, `meta_key`, `meta_value`) VALUES (3, 'Block', 1);

INSERT INTO `linfo_open_db`.`metadata_int` (`fkey_service`, `meta_key`, `meta_value`) VALUES (1, 'Rating', 4);
INSERT INTO `linfo_open_db`.`metadata_int` (`fkey_service`, `meta_key`, `meta_value`) VALUES (2, 'Rating', 5);
INSERT INTO `linfo_open_db`.`metadata_int` (`fkey_service`, `meta_key`, `meta_value`) VALUES (3, 'Rating', 3);
INSERT INTO `linfo_open_db`.`metadata_int` (`fkey_service`, `meta_key`, `meta_value`) VALUES (4, 'Rating', 4);
#METADATA DOUBLE
INSERT INTO `linfo_open_db`.`metadata_double` (`fkey_service`, `meta_key`, `meta_value`) VALUES (1, 'Fraction', 0.25);
INSERT INTO `linfo_open_db`.`metadata_double` (`fkey_service`, `meta_key`, `meta_value`) VALUES (3, 'Fraction', 0.75);
