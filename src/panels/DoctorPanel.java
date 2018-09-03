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
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DoctorPanel extends JPanel {

    private JScrollPane scroll;
    private JTable doctorTable;
    private DefaultTableModel model;
    private ArrayList<DataUpdate> updates;

    private int orderedBy;

    // Sets up panel in general
    public DoctorPanel() {
        super();

        // Make it so ID is not editable!
        doctorTable = new JTable() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column != 0;
            }
        };

        orderedBy = 0;

        updates = new ArrayList<DataUpdate>();

        this.setLayout(new BorderLayout());

        model = new DefaultTableModel();

        model.setColumnIdentifiers(new String[]{"ID", "Name", "Phone", "Hospital Name"});

        doctorTable.setModel(model);
        doctorTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        doctorTable.getColumnModel().getColumn(0).setMaxWidth(37);

        doctorTable.getTableHeader().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = doctorTable.rowAtPoint(e.getPoint());
                int col = doctorTable.columnAtPoint(e.getPoint());

                // GROUP BY THAT
                refreshData(col);
            }
        });

        doctorTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = doctorTable.rowAtPoint(e.getPoint());
                int col = doctorTable.columnAtPoint(e.getPoint());

                // SHOW BUTTONS
                Main.showButtons(row, col);
            }
        });

        model.addTableModelListener(new TableModelListener() {

            @Override
            public void tableChanged(TableModelEvent e) {
                if (e.getFirstRow() != -1 && e.getColumn() != -1)
                {
                    int id = (int) doctorTable.getValueAt(e.getFirstRow(), 0);
                    Object data = doctorTable.getValueAt(e.getFirstRow(), e.getColumn());
                    updates.add(new DataUpdate(id, e.getColumn(), data));
                }
            }
        });

        scroll = new JScrollPane(doctorTable);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        refreshData(0);

        add(scroll);
    }

    public void refreshData()
    {
        refreshData(orderedBy);
    }

    // Refreshes data by whatever column (clicked on)
    public void refreshData(int column) {
        try {
            // Save current column ordered by
            orderedBy = column;

            // +1 to account for difference SQL->Java
            ResultSet rs = getDoctorData(column + 1);

            int i = 0;

            model.setRowCount(0);

            while (rs.next()) {

                int doctorId = rs.getInt(1);

                String doctorName = rs.getString(2);

                String doctorPhone = rs.getString(3);

                String hospitalName = rs.getString(4);

                model.addRow(new Object[]{doctorId, doctorName, doctorPhone, hospitalName});

                i++;

            }

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    // Updates database with all the current changes
    public void updateData()
    {
        try {

            // Uses transaction to ensure that the updates all complete successfully (or not)
            String query = "START TRANSACTION";

            Statement stmt = Main.conn.createStatement();

            stmt.execute(query);

            for (int i = 0; i < updates.size(); ++i) {

                DataUpdate update = updates.get(i);
                query = "UPDATE Doctors ";

                if (update.getCol() == 1) {
                    String doctorName = (String) update.getData();

                    query += "SET Name = ? ";
                    query += "WHERE DoctorId = ?;";

                    PreparedStatement statement = Main.conn.prepareStatement(query);

                    statement.setString(1, doctorName);
                    statement.setInt(2, update.getId());

                    statement.executeUpdate();
                } else if (update.getCol() == 2) {
                    String doctorPhone = (String) update.getData();

                    query += "SET Phone  = ? ";
                    query += "WHERE DoctorId = ?;";

                    PreparedStatement statement = Main.conn.prepareStatement(query);

                    statement.setString(1, doctorPhone);
                    statement.setInt(2, update.getId());

                    statement.executeUpdate();
                } else if (update.getCol() == 3) {

                    // Should go to Insert Hospital panel!

                    String hospitalName = (String) update.getData();

                    query += "SET HospitalId = ? ";
                    query += "WHERE DoctorId = ?;";

                    PreparedStatement statement = Main.conn.prepareStatement(query);

                    // Find hospital id somehow

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

    // Delete only selected row
    public void deleteSelectedRow()
    {
        try {
            int row = doctorTable.getSelectedRow();

            // Get ID from first column to delete that doctor from database
            int id = (int) doctorTable.getValueAt(row, 0);

            String query = "DELETE FROM Doctors WHERE DoctorId = ?";

            PreparedStatement statement = Main.conn.prepareStatement(query);

            statement.setInt(1, id);

            statement.executeUpdate();

            refreshData();

        }
        catch (SQLException sqle)
        {
            sqle.printStackTrace();
        }
    }

    // Insert given data
    public boolean insertData(String[] data)
    {
        try {

            String query = "INSERT INTO Doctors (HospitalId, Name, Phone) " +
                    "VALUES (?, ?, ?)";

            PreparedStatement statement = Main.conn.prepareStatement(query);

            statement.setString(1, data[0]);
            statement.setString(2, data[1]);
            statement.setString(3, data[2]);

            int resultCode = statement.executeUpdate();

            if (resultCode < 0) {
                return false;
            }

            refreshData(0);

        } catch(SQLException e) {
            System.out.println("\nThat is not a valid Hospital ID! Try again.\n");

            return false;
        }
        return true;
    }


    private ResultSet getDoctorData(int columnToOrderBy) {
        try {

            String query = "SELECT * from vDoctorData " +
                    "ORDER BY " + columnToOrderBy;

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

    // Filter by the given Hospital Name and update database
    public void filterDataByHospitalName(String text) {

        try {
            // Save current column ordered by

            // +1 to account for difference SQL->Java
            String query = "SELECT * from vDoctorData " +
                    "WHERE hosName = ? " +
                    "ORDER BY " + (orderedBy + 1);


            PreparedStatement stmt = Main.conn.prepareStatement(query);

            stmt.setString(1, text);

            ResultSet rs = stmt.executeQuery();

            int i = 0;

            model.setRowCount(0);

            while (rs.next()) {

                int doctorId = rs.getInt(1);

                String doctorName = rs.getString(2);

                String doctorPhone = rs.getString(3);

                String hospitalName = rs.getString(4);

                model.addRow(new Object[]{doctorId, doctorName, doctorPhone, hospitalName});

                i++;

            }

        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
