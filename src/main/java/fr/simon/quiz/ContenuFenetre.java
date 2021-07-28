package fr.simon.quiz;

import javax.swing.*;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.event.*;
import java.awt.Dimension;

class ContenuFenetre extends JPanel implements ActionListener, FocusListener, KeyListener {
    private static final long serialVersionUID = 2L;
    JLabel numQuestion = new JLabel();
    JLabel ennonceQuestion = new JLabel();

    JPanel panneauQuestion = new JPanel(new GridLayout(2, 1));
    JPanel panneauReponses = new JPanel();

    JRadioButton repA = new JRadioButton();
    JRadioButton repB = new JRadioButton();
    JRadioButton repC = new JRadioButton();
    JRadioButton repD = new JRadioButton();
    JRadioButton repIDK = new JRadioButton();
    ButtonGroup group = new ButtonGroup();

    JLabel justification = new JLabel();
    JButton validation = new JButton("Valider");
    
    private int modeBouton; // 0 = Valider / 1 = Suivant / 2 = Quitter
    private int afficherReponse = 1;
    private int afficherNombreReponse = 1;

    boolean ansA = false, ansB = false, ansC = false, ansD = false, ansIDK = false;

    public ContenuFenetre() {
        modeBouton = -1;
        numQuestion.setText("");
        numQuestion.setHorizontalAlignment(JLabel.CENTER);
        numQuestion.setVerticalAlignment(JLabel.CENTER);

        ennonceQuestion.setText("");
        ennonceQuestion.setHorizontalAlignment(JLabel.CENTER);
        ennonceQuestion.setVerticalAlignment(JLabel.CENTER);

        validation.setSize(new Dimension(10, 20));
        validation.setOpaque(true);
        validation.addActionListener(this);

        addFocusListenerFunction();
        addKeyListenerFunction();
        addActionListenerFunction();

        panneauQuestion.add(numQuestion);
        panneauQuestion.add(ennonceQuestion);

        justification.setHorizontalAlignment(JLabel.CENTER);
        justification.setVerticalAlignment(JLabel.CENTER);

        JPanel boutonValidation = new JPanel(new GridLayout(2, 0));
        boutonValidation.add(justification);
        boutonValidation.add(validation);

        JPanel panneauReponses = new JPanel(new GridLayout(3, 2));
        panneauReponses.add(repA);
        panneauReponses.add(repB);
        panneauReponses.add(repC);
        panneauReponses.add(repD);
        panneauReponses.add(repIDK);

        setLayout(new BorderLayout());
        add(panneauQuestion, BorderLayout.NORTH);
        add(panneauReponses, BorderLayout.CENTER);
        add(boutonValidation, BorderLayout.SOUTH);
    }

    private void addFocusListenerFunction() {
        repA.addFocusListener(this);
        repB.addFocusListener(this);
        repC.addFocusListener(this);
        repD.addFocusListener(this);
        repIDK.addFocusListener(this);
    }

    private void addKeyListenerFunction() {
        repA.addKeyListener(this);
        repB.addKeyListener(this);
        repC.addKeyListener(this);
        repD.addKeyListener(this);
        repIDK.addKeyListener(this);
    }

    private void addActionListenerFunction() {
        repA.addActionListener(this);
        repB.addActionListener(this);
        repC.addActionListener(this);
        repD.addActionListener(this);
        repIDK.addActionListener(this);
    }

    public void grouperBoutons() {
        group.add(repA);
        group.add(repB);
        group.add(repC);
        group.add(repD);
        //group.add(repIDK); 
    }

