import java.util.HashMap;
import java.util.Map;

// https://github.com/wandcs/util/blob/master/LZW/java/LZW.java
// Original author : wandcs. 
// Modify by JulioJu

public class LZWByWandcs {
    private static int DICT_SIZE = 256;

    /** Compress a string to a list of output symbols. */
    public static String compress(String uncompressed) {
        // uncompressed = uft8Encode(uncompressed);
        // Build the dictionary.
        int index = DICT_SIZE;
        Map<String,Integer> dictionary = new HashMap<String,Integer>();
        for (int i = 0; i < DICT_SIZE; i++) {
            dictionary.put("" + (char)i, i);
        }

        // System.out.println(dictionary);
        // Iterator<Entry<String, Integer>> it = dictionary.entrySet().iterator();
        // while (it.hasNext()){
        //     // System.out.println(it.next());
        //     Map.Entry<String, Integer> entry = (Map.Entry<String, Integer>) it.next();
        //     System.out.println(entry.getKey() + " " + entry.getValue());
        // }
        // for (Map.Entry<String, Integer> paire : dictionary.entrySet())
        //     System.out.println(paire.getKey() + " " + paire.getValue());
        // dictionary.forEach((k, v)-> System.out.println(k + " " + v));

        String w = "";
        StringBuilder res = new StringBuilder();
        for (char c : uncompressed.toCharArray()) {
            String wc = w + c;
            if (dictionary.containsKey(wc)) {
                w = wc;
                System.out.format("if     %20s %5s\n", res, wc); // Bien pour comprendre
            }

            else {
                int i = dictionary.get(w);
                res.append((char) i);
                System.out.format("else   %20s %5s\n", res, wc); // Bien pour comprendre
                // Add wc to the dictionary.
                dictionary.put(wc, index++);
                w = "" + c;
            }
        }

        // Output the code for w.
        if (!w.equals("")) {
            int i = dictionary.get(w);
            res.append((char) i);
        }
        return res.toString();
    }

    /** Decompress a list of output ks to a string. */
    public static String decompress(String compressed) {
        // Build the dictionary.
        int index = DICT_SIZE;
        Map<Integer,String> dictionary = new HashMap<Integer,String>();
        for (int i = 0; i < DICT_SIZE; i++)
            dictionary.put(i, "" + (char)i);

        char[] chars = compressed.toCharArray();
        String w = "" + chars[0];
        StringBuffer result = new StringBuffer(w);
        for (int j = 1; j < chars.length; j++) {
            int k = (int) chars[j];
            String entry;
            if (dictionary.containsKey(k)) {
                entry = dictionary.get(k);
            }
            else if (k == index) {
                // https://fr.wikipedia.org/wiki/Lempel-Ziv-Welch
                //  Cependant un cas inhabituel apparaît chaque fois que la séquence caractère/chaîne/caractère/chaîne/caractère (avec le même caractère pour chaque caractère et la même chaîne de caractères pour chaque chaîne) est rencontré en entrée et que caractère/chaîne est déjà présent dans la table. Quand l’algorithme de décompression lit le code pour caractère/chaîne/caractère, il ne peut pas le traiter car il n’a pas encore stocké ce code dans la table. Ce cas particulier peut être géré car le programme de décompression sait que le caractère supplémentaire est le précédent caractère rencontré2

                System.out.println("blop");
                entry = w + w.charAt(0);
            }
            else
                throw new IllegalArgumentException("Bad compressed k: " + k);

            result.append(entry);

            // Add w+entry[0] to the dictionary.
            dictionary.put(index++, w + entry.charAt(0));

            w = entry;
        }
        // return utf8Decode(result.toString());
        return result.toString();
    }

    // #<{(|*
    //  * encode utf8 string into char 0 ~ 127
    //  * @param text
    //  * @return
    //  |)}>#
    // public static String uft8Encode(String text) {
    //     StringBuilder res = new StringBuilder();
    //     for (char c : text.toCharArray()) {
    //         int i = (int) c;
    //         if (i < 128) {
    //             res.append((char) i);
    //         } else if (i > 127 && i < 2048) {
    //             int j = (i >> 6) | 192;
    //             res.append((char) j);
    //             j = (i & 63) | 128;
    //             res.append((char) j);
    //         } else {
    //             int j = (i >> 12) | 224;
    //             res.append((char) j);
    //             j = ((i >> 6) & 63) | 128;
    //             res.append((char) j);
    //             j = (c & 63) | 128;
    //             res.append((char) j);
    //         }
    //     }
    //     return res.toString();
    // }
    //
    // #<{(|*
    //  * decode char 0 ~ 127 to utf8 string
    //  * @param text
    //  * @return
    //  |)}>#
    // public static String utf8Decode(String text) {
    //     StringBuilder res = new StringBuilder();
    //     int i = 0;
    //     char[] chars = text.toCharArray();
    //     while (i < chars.length) {
    //         int c = chars[i++];
    //         if (c > 191 && c < 224) {
    //             int c1 = chars[i++];
    //             c = ((c & 31) << 6) | (c1 & 63);
    //         } else if (c > 127) {
    //             int c1 = chars[i++];
    //             int c2 = chars[i++];
    //             c = ((c & 15) << 12) | ((c1 & 63) << 6) | (c2 & 63);
    //         }
    //         res.append((char) c);
    //     }
    //     return res.toString();
    // }

    public static void main(String[] args) {
        String text = "TTTTOBEORNOTTOBEORTOBEORNOT";
        System.out.println(text);
        String compressed = compress(text);
        System.out.println(compressed);
        for (char c : compressed.toCharArray())
            System.out.print("<" + (int)c + ">"); // transformé en code ascii.
        System.out.println();
        String decompressed = decompress(compressed);
        System.out.println(decompressed);

        // String text = "的弄饭呢哦圣达菲欧大海";
        // String t1 = uft8Encode(text);
        // String t2 = utf8Decode(t1);
        // System.out.println(text);
        // System.out.println(t1);
        // System.out.println(t2);
    }
}
