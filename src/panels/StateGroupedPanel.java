package panels;

import controllers.Main;
import models.DataUpdate;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class StateGroupedPanel extends JPanel {

    private JTable patientTable;
    private int orderedBy;
    private ArrayList<DataUpdate> updates;
    private DefaultTableModel model;

    // Sets up panel
    public StateGroupedPanel() {

        patientTable = new JTable();
        orderedBy = 0;

        updates = new ArrayList<DataUpdate>();

        model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[]{"State", "# Of Patients"});

        this.setLayout(new BorderLayout());

        patientTable.setModel(model);
        patientTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        patientTable.getTableHeader().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = patientTable.rowAtPoint(e.getPoint());
                int col = patientTable.columnAtPoint(e.getPoint());

                // GROUP BY THAT
                refreshData(col);
            }
        });

        JScrollPane scroll = new JScrollPane(patientTable);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        refreshData(0);

        add(scroll);
    }

    // Refreshes data (only used once because data is not editable)
    public void refreshData(int column) {

        try {

            String query = "SELECT Locations.State, COUNT(*) NumPatients from Patients " +
                    "JOIN Locations ON Patients.LocationId = Locations.LocationId " +
                    "GROUP BY Locations.State " +
                    "ORDER BY " + (column + 1);

            PreparedStatement stmt = Main.conn.prepareStatement(query);

            ResultSet rs = stmt.executeQuery();

            int i = 1;

            model.setRowCount(0);

            while (rs.next()) {

                String state = rs.getString("State");

                String numPatients = rs.getString("NumPatients");

                model.addRow(new Object[]{state, numPatients});

                i++;

            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
