package panels;

import controllers.Main;
import models.DataUpdate;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class StateGroupedHospitalPanel extends JPanel {

    private JTable hospitalPanel;
    private int orderedBy;
    private ArrayList<DataUpdate> updates;
    private DefaultTableModel model;

    // Sets up panel in general
    public StateGroupedHospitalPanel() {

        hospitalPanel = new JTable();
        orderedBy = 0;

        updates = new ArrayList<DataUpdate>();

        model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[]{"State", "# Of Hospitals"});

        this.setLayout(new BorderLayout());

        hospitalPanel.setModel(model);
        hospitalPanel.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        hospitalPanel.getTableHeader().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = hospitalPanel.rowAtPoint(e.getPoint());
                int col = hospitalPanel.columnAtPoint(e.getPoint());

                // GROUP BY THAT
                refreshData(col);
            }
        });

        JScrollPane scroll = new JScrollPane(hospitalPanel);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        refreshData(0);

        add(scroll);
    }

    // Refreshes data (only used once)
    public void refreshData(int column) {

        try {

            String query = "SELECT Locations.State, COUNT(*) NumHospitals from Hospitals " +
                    "JOIN Locations ON Hospitals.LocationId = Locations.LocationId " +
                    "GROUP BY Locations.State " +
                    "ORDER BY " + (column + 1);

            PreparedStatement stmt = Main.conn.prepareStatement(query);

            ResultSet rs = stmt.executeQuery();

            int i = 1;

            model.setRowCount(0);

            while (rs.next()) {

                String state = rs.getString("State");

                String numHospitals = rs.getString("NumHospitals");

                model.addRow(new Object[]{state, numHospitals});

                i++;

            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