    public void separerBoutons() {
        group.remove(repA);
        group.remove(repB);
        group.remove(repC);
        group.remove(repD);
        //group.remove(repIDK); 
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == validation) {
            if(modeBouton == -1) { // Si le comportement du bouton est "Valider" (0 = Valider / 1 = Suivant / 2 = Quitter)
                if (repA.isSelected()) // Si la réponse A est sélectionnée alors la variable ansA passe à true pour indiquer que la réponse cochée est la réponse A
                    ansA = true;
                if (repB.isSelected())
                    ansB = true;
                if (repC.isSelected())
                    ansC = true;
                if (repD.isSelected())
                    ansD = true;
                if (repIDK.isSelected())
                    ansIDK = true;
                this.setModeBouton(1);
            } else if (modeBouton == 1){
                modeBouton = 0;
            } else if(modeBouton == 2) {
                System.exit(0);
            }

            /*
            if(modeBouton == 0) { // Si le comportement du bouton est "Valider" (0 = Valider / 1 = Suivant / 2 = Quitter)
                if (repA.isSelected()) // Si la réponse A est sélectionnée alors la variable ansA passe à true pour indiquer que la réponse cochée est la réponse A
                    ansA = true;
                else if (repB.isSelected())
                    ansB = true;
                else if (repC.isSelected())
                    ansC = true;
                else if (repD.isSelected())
                    ansD = true;
                else if (repIDK.isSelected())
                    ansIDK = true;
            }
            */
        }
    }

    public void focusLost(FocusEvent e) {}
    public void keyPressed(KeyEvent e) {}
    public void focusGained(FocusEvent e) {}
    public void keyReleased(KeyEvent e) {}
    public void keyTyped(KeyEvent e) {}

    /**
     * Fonction permettant de définir la valeur de la variable modeBouton, elle permet de définir le comportement 
     * du bouton du bas (0 = Valider / 1 = Suivant / 2 = Quitter)
     * @param val
     */
    public void setModeBouton(int val) {
        this.modeBouton = val;
    }

    /**
     * Fonction permettant d'accéder à la valeur de la variable modeBouton, elle permet de définir le comportement 
     * du bouton du bas (0 = Valider / 1 = Suivant / 2 = Quitter)
     * @return
     */
    public int getModeBouton() {
        return this.modeBouton;
    }

    /**
     * Fonction permettant de voir quelle case de réponse a été cochée.
     * @return la valeur 0 pour la réponse A, 1 pour la réponse B ... et 9 pour la réponse "Je ne sais pas".
     */
    public int getReponse() {
        int somme = 0;
        if(ansA == true) {
            ansA = false;
            somme += 16;
        }
        if (ansB == true) {
            ansB = false;
            somme += 8;
        }
        if (ansC == true) {
            ansC = false;
            somme += 4;
        }
        if (ansD == true) {
            ansD = false;
            somme += 2;
        }
        if (ansIDK == true) {
            ansIDK = false;
            somme += 1;
        }
        return somme;
    }

    /**
     * Fonction permettant de voir quelle(s) case(s) de réponse a été cochée(s). Renvoie une valeur codée sur 5 bits.
     * @return la somme des réponses cochées
     */
    public int getReponses() {
        if(this.modeBouton != -1) {
            int somme = 0;
            if(ansA == true) {
                ansA = false;
                somme += 16;
            } 
            if (ansB == true) {
                ansB = false;
                somme += 8;
            } 
            if (ansC == true) {
                ansC = false;
                somme += 4;
            } 
            if (ansD == true) {
                ansD = false;
                somme += 2;
            } 
            if (ansIDK == true) {
                ansIDK = false;
                somme += 1;
            }
            return somme;
        }
        return 0;
    }

    /**
     * Fonction permettant d'afficher la proposition dans le champ texte A, si la question ne contient pas de proposition pour cette case alors le bouton radio n'est
     * pas selectionnable.
     * @param txt : Texte à afficher
     */
    public void setTextRepA(String txt) {
        this.repA.setText(txt);
        if (txt == "")
            this.repA.setEnabled(false);
        else
            this.repA.setEnabled(true);
    }

    /**
     * Fonction permettant d'afficher la proposition dans le champ texte B, si la question ne contient pas de proposition pour cette case alors le bouton radio n'est
     * pas selectionnable.
     * @param txt : Texte à afficher
     */
    public void setTextRepB(String txt) {
        this.repB.setText(txt);
        if (txt == "")
            this.repB.setEnabled(false);
        else
            this.repB.setEnabled(true);
    }

    /**
     * Fonction permettant d'afficher la proposition dans le champ texte C, si la question ne contient pas de proposition pour cette case alors le bouton radio n'est
     * pas selectionnable.
     * @param txt : Texte à afficher
     */
    public void setTextRepC(String txt) {
        this.repC.setText(txt);
        if(txt == "")
            this.repC.setEnabled(false);
        else
            this.repC.setEnabled(true);
    }

    /**
     * Fonction permettant d'afficher la proposition dans le champ texte D, si la question ne contient pas de proposition pour cette case alors le bouton radio n'est
     * pas selectionnable.
     * @param txt : Texte à afficher
     */
    public void setTextRepD(String txt) {
        this.repD.setText(txt);
        if (txt == "")
            this.repD.setEnabled(false);
        else
            this.repD.setEnabled(true);
    }

    /**
     * Fonction permettant d'afficher le texte du champ "Je ne sais pas", si la question ne contient pas de proposition pour cette case alors le bouton radio n'est
     * pas selectionnable.
     * @param txt : Texte à afficher
     */
    public void setTextRepIDK(String txt) {
        this.repIDK.setText(txt);
        if (txt == "")
            this.repIDK.setEnabled(false);
        else
            this.repIDK.setEnabled(true);
    }

    /**
     * Fonction permettant de modifier le texte dans le bouton du bas, utilisé pour valider et passer à la question suivante.
     * @param txt : Texte à afficher
     */
    public void setTextValidation(String txt) {
        this.validation.setText(txt);
    }

    /**
     * Fonction affichant la progression du quiz via le numéro de la question et le nombre total des question (ex : 5 / 10)
     * @param txt : Texte a afficher en haut de la fenêtre
     */
    public void setTextNumQuestion(String txt) {
        this.numQuestion.setText(txt);
    }

    /**
     * Fonction permettant de définir le texte à afficher pour l'enoncé de la question
     * @param txt : Texte à afficher
     */
    public void setTextennonce(String txt) {
        this.ennonceQuestion.setText(txt);
    }

    /**
     * Fonction pas encore utilisée. Permet d'afficher une justification en cas de mauvaise réponse, justification à renseigner dans le fichier CSV.
     * @param txt : Texte de la justification
     */
    public void setJustification(String txt) {
        this.justification.setText(txt);
    }

    /** 
     * Fonction permettant de déséletionner tous les boutons, par exemple lors d'une nouvelle question.
     */
    public void unselectAllButtons() {
        repA.setSelected(false);
        repB.setSelected(false);
        repC.setSelected(false);
        repD.setSelected(false);
        repIDK.setSelected(false);
        group.clearSelection();
    }

    /**
     * Fonction renvoyant l'état de l'option Afficher Reponse dans le menu Fichier
     * @return Valeur de l'option : 0 pour ne pas afficher la reponse tout de suite, 1 pour afficher la réponse après avoir validé
     */
    public int getAfficherReponse() {
        return this.afficherReponse;
    }

    /**
     * Fonction permettant de définir la valeur de l'option
     * @param val : Nouvelle valeur de l'option
     */
    public void setAfficherReponse(int val) {
        this.afficherReponse = val;
    }

    public int getAfficherNombreReponse() {
        return this.afficherNombreReponse;
    }

    public void setAfficherNombreReponse(int val) {
        this.afficherNombreReponse = val;
    }
}