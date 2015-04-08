import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * @author Samir Undavia
 *
 * This alogrithm works by first looping through all artists in the file and adds the artists that appear in at least THRESHOLD user-lists to a map.
 * Then, the program loops through each artist in the map and sees if the artist appears with another artist in at least THRESHOLD user-lists.
 * Each of these pairs is added to an ArrayList.
 * Lastly, the program loops through the ArrayList containing the pairs and prints them.
 *
 * This algorithm begins by running at O(N) because it loops through all of the artists in the file.
 * Then, the next loop runs (number of artists that appear in at least THRESHOLD user-lists)^2 times while finding pairs.
 * Lastly, the program has a loop (O(number of pairs)) that prints the pairs.
 * The best-case is O(N) if none of that artists appear in at least THRESHOLD user-lists.
 * The worst-case is O(N^2) if every artist appears in at least THRESHOLD user-lists.
 *
 * With the given file, the algorithm calculated the pairs on my computer in about a third of a second.
 * Furthermore, the number of arists who were in at least THRESHOLD user-lists was significantly smaller than the number of artists,
 * so the algorithm ran at O(N), where N is the number of artists.
 * In this case, there were 45786 artists and 124 artists who appeared in at least THRESHOLD user-lists.
 * There were 101 pairs of artists who appeared in at least THRESHOLD user-lists together.
 *
 */
public class ArtistList {

    private static final int THRESHOLD = 50; // number of lists a pair of artists must appear in together
    private static String filename = "Artist_lists_small.txt";

    private static HashMap<String, ArrayList<Integer>> artistsGreaterThanThreshold; // map of artists that appear in more than THRESHOLD user-lists

    private static ArrayList<String> pairsSolution; // stores pairs that appear together in more than THRESHOLD user-lists

    public static void main(String[] args) throws IOException {
        if (args.length > 0 && !args[0].equals("")) // place file in the same location as the compiled class
            filename = args[0];
        artistsGreaterThanThreshold = new HashMap<>();
        pairsSolution = new ArrayList<>();
        buildMapOfArtists(readFile());
        findPairs();
        printPairs();
    }

    /**
     * Reads the file into a list
     * @return list of lines in file
     * @throws IOException
     */
    private static List<String> readFile() throws IOException {
        return Files.readAllLines(Paths.get(filename), Charset.defaultCharset());
    }

    /**
     * Creates a map of artist names as the key and a list of the user lists the artist appears in.
     * The map only contains artists that appear in more than THRESHOLD lists because an artist must appear in at least
     * THRESHOLD lists in order to be in a pair that appears at least THRESHOLD times.
     * @param listOfUserLists List of user lists from file
     */
    private static void buildMapOfArtists(List<String> listOfUserLists) {
        HashMap<String, ArrayList<Integer>> artistMap = new HashMap<>(); // map to store all artists and the lists they appear in
        for (int i = 0; i < listOfUserLists.size(); i++) { // loop through lines of file
            String[] artists = listOfUserLists.get(i).split(",");
            for (String anArtist : artists) { // loop through each artist in a list in each line
                String artist = anArtist.trim();
                if (!artist.equals("")) {
                    ArrayList<Integer> list = artistMap.get(artist); // list of user lists artist appears in
                    if (!artistMap.containsKey(artist))
                        list = new ArrayList<>();
                    list.add(i); // add user-list index to artist's list of user-lists the artist appears in
                    artistMap.put(artist, list); // add artist with list to map of all artists and lists
                    if (list.size() >= THRESHOLD) { // if the artist appears in more than THRESHOLD lists
                        artistsGreaterThanThreshold.put(artist, list); // add it to the map
                    }
                }
            }
        }
    }

    /**
     *  Finds the pairs that appear in at least THRESHOLD user-lists together and puts them in pairsSolution.
     *  This algorithm only compares artists that appear in at least THRESHOLD lists instead of comparing every artist
     *  because an artist must appear in at least THRESHOLD lists in order to be in a pair that appears in at least
     *  THRESHOLD lists.
     */
    private static void findPairs() {
        Set<String> strings = artistsGreaterThanThreshold.keySet();
        String[] artistNamesArray = strings.toArray(new String[strings.size()]); // array of artists names to loop through

        for (int i = 0; i < artistNamesArray.length; i++) { // loop through artist names

            String artistA = artistNamesArray[i];
            ArrayList<Integer> artistAList = artistsGreaterThanThreshold.get(artistA); // list of user-lists artist A appears in

            for (int j = i; j < artistNamesArray.length; j++) { // loop through all other artists in the map
                String artistB = artistNamesArray[j];

                if (!artistA.equals(artistB)) { // if not comparing the same artists
                    ArrayList<Integer> artistBList = artistsGreaterThanThreshold.get(artistB); // list of user-lists artist B appears in

                    ArrayList<Integer> aAndBListIntersection = new ArrayList<>(artistAList); // make a copy of artistAList
                    aAndBListIntersection.retainAll(artistBList); // get the intersection of artistA and artistB lists

                    if (aAndBListIntersection.size() >= THRESHOLD) // if the artists are in more than THRESHOLD lists together
                        pairsSolution.add(artistA + ", " + artistB); // add the pair to the list of pairs
                }
            }
        }
//        Collections.sort(pairsSolution); // sort the pairs -- not sorting to save time
    }

    /**
     * Prints the pairs that appear in at least THRESHOLD lists together.
     */
    private static void printPairs() {
        for (String pair : pairsSolution) {
            System.out.println(pair);
        }
    }

}