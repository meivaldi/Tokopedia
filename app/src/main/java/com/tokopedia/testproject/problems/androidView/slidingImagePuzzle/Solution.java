package com.tokopedia.testproject.problems.androidView.slidingImagePuzzle;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.StrictMode;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Solution {
    public interface onSuccessLoadBitmap{
        void onSliceSuccess(List<Bitmap> bitmapList, int[] indices);
        void onSliceFailed(Throwable throwable);
    }

    public static void sliceTo4x4(Context context, onSuccessLoadBitmap onSuccessLoadBitmap, final String imageUrl) {
        ArrayList<Bitmap> bitmapList = new ArrayList<>();
        // TODO, download the image, crop, then sliced to 15 Bitmap (4x4 Bitmap). ignore the last Bitmap
        // below is stub, replace with your implementation!

        Bitmap bitmap = getBitmapFromURL(imageUrl);

        Bitmap[] bmp = new Bitmap[16];

        int counter = 0;
        for (int x=0; x<4; x++) {
            for (int y=0; y<4; y++) {
                bmp[counter++] = Bitmap.createBitmap((bitmap.getWidth()/4), (bitmap.getHeight()/4), Bitmap.Config.RGB_565);
            }
        }

        for (int row=0; row<4; row++) {
            for (int x = 0 + (row * 100); x < 100 + (row * 100); x++) {
                for (int col = 0; col < 4; col++) {
                    for (int y = 0 + (col * 100); y < 100 + (col * 100); y++) {
                        bmp[row + (4*col)].setPixel((x%100), (y%100), bitmap.getPixel(x, y));
                    }
                }
            }
        }

        int[] indexShuffle = shuffle(bmp.length-1);

        for (int i=0; i<bmp.length-1; i++) {
            bitmapList.add(bmp[indexShuffle[i]]);
        }

        onSuccessLoadBitmap.onSliceSuccess(bitmapList, indexShuffle);
    }

    public static Bitmap getBitmapFromURL(String src) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            return null;
        }
    }

    private static int[] shuffle(int size) {
        int array[] = new int[size];
        ArrayList<Integer> list = new ArrayList<>();

        for(int i=0; i<size; i++) {
            list.add(new Integer(i));
        }

        Collections.shuffle(list);

        for(int i=0; i<size; i++) {
            array[i] = list.get(i);
        }

        return array;
    }

}
