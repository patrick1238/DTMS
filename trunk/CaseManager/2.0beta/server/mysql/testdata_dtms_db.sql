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
INSERT INTO `linfo_open_db`.`clinic_case` (`idcase`, `case_number`, `fkey_case_clinic`, `case_entry_date`, `case_diagnosis`, `fkey_case_submitter`) VALUES (10, 'H1001-18', 2, '2020-01-22', 'cHL (NS)', 3);
INSERT INTO `linfo_open_db`.`clinic_case` (`idcase`, `case_number`, `fkey_case_clinic`, `case_entry_date`, `case_diagnosis`, `fkey_case_submitter`) VALUES (11, 'L501-18', 2, '2020-06-22', 'cHL (NS)', 3);

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
INSERT INTO `linfo_open_db`.`service` (`idservice`, `fkey_case`, `fkey_service_definition`) VALUES (2, 1, 3);
INSERT INTO `linfo_open_db`.`service` (`idservice`, `fkey_case`, `fkey_service_definition`) VALUES (3, 1, 4);
INSERT INTO `linfo_open_db`.`service` (`idservice`, `fkey_case`, `fkey_service_definition`) VALUES (4, 1, 4);
INSERT INTO `linfo_open_db`.`service` (`idservice`, `fkey_case`, `fkey_service_definition`) VALUES (5, 2, 3);
INSERT INTO `linfo_open_db`.`service` (`idservice`, `fkey_case`, `fkey_service_definition`) VALUES (6, 2, 3);
INSERT INTO `linfo_open_db`.`service` (`idservice`, `fkey_case`, `fkey_service_definition`) VALUES (7, 2, 3);
INSERT INTO `linfo_open_db`.`service` (`idservice`, `fkey_case`, `fkey_service_definition`) VALUES (8, 2, 4);
INSERT INTO `linfo_open_db`.`service` (`idservice`, `fkey_case`, `fkey_service_definition`) VALUES (9, 2, 5);
INSERT INTO `linfo_open_db`.`service` (`idservice`, `fkey_case`, `fkey_service_definition`) VALUES (10, 2, 5);
INSERT INTO `linfo_open_db`.`service` (`idservice`, `fkey_case`, `fkey_service_definition`) VALUES (11, 3, 3);
INSERT INTO `linfo_open_db`.`service` (`idservice`, `fkey_case`, `fkey_service_definition`) VALUES (12, 3, 4);
INSERT INTO `linfo_open_db`.`service` (`idservice`, `fkey_case`, `fkey_service_definition`) VALUES (13, 3, 5);
INSERT INTO `linfo_open_db`.`service` (`idservice`, `fkey_case`, `fkey_service_definition`) VALUES (14, 4, 3);
INSERT INTO `linfo_open_db`.`service` (`idservice`, `fkey_case`, `fkey_service_definition`) VALUES (15, 4, 3);
INSERT INTO `linfo_open_db`.`service` (`idservice`, `fkey_case`, `fkey_service_definition`) VALUES (16, 4, 4);
INSERT INTO `linfo_open_db`.`service` (`idservice`, `fkey_case`, `fkey_service_definition`) VALUES (17, 4, 4);
INSERT INTO `linfo_open_db`.`service` (`idservice`, `fkey_case`, `fkey_service_definition`) VALUES (18, 5, 3);
INSERT INTO `linfo_open_db`.`service` (`idservice`, `fkey_case`, `fkey_service_definition`) VALUES (19, 5, 3);

# CLINIC_CONTACTS
INSERT INTO `linfo_open_db`.`contact_for_clinic` (`fkey_cfc_clinic`, `fkey_cfc_contact`, `notes`) VALUES (1, 3, 'Allgemeiner Ansprechpartner und so');
INSERT INTO `linfo_open_db`.`contact_for_clinic` (`fkey_cfc_clinic`, `fkey_cfc_contact`, `notes`) VALUES (2, 1, 'Nur Putzangestellte/r hat keine Ahnung von nix');

