package com.algorithm_termproject.travelmate.utils;

import static com.algorithm_termproject.travelmate.utils.UtilsKt.aryToStr;
import static com.algorithm_termproject.travelmate.utils.UtilsKt.showMatrix;

import android.util.Log;

import com.algorithm_termproject.travelmate.data.Place;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

import java.util.ArrayList;

/**
 * Algorithm Class to find the best path
 *
 * @author Yooshin Kim
 */
public class Algorithm {
    private final int size;             // Size of placeList
    public ArrayList<Place> placeList;  // Places to visit (ArrayList)
    public int[][] matrix;              // Matrix recording the distance cost of each places

    private int min = Integer.MAX_VALUE;                            // Minimum Sum
    private final ArrayList<Place> pickedList = new ArrayList<Place>(); // Current Path
    private final ArrayList<Place> path = new ArrayList<Place>();       // Minimum Path

    public Algorithm(ArrayList<Place> placeList) {
        this.placeList = placeList;

        // Initialize the matrix
        size = placeList.size();
        matrix = new int[size][size];

        fillMatrix();
    }

    /**
     * A function to find the best path to visit every place
     * @return optimal path: ArrayList(Place)
     */
    public ArrayList<Place> getPath() {
        pickedList.add(placeList.get(0));
        min = dfs(0, 0, 0, min);

        Log.d("Algorithm-result", "min: " + min + " result path: " + aryToStr(path));
        return path;
    }

    //

    /**
     * A function to record the distance of each place in the matrix
     */
    private void fillMatrix() {

        // i = 1 -> n, j = 1 -> (n-i)
        // Compute distance between p[i], p[i+j]
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size - i; j++) {
                LatLng pi = placeList.get(i).getLatLng();
                LatLng pj = placeList.get(i + j).getLatLng();

                int dist = (int) SphericalUtil.computeDistanceBetween(pi, pj);
                Log.d("Algorithm-dist", "Dist between " + placeList.get(i).getName() + " - " + placeList.get(i + j).getName() + " -> " + dist);

                matrix[i][i + j] = dist;
                matrix[i + j][i] = dist;
            }
        }

        Log.d("Algorithm-mat", showMatrix(matrix, size));
    }


    /**
     * A function that recursively visits all locations using DFS, Backtracking
     * @param now   The place current visiting
     * @param count The number of visited place
     * @param cost  The sum of places visited
     * @param min   The minimum cost
     * @return Minimum cost
     */
    private int dfs(int now, int count, int cost, int min) {
        if (count == size - 1 && matrix[now][0] > 0) { // FIN condition
            if (cost < min) {
                min = cost;
                path.clear();
                path.addAll(pickedList);
            }
            return min;
        }

        for (int i = 0; i < size; i++) { // Check all possibles
            Place place = placeList.get(i);

            if (isPromising(now, i, cost)) {
                pickedList.add(place);
                min = dfs(i, count + 1, cost + matrix[now][i], min);
                pickedList.remove(place);
            }
        }
        return min;
    }

    /**
     * A function to check current place is promising
     * (1) Is NOT visited (2) Can go (3) Is NOT over the minimum cost
     * @param now  The place current visiting
     * @param i    The place for next visit
     * @param cost The sum of places visited + The cost of next visit
     * @return Whether it's promising or not
     */
    private boolean isPromising(int now, int i, int cost) {
        Place place = placeList.get(i);

        boolean isVisited = pickedList.contains(place);
        boolean isConnected = matrix[now][i] > 0;
        boolean isOver = min != Integer.MAX_VALUE && cost > min;

        return !isVisited && isConnected && !isOver;
    }
}
