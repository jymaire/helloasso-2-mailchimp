Le but de ce programme est de convertir un fichier d'export Hello Asso vers un fichier d'import MailChimp

## Génération d'un fichier .exe

Utilisation de Launch4j

https://sourceforge.net/projects/launch4j/

à remplir : 

Output file : C:\chemin\vers\nouvel\exe\test.exe

Jar : C:\chemin\vers\jar\target\helloasso-to-mailchimp-1.0-SNAPSHOT.jar

Puis dans l'onglet "JRE", mettre le chemin de l'option Bundled JRE Paths ainsi que l'option de JVM `-Dfile.encoding=UTF-8` pour avoir de jolis accents sur Windows

Cliquer sur l'engrenage pour générer le .exe