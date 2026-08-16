package cinemax.serverCM.dao.Utils;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DbHelper {

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
	
	public static int executeUpdate(Connection conn, String sql, List<Object> params) throws SQLException {
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			for (int i = 0; i < params.size(); i++) {
				pstmt.setObject(i + 1, params.get(i));
			}

			return pstmt.executeUpdate();
		}
	}
}