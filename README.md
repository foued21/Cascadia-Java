# Manuel Utilisateur – Cascadia en ligne

> Réalisé en binôme avec **Hassina138**

---


## Introduction

Bienvenue dans **Cascadia en ligne**, une version numérique du jeu de société. Ce manuel vous guide pas à pas pour comprendre les règles et maîtriser l'interface de jeu.

---

## Démarrage

### 1. Lancement du jeu

Assurez-vous que votre environnement Java est configuré correctement. Dans un terminal, exécutez le fichier principal via la commande :

```bash
java -jar Cascadia.jar
```

Dans le terminal s'affichent le menu principal et vous aurez des choix à faire concernant la partie du jeu.

---

### 2. Modes de Jeu

**a. Mode Terminal**
- Une version jouable entièrement dans la console.
- Idéal pour les configurations simples.

**b. Mode Graphique avec tuiles carrées**
- Une expérience avec une interface graphique et des tuiles carrées.

**c. Mode Graphique avec tuiles hexagonales**
- Jouez avec des tuiles hexagonales (version originale du jeu).

---

### 3. Menu Principal

Options disponibles :
- Mode **Solo** ou **Multijoueur**.
- **Nombre de joueurs** : Choisissez entre 2 et 4 joueurs en cas de mode multijoueur.
- **Nom des joueurs** : Entrez un nom unique pour chaque joueur.
- **Variante** : famille, intermédiaire ou cartes décompte.
- **Choix des cartes décompte** : Sélectionnez une carte de score (A, B, C ou D).

---

## Déroulement d'une Partie

### 1. Tour de Jeu

1. Chaque joueur choisit un **lot** (une tuile Habitat et son jeton Faune associé).
2. Placez la tuile sur votre plateau personnel en respectant les règles suivantes :
   - Une tuile doit toucher une autre déjà posée.
   - Les habitats peuvent être alignés pour obtenir des points supplémentaires.
3. Placez un **jeton Faune** sur une tuile, si autorisé, pour maximiser vos points.
4. **Fin du tour** : le lot choisi est remplacé par un autre et le tour passe au joueur suivant.

---

### 2. Règles Spéciales

**Jetons Nature :**
- Obtenez un jeton Nature en plaçant un jeton Faune sur une tuile idéale.
- Dépensez un jeton Nature pour :
  - Choisir indépendamment une tuile et un jeton Faune présents dans les lots disponibles.
  - Remplacer les jetons Faune disponibles.

---

### 3. Interface Graphique

**Plateau personnel**
- Votre plateau s'affiche en haut à gauche avec une tuile habitat de départ.
- Les tuiles et jetons placés y sont visibles.

**Lots disponibles**
- Les lots sont alignés à droite de l'écran. Cliquez pour sélectionner.

**Boutons d'action**

| Bouton | Description |
|---|---|
| `Rotate` | Tourner une tuile dans le sens des aiguilles d'une montre avant de la placer. |
| `Confirm` | Valider votre choix. |
| `Use Nature Token` | Dépenser un jeton nature pour choisir la tuile et le jeton faune indépendamment. |

---

### 4. Fin de Partie

Une fois les 20 tours de chaque joueur terminés, le jeu affiche les **scores finaux** :

- 🐻 **Score des animaux** : Calculé selon la carte Faune sélectionnée.
- 🌲 **Score des habitats** : Basé sur les connexions entre les habitats similaires.
- 🍃 **Score des jetons nature** : Les jetons nature non dépensés sont ajoutés au score.
- ⭐ **Bonus habitats** : Points supplémentaires selon les configurations.

> Le joueur avec le plus grand score gagne la partie.

---

### 5. Conseils pour Bien Jouer

- Maximisez vos points en **alignant les habitats similaires**.
- Placez les jetons Faune pour créer des **combinaisons stratégiques**.
- Utilisez vos jetons Nature à bon escient pour **optimiser vos choix**.

---

### 6. Problèmes Courants

**Le jeu ne démarre pas**
- Vérifiez que Java est installé et que votre commande est correcte.

**L'affichage est incorrect**
- Assurez-vous que toutes les images sont présentes dans le dossier `/resources/images`.

---

### 7. Ressources

- 📄 **Manuel de développement** : Consultez `dev.pdf` pour comprendre les choix techniques.