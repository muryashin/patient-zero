/**
 * Missions 1 and 2. Compare two strains with dynamic programming.
 *
 * Both are the same kind of table: d[i][j] answers a question about the first i letters of a and the first j letters of b.
 * Think about three things before you type: what d[i][j] means, how it follows from smaller cells, and what the first row and column are.
 */
public class Strains {

    /**
     * Mission 1. The smallest number of single-letter changes that turn a into b. One change is: replace a letter,
     * insert a letter, or delete a letter. This number is the "Mutations" line.
     * Example: "ACGT" -> "AGT" is 1 (delete C). "GATTACA" -> "GCATGCA" needs 3.
     */
    public static int editDistance(String a, String b) {
        int[][] distance = new int[a.length() + 1][b.length() + 1];
        for (int i = 0; i <= a.length(); i++) distance[i][0] = i;
        for (int j = 0; j <= b.length(); j++) distance[0][j] = j;

        for (int i = 1; i <= a.length(); i++) {
            for (int j = 1; j <= b.length(); j++) {
                int deletion = distance[i - 1][j] + 1;
                int insertion = distance[i][j - 1] + 1;
                int replacement = distance[i - 1][j - 1]
                        + (a.charAt(i - 1) == b.charAt(j - 1) ? 0 : 1);
                distance[i][j] = Math.min(replacement, Math.min(deletion, insertion));
            }
        }
        return distance[a.length()][b.length()];
    }

    /**
     * Mission 2. The length of the longest common subsequence: letters that appear in both strains in the same order,
     * but not necessarily next to each other. This is the "Shared core" line.
     * Example: "ACGTT" and "AGTC": the longest is "AGT", length 3.
     */
    public static int lcsLength(String a, String b) {
        int[][] lengths = new int[a.length() + 1][b.length() + 1];
        for (int i = 1; i <= a.length(); i++) {
            for (int j = 1; j <= b.length(); j++) {
                if (a.charAt(i - 1) == b.charAt(j - 1)) {
                    lengths[i][j] = lengths[i - 1][j - 1] + 1;
                } else {
                    lengths[i][j] = Math.max(lengths[i - 1][j], lengths[i][j - 1]);
                }
            }
        }
        return lengths[a.length()][b.length()];
    }
}
