package com.algorithm_termproject.travelmate.utils;

import android.util.Log;

import com.algorithm_termproject.travelmate.data.Place;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

import java.util.ArrayList;

public class Algorithm {
    public ArrayList<Place> placeList;
    public int[][] matrix;

    public Algorithm(ArrayList<Place> placeList){
        this.placeList = placeList;

        int size = placeList.size();
        matrix = new int[size][size];

        fillMatrix();
    }

    private void fillMatrix(){
        int n = placeList.size();

        for(int i=0; i<n; i++){
            for(int j=0; j<n-i; j++){
                LatLng pi = placeList.get(i).getLatLng();
                LatLng pj = placeList.get(i+j).getLatLng();

                int dist = (int) SphericalUtil.computeDistanceBetween(pi, pj);
                Log.d("Algorithm-dist", "Dist between " + placeList.get(i).getName() + " - " + placeList.get(i+j).getName() + " -> " + dist);
                matrix[i][i+j] = dist;
                matrix[i+j][i] = dist;
            }
        }

        showMatrix();
    }

    private void showMatrix(){
        for(int i=0; i< placeList.size(); i++){
            for(int j=0; j<placeList.size(); j++){
                Log.d("Algorithm-mat", "Mat[" + i +", " + j +"] = " + matrix[i][j]);
            }
        }
    }
}
