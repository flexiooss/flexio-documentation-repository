# flexio-documentation-repository

Stockage de documentations

##Infos
- Si / retourne DirectoryException gros probleme, n'est pas censé arriver, d'où le 500
- Si le chemin de dossier n'existe pas, une DirectoryException est soulevé ! -> 404
- Si le dossier est vide retourne liste vide -> 200
- Pour un POST si le fichier est déjà present (md5) retourne 200, sinon 201

##API
- / -> liste des groupes GET
- /{group} -> liste des modules GET
- /{group}/{module} -> liste des versions GET
- /{group}/{module}/{version} -> liste des classifiers GET
- /{group}/{module}/{version}/{classifier} POST GET

##Exemples

Envoie d'un zip:
- ```curl -v 127.0.0.1:8081/flexio-api-documentation/groupe/module/1.0.0/classifier --data-binary @"PATH.zip" | json_pp```

Liste des groupes:
- ```curl -v 127.0.0.1:8081/flexio-api-documentation/ | json_pp```

Listes des modules dans le groupe "groupe":
- ```curl -v 127.0.0.1:8081/flexio-api-documentation/groupe | json_pp```

Manifest final :
- ```curl -v 127.0.0.1:8081/flexio-api-documentation/groupe/module/1.0.0/classifier | json_pp```

##TODO