package com.scrumsquad.taskmaster.database.conceptmatching_matches;

import com.scrumsquad.taskmaster.database.DBData;

import java.sql.*;
import java.util.ArrayList;
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
        List<DefinicionesDTO> defList = new ArrayList<>();
        String query = "SELECT * FROM definiciones WHERE n_concepto = ?";
        Connection con = null;
        try {
            con = DriverManager.getConnection(DBData.DB_URL, DBData.DB_USER, DBData.DB_PASSWORD);
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, conceptoId);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                defList.add(new DefinicionesDTO(rs.getInt("id"), rs.getString("definicion"), rs.getInt("n_concepto")));
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            defList = null;
        } finally {
            try { if(con != null) con.close(); } catch (SQLException ignored) {}
        }
        return defList;
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

    @Override
    public DefinicionesDTO getDefinicionById(int id) {
        DefinicionesDTO definicion = null;
        String query = "SELECT * FROM definiciones WHERE id = ?";
        Connection con = null;
        try {
            con = DriverManager.getConnection(DBData.DB_URL, DBData.DB_USER, DBData.DB_PASSWORD);
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                definicion = new DefinicionesDTO(rs.getInt("id"), rs.getString("definicion"), rs.getInt("n_concepto"));
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            definicion = null;
        } finally {
            try { if(con != null) con.close(); } catch (SQLException ignored) {}
        }
        return definicion;
    }
}
