package com.algorithm_termproject.travelmate.utils;

import android.util.Log;

import com.algorithm_termproject.travelmate.data.Place;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

import java.util.ArrayList;

public class Algorithm {
    public ArrayList<Place> placeList;
    public int[][] matrix;

    private final int size;
    private int min = Integer.MAX_VALUE;
    private final ArrayList<Place> pickedList = new ArrayList<Place>();
    private final ArrayList<Place> path = new ArrayList<Place>();

    public Algorithm(ArrayList<Place> placeList) {
        this.placeList = placeList;

        size = placeList.size();
        matrix = new int[size][size];

        fillMatrix();
    }

    public ArrayList<Place> getPath(){
        pickedList.add(placeList.get(0));
        min = dfs(0, 0, 0, min);

        Log.d("Algorithm-result", "min: " + min + " result path: " + aryListToStr(path));

        return path;
    }

    private void fillMatrix() {
        int n = placeList.size();

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n - i; j++) {
                LatLng pi = placeList.get(i).getLatLng();
                LatLng pj = placeList.get(i + j).getLatLng();

                int dist = (int) SphericalUtil.computeDistanceBetween(pi, pj);
                Log.d("Algorithm-dist", "Dist between " + placeList.get(i).getName() + " - " + placeList.get(i + j).getName() + " -> " + dist);
                matrix[i][i + j] = dist;
                matrix[i + j][i] = dist;
            }
        }

        showMatrix();
    }

    private void showMatrix() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Log.d("Algorithm-mat", "Mat[" + i + ", " + j + "] = " + matrix[i][j]);
            }
        }
    }

    /* TSP - backtracking */
    private int dfs(int now, int count, int cost, int min) {
        if (count == size - 1 && matrix[now][0] > 0) { // 종료 조건
            min = Math.min(min, cost);

            path.clear();
            path.addAll(pickedList);

            Log.d("Algorithm-DFS", "min: " + cost + " result: " + aryListToStr(path));
            return min;
        }

        for (int i = 0; i < size; i++) {
            Place place = placeList.get(i);

            if (isPromising(now, i, cost)) {
                pickedList.add(place);
                min = dfs(i, count + 1, cost + matrix[now][i], min);
                pickedList.remove(place);
            }
        }

        return min;
    }

    private boolean isPromising(int now, int i, int cost){
        Place place = placeList.get(i);

        boolean isExist = pickedList.contains(place);
        boolean isConnected = matrix[now][i] > 0;
        boolean isOver = min != Integer.MAX_VALUE && cost > min;

        return !isExist && isConnected && !isOver;
    }

    private String aryListToStr(ArrayList<Place> list) {
        StringBuilder str = new StringBuilder();
        for (Place place : list) {
            str.append(place.getName()).append(", ");
        }

        return str.toString();
    }
}
