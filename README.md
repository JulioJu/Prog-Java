# Prog-Java

# Présentation

« Le but du travail est de développer en Java un flux « filtre » permettant de compresser avec LZW des sources de données en format binaire. L'algorithme de compression encode des séquences d'octets (i.e. des séquences de 8 bits) par des codes de longueur fixe de 12 bits (que nous représenteront par des entiers). Pour cela, on utilise un dictionnaire (attribut dictionnary) qui associe des séquences d'octets à son code. Par la suite, les séquences de plusieurs octets rencontrées au fur et à mesure dans les données serontassociées aux codes allant de 256 à 4096 dans le dictionnaire.La compression se déroule de la manière suivante: Lors de l'appel à la méthode write(int b), l'octetà écrire est ajouté à la séquence courante (attribut currentByteSeq). Si la séquence courante existedans le dictionnaire, on ne fait rien, sinon la séquence courante est ajoutée dans le dictionnaire (avec lecode qui correspond au code maximal du dictionnaire), le code correspondant à la séquence courante sansle dernier caractère est effectivement écrit dans le flux sous-jacent via la méthhode writeCode(intc), la séquence courante est remplacée par une nouvelle séquence ne contenant que le dernier caractère.Lors de la fermeture du flux, si la séquence courante n'est pas vide, alors son code est effectivement écrit(et le flux est éventuellement complété pour qu'il soit un multiple de 8 bits). »
Source : http://imss-www.upmf-grenoble.fr/~davidjer/prog2/2015ExamProg2.pdf

## Instruction

1. Dans un terminal se pacer dans `/workspace/monProjet/bin/`, (commande `cd`);

2. ``` echo 'TTTTTOBEORNOTTOBEORTOBEORNOT#' > file.txt  ```

3. ```java LZWTestJu1 file.txt fileSortieEncode.txt fileSortieDecode.txt && cat file.txt && cat fileSortieDecode.txt```


Attention, détruit fileSortieEncode.txt et fileSortieDecode.txt.

Le dictionnaire ne doit pas dépasser 4096 entrées. J'imagine que les caractères doivent être être encodés entre 0 et 255 (par exemple, pas de caractères grecs).

## See also

* http://imss-www.upmf-grenoble.fr/~davidjer/prog2/2015ExamProg2.pdf
* https://github.com/wandcs/util/blob/master/LZW/java/LZW.java
* https://fr.wikipedia.org/wiki/Lempel-Ziv-Welch

Le fichier LZWCompressionJu1 a été construit à partir du sujet d'examen de 2015 de Jérôme David (enseignant à l'Université Grenoble Alpes). Le sujet est actuellement disponible à : http://imss-www.upmf-grenoble.fr/~davidjer/prog2/2015ExamProg2.pdf

Author JulioJu

https://github.com/JulioJu

MIT Licence

2016-05-09
