import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;


// Author JulioJu
// https://github.com/JulioJu
// MIT Licence
// 2016-05-09
// Le fichier LZWCompressionJu1 a été construit à partir du sujet d'examen de 2015 de Jérôme David (enseignant à l'Université Grenoble Alpes). Le sujet est actuellement disponible à : http://imss-www.upmf-grenoble.fr/~davidjer/prog2/2015ExamProg2.pdf
// See also http://imss-www.upmf-grenoble.fr/~davidjer/prog2/2015ExamProg2.pdf
// https://github.com/wandcs/util/blob/master/LZW/java/LZW.java
// https://fr.wikipedia.org/wiki/Lempel-Ziv-Welch

// TESTER AVEC DES CARACTÈRES ASCII, PAR EXMEPLE dans « args[0] » on met : TTTTTOBEORNOTTOBEORTOBEORNOT# 
// Attention, détruit le fichier désigné par args[1] et args[2].
// Pour l'exécuter, en ligne de commandes se placer dans /workspace/monProjet/bin/
// Puis 
//  java LZWTestJu1 file.txt fileSortieEncode.txt fileSortieDecode.txt
// file.txt doit être dans le bin.


public class LZWTestJu1 {
    public static void main(String[] args) throws IOException {
        if (args.length==3) {
            String nomFichierEntree = args[0];
            String nomFichierSortieEncodee = args[1];
            String nomFichierSortieDecodee = args[2];

            Path fileEntree = Paths.get(nomFichierEntree);
            Path fileSortieEncodee = Paths.get(nomFichierSortieEncodee);
            Path fileSortieDecodee = Paths.get(nomFichierSortieDecodee);

            {
                InputStream in = null;
                LZWCompressionJu1 out = null;

                try {
                    in = new BufferedInputStream (Files.newInputStream(fileEntree));
                    out = new LZWCompressionJu1 (new BufferedOutputStream (Files.newOutputStream(fileSortieEncodee)));

                    int c=-1;
                        while ((c=in.read())!=-1) {
                            out.write(c);
                    }
                }
                catch (NoSuchFileException e) {
                    e.printStackTrace();
                }
                finally {
                    if (in!=null)
                        in.close();
                    if (out!=null)
                        out.close();
                }
            }

            { 
                LZWDecompressionJu1 in = null;
                BufferedOutputStream out = null;
                try {
                    in = new LZWDecompressionJu1 (new BufferedInputStream (Files.newInputStream(fileSortieEncodee)));
                    out = new BufferedOutputStream (Files.newOutputStream(fileSortieDecodee));

                    String sortie = in.readEncode();
                    for (int c : sortie.toCharArray())
                        out.write(c);
                }
                catch (NoSuchFileException e) {
                    e.printStackTrace();
                }
                finally {
                    if (in!=null)
                        in.close();
                    if (out!=null)
                        out.close();
                }
            }

        }
        else {
            throw new RuntimeException("Nombre d'arguments incorrect.");
        }
    }
}
