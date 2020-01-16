# Minigames Marathon

 * Froissart Kévin
 * Bou Sala Elias

### Présentation de Minigames Marathon

<p>Pour notre projet ludo-pédagogique, nous avons eu l’idée de faire un jeu<br>
mêlant plusieurs mini-jeux qui seront proposés à la suite.<br>
Il dispose d'un système de points et de classement (un<br>
mini-jeu “boss” à la fin est proposé, qui rapporte plus de points).<br>
Les mini-jeux disponibles sont : un quizz, un jeu de calculs (pyramide),<br>
un pendu, un jeu de mémoire, un jeu de géographie.<br>
Le jeu dispose d'un mode entraînement (sans comptage des points,<br>
afin d'essayer les jeux individuellement) et de plusieurs modes de<br>
difficulté (pour s'adapter à l'âge et au niveau de l'élève).<br>
Plusieurs thèmes (matières enseignées au primaire) pour le mini-jeu du<br>
quizz et du pendu sont disponibles (choisi aléatoirement, ou<br>
manuellement pour le mode entraînement).</p>

### Utilisation de Minigames Marathon

Afin d'utiliser le projet, il doit être suffisant de taper les
commandes suivantes :

> ./compile.sh<br>
> ./run.sh main

### Ajout de données pour les jeux (fichiers CSV)

Il est possible d'ajouter ou de modifier les données de certains jeux<br>
grâce aux fichiers CSV présents dans le répertoire ressources.

#### Pendu

Ce fichier fonctionne par colonnes, chaque colonne étant un thème
différent.

* Nom du fichier : MotsPendu.csv
* 1ère ligne : Thèmes
* 2ème ligne : Nombre de mots pour chaque thème
* 3ème ligne : Difficulté minimale pour se voir proposer ce thème en
mode marathon
* 4ème ligne et plus : Mots

#### Jeu de géographie

* Nom du fichier : jeuGeo.csv
* Une carte par ligne
* Colonnes 1 à 3 : Bouts de textes repris dans le programme en fonction
du texte
* Colonne 4 : Nombre de réponses nécessaires (max 9)
* Colonne 5 : Difficulté minimale pour se voir proposer cette carte en
mode marathon
* Colonne 6 : Carte en Ascii Art (voir Google pour les cartes en noir et
blan, convertir via [un convertisseur](https://manytools.org/hacker-tools/convert-images-to-ascii-art/) ;
séparer les sauts de lignes avec des \n)
* Colonnes 7 et plus : Réponses

#### Quizz

* Nom du fichier : QuestionsQuizz.csv
* 1ère cellulle de la 1ère ligne : Nombre de thèmes
    Cellules suivantes : Nom des thèmes
* 2ème ligne : Nombre de questions pour chaque thème
* 3ème ligne : Difficulté minimale pour se voir proposer ce thème en
mode marathon
* Lignes suivantes :
    - Chaque question est gérée en 4 colonnes ;
    - 1ère colonne : La question
    - 2ème colonne : La bonne réponse
    - 3ème et 4ème colonne : Les mauvaises réponses
    - Chaque groupe de 4 colonnes correspond à un thème en fonction de
    son ordre.
* Important ! Chaque colonne et ligne du fichier CSV doivent être de la
même taille !<br>
Il faut ajouter le texte "vide" aux endroits vides.
