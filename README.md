# flexio-documentation-repository

##Info
- Si getGroups retourne noDir gros probleme, n'est pas censé arriver -> 500
- Si le chemin de dossier n'existe pas, une DirectoryException est soulevé ! -> 404
- Si le dossier est vide retourne liste vide -> 200

##API
- / -> liste des groupes GET
- /{group} -> liste des modules GET
- /{group}/{module} -> liste des versions GET
- /{group}/{module}/{version} -> liste des classifiers GET
- /{group}/{module}/{version}/{classifier} POST GET