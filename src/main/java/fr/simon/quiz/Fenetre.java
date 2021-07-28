package fr.simon.quiz;

import javax.swing.*;

import java.awt.Dimension;
import java.awt.event.*;
import java.util.List;

import javax.swing.event.*;

public class Fenetre extends JFrame implements MenuListener, ActionListener {
    private static final long serialVersionUID = 1L;
    JPanel panneauMenu = new JPanel();
    JMenuBar barreMenu;
    JMenu menuFichier;
    JCheckBoxMenuItem afficherReponse;
    JCheckBoxMenuItem afficherNombreReponses;
    JMenuItem quitter;
    ContenuFenetre cf = new ContenuFenetre();
    
    public Fenetre(int x, int y) {
        int largeur = 800;
        int hauteur = 300;
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

        //Afficher reponse
        afficherReponse = new JCheckBoxMenuItem("Afficher la reponse");
        afficherReponse.setState(true);
        afficherReponse.addActionListener(this);
        menuFichier.add(afficherReponse);

        // Afficher nombre de réponses
        afficherNombreReponses = new JCheckBoxMenuItem("Afficher le nombre de reponses");
        afficherNombreReponses.setState(false);
        afficherNombreReponses.addActionListener(this);
        menuFichier.add(afficherNombreReponses);

        // Quitter
        quitter = new JMenuItem("Quitter");
        quitter.addActionListener(this);
        menuFichier.add(quitter);

        pack();
    }

    /**
     * Methode permettant d'afficher le score de l'utilisateur une fois le QCM terminé
     * @param score : Chaine de caractère contenant le score
     */
    public void afficherScore(String score, Chrono chrono) {
        cf.setTextNumQuestion("Terminé ! Temps : " + chrono.getDureeTxt());
        cf.setTextennonce("Votre Score " + score);
        cf.setTextRepA("");
        cf.setTextRepB("");
        cf.setTextRepC("");
        cf.setTextRepD("");
        cf.setTextRepIDK("");
        cf.setJustification("Retrouvez vos erreurs dans le fichier texte");
    }

    /**
     * Methode permettant de mettre à jour l'affichage lorsqu'une nouvelle question est affichée
     * @param numQuestion : Affiche le numéro de la question dans la liste des quesitons à poser (ex : 1/5)
     * @param listeQuestions : Liste contenant tous les objets Question
     * @param numeroQuesiton : Numéro du rang de la question dans la liste des objets Question
     * @param just : Texte à affichier pour une justification au dessus du bouton de validation
     */
    public void updateContent(String numQuestion, List<Question> listeQuestions, int numeroQuesiton, String just) {
        cf.setTextNumQuestion(numQuestion);
        String ennonceQuestion = listeQuestions.get(numeroQuesiton).getQuestion();
        int nbreRep = listeQuestions.get(numeroQuesiton).getNbreReponses();
        
        /* if(getAfficherNombreReponse() == 1 && nbreRep == 1)
            ennonceQuestion += " (" + nbreRep + " réponse)" ;
        else if(getAfficherNombreReponse() == 1)
            ennonceQuestion += " (" + nbreRep + " réponses)" ; */
        if(getAfficherNombreReponse() == 1 && nbreRep > 1)
            ennonceQuestion += " (" + nbreRep + " réponses)" ;
        

        cf.setTextennonce(ennonceQuestion);

        if(nbreRep == 1 && getAfficherNombreReponse() == 1)
            cf.grouperBoutons();
        else
            cf.separerBoutons();

        int nbrePropositions = listeQuestions.get(numeroQuesiton).getPropositions().size();
        if(nbrePropositions == 2) {
            cf.setTextRepA(listeQuestions.get(numeroQuesiton).getPropositions().get(0));
            cf.setTextRepB(listeQuestions.get(numeroQuesiton).getPropositions().get(1));
            cf.setTextRepC("");
            cf.setTextRepD("");
        } else if (nbrePropositions == 3) {
            cf.setTextRepA(listeQuestions.get(numeroQuesiton).getPropositions().get(0));
            cf.setTextRepB(listeQuestions.get(numeroQuesiton).getPropositions().get(1));
            cf.setTextRepC(listeQuestions.get(numeroQuesiton).getPropositions().get(2));
            cf.setTextRepD("");
        } else if (nbrePropositions == 4) {
            cf.setTextRepA(listeQuestions.get(numeroQuesiton).getPropositions().get(0));
            cf.setTextRepB(listeQuestions.get(numeroQuesiton).getPropositions().get(1));
            cf.setTextRepC(listeQuestions.get(numeroQuesiton).getPropositions().get(2));
            cf.setTextRepD(listeQuestions.get(numeroQuesiton).getPropositions().get(3));
        }
        cf.setTextRepIDK("Je ne sais pas");
        cf.setJustification(just);
    }

    /**
     * Methode permettant de récupérer la réponse de l'utilisateur en vérifiant quel bouton a été coché
     * @return : Renvoie le numéro du bouton (0, 1, 2 ou 3)
     */
    public int getReponse() {
        return cf.getReponse();
    }

    public int getReponses() {
        return cf.getReponses();
    }

    /**
     * Methode détectant quand l'utilisateur clique sur quitter dans le menu fichier afin de fermer l'application
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == quitter)
            System.exit(0);
        else if(e.getSource() == afficherReponse) {
            if(afficherReponse.getState() == true)
                cf.setAfficherReponse(1);
            else
                cf.setAfficherReponse(0);
        } else if(e.getSource() == afficherNombreReponses) {
            if(afficherNombreReponses.getState() == true)
                cf.setAfficherNombreReponse(1);
            else
                cf.setAfficherNombreReponse(0);
        }
    }

    /**
     * Methode permettant de modifier le champ justification au dessus du bouton de validation
     * @param txt : Texte à afficher
     */
    public void setJustification(String txt) {
        cf.setJustification(txt);
    }

    /**
     * Methode permettant de modifier le comportement du bouton 
     * @param val : Valeur du bouton (0 = Valider une réponse, 1 = Passer à la question suivante et 2 = quitter l'application à la fin du QCM)
     */
    public void setModeBouton(int val) {
        cf.setModeBouton(val);
    }

    /**
     * Methode renvoyant la valeur du bouton
     * @return Valeur du bouton (0 = Valider une réponse, 1 = Passer à la question suivante et 2 = quitter l'application à la fin du QCM)
     */
    public int getModeBouton() {
        return cf.getModeBouton();
    }

    /**
     * Methode permettant de modifier le texte sur le bouton de validation
     * @param txt : Texte à afficher
     */
    public void setTextValidation(String txt) {
        cf.validation.setText(txt);
    }

    /**
     * Méthode permettant de déselectionner tous les boutons
     */
    public void unselectAllButtons() {
        cf.unselectAllButtons();
    }

    public void menuCanceled(MenuEvent e) {}
    public void menuDeselected(MenuEvent e) {}
    public void menuSelected(MenuEvent e) {}

    /**
     * Fonction renvoyant l'état de l'option Afficher Reponse dans le menu Fichier
     * @return Valeur de l'option : 0 pour ne pas afficher la reponse tout de suite, 1 pour afficher la réponse après avoir validé
     */
    public int getAfficherReponse() {
        return cf.getAfficherReponse();
    }

    /**
     * Fonction permettant de définir la valeur de l'option
     * @param val : Nouvelle valeur de l'option
     */
    public void setAfficherReponse(int val) {
        cf.setAfficherReponse(val);
    }

    public int getAfficherNombreReponse() {
        return cf.getAfficherNombreReponse();
    }

    public void setAfficherNombreReponse(int val) {
        cf.setAfficherNombreReponse(val);
    }
}
