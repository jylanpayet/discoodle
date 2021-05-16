## <div id="issues">Feuille de route des issues</div>

#### <ins>_DISCOO-START_</ins> :
Nous avons, lors du premier sprint, réfléchi aux technologies vers lesquelles nous allions
nous tourner : soit java pour la globalité du projet, soit des technologies web pour le frontend,
avec du Java pour le backend.
Nous avons trouvé plus intéressant (que ce soit sur un aspect d’apprentissage mais également
sur un aspect rendu afin de répondre pour le mieux au cahier des charges comme le fait de rendre
l’application la plus interactive possible...) de choisir VueJS qui est un framework JavaScript en
frontend, Spring qui est un framework Java pour le backend, Maven pour construire le projet
et MariaDB pour stocker les données des différents utilisateurs ainsi que le résultat de leurs
actions sur notre application.

#### <ins>_DISCOO-CHAT_</ins> :
Durant ce second sprint, nous nous sommes familiarisés avec les technologies évoquées au premier sprint,
nous avons commencé à travailler la structure et le design sur Figma du pattern global du site et
de l’accueil. Nous avons également commencé la mise en place des premières fonctionnalités côté
backend (Enregistrement des utilisateurs avec le système d’authentification, envoie de messages
entre plusieurs utilisateurs... en stockant toutes les données nécessaires).

#### <ins>_DISCOO-UPDATE_</ins> :
Apres avoir obtenu un chat assez fonctionnel et des classes pour les utilisateurs qui traitaient
tous les cas souhaités, nous avons décidé pour le troisième sprint de finaliser totalement la partie
chat en nous attardant sur l’intégration d’un système d’envoi d’émojis et celui d’upload de fichiers
(qui nous serviraient aussi pour la page d’informations d’un utilisateur qui souhaiterait upload
une image d’avatar ou pour la remise d’examens ou le partage de fichiers importants par les
professeurs pour les étudiants...).
Nous avons implémenté des Flux RSS dans la page d’accueil afin que les utilisateurs puissent trouver
facilement les dernières informations du monde universitaire.

#### <ins>_DISCOO-DLE_</ins> :
Désormais que la partie Discord était en partie réalisée sur le site (nous avions pour objectif
d’octroyer les fonctionnalités de salons vocaux, de partage d’écran... propre au protocole nommé
WebRTC mais cela nous semblait plus pertinent de s’y attarder en fin de projet si il nous restait
du temps), il fallait, avec ce quatrième sprint, plancher sur la structure axée Moodle et les fonctionnalités qui
étaient directement impliquée à son implémentation (Affiliation d’un utilisateur a un établissement,
cours en commun, page d’information sur les différentes matières, système de publication et
d’enregistrement des notes attribués aux étudiants...).
Nous avons esquissé un système de groupes qui permettrait d'avoir une sorte d'arborescence de
Discoodle afin que chaque élément soit facilement manipulable par tous les acteurs de l'application
(Administrateur, directeur d'établissement...) : nous avons un groupe principal qui est Discoodle
puis nous avons des sous-groupes qui incarnent chacun un établissement (ils sont tous au même niveau),
puis au niveau d'en dessous nous avons les licences/masters de cet établissement si c'est une université ou
les différents classes si c'est un collège... puis nous avons les différentes années de
la licence/le master concerné (toujours dans le cadre d'une université) ou directement les matières des
classes évoquées précédemment pour un collège...
Mais cette structure est totalement modulable ! Il est possible de créer n'importe
quel pattern souhaité tant que l'utilisateur possède les droits nécessaires pour les sous-groupes (il faut faire une demande pour créer un établissement).
Les droits (créer un sous-groupe, parler dans le chat, modifier un cours...) sont accordés en fonction du rôle attribué sur l'infrastructure : les rôles sont
totalement customisables que ce soit le nom ou les permissions.
Chaque groupe possède un serveur qui lui est dédié, celui-ci est constitué d'un ou plusieurs salons.
Nous avons également intégré le protocole WebRTC évoqué plus haut afin que les professeurs puissent
partager le caméra et leur écran dans la page de cours. Il peut également lancer un Quizz pour
interroger les étudiants.

#### <ins>_DISCOO-BUILD_</ins> :
Nous devions recalibrer notre structure, lors de ce cinquième sprint, car nous avions établi un processus
de sauvegarde avec la base de données en MariaDB pour les utilisateurs, les différents groupes...
mais nous stockions également dans des fichiers JSON certaines informations comme les messages,
les notes... et monsieur Degorre trouvait qu’il aurait été plus cohérent de tout centraliser soit
sous format Json soit dans la base de données : nous avons pris la décision de tout mettre dans
la base de données.
Nous avons mis en place un script dans le but d'avoir un projet facilement executable.
Nous avons essayé d'intégrer le projet dans un serveur web (build) mais lors du build de VueJS,
le frontend fonctionnait à 60%... donc nous avons donc décidé de ne pas l'implémenter.

#### <ins>_DISCOO-HAPPY-END_</ins> :
Il s'est avéré que ce dernier sprint fut un grand nettoyage avec l’annotation de commentaires
décrivant de manière simple et explicite les lignes de code. Nous avons aussi à finir de fixer
les derniers bugs qui étaient passés entre les mailles du filet.


Regardez, de manière plus détaillée, [les issues](https://gaufre.informatique.univ-paris-diderot.fr/somon/discoodle/issues) créées afin de prendre connaissance du travail réalisé, des améliorations déjà effectuées
ainsi que des problèmes rencontrés.
