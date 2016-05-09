import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

// Author JulioJu
// https://github.com/JulioJu
// MIT Licence
// 2016-05-09
// Le fichier LZWCompressionJu1 a été construit à partir du sujet d'examen de 2015 de Jérôme David (enseignant à l'Université Grenoble Alpes). Le sujet est actuellement disponible à : http://imss-www.upmf-grenoble.fr/~davidjer/prog2/2015ExamProg2.pdf
// See also http://imss-www.upmf-grenoble.fr/~davidjer/prog2/2015ExamProg2.pdf
// https://github.com/wandcs/util/blob/master/LZW/java/LZW.java
// https://fr.wikipedia.org/wiki/Lempel-Ziv-Welch

public class LZWDecompressionJu1 extends FilterInputStream {
    /*
     * La constante et deux attributs suivants
     * sont utilisés par les méthodes readCode et close.
     * Il ne faut pas les utiliser autre part
     */
    private final static int CODE_SIZE=12;
    private int buffer;
    private int bufferSize = 0;
    /*
     * Dictionnaire qui associe chaque séquence d'octets
     * à son code
     */
    private HashMap<Integer, String> dictionary;
    /*
     * Le prochain code à utiliser quand une séquence d'octets
     * est ajoutée dans le dictionnaire
     */
    private int maxCode;
    /*
     * La séquence d'octets courante
     */
    private String currentByteSeq;
    public LZWDecompressionJu1 (InputStream in) {
        super(in);
        // instanciation du dictionnaire
        dictionary=new HashMap<Integer, String>();
        // initialisation du contenu du dictionnaire
        for (int i=0 ; i<= 255 ; i++) {
            dictionary.put(i, "" + (char)i);
        }
        maxCode = 255;
        // initialisation de la séquence courante
        currentByteSeq="";
    }

    public String readEncode() throws IOException {
        // Si c == -1, c'est pas grave, au prochain coup il revaudra -1
        int c=this.readCode();
        if (c!=-1)
            currentByteSeq = "" + (char)c;
        StringBuilder result= new StringBuilder(currentByteSeq);
        while ((c=this.readCode()) != -1) {
            String entry;
            if (dictionary.containsKey(c)) {
                entry = dictionary.get(c);
            }
            else if (c==maxCode+1) {
                // https://fr.wikipedia.org/wiki/Lempel-Ziv-Welch
                //  Cependant un cas inhabituel apparaît chaque fois que la séquence caractère/chaîne/caractère/chaîne/caractère (avec le même caractère pour chaque caractère et la même chaîne de caractères pour chaque chaîne) est rencontré en entrée et que caractère/chaîne est déjà présent dans la table. Quand l’algorithme de décompression lit le code pour caractère/chaîne/caractère, il ne peut pas le traiter car il n’a pas encore stocké ce code dans la table. Ce cas particulier peut être géré car le programme de décompression sait que le caractère supplémentaire est le précédent caractère rencontré2
                // Tester par exemple avec TTTTTOBEORNOTTOBEORTOBEORNOT#
                System.out.println("Blop, on a caracètre/chaîne/caractère/chaîne/caractère");
                entry = currentByteSeq + currentByteSeq.charAt(0);
            }
            else
                throw new IllegalArgumentException("Bad compressed c " + c);
            dictionary.put(++maxCode, currentByteSeq + entry.charAt(0));
            result.append(entry);
            currentByteSeq = entry;
        }
        return result.toString(); // On est obligé de retourner un String, parce que pour un read il peut arriver qu'on retourne plusieurs caractères. 
    }

    /**
     * Méthode qui écrit effectivement
     * les 12 premiers bits de c dans le flux cible.
     * Comme les écritures sont faites uniquement par paquet de 8 bits,
     * un buffer est utilisé.
     * @param c
     * @throws IOException
     */

    protected int readCode() throws IOException {
        // J'ai vérifié, quand on arrive en fin de fichier, on peut faire plusieurs in.read(), ça renverra toujours -1.
        int c;
        // On vide le buffer dans c, et on rajoute 8 zéro à la fin.
        if (bufferSize == 4) {
            c = buffer << CODE_SIZE - bufferSize; // Décallage de 8 (12 - 4).
        }
        // bufferSize == 0
        // On lit, et on met le contenu de ce qu'on a lu dans c. Si on n'est pas en fin de fichier, on ajoute 4 zéro
        else { 
            c = super.read();
            if (c!=-1) 
                c <<= CODE_SIZE - 8; // Décallage de 4
        }

        buffer = in.read();
        if (buffer == -1) {
            return c;
        }
        else {
            bufferSize = CODE_SIZE - (8+bufferSize); // Si on avait un bufferSize de 4, alors on aura un bufferSize de 0 ; si on avait un bufferSize de 0, alors on aura un bufferSize de 4.
            int bufferTmp = buffer >>> bufferSize;
            buffer <<= 32 - bufferSize;
            buffer >>>= 32 - bufferSize;
            return c | bufferTmp;
        }

    }
}

