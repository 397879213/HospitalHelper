package panels;

import controllers.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InputPanel extends JPanel {
    String[] variables;
    JTextField[] textFields;

    public InputPanel() {
        super();
    }

    // Creates inputpanel with given input labels in String array
    public InputPanel(String[] variables)
    {
        this.variables = variables;
        this.textFields = new JTextField[variables.length];

        GridLayout gl = new GridLayout(20, 2);
        setLayout(gl);

        for (int i = 0; i < variables.length; ++i)
        {
            add(new JLabel(variables[i]));

            textFields[i] = new JTextField(50);
            add(textFields[i]);
        }

        JButton saveBtn = new JButton(("Save!"));
        JButton cancelBtn = new JButton("Cancel!");

        add(saveBtn);
        add(cancelBtn);

        saveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Validate here

                Main.processData(getData());
            }
        });

        cancelBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Main.ignoreData();
            }
        });

    }

    // Create array for sending back to Main to be processed by the currentPanel
    private String[] getData() {
        String[] results = new String[variables.length];

        for (int i = 0; i < textFields.length; ++i)
        {
            results[i] = textFields[i].getText();
        }
        return results;

    }
}
