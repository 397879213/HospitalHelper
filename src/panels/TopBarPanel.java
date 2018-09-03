package panels;

import controllers.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.function.DoubleToIntFunction;

public class TopBarPanel extends JPanel {
    private static ArrayList<JButton> buttons;

    public TopBarPanel() {
        super();

        buttons = new ArrayList<JButton>();

        switchToHome();
    }

    // Switches to Patient layout of top bar
    public void switchToPatient() {
        buttons.clear();

        buttons.add(new JButton("Export Data!"));
        buttons.add(new JButton("Group By State!"));
        buttons.add(new JButton("View Doctor Data"));
        buttons.add(new JButton("View Hospital Data"));
        buttons.add(new JButton("Insert Data!"));

        buttons.get(0).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Main.exportPatientData();
            }
        });
        buttons.get(1).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Main.groupPanel();
            }
        });

        buttons.get(2).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Main.switchToDoctor();
            }
        });

        buttons.get(3).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Main.switchToHospital();
            }
        });

        buttons.get(4).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Do inserting stuff here!
                Main.getInput();
            }
        });

        reset();
    }

    // Switches to Home layout of top bar
    public void switchToHome() {
        buttons.clear();

        buttons.add(new JButton("View Patient Data"));
        buttons.add(new JButton("View Doctor Data"));
        buttons.add(new JButton("View Hospital Data"));

        buttons.get(0).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Main.switchToPatient();
            }
        });

        buttons.get(1).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Main.switchToDoctor();
            }
        });

        buttons.get(2).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Main.switchToHospital();
            }
        });

        reset();
    }

    // Switches to Doctor layout of top bar
    public void switchToDoctor() {
        buttons.clear();

        buttons.add(new JButton("Export Data!"));
        buttons.add(new JButton("View Patient Data"));
        buttons.add(new JButton("Filter to Hospital"));
        buttons.add(new JButton("View Hospital Data"));
        buttons.add(new JButton("Insert Data!"));

        buttons.get(0).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Main.exportDoctorData();
            }
        });

        buttons.get(1).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Main.switchToPatient();
            }
        });

        buttons.get(2).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Main.groupPanel();
            }
        });

        buttons.get(3).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Main.switchToHospital();
            }
        });

        buttons.get(4).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Do inserting stuff here!
                Main.getInput();
            }
        });

        reset();
    }

    // Switches to Hospital layout of top bar
    public void switchToHospital() {
        buttons.clear();

        buttons.add(new JButton("Export Data!"));
        buttons.add(new JButton("View Patient Data"));
        buttons.add(new JButton("View Doctor Data"));
        buttons.add(new JButton("Group By State"));
        buttons.add(new JButton("Insert Data!"));


        buttons.get(0).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Main.exportHospitalData();
            }
        });

        buttons.get(1).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Main.switchToPatient();
            }
        });

        buttons.get(2).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Main.switchToDoctor();
            }
        });

        buttons.get(3).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Main.groupPanel();
            }
        });

        buttons.get(4).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Do inserting stuff here!
                Main.getInput();
            }
        });

        reset();
    }

    // Switches to layout of top bar when there is a cell selected from table
    public void switchToSelected(JPanel panel, int row, int col) {
        buttons.clear();

        buttons.add(new JButton("Back"));
        buttons.add(new JButton("Save Changes"));
        buttons.add(new JButton("Delete Row"));

        buttons.get(0).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (panel instanceof PatientPanel)
                {
                    switchToPatient();
                    ((PatientPanel) panel).refreshData();
                }
                else if (panel instanceof DoctorPanel)
                {
                    switchToDoctor();
                    ((DoctorPanel) panel).refreshData();
                }
                else if (panel instanceof HospitalPanel)
                {
                    switchToHospital();
                    ((HospitalPanel) panel).refreshData();
                }
            }
        });

        buttons.get(1).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (panel instanceof PatientPanel)
                {
                    ((PatientPanel) panel).updateData();
                    switchToPatient();
                }
                else if (panel instanceof DoctorPanel)
                {
                    ((DoctorPanel) panel).updateData();
                    switchToDoctor();
                }
                else if (panel instanceof HospitalPanel)
                {
                    ((HospitalPanel) panel).updateData();
                    switchToHospital();
                }
            }
        });

        buttons.get(2).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (panel instanceof PatientPanel)
                {
                    ((PatientPanel) panel).deleteSelectedRow();
                    switchToPatient();
                }
                else if (panel instanceof DoctorPanel)
                {
                    ((DoctorPanel) panel).deleteSelectedRow();
                    switchToDoctor();
                }
                else if (panel instanceof HospitalPanel)
                {
                    ((HospitalPanel) panel).deleteSelectedRow();
                    switchToHospital();
                }
            }
        });

        reset();
    }

    // Resets the panel in case things get weird
    public void reset() {
        this.removeAll();

        for (int i = 0; i < buttons.size(); ++i) {
            add(buttons.get(i));
        }

        repaint();
        revalidate();
    }

    // Again resets panel because Java Swing is weird
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(new Color(0, 150, 169));
        g.fillRect(0, 0, getWidth(), getHeight());
    }

    // Switches to "back" button so that user can return
    public void switchToBackBar(JPanel panel) {

        buttons.clear();

        buttons.add(new JButton("Back"));

        buttons.get(0).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (panel instanceof PatientPanel)
                {
                    Main.switchToPatient();
                }
                else if (panel instanceof DoctorPanel)
                {
                    Main.switchToDoctor();
                }
                else if (panel instanceof HospitalPanel)
                {
                    Main.switchToHospital();
                }
            }
        });

        reset();
    }

    // Allows user to filter data by something in the top bar
    public void switchToSearch(String prompt)
    {
        buttons.clear();

        this.removeAll();

        JLabel label = new JLabel(prompt);
        JTextField textField = new JTextField(50);
        JButton submit = new JButton("Filter Data");

        label.setBackground(Color.WHITE);
        submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Main.filterPanel(textField.getText());
            }
        });

        add(label);
        add(textField);
        add(submit);

        repaint();
        revalidate();
    }

    // Switches to filter bar (based on state)
    public void switchToFilterBar(JPanel panel) {

        buttons.clear();

        buttons.add(new JButton("Back"));
        buttons.add(new JButton("Filter By State"));

        buttons.get(0).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (panel instanceof PatientPanel)
                {
                    Main.switchToPatient();
                }
                else if (panel instanceof DoctorPanel)
                {
                    Main.switchToDoctor();
                }
                else if (panel instanceof HospitalPanel)
                {
                    Main.switchToHospital();
                }
            }
        });

        buttons.get(1).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switchToSearch("State: ");
            }
        });

        reset();
        
    }
}
