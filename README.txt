================================================================================
 CineMax - Sistema di gestione cinema monosala
 Laboratorio Interdisciplinare B - a.a. 2025/2026
================================================================================

AUTORI
------
Vedi il file autori.txt nella cartella radice del repository.


REQUISITI SOFTWARE
-------------------
- JDK 25 (o superiore)
- Apache Maven >= 3.6.3 (requisito minimo dichiarato nel <prerequisites> del
  maven-compiler-plugin 3.14.0 usato dal progetto - verificabile nel file
  ~/.m2/repository/org/apache/maven/plugins/maven-compiler-plugin/3.14.0/
  maven-compiler-plugin-3.14.0.pom). Build testata con Maven 3.9.6
  (https://maven.apache.org/)
- PostgreSQL (>= 12) in esecuzione e raggiungibile in rete
  (http://www.postgresql.org)
- Driver JDBC PostgreSQL 42.7.3 (scaricato automaticamente da Maven Central
  tramite il pom.xml, nessuna installazione manuale richiesta)


STRUTTURA DEL PROGETTO (moduli Maven)
--------------------------------------
Il progetto e' un progetto Maven multi-modulo. Il pom.xml nella radice
del repository dichiara e aggrega i seguenti moduli, compilati nell'ordine
corretto in base alle dipendenze:

  1. cinemax-contracts    -> DTO, Response, Command condivisi tra client e server
  2. cinemax-application   -> Logica di business e servizi (usati dal client)
  3. cinemax-serverCM      -> Modulo server: DAO, connessione DB, socket server
  4. cinemax-gui           -> Modulo client: interfaccia grafica (Swing)


COMANDI MAVEN PER LA COMPILAZIONE
----------------------------------
Da eseguire nella cartella radice del repository (dove si trova il pom.xml
principale):

  1) Compilare tutti i moduli e installarli nel repository Maven locale:

       mvn clean install

  2) Solo compilazione (senza generare i .jar):

       mvn clean compile

  3) Generare la Javadoc di tutti i moduli (jar allegati ad ogni modulo):

       mvn javadoc:jar

     Generare la Javadoc aggregata di tutto il progetto (in target/site/apidocs
     del modulo radice):

       mvn site

  4) Eseguire i test (se presenti):

       mvn test

Al termine di "mvn clean install" (o "mvn package"), i file .jar dei singoli
moduli si trovano in:
  cinemax-contracts/target/cinemax-contracts-1.0.0.jar     (libreria, non eseguibile)
  cinemax-application/target/cinemax-application-1.0.0.jar (libreria, non eseguibile)
  cinemax-serverCM/target/server-cm-1.0.0.jar              (ESEGUIBILE - Server)
  cinemax-gui/target/cinemax-gui-1.0.0.jar                 (ESEGUIBILE - Client)

Una copia dei due jar eseguibili viene inoltre mantenuta nella cartella
"bin/" nella radice del repository, come richiesto dalla consegna:
  bin/server-cm-1.0.0.jar
  bin/cinemax-gui-1.0.0.jar

I due jar "server-cm" e "cinemax-gui" sono jar "fat" (auto-contenuti, generati
tramite maven-shade-plugin): includono tutte le dipendenze (compreso il
driver JDBC PostgreSQL) e hanno l'attributo Main-Class impostato nel
manifest, quindi sono avviabili direttamente con "java -jar" senza bisogno
di specificare un classpath.


COME AVVIARE IL SERVER (serverCM)
-----------------------------------
Classe main: cinemax.serverCM.ServerMain
(file: cinemax-serverCM/src/main/java/cinemax/serverCM/ServerMain.java)

Opzione A - Da riga di comando, usando il jar eseguibile (consigliato):

    java -jar bin/server-cm-1.0.0.jar

  Alla partenza, il server richiede da riga di comando (stdin):
    - host del database PostgreSQL
    - porta del database
    - username per accedere al database (dbCM)
    - password per accedere al database
  Il server rimane in ascolto sulla porta 12345 in attesa di connessioni
  client, gestendo piu' client in parallelo (un thread per client).

Opzione B - Da Eclipse (o altro IDE):
  1. Importare il progetto come "Existing Maven Projects".
  2. Selezionare la classe ServerMain nel modulo cinemax-serverCM.
  3. Run As -> Java Application.


COME AVVIARE IL CLIENT (clientCM / GUI)
------------------------------------------
Classe main: cinemax.clientCM.Cinemaxhome
(file: cinemax-gui/src/main/java/cinemax/clientCM/Cinemaxhome.java)

Opzione A - Da riga di comando, usando il jar eseguibile (consigliato):

    java -jar bin/cinemax-gui-1.0.0.jar

  E' possibile avviare piu' istanze del client in parallelo (anche da
  postazioni diverse) per simulare piu' utenti connessi contemporaneamente
  allo stesso serverCM.

Opzione B - Da Eclipse (o altro IDE):
  1. Selezionare la classe Cinemaxhome nel modulo cinemax-gui.
  2. Run As -> Java Application.


DATABASE
--------
Il server si collega ad un'istanza PostgreSQL le cui credenziali e host
vengono richiesti a runtime all'avvio di ServerMain (nessuna configurazione
hard-coded). Assicurarsi che:
  - il database sia raggiungibile all'host/porta indicati
  - esista un database dedicato a CineMax (dbCM) con le tabelle Utenti,
    Proiezioni/Film e Prenotazioni correttamente popolate
  - la tabella Utenti contenga almeno 2 proiezionisti e 5 bigliettai
    precaricati, come richiesto dalle specifiche di progetto


LIBRERIE ESTERNE
------------------
- org.postgresql:postgresql:42.7.3 - driver JDBC per la connessione a
  PostgreSQL, gestito automaticamente da Maven (dependencyManagement nel
  pom.xml radice). Non e' richiesta alcuna installazione manuale ne'
  l'aggiunta di jar esterni nella cartella lib/.
- org.apache.maven.plugins:maven-shade-plugin:3.5.1 - plugin di build (non
  una libreria a runtime) usato nei moduli cinemax-serverCM e cinemax-gui
  per impacchettare un jar "fat" auto-contenuto con Main-Class impostata.

Nessun'altra libreria non standard e' attualmente utilizzata dal progetto.


DOCUMENTAZIONE
---------------
La documentazione di progetto (manuale utente, manuale tecnico, diagrammi
ER/UML, javadoc) si trova nella cartella "documentazione/" nella radice
del repository.
