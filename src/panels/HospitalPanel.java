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
import java.sql.Statement;
import java.util.ArrayList;

public class HospitalPanel extends JPanel {

    private JTable hospitalTable;
    private DefaultTableModel model;
    private ArrayList<DataUpdate> updates;
    private int orderedBy;

    public HospitalPanel() {
        super();

        updates = new ArrayList<DataUpdate>();
        orderedBy = 0;

        // Make it so ID is not editable!
        hospitalTable = new JTable() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column != 0;
            }
        };

        model = new DefaultTableModel();

        model.setColumnIdentifiers(new String[]{"ID", "Name", "Phone Number", "Address"});

        this.setLayout(new BorderLayout());

        hospitalTable.setModel(model);
        hospitalTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        hospitalTable.getColumnModel().getColumn(0).setMaxWidth(37);

        hospitalTable.getTableHeader().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = hospitalTable.rowAtPoint(e.getPoint());
                int col = hospitalTable.columnAtPoint(e.getPoint());

                // GROUP BY THAT
                refreshData(col);
            }
        });

        hospitalTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = hospitalTable.rowAtPoint(e.getPoint());
                int col = hospitalTable.columnAtPoint(e.getPoint());

                // SHOW BUTTONS
                Main.showButtons(row, col);
            }
        });

        model.addTableModelListener(new TableModelListener() {

            @Override
            public void tableChanged(TableModelEvent e) {
                if (e.getFirstRow() != -1 && e.getColumn() != -1)
                {
                    int id = (int) hospitalTable.getValueAt(e.getFirstRow(), 0);
                    Object data = hospitalTable.getValueAt(e.getFirstRow(), e.getColumn());
                    updates.add(new DataUpdate(id, e.getColumn(), data));
                }
            }
        });

        JScrollPane scroll = new JScrollPane(hospitalTable);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        refreshData(0);

        add(scroll);
    }

    public void refreshData()
    {
        refreshData(orderedBy);
    }

    // Order by the given column, reset table and get all values from the database in a totally clean way
    public void refreshData(int column) {
        try {
            orderedBy = column;

            // Column + 1 to account for SQL starting with 1 and Java having column 0
            ResultSet rs = getHospitalData(column + 1);

            int i = 0;

            model.setRowCount(0);

            while (rs.next()) {

                int hospitalId = rs.getInt(1);

                String hospitalName = rs.getString(2);

                String hospitalTollFreeNum = rs.getString(3);

                String hospitalAddress = rs.getString(4);

                model.addRow(new Object[]{hospitalId, hospitalName, hospitalTollFreeNum, hospitalAddress});

                i++;

            }

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    // Updates data that has been changed on this panel
    public void updateData()
    {

        try {

            // Uses transaction so that either all updates are completed or not
            String query = "START TRANSACTION";

            Statement stmt = Main.conn.createStatement();

            stmt.execute(query);

            for (int i = 0; i < updates.size(); ++i) {
                DataUpdate update = updates.get(i);
                query = "UPDATE Hospitals ";
                if (update.getCol() == 1) {
                    String hospitalName = (String) update.getData();

                    query += "SET Name = ? ";
                    query += "WHERE HospitalId = ?;";

                    PreparedStatement statement = Main.conn.prepareStatement(query);

                    statement.setString(1, hospitalName);
                    statement.setInt(2, update.getId());

                    statement.executeUpdate();
                } else if (update.getCol() == 2) {
                    String hospitalTollFreeNum = (String) update.getData();

                    query += "SET TollFreePhoneNum = ? ";
                    query += "WHERE HospitalId = ?;";

                    PreparedStatement statement = Main.conn.prepareStatement(query);

                    statement.setString(1, hospitalTollFreeNum);
                    statement.setInt(2, update.getId());

                    statement.executeUpdate();
                } else if (update.getCol() == 3) {
                    String hospitalAddress = (String) update.getData();
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

    // Deletes row currently selected
    public void deleteSelectedRow()
    {
        try {
            int row = hospitalTable.getSelectedRow();

            // Gets the ID to use for the delete from the first column
            int id = (int) hospitalTable.getValueAt(row, 0);

            String query = "DELETE FROM Doctors WHERE HospitalId = ?";

            PreparedStatement statement = Main.conn.prepareStatement(query);

            statement.setInt(1, id);

            int resultCode = statement.executeUpdate();

            query = "DELETE FROM Hospitals WHERE HospitalId = ?";

            statement = Main.conn.prepareStatement(query);

            statement.setInt(1, id);

            resultCode = statement.executeUpdate();

            refreshData();
        }
        catch (SQLException sqle)
        {
            sqle.printStackTrace();
        }
    }

    public boolean insertData(String[] data)
    {
        try {

            String query = "INSERT INTO Locations (Address, State, PostCode) " +
                    "VALUES (?, ?, ?)";

            PreparedStatement statement = Main.conn.prepareStatement(query, new String[]{"LocationId"});

            statement.setString(1, data[1]);
            statement.setString(2, data[2]);
            statement.setString(3, data[3]);

            int resultCode = statement.executeUpdate();

            if (resultCode < 0) {
                return false;
            }

            ResultSet keys = statement.getGeneratedKeys();

            int locationId = -1;
            if (keys.next()) {
                locationId = keys.getInt(1);
            }

            query = "INSERT INTO Hospitals (Name, LocationId, PhoneNum, TollFreePhoneNum) " +
                    "VALUES (?, ?, ?, ?)";

            statement = Main.conn.prepareStatement(query);

            statement.setString(1,data[0]);
            statement.setInt(2, locationId);
            statement.setString(3, data[4]);
            statement.setString(4, data[5]);

            resultCode = statement.executeUpdate();

            if (resultCode < 0) {
                return false;
            }

            refreshData(0);

        } catch(SQLException e) {

            e.printStackTrace();

            return false;
        }
        return true;
    }

    // Gets hospital data in general
    private ResultSet getHospitalData(int columnToOrderBy) {
        try {

            String query = "SELECT * from vHospitalData " +
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

    // Gets hospital data only from a certain state by using a subquery, and updates table
    public void filterDataByState(String text) {

        try {
            String query = "SELECT HospitalId, Name, TollFreePhoneNum from Hospitals hos, (" +
                    "SELECT LocationId, State, Address FROM Locations loc " +
                    "WHERE State = ?) states " +
                    "WHERE hos.LocationId = states.LocationId";

            PreparedStatement stmt = Main.conn.prepareStatement(query);

            stmt.setString(1, text);

            ResultSet rs = stmt.executeQuery();

            int i = 0;

            model.setRowCount(0);

            while (rs.next()) {

                int hospitalId = rs.getInt(1);

                String hospitalName = rs.getString(2);

                String hospitalTollFreeNum = rs.getString(3);

                String hospitalAddress = text;

                model.addRow(new Object[]{hospitalId, hospitalName, hospitalTollFreeNum, hospitalAddress});

                i++;

            }

        } catch(SQLException sqle) {
            sqle.printStackTrace();
        }
    }
}
