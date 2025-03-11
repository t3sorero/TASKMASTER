package com.scrumsquad.taskmaster.DB.conceptmatching_concepts;

import com.scrumsquad.taskmaster.DB.DBData;
import com.scrumsquad.taskmaster.DB.conceptmatching_matches.DefinicionesDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ConceptoDAOImp implements ConceptoDAO {

    private boolean empoderado;


    private Connection connectionEmpoderado() throws SQLException {

        empoderado = false;

        Connection conn = null;
        /**
         * TRANSACCIONES
         * */
        return DriverManager.getConnection(DBData.DB_URL, DBData.DB_USER, DBData.DB_PASSWORD);
    }
    @Override
    public List<ConceptoDTO> getConcepts(List<Integer> ids) {

        List<ConceptoDTO> matchList = new ArrayList<>();

        String query= "SELECT * FROM concepto WHERE id = ANY(?)";

        try {
            Connection con = connectionEmpoderado();
            if (!empoderado) {
                query += " FOR UPDATE";
            }
            PreparedStatement ps = con.prepareStatement(query);

            Array sqlArray = con.createArrayOf("integer", ids.toArray());
            ps.setArray(1, sqlArray);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                matchList.add(new ConceptoDTO(rs.getInt("id"), rs.getString("nombre")));            }
            rs.close();
            ps.close();

            if (empoderado) {
                con.close();
            }

        } catch (SQLException e) {
            matchList= null;

        }
        return matchList;
    }

    @Override
    public List<ConceptoDTO> getAllConceptos() {
        List<ConceptoDTO> matchList = new ArrayList<>();

        String query= "SELECT * FROM concepto";

        try {
            Connection con = connectionEmpoderado();
            if (!empoderado) {
                query += " FOR UPDATE";
            }
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                matchList.add(new ConceptoDTO(rs.getInt("id"), rs.getString("nombre")));            }
            rs.close();
            ps.close();

            if (empoderado) {
                con.close();
            }

        } catch (SQLException e) {
            matchList= null;

        }
        return matchList;
    }
}
