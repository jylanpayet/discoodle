echo ""
echo "  _____  _                         _ _
 |  __ \(_)                       | | |
 | |  | |_ ___  ___ ___   ___   __| | | ___
 | |  | | / __|/ __/ _ \ / _ \ / _  | |/ _ \\
 | |__| | \__ \ (_| (_) | (_) | (_| | |  __/
 |_____/|_|___/\___\___/ \___/ \__,_|_|\___|

                                            "
if [ $# == 2 ] || [ $# == 1 ]; then
  echo -e "\033[32;1m[ATTENTION] N'éteignez pas le programme avec un Ctrl+c au risque de laisser le port 8080 actif !\033[m"
  sleep 2
  echo ""
  clear
  if [ $# == 2 ] && [ $2 != "no-bdd" ] || [ $# == 1 ]; then
    echo "Quel est votre mot de passe mariadb ? :"
    mariadb -u $1 -p -e 'CREATE DATABASE IF NOT EXISTS discoodle  DEFAULT CHARSET = utf8mb4 DEFAULT COLLATE = utf8mb4_unicode_ci'
    if [ $? != 0 ]; then
      echo -e "\033[32;1m[ERREUR] Impossible d'exécuter le script SQL. Veuillez rééssayer.\033[m"
      exit
    fi
  fi
  cd api
  mvn spring-boot:run &
  sleep 15
  cd ../frontend
  npm run serve
elif [ $# == 0 ]; then
  echo -e "\033[31;1mVous n'avez pas entré votre nom d'utilisateur mariadb en argument du programme\033[m"
  echo ""
  echo "Veuillez rééssayer avec la commande suivante :"
  echo -e "\033[32;1m./discoodle.sh mariadbUserName\033[m"
else
  echo -e "\033[31;1m [ERREUR] Vous avez entré un trop grand nombre d'arguments. \033[m"
fi
