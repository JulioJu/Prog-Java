import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

// Author JulioJu
// https://github.com/JulioJu
// MIT Licence
// 2016-05-09
// Le fichier LZWCompressionJu1 a été construit à partir du sujet d'examen de 2015 de Jérôme David (enseignant à l'Université Grenoble Alpes). Le sujet est actuellement disponible à : http://imss-www.upmf-grenoble.fr/~davidjer/prog2/2015ExamProg2.pdf
// See also http://imss-www.upmf-grenoble.fr/~davidjer/prog2/2015ExamProg2.pdf
// https://github.com/wandcs/util/blob/master/LZW/java/LZW.java
// https://fr.wikipedia.org/wiki/Lempel-Ziv-Welch

public class LZWCompressionJu1 extends FilterOutputStream {
    /*
     * La constante et deux attributs suivants
     * sont utilisés par les méthodes writeCode et close.
     * Il ne faut pas les utiliser autre part
     */
    private final static int CODE_SIZE=12;
    private int buffer;
    private int bufferSize;
    /*
     * Dictionnaire qui associe chaque séquence d'octets
     * à son code
     */
    private HashMap<String, Integer> dictionnary;
    /*
     * Le prochain code à utiliser quand une séquence d'octets
     * est ajoutée dans le dictionnaire
     */
    private int maxCode;
    /*
     * La séquence d'octets courante
     */
    private String currentByteSeq;
    public LZWCompressionJu1 (OutputStream out) {
        super(out);
        // instanciation du dictionnaire
        dictionnary=new HashMap<String, Integer>();
        // initialisation du contenu du dictionnaire
        for (int i=0 ; i<= 255 ; i++) {
            dictionnary.put(Integer.toString(i), i);
        }
        maxCode = 255;
        // initialisation de la séquence courante
        currentByteSeq="";
    }

    @Override
    public void write(int b) throws IOException {
        String currentByteSeqAgrand =  currentByteSeq + Integer.toString(b);
        if (dictionnary.containsKey(currentByteSeqAgrand)) {
            currentByteSeq = currentByteSeqAgrand;
        }
        else {
            dictionnary.put(currentByteSeqAgrand, ++maxCode);
            this.writeCode(dictionnary.get(currentByteSeq));
            currentByteSeq = Integer.toString(b);
        }
    }

    @Override
    public void close() throws IOException {
        if (currentByteSeq != "") {
            // il faut supposer que le dernier caractère est un caractère encodé entre 0 et 255, pas de caractères grecs par exemple. D'après moi.
            this.writeCode(dictionnary.get(currentByteSeq));
            // Si il ne reste que 4 bits à écrire, alors
            // on les écrit et on fini par 4 bits à 0
            if (bufferSize>0) {
                writeCode(0);
            }
        }
        super.close();
    }

    /**
     * Méthode qui écrit effectivement
     * les 12 premiers bits de c dans le flux cible.
     * Comme les écritures sont faites uniquement par paquet de 8 bits,
     * un buffer est utilisé.
     * @param c
     * @throws IOException
     */
    protected void writeCode(int c) throws IOException {
        buffer=buffer<<CODE_SIZE;
        buffer|=c;
        bufferSize+=CODE_SIZE;
        while (bufferSize>7){
            super.write(buffer>>>bufferSize-8);
            bufferSize-=8;
        }
    }
}

