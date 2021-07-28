package fr.simon.quiz;

import java.util.*;

public class Question {
    private String question;
    private List<String> reponse;
    private int nbreReponses;
    private int sommeReponses;
    private String justification;
    private List<String> propositions;
    private String formation;
    private String theme;
    private boolean jeNeSaisPas;
    
    public Question(String pQuestion, int pnbreReponses, List<String> pReponse, List<String> pPropositions){
        question = pQuestion;
        nbreReponses = pnbreReponses;
        sommeReponses = 0;
        reponse = pReponse;
        propositions = pPropositions;
        justification = "";
        formation = "";
        theme = "";
        jeNeSaisPas = false;
    }

    public Question(String pQuestion, int pnbreReponses, List<String> pReponse, List<String> pPropositions, String ptheme){
        question = pQuestion;
        nbreReponses = pnbreReponses;
        sommeReponses = 0;
        reponse = pReponse;
        propositions = pPropositions;
        justification = "";
        formation = "";
        theme = ptheme;
        jeNeSaisPas = false;
    }

    public Question(String pQuestion, int pnbreReponses, List<String> pReponse, List<String> pPropositions, String pformation, String ptheme){
        question = pQuestion;
        nbreReponses = pnbreReponses;
        sommeReponses = 0;
        reponse = pReponse;
        propositions = pPropositions;
        justification = "";
        formation = pformation;
        theme = ptheme;
        jeNeSaisPas = false;
    }

    public Question(String pQuestion, int pnbreReponses, int psommeReponses, List<String> pReponse, List<String> pPropositions, String ptheme, String pformation){
        question = pQuestion;
        nbreReponses = pnbreReponses;
        sommeReponses = psommeReponses;
        reponse = pReponse;
        propositions = pPropositions;
        justification = "";
        formation = pformation;
        theme = ptheme;
        jeNeSaisPas = false;
    }

    public String getQuestion() {
        return this.question;
    }

    public List<String> getReponse() {
        return this.reponse;
    }

    public int getNbreReponses() {
        return this.nbreReponses;
    }

    public void setNbreReponses(int val) {
        this.nbreReponses = val;
    }

    public List<String> getPropositions() {
        return this.propositions;
    }

    public String getJustification() {
        return this.justification;
    }

    public String getFormation() {
        return this.formation;
    }

    public String getTheme() {
        return this.theme;
    }

    public void setQuestion(String val) {
        this.question = val;
    }

    public void setReponse(List<String> val) {
        this.reponse = val;
    }

    public void setProposition(List<String> val) {
        this.propositions = val;
    }

    public void setJustification(String val) {
        this.justification = val;
    }

    public void setFormation(String val) {
        this.formation = val;
    }

    public void setTheme(String val) {
        this.theme = val;
    }

    /**
     * Renvoie la somme des réponses, chaque réponse a un poids comme un bit sur un octet. Ici 4 réponses possibles + 1 case "je ne sais pas" permettent de
     * créer un système sur 5 bits avec un bit représentant une réponse. (réponse A = 16 (ou 2^4), B = 8 (ou 2^3), ... , réponse "je ne sais pas" = 1 (ou 2^0))
     * @return la somme des réponses
     */
    public int getSommeReponses() {
        return this.sommeReponses;
    }

    /**
     * Permet de définir la somme des réponses, chaque réponse a un poids comme un bit sur un octet. Ici 4 réponses possibles + 1 case "je ne sais pas" permettent de
     * créer un système sur 5 bits avec un bit représentant une réponse. (réponse A = 16 (ou 2^4), B = 8 (ou 2^3), ... , réponse "je ne sais pas" = 1 (ou 2^0))
     */
    public void setSommeReponses(int val) {
        this.sommeReponses = val;
    }

    public boolean getJeNeSaisPas() {
        return this.jeNeSaisPas;
    }

    public void setJeNeSaisPas(boolean jeNeSaisPas) {
        this.jeNeSaisPas = jeNeSaisPas;
    }

}