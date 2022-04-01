-- First Please create a ROLE named 'sao' in psql, a database named 'sao',
-- and GRANT ALL PRIVILEGES of the database to the role. e.g. for linux :
--
-- sudo su - postgres
-- psql
-- CREATE USER sao WITH PASSWORD 'sao';
-- CREATE DATABASE sao OWNER sao;
-- GRANT ALL PRIVILEGES ON DATABASE sao to sao;
-- \q
--
-- Then execute the following SQL on the public scheme of the sao database.


CREATE TABLE sao.public.account (
                                id uuid NOT NULL,
                                name character varying(32) NOT NULL,
                                password character varying(64) NOT NULL,
                                email character varying(355) NOT NULL,
                                created_on timestamp without time zone NOT NULL
);


CREATE TABLE sao.public.account_role (
                                     account_id uuid NOT NULL,
                                     role_id integer NOT NULL
);


CREATE TABLE sao.public.optimize_algorithm (
                                           id integer NOT NULL,
                                           name character varying(32) NOT NULL,
                                           description character varying(64)
);


CREATE TABLE sao.public.role (
                             id integer NOT NULL,
                             role character varying(32) NOT NULL
);


CREATE TABLE sao.public.scenario_analysis_result (
                                                 id uuid NOT NULL,
                                                 result double precision,
                                                 url character varying(255) NOT NULL
);



CREATE TABLE sao.public.scenario_config_strategy (
                                                 id integer NOT NULL,
                                                 name character varying(32) NOT NULL,
                                                 description character varying(64)
);



CREATE TABLE sao.public.scenario_record (
                                        id uuid NOT NULL,
                                        created_on timestamp without time zone NOT NULL,
                                        account_id uuid NOT NULL,
                                        scenario_analysis_result_id uuid NOT NULL,
                                        scenario_config_strategy_id integer NOT NULL,
                                        scenario_unit_delineation_id integer NOT NULL,
                                        optimize_algorithm_id integer NOT NULL,
                                        title character varying(255)
);


CREATE TABLE sao.public.scenario_unit_delineation (
                                                  id integer NOT NULL,
                                                  name character varying(32) NOT NULL,
                                                  description character varying(64)
);


CREATE VIEW sao.public.v_account_role AS
SELECT account.name,
       account.password,
       account.email,
       account.created_on,
       role.role,
       account_role.role_id,
       account_role.account_id
FROM ((sao.public.account
    JOIN sao.public.account_role ON ((account_role.account_id = account.id)))
         JOIN sao.public.role ON ((account_role.role_id = role.id)));


CREATE VIEW sao.public.v_records AS
SELECT scenario_analysis_result.result,
       scenario_record.id AS scenario_record_id,
       scenario_record.account_id,
       optimize_algorithm.name AS optimize_algorithm,
       scenario_unit_delineation.name AS scenario_unit_delineation,
       scenario_config_strategy.name AS scenario_config_strategy,
       scenario_record.scenario_analysis_result_id,
       scenario_record.created_on,
       scenario_record.title
FROM ((((sao.public.scenario_analysis_result
    JOIN sao.public.scenario_record ON ((scenario_record.scenario_analysis_result_id = scenario_analysis_result.id)))
    JOIN sao.public.scenario_config_strategy ON ((scenario_record.scenario_config_strategy_id = scenario_config_strategy.id)))
    JOIN sao.public.scenario_unit_delineation ON ((scenario_record.scenario_unit_delineation_id = scenario_unit_delineation.id)))
         JOIN sao.public.optimize_algorithm ON ((scenario_record.optimize_algorithm_id = optimize_algorithm.id)));


INSERT INTO sao.public.account VALUES ('2a2d99ee-150c-468b-a53c-3b188121d5d6', 'test', '123', '123@1.com', '2019-03-13 14:28:32.913');


INSERT INTO sao.public.account_role VALUES ('2a2d99ee-150c-468b-a53c-3b188121d5d6', 2);


