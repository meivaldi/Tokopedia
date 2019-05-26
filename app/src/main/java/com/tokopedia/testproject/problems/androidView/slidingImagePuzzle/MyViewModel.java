package com.tokopedia.testproject.problems.androidView.slidingImagePuzzle;

import android.arch.lifecycle.ViewModel;
import android.graphics.Bitmap;

import java.util.ArrayList;

public class MyViewModel extends ViewModel {

    public ArrayList<Integer> index = new ArrayList<>();
    public ArrayList<Bitmap> bitmap = new ArrayList<>();

}
