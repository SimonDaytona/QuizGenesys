package fr.simon.quiz;

import java.util.*;

import com.mysql.cj.xdevapi.Statement;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;

public class Quiz {
    final static int tempsThread_ms = 10;
    static String cheminDossierCSV;
    static String nomFichierCSV;
    static String cheminFichierRecapitulatif;

    public static void main(String[] args) {
        Thread t = new Thread();
        t.start();

        boolean debug = false;

        // Récupérer le chemin du projet pour l'utiliser plus tard
        String curDir = System.getProperty("user.dir").replace("\\", "/");
        String CheminProjet = "";

        if(!debug)
            CheminProjet = curDir.substring(0, curDir.lastIndexOf("/"));
        else
            CheminProjet = curDir; // Pour le déboguage

        try {
            FileInputStream fichierConfig = new FileInputStream(CheminProjet + "/Ressources/config.properties");
            Properties prop = new Properties();
            prop.load(fichierConfig);
            fichierConfig.close();

            cheminDossierCSV = CheminProjet + "/Ressources/";
            nomFichierCSV = prop.getProperty("nomFichierCSV");
            cheminFichierRecapitulatif = prop.getProperty("cheminFichierRecapitulatif");
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<Question> listeQuestions = new LinkedList<Question>();
        List<Question> erreurs = new LinkedList<Question>();
        Map<String, Integer> themes = new HashMap<String, Integer>();
        Map<String, Integer> nbreQuestionsThemes = new HashMap<String, Integer>();
        int score = 0, totalQuestions = 0;

        String csvFile = cheminDossierCSV + nomFichierCSV;
        String separateur = ";";
        int erreur = lireFichierCSV(listeQuestions, csvFile, separateur);
        if(erreur == 1) {
            System.out.println("Impossible de trouver le fichier CSV ! Vérfier que le chemin d'accès ou son nom est correct dans le fichier de config");
            System.exit(0);
        } 

        /*
         *   FENETRE LISTANT LES FORMATIONS 
         */

        // Récupérer toutes les formations
        List<String> listeFormations = new LinkedList<String>();
        for(int i = 0; i < listeQuestions.size(); i++) {
            if(!listeFormations.contains(listeQuestions.get(i).getFormation()))
                listeFormations.add(listeQuestions.get(i).getFormation());
        }

        listeFormations.sort(String.CASE_INSENSITIVE_ORDER);

        FenetreChoixFormation fenetreFormations = new FenetreChoixFormation();
        fenetreFormations.creerContenuFormations(listeFormations);

        // Thread permettant d'attendre que l'utilisateur clique sur le bouton sans figer la fenêtre
        while (fenetreFormations.getValBouton() == 0) {
            try {
                Thread.sleep(tempsThread_ms);
                if(fenetreFormations.getValBouton() == 1)
                    break;
            } catch (InterruptedException e) {
            }
        }

        List<String> formationChoisie = new LinkedList<String>();

        formationChoisie = fenetreFormations.getFormationsChoisies();

        // Ne garder que les questions de la formation choisie
        for(int i = 0; i < listeQuestions.size(); i++) {
            if(!formationChoisie.contains(listeQuestions.get(i).getFormation()))
                listeQuestions.remove(i--); // i-- afin de ne pas sauter une question lorsqu'on retire une question étant donné que tous les rangs décrémentent de 1
        }

        /* 
         *   FENETRE LISTANT LES THEMES
         */

        // Récupérer tous les thèmes
        List<String> listeThemes = new LinkedList<String>();
        for(int i = 0; i < listeQuestions.size(); i++) {
            if(!listeThemes.contains(listeQuestions.get(i).getTheme()))
                listeThemes.add(listeQuestions.get(i).getTheme());
        }

        /* Compter le nombre de questions */
        for(int i = 0; i < listeQuestions.size(); i++) {
            //nbreQuestionsThemes 
            String theme = listeQuestions.get(i).getTheme();
            if(nbreQuestionsThemes.containsKey(theme)) {
                int nbre = nbreQuestionsThemes.get(theme);
                nbreQuestionsThemes.put(theme, ++nbre);
            } else {
                nbreQuestionsThemes.put(theme, 1);
            }
            
        }

        // On mémorise les coordonnées du centre de la fenêtre pour que la suivante apparaisse au même endroit
        int x = fenetreFormations.getLocation().x + fenetreFormations.getSize().width / 2;
        int y = fenetreFormations.getLocation().y + fenetreFormations.getSize().height / 2;
        
        // Détrire la fenêtre des thèmes
        fenetreFormations.setVisible(false);
        System.gc();

        FenetreChoixThemes fenetreThemes = new FenetreChoixThemes(x, y);
        fenetreThemes.creerContenuThemes(listeThemes, nbreQuestionsThemes);

        // Thread permettant d'attendre que l'utilisateur clique sur le bouton sans figer la fenêtre
        while (fenetreThemes.getValBouton() == 0) {
            try {
                Thread.sleep(tempsThread_ms);
                if(fenetreThemes.getValBouton() == 1)
                    break;
            } catch (InterruptedException e) {
            }
        }

        List<String> themesChoisis = new LinkedList<String>();
        themesChoisis = fenetreThemes.getThemesChoisis();

        // On mémorise les coordonnées du centre de la fenêtre pour que la suivante apparaisse au même endroit
        x = fenetreThemes.getLocation().x + fenetreThemes.getSize().width / 2;
        y = fenetreThemes.getLocation().y + fenetreThemes.getSize().height / 2;
        
        // Détrire la fenêtre des thèmes
        fenetreThemes.setVisible(false);
        System.gc();

        //Retirer le nombre de questions dans le theme
        for(int i = 0; i < themesChoisis.size(); i++) {
            String theme = themesChoisis.get(0);
            themesChoisis.remove(0);
            theme = theme.substring(0, theme.lastIndexOf("(") - 1);
            themesChoisis.add(theme);
        }

        //Retirer les questions dont le thème n'est pas choisit
        for(int i = 0; i < listeQuestions.size(); i++) {
            if(!themesChoisis.contains(listeQuestions.get(i).getTheme()))
                listeQuestions.remove(i--); // i-- afin de ne pas sauter une question lorsqu'on retire une question étant donné que tous les rangs décrémentent de 1
        }

        /*
         *   FENETRE INDIQUANT LE NOMBRE DE QUESTIONS DISPONIBLES
         */

        FenetreNbreQuestions fenetreNbreQuestions = new FenetreNbreQuestions(x, y);
        fenetreNbreQuestions.setTextNbreQuestion("Choisissez le nombre de questions (" + listeQuestions.size() + " questions disponibles)");

        // Thread permettant d'attendre que l'utilisateur clique sur le bouton sans figer la fenêtre
        while (fenetreNbreQuestions.getValBouton() == 0) {
            try {
                Thread.sleep(tempsThread_ms);
                if(fenetreNbreQuestions.getValBouton() == 1)
                    break;
            } catch (InterruptedException e) {
            }
        }

        int nbreQuestions;
        try {
            nbreQuestions = (Integer.parseInt(fenetreNbreQuestions.getNbreQuestions()) > listeQuestions.size()) ? listeQuestions.size() : Integer.parseInt(fenetreNbreQuestions.getNbreQuestions());
        } catch(NumberFormatException e) {
            nbreQuestions = listeQuestions.size();
        }

        // On mémorise les coordonnées du centre de la fenêtre pour que la suivante apparaisse au même endroit
        x = fenetreNbreQuestions.getLocation().x + fenetreNbreQuestions.getSize().width / 2;
        y = fenetreNbreQuestions.getLocation().y + fenetreNbreQuestions.getSize().height / 2;

        // Détrire la première fenêtre
        fenetreNbreQuestions.setVisible(false);
        System.gc();

        /*
         *   FENETRE DES QUESTIONS
         */

        Collections.shuffle(listeQuestions);
        Fenetre ma_fenetre = new Fenetre(x, y);

        Chrono chrono = new Chrono();
        chrono.start();

        for(int i = 0; i < nbreQuestions; i++) {
            ma_fenetre.unselectAllButtons();
            ma_fenetre.updateContent( (++totalQuestions + " / " + nbreQuestions), listeQuestions, i, "");
            
            while(ma_fenetre.getModeBouton() == -1) {
                try {
                    Thread.sleep(tempsThread_ms);
                } catch(InterruptedException e) {
                }
            }
           
            int pointQuestion = verifierReponses(ma_fenetre, listeQuestions, erreurs, i, ma_fenetre.getReponses());
            String themeQuestion = listeQuestions.get(i).getTheme();

            score += pointQuestion;

            if(themes.containsKey(themeQuestion)) { // On incrémente le nombre de questions sur le theme pour les stats du fichier final
                Object valTheme = themes.get(themeQuestion);
                Object valThemeTotal = themes.get("Total" + themeQuestion);
                int ptTheme = Integer.parseInt(valTheme.toString());
                int totalTheme = Integer.parseInt(valThemeTotal.toString());
                themes.put(themeQuestion, ptTheme + pointQuestion);
                themes.put("Total" + themeQuestion, totalTheme + 1);
            } else {                                // Si le thème n'existe pas encore on le crée et initialise le nombre de questions de ce thèmes sur 1
                themes.put(themeQuestion, pointQuestion);
                themes.put("Total" + themeQuestion, 1);
            }

            if(ma_fenetre.getAfficherReponse() == 1) {
                ma_fenetre.setTextValidation("Suivant");
                ma_fenetre.setModeBouton(1);
                while(ma_fenetre.getModeBouton() == 1) {
                    try {
                        Thread.sleep(tempsThread_ms);
                        if(ma_fenetre.getModeBouton() == 0)
                            break;
                    } catch (InterruptedException e) {
                    }
                }
                ma_fenetre.setTextValidation("Valider");
                ma_fenetre.setModeBouton(-1);
            } else {
                ma_fenetre.setTextValidation("Valider");
                ma_fenetre.setModeBouton(-1);
            }
        }
        chrono.stop();

        ma_fenetre.afficherScore((score * 100 / totalQuestions) + "%", chrono);

        ecrireFicher(erreurs, score, totalQuestions, cheminFichierRecapitulatif, themes, chrono);

        ma_fenetre.setModeBouton(2);
        ma_fenetre.setTextValidation("Quitter");
    }

    /**
     * Fonction permettant de créer un objet question.
     * @param liste : Indique la liste où l'objet Question sera ajouté
     * @param pQuestion : Ennoncé de la question
     * @param pReponse : Réponse de la question
     * @param pPropositions : Propositions pour le QCM
     * @param theme : Theme de la question
     */
    public static void creerQuestion(List<Question> liste, int pNbreReponses, int somme, String pQuestion, List<String> pReponse, List<String> pPropositions, String ptheme, String pformation) {
        liste.add(new Question(pQuestion, pNbreReponses, somme, pReponse, pPropositions, ptheme, pformation));
    }

    /**
     * Fonction permattant de vérifier une réponse à une question
     * @param fenetre : Objet fenêtre de la fenêtre affichée (Permet de modifier les différents champs)
     * @param liste : Liste contenant les questions
     * @param erreurs : Liste contenant les erreurs commises qui seront résumées à la fin du test dans un fichier texte
     * @param num : Nombre qui permet de choisir une question parmi la liste de questions
     * @param sommeReponsesUtilisateur : Numéro de la réponse donnée par l'utilisateur
     * @return : Renvoie 0 si l'utilisateur a la mauvaison réponse et 1 si l'utilisateur a la bonne réponse
     */
    public static int verifierReponses(Fenetre fenetre, List<Question> liste, List<Question> erreurs, int num, int sommeReponsesUtilisateur) {
        boolean saveRep = false;
        if(sommeReponsesUtilisateur % 2 == 1) { // Si on a coché "je ne sais pas" on écrira la réponse dans le rapport même si la réponse était bonne. Le poids des autres réponses étant pair, si le score des réponse est impaire c'est que la case "je ne sais pas" a été cochée
            saveRep = true;
            sommeReponsesUtilisateur--;
        }
        if(sommeReponsesUtilisateur == liste.get(num).getSommeReponses()) { // Bonne(s) réponse(s)
            if(fenetre.getAfficherReponse() == 1)
                fenetre.setJustification("Correct !");
            if(saveRep) {
                liste.get(num).setJeNeSaisPas(true); // On indique que l'utilisateur a coché "je ne sais pas"
                erreurs.add(liste.get(num));
            }
            return 1;
        } else { // Mauvaises réponses
            if(fenetre.getAfficherReponse() == 1) {
                if(liste.get(num).getNbreReponses() == 1)
                    fenetre.setJustification("La réponse était : " + ((Question) liste.get(num)).getReponse());
                else {
                    String justification = "Les réponses étaient : ";
                    List<String> reponses = liste.get(num).getReponse();
                    for(int i = 0; i < liste.get(num).getNbreReponses(); i++)
                        justification += reponses.get(i) + " / ";
                    fenetre.setJustification(justification.substring(0, justification.length() - 3));
                }
            }
            if(saveRep)
                liste.get(num).setJeNeSaisPas(true); // On indique que l'utilisateur a coché "je ne sais pas"
            erreurs.add(liste.get(num));
            return 0;
        }
    }


    /**
     * Cette fonction permet de lire un fichier .csv contenant la liste des questions et les stocker dans une liste d'objets Question
     * @param liste : Liste contenant tous les objets question
     * @param csvFile : Chemin du fichier .csv
     * @param separateur : Séparateur utilisé par le fichier .csv
     * @return : Renvoie 0 si tout s'est bien passé, 1 si la fonction ne parvient pas à accéder au fichier CSV.
     */
    public static int lireFichierCSV(List<Question> liste, String csvFile, String separateur) {
        BufferedReader br = null;
        String line = "";
        String formation = csvFile.replace(cheminDossierCSV, "");
        formation = formation.replace(".csv", "");
        try {
            br = new BufferedReader(new FileReader(csvFile));
            line = br.readLine(); // Ne prend pas en compte la première ligne (titre des cases)
            while ((line = br.readLine()) != null) {
                List<String> listePropositions = new LinkedList<String>();
                List<String> listeReponses = new LinkedList<String>();
                String[] val = line.split(separateur);

                /*
                    val[0] : Ennoncé
                    val[1] : Nbre bonnes reponses
                    val[2] : A
                    val[3] : B
                    val[4] : C
                    val[5] : D
                    val[6] : Justification
                    val[7] : Theme
                    val[8] : formation
                */

                /* if(Integer.parseInt(val[1]) > 1) 
                    val[0] += " (" + Integer.parseInt(val[1]) + " réponses)"; */
                
                // Récupérer les bonnes réponses
                for(int i = 2; i < 2 + Integer.parseInt(val[1]); i++)
                    listeReponses.add(val[i]);

                for(int i = 2; i < val.length - 2; i++) {
                    if(val[i] != "")
                        listePropositions.add(val[i]);
                }
                Collections.shuffle(listePropositions);

                // Récupérer la somme des réponses
                int somme = 0;
                for(int i = 0; i < listePropositions.size(); i++) {
                    for(int j = 0; j < listeReponses.size(); j++) {
                        if(listePropositions.get(i).equals(listeReponses.get(j))) {
                            if(i == 0)
                                somme += 16;
                            else if(i == 1)
                                somme += 8;
                            else if(i == 2)
                                somme += 4;
                            else if(i == 3)
                                somme += 2;
                        }
                    }
                }

                //String theme = val[val.length - 1];
                creerQuestion(liste, Integer.parseInt(val[1]), somme, val[0], listeReponses, listePropositions, val[7], val[8]);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return 1;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return 0;
    }

    /**
     * Cette methode permet d'écrire les questions où l'utilistaeur a eu faux pour qu'il ait une trace.
     * @param erreurs : Liste contenant les erreurs commises par l'utilisateur
     * @param score : Score de l'utilisteur
     * @param totalQuestions : Nombre total de questions posées
     * @param cheminFichierRecapitulatif : Chemin du fichier où seront notées les erreurs
     */
    public static void ecrireFicher(List<Question> erreurs, int score, int totalQuestions, String cheminFichierRecapitulatif, Map<String, Integer> themes, Chrono chrono) {
        LocalDateTime date = LocalDateTime.now();
        String nomFichier = "Rapport-" + date.getYear() + "-" + date.getMonthValue() + "-" + date.getDayOfMonth() + "_" 
                                       + date.getHour() + "-" + date.getMinute() + "-" + date.getSecond() + ".txt";
        File fichierRecapitulatif = new File(cheminFichierRecapitulatif + nomFichier);
        
        if(!fichierRecapitulatif.exists()) {
            try {
                fichierRecapitulatif.createNewFile();
            } catch(IOException e) {
                e.printStackTrace();
            }
        }

        try {
            FileWriter writer = new FileWriter(fichierRecapitulatif);
            BufferedWriter bw = new BufferedWriter(writer);

            ecritureLigne(bw, "Score = " + score + " / " + totalQuestions + " (" + (score * 100 / totalQuestions) + " %)");
            ecritureLigne(bw, "Durée : " + chrono.getDureeTxt() + "\n");
            ecritureLigne(bw, "-------------------------------------------------------------------");
            ecritureLigne(bw, "                         SCORE PAR THEMES");
            ecritureLigne(bw, "-------------------------------------------------------------------\n");

            Map<String, Integer> themesTrie = new TreeMap<>(themes); // Liste triée pour une meilleure lisibilité du fichier récapitulatif
            
            for (Map.Entry<String, Integer> entry : themesTrie.entrySet()) {
                if(!entry.getKey().contains("Total")) {
                    int scoreTheme = entry.getValue();
                    int totalQuest = themesTrie.get("Total" + entry.getKey());
                    ecritureLigne(bw, entry.getKey() + " : " + (scoreTheme * 100 / totalQuest) + "%  (" + scoreTheme + " / " + totalQuest + ")");
                }
            }

            // Créer 2 listes
            List<Question> err = new LinkedList<>();
            List<Question> idk = new LinkedList<>();

            for(int i = 0; i < erreurs.size(); i++) {
                if(erreurs.get(i).getJeNeSaisPas()) { // Erreur
                    err.add(erreurs.get(i));
                } else { // Je ne sais pas
                    idk.add(erreurs.get(i));
                }
            }

            // Liste des erreurs
            if(err.size() != 0) {
                ecritureLigne(bw, "");
                ecritureLigne(bw, "-------------------------------------------------------------------");
                ecritureLigne(bw, "                        LISTES DES ERREURS");
                ecritureLigne(bw, "-------------------------------------------------------------------\n");
            
                for(int i = 0; i < err.size(); i++) {
                    if(i > 0 || i < err.size() - 1)
                        ecritureLigne(bw, "-------------------------------------------------------------------");
                    ecritureLigne(bw, err.get(i).getQuestion());
                    List<String> rep;
                    rep = err.get(i).getReponse();
                    String reponses = rep.get(0);
                    for(int j = 1; j < rep.size(); j++)
                        reponses += " / " + rep.get(j);
                    String justif = err.get(i).getJustification();
                    if(justif != "")
                        reponses += ("\n" + justif);
                    ecritureLigne(bw, reponses);
                    //ecritureLigne(bw, "\n");
                }
            }

            // Je ne sais pas
            if(idk.size() != 0) {
                ecritureLigne(bw, "");
                ecritureLigne(bw, "-------------------------------------------------------------------");
                ecritureLigne(bw, "             LISTES DES QUESTIONS \"JE NE SAIS PAS\"");
                ecritureLigne(bw, "-------------------------------------------------------------------\n");
            
                for(int i = 0; i < idk.size(); i++) {
                    if(i > 0 || i < idk.size() - 1)
                        ecritureLigne(bw, "-------------------------------------------------------------------");
                    ecritureLigne(bw, idk.get(i).getQuestion());
                    List<String> rep;
                    rep = idk.get(i).getReponse();
                    String reponses = rep.get(0);
                    for(int j = 1; j < rep.size(); j++)
                        reponses += " / " + rep.get(j);
                    String justif = idk.get(i).getJustification();
                    if(justif != "")
                        reponses += ("\n" + justif);
                    ecritureLigne(bw, reponses);
                    //ecritureLigne(bw, "\n");
                }
            }

            bw.close();
            writer.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Fonction permettant d'écrire une ligne dans un fichier
     * @param bw
     * @param txt
     */
    public static void ecritureLigne(BufferedWriter bw, String txt) {
        try{
            bw.write(txt);
            bw.newLine();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}