# Assignment 4. Patient Zero

Student ID **254488**

See [START_HERE.md](START_HERE.md) for setup and [Assignment 4](docs/Assignment4.pdf) for the specification.

## Program output

Results from `java -cp out Main` on the generated variant.

    Mutations: 65
    Shared core: 1195
    Marker hits: 6
    First hit: 5421
    Border length: 4
    Shared words: 666
    Tests needed: 38
    Cheapest testing: 169
    Longest chain: 27

My outbreak report (a screenshot of `output/outbreak.html`):

![outbreak](output/outbreak.png)

## Questions

Draft explanations to review and rewrite in your own words before submitting.

Mission 1. What does the cell d[i][j] of your table mean, and how is it computed from other cells?

`d[i][j]` is the minimum number of edits needed to turn the first `i` letters of A into the first `j` letters of B. Take the minimum of the cell above plus one for deletion, the cell to the left plus one for insertion, and the diagonal plus zero or one depending on whether the last letters agree. An empty prefix needs one insertion or deletion per letter in the other prefix.

Mission 2. How is the longest common subsequence different from the longest common substring? My Shared core is bigger than the number of letters that are the same at the same place: why?

A common subsequence can skip letters in either strain while keeping the letters in order. A common substring has to be contiguous in both strains. An insertion can shift every later letter by one position, but the LCS can still pair those letters at their new positions.

Mission 3. What does the prefix function tell you, and why does the pointer in the text never go back in KMP? What is the Border length of my marker and what does it mean?

For each prefix ending at index `i`, `pi[i]` gives the length of its longest proper prefix that is also its suffix. On a mismatch KMP shortens the current match using `pi` and retries the same text letter, since the suffix of the text already read still matches the shorter prefix. The marker `ATCGCAGATCG` has border length 4 because its longest proper border is `ATCG`.

Mission 4. Why must you compare the letters after the hashes are equal? What happens to the cost of Rabin-Karp if many windows have the same hash?

Two different words can have the same hash, so the program checks their letters before counting a match. Rolling the hash still takes constant time per shift, but a crowded hash bucket can make each window of A check many candidate windows of B, at up to `k` letter comparisons per candidate.

Mission 5. What are the two states of a person in your DP, and how do you get the answer for a person from its children? Why does a greedy "test the person with most links first" not always give the cheapest answer?

For each subtree, I compute its minimum cost with the root tested and its minimum cost with the root untested. If the root is tested, I add its price and the cheaper state for each child. If it is untested, every child must be tested to cover the link to the root. In a star with a center that costs 10 and three leaves that cost 1 each, testing the three leaves costs 3 even though the center has the most links.

## AI use

OpenAI Codex generated the first implementation of the mission algorithms and drafted the README explanations using the provided starter project and course hints. It also generated the variant data with the supplied tool, ran the checks and benchmark, and captured the lab screenshot.

## Timing (Mission 7)

Milliseconds, average of 3 runs, from `java -cp out Bench`.

| test | size | time |
|---|---|---|
| naive search | 20 000 / 40 000 / 80 000 | 37.1 / 93.6 / 202.7 |
| KMP | 20 000 / 40 000 / 80 000 | 3.8 / 2.3 / 0.6 |
| edit distance | 500 / 1000 / 2000 / 4000 | 13.0 / 17.2 / 68.4 / 213.1 |

With the pattern held at 2,000 letters, doubling the text increased naive search time by about 2.5× and then 2.2×. The measured KMP times fell to about 61% and 26% of the previous measurement, likely because of JVM warm-up and noise at this scale, although the algorithm does linear work. Doubling both strains grew the edit-distance times by about 1.3×, 4.0× and 3.1×, while the DP table itself grows by roughly 4× on each doubling.

## Complexity

`n` and `m` are the lengths of the two strings for each algorithm. For KMP, `n` is the text length, `m` the marker length and `h` the number of hits. For Rabin–Karp, `k` is the window length and `c` is the number of candidate pairs examined. The tree has `v` people.

| algorithm | time | space | assumptions |
|---|---|---|---|
| Edit distance | O(nm) | O(nm) | One constant-time update per cell of the full table. |
| LCS | O(nm) | O(nm) | One constant-time update per cell of the full table. |
| Prefix function and KMP | O(n + m) | O(m + h) | The prefix function takes O(m) time and space. KMP stores all hit positions. |
| Rabin–Karp (shared words) | O(n + m + kc) with expected constant-time map lookups | O(m) | Each shift takes constant time. In the worst case, every window of A can check every window of B, giving O(nmk) time. |
| Tree DP and diameter | O(v) | O(v) | A constant number of traversals of the tree. The recursion stack can grow to the height of the tree. |

## Before you submit

- Review and adapt the draft implementation and explanations to meet the course's own-work requirement.
- Run `java tools/Check.java` after any changes.
- Verify that the public repository link opens and submit it in Moodle.
