--
-- utenti.sql
-- Script di popolamento della tabella Utenti con il set iniziale di utenze
-- pre-registrate richiesto dalle specifiche di progetto (pag. 6):
--   "N.B. La tabella dovrà già contenere 2 proiezionisti e 5 bigliettai"
--
-- Da eseguire DOPO creazione_db.sql
-- L'ordine rispetto a films.sql non è rilevante (tabelle indipendenti).
--

-- ============================================================
-- PROIEZIONISTI (2)
-- ============================================================

-- Username: mbianchi | Password in chiaro: Proiezionista#1
INSERT INTO public."Utenti" (nome, cognome, username, password, data_nascita, domicilio, ruolo)
VALUES ('Marco', 'Bianchi', 'mbianchi', md5('Proiezionista#1'), '1985-03-14', 'Varese', 'proiezionista');

-- Username: gferrari | Password in chiaro: Proiezionista#2
INSERT INTO public."Utenti" (nome, cognome, username, password, data_nascita, domicilio, ruolo)
VALUES ('Giulia', 'Ferrari', 'gferrari', md5('Proiezionista#2'), '1990-07-22', 'Como', 'proiezionista');

-- ============================================================
-- BIGLIETTAI (5)
-- ============================================================

-- Username: lromano | Password in chiaro: Bigliettaio#1
INSERT INTO public."Utenti" (nome, cognome, username, password, data_nascita, domicilio, ruolo)
VALUES ('Luca', 'Romano', 'lromano', md5('Bigliettaio#1'), '1995-01-10', 'Busto Arsizio', 'bigliettaio');

-- Username: scolombo | Password in chiaro: Bigliettaio#2
INSERT INTO public."Utenti" (nome, cognome, username, password, data_nascita, domicilio, ruolo)
VALUES ('Sara', 'Colombo', 'scolombo', md5('Bigliettaio#2'), '1998-05-30', 'Gallarate', 'bigliettaio');

-- Username: dricci | Password in chiaro: Bigliettaio#3
INSERT INTO public."Utenti" (nome, cognome, username, password, data_nascita, domicilio, ruolo)
VALUES ('Davide', 'Ricci', 'dricci', md5('Bigliettaio#3'), '1993-11-02', 'Varese', 'bigliettaio');

-- Username: emarino | Password in chiaro: Bigliettaio#4
INSERT INTO public."Utenti" (nome, cognome, username, password, data_nascita, domicilio, ruolo)
VALUES ('Elena', 'Marino', 'emarino', md5('Bigliettaio#4'), '1997-09-18', 'Como', 'bigliettaio');

-- Username: agreco | Password in chiaro: Bigliettaio#5
INSERT INTO public."Utenti" (nome, cognome, username, password, data_nascita, domicilio, ruolo)
VALUES ('Andrea', 'Greco', 'agreco', md5('Bigliettaio#5'), '1992-04-05', 'Milano', 'bigliettaio');
