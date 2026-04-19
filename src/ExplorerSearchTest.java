import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

public class ExplorerSearchTest {
    @Test
    public void testReachableArea_someUnreachable() {
        int[][] island = {
            {1,1,1,3,1,1},
            {3,2,3,1,3,1},
            {1,1,1,1,3,3},
            {3,1,2,1,0,1},
            {1,1,1,2,1,1},
        };
        int actual = ExplorerSearch.reachableArea(island);
        assertEquals(14, actual);
    }

    @Test
    public void testReachableArea_AllUnreachable() {
        int[][] island = {
            {2,2,2,3,2,2},
            {3,2,3,2,3,2},
            {2,2,2,2,3,3},
            {3,2,2,2,0,2},
            {2,2,2,2,2,2},
        };
        int actual = ExplorerSearch.reachableArea(island);
        assertEquals(1, actual);
    }
    
    @Test
    public void testReachableArea_AllReachable() {
        int[][] island = {
            {1,1,1},
            {1,0,1},
            {1,1,1},
        };
        int actual = ExplorerSearch.reachableArea(island);
        assertEquals(9, actual);
    }

    // Add more tests here!
    // Come up with varied cases
    @Test
    public void testExplorerLocation_someUnreachable() {
        int[][] island = {
            {1,1,1,3,1,1},
            {3,2,3,1,3,1},
            {1,1,1,1,3,3},
            {3,1,2,1,0,1},
            {1,1,1,2,1,1},
        };
        int[] actual = ExplorerSearch.explorerLocation(island);
        int[] expected = new int[]{3, 4};
        assertArrayEquals(expected, actual);
    }
    @Test
    public void testExplorerLocation_DoesNotExist() {
        int[][] island = {
            {1,1,1,3,1,1},
            {3,2,3,1,3,1},
            {1,1,1,1,3,3},
            {3,1,2,1,1,1},
            {1,1,1,2,1,1},
        };
        assertThrows(IllegalArgumentException.class, () -> {
            ExplorerSearch.explorerLocation(island);
        });
    }

    @Test
    public void testPossibleMoves_allDirectionsOpen() {
        int[][] enclosure = {
            {1, 1, 1},
            {1, 0, 1},
            {1, 1, 1}
        };
        int[] location = {1, 1};
        List<int[]> moves = ExplorerSearch.possibleMoves(enclosure, location);
        Set<String> moveSet = toSet(moves);

        assertEquals(4, moves.size());
        assertTrue(moveSet.contains("0,1")); // up
        assertTrue(moveSet.contains("2,1")); // down
        assertTrue(moveSet.contains("1,0")); // left
        assertTrue(moveSet.contains("1,2")); // right
    }

    @Test
    public void testPossibleMoves_allDirectionsBlockedByWalls() {
        int[][] enclosure = {
            {2, 2, 2},
            {3, 0, 2},
            {2, 3, 2}
        };
        int[] location = {1, 1};
        List<int[]> moves = ExplorerSearch.possibleMoves(enclosure, location);
        assertTrue(moves.isEmpty());
    }

    @Test
    public void testPossibleMoves_partialEdge() {
        int[][] enclosure = {
            {0, 1, 1}
        };
        int[] location = {0, 0};
        List<int[]> moves = ExplorerSearch.possibleMoves(enclosure, location);
        Set<String> moveSet = toSet(moves);

        assertEquals(1, moves.size());
        assertTrue(moveSet.contains("0,1")); // only right
    }

    @Test
    public void testPossibleMoves_oneOpen_twoWalls_oneEdge() {
        int[][] enclosure = {
            {2, 2, 2},
            {1, 0, 1},
            {2, 2, 2}
        };
        int[] location = {1, 1};
        List<int[]> moves = ExplorerSearch.possibleMoves(enclosure, location);
        Set<String> moveSet = toSet(moves);

        assertEquals(2, moves.size());
        assertTrue(moveSet.contains("1,2"));
        assertTrue(moveSet.contains("1,0"));
    }

    @Test
    public void testPossibleMoves_blockedAboveByWall() {
        int[][] enclosure = {
            {1, 2, 1},
            {1, 0, 1},
            {1, 1, 1}
        };
        int[] location = {1, 1};
        List<int[]> moves = ExplorerSearch.possibleMoves(enclosure, location);
        Set<String> moveSet = toSet(moves);

        assertFalse(moveSet.contains("0,1")); // up blocked
        assertTrue(moveSet.contains("2,1"));  // down open
        assertTrue(moveSet.contains("1,0"));  // left open
        assertTrue(moveSet.contains("1,2"));  // right open
    }

    @Test
    public void testPossibleMoves_blockedBelowByWall() {
        int[][] enclosure = {
            {1, 1, 1},
            {1, 0, 1},
            {1, 2, 1}
        };
        int[] location = {1, 1};
        Set<String> moveSet = toSet(ExplorerSearch.possibleMoves(enclosure, location));

        assertTrue(moveSet.contains("0,1"));  // up open
        assertFalse(moveSet.contains("2,1")); // down blocked
        assertTrue(moveSet.contains("1,0"));  // left open
        assertTrue(moveSet.contains("1,2"));  // right open
    }

    @Test
    public void testPossibleMoves_blockedLeftByWall() {
        int[][] enclosure = {
            {1, 1, 1},
            {3, 0, 1},
            {1, 1, 1}
        };
        int[] location = {1, 1};
        Set<String> moveSet = toSet(ExplorerSearch.possibleMoves(enclosure, location));

        assertTrue(moveSet.contains("0,1"));  // up open
        assertTrue(moveSet.contains("2,1"));  // down open
        assertFalse(moveSet.contains("1,0")); // left blocked
        assertTrue(moveSet.contains("1,2"));  // right open
    }

    @Test
    public void testPossibleMoves_blockedRightByWall() {
        int[][] enclosure = {
            {1, 1, 1},
            {1, 0, 2},
            {1, 1, 1}
        };
        int[] location = {1, 1};
        Set<String> moveSet = toSet(ExplorerSearch.possibleMoves(enclosure, location));

        assertTrue(moveSet.contains("0,1"));  // up open
        assertTrue(moveSet.contains("2,1"));  // down open
        assertTrue(moveSet.contains("1,0"));  // left open
        assertFalse(moveSet.contains("1,2")); // right blocked
    }

    @Test
    public void testPossibleMoves_topLeftCornerWithOneOpen() {
        int[][] enclosure = {
            {0, 2},
            {3, 1}
        };
        int[] location = {0, 0};
        List<int[]> moves = ExplorerSearch.possibleMoves(enclosure, location);

        assertTrue(moves.isEmpty()); // surrounded by walls/edges
    }

    @Test
    public void testPossibleMoves_rightEdgeWithOpenLeft() {
        int[][] enclosure = {
            {1, 1, 0}
        };
        int[] location = {0, 2};
        List<int[]> moves = ExplorerSearch.possibleMoves(enclosure, location);
        Set<String> moveSet = toSet(moves);

        assertEquals(1, moves.size());
        assertTrue(moveSet.contains("0,1"));
    }


    private Set<String> toSet(List<int[]> list) {
        Set<String> set = new HashSet<>();
        for (int[] arr : list) {
            set.add(arr[0] + "," + arr[1]);
        }
        return set;
    }
}
