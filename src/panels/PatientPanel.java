package panels;

import controllers.Main;
import models.DataUpdate;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class PatientPanel extends JPanel {

    private JTable patientTable;
    private DefaultTableModel model;
    private ArrayList<DataUpdate> updates;
    int orderedBy;

    // Sets up basic GUI components and onclick/onedit listeners
    public PatientPanel() {
        super();

        // Make it so ID is not editable!
        patientTable = new JTable() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column != 0;
            }
        };
        orderedBy = 0;

        updates = new ArrayList<DataUpdate>();

        model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[]{"ID", "Name", "Address", "Condition"});

        this.setLayout(new BorderLayout());

        patientTable.setModel(model);
        patientTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        patientTable.getColumnModel().getColumn(0).setMaxWidth(37);

        patientTable.getTableHeader().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = patientTable.rowAtPoint(e.getPoint());
                int col = patientTable.columnAtPoint(e.getPoint());

                // ORDER BY THAT
                refreshData(col);
            }
        });

        patientTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = patientTable.rowAtPoint(e.getPoint());
                int col = patientTable.columnAtPoint(e.getPoint());

                // SHOW BUTTONS
                Main.showButtons(row, col);
            }
        });

        model.addTableModelListener(new TableModelListener() {

            @Override
            public void tableChanged(TableModelEvent e) {
                if (e.getFirstRow() != -1 && e.getColumn() != -1)
                {
                    int id = Integer.parseInt((String)(patientTable.getValueAt(e.getFirstRow(), 0)));
                    Object data = patientTable.getValueAt(e.getFirstRow(), e.getColumn());
                    updates.add(new DataUpdate(id, e.getColumn(), data));
                }
            }
        });

        JScrollPane scroll = new JScrollPane(patientTable);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        refreshData(0);

        add(scroll);
    }

    public void refreshData()
    {
        refreshData(orderedBy);
    }

    // Can be used anytime to signify the refreshing of the panel (by replacing the table's row count with 0)
    public void refreshData(int column) {
        try {
            orderedBy = column;

            // Column + 1 to avoid off by one issue (SQL starts with 1)
            ResultSet rs = getPatientData(column + 1);

            int i = 1;

            // Resets table
            model.setRowCount(0);

            // Fills up table
            while (rs.next()) {

                String patientId = rs.getString("PatientId");

                String patientName = rs.getString("Name");

                String address = rs.getString("Address");

                String condition = rs.getString("GeneralDiagnosis");

                model.addRow(new Object[]{patientId, patientName, address, condition});

                i++;

            }

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    // Update data based on current changes
    public void updateData()
    {
        try {

            // Use transaction to ensure all updates are completed (or not)
            String query = "START TRANSACTION";

            Statement stmt = Main.conn.createStatement();

            stmt.execute(query);

            for (int i = 0; i < updates.size(); ++i) {

                DataUpdate update = updates.get(i);
                query = "UPDATE Patients ";

                if (update.getCol() == 1) {
                    String patientName = (String) update.getData();

                    query += "SET Name = ? ";
                    query += "WHERE PatientId = ?;";

                    PreparedStatement statement = Main.conn.prepareStatement(query);

                    statement.setString(1, patientName);
                    statement.setInt(2, update.getId());

                    statement.executeUpdate();

                } else if (update.getCol() == 2) {
                    String patientAddress = (String) update.getData();

                } else if (update.getCol() == 3) {
                    String patientCondition = (String) update.getData();
                }
            }

            query = "COMMIT;";

            stmt = Main.conn.createStatement();

            stmt.execute(query);

            updates = new ArrayList<DataUpdate>();
        }
        catch (SQLException sqle)
        {
            sqle.printStackTrace();
        }
    }

    // Needs the value of the ID for deletion, so get from first column
    public void deleteSelectedRow()
    {
        try {
            int row = patientTable.getSelectedRow();
            int id = Integer.parseInt(patientTable.getValueAt(row, 0).toString());

            String query = "DELETE FROM Patient_Phones WHERE PatientId = ?";

            PreparedStatement statement = Main.conn.prepareStatement(query);

            statement.setInt(1, id);

            statement.executeUpdate();

            query = "DELETE FROM Prescriptions WHERE PatientId = ?";

            statement = Main.conn.prepareStatement(query);

            statement.setInt(1, id);

            statement.executeUpdate();

            query = "DELETE FROM Patients WHERE PatientId = ?";

            statement = Main.conn.prepareStatement(query);

            statement.setInt(1, id);

            statement.executeUpdate();

            refreshData();
        }
        catch (SQLException sqle)
        {
            sqle.printStackTrace();
        }
    }

    // Insert into Locations first because of foreign key constraint
    public boolean insertData(String[] data)
    {
        try {

            String query = "INSERT INTO Locations (Address, State, PostCode) " +
                    "VALUES (?, ?, ?)";

            PreparedStatement statement = Main.conn.prepareStatement(query, new String[]{"LocationId"});

            statement.setString(1, data[4]);
            statement.setString(2, data[5]);
            statement.setString(3, data[6]);

            int resultCode = statement.executeUpdate();

            if (resultCode < 0) {
                return false;
            }

            ResultSet keys = statement.getGeneratedKeys();

            int locationId = -1;
            if (keys.next()) {
                locationId = keys.getInt(1);
            }

            query = "INSERT INTO Conditions (GeneralDiagnosis, SpecificDiagnosis, Severity) " +
                    "VALUES (?, ?, ?)";

            statement = Main.conn.prepareStatement(query, new String[]{"ConditionId"});

            statement.setString(1,data[1]);
            statement.setString(2, data[2]);
            statement.setInt(3, Integer.parseInt(data[3]));

            resultCode = statement.executeUpdate();

            if (resultCode < 0) {
                return false;
            }

            keys = statement.getGeneratedKeys();

            int conditionId = -1;
            if (keys.next()) {
                conditionId = keys.getInt(1);
            }

            query = "INSERT INTO CreditCards (CreditCardNum, CreditCardType, CreditCardExpType) " +
                    "VALUES (?, ?, ?)";

            statement = Main.conn.prepareStatement(query, new String[]{"CreditCardId"});

            statement.setString(1,data[7]);
            statement.setString(2, data[8]);
            statement.setString(3, data[9]);

            resultCode = statement.executeUpdate();

            if (resultCode < 0) {
                return false;
            }

            keys = statement.getGeneratedKeys();

            int creditCardId = -1;
            if (keys.next()) {
                creditCardId = keys.getInt(1);
            }

            query = "INSERT INTO Patients (Name, ConditionId, LocationId, CreditCardId) " +
                    "VALUES (?, ?, ?, ?)";

            statement = Main.conn.prepareStatement(query);

            statement.setString(1, data[0]);
            statement.setInt(2, conditionId);
            statement.setInt(3, locationId);
            statement.setInt(4, creditCardId);

            resultCode = statement.executeUpdate();

            if (resultCode < 0) {
                return false;
            }
            refreshData(0);

        } catch(Exception e) {

            e.printStackTrace();

            return false;
        }
        return true;
    }

    // Orders by the given column
    private ResultSet getPatientData(int column) {
        try {

            String query = "SELECT * from vPatientData " +
                    "ORDER BY " + column;

            PreparedStatement stmt = Main.conn.prepareStatement(query);

            return stmt.executeQuery();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
    }

    public void filterDataByState(String text) {

        try {
            String query = "SELECT pat.PatientId, pat.Name, con.GeneralDiagnosis from Patients pat " +
                    "JOIN Locations loc ON pat.LocationId = loc.LocationId " +
                    "JOIN Conditions con ON pat.ConditionId = con.ConditionId " +
                    "WHERE loc.State = ?";

            PreparedStatement stmt = Main.conn.prepareStatement(query);

            stmt.setString(1, text);

            ResultSet rs = stmt.executeQuery();

            int i = 0;

            model.setRowCount(0);

            while (rs.next()) {

                int patientId = rs.getInt(1);

                String patientName = rs.getString(2);

                String state = text;

                String generalDiagnosis = rs.getString(3);

                model.addRow(new Object[]{patientId, patientName, state, generalDiagnosis});

                i++;

            }

        } catch(SQLException sqle) {
            sqle.printStackTrace();
        }
    }
}
