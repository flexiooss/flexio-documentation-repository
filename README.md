# flexio-documentation-repository

Stockage de documentations

##Info
- Si getGroups retourne DirectoryException gros probleme, n'est pas censé arriver -> 500
- Si le chemin de dossier n'existe pas, une DirectoryException est soulevé ! -> 404
- Si le dossier est vide retourne liste vide -> 200

##API
- / -> liste des groupes GET
- /{group} -> liste des modules GET
- /{group}/{module} -> liste des versions GET
- /{group}/{module}/{version} -> liste des classifiers GET
- /{group}/{module}/{version}/{classifier} POST GET

##Exemples

Envoie d'un zip:
- ```curl -v 127.0.0.1:8081/flexio-api-documentation/groupe/module/version/classifier --data-binary @"PATH.zip" | json_pp```

Liste des groupes:
- ```curl -v 127.0.0.1:8081/flexio-api-documentation/ | json_pp```

Listes des modules dans le groupe "groupe":
- ```curl -v 127.0.0.1:8081/flexio-api-documentation/groupe | json_pp```

Manifest final :
- ```curl -v 127.0.0.1:8081/flexio-api-documentation/groupe/module/version/classifier | json_pp```