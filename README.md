<p align="center">
<a href="https://gaufre.informatique.univ-paris-diderot.fr/somon/discoodle">
    <img src="https://i.ibb.co/rGY9HVC/Discoodle.png" alt="Discoodle">
</a>
</p>

<h3 align="center">Discoodle - La meilleure plateforme d'éducation et de communication</h3>

  <p align="center">
    <a href="https://discoodle.fr/">Voir la démonstration</a>
    ·
    <a href="https://gaufre.informatique.univ-paris-diderot.fr/somon/discoodle/issues">Signaler des bugs</a>
    ·
    <a href="https://gaufre.informatique.univ-paris-diderot.fr/somon/discoodle/issues">Faire une requête d'amélioration</a>
  </p>

<details open="open">
  <summary>Table des matières</summary>
  <ol>
    <li>
      <a href="projet">À propos du projet</a>
      <ul>
        <li><a href="#langages-et-outils">Langages et outils utilisés</a></li>
      </ul>
    </li>
    <li>
      <a href="#commencer">Pour commencer</a>
      <ul>
        <li><a href="#prerequis">Prérequis</a></li>
        <li><a href="#demarrage-global">Démarrage de l'intégralité du projet</a></li>
      </ul>
    </li>
    <li><a href="#tests">Les tests à réaliser</a></li>
    <li><a href="#issues">La feuille de route des issues</a></li>
    <li><a href="#auteurs">Auteurs</a></li>
    <li><a href="#remerciements">Remerciements</a></li>
  </ol>
</details>

## <div id="projet">À propos du projet</div>

<p align="center">
    <img src="https://i.ibb.co/dgZHrw0/project-screenshot.png" alt="Discoodle" width="1000" height="500">
</p>

Discoodle a été réalisé suite à une proposition du projet par monsieur Degorre, qui découlait de la situation sanitaire actuelle :  

_Un jour, la covid est arrivée, et malheureusement le monde fut bouleverser... dont l'ordre public et son 
infrastructure scolaire : les étudiants ne pouvaient plus se déplacer en cours car le virus était partout et 
empêchait toutes les interactions sociales, les professeurs ne pouvaient assurer leurs cours dans de bonnes 
conditions et devaient se creuser la tête afin de pouvoir réunir diverses outils pour communiquer facilement 
avec les élèves tout en ayant une plateforme qui leur permettait de mettre à disposition de tous les éléments 
essentiels au bon déroulement du cursus d'apprentissage... Le soucis c'est que chacun ne procédait pas de la 
même façon et nous pouvions passer d'une plateforme à une autre entre le cours de d'AAL et celui de POO, par exemple.  
Heureusement, une équipe de développeurs hors du commun se mit à réaliser une application révolutionnaire : Discoodle !_

Pourquoi Discoodle ?

* Discoodle a pour objectif de faciliter la communication entre les différents acteurs d'un établissement (professeurs/étudiants, professeurs/professeurs, étudiants/étudiants, directeur/professeurs...).
* Il permet également de centraliser toutes les informations telles que les polycopiés/documents de cours, les devoirs à réaliser, les dates d'examen...
* Mais de plus, Discoodle a été réaliser de sorte à ce que chaque utilisateur puisse forger l'application en 
  fonction de ses propres besoins : création de salons personnalisables, emploie du temps modifiable 
  selon nos tâches quotidiennes...

### <div id="langages-et-outils"><ins>Langages et outils utilisés</ins></div>

* [Vue.js](https://vuejs.org/) - Le framework web utilisé
* [Spring](https://spring.io/) - Le framework backend utilisé
* [Maven](https://maven.apache.org/) - L'outil de gestion des dépendances utilisé
* [JUnit](https://junit.org/junit5/) - Le framework de test unitaire pour le backend

## <div id="commencer">Pour commencer</div>

Nous allons vous faire part de toutes les instructions afin d'avoir le projet sur votre machine et 
d'installer toutes les dépendances nécessaires pour la bonne compilation du projet.

### <div id="prerequis"><ins>Prérequis</ins>
Vous aurez besoin d'avoir sur votre machine :
* [MariaDB](https://mariadb.org/download/)
* [Node.js](https://nodejs.org/fr/)
* [Maven](https://maven.apache.org/download.cgi)
* [npm](https://www.npmjs.com/get-npm)

_NB : npm est installé avec Node.js, il ne sera donc pas nécessaire de l'installer._  

### <div id="demarrage-global"><ins>Démarrage de l'intégralité du projet</ins></div>

Avant de compiler pour la première fois le projet, pensez à exécuter la commande suivante:
```
cd frontend && npm install
```
Ensuite, depuis un terminal Unix, lancez la commande suivante à la racine du projet:
```
./discoodle.sh [user] no-bdd
```
Où user est le nom d'utilisateur MariaDB et no-bdd est un argument optionnel.
#### <ins>_Uniquement le frontend_</ins> :

- Lancer le frontend

```
cd frontend
npm install
npm run serve 
```

Pour customiser la configuration : [ici](https://cli.vuejs.org/config/).

#### <ins>_Uniquement le backend_</ins> :

- Lancer le backend

```
cd api
mvn spring-boot:run
```


## <div id="tests">Les tests à réaliser</div>

*Noter tous les tests envisageables*

## <div id="issues">Feuille de route des issues</div>

#### <ins>_DISCOO-START_</ins> :
Nous avons lors du premier sprint...

#### <ins>_DISCOO-WEBRTC_</ins> :
Durant ce second sprint, ...

#### <ins>_DISCOO-API_</ins> :
Nous avons durant ce troisième sprint, ...

#### <ins>_DISCOO-WEBRTC_</ins> :
Lors de ce quatrième sprint, ...

#### <ins>_DISCOO-BUILD_</ins> :
Le cinquième sprint rime avec "dint" (signifiant force) car nous voyons le rendu final de notre 
travail : ...

#### <ins>_DISCOO-HAPPY-END_</ins> :
Il s'est avéré que ce dernier sprint fut l'élément manquant car ...

Regardez, de manière plus détaillée, [les issues](https://gaufre.informatique.univ-paris-diderot.fr/somon/discoodle/issues) créées afin de prendre connaissance du travail réalisé, des améliorations déjà effectuées 
ainsi que des problèmes rencontrés.


## <div id="issues">TODO</div>
#### <ins>_Les différentes idées possibles à intégrer_</ins> :
- améliorer le webrtc en faisant en sorte que le système fonctionne pour des 
  conversations entre différents utilisateurs (au sein d'une conversation...).
- faire en sorte que l'application soit fonctionnelle sous forme d'application bureau (et 
  toujours web évidemment) afin de faciliter son utilisation.
- améliorer la sécurité en instaurant un système de tokens lorsqu'une action s'effectue 
  côté back afin de prévenir des failles CSRF.
- intégrer un système d'authentification grâce gmail afin de rendre plus intuitive 
  l'inscription à la plateforme.
- améliorer l'api en rajoutant quelques fonctionnalités pour l'intégration des patterns, 
  l'auto-sauvegarde régulière des BDDs...
- rajouter des limitations à l'upload des fichiers avec timers prédéfinis...

## <div id="auteurs">Auteurs</div>

* **SOMON Bastian** - [@somon](https://gaufre.informatique.univ-paris-diderot.fr/somon)
* **ALEXANDRE Dylan** - [@alexandr](https://gaufre.informatique.univ-paris-diderot.fr/alexandr)
* **PAYET Jylan** - [@payetj](https://gaufre.informatique.univ-paris-diderot.fr/payetj)
* **CLIPAL Moïse** - [@clipal](https://gaufre.informatique.univ-paris-diderot.fr/clipal)

<!-- REMERCIEMENTS -->
## <div id="remerciements">Remerciements</div>

* [DEGORRE Aldric](https://www.irif.fr/users/adegorre/index)
