import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Missions 3 and 4. Find a word in a long text.
 */
public class Marker {

    private static final long BASE = 31;
    private static final long MOD = 1_000_000_007;

    /**
     * Mission 3a. The prefix function of p. pi[i] = the length of the longest proper prefix of p[0..i] that is
     * also a suffix of p[0..i] ("proper" means shorter than p[0..i] itself).
     * Example: p = "ACGAC" gives [0, 0, 0, 1, 2]. The last number is the "Border length" line.
     */
    public static int[] prefixFunction(String p) {
        int[] pi = new int[p.length()];
        for (int i = 1, matched = 0; i < p.length(); i++) {
            while (matched > 0 && p.charAt(i) != p.charAt(matched)) {
                matched = pi[matched - 1];
            }
            if (p.charAt(i) == p.charAt(matched)) matched++;
            pi[i] = matched;
        }
        return pi;
    }

    /**
     * Mission 3b. KMP. All start positions (counting from 0, in increasing order) where p occurs in text.
     * Overlapping occurrences count: "ACG" occurs in "ACGACG" at 0 and 3, and "AA" in "AAA" at 0 and 1.
     * Use the prefix function: the text pointer never goes back. The number of positions is "Marker hits",
     * the first one is "First hit" (-1 when there is none).
     */
    public static int[] findAll(String text, String p) {
        if (p.isEmpty() || p.length() > text.length()) return new int[0];

        int[] pi = prefixFunction(p);
        List<Integer> hits = new ArrayList<>();
        for (int i = 0, matched = 0; i < text.length(); i++) {
            char letter = text.charAt(i);
            while (matched > 0 && letter != p.charAt(matched)) {
                matched = pi[matched - 1];
            }
            if (letter == p.charAt(matched)) matched++;
            if (matched == p.length()) {
                hits.add(i - p.length() + 1);
                matched = pi[matched - 1];
            }
        }

        int[] positions = new int[hits.size()];
        for (int i = 0; i < positions.length; i++) positions[i] = hits.get(i);
        return positions;
    }

    /**
     * Mission 4. Rabin-Karp. The number of positions i in a such that the word of k letters starting at i
     * (a[i..i+k-1]) also occurs somewhere in b. Count positions, not different words. This is the "Shared words" line.
     * The idea: give every window of k letters a number (a rolling hash, updated in O(1) when the window moves by one),
     * look the number up among the windows of b, and when it is found COMPARE THE LETTERS, because two different words
     * can have the same hash.
     * Example: a = "ACGTAC", b = "TTACGT", k = 4: the words of a are ACGT, CGTA, GTAC; only ACGT is in b, so the answer is 1.
     */
    public static int sharedWords(String a, String b, int k) {
        if (k <= 0 || k > a.length() || k > b.length()) return 0;

        long leadingPower = 1;
        for (int i = 1; i < k; i++) leadingPower = leadingPower * BASE % MOD;

        Map<Long, List<Integer>> startsInB = new HashMap<>();
        long hash = firstHash(b, k);
        for (int start = 0; start <= b.length() - k; start++) {
            startsInB.computeIfAbsent(hash, key -> new ArrayList<>()).add(start);
            if (start < b.length() - k) {
                hash = slideHash(hash, b.charAt(start), b.charAt(start + k), leadingPower);
            }
        }

        int shared = 0;
        hash = firstHash(a, k);
        for (int start = 0; start <= a.length() - k; start++) {
            List<Integer> candidates = startsInB.get(hash);
            if (candidates != null) {
                for (int candidate : candidates) {
                    if (sameWord(a, start, b, candidate, k)) {
                        shared++;
                        break;
                    }
                }
            }
            if (start < a.length() - k) {
                hash = slideHash(hash, a.charAt(start), a.charAt(start + k), leadingPower);
            }
        }
        return shared;
    }

    private static long firstHash(String sequence, int k) {
        long hash = 0;
        for (int i = 0; i < k; i++) hash = (hash * BASE + sequence.charAt(i)) % MOD;
        return hash;
    }

    private static long slideHash(long hash, char outgoing, char incoming, long leadingPower) {
        long remaining = (hash - outgoing * leadingPower % MOD + MOD) % MOD;
        return (remaining * BASE + incoming) % MOD;
    }

    private static boolean sameWord(String a, int startA, String b, int startB, int k) {
        for (int i = 0; i < k; i++) {
            if (a.charAt(startA + i) != b.charAt(startB + i)) return false;
        }
        return true;
    }
}
