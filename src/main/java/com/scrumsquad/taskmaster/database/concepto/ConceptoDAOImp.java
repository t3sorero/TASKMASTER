package com.scrumsquad.taskmaster.database.concepto;

import com.scrumsquad.taskmaster.database.DBData;
import com.scrumsquad.taskmaster.lib.transactions.Transaction;
import com.scrumsquad.taskmaster.lib.transactions.TransactionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ConceptoDAOImp implements ConceptoDAO {

    @Override
    public List<ConceptoDTO> getConcepts(List<Integer> ids) {

        List<ConceptoDTO> matchList = new ArrayList<>();

        String query = "SELECT * FROM concepto WHERE id = ANY(?)";

        Connection con = null;
        try {
            con = DriverManager.getConnection(DBData.DB_URL, DBData.DB_USER, DBData.DB_PASSWORD);

            PreparedStatement ps = con.prepareStatement(query);

            Array sqlArray = con.createArrayOf("integer", ids.toArray());
            ps.setArray(1, sqlArray);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                matchList.add(new ConceptoDTO(rs.getInt("id"), rs.getString("nombre")));
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
    public List<ConceptoDTO> getAllConceptos() throws Exception {
        List<ConceptoDTO> matchList = new ArrayList<>();
        String query = "SELECT * FROM concepto";
        Transaction transaction = TransactionManager.getInstance().getTransaccion();
        Connection con = transaction.getResource();
        PreparedStatement ps = con.prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            matchList.add(new ConceptoDTO(
                    rs.getInt("id"),
                    rs.getString("nombre")));
        }
        rs.close();
        ps.close();
        return matchList;
    }

    @Override
    public ConceptoDTO getConceptoById(Integer key) {

        ConceptoDTO concepto = null;
        String query = "SELECT * FROM concepto WHERE id = ?";
        Connection con = null;
        try {
            con = DriverManager.getConnection(DBData.DB_URL, DBData.DB_USER, DBData.DB_PASSWORD);
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, key);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                concepto = new ConceptoDTO(rs.getInt("id"), rs.getString("nombre"));
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            concepto = null;
        } finally {
            try {
                if (con != null) con.close();
            } catch (SQLException ignored) {
            }
        }
        return concepto;
    }
}