INSERT INTO sao.public.optimize_algorithm VALUES (1, 'NONE', 'no algorithm');
INSERT INTO sao.public.optimize_algorithm VALUES (2, 'NSGA2', 'Non-dominated Sorting Genetic Algorithm');


INSERT INTO sao.public.role VALUES (1, 'admin');
INSERT INTO sao.public.role VALUES (2, 'user');


INSERT INTO sao.public.scenario_config_strategy VALUES (1, 'RAND', 'Random');
INSERT INTO sao.public.scenario_config_strategy VALUES (2, 'SUIT', 'Suit');
INSERT INTO sao.public.scenario_config_strategy VALUES (3, 'UPDOWN', 'Up-Down');
INSERT INTO sao.public.scenario_config_strategy VALUES (4, 'HILLSLP', 'Hillslope');


INSERT INTO sao.public.scenario_unit_delineation VALUES (1, 'HRU', 'HRU');
INSERT INTO sao.public.scenario_unit_delineation VALUES (2, 'EXPLICITHRU', 'Explicit HRU');
INSERT INTO sao.public.scenario_unit_delineation VALUES (3, 'CONNFIELD', 'Connected Field');
INSERT INTO sao.public.scenario_unit_delineation VALUES (4, 'SLPPOS', 'Slope Position');


ALTER TABLE ONLY sao.public.account
    ADD CONSTRAINT account_email_key UNIQUE (email);


ALTER TABLE ONLY sao.public.account
    ADD CONSTRAINT account_name_key UNIQUE (name);


ALTER TABLE ONLY sao.public.account
    ADD CONSTRAINT account_pkey PRIMARY KEY (id);


ALTER TABLE ONLY sao.public.optimize_algorithm
    ADD CONSTRAINT optimize_algorithm_pkey PRIMARY KEY (id);


ALTER TABLE ONLY sao.public.role
    ADD CONSTRAINT role_pkey PRIMARY KEY (id);


ALTER TABLE ONLY sao.public.scenario_analysis_result
    ADD CONSTRAINT scenario_analysis_result_pkey PRIMARY KEY (id);


ALTER TABLE ONLY sao.public.scenario_config_strategy
    ADD CONSTRAINT scenario_config_strategy_pkey PRIMARY KEY (id);


ALTER TABLE ONLY sao.public.scenario_record
    ADD CONSTRAINT scenario_record_pkey PRIMARY KEY (id);


ALTER TABLE ONLY sao.public.scenario_unit_delineation
    ADD CONSTRAINT scenario_unit_delineation_pkey PRIMARY KEY (id);


ALTER TABLE ONLY sao.public.account_role
    ADD CONSTRAINT account FOREIGN KEY (account_id) REFERENCES sao.public.account(id);


ALTER TABLE ONLY sao.public.account_role
    ADD CONSTRAINT role FOREIGN KEY (role_id) REFERENCES sao.public.role(id);


ALTER TABLE ONLY sao.public.scenario_record
    ADD CONSTRAINT scenario_record_account_id_fkey FOREIGN KEY (account_id) REFERENCES sao.public.account(id);



ALTER TABLE ONLY sao.public.scenario_record
    ADD CONSTRAINT scenario_record_optimize_algorithm_id_fkey FOREIGN KEY (optimize_algorithm_id) REFERENCES sao.public.optimize_algorithm(id);


ALTER TABLE ONLY sao.public.scenario_record
    ADD CONSTRAINT scenario_record_scenario_analysis_result_id_fkey FOREIGN KEY (scenario_analysis_result_id) REFERENCES sao.public.scenario_analysis_result(id) ON DELETE CASCADE;


ALTER TABLE ONLY sao.public.scenario_record
    ADD CONSTRAINT scenario_record_scenario_config_strategy_id_fkey FOREIGN KEY (scenario_config_strategy_id) REFERENCES sao.public.scenario_config_strategy(id);


ALTER TABLE ONLY sao.public.scenario_record
    ADD CONSTRAINT scenario_record_scenario_unit_delineation_id_fkey FOREIGN KEY (scenario_unit_delineation_id) REFERENCES sao.public.scenario_unit_delineation(id);
