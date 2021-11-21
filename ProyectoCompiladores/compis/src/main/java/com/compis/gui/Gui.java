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
    private JTextArea tA1 = new JTextArea(45, 40);
    private JTextArea console = new JTextArea(45, 40);
    private JTextArea mipsVisual = new JTextArea(45, 40);
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
        console.setEditable(false);
        mipsVisual.setEditable(false);
        JScrollPane scrollpane1 = new JScrollPane(tA1);
        JScrollPane scrollpane2 = new JScrollPane(console);
        JScrollPane scrollpane3 = new JScrollPane(mipsVisual);
        myPanel.add(scrollpane1);
        myPanel.add(button);
        myPanel.add(scrollpane2);
        myPanel.add(scrollpane3);

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
        String[] returnText = compiler.Compile(tA1.getText());
        int lenAct = returnText[0].length();
        int cont = 0;
        for (int i = 0; i < lenAct; i++) {
            if (cont == 120) {
                returnText[0] = returnText[0].substring(0, i) + "\n" + returnText[0].substring(i);
                cont = 0;
            }
            cont++;
            if (returnText[0].charAt(i) == '\n') {
                cont = 0;
            }
            lenAct = returnText[0].length();
        }
        console.setText("Console:\n"+returnText[0]);
        mipsVisual.setText(returnText[1]);
    }
}