#METADATA STRING
INSERT INTO `linfo_open_db`.`metadata_string` (`fkey_service`, `meta_key`, `meta_value`) VALUES (1, 'Capture date', '01.01.2020 15:00 MEZ');
INSERT INTO `linfo_open_db`.`metadata_string` (`fkey_service`, `meta_key`, `meta_value`) VALUES (2, 'Capture date', '05.01.2020 15:23 MEZ');
INSERT INTO `linfo_open_db`.`metadata_string` (`fkey_service`, `meta_key`, `meta_value`) VALUES (3, 'Capture date', '14.02.2020 15:10 MEZ');
INSERT INTO `linfo_open_db`.`metadata_string` (`fkey_service`, `meta_key`, `meta_value`) VALUES (4, 'Capture date', '08.03.2020 15:45 MEZ');
INSERT INTO `linfo_open_db`.`metadata_string` (`fkey_service`, `meta_key`, `meta_value`) VALUES (5, 'Capture date', '07.03.2020 15:00 MEZ');
INSERT INTO `linfo_open_db`.`metadata_string` (`fkey_service`, `meta_key`, `meta_value`) VALUES (6, 'Capture date', '07.06.2020 15:23 MEZ');
INSERT INTO `linfo_open_db`.`metadata_string` (`fkey_service`, `meta_key`, `meta_value`) VALUES (7, 'Capture date', '07.06.2020 15:10 MEZ');
INSERT INTO `linfo_open_db`.`metadata_string` (`fkey_service`, `meta_key`, `meta_value`) VALUES (8, 'Capture date', '07.06.2020 15:45 MEZ');
INSERT INTO `linfo_open_db`.`metadata_string` (`fkey_service`, `meta_key`, `meta_value`) VALUES (9, 'Capture date', '08.07.2020 15:00 MEZ');
INSERT INTO `linfo_open_db`.`metadata_string` (`fkey_service`, `meta_key`, `meta_value`) VALUES (10, 'Capture date', '09.07.20209 15:23 MEZ');
INSERT INTO `linfo_open_db`.`metadata_string` (`fkey_service`, `meta_key`, `meta_value`) VALUES (11, 'Capture date', '09.07.2020 15:10 MEZ');
INSERT INTO `linfo_open_db`.`metadata_string` (`fkey_service`, `meta_key`, `meta_value`) VALUES (12, 'Capture date', '09.07.2020 15:45 MEZ');
INSERT INTO `linfo_open_db`.`metadata_string` (`fkey_service`, `meta_key`, `meta_value`) VALUES (13, 'Capture date', '09.07.2020 15:00 MEZ');
INSERT INTO `linfo_open_db`.`metadata_string` (`fkey_service`, `meta_key`, `meta_value`) VALUES (14, 'Capture date', '09.08.2020 15:23 MEZ');
INSERT INTO `linfo_open_db`.`metadata_string` (`fkey_service`, `meta_key`, `meta_value`) VALUES (15, 'Capture date', '10.08.2020 15:10 MEZ');
INSERT INTO `linfo_open_db`.`metadata_string` (`fkey_service`, `meta_key`, `meta_value`) VALUES (16, 'Capture date', '10.08.2020 15:45 MEZ');
INSERT INTO `linfo_open_db`.`metadata_string` (`fkey_service`, `meta_key`, `meta_value`) VALUES (17, 'Capture date', '10.09.2020 15:00 MEZ');
INSERT INTO `linfo_open_db`.`metadata_string` (`fkey_service`, `meta_key`, `meta_value`) VALUES (18, 'Capture date', '11.09.2020 15:23 MEZ');
INSERT INTO `linfo_open_db`.`metadata_string` (`fkey_service`, `meta_key`, `meta_value`) VALUES (19, 'Capture date', '14.09.2020 15:10 MEZ');


#METADATA TEXT
INSERT INTO `linfo_open_db`.`metadata_text` (`fkey_service`, `meta_key`, `meta_value`) VALUES (1, 'Comment', 'Testservice-1');
INSERT INTO `linfo_open_db`.`metadata_text` (`fkey_service`, `meta_key`, `meta_value`) VALUES (2, 'Comment', 'Testservice-2');
INSERT INTO `linfo_open_db`.`metadata_text` (`fkey_service`, `meta_key`, `meta_value`) VALUES (3, 'Comment', 'Testservice-3');
INSERT INTO `linfo_open_db`.`metadata_text` (`fkey_service`, `meta_key`, `meta_value`) VALUES (4, 'Comment', 'Testservice-4');
INSERT INTO `linfo_open_db`.`metadata_text` (`fkey_service`, `meta_key`, `meta_value`) VALUES (5, 'Comment', 'Testservice-1b');
INSERT INTO `linfo_open_db`.`metadata_text` (`fkey_service`, `meta_key`, `meta_value`) VALUES (6, 'Comment', 'Testservice-2b');
INSERT INTO `linfo_open_db`.`metadata_text` (`fkey_service`, `meta_key`, `meta_value`) VALUES (7, 'Comment', 'Testservice-3b');
INSERT INTO `linfo_open_db`.`metadata_text` (`fkey_service`, `meta_key`, `meta_value`) VALUES (8, 'Comment', 'Testservice-4b');
INSERT INTO `linfo_open_db`.`metadata_text` (`fkey_service`, `meta_key`, `meta_value`) VALUES (9, 'Comment', 'Testservice-1v');
INSERT INTO `linfo_open_db`.`metadata_text` (`fkey_service`, `meta_key`, `meta_value`) VALUES (10, 'Comment', 'Testservice-2v');
INSERT INTO `linfo_open_db`.`metadata_text` (`fkey_service`, `meta_key`, `meta_value`) VALUES (11, 'Comment', 'Testservice-3v');
INSERT INTO `linfo_open_db`.`metadata_text` (`fkey_service`, `meta_key`, `meta_value`) VALUES (12, 'Comment', 'Testservice-4v');
INSERT INTO `linfo_open_db`.`metadata_text` (`fkey_service`, `meta_key`, `meta_value`) VALUES (13, 'Comment', 'Testservice-1c');
INSERT INTO `linfo_open_db`.`metadata_text` (`fkey_service`, `meta_key`, `meta_value`) VALUES (14, 'Comment', 'Testservice-2c');
INSERT INTO `linfo_open_db`.`metadata_text` (`fkey_service`, `meta_key`, `meta_value`) VALUES (15, 'Comment', 'Testservice-3c');
INSERT INTO `linfo_open_db`.`metadata_text` (`fkey_service`, `meta_key`, `meta_value`) VALUES (16, 'Comment', 'Testservice-4c');
INSERT INTO `linfo_open_db`.`metadata_text` (`fkey_service`, `meta_key`, `meta_value`) VALUES (17, 'Comment', 'Testservice-1k');
INSERT INTO `linfo_open_db`.`metadata_text` (`fkey_service`, `meta_key`, `meta_value`) VALUES (18, 'Comment', 'Testservice-2k');
INSERT INTO `linfo_open_db`.`metadata_text` (`fkey_service`, `meta_key`, `meta_value`) VALUES (19, 'Comment', 'Testservice-3k');

INSERT INTO `linfo_open_db`.`metadata_text` (`fkey_service`, `meta_key`, `meta_value`) VALUES (1, 'Blue', 'hematoxilin');
INSERT INTO `linfo_open_db`.`metadata_text` (`fkey_service`, `meta_key`, `meta_value`) VALUES (2, 'Blue', 'hematoxilin');
INSERT INTO `linfo_open_db`.`metadata_text` (`fkey_service`, `meta_key`, `meta_value`) VALUES (3, 'Blue', 'DAPI');
INSERT INTO `linfo_open_db`.`metadata_text` (`fkey_service`, `meta_key`, `meta_value`) VALUES (4, 'Blue', 'DAPI');
INSERT INTO `linfo_open_db`.`metadata_text` (`fkey_service`, `meta_key`, `meta_value`) VALUES (5, 'Blue', 'hematoxilin');
INSERT INTO `linfo_open_db`.`metadata_text` (`fkey_service`, `meta_key`, `meta_value`) VALUES (6, 'Blue', 'hematoxilin');
INSERT INTO `linfo_open_db`.`metadata_text` (`fkey_service`, `meta_key`, `meta_value`) VALUES (7, 'Blue', 'hematoxilin');
INSERT INTO `linfo_open_db`.`metadata_text` (`fkey_service`, `meta_key`, `meta_value`) VALUES (8, 'Blue', 'DAPI');
INSERT INTO `linfo_open_db`.`metadata_text` (`fkey_service`, `meta_key`, `meta_value`) VALUES (9, 'Blue', 'DAPI');
INSERT INTO `linfo_open_db`.`metadata_text` (`fkey_service`, `meta_key`, `meta_value`) VALUES (10, 'Blue', 'DAPI');
INSERT INTO `linfo_open_db`.`metadata_text` (`fkey_service`, `meta_key`, `meta_value`) VALUES (11, 'Blue', 'hematoxilin');
INSERT INTO `linfo_open_db`.`metadata_text` (`fkey_service`, `meta_key`, `meta_value`) VALUES (12, 'Blue', 'DAPI');
INSERT INTO `linfo_open_db`.`metadata_text` (`fkey_service`, `meta_key`, `meta_value`) VALUES (13, 'Blue', 'DAPI');
INSERT INTO `linfo_open_db`.`metadata_text` (`fkey_service`, `meta_key`, `meta_value`) VALUES (14, 'Blue', 'DAPI');
INSERT INTO `linfo_open_db`.`metadata_text` (`fkey_service`, `meta_key`, `meta_value`) VALUES (15, 'Blue', 'hematoxilin');
INSERT INTO `linfo_open_db`.`metadata_text` (`fkey_service`, `meta_key`, `meta_value`) VALUES (16, 'Blue', 'hematoxilin');
INSERT INTO `linfo_open_db`.`metadata_text` (`fkey_service`, `meta_key`, `meta_value`) VALUES (17, 'Blue', 'DAPI');
INSERT INTO `linfo_open_db`.`metadata_text` (`fkey_service`, `meta_key`, `meta_value`) VALUES (18, 'Blue', 'hematoxilin');
INSERT INTO `linfo_open_db`.`metadata_text` (`fkey_service`, `meta_key`, `meta_value`) VALUES (19, 'Blue', 'hematoxilin');

INSERT INTO `linfo_open_db`.`metadata_text` (`fkey_service`, `meta_key`, `meta_value`) VALUES (1, 'Red', 'CD30');
INSERT INTO `linfo_open_db`.`metadata_text` (`fkey_service`, `meta_key`, `meta_value`) VALUES (2, 'Red', 'CD4');
INSERT INTO `linfo_open_db`.`metadata_text` (`fkey_service`, `meta_key`, `meta_value`) VALUES (3, 'Red', 'CD20');
INSERT INTO `linfo_open_db`.`metadata_text` (`fkey_service`, `meta_key`, `meta_value`) VALUES (4, 'Red', 'Actin');
INSERT INTO `linfo_open_db`.`metadata_text` (`fkey_service`, `meta_key`, `meta_value`) VALUES (5, 'Red', 'CD34');
INSERT INTO `linfo_open_db`.`metadata_text` (`fkey_service`, `meta_key`, `meta_value`) VALUES (6, 'Red', 'CD4');
INSERT INTO `linfo_open_db`.`metadata_text` (`fkey_service`, `meta_key`, `meta_value`) VALUES (7, 'Red', 'CD20');
INSERT INTO `linfo_open_db`.`metadata_text` (`fkey_service`, `meta_key`, `meta_value`) VALUES (8, 'Red', 'Actin');
INSERT INTO `linfo_open_db`.`metadata_text` (`fkey_service`, `meta_key`, `meta_value`) VALUES (9, 'Red', 'CD8');
INSERT INTO `linfo_open_db`.`metadata_text` (`fkey_service`, `meta_key`, `meta_value`) VALUES (10, 'Red', 'CD4');
INSERT INTO `linfo_open_db`.`metadata_text` (`fkey_service`, `meta_key`, `meta_value`) VALUES (11, 'Red', 'CD20');
INSERT INTO `linfo_open_db`.`metadata_text` (`fkey_service`, `meta_key`, `meta_value`) VALUES (12, 'Red', 'Actin');
INSERT INTO `linfo_open_db`.`metadata_text` (`fkey_service`, `meta_key`, `meta_value`) VALUES (13, 'Red', 'CD30');
INSERT INTO `linfo_open_db`.`metadata_text` (`fkey_service`, `meta_key`, `meta_value`) VALUES (14, 'Red', 'CD4');
INSERT INTO `linfo_open_db`.`metadata_text` (`fkey_service`, `meta_key`, `meta_value`) VALUES (15, 'Red', 'CD20');
INSERT INTO `linfo_open_db`.`metadata_text` (`fkey_service`, `meta_key`, `meta_value`) VALUES (16, 'Red', 'Actin');
INSERT INTO `linfo_open_db`.`metadata_text` (`fkey_service`, `meta_key`, `meta_value`) VALUES (17, 'Red', 'CD30');
INSERT INTO `linfo_open_db`.`metadata_text` (`fkey_service`, `meta_key`, `meta_value`) VALUES (18, 'Red', 'CD34');
INSERT INTO `linfo_open_db`.`metadata_text` (`fkey_service`, `meta_key`, `meta_value`) VALUES (19, 'Red', 'CD168');

INSERT INTO `linfo_open_db`.`metadata_text` (`fkey_service`, `meta_key`, `meta_value`) VALUES (4, 'Green', 'CD168');
INSERT INTO `linfo_open_db`.`metadata_text` (`fkey_service`, `meta_key`, `meta_value`) VALUES (8, 'Green', 'Giemsa');
INSERT INTO `linfo_open_db`.`metadata_text` (`fkey_service`, `meta_key`, `meta_value`) VALUES (9, 'Green', 'Actin');
INSERT INTO `linfo_open_db`.`metadata_text` (`fkey_service`, `meta_key`, `meta_value`) VALUES (12, 'Green', 'Actin');
#METADATA INT
INSERT INTO `linfo_open_db`.`metadata_int` (`fkey_service`, `meta_key`, `meta_value`) VALUES (1, 'Block', 1);
INSERT INTO `linfo_open_db`.`metadata_int` (`fkey_service`, `meta_key`, `meta_value`) VALUES (3, 'Block', 1);
INSERT INTO `linfo_open_db`.`metadata_int` (`fkey_service`, `meta_key`, `meta_value`) VALUES (4, 'Block', 1);
INSERT INTO `linfo_open_db`.`metadata_int` (`fkey_service`, `meta_key`, `meta_value`) VALUES (5, 'Block', 1);
INSERT INTO `linfo_open_db`.`metadata_int` (`fkey_service`, `meta_key`, `meta_value`) VALUES (6, 'Block', 1);
INSERT INTO `linfo_open_db`.`metadata_int` (`fkey_service`, `meta_key`, `meta_value`) VALUES (9, 'Block', 1);
INSERT INTO `linfo_open_db`.`metadata_int` (`fkey_service`, `meta_key`, `meta_value`) VALUES (11, 'Block', 1);
INSERT INTO `linfo_open_db`.`metadata_int` (`fkey_service`, `meta_key`, `meta_value`) VALUES (12, 'Block', 1);

INSERT INTO `linfo_open_db`.`metadata_int` (`fkey_service`, `meta_key`, `meta_value`) VALUES (1, 'Rating', 4);
INSERT INTO `linfo_open_db`.`metadata_int` (`fkey_service`, `meta_key`, `meta_value`) VALUES (2, 'Rating', 5);
INSERT INTO `linfo_open_db`.`metadata_int` (`fkey_service`, `meta_key`, `meta_value`) VALUES (3, 'Rating', 3);
INSERT INTO `linfo_open_db`.`metadata_int` (`fkey_service`, `meta_key`, `meta_value`) VALUES (4, 'Rating', 4);
INSERT INTO `linfo_open_db`.`metadata_int` (`fkey_service`, `meta_key`, `meta_value`) VALUES (5, 'Rating', 2);
INSERT INTO `linfo_open_db`.`metadata_int` (`fkey_service`, `meta_key`, `meta_value`) VALUES (6, 'Rating', 2);
INSERT INTO `linfo_open_db`.`metadata_int` (`fkey_service`, `meta_key`, `meta_value`) VALUES (7, 'Rating', 3);
INSERT INTO `linfo_open_db`.`metadata_int` (`fkey_service`, `meta_key`, `meta_value`) VALUES (8, 'Rating', 4);
INSERT INTO `linfo_open_db`.`metadata_int` (`fkey_service`, `meta_key`, `meta_value`) VALUES (9, 'Rating', 4);
INSERT INTO `linfo_open_db`.`metadata_int` (`fkey_service`, `meta_key`, `meta_value`) VALUES (10, 'Rating', 5);
INSERT INTO `linfo_open_db`.`metadata_int` (`fkey_service`, `meta_key`, `meta_value`) VALUES (11, 'Rating', 1);
INSERT INTO `linfo_open_db`.`metadata_int` (`fkey_service`, `meta_key`, `meta_value`) VALUES (12, 'Rating', 4);
INSERT INTO `linfo_open_db`.`metadata_int` (`fkey_service`, `meta_key`, `meta_value`) VALUES (13, 'Rating', 2);
INSERT INTO `linfo_open_db`.`metadata_int` (`fkey_service`, `meta_key`, `meta_value`) VALUES (14, 'Rating', 5);
INSERT INTO `linfo_open_db`.`metadata_int` (`fkey_service`, `meta_key`, `meta_value`) VALUES (15, 'Rating', 3);
INSERT INTO `linfo_open_db`.`metadata_int` (`fkey_service`, `meta_key`, `meta_value`) VALUES (16, 'Rating', 4);
INSERT INTO `linfo_open_db`.`metadata_int` (`fkey_service`, `meta_key`, `meta_value`) VALUES (19, 'Rating', 5);

#METADATA DOUBLE
INSERT INTO `linfo_open_db`.`metadata_double` (`fkey_service`, `meta_key`, `meta_value`) VALUES (1, 'Fraction', 0.25);
INSERT INTO `linfo_open_db`.`metadata_double` (`fkey_service`, `meta_key`, `meta_value`) VALUES (3, 'Fraction', 0.75);
