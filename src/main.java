// Version du 12/01/2019 21h finale
import extensions.CSVFile;

class main extends Program {
    // PARAMETRAGE DU MENU
    final String[] listeJeuxString = new String[]{"Pendu", "Pyramide de calcul", "Géographie", "Mémoire", "Quizz"};
    final int nbJeux = length(listeJeuxString);
    final int[][] listeDifficultesJeux = new int[][]{{1, 2, 3, 4, 5}, {1, 2, 3, 4, 5}, {1, 2, 3, 4, 5}, {1, 2, 3, 4, 5}, {1, 2, 3, 4, 5}};
    final int[] listeDifficultesPossibles = new int[]{1, 2, 3, 4, 5};
    final String[] typeDifficulte = new String[]{"1. Facile", "2. Normal", "3. Moyen", "4. Difficile", "5. Très difficile"};
    final int nbPlacesClassement = 5;
    final String classementFile = "Classement.csv";
    final String motsPenduFile = "MotsPendu.csv";
    final String jeuGeoFile = "jeuGeo.csv";
    final String questionQuizzFile = "QuestionsQuizz.csv";

    // PARAMETRAGE DU JEU DE MEMOIRE
    final String[] symbolesCartes = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "K"};
    final String[] symbolesCartesCouleurs = new String[]{"cyan", "red", "green", "yellow", "blue", "purple", "cyan", "red", "green", "yellow"};

    void algorithm() {
        clearScreen();
        boolean jouer = true;

        titreJeu();
        afficherMenu("Menu", ANSI_YELLOW, new String[]{"Jouer", "Quitter"}, ANSI_TEXT_DEFAULT_COLOR, ". ", new int[0], false, ANSI_TEXT_DEFAULT_COLOR);

        String choix = inputMenu("\nTon choix ?");

        if(equals(choix, "2")) {
            jouer = false;
        }

        while(jouer) {
            titreJeu();
            afficherMenu("Mode de jeu", ANSI_YELLOW, new String[]{"Mode marathon", "Mode entraînement", "Classement", "Quitter"}, ANSI_TEXT_DEFAULT_COLOR, ". ", new int[0], false, ANSI_TEXT_DEFAULT_COLOR);

            String choix2 = inputMenu("\nTon choix ?");

            if(equals(choix2, "1")) {
                marathon();
            } else if(equals(choix2, "2")) {
                entrainement();
            } else if(equals(choix2, "3")) {
                classement();
            } else if(equals(choix2, "4")) {
                jouer = false;
            } else {
                text("red");
                println("\nEntrée invalide !");
                resetTextColor();
            }
        }
    }

    void titreJeu() {
        eraseAllScreen();

        print(ANSI_BOLD + ANSI_CYAN);
        println("  __  __ _       _                                  __  __                 _   _                 ");
        println(" |  \\/  (_)     (_)                                |  \\/  |               | | | |                ");
        println(" | \\  / |_ _ __  _  __ _  __ _ _ __ ___   ___ ___  | \\  / | __ _ _ __ __ _| |_| |__   ___  _ __  ");
        println(" | |\\/| | | '_ \\| |/ _` |/ _` | '_ ` _ \\ / _ / __| | |\\/| |/ _` | '__/ _` | __| '_ \\ / _ \\| '_ \\ ");
        println(" | |  | | | | | | | (_| | (_| | | | | | |  __\\__ \\ | |  | | (_| | | | (_| | |_| | | | (_) | | | |");
        println(" |_|  |_|_|_| |_|_|\\__, |\\__,_|_| |_| |_|\\___|___/ |_|  |_|\\__,_|_|  \\__,_|\\__|_| |_|\\___/|_| |_|");
        println("                    __/ |                                                                        ");
        println("                   |___/                                                                         \n");

        resetTextColor();
    }

    int[] plusGrandTableau(String[] tableau) {
        int resultat = 0;
        int indice = 0;

        for(int i = 0; i < length(tableau); i++) {
            if(length(tableau[i]) > resultat) {
                resultat = length(tableau[i]);
                indice = i;
            }
        }

        return new int[]{resultat, indice};
    }

    String repeterChaine(String chaine, int nb) {
        String resultat = "";

        for(int i = 0; i < nb; i++) {
            resultat += chaine;
        }

        return resultat;
    }

    // Affichage d'un menu avec centrage automatique
    void afficherMenu(String titre, String couleurTitre, String[] contenu, String couleurContenu, String avantContenu, int[] filtreContenu, boolean afficherRetour, String couleurBordures) {
        print("\n");
        String affAvantContenu = "";

        if(length(avantContenu) > 0) {
            affAvantContenu = avantContenu;
        }

        int tailleTitre = length(titre) + 2;
        int plusGrand = (length(affAvantContenu) > 0 ? length("" + plusGrandTableau(contenu)[1] + affAvantContenu) : 0) + plusGrandTableau(contenu)[0] + 2;
        plusGrand = (tailleTitre > plusGrand ? tailleTitre : plusGrand) + 2;
        int gaucheTexte = 0;
        int droiteTexte = 0;
        int tailleTexte = 0;

        println(couleurBordures + "." + repeterChaine("-", plusGrand) + "." + ANSI_RESET);

        tailleTexte = tailleTitre;
        gaucheTexte = (plusGrand - tailleTexte) / 2;
        droiteTexte = plusGrand - gaucheTexte - tailleTexte;
        println(couleurBordures + "|" + repeterChaine(" ", gaucheTexte) + "" + ANSI_UNDERLINE + ANSI_BOLD + couleurTitre + titre + ANSI_RESET + ANSI_BOLD + couleurTitre + " :" + ANSI_RESET + repeterChaine(" ", droiteTexte) + couleurBordures + "|" + ANSI_RESET);

        println(couleurBordures + "|" + repeterChaine("-", plusGrand) + "|" + ANSI_RESET);

        for(int i = 0; i < length(contenu); i++) {
            if(length(filtreContenu) <= 0 || dansTableau(i + 1, filtreContenu)) {
                tailleTexte = length("" + (length(affAvantContenu) > 0 ? ("" + (i + 1) + affAvantContenu) : "") + contenu[i]);
                gaucheTexte = (plusGrand - tailleTexte) / 2;
                droiteTexte = plusGrand - gaucheTexte - tailleTexte;

                println(couleurBordures + "|" + repeterChaine(" ", gaucheTexte) + couleurContenu + (length(affAvantContenu) > 0 ? ("" + (i + 1) + affAvantContenu) : "") + contenu[i] + ANSI_RESET + repeterChaine(" ", droiteTexte) + couleurBordures + "|");
            }
        }

        if(afficherRetour) {
            tailleTexte = length("" + (length(affAvantContenu) > 0 ? ("" + (length(contenu) + 1) + affAvantContenu) : "") + "Retour");
            gaucheTexte = (plusGrand - tailleTexte) / 2;
            droiteTexte = plusGrand - gaucheTexte - tailleTexte;

            println(couleurBordures + "|" + repeterChaine(" ", gaucheTexte) + couleurContenu + (length(affAvantContenu) > 0 ? ("" + (length(contenu) + 1) + affAvantContenu) : "") + "Retour" + ANSI_RESET + repeterChaine(" ", droiteTexte) + couleurBordures + "|" + ANSI_RESET);
        }

        println(couleurBordures + "|" + repeterChaine("_", plusGrand) + "|" + ANSI_RESET);
    }

    void entrainement() {
        titreJeu();
        afficherMenu("Choix du jeu", ANSI_YELLOW, listeJeuxString, ANSI_TEXT_DEFAULT_COLOR, ". ", new int[0], true, ANSI_TEXT_DEFAULT_COLOR);

        String choix = inputMenu("\nTon choix ?");

        while(!estNombre(choix) || stringToInt(choix) < 1 || stringToInt(choix) > nbJeux + 1) {
            text("red");
            println("\nNuméro de jeu invalide !");
            resetTextColor();

            choix = inputMenu("\nTon choix ?");
        }

        if(!equals(choix, "" + (nbJeux + 1))) {
            int score = 0;
            score = lancerJeu(choix, 1, true, true);

            println("\n---------------------------------------");
            println(ANSI_CYAN + "\n> Ton score pour ce jeu est de : " + ANSI_BOLD + score + ANSI_RESET);
            println("\n---------------------------------------");

            text("cyan");
            println("\nAppuies sur Entrer pour continuer.");
            resetTextColor();

            readString();
        }
    }

    void classement() {
        titreJeu();
        CSVFile classement = loadCSV(classementFile);
        String[] classementAffichage = new String[nbPlacesClassement];

        String dernierScore = getCell(classement, 0, 1);
        int difficulte = 0;
        String difficulteString = "";
        int dernierClassement = 1;

        for(int i = 0; i < nbPlacesClassement; i++) {
            if(!equals(getCell(classement, i, 1), dernierScore)) {
                dernierClassement = dernierClassement + 1;
            }

            difficulte = stringToInt(getCell(classement, i, 2));
            difficulteString = typeDifficulte[(difficulte - 1) >= length(typeDifficulte) || (difficulte - 1) < 0 ? 1 : (difficulte - 1)];

            classementAffichage[i] = " " + dernierClassement + ". " + getCell(classement, i, 0) + " - " + getCell(classement, i, 1) + " points - Difficulté : " + substring(difficulteString, 3, length(difficulteString));

            dernierScore = "" + getCell(classement, i, 1);
        }

        afficherMenu("Classement du mode marathon", ANSI_YELLOW, classementAffichage, ANSI_TEXT_DEFAULT_COLOR, "", new int[0], false, ANSI_TEXT_DEFAULT_COLOR);

        text("cyan");
        println("\nAppuies sur Entrer pour continuer.");
        resetTextColor();

        readString();
    }

    void marathon() {
        titreJeu();

        int[] listeJeu = new int[nbJeux + 1];
        int randomJeu = 0;
        int score = 0;
        int scoreJeu = 0;

        for(int i = 0; i < length(listeJeu) - 1; i++) {
            do {
                randomJeu = (int) (1.0 + (random() * nbJeux));
            } while(dansTableau(randomJeu, listeJeu));

            listeJeu[i] = randomJeu;
        }

        listeJeu[nbJeux] = (int) (1.0 + (random() * nbJeux)); // jeu "boss"
        int difficulte = entrerNumDifficulte(listeDifficultesPossibles);
        int difficulteDebut = difficulte;

        for(int i = 0; i < length(listeJeu); i++) {
            println("\n---------------------------------------");
            println(ANSI_YELLOW + "\n> Tu t'apprêtes à jouer au jeu " + ANSI_BOLD + listeJeuxString[listeJeu[i] - 1] + ANSI_RESET);

            if(i == length(listeJeu) - 1) {
                println(ANSI_RED + ANSI_BOLD + "Attention ! Il s'agit du mini-jeu Boss, avec une difficulté plus élevée ! Si tu le réussi, tu gagneras le score du jeu doublé." + ANSI_RESET);
            }

            text("cyan");
            println("\n> Appuies sur Entrée dès que tu es prêt !");
            resetTextColor();
            readString();

            if(i == length(listeJeu) - 1) {
                difficulte = listeDifficultesPossibles[length(listeDifficultesPossibles) - 1]; // difficulté la plus élevée
            }

            scoreJeu = lancerJeu("" + listeJeu[i], difficulte, false, false);

            resetTextColor();

            println("\n---------------------------------------");
            println(ANSI_CYAN + "\n> Ton score pour ce jeu est de : " + ANSI_BOLD + scoreJeu + ANSI_RESET);

            if(i == length(listeJeu) - 1) {
                scoreJeu = scoreJeu * 2;
                println(ANSI_GREEN + "Il s'agit du mini-jeu Boss, ce score est donc doublé ! Tu obtiens donc un score de : " + ANSI_BOLD + scoreJeu + ANSI_RESET);
            }

            score += scoreJeu;
        }

        println(ANSI_CYAN + "\n> Ton score total pour cette partie est de : "  + ANSI_BOLD + score + ANSI_RESET);

        verifierClassement(score, difficulteDebut);

        text("cyan");
        println("\nAppuies sur Entrer pour continuer.");
        resetTextColor();

        readString();
    }

    // Classement - gestion
    void verifierClassement(int score, int difficulte) {
        int placeClassement = 0;
        boolean egalite = false;

        CSVFile classement = loadCSV(classementFile);

        for(int i = 0; i < nbPlacesClassement; i++) {
            if(score > stringToInt(getCell(classement, i, 1))) {
                placeClassement = i + 1;
                break;
            } else if(score == stringToInt(getCell(classement, i, 1))) {
                placeClassement = i + 1;
                egalite = true;
                break;
            }
        }

        if(placeClassement != 0) {
            if(egalite) {
                println(ANSI_GREEN + "Bravo ! Tu te places dans le classement, à la place n°"  + ANSI_BOLD + placeClassement + ANSI_RESET + ANSI_GREEN + " ex-aequo avec " + ANSI_BOLD + getCell(classement, placeClassement - 1, 0) + ANSI_RESET + ANSI_GREEN + " !" + ANSI_RESET);
            } else {
                println(ANSI_GREEN + "Bravo ! Tu te places dans le classement, à la place n°"  + ANSI_BOLD + placeClassement + ANSI_RESET + ANSI_GREEN + " !" + ANSI_RESET);
                resetTextColor();
            }

            String nom = inputMenu("\n> Entres ton prénom/nom :");

            boolean cree = false;
            String[][] classementFinal = new String[nbPlacesClassement][3];

            for(int i = 0; i < nbPlacesClassement; i++) {
                if(placeClassement == i + 1) {
                    classementFinal[i][0] = nom;
                    classementFinal[i][1] = "" + score;
                    classementFinal[i][2] = "" + difficulte;
                    cree = true;
                } else if(!cree) {
                    classementFinal[i][0] = getCell(classement, i, 0);
                    classementFinal[i][1] = getCell(classement, i, 1);
                    classementFinal[i][2] = getCell(classement, i, 2);
                } else {
                    if(i > 0) {
                        classementFinal[i][0] = getCell(classement, i - 1, 0);
                        classementFinal[i][1] = getCell(classement, i - 1, 1);
                        classementFinal[i][2] = getCell(classement, i - 1, 2);
                    } else {
                        classementFinal[i][0] = getCell(classement, i, 0);
                        classementFinal[i][1] = getCell(classement, i, 1);
                        classementFinal[i][2] = getCell(classement, i, 2);
                    }
                }
            }

            saveCSV(classementFinal, classementFile);
        }
    }

    int lancerJeu(String numJeu, int difficulte, boolean choisirDifficulte, boolean choisirTheme) {
        int score = 0;

        if(choisirDifficulte) {
            difficulte = entrerNumDifficulte(listeDifficultesJeux[stringToInt(numJeu) - 1]);
        }

        if(equals(numJeu, "1")) {
            score = jeu_pendu(difficulte, choisirTheme);
        } else if(equals(numJeu, "2")) {
            score = jeu_pyramide(difficulte);
        } else if(equals(numJeu, "3")) {
            score = jeu_geographie(difficulte);
        } else if(equals(numJeu, "4")) {
            score = jeu_memoire(difficulte);
        } else if(equals(numJeu, "5")) {
            score = jeu_quizz(difficulte, choisirTheme);
        }

        return score;
    }

    int entrerNumDifficulte(int[] nb) {
        titreJeu();
        afficherMenu("Entres un niveau de difficulté", ANSI_YELLOW, typeDifficulte, ANSI_TEXT_DEFAULT_COLOR, "", nb, false, ANSI_TEXT_DEFAULT_COLOR);

        String choix = inputMenu("\nTon choix ?");

        while(!estNombre(choix) || !dansTableau(stringToInt(choix), nb)) {
            text("red");
            println("\nChoix invalide !");

            choix = inputMenu("\nTon choix ?");
        }

        return stringToInt(choix);
    }

    String inputMenu(String aDemander) {
        text("cyan");
        println(aDemander);
        resetTextColor();

        String resultat = readString();

        while(equals(resultat, "")) {
            text("red");
            println("Valeur invalide.\n");
            resetTextColor();

            text("cyan");
            println(aDemander);
            resetTextColor();

            resultat = readString();
        }

        resetTextColor();
        return resultat;
    }

    // Réinitialise la couleur du texte
    void resetTextColor() {
        print(ANSI_RESET);
    }

    // Génère un entier compris entre min et max (exclus)
    int genereNbAleat(int min, int max) {
        return min + (int) (random() * (max - min));
    }

    boolean dansTableau(char car, char[] tableau) {
        boolean resultat = false;

        for(int i = 0; i < length(tableau); i++) {
            if(car == tableau[i]) {
                resultat = true;
            }
        }

        return resultat;
    }

    boolean dansTableau(int nb, int[] tableau) {
        boolean resultat = false;

        for(int i = 0; i < length(tableau); i++) {
            if(nb == tableau[i]) {
                resultat = true;
            }
        }

        return resultat;
    }

    boolean dansTableau(String chaine, String[] tableau) {
        boolean resultat = false;

        for(int i = 0; i < length(tableau); i++) {
            if(equals(chaine, tableau[i])) {
                resultat = true;
            }
        }

        return resultat;
    }

    boolean estNombre(String nb) {
        boolean resultat = true;

        for(int i = 0; i < length(nb); i++) {
            if(!dansTableau(charAt(nb, i), new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'})) {
                resultat = false;
            }
        }

        return resultat;
    }

    void eraseAllScreen() {
        for(int i = 0; i < 50; i++) {
            clearScreen();
        }
    }

    // Jeu pendu
    int jeu_pendu(int difficulte, boolean demanderTheme) {
        eraseAllScreen();
        CSVFile mots = loadCSV(motsPenduFile);
        int nbThemes = columnCount(mots);
        int theme = 1;

        resetTextColor();

        if(demanderTheme) {
            theme = demanderThemePendu(nbThemes);
        } else {
            do {
                theme = genereNbAleat(1, nbThemes + 1);
            } while(stringToInt(getCell(mots, 2, theme - 1)) > difficulte);
        }

        if(theme < 1 || theme > nbThemes) {
            theme = 1;
        }

        String choixDiff = "1"; // Plus facile

        if(difficulte >= 4) {
            choixDiff = "2"; // Plus difficile
        } else {
            choixDiff = "1"; // Plus facile
        }

        eraseAllScreen();
        String motRandom = motRandom(theme);
        String lettresUtilisees = "";

        int score = 0;
        Mot mot = creerMot(toLowerCase(motRandom));
        int essais = 0;
        int essaisMax = 7;
        boolean jouer = true;
        String lettreChoix = "";
        char carChoix = ' ';
        boolean gagne = false;
        int tempsMax = (length(motRandom) + (5 - (difficulte - 1))) * 3;
        long tempsDebut = getTime();
        int lettresSpecialesRetirees = 0;

        if(equals(choixDiff, "1") && length(motRandom) > 2) {
            if(equals("" + mot.leMot[0].car, "" + mot.leMot[length(mot.leMot) - 1].car)) {
                lettresUtilisees = lettresUtilisees + mot.leMot[0].car; // Si la première et la dernière lettre est la même, on ne prend qu'une seule fois cette lettre
            } else {
                lettresUtilisees = lettresUtilisees + mot.leMot[0].car + mot.leMot[length(mot.leMot) - 1].car;
            }

            for(int i = 0; i < length(mot.leMot); i++) {
                if(mot.leMot[i].car == mot.leMot[0].car || mot.leMot[i].car == mot.leMot[length(mot.leMot) - 1].car) {
                    mot.leMot[i].decouverte = true;
                }
            }

            // Si toutes les lettres sont dévoilées, aucune ne l'est finalement, ce serait trop simple...
            if(nbDecouverts(mot, false) >= length(mot.leMot)) {
                lettresUtilisees = "";

                for(int i = 0; i < length(mot.leMot); i++) {
                    mot.leMot[i].decouverte = false;
                }
            }
        }

        // Révèle tous les caractères espace et tirets
        for(int i = 0; i < length(mot.leMot); i++) {
            if(mot.leMot[i].car == ' ' || mot.leMot[i].car == '-' || mot.leMot[i].car == '_') {
                if(!dansChaine(lettresUtilisees, mot.leMot[i].car)) {
                    lettresUtilisees = lettresUtilisees + mot.leMot[i].car;
                }

                mot.leMot[i].decouverte = true;
                lettresSpecialesRetirees++;
            }
        }

        do {
            clearScreen();
            carChoix = ' ';

            text("cyan");
            println("\n" + ANSI_UNDERLINE + ANSI_BOLD + "Mot" + ANSI_RESET + ANSI_CYAN + ANSI_BOLD + " : " + ANSI_RESET + ANSI_CYAN + toString(mot));

            text("yellow");
            print(ANSI_UNDERLINE + ANSI_BOLD + "\nThème actuel" + ANSI_RESET + ANSI_BOLD + ANSI_YELLOW + " : " + ANSI_RESET + ANSI_CYAN + getCell(mots, 0, theme - 1) + "\n");
            println(ANSI_YELLOW + "Trouves le mot en moins de " + ANSI_BOLD + tempsMax + ANSI_RESET + ANSI_YELLOW + (tempsMax > 1 ? " secondes" : " seconde") + " afin d'obtenir un bonus de score !");
            println("Il te reste " + ANSI_BOLD + (essaisMax - essais) + ANSI_RESET + ANSI_YELLOW + ((essaisMax - essais) > 1 ? " essais" : " essai") + ".");
            if(length(lettresUtilisees) > 0) {
                println("Tu as utilisé " + (length(lettresUtilisees) > 1 ? "les lettres" : "la lettre") + " : " + ANSI_BOLD + lettresUtilisees);
            } else {
                println("Tu n'as encore utilisé aucune lettre.");
            }

            text("cyan");
            println(ANSI_BOLD + "> Les lettres accentuées ne sont pas prises en compte ! Il faut entrer la lettre sans accent." + ANSI_RESET);

            while(carChoix == ' ') {
                resetTextColor();
                println("\nEntres une lettre :");
                lettreChoix = readString();

                if(length(lettreChoix) > 0) {
                    carChoix = charAt(toLowerCase(lettreChoix), 0);
                }

                if(carChoix == ' ') {
                    text("red");
                    println("\n> C'est vide ! Réassayes.\n");
                }

                for(int i = 0; i < length(lettresUtilisees); i++) {
                    if(carChoix == charAt(lettresUtilisees, i)) {
                        text("red");
                        println("\n> Lettre déjà utilisée !");
                        carChoix = ' ';
                        break;
                    }
                }
            }

            if(decouvrir(mot, carChoix)) {
                for(int i = 0; i < length(mot.leMot); i++) {
                    if(mot.leMot[i].car == carChoix) {
                        mot.leMot[i].decouverte = true;
                    }
                }
            } else {
                essais++;
            }

            lettresUtilisees = lettresUtilisees + carChoix;

            if(estDecouvert(mot)) {
                jouer = false;
                gagne = true;
            } else if(essais == essaisMax) {
                jouer = false;
            }
        } while(jouer);

        if(gagne) {
            text("green");
            println("\n> Tu as gagné !");

            long timeTermine = (getTime() - tempsDebut) / 1000;
            score = (length(mot.leMot) - lettresSpecialesRetirees) * 10 * (equals(choixDiff, "2") ? 2 : 1);

            if(timeTermine <= tempsMax) { // Bonus score
                text("cyan");
                print("\nTu obtiens un bonus de score (x2) car tu as terminé le jeu en moins de " + ANSI_BOLD + tempsMax + ANSI_RESET + ANSI_CYAN + (tempsMax > 1 ? " secondes" : " seconde") + ".");
                print("\nTu l'as terminée en " + ANSI_BOLD + timeTermine + ANSI_RESET + ANSI_CYAN + (timeTermine > 1 ? " secondes" : " seconde") + ".\n");
                resetTextColor();

                score = score * 2;
            }
        } else {
            text("red");
            println("\n> Tu as perdu...");
        }

        text("cyan");
        println("\nLe mot qui devait être trouvé était : " + ANSI_BOLD + toLowerCase(motRandom) + ANSI_RESET);

        resetTextColor();

        return score;
    }

    Lettre creerLettre(char car) {
        Lettre lettre = new Lettre();
        lettre.car = car;
        lettre.decouverte = false;
        return lettre;
    }

    Mot creerMot(String mot) {
        Mot motType = new Mot();
        motType.leMot = new Lettre[length(mot)];

        for(int i = 0; i < length(mot); i++) {
            motType.leMot[i] = creerLettre(charAt(mot, i));
        }

        return motType;
    }

    String toString(Lettre lettre) {
        return lettre.decouverte ? "" + lettre.car + " " : "* ";
    }

    String toString(Mot mot) {
        String resultat = "";

        for(int i = 0; i < length(mot.leMot); i++) {
            resultat += toString(mot.leMot[i]);
        }

        return resultat;
    }

    boolean estDecouvert(Mot mot) {
        boolean resultat = true;

        for(int i = 0; i < length(mot.leMot); i++) {
            if(!mot.leMot[i].decouverte) {
                resultat = false;
            }
        }

        return resultat;
    }

    boolean decouvrir(Mot mot, char car) {
        boolean resultat = false;

        for(int i = 0; i < length(mot.leMot); i++) {
            if(mot.leMot[i].car == car) {
                resultat = true;
            }
        }

        return resultat;
    }

    int nbDecouverts(Mot mot, boolean differents) {
        int resultat = 0;
        String decouverts = "";

        for(int i = 0; i < length(mot.leMot); i++) {
            if(differents) {
                if(mot.leMot[i].decouverte && !dansChaine(decouverts, mot.leMot[i].car)) {
                    resultat++;
                    decouverts = decouverts + mot.leMot[i].car;
                }
            } else {
                if(mot.leMot[i].decouverte) {
                    resultat++;
                }
            }
        }

        return resultat;
    }

    void testDecouverts() {
        Mot mot = creerMot("test");
        mot.leMot[0].decouverte = true;
        mot.leMot[3].decouverte = true;

        assertEquals(nbDecouverts(mot, false), 2);
        assertEquals(nbDecouverts(mot, true), 1);
    }

    boolean dansChaine(String chaine, char car) {
        boolean resultat = false;

        for(int i = 0; i < length(chaine); i++) {
            if(charAt(chaine, i) == car) {
                resultat = true;
            }
        }

        return resultat;
    }

    void testDansChaine() {
        assertTrue(dansChaine("test", 'e'));
        assertFalse(dansChaine("test", 'f'));
    }

    boolean nbValide(String nb) {
        boolean resultat = true;

        if(!equals(nb, "")) {
            for(int j = 0; j < length(nb); j++) {
                if(!dansChaine("0123456789", charAt(nb, j))) {
                    resultat = false;
                }
            }
        } else {
            resultat = false;
        }

        return resultat;
    }

    int demanderThemePendu(int nbThemes) {
        CSVFile mots = loadCSV(motsPenduFile);
        String entree = "";
        String[] contenu = new String[nbThemes];

        for(int i = 0; i < nbThemes; i++) {
            contenu[i] = getCell(mots, 0, i);
        }

        afficherMenu("Thèmes", ANSI_YELLOW, contenu, ANSI_TEXT_DEFAULT_COLOR, ". ", new int[]{}, false, ANSI_TEXT_DEFAULT_COLOR);

        text("cyan");
        println("\nEntres le numéro du thème souhaité :");
        resetTextColor();

        entree = readString();

        while(equals(entree, "") || !nbValide(entree) || stringToInt(entree) < 1 || stringToInt(entree) > nbThemes) {
            text("red");
            println("\n> Entrée invalide !");
            resetTextColor();

            text("cyan");
            println("\nEntres le numéro du thème souhaité :");
            resetTextColor();

            entree = readString();
        }

        return stringToInt(entree);
    }

    String motRandom(int theme) {
        CSVFile mots = loadCSV(motsPenduFile);

        String resultat = "";
        int nbMots = stringToInt(getCell(mots, 1, theme - 1));
        int random = 0;

        while(resultat == "") {
            random = genereNbAleat(0, nbMots);
            resultat = getCell(mots, random + 3, theme - 1);
        }

        return resultat;
    }

    // Jeu de calcul (pyramide)
    int jeu_pyramide(int difficulte) {
        resetTextColor();
        eraseAllScreen();
        int[] partieGenere = genererTypePartie(difficulte);
        Pyramide pyramide = creerPyramide(new int[]{partieGenere[0], partieGenere[1]}, partieGenere[2], 10, partieGenere[3]);

        while(pyramide.nbADeviner == 0) { // pour éviter les pyramides sans nombre caché
            pyramide = initialiserPyramide(pyramide);
            pyramide = resoudrePyramide(pyramide);
            pyramide = initialiserPyramideCachee(pyramide);
        }

        int nbDevines = 0;
        int chances = pyramide.nbADeviner * 2;
        long timeStart = getTime();
        long timeBonus = (long) ((pyramide.nbADeviner * 9) * ((difficulte / 2) > 0 ? (difficulte / 2) : 1) * ((pyramide.type - 0.5) > 1.0 ? (pyramide.type * 0.75) : 1.0));

        int score = 0;

        int[] valeurEntree = new int[]{0, 0}; // num case / reponse
        String typeJeuString = "";
        String typeJeuStringV = "";

        switch(pyramide.type) {
            case 1:
                typeJeuString = "Addition";
                typeJeuStringV = "additionner";
                break;
            case 2:
                typeJeuString = "Soustraction";
                typeJeuStringV = "soustraire";
                break;
            case 3:
                typeJeuString = "Multiplication";
                typeJeuStringV = "multiplier";
                break;
        }

        do {
            resetTextColor();
            afficher(pyramide, false);

            println(ANSI_CYAN + ANSI_BOLD + "\nEntres d'abord le numéro de la case cachée que tu souhaites compléter, puis entres la réponse." + ANSI_RESET + ANSI_CYAN);
            print("Tu dois " + ANSI_YELLOW + ANSI_BOLD + typeJeuStringV + ANSI_RESET + ANSI_CYAN + " les deux nombres sous une case cachée pour trouver la bonne réponse.\n");
            println("\nTrouvé/max : " + ANSI_BOLD + nbDevines + ANSI_RESET + ANSI_CYAN + "/" + ANSI_BOLD + pyramide.nbADeviner + ANSI_RESET + ANSI_CYAN);
            println("Chances restantes : " + ANSI_BOLD + chances + ANSI_RESET + ANSI_CYAN);
            print("Type de pyramide : " + ANSI_YELLOW + ANSI_BOLD + typeJeuString + ANSI_RESET + ANSI_CYAN + "\n");
            println("> Termines la pyramide en moins de " + ANSI_BOLD + timeBonus + ANSI_RESET + ANSI_CYAN + (timeBonus > 1 ? " secondes" : " seconde") + " pour doubler ton score !");

            resetTextColor();

            valeurEntree = entrerValeur(pyramide);

            clearScreen();

            if(estValide(valeurEntree, pyramide)) {
                text("green");
                println("\n> Bonne réponse !");
                nbDevines++;
            } else {
                text("red");
                println("\n> Mauvaise réponse...");
                chances--;
            }
        } while(chances > 0 && nbDevines < pyramide.nbADeviner);

        clearScreen();
        resetTextColor();

        if(chances <= 0) {
            afficher(pyramide, true);

            text("red");
            println("\n> Tu as malheureusement perdu...");
            resetTextColor();

            text("cyan");
            println("> Les bonnes réponses sont affichées ci-dessus.");
        } else {
            afficher(pyramide, true);

            text("green");
            println("\n\n> Tu as tout trouvé !");
            resetTextColor();

            long timeTermine = (getTime() - timeStart) / 1000;
            score = ((chances * 10) * ((difficulte / 2) > 0 ? (difficulte / 2) : 1));

            text("cyan");

            if(timeBonus >= timeTermine) {
                print("\nTu obtiens un bonus de score (x2) car tu as terminé la pyramide en moins de " + ANSI_BOLD + timeBonus + ANSI_RESET + ANSI_CYAN + (timeBonus > 1 ? " secondes" : " seconde") + ".");
                score = score * 2;
            } else {
                print("\nTu aurais obtenu un bonus de score (x2) si tu avais terminé la pyramide en moins de " + ANSI_BOLD + timeBonus + ANSI_RESET + ANSI_CYAN + (timeBonus > 1 ? " secondes" : " seconde") + ".");
            }

            print("\nTu l'as terminée en " + ANSI_BOLD + timeTermine + ANSI_RESET + ANSI_CYAN + (timeTermine > 1 ? " secondes" : " seconde") + ".\n");
        }

        resetTextColor();

        return score;
    }

    // Génère la partie en fonction de la difficulté (valeur max des nbs générés en bas de la pyramide, type de pyramide, taille de la pyramide)
    int[] genererTypePartie(int difficulte) {
        int nbMax = 20;
        double typeJeuMax = 1.0;
        int[] taillePyramide = new int[]{4, 4}; // 1er = nb de carrés bas / 2ème hauteur (nombre de lignes)

        switch(difficulte) {
            case 1:
                taillePyramide = new int[]{4, 4};
                nbMax = 20;
                typeJeuMax = 1.0;
                break;
            case 2:
                taillePyramide = new int[]{5, 5};
                nbMax = 50;
                typeJeuMax = 1.0;
                break;
            case 3:
                taillePyramide = new int[]{6, 6};
                nbMax = 99;
                typeJeuMax = 1.0;
                break;
            case 4:
                taillePyramide = new int[]{8, 8};
                nbMax = 150;
                typeJeuMax = 1.0;
                break;
            case 5:
                taillePyramide = new int[]{9, 9};
                nbMax = 150;
                typeJeuMax = 2.0;
                break;
        }

        int typeJeu = 1 + (int) (random() * typeJeuMax); // 1 = addition ; 2 = soustraction ; 3 = multiplication

        return new int[]{taillePyramide[0], taillePyramide[1], typeJeu, nbMax};
    }

    void afficher(Pyramide pyramide, boolean afficherResolu) {
        String espaces = "";
        int num = 1;
        int taille = 1;

        print(" ");
        text("yellow");

        for(int i = 0; i < length(pyramide.pyramide, 1); i++) {
            espaces = "";

            // Dessin espaces ligne (avant cases)
            for(int k = length(pyramide.pyramide, 1); k > i + 1; k--) {
                espaces += "   ";
            }

            print(espaces);

            // Dessin tirets haut ligne
            for(int l = 0; l < length(pyramide.pyramide, 1) - ((length(pyramide.pyramide, 1) - 1) - i); l++) {
                print("-------");
            }

            // Cases ligne
            print("\n" + espaces + "|");

            for(int j = 0; j < length(pyramide.pyramide, 1) - ((length(pyramide.pyramide, 1) - 1) - i); j++) {
                if(pyramide.secret[i][j] && !afficherResolu) {
                    print(ANSI_CYAN + "  __  " + ANSI_YELLOW);
                } else {
                    // On centre les nombres
                    taille = length("" + pyramide.pyramide[i][j]);

                    for(int n = 0; n < (6 - taille) / 2; n++) {
                        print(" ");
                    }

                    print(pyramide.pyramide[i][j]);

                    for(int n = 0; n < 6 - (((6 - taille) / 2) + taille); n++) {
                        print(" ");
                    }
                }

                print("|");
            }

            print("\n" + espaces + "|");

            // bas case ligne
            for(int j = 0; j < length(pyramide.pyramide, 1) - ((length(pyramide.pyramide, 1) - 1) - i); j++) {
                if(pyramide.secret[i][j] && !afficherResolu) {
                    // Centrage
                    taille = length("" + "n°" + num);

                    for(int n = 0; n < (6 - taille) / 2; n++) {
                        print(" ");
                    }

                    print(ANSI_RESET + ANSI_BOLD + "n°" + num + ANSI_RESET + ANSI_YELLOW);

                    for(int n = 0; n < 6 - (((6 - taille) / 2) + taille); n++) {
                        print(" ");
                    }

                    num++;
                } else {
                    print("      ");
                }

                print("|");
            }

            print("\n ");
        }

        // Tiret ligne bas
        for(int m = 0; m < length(pyramide.pyramide, 1); m++) {
            print("-------");
        }

        resetTextColor();
    }

    Pyramide creerPyramide(int[] taille, int typePyramide, int nbMin, int nbMax) {
        Pyramide pyramide = new Pyramide();
        pyramide.pyramide = new int[taille[0]][taille[1]];
        pyramide.secret = new boolean[taille[0]][taille[1]];
        pyramide.nbADeviner = 0;
        pyramide.type = typePyramide;
        pyramide.nbMin = nbMin;
        pyramide.nbMax = nbMax;
        return pyramide;
    }

    // Genere nbs à la dernière ligne
    Pyramide initialiserPyramide(Pyramide pyramide) {
        int genere = 0;

        if(pyramide.type == 2) {
            genere = pyramide.nbMax;
        } else {
            genere = genereNbAleat(pyramide.nbMin, pyramide.nbMax);
        }

        int nbPrec = genere;

         for(int j = 0; j < length(pyramide.pyramide, 1) - ((length(pyramide.pyramide, 1) - 1) - (length(pyramide.pyramide, 1) - 1)); j++) {
            if(pyramide.type == 2) { // Pour le type soustraction, on génère en bas des nombres du plus grand au plus petit, pour faciliter le jeu
                while(genere > nbPrec || genere <= 0) {
                    genere = genereNbAleat(pyramide.nbMin, pyramide.nbMax);
                }

                pyramide.pyramide[length(pyramide.pyramide, 1) - 1][j] = genere;
                nbPrec = genere;
            } else {
                pyramide.pyramide[length(pyramide.pyramide, 1) - 1][j] = genere;
            }

            genere = genereNbAleat(pyramide.nbMin, pyramide.nbMax);
         }

         return pyramide;
     }

    // Résouds la pyramide
    Pyramide resoudrePyramide(Pyramide pyramide) {
        for(int i = length(pyramide.pyramide, 1) - 1; i >= 0; i--) {
            for(int j = 0; j < length(pyramide.pyramide, 1) - ((length(pyramide.pyramide, 1) - 1) - i); j++) {
                if(pyramide.pyramide[i][j] == 0) {
                    if(pyramide.type == 1) {
                        pyramide.pyramide[i][j] = pyramide.pyramide[i + 1][j] + pyramide.pyramide[i + 1][j + 1];
                    } else if(pyramide.type == 2) {
                        pyramide.pyramide[i][j] = pyramide.pyramide[i + 1][j] - pyramide.pyramide[i + 1][j + 1];
                    } else if(pyramide.type == 3) {
                        pyramide.pyramide[i][j] = pyramide.pyramide[i + 1][j] * pyramide.pyramide[i + 1][j + 1];
                    }
                }
            }
        }

        return pyramide;
    }

    Pyramide initialiserPyramideCachee(Pyramide pyramide) {
        int nbADeviner = 0;

        for(int i = 0; i < length(pyramide.pyramide, 1) - 1; i++) {
            for(int j = 0; j < length(pyramide.pyramide, 1) - ((length(pyramide.pyramide, 1) - 1) - i); j++) {
                if(((int) (random() * 2.0)) == 1 && pyramide.nbADeviner < length(pyramide.pyramide, 1)) { // on cache certains nbs
                    pyramide.secret[i][j] = true;
                    pyramide.nbADeviner++;
                }
            }
        }

        return pyramide;
    }

    int[] entrerValeur(Pyramide pyramide) {
        int caseChoix = 0;
        int reponse = 0;
        String choix = "";
        boolean valide = true;

        do {
            if(!valide) {
                text("red");
                println("\nCase invalide !");
                resetTextColor();
            }

            println("\nEntres le numéro de la case : ");
            choix = readString();

            valide = entreeValide(choix, pyramide, true);
        } while(!valide);

        caseChoix = stringToInt(choix);

        do {
            if(!valide) {
                text("red");
                println("\nEntrée invalide !");
                resetTextColor();
            }

            println("\nEntres ta réponse : ");
            choix = readString();

            valide = entreeValide(choix, pyramide, false);
        } while(!valide);

        reponse = stringToInt(choix);

        return new int[]{caseChoix, reponse};
    }

    boolean entreeValide(String choix, Pyramide pyramide, boolean verifierCase) {
        int num = 1;
        boolean resultat = false;
        boolean nbValide = true;

        if(!equals(choix, "") && !equals(choix, "-")) {
            for(int j = 0; j < length(choix); j++) {
                if(!dansTableau(charAt(choix, j), new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '-'})) {
                    nbValide = false;
                }
            }

            if(nbValide && verifierCase) {
                for(int i = 0; i < length(pyramide.pyramide, 1); i++) {
                    for(int j = 0; j < length(pyramide.pyramide, 1) - ((length(pyramide.pyramide, 1) - 1) - i); j++) {
                        if(pyramide.secret[i][j]) {
                            if(num == stringToInt(choix)) {
                                resultat = true;
                            } else {
                                num++;
                            }
                        }
                    }
                }
            } else if(nbValide && !verifierCase) {
                resultat = true;
            }
        }

        return resultat;
    }

    // Est valide par rapport à la bonne réponse
    boolean estValide(int[] valeurEntree, Pyramide pyramide) {
        int num = 1;
        boolean resultat = false;

        for(int i = 0; i < length(pyramide.pyramide, 1); i++) {
            for(int j = 0; j < length(pyramide.pyramide, 1) - ((length(pyramide.pyramide, 1) - 1) - i); j++) {
                if(pyramide.secret[i][j]) {
                    if(num == valeurEntree[0] && pyramide.pyramide[i][j] == valeurEntree[1]) {
                        resultat = true;
                        pyramide.secret[i][j] = false;
                        break;
                    } else {
                        num++;
                    }
                }
            }
        }

        return resultat;
    }

    void testEntreeValide() {
        Pyramide pyramide = creerPyramide(new int[]{4, 4}, 1, 10, 20);
        pyramide = initialiserPyramide(pyramide);
        pyramide = resoudrePyramide(pyramide);
        pyramide.secret = new boolean[][]{{true, false, false, false}, {true, false, false, false}, {true, false, true, false}, {false, false, false, false}};

        assertTrue(entreeValide("1", pyramide, true));
        assertTrue(entreeValide("3", pyramide, true));
        assertFalse(entreeValide("5", pyramide, true));
        assertFalse(entreeValide("9", pyramide, true));
        assertFalse(entreeValide("99", pyramide, true));
        assertFalse(entreeValide("E", pyramide, true));
        assertFalse(entreeValide(" ", pyramide, true));
        assertFalse(entreeValide("9e", pyramide, true));
    }

    void testDansTableau() {
        char[] tab = new char[]{'A', 'B', 'C'};

        assertTrue(dansTableau('A', tab));
        assertTrue(dansTableau('B', tab));
        assertFalse(dansTableau('E', tab));
        assertFalse(dansTableau('f', tab));
    }

    void testResoudrePyramide() {
        Pyramide pyramide = creerPyramide(new int[]{4, 4}, 1, 10, 20);
        pyramide.pyramide = new int[][]{{0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {11, 12, 15, 12}};

        Pyramide pyramideSoustractions = creerPyramide(new int[]{4, 4}, 2, 10, 20);
        pyramideSoustractions.pyramide = new int[][]{{0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {11, 12, 15, 12}};

        Pyramide pyramideMulitplication = creerPyramide(new int[]{4, 4}, 3, 1, 10);
        pyramideMulitplication.pyramide = new int[][]{{0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {2, 4, 6, 6}};

        int[][] pyramideAResoudreAdditions = {{104, 0, 0, 0}, {50, 54, 0, 0}, {23, 27, 27, 0}, {11, 12, 15, 12}};
        int[][] pyramideAResoudreSoustractions = {{8, 0, 0, 0}, {2, -6, 0, 0}, {-1, -3, 3, 0}, {11, 12, 15, 12}};
        int[][] pyramideAResoudreMultiplications = {{165888, 0, 0, 0}, {192, 864, 0, 0}, {8, 24, 36, 0}, {2, 4, 6, 6}};

        assertArrayEquals(resoudrePyramide(pyramide).pyramide, pyramideAResoudreAdditions);
        pyramide.pyramide = new int[][]{{0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {11, 12, 15, 12}};
        assertArrayEquals(resoudrePyramide(pyramideSoustractions).pyramide, pyramideAResoudreSoustractions);
        assertArrayEquals(resoudrePyramide(pyramideMulitplication).pyramide, pyramideAResoudreMultiplications);
    }

    void testEstValide() {
        Pyramide pyramide = creerPyramide(new int[]{4, 4}, 1, 10, 20);
        pyramide.pyramide = new int[][]{{0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {11, 12, 15, 12}};
        pyramide = resoudrePyramide(pyramide);
        pyramide.secret = new boolean[][]{{true, false, false, false}, {true, true, false, false}, {false, false, true, false}, {false, false, false, false}};

        Pyramide pyramideSoustractions = creerPyramide(new int[]{4, 4}, 2, 10, 20);
        pyramideSoustractions.pyramide = new int[][]{{0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {11, 12, 15, 12}};
        pyramideSoustractions = resoudrePyramide(pyramideSoustractions);
        pyramideSoustractions.secret = new boolean[][]{{true, false, false, false}, {true, false, false, false}, {true, false, true, false}, {false, false, false, false}};

        assertTrue(estValide(new int[]{2, 50}, pyramide));
        assertFalse(estValide(new int[]{3, 64}, pyramide));

        assertTrue(estValide(new int[]{2, 2}, pyramideSoustractions));
        assertFalse(estValide(new int[]{4, -2}, pyramideSoustractions));
    }

    // Jeu de géographie
    int jeu_geographie(int difficulte) {
        eraseAllScreen();
        CSVFile csvGeo = loadCSV(jeuGeoFile);

        QuestionGeo question = creerQuestion(csvGeo, difficulte);

        boolean jeu = true;
        int[] etatJeu = new int[]{1, 0, question.nbReponses, 0};
        boolean reponse = false;
        int numero = 0;
        int score = 0;
        int tempsMax = ((question.nbReponses) + (5 - (difficulte - 1)) * 2) * 5;
        long tempsDebut = getTime();

        affichage(question);
        regles(question, tempsMax);
        afficherEtat(etatJeu[1], etatJeu[2], question);

        while(etatJeu[0] == 1) {
            numero = numero(question);
            String continent = continent(question);
            reponse = bonneReponse(question, numero, continent);
            etatJeu = apresCoup(reponse, etatJeu[1], etatJeu[2], question.nbReponses);

            if(etatJeu[0] == 1) {
                affichage(question);
                afficherRep(etatJeu[3]);
                regles(question, tempsMax);
                afficherEtat(etatJeu[1], etatJeu[2], question);
            } else {
                afficherRep(etatJeu[3]);
            }
        }

        if(etatJeu[0] == 2) {
            long timeTermine = (getTime() - tempsDebut) / 1000;
            score = question.nbReponses * 15;

            if(timeTermine <= tempsMax) {
                text("cyan");
                print("\nTu obtiens un bonus de score (x2) car tu as terminé le jeu en moins de " + ANSI_BOLD + tempsMax + ANSI_RESET + ANSI_CYAN + (tempsMax > 1 ? " secondes" : " seconde") + ".");
                print("\nTu l'as terminée en " + ANSI_BOLD + timeTermine + ANSI_RESET + ANSI_CYAN + (timeTermine > 1 ? " secondes" : " seconde") + ".\n");
                resetTextColor();

                score = score * 2;
            }

            score = score < 0 ? 0 : score;
        }

        return score;
    }

    QuestionGeo creerQuestion(CSVFile csvGeo, int difficulte) {
        int index = 0;

        do {
            index = genereNbAleat(0, rowCount(csvGeo));
        } while(stringToInt(getCell(csvGeo, index, 4)) > difficulte);

        QuestionGeo question = new QuestionGeo();

        question.recherche = getCell(csvGeo, index, 0);
        question.rechercheDe = getCell(csvGeo, index, 1);
        question.rechercheCe = getCell(csvGeo, index, 2);
        question.nbReponses = stringToInt(getCell(csvGeo, index, 3));
        question.carte = getCell(csvGeo, index, 5);

        question.trouve = new boolean[question.nbReponses];
        question.reponses = new String[question.nbReponses];

        for(int i = 0; i < question.nbReponses; i++) {
            question.reponses[i] = getCell(csvGeo, index, 6 + i);
        }

        return question;
    }

    int[] apresCoup(boolean reponse, int score, int chance, int nbQuestions){
        int result = 1; // 1 = jeu en cours / 2 = gagné / 3 = perdu
        int trouvePrec = 0;

        if(reponse){
            score++;
            trouvePrec = 1;
        } else{
            chance -= 1;
        }

        if(score == nbQuestions){
            trouvePrec = 2;
            result = 2;
        }

        if(chance == 0){
            trouvePrec = 3;
            result = 3;
        }

        resetTextColor();

        return new int[]{result, score, chance, trouvePrec};
    }

    boolean bonneReponse(QuestionGeo question, int numero, String continent) {
        boolean result = false;

        if(equals(toUpperCase(question.reponses[numero - 1]), toUpperCase(continent)) && !question.trouve[numero - 1]){
            result = true;
            question.trouve[numero - 1] = true;
        } else {
            result = false;
        }

        return result;
    }

    String continent(QuestionGeo question) {
        println("\nInscris le nom " + ANSI_BOLD + question.rechercheDe + ANSI_RESET + " : ");

        String nom = readString();
        String resultat = "";

        for(int i = 0; i < length(nom); i++) {
            if(charAt(nom, i) != '-' && charAt(nom, i) != '_' && charAt(nom, i) != '\'') {
                resultat += charAt(nom, i);
            } else {
                resultat += ' ';
            }
        }

        return resultat;
    }

    int numero(QuestionGeo question) {
        boolean saisie = false;
        String input = "0";
        int choice = 0;

        while(!saisie) {
            println("\nInscris le numéro " + ANSI_BOLD + question.rechercheDe + ANSI_RESET + " que tu souhaites trouver : ");
            input = readString();

            if(length(input) > 0) {
                choice = (int) (charAt(input, 0) - '0');
            }

            if(choice < 1 || choice > question.nbReponses){
                println("\n> Saisie incorrecte !");
            } else if(question.trouve[choice - 1]) {
                println("\n> Tu as déjà trouvé " + ANSI_BOLD + question.rechercheCe + ANSI_RESET + " !");
            } else{
                saisie = true;
            }
        }

        return choice;
    }

    void afficherRep(int trouvePrec) {
        if(trouvePrec == 1) {
            text("green");
            println("> Bonne réponse !\n");
        } else if(trouvePrec == 2) {
            text("green");
            println("\n> Tu as trouvé toute les bonnes réponses !\n");
        } else if(trouvePrec == 3) {
            text("red");
            println("\n> Tu n'as plus de chances ! Tu as donc perdu...\n");
        } else {
            text("red");
            println("> Mauvaise réponse !\n");
        }

        resetTextColor();
    }

    void regles(QuestionGeo question, int tempsMax) {
        println(ANSI_CYAN + "Trouver le nom des " + ANSI_BOLD + question.recherche + ANSI_RESET + ANSI_CYAN + " à leurs numéros. Exemple: 1 = *******");
        println(ANSI_BOLD + "> Les lettres accentuées ne sont pas prises en compte ! Il faut entrer la lettre sans accent." + ANSI_RESET);
        println(ANSI_YELLOW + "> Finis cette partie en moins de " + ANSI_BOLD + tempsMax + ANSI_RESET + ANSI_YELLOW + (tempsMax > 1 ? " secondes" : " seconde") + " afin d'obtenir un bonus de score !\n");
        resetTextColor();
    }

    void afficherEtat(int score, int chance, QuestionGeo question) {
        println(ANSI_YELLOW + "Nombre de " + ANSI_BOLD + question.recherche + ANSI_RESET + ANSI_YELLOW + " trouvés/chances restantes : " + ANSI_BOLD + score + ANSI_RESET + ANSI_YELLOW + "/" + ANSI_BOLD + chance + ANSI_RESET + ANSI_YELLOW);

        String numeroATrouver = "";
        boolean premier = true;

        for(int i = 0; i < length(question.trouve); i++) {
            if(!question.trouve[i]) {
                if(!premier) {
                    numeroATrouver += ", ";
                }

                numeroATrouver += (i + 1);
                premier = false;
            }
        }

        println("Il te reste à trouver " + (length(numeroATrouver) > 1 ? "les n° : " : "le n°") + ANSI_BOLD + numeroATrouver + ANSI_RESET);
        resetTextColor();
    }

    void affichage(QuestionGeo question) {
        text("yellow");

        for(int i = 0; i < length(question.carte); i++) {
            if(charAt(question.carte, i) == '\\' && charAt(question.carte, i + 1) == 'n') {
                print("\n");
                i += 2;
            } else {
                print(charAt(question.carte, i));
            }
        }

        print("\n");
        resetTextColor();
    }

    // Jeu de mémoire
    int jeu_memoire(int difficulte) {
        resetTextColor();
        int[] partieGenere = genererTypePartieMemoire(difficulte);

        Deck deck = creerDeck(new int[]{partieGenere[0], partieGenere[1]}, partieGenere[2]);
        deck = initialiserDeck(deck);

        int nbPairesDevinees = 0;
        int nbCartes = length(deck.deck, 1) * length(deck.deck, 2);
        int nbPaires = nbCartes / 2;
        int chances = (int) (nbCartes * 0.9);
        int chancesDebut = chances;
        long timeStart = getTime();
        long timeBonus = (long) ((length(deck.deck, 1) * 20) * ((difficulte / 2) > 0 ? (difficulte / 2) : 1));
        int score = 0;
        int[] valeurEntree = new int[]{0, 0};

        eraseAllScreen();

        do {
            resetTextColor();
            afficher(deck, false);

            text("yellow");
            println(ANSI_BOLD + "\nTrouves toutes les paires de même symbole !" + ANSI_RESET);
            text("cyan");
            println("\nTrouvé/nb paires : " + ANSI_BOLD + nbPairesDevinees + ANSI_RESET + ANSI_CYAN + "/" + ANSI_BOLD + nbPaires + ANSI_RESET + ANSI_CYAN);
            println("Chances restantes : " + ANSI_BOLD + chances + ANSI_RESET + ANSI_CYAN);
            println("> Termines le jeu en moins de " + ANSI_BOLD + timeBonus + ANSI_RESET + ANSI_CYAN + (timeBonus > 1 ? " secondes" : " seconde") + " pour doubler ton score.");

            resetTextColor();

            valeurEntree = entrerValeur(deck);

            clearScreen();

            if(estValide(valeurEntree, deck)) {
                text("green");
                println("\n> Bonne réponse !");
                nbPairesDevinees++;
            } else {
                text("red");
                println("\n> Mauvaise réponse...");
                chances--;
            }
        } while(chances > 0 && nbPairesDevinees < nbPaires);

        clearScreen();
        resetTextColor();

        if(chances <= 0) {
            afficher(deck, true);

            text("red");
            println("\n> Tu as malheureusement perdu...");
            resetTextColor();

            text("cyan");
            println("> Les bonnes réponses sont affichées ci-dessus.");
        } else {
            afficher(deck, true);

            text("green");
            println("\n\n> Tu as tout trouvé !");
            resetTextColor();

            long timeTermine = (getTime() - timeStart) / 1000;
            score = ((nbPairesDevinees - (nbPairesDevinees - chances)) * 25) * ((difficulte / 2) > 0 ? (difficulte / 2) : 1);

            text("cyan");

            if(timeBonus >= timeTermine) {
                print("\nTu obtiens un bonus de score (x2) car tu as terminé le jeu en moins de " + ANSI_BOLD + timeBonus + ANSI_RESET + ANSI_CYAN + (timeBonus > 1 ? " secondes" : " seconde") + ".");
                score = score * 2;
            } else {
                print("\nTu aurais obtenu un bonus de score (x2) si tu avais terminé le jeu en moins de " + ANSI_BOLD + timeBonus + ANSI_RESET + ANSI_CYAN + (timeBonus > 1 ? " secondes" : " seconde") + ".");
            }

            print("\nTu l'as terminée en " + ANSI_BOLD + timeTermine + ANSI_RESET + ANSI_CYAN + (timeTermine > 1 ? " secondes" : " seconde") + ".");
        }

        resetTextColor();

        return score;
    }

    void afficher(Deck deck, boolean afficherResolu) {
        int numCarte = 1;
        int taille = 0;

        print("\n\n");

        for(int i = 0; i < length(deck.deck, 1); i++) {
            // Dessin tirets haut ligne
            for(int k = 0; k < length(deck.deck, 2); k++) {
                print("------- ");
            }

            print("\n");

            for(int k = 0; k < length(deck.deck, 2); k++) {
                print("|     | ");
            }

            print("\n");

            for(int j = 0; j < length(deck.deck, 2); j++) {
                print("|");

                if(afficherResolu || deck.deck[i][j].trouve || deck.deck[i][j].retourne) {
                    if(deck.afficherCouleurs) {
                        text(symbolesCartesCouleurs[deck.deck[i][j].type]);
                    } else {
                        text("green");
                    }

                    taille = length(symbolesCartes[deck.deck[i][j].type]);

                    for(int n = 0; n < (5 - taille) / 2; n++) {
                        print(" ");
                    }

                    print(symbolesCartes[deck.deck[i][j].type]);

                    for(int n = 0; n < 5 - (((5 - taille) / 2) + taille); n++) {
                        print(" ");
                    }

                    resetTextColor();
                } else {
                    text("red");
                    print("  ?  ");
                    resetTextColor();
                }

                print("| ");
            }

            print("\n");

            for(int k = 0; k < length(deck.deck, 2); k++) {
                print("|     | ");
            }

            print("\n");

            for(int k = 0; k < length(deck.deck, 2); k++) {
                print("------- ");
            }

            print("\n");

            for(int j = 0; j < length(deck.deck, 2); j++) {
                taille = length("" + "n°" + numCarte);

                for(int n = 0; n < (8 - taille) / 2; n++) {
                    print(" ");
                }

                print("n°" + numCarte);

                for(int n = 0; n < 8 - (((8 - taille) / 2) + taille); n++) {
                    print(" ");
                }

                numCarte++;
            }

            print("\n");
        }
    }

    // Génère la partie en fonction de la difficulté
    int[] genererTypePartieMemoire(int difficulte) {
        int[] tailleDeck = new int[]{2, 3}; // 2 rangées de 3 cartes chacunes
        int afficherCouleurs = 1;

        switch(difficulte) {
            case 1:
                tailleDeck = new int[]{2, 3};
                afficherCouleurs = 1;
                break;
            case 2:
                tailleDeck = new int[]{2, 4};
                afficherCouleurs = 1;
                break;
            case 3:
                tailleDeck = new int[]{2, 5};
                afficherCouleurs = 1;
                break;
            case 4:
                tailleDeck = new int[]{2, 7};
                afficherCouleurs = 0;
                break;
            case 5:
                tailleDeck = new int[]{3, 6};
                afficherCouleurs = 0;
                break;
        }

        return new int[]{tailleDeck[0], tailleDeck[1], afficherCouleurs};
    }

    Carte creerCarte() {
        Carte carte = new Carte();
        carte.type = 0;

        return carte;
    }

    Deck creerDeck(int[] taille, int afficherCouleurs) {
        Deck deck = new Deck();
        deck.afficherCouleurs = false;

        if(afficherCouleurs == 1) {
            deck.afficherCouleurs = true;
        }

        deck.deck = new Carte[taille[0]][taille[1]];

        for(int i = 0; i < length(deck.deck, 1); i++) {
            for(int j = 0; j < length(deck.deck, 2); j++) {
                deck.deck[i][j] = creerCarte();
            }
        }

        return deck;
    }

    int[] indexCarte(Deck deck, int carte) {
        return new int[]{(carte - 1) / length(deck.deck, 2), (carte - 1) % length(deck.deck, 2)};
    }

    int typeCarte(Deck deck, int carte) {
        return deck.deck[indexCarte(deck, carte)[0]][indexCarte(deck, carte)[1]].type;
    }

    boolean estTrouveCarte(Deck deck, int carte) {
        return deck.deck[indexCarte(deck, carte)[0]][indexCarte(deck, carte)[1]].trouve;
    }

    boolean estRetourneCarte(Deck deck, int carte) {
        return deck.deck[indexCarte(deck, carte)[0]][indexCarte(deck, carte)[1]].retourne;
    }

    boolean estInitialiseCarte(Deck deck, int carte) {
        return deck.deck[indexCarte(deck, carte)[0]][indexCarte(deck, carte)[1]].estInitialise;
    }

    Deck initialiserDeck(Deck deck) {
        int randomSigne = 0;
        int randomAutreCarte = 0;

        for(int i = 1; i <= length(deck.deck, 1) * length(deck.deck, 2); i++) {
            randomSigne = 0;
            randomAutreCarte = 0;

            if(!estInitialiseCarte(deck, i)) {
                do {
                    randomSigne = genereNbAleat(0, length(symbolesCartes));
                } while(dansTableauCarte(deck, randomSigne));

                do {
                    randomAutreCarte = genereNbAleat(i + 1, (length(deck.deck, 1) * length(deck.deck, 2)) + 1);
                } while(estInitialiseCarte(deck, randomAutreCarte));

                deck.deck[indexCarte(deck, i)[0]][indexCarte(deck, i)[1]].type = randomSigne;
                deck.deck[indexCarte(deck, i)[0]][indexCarte(deck, i)[1]].estInitialise = true;
                deck.deck[indexCarte(deck, randomAutreCarte)[0]][indexCarte(deck, randomAutreCarte)[1]].type = randomSigne;
                deck.deck[indexCarte(deck, randomAutreCarte)[0]][indexCarte(deck, randomAutreCarte)[1]].estInitialise = true;
            }
        }

        return deck;
    }

    boolean dansTableauCarte(Deck deck, int type) {
        boolean resultat = false;

        for(int i = 0; i < length(deck.deck, 1); i++) {
            for(int j = 0; j < length(deck.deck, 2); j++) {
                if(type == deck.deck[i][j].type) {
                    resultat = true;
                }
            }
        }

        return resultat;
    }

    int[] entrerValeur(Deck deck) {
        int carteOne = 0;
        int carteTwo = 0;
        String choix = "";
        boolean valide = true;

        do {
            if(!valide) {
                text("red");
                println("\nEntrée invalide !");
                resetTextColor();
            }

            println("\nEntres le numéro de la première carte à retourner : ");
            choix = readString();

            valide = entreeValide(choix, deck);
        } while(!valide);

        carteOne = stringToInt(choix);
        deck.deck[indexCarte(deck, carteOne)[0]][indexCarte(deck, carteOne)[1]].retourne = true;

        afficher(deck, false);

        do {
            if(!valide) {
                text("red");
                println("\nEntrée invalide !");
                resetTextColor();
            }

            println("\nEntres le numéro de la seconde carte à retourner : ");
            choix = readString();

            valide = entreeValide(choix, deck);
        } while(!valide);

        carteTwo = stringToInt(choix);

        deck.deck[indexCarte(deck, carteTwo)[0]][indexCarte(deck, carteTwo)[1]].retourne = true;

        afficher(deck, false);
        delay(1000);

        eraseAllScreen();

        deck.deck[indexCarte(deck, carteOne)[0]][indexCarte(deck, carteOne)[1]].retourne = false;
        deck.deck[indexCarte(deck, carteTwo)[0]][indexCarte(deck, carteTwo)[1]].retourne = false;

        return new int[]{carteOne, carteTwo};
    }

    boolean entreeValide(String choix, Deck deck) {
        int choice = 0;
        boolean nbValide = true;
        boolean resultat = true;

        if(length(choix) > 0) {
            for(int j = 0; j < length(choix); j++) {
                if(!dansTableau(charAt(choix, j), new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'})) {
                    nbValide = false;
                }
            }

            if(nbValide) {
                choice = stringToInt(choix);

                if(choice < 1 || choice > length(deck.deck, 1) * length(deck.deck, 2) || estTrouveCarte(deck, choice) || estRetourneCarte(deck, choice)) {
                    resultat = false;
                }
            } else {
                resultat = false;
            }
        } else {
            resultat = false;
        }

        return resultat;
    }

    boolean estValide(int entree[], Deck deck) {
        boolean resultat = false;

        if(typeCarte(deck, entree[0]) == typeCarte(deck, entree[1])) {
            deck.deck[indexCarte(deck, entree[0])[0]][indexCarte(deck, entree[0])[1]].trouve = true;
            deck.deck[indexCarte(deck, entree[1])[0]][indexCarte(deck, entree[1])[1]].trouve = true;

            resultat = true;
        }

        return resultat;
    }

    // Quizz
    int jeu_quizz(int difficulte, boolean demanderTheme) {
        eraseAllScreen();
        CSVFile questions = loadCSV(questionQuizzFile, ';');
        int nbThemes = stringToInt(getCell(questions, 0, 0));
        int theme = 1;

        resetTextColor();

        if(demanderTheme) {
            theme = demanderThemeQuizz(nbThemes, questions);
        } else {
            do {
                theme = genereNbAleat(1, nbThemes + 1);
            } while(stringToInt(getCell(questions, 2, theme)) > difficulte);
        }

        if(theme < 1 || theme > nbThemes) {
            theme = 1;
        }

        int nbQuestionAPoser = 5;
        int score = 0;
        int nbBonneReponse = 0;
        int tempsReponseParQuestion = (8 - (difficulte - 1)) * 5;
        long timeBonus = (long) ((nbQuestionAPoser * tempsReponseParQuestion) / 2);
        QuestionQuizz question;
        int[] questionAPoser = questionRandom(theme, questions);
        String choix = "";
        long timeStart = getTime();
        long timeStartQuestion = 0;
        long timeReponseQuestion = 0;

        eraseAllScreen();

        for(int i=0; i < nbQuestionAPoser; i++){
            question = creerQuestion(theme, questionAPoser[i], questions);
            print(ANSI_YELLOW + ANSI_UNDERLINE + ANSI_BOLD + "\nThème" + ANSI_RESET + " : " + getCell(questions, 0, theme));
            print(ANSI_YELLOW + ANSI_UNDERLINE + ANSI_BOLD + "\nQuestion n°" + (i+1) + ANSI_RESET + " : ");
            print(ANSI_BOLD + question.question + ANSI_RESET + "\n");
            println(ANSI_CYAN + ANSI_BOLD + "> Tu as " + tempsReponseParQuestion + " secondes pour répondre à cette question" + ANSI_RESET);
            println(ANSI_CYAN + "> Finis cette partie sans aucune erreur en moins de " + timeBonus + " secondes pour obtenir un bonus de score !\n" + ANSI_RESET);

            for(int j=0; j < length(question.reponses); j++){
                print(ANSI_BOLD + (j+1) + ") " + ANSI_RESET + question.reponsesString[j] + " ");
            }

            timeStartQuestion = getTime();
            timeReponseQuestion = timeStartQuestion;

            do {
                text("cyan");
                println("\n\nEntres le numéro de ta réponse :");
                resetTextColor();

                choix = readString(tempsReponseParQuestion * 1000 - (timeReponseQuestion - timeStartQuestion));
                timeReponseQuestion = getTime();

                if(timeReponseQuestion - timeStartQuestion >= tempsReponseParQuestion * 1000) {
                    text("red");
                    println("\n> Temps écoulé !");
                    resetTextColor();
                } else if(!estNombre(choix) || stringToInt(choix) < 1 || stringToInt(choix) > length(question.reponses)) {
                    text("red");
                    println("\n> Entrée invalide !");
                    resetTextColor();
                }
            } while(timeReponseQuestion - timeStartQuestion < tempsReponseParQuestion * 1000 && (!estNombre(choix) || stringToInt(choix) < 1 || stringToInt(choix) > length(question.reponses)));

            if(timeReponseQuestion - timeStartQuestion >= tempsReponseParQuestion * 1000) {
                afficherReponse(question);
            } else if(stringToInt(choix) == question.bonneReponse + 1) {
                nbBonneReponse++;

                text("green");
                println("\n> Bonne réponse !");
                resetTextColor();
            } else {
                text("red");
                println("\n> Mauvaise réponse...");
                afficherReponse(question);
            }
        }

        resetTextColor();

        long timeTermine = (getTime() - timeStart) / 1000;
        score = ((nbQuestionAPoser - (nbQuestionAPoser - nbBonneReponse)) * 20) * ((difficulte / 2) > 0 ? (difficulte / 2) : 1);

        println("\n------------------------------------------");
        text("cyan");
        println("\nC'est terminé !");
        resetTextColor();

        if(nbBonneReponse <= 0) {
            text("red");
            println("\n> Tu as malheureusement eu tout faux...");
            resetTextColor();
            score = 0;
        } else if(nbBonneReponse == nbQuestionAPoser) {
            text("green");
            println("\n> Bravo ! Tu as tout trouvé !");
            resetTextColor();

            text("cyan");
            if(timeBonus >= timeTermine) {
                print("\nTu obtiens un bonus de score (x2) car tu as terminé la partie en moins de " + ANSI_BOLD + timeBonus + ANSI_RESET + ANSI_CYAN + (timeBonus > 1 ? " secondes" : " seconde") + ", en plus d'avoir fait un sans faute !");
                score = score * 2;
            } else {
                print("\nTu aurais obtenu un bonus de score (x2) si tu avais terminé la partie en moins de " + ANSI_BOLD + timeBonus + ANSI_RESET + ANSI_CYAN + (timeBonus > 1 ? " secondes" : " seconde") + ", car tu as fais un sans faute. Dommage !");
            }

            print("\nTu l'as terminée en " + ANSI_BOLD + timeTermine + ANSI_RESET + ANSI_CYAN + (timeTermine > 1 ? " secondes" : " seconde") + ".");
            resetTextColor();
        } else {
            text("green");
            println("\n> " + (nbBonneReponse > 2 ? "Bien joué !" : "Pas mal !") + " Tu as répondu juste à " + ANSI_BOLD + nbBonneReponse + ANSI_RESET + ANSI_GREEN + " " + (nbBonneReponse > 1 ? "questions" : "question") + " !");
            resetTextColor();
        }

        return score;
    }

    void afficherReponse(QuestionQuizz question) {
        text("yellow");
        println("La bonne réponse était la réponse n°" + (question.bonneReponse + 1) + ANSI_YELLOW + " (" + ANSI_BOLD + question.reponsesString[question.bonneReponse] + ANSI_RESET + ANSI_YELLOW + ")");
        resetTextColor();
    }

    QuestionQuizz creerQuestion(int theme, int nbQuestion, CSVFile questionFILE) {
        QuestionQuizz question = new QuestionQuizz();
        question.question = getCell(questionFILE, nbQuestion + 2, (theme - 1) * 4);
        question.bonneReponse = 0;

        int[] listeReponse = new int[3];
        String[] listeReponseString = new String[3];
        int randomReponse = 0;

        for(int i = 0; i < 3; i++) {
            do {
                randomReponse = (int) (random() * 4);
            } while(dansTableau(randomReponse, listeReponse));

            if(randomReponse == 1) {
                question.bonneReponse = i;
            }

            listeReponseString[i] = getCell(questionFILE, nbQuestion + 2, ((theme - 1) * 4) + randomReponse);
            listeReponse[i] = randomReponse;
        }

        question.reponses = listeReponse;
        question.reponsesString = listeReponseString;

        return question;
    }

    int demanderThemeQuizz(int nbThemes, CSVFile questionFILE) {
        String entree = "";
        String[] contenu = new String[nbThemes];

        for(int i = 0; i < nbThemes; i++) {
            contenu[i] = getCell(questionFILE, 0, i + 1);
        }

        afficherMenu("Thèmes", ANSI_YELLOW, contenu, ANSI_TEXT_DEFAULT_COLOR, ". ", new int[]{}, false, ANSI_TEXT_DEFAULT_COLOR);

        text("cyan");
        println("\nEntres le numéro du thème souhaité :");
        resetTextColor();

        entree = readString();

        while(equals(entree, "") || !estNombre(entree) || stringToInt(entree) < 1 || stringToInt(entree) > nbThemes) {
            text("red");
            println("\n> Entrée invalide !");
            resetTextColor();

            text("cyan");
            println("\nEntres le numéro du thème souhaité :");
            resetTextColor();

            entree = readString();
        }

        return stringToInt(entree);
    }

    int[] questionRandom(int theme, CSVFile questionFILE) {
        int randomQuestion = 0;
        int nbQuestion = stringToInt(getCell(questionFILE, 1, theme));
        int[] listeQuestion = new int[nbQuestion];

        for(int i = 0; i < length(listeQuestion) - 1; i++) {
            do {
                randomQuestion = (int) (1.0 + (random() * nbQuestion));
            } while(dansTableau(randomQuestion, listeQuestion));

            listeQuestion[i] = randomQuestion;
        }

        return listeQuestion;
    }
}