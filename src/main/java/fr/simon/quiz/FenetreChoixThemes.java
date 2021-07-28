package fr.simon.quiz;

import javax.swing.*;

import java.awt.Dimension;
import java.awt.event.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.event.*;

public class FenetreChoixThemes extends JFrame implements MenuListener, ActionListener {
    private static final long serialVersionUID = 5L;
    JPanel panneauMenu = new JPanel();
    JMenuBar barreMenu;
    JMenu menuFichier;
    JMenuItem quitter;
    ContenuFenetreChoixThemes cf = new ContenuFenetreChoixThemes();

    public FenetreChoixThemes(int x, int y) {
        int largeur = 600;
        int hauteur = 200;
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

        // Quitter
        quitter = new JMenuItem("Quitter");
        quitter.addActionListener(this);
        menuFichier.add(quitter);

        pack();
    }

    /**
     * Fonction permettant d'accéder à la valeur du bouton de validation, si aucune case n'est cochée alors la valeur est à 0 et il est impossible
     * de continuer, si au moins une case est cochée alors on peut valider la liste des thèmes choisis.
     * @return La valeur de modeBouton.
     */
    public int getValBouton() {
        return cf.getValBouton();
    }

    public void menuCanceled(MenuEvent e) {}
    public void menuDeselected(MenuEvent e) {}
    public void menuSelected(MenuEvent e) {}
    
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == quitter)
            System.exit(0);
    }

    /**
     * Fonction permettant d'obtenir les cases de thème qui sont cochées afin de faire une liste des thèmes choisis par l'utilisateur.
     * @return la liste des thèmes choisis par l'utilisateur.
     */
    public List<String> getThemesChoisis() {
        List<String> themesChoisis = new LinkedList<String>();
        List<JCheckBox> listeCases = cf.getListeCases();
        for(int i = 0; i < listeCases.size(); i++) {
            if(listeCases.get(i).isSelected())
                themesChoisis.add(listeCases.get(i).getText());
        }
        return themesChoisis;
    }

    public List<String> getFormationsChoisies() {
        List<String> formationsChoisies = new LinkedList<String>();
        List<JRadioButton> listeCases = cf.getListeRadioButtons();
        for(int i = 0; i < listeCases.size(); i++) {
            if(listeCases.get(i).isSelected())
                formationsChoisies.add(listeCases.get(i).getText());
        }
        return formationsChoisies;
    }

    /**
     * Fonction permettant de créer le contenu pour le choix des thèmes après avoir récupéré la liste des questions
     * et les themes associés.
     * @param nomCases : Liste de String contenant les thèmes des questions
     */
    public void creerContenuThemes(List<String> nomCases, Map<String, Integer> nbre) {
        cf.creerContenuThemes(nomCases, nbre);
    }

    /**
     * Fonction permettant de créer le contenu pour le choix de la formation après avoir récupéré la liste des questions
     * et les themes associés.
     * @param nomCases : Liste de String contenant les thèmes des questions
     */
    /*public void creerContenuFormations(List<String> nomCases) {
        cf.creerContenuFormations(nomCases);
    }*/
}
