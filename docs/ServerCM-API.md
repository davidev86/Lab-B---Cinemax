# ServerCM — API (protocollo TCP con oggetti Java serializzati)

## Panoramica generale

- Tipo di server: TCP server Java che scambia oggetti Java serializzati (ObjectOutputStream/ObjectInputStream).
- Porta di default: `12345` (configurata in `serverMain`).
- Protocollo: il client apre la socket, crea prima `ObjectOutputStream` poi `ObjectInputStream` (attenzione all'ordine), invia un oggetto che implementa le interfacce `cinemax.contracts.interfaces.Query` oppure `cinemax.contracts.interfaces.Command`, poi legge la `Response` corrispondente dal server.
- Requisito: client e server devono condividere le stesse classi (stesso package / serialVersionUID), perché la comunicazione usa la serializzazione Java.
- Avvio server: eseguire `cinemax.serverCM.serverMain`. All'avvio il server chiede i parametri per il DB (host, username, password). Il server usa questi parametri per la connessione JDBC.

---

## Come chiamare il server (step minimal)

1. Apri socket TCP a host:porta (es. `127.0.0.1:12345`).
2. Crea `ObjectOutputStream` sul `socket.getOutputStream()` e fai `oos.flush()`.
3. Crea `ObjectInputStream` sul `socket.getInputStream()`.
4. Serializza e invia un oggetto `Query` o `Command` con `oos.writeObject(obj); oos.flush();`.
5. Leggi la response con `ois.readObject()` e cast al tipo di `Response` atteso.
6. Chiudi socket/stream quando finito.

Esempio reale: nel repository è presente `TestClientTCP` che mostra l'uso di `GetProjections` e `StoreProjection`.

---

## Request disponibili (oggetti Java)

> Nota: sono oggetti Java definiti nel modulo `cinemax-contracts`. Il server non accetta JSON nativamente.

### GetProjections (Query)
- Tipo: `Query`, implementa `ProjectionRequest`.
- Scopo: cercare proiezioni secondo filtri (titolo, genere, range date, range prezzi).
- Campi: `String titolo`, `String genere`, `LocalDate daDataProiezione`, `LocalDate aDataProiezione`, `BigDecimal daCosto`, `BigDecimal aCosto`.
- Comportamento: campi `null` o stringhe vuote vengono ignorati; vengono aggiunte condizioni SQL solo per i campi validi.
- Response: `GetProjectionResponse` (contiene `List<ProjectionDetails>`).

Esempio Java:
```java
GetProjections req = new GetProjections();
req.setGenere("Fantasy");
req.setDaDataProiezione(LocalDate.of(2026, 8, 1));
req.setaDataProiezione(LocalDate.of(2026, 9, 1));
// invia req via ObjectOutputStream
```

Esempio JSON-like (solo documentativo):
```json
{
  "type": "GetProjections",
  "titolo": null,
  "genere": "Fantasy",
  "daDataProiezione": "2026-08-01",
  "aDataProiezione": "2026-09-01",
  "daCosto": null,
  "aCosto": null
}
```

---

### GetProjectionsByFilmIdAndDate (Query)
- Tipo: `Query`, implementa `ProjectionRequest`.
- Scopo: ottenere proiezioni per un film specifico con vincolo sulla data massima di prenotazione.
- Campi: `int film`, `LocalDate maxDataPrenotazione`.
- Response: `GetProjectionResponse`.

Esempio Java:
```java
GetProjectionsByFilmIdAndDate req = new GetProjectionsByFilmIdAndDate();
req.setFilm(42);
req.setMaxDataPrenotazione(LocalDate.of(2026, 8, 20));
```

---

### StoreProjection (Command)
- Tipo: `Command`, implementa `ProjectionRequest`.
- Scopo: inserire (salvare) una nuova proiezione nel DB.
- Campi: `Integer id` (opzionale/null per nuovo inserimento), `LocalDateTime DataOraProiezione`, `Integer idFilm`, `BigDecimal prezzoBiglietto`.
- Response: `StoreProjectionResponse` (contiene `Integer id` generato).

Esempio Java (dal `TestClientTCP` del repo):
```java
StoreProjection requestStore = new StoreProjection();
requestStore.setDataOraProiezione(LocalDateTime.now());
requestStore.setIdFilm(2);
requestStore.setPrezzoBiglietto(new BigDecimal("10.50"));
oos.writeObject(requestStore);
oos.flush();
// leggere StoreProjectionResponse
```

JSON-like:
```json
{
  "type": "StoreProjection",
  "dataOraProiezione": "2026-08-13T20:30:00",
  "idFilm": 2,
  "prezzoBiglietto": "10.50"
}
```

---

### GetUserByCredentials (Query)
- Tipo: `Query`, implementa `UserRequest`.
- Scopo: autenticare / ottenere info minima dell'utente tramite username e password (campo `md5Password`).
- Campi: `String username`, `String md5Password`.
- Response: `GetUserByCredentialResponse` (contiene `UserMinInfos`) oppure `null` se non trovato.

Esempio Java:
```java
GetUserByCredentials req = new GetUserByCredentials();
req.setUsername("mario");
req.setMd5Password("5f4dcc3b5aa765d61d8327deb882cf99"); // md5("password") esempio
oos.writeObject(req);
oos.flush();
```

---

## Risposte (Responses) principali

- `GetProjectionResponse`: `List<ProjectionDetails> getProjections()`.
  - `ProjectionDetails` contiene: `id`, `idFilm`, `dataOraProiezione` (LocalDateTime), `titoloFilm`, `genere`, `regista`, `anno`, `durataMinuti`, `etaMinima`, `costo` (BigDecimal).

- `StoreProjectionResponse`: `Integer id` (id del nuovo record).

- `GetUserByCredentialResponse`: `UserMinInfos user` (`id`, `nome`, `cognome`, `username`, `ruolo`).

---

## Esempio client compatto
Vedi `TestClientTCP` nel repository; qui un esempio minimale:

```java
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalDate;
import cinemax.contracts.queries.GetProjections;
import cinemax.contracts.responses.GetProjectionResponse;

public class TestClientExample {
    public static void main(String[] args) throws Exception {
        try (Socket socket = new Socket("127.0.0.1", 12345);
             ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream ois = new ObjectInputStream(socket.getInputStream())) {
             
            oos.flush(); // invia header
            GetProjections req = new GetProjections();
            req.setGenere("Comedy");
            oos.writeObject(req);
            oos.flush();
            
            GetProjectionResponse resp = (GetProjectionResponse) ois.readObject();
            System.out.println("Ricevute proiezioni: " + (resp.getProjections() == null ? 0 : resp.getProjections().size()));
        }
    }
}
```

---

## Requisiti / avvertenze operative
- Assicurati che client e server usino lo stesso artifact `cinemax-contracts` (stesse classi e serialVersionUID).
- Sicurezza: l'uso di `md5Password` non è sicuro per produzione.
- Concorrenza: il server crea un thread per client (`ClientHandler`).
- DB: il server usa PostgreSQL (dependency nel pom); al lancio inserire host/user/password.

---

## Dove ho preso le informazioni
- Dal codice del repository: `cinemax-serverCM` e `cinemax-contracts` (file come `serverMain`, `ClientHandler`, `ProjectionService`, `UserService`, `TestClientTCP`, `GetProjections`, `StoreProjection`, `GetProjectionResponse`, `ProjectionDetails`, `GetUserByCredentials`, ecc.).

---

## Note finali
- Questo repo comunica tramite oggetti Java serializzati, quindi non è compatibile con richieste HTTP/JSON senza un adattatore.
- Se vuoi, posso:
  - convertire questo documento in un file `.doc` (HTML) e salvarlo nel repo così puoi scaricarlo e aprirlo con Word;
  - generare un file `.docx` vero (serve un passaggio extra: posso creare il file e fornirti il link per il download se preferisci che lo generi e ti dia il file);
  - aggiungere ulteriori request/command se vuoi che faccia una ricerca approfondita nel repo.
