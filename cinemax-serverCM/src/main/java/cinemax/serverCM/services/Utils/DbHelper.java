package cinemax.serverCM.services.Utils;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
}