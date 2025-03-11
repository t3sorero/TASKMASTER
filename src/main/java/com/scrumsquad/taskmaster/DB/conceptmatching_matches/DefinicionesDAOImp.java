package com.scrumsquad.taskmaster.DB.conceptmatching_matches;

import com.scrumsquad.taskmaster.DB.DBData;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DefinicionesDAOImp implements DefinicionesDAO {

    @Override
    public List<DefinicionesDTO> getMatches(List<Integer> ids) {
        List<DefinicionesDTO> matchList = new ArrayList<>();

        String query= "SELECT * FROM definiciones WHERE id = ANY(?)";
        Connection con = null;
        try {
            con = DriverManager.getConnection(DBData.DB_URL, DBData.DB_USER, DBData.DB_PASSWORD);
            PreparedStatement ps = con.prepareStatement(query);

            Array sqlArray = con.createArrayOf("integer", ids.toArray());
            ps.setArray(1, sqlArray);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                matchList.add(new DefinicionesDTO(rs.getInt("id"), rs.getString("definicion"), rs.getInt("n_concepto")));
            }
            rs.close();
            ps.close();

        } catch (SQLException e) {
            matchList= null;

        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ignored) {}
        }
        return matchList;
    }

    @Override
    public List<DefinicionesDTO> getDefinicionesByConcepto(int conceptoId) {
        return List.of();
    }

    @Override
    public List<DefinicionesDTO> getAllDefiniciones() {
        List<DefinicionesDTO> matchList = new ArrayList<>();

        String query = "SELECT * FROM definiciones";
        Connection con = null;
        try {
             con = DriverManager.getConnection(DBData.DB_URL, DBData.DB_USER, DBData.DB_PASSWORD);
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                matchList.add(new DefinicionesDTO(rs.getInt("id"), rs.getString("definicion"), rs.getInt("n_concepto")));
            }
            rs.close();
            ps.close();

        } catch (SQLException e) {
            matchList = null;

        }
        finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ignored) {}
        }
        return matchList;
    }
}
