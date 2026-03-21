### Manuel Utilisateur – Cascadia en ligne
## Introduction
# Démarrage
Déroulement d’une Partie
Introduction
Bienvenue dans Cascadia en ligne, une version numérique du jeu de société. Ce manuel vous guide
pas à pas pour comprendre les règles et maîtriser l’interface de jeu.
Démarrage
# 1. Lancement du jeu
Assurez-vous que votre environnement Java est configuré correctement.
Dans un terminal éxécutez le fichier principal via la commande :
java -jar Cascadia.jar
Dans le terminal s'affichent le menu principal et vous aurez des choix à faire concernant la partie du
jeu.
# 2. Modes de Jeu
Modes disponibles
a. Mode Terminal :
- Une version jouable entièrement dans la console.
- Idéal pour les configurations simples.
b. Mode Graphique avec tuiles carrées :
- Une expérience avec une interface graphique et des tuiles carrées.
c. Mode Graphique avec tuiles hexagonales :
- Jouez avec des tuiles hexagonales (version originale du jeu).
## 3. Menu Principal
Options disponibles :
Mode Solo ou Multijoueur.
Nombre de joueurs : Choisissez entre 2 et 4 joueurs en cas de mode multijoueur.
Nom des joueurs : Entrez un nom unique pour chaque joueur.
Variante famille, intérmediare ou cartes décompte.
Choix des cartes décompte : Sélectionner une carte de score (A, B, C ou D).
Déroulement d’une Partie
# 1. Tour de Jeu
Chaque joueur choisit un lot (une tuile Habitat et son jeton Faune associé).
Placez la tuile sur votre plateau personnel en respectant les règles suivantes :
Une tuile doit toucher une autre déjà posée.
Les habitats peuvent être alignés pour obtenir des points supplémentaires.
Placez un jeton Faune sur une tuile, si autorisé, pour maximiser vos points.
Fin du Tour le lot choisie est remplacé par un autre et vous vous passez au tour suivant.
# 2. Règles spéciales
Jetons Nature :
Obtenez un jeton Nature en plaçant un jeton Faune sur une tuile idéale.
Dépensez un jeton Nature pour :
Choisir indépendamment une tuile et un jeton Faune présents dans les lots disponibles.
Remplacer les jetons Faune disponibles.
# 3. Interface Graphique
Comprendre l’affichage
Plateau personnel :
Votre plateau s’affiche en haut à gauche avec une tuiles habitat de départ.
Les tuiles et jetons placés y sont visibles.
Lots disponibles :
Les lots sont alignés à droite de l’écran. Cliquez pour sélectionner.
Boutons d’action :
Rotate : Tourner une tuile dans la direction des aiguilles d'une horloges avant de la
placer.
Confirm : Valider votre choix.
use nature token : Dépensez un jeton nature pour choisir la tuile et jeton faune
indépendemment.
# 4. Fin de Partie
Une fois les 20 tours de chaque joueur sont terminés, le jeu affiche les scores finaux :
Score des animaux : Calculé selon la carte Faune sélectionnée.
Score des habitats : Basé sur les connexions entre les habitats similaires.
Score des jetons nature : Les jetons nature non dépensés son ajoutés au score.
Scores des bonus habitats
Le joueur avec le plus grand score gagne la partie.
# 5. Conseils pour Bien Jouer
Maximisez vos points en alignant les habitats similaires.
Placez les jetons Faune pour créer des combinaisons stratégiques.
Utilisez vos jetons Nature à bon escient pour optimiser vos choix.
# 6. Problèmes Courants
Si le jeu ne démarre pas :
Vérifiez que Java est installé et que votre commande est correcte.
L’affichage est incorrect :
Assurez-vous que toutes les images sont présentes dans le dossier /resources/images.
# 7. Ressources
Manuel de développement : Consultez dev.pdf pour comprendre les choix techniques.
