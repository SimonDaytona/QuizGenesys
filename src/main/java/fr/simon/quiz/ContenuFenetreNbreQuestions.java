package fr.simon.quiz;

import javax.swing.*;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.*;

class ContenuFenetreNbreQuestions extends JPanel implements ActionListener, FocusListener, KeyListener {
    private static final long serialVersionUID = 2L;
    JLabel nbreQuestion = new JLabel();
    JTextField reponse = new JTextField();
    JPanel pannelPF = new JPanel(new GridLayout(2, 1));
    JPanel pannelThemes = new JPanel(new GridLayout(4, 3));
    JButton validation = new JButton("Valider");
    
    int modeBouton;

    public ContenuFenetreNbreQuestions() {
        modeBouton = 0;
        nbreQuestion.setText("");
        nbreQuestion.setHorizontalAlignment(JLabel.CENTER);
        nbreQuestion.setVerticalAlignment(JLabel.CENTER);

        reponse.setPreferredSize(new Dimension(40, 20));
        reponse.setText("");

        validation.setSize(new Dimension(10, 20));
        validation.setOpaque(true);
        validation.addActionListener(this);

        addFocusListenerFunction();
        addKeyListenerFunction();
        addActionListenerFunction();

        JPanel pannelPF = new JPanel(new GridLayout(3, 0));
        pannelPF.add(nbreQuestion);
        pannelPF.add(reponse);
        pannelPF.add(validation);

        setLayout(new BorderLayout());
        add(pannelPF, BorderLayout.NORTH);
    }

    private void addFocusListenerFunction() {
        reponse.addFocusListener(this);
    }

    private void addKeyListenerFunction() {
        reponse.addKeyListener(this);
    }
    private void addActionListenerFunction() {
        reponse.addActionListener(this);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == validation) {
            if(modeBouton == 0) {
                if(reponse.getText() != "")
                    modeBouton = 1;
            }
        }
    }

    public void focusLost(FocusEvent e) {}
    public void keyPressed(KeyEvent e) {}
    public void focusGained(FocusEvent e) {}
    public void keyReleased(KeyEvent e) {}
    public void keyTyped(KeyEvent e) {}

    public void setModeBouton(int val) {
        this.modeBouton = val;
    }

    public int getModeBouton() {
        return this.modeBouton;
    }
 
    public void setTextNbreQuestion(String txt) {
        this.nbreQuestion.setText(txt);
    }

    public String getTextReponse() {
        try {
            return this.reponse.getText();
        } catch(NumberFormatException e) {
            return "99999";
        }
    }
}