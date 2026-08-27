/**
 * @authors Francesca Pelizzoni, matricola 751550 (VA) e Davide Villa, matricola 701105 (VA)
 */
package cinemax.serverCM.dao.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe di utilità per l'esecuzione di operazioni SQL (query di selezione, inserimenti e aggiornamenti)
 * tramite JDBC, fornendo la gestione automatica dei {@link PreparedStatement}, dei parametri posizionali
 * e la mappatura generica dei risultati tramite callback {@link RowMapper}.
 */
public class DbHelper {

	/**
	 * Costruttore privato per prevenire l'istanziazione diretta della classe di utilità.
	 */
	private DbHelper() {
	}

	/**
	 * Esegue una query SQL parametrizzata di selezione e mappa ogni record del {@link ResultSet}
	 * in una lista di oggetti tipizzati tramite il {@link RowMapper} specificato.
	 *
	 * @param <T>    il tipo generico dell'oggetto DTO o entità di ritorno
	 * @param conn   la connessione JDBC attiva verso il database
	 * @param sql    l'istruzione SQL contenente i segnaposto posizionali (?)
	 * @param params la lista degli oggetti da associare ai parametri posizionali
	 * @param mapper l'istanza di {@link RowMapper} incaricata di convertire la singola riga in oggetto di tipo {@code T}
	 * @return la lista contenente gli elementi estratti e mappati
	 * @throws SQLException in caso di errore durante l'accesso al database o l'esecuzione della query
	 */
	public static <T> List<T> executeQuery(Connection conn, String sql, List<Object> params, RowMapper<T> mapper) throws SQLException {
		List<T> results = new ArrayList<>();

		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			for (int i = 0; i < params.size(); i++) {
				pstmt.setObject(i + 1, params.get(i));
			}

			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					// Restituisce l'oggetto mappato del tipo corretto <T>
					results.add(mapper.mapRow(rs));
				}
			}
		}
		return results;
	}

	/**
	 * Esegue un'istruzione SQL di inserimento (INSERT) con recupero automatico della chiave primaria
	 * generata dal database (chiave surrogata intera).
	 *
	 * @param conn   la connessione JDBC attiva
	 * @param sql    l'istruzione SQL di inserimento parametrizzata
	 * @param params i valori da associare ai parametri posizionali
	 * @return l'identificativo numerico intero (ID) assegnato al record appena creato
	 * @throws SQLException se l'inserimento fallisce, nessuna riga viene creata o non viene restituito alcun ID generato
	 */
	public static Integer executeInsert(Connection conn, String sql, List<Object> params) throws SQLException {
		// Statement.RETURN_GENERATED_KEYS indica al driver JDBC di catturare l'ID creato
		try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			for (int i = 0; i < params.size(); i++) {
				pstmt.setObject(i + 1, params.get(i));
			}

			int affectedRows = pstmt.executeUpdate();
			if (affectedRows == 0) {
				throw new SQLException("Inserimento fallito, nessuna riga creata.");
			}

			try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					return generatedKeys.getInt("id"); // Restituisce il valore del primo campo (l'ID)
				} else {
					throw new SQLException("Inserimento riuscito ma nessun ID è stato restituito.");
				}
			}
		}
	}

	/**
	 * Esegue un'istruzione SQL di modifica o cancellazione dei dati (UPDATE o DELETE)
	 * restituendo il conteggio delle righe coinvolte.
	 *
	 * @param conn   la connessione JDBC attiva
	 * @param sql    l'istruzione SQL parametrizzata da eseguire
	 * @param params la lista dei valori da associare ai parametri
	 * @return il numero di record modificati o eliminati a seguito dell'operazione
	 * @throws SQLException in caso di errore di sintassi SQL o fallimento del comando
	 */
	public static int executeUpdate(Connection conn, String sql, List<Object> params) throws SQLException {
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			for (int i = 0; i < params.size(); i++) {
				pstmt.setObject(i + 1, params.get(i));
			}

			return pstmt.executeUpdate();
		}
	}
}