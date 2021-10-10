package com.compis.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.compis.Compiler;

public class Gui implements ActionListener {
    private JFrame frame = new JFrame();
    private JPanel myPanel = new JPanel();
    private JTextArea tA1 = new JTextArea(45, 60);
    private JTextArea console = new JTextArea(45, 60);
    private JButton button = new JButton();

    public Gui() {
        myPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        // myPanel.setLayout(new GridLayout(0,1));
        button.setText("Compile");
        button.setName("compile");
        button.addActionListener(this);
        console.setText("Console: ");
        console.setBackground(Color.BLACK);
        console.setForeground(Color.GREEN);
        JScrollPane scrollpane1 = new JScrollPane(tA1);
        JScrollPane scrollpane2 = new JScrollPane(console);
        myPanel.add(scrollpane1);
        myPanel.add(button);
        myPanel.add(scrollpane2);

        frame.setResizable(false);
        frame.add(myPanel, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("DECAF COMPILER");
        frame.pack();
        frame.setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (((JButton) e.getSource()).getName().equals("compile")) {
            Compile();
        }

    }

    private void Compile() {
        Compiler compiler = new Compiler();
        String returnText = compiler.Compile(tA1.getText());
        int lenAct = returnText.length();
        int cont = 0;
        for (int i = 0; i < lenAct; i++) {
            if (cont == 120) {
                returnText = returnText.substring(0, i) + "\n" + returnText.substring(i);
                cont = 0;
            }
            cont++;
            if (returnText.charAt(i) == '\n') {
                cont = 0;
            }
            lenAct = returnText.length();
        }
        console.setText("Console:\n"+returnText);
    }
}
