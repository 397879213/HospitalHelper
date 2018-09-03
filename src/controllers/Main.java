package controllers;/*
 * controllers.Main: Setup/configuration for the main project.
 *
 */

import panels.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class Main {
    /* Assumes there exists a database called 'cpsc_408'! */
    public static final String CONNECTION_STRING = "jdbc:mysql://localhost:3306/cpsc_408";
    public static java.sql.Connection conn;
    private static JFrame frame;
    private static TopBarPanel topBarPanel;
    private static JPanel currentPanel;
    private static DataManager manager;

    /* controllers.Main application setup and run */
    public static void main(String[] args) {
        manager = new DataManager();
        manager.connect();
        boolean createdTables= manager.createTables();

        // Only generate data if the tables were just created successfully!
        if (createdTables) {
            manager.generateData();
        }
        Main main = new Main();
        main.run();
    }

    /*
     * Runs the window, instantiated with
     */
    public void run() {
        frame = new JFrame("Hospital Helper!");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setResizable(true);
        frame.setVisible(true);
        frame.setName("TROGDOR!");
        frame.setSize(new Dimension(800, 600));
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation((dim.width / 2) - (frame.getWidth() / 2), (dim.height / 2) - frame.getHeight() / 2);      // Set in the middle of the screen    }
        frame.repaint();

        topBarPanel = new TopBarPanel();
        topBarPanel.setBorder(new EmptyBorder(0, 0, 10, 0));
        topBarPanel.setPreferredSize(new Dimension(frame.getWidth(), 30));
        frame.add(topBarPanel);

        switchToHome();
    }

    public static void switchToPatient() {
        switchPanel(new PatientPanel());
        topBarPanel.switchToPatient();
    }

    public static void switchToHome() {
        switchPanel(new HomePanel());
        topBarPanel.switchToHome();
    }

    public static void switchToHospital() {
        switchPanel(new HospitalPanel());
        topBarPanel.switchToHospital();
    }

    public static void switchToDoctor() {
        switchPanel(new DoctorPanel());
        topBarPanel.switchToDoctor();
    }

    private static void switchPanel(JPanel panel) {
        frame.getContentPane().removeAll();
        currentPanel = panel;
        currentPanel.setPreferredSize(new Dimension(frame.getWidth(), frame.getHeight() - 30));
        frame.add(currentPanel, BorderLayout.CENTER);
        frame.add(topBarPanel, BorderLayout.NORTH);
        frame.revalidate();
    }

    public static void getInput() {
        if (currentPanel instanceof PatientPanel)
        {
            String[] values = new String[]
            {
                "Patient Name",
                "General Diagnosis",
                "Specific Diagnosis",
                "Severity",
                "Address",
                "State",
                "Postal Code",
                "Credit Card Number",
                "Credit Card Type",
                "Credit Card Expiration Date"
            };

            InputPanel panel = new InputPanel(values);
            frame.getContentPane().removeAll();
            panel.setPreferredSize(new Dimension(frame.getWidth(), frame.getHeight() - 30));
            frame.add(panel, BorderLayout.CENTER);
            frame.add(topBarPanel, BorderLayout.NORTH);
            frame.revalidate();
        }
        else if (currentPanel instanceof HospitalPanel)
        {
            String[] values = new String[]
            {
                "Hospital Name",
                "Address",
                "State",
                "Postal Code",
                "Phone Number",
                "Toll Free Phone Number"
            };

            InputPanel panel = new InputPanel(values);
            frame.getContentPane().removeAll();
            panel.setPreferredSize(new Dimension(frame.getWidth(), frame.getHeight() - 30));
            frame.add(panel, BorderLayout.CENTER);
            frame.add(topBarPanel, BorderLayout.NORTH);
            frame.revalidate();
        }
        else if (currentPanel instanceof DoctorPanel)
        {
            String[] values = new String[]
            {
                "Hospital ID",
                "Doctor Name",
                "Doctor Phone"
            };

            InputPanel panel = new InputPanel(values);
            frame.getContentPane().removeAll();
            panel.setPreferredSize(new Dimension(frame.getWidth(), frame.getHeight() - 30));
            frame.add(panel, BorderLayout.CENTER);
            frame.add(topBarPanel, BorderLayout.NORTH);
            frame.revalidate();
        }
    }

    public static void showButtons(int row, int col)
    {
        topBarPanel.switchToSelected(currentPanel, row, col);
    }

    public static void processData(String[] data) {

        if (currentPanel instanceof PatientPanel)
        {
            boolean result = ((PatientPanel) currentPanel).insertData(data);
            if (result) {
                switchPanel(currentPanel);
                topBarPanel.switchToPatient();
            }
        }
        else if (currentPanel instanceof HospitalPanel)
        {
            boolean result = ((HospitalPanel) currentPanel).insertData(data);
            if (result) {
                switchPanel(currentPanel);
                topBarPanel.switchToHospital();
            }
        }
        else if (currentPanel instanceof DoctorPanel)
        {
            boolean result = ((DoctorPanel) currentPanel).insertData(data);
            if (result) {
                switchPanel(currentPanel);
                topBarPanel.switchToDoctor();
            }
        }
    }

    public static void ignoreData()
    {
        switchPanel(currentPanel);

        if (currentPanel instanceof PatientPanel)
        {
            topBarPanel.switchToPatient();
        }
        else if (currentPanel instanceof HospitalPanel)
        {
            topBarPanel.switchToHospital();
        }
        else if (currentPanel instanceof DoctorPanel)
        {
            topBarPanel.switchToDoctor();
        }
    }

    public static void exportHospitalData() {
        manager.exportHospitalData();
    }

    public static void exportPatientData() {
        manager.exportPatientData();
    }

    public static void exportDoctorData() {
        manager.exportDoctorData();
    }

    public static void groupPanel() {
        if (currentPanel instanceof PatientPanel)
        {
            StateGroupedPanel panel = new StateGroupedPanel();
            frame.getContentPane().removeAll();
            panel.setPreferredSize(new Dimension(frame.getWidth(), frame.getHeight() - 30));
            frame.add(panel, BorderLayout.CENTER);
            frame.add(topBarPanel, BorderLayout.NORTH);
            frame.revalidate();

            topBarPanel.switchToFilterBar(currentPanel);
        }
        else if (currentPanel instanceof DoctorPanel)
        {
            topBarPanel.switchToSearch("Hospital Name: ");
        }
        else if (currentPanel instanceof HospitalPanel)
        {
            StateGroupedHospitalPanel panel = new StateGroupedHospitalPanel();
            frame.getContentPane().removeAll();
            panel.setPreferredSize(new Dimension(frame.getWidth(), frame.getHeight() - 30));
            frame.add(panel, BorderLayout.CENTER);
            frame.add(topBarPanel, BorderLayout.NORTH);
            frame.revalidate();

            topBarPanel.switchToFilterBar(currentPanel);
        }
    }

    public static void filterPanel(String text) {
        switchPanel(currentPanel);
        if (currentPanel instanceof PatientPanel)
        {
            ((PatientPanel)currentPanel).filterDataByState(text);
            topBarPanel.switchToBackBar(currentPanel);
        }

        if (currentPanel instanceof DoctorPanel)
        {
            ((DoctorPanel)currentPanel).filterDataByHospitalName(text);
            topBarPanel.switchToBackBar(currentPanel);
        }
        else if (currentPanel instanceof HospitalPanel)
        {
            ((HospitalPanel)currentPanel).filterDataByState(text);
            topBarPanel.switchToBackBar(currentPanel);
        }

    }
}