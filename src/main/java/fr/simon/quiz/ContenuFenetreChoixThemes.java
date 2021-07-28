package fr.simon.quiz;

import javax.swing.*;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ContenuFenetreChoixThemes extends JPanel implements ActionListener, FocusListener, KeyListener {
    List<JCheckBox> listeCases = new LinkedList<JCheckBox>();
    List<JRadioButton> listeFormations = new LinkedList<JRadioButton>();
    List<String> themes = new LinkedList<String>();

    private static final long serialVersionUID = 6L;
    JPanel pannelThemes = new JPanel(new GridLayout(4, 10));
    JPanel pannelFormations = new JPanel(new GridLayout(4, 5));
    JButton validation = new JButton("Valider");
    
    int modeBouton;

    public ContenuFenetreChoixThemes() {
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == validation) {
            if(modeBouton == 0) {
                for(int i = 0; i < listeCases.size(); i++) {
                    if(listeCases.get(i).isSelected())
                        modeBouton = 1;
                }
            
                for(int i = 0; i < listeFormations.size(); i++) {
                    if(listeFormations.get(i).isSelected())
                        modeBouton = 1;
                }
            }
        }
    }

    private void addFocusListenerFunction() {}
    private void addKeyListenerFunction() {}
    private void addActionListenerFunction() {}
    public void focusLost(FocusEvent e) {}
    public void keyPressed(KeyEvent e) {}
    public void focusGained(FocusEvent e) {}
    public void keyReleased(KeyEvent e) {}
    public void keyTyped(KeyEvent e) {}

    /**
     * Fonction permettant d'accéder à la liste contenant les cases à cocher pour chaque theme.
     * @return Liste contenant les cases.
     */
    public List<JCheckBox> getListeCases() {
        return listeCases;
    }

    public List<JRadioButton> getListeRadioButtons() {
        return listeFormations;
    }

    /**
     * Fonction permettant d'accéder à la valeur du bouton de validation, si aucune case n'est cochée alors la valeur est à 0 et il est impossible
     * de continuer, si au moins une case est cochée alors on peut valider la liste des thèmes choisis.
     * @return La valeur de modeBouton.
     */
    public int getValBouton() {
        return this.modeBouton;
    }

    /**
     * Fonction permettant de créer le contenu pour le choix des thèmes après avoir récupéré la liste des questions
     * et les themes associés.
     * @param nomCases : Liste de String contenant les thèmes des questions
     */
    public void creerContenuThemes(List<String> nomCases, Map<String, Integer> nbre) {
        modeBouton = 0;
        for(int i = 0; i < nomCases.size(); i++) {
            String nomCase = nomCases.get(i) + " (" + nbre.get(nomCases.get(i)) + ")";
            JCheckBox caseCocher = new JCheckBox(nomCase);
            listeCases.add(caseCocher);
        }

        for(int i = 0; i < listeCases.size(); i++) {
            pannelThemes.add(listeCases.get(i));
            listeCases.get(i).setSelected(true);
        }

        validation.setSize(new Dimension(10, 20));
        validation.setOpaque(true);
        validation.addActionListener(this);

        addFocusListenerFunction();
        addKeyListenerFunction();
        addActionListenerFunction();

        JPanel pannelPF = new JPanel(new GridLayout(1, 0));
        pannelPF.add(validation);

        setLayout(new BorderLayout());
        add(pannelThemes, BorderLayout.NORTH);
        add(pannelPF, BorderLayout.CENTER);

        //repaint();
        revalidate();
    }

    /**
     * Fonction permettant de créer le contenu pour le choix de la formation après avoir récupéré la liste des questions
     * et les formations associées.
     * @param nomCases : Liste de String contenant les formations
     */
    /*public void creerContenuFormations(List<String> nomCases) {
        modeBouton = 0;
        ButtonGroup group = new ButtonGroup();
        for(int i = 0; i < nomCases.size(); i++) {
            JRadioButton caseCocher = new JRadioButton(nomCases.get(i));
            group.add(caseCocher);
            listeFormations.add(caseCocher);
        }

        for(int i = 0; i < listeFormations.size(); i++)
            pannelFormations.add(listeFormations.get(i));
        listeFormations.get(0).setSelected(true);


        validation.setSize(new Dimension(10, 20));
        validation.setOpaque(true);
        validation.addActionListener(this);

        addFocusListenerFunction();
        addKeyListenerFunction();
        addActionListenerFunction();

        JPanel pannelPF = new JPanel(new GridLayout(1, 0));
        pannelPF.add(validation);

        setLayout(new BorderLayout());
        add(pannelFormations, BorderLayout.NORTH);
        add(pannelPF, BorderLayout.CENTER);

        //repaint();
        revalidate();
    }*/
}
