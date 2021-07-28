package fr.simon.quiz;

import javax.swing.*;

import java.awt.Dimension;
import java.awt.event.*;

import javax.swing.event.*;

public class FenetreNbreQuestions extends JFrame implements MenuListener, ActionListener {
    private static final long serialVersionUID = 1L;
    JPanel panneauMenu = new JPanel();
    JMenuBar barreMenu;
    JMenu menuFichier;
    JMenuItem quitter;
    //int nbreThemes = 0;
    ContenuFenetreNbreQuestions cf = new ContenuFenetreNbreQuestions();
    
    public FenetreNbreQuestions(int x, int y) {
        int largeur = 600;
        int hauteur = 400;
        setContentPane(cf);
        setLocation(x - largeur / 2, y - hauteur / 2);
        setTitle("Quiz Genesys");
        setJMenuBar(barreMenu);
        setVisible(true);
        setResizable(true);
        setPreferredSize(new Dimension(largeur, hauteur));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        barreMenu = new JMenuBar();
        setJMenuBar(barreMenu);

        menuFichier = new JMenu("Fichier");
        barreMenu.add(menuFichier);

        // Option "Quitter" du menu Fichier
        quitter = new JMenuItem("Quitter");
        quitter.addActionListener(this);
        menuFichier.add(quitter);

        pack();
    }

    public int getValBouton() {
        return cf.getModeBouton();
    }

    public String getNbreQuestions() {
        return cf.getTextReponse();
    }

    public void menuCanceled(MenuEvent e) {}
    public void menuDeselected(MenuEvent e) {}
    public void menuSelected(MenuEvent e) {}
    
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == quitter)
            System.exit(0);
    }

    public void setTextNbreQuestion(String txt) {
        cf.setTextNbreQuestion(txt);
    }
}