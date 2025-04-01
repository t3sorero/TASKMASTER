package com.scrumsquad.taskmaster.database.definicion;

import com.scrumsquad.taskmaster.database.DBData;
import com.scrumsquad.taskmaster.lib.transactions.Transaction;
import com.scrumsquad.taskmaster.lib.transactions.TransactionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DefinicionDAOImp implements DefinicionDAO {

    @Override
    public List<DefinicionDTO> getMatches(List<Integer> ids) {
        List<DefinicionDTO> matchList = new ArrayList<>();

        String query = "SELECT * FROM definicion WHERE id = ANY(?)";
        Connection con = null;
        try {
            con = DriverManager.getConnection(DBData.DB_URL, DBData.DB_USER, DBData.DB_PASSWORD);
            PreparedStatement ps = con.prepareStatement(query);

            Array sqlArray = con.createArrayOf("integer", ids.toArray());
            ps.setArray(1, sqlArray);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                matchList.add(new DefinicionDTO(rs.getInt("id"), rs.getString("definicion"), rs.getInt("n_concepto")));
            }
            rs.close();
            ps.close();

        } catch (SQLException e) {
            matchList = null;

        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ignored) {
            }
        }
        return matchList;
    }

    @Override
    public List<DefinicionDTO> getDefinicionesByConcepto(int conceptoId) throws Exception {
        List<DefinicionDTO> defList = new ArrayList<>();
        String query = "SELECT * FROM definicion WHERE id_concepto = ?";
        Transaction transaction = TransactionManager.getInstance().getTransaccion();
        Connection con = transaction.getResource();
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, conceptoId);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            defList.add(new DefinicionDTO(
                    rs.getInt("id"),
                    rs.getString("descripcion"),
                    rs.getInt("id_concepto")));
        }
        rs.close();
        ps.close();
        return defList;
    }

    @Override
    public List<DefinicionDTO> getAllDefiniciones() {
        List<DefinicionDTO> matchList = new ArrayList<>();

        String query = "SELECT * FROM definicion";
        Connection con = null;
        try {
            con = DriverManager.getConnection(DBData.DB_URL, DBData.DB_USER, DBData.DB_PASSWORD);
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                matchList.add(new DefinicionDTO(rs.getInt("id"), rs.getString("definicion"), rs.getInt("n_concepto")));
            }
            rs.close();
            ps.close();

        } catch (SQLException e) {
            matchList = null;

        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ignored) {
            }
        }
        return matchList;
    }

    @Override
    public DefinicionDTO getDefinicionById(int id) {
        DefinicionDTO definicion = null;
        String query = "SELECT * FROM definicion WHERE id = ?";
        Connection con = null;
        try {
            con = DriverManager.getConnection(DBData.DB_URL, DBData.DB_USER, DBData.DB_PASSWORD);
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                definicion = new DefinicionDTO(
                        rs.getInt("id"),
                        rs.getString("descripcion"),
                        rs.getInt("id_concepto"));
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            definicion = null;
        } finally {
            try {
                if (con != null) con.close();
            } catch (SQLException ignored) {
            }
        }
        return definicion;
    }
}
