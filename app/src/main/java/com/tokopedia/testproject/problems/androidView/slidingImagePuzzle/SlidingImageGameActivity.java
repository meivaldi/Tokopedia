package com.tokopedia.testproject.problems.androidView.slidingImagePuzzle;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.tokopedia.testproject.R;

import java.util.ArrayList;
import java.util.List;

public class SlidingImageGameActivity extends AppCompatActivity {
    public static final String X_IMAGE_URL = "x_image_url";
    public static final int GRID_NO = 4;
    private String imageUrl;
    ImageView[][] imageViews = new ImageView[4][4];
    private GridLayout gridLayout;

    int[] rowNbr = {-1, 0, 0, 1};
    int[] colNbr = { 0, -1, 1, 0};

    private ArrayList<Integer> index = new ArrayList<>();
    private ArrayList<Bitmap> bitmap = new ArrayList<>();

    public static Intent getIntent(Context context, String imageUrl) {
        Intent intent = new Intent(context, SlidingImageGameActivity.class);
        intent.putExtra(X_IMAGE_URL, imageUrl);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        imageUrl = getIntent().getStringExtra(X_IMAGE_URL);
        setContentView(R.layout.activity_sliding_image_game);
        gridLayout = findViewById(R.id.gridLayout);

        LayoutInflater inflater = LayoutInflater.from(this);
        for (int i = 0; i < GRID_NO; i++) {
            for (int j = 0; j < GRID_NO; j++) {
                ImageView view = (ImageView) inflater.inflate(R.layout.item_image_sliding_image,
                        gridLayout, false);
                gridLayout.addView(view);
                imageViews[i][j] = view;
            }
        }

        if (savedInstanceState == null) {
            Solution.sliceTo4x4(this, new Solution.onSuccessLoadBitmap() {
                @Override
                public void onSliceSuccess(List<Bitmap> bitmapList, int[] indices) {
                    //TODO will randomize placement to grid. Note: the game must be solvable.
                    //replace below implementation to your implementation.
                    int counter = 0;
                    int bitmapSize = bitmapList.size();
                    for (int i = 0; i < GRID_NO; i++) {
                        for (int j = 0; j < GRID_NO; j++) {
                            if (counter >= bitmapSize) break;
                            imageViews[i][j].setImageBitmap(bitmapList.get(counter));
                            counter++;
                        }
                        if (counter >= bitmapSize) break;
                    }

                    for (int i = 0; i < indices.length; i++) {
                        index.add(new Integer(indices[i]));
                        bitmap.add(bitmapList.get(i));
                    }

                    index.add(indices.length);
                    bitmap.add(null);
                }

                @Override
                public void onSliceFailed(Throwable throwable) {
                    Toast.makeText(SlidingImageGameActivity.this,
                            throwable.getMessage(), Toast.LENGTH_LONG).show();
                }
            }, imageUrl);
        }

        // TODO add implementation of the game.
        // There is image adjacent to blank space (either horizontal or vertical).
        // If that image is clicked, it will swap to the blank space
        // if the puzzle is solved (the image in the view is aligned with the original image), then show a "success" dialog
        swapImages();

        // TODO add handling for rotation to save the user input.
        // If the device is rotated, it should retain user's input, so user can continue the game.
        if (savedInstanceState != null) {
            bitmap = savedInstanceState.getParcelableArrayList("bitmap");
            index = savedInstanceState.getIntegerArrayList("index");

            int counter = 0;
            for (int i = 0; i < GRID_NO; i++) {
                for (int j = 0; j < GRID_NO; j++) {
                    imageViews[i][j].setImageBitmap(bitmap.get(counter));
                    counter++;
                }
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putIntegerArrayList("index", index);
        outState.putParcelableArrayList("bitmap", bitmap);
    }

    private void swapImages() {
        imageViews[0][0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int row=0; int rowNeighbour;
                int col=0; int colNeighbour;
                int temp;
                Bitmap bmpTemp;

                for (int k=0; k<GRID_NO; k++) {
                    rowNeighbour = row + rowNbr[k];
                    colNeighbour = col + colNbr[k];

                    if (isSafe(rowNeighbour, colNeighbour)) {
                        Bitmap bmp = null;

                        try {
                            bmp = ((BitmapDrawable) imageViews[rowNeighbour][colNeighbour].getDrawable()).getBitmap();
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }

                        if (bmp == null) {
                            Bitmap image = ((BitmapDrawable) imageViews[0][0].getDrawable()).getBitmap();
                            imageViews[rowNeighbour][colNeighbour].setImageBitmap(image);
                            imageViews[0][0].setImageBitmap(null);

                            int idx = row > 0 ? col+(4*row) : col;
                            int idx2 = rowNeighbour > 0 ? colNeighbour+(4*rowNeighbour) : colNeighbour;

                            temp = index.get(idx);
                            index.set(idx, index.get(idx2));
                            index.set(idx2, temp);

                            bmpTemp = bitmap.get(idx);
                            bitmap.set(idx, bitmap.get(idx2));
                            bitmap.set(idx2, bmpTemp);
                        }
                    }
                }

                check();
            }
        });

        imageViews[0][1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int row=0; int rowNeighbour;
                int col=1; int colNeighbour;

                int temp;
                Bitmap bmpTemp;
                for (int k=0; k<GRID_NO; k++) {
                    rowNeighbour = row + rowNbr[k];
                    colNeighbour = col + colNbr[k];

                    if (isSafe(rowNeighbour, colNeighbour)) {
                        Bitmap bmp = null;

                        try {
                            bmp = ((BitmapDrawable) imageViews[rowNeighbour][colNeighbour].getDrawable()).getBitmap();
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }

                        if (bmp == null) {
                            Bitmap image = ((BitmapDrawable) imageViews[0][1].getDrawable()).getBitmap();
                            imageViews[rowNeighbour][colNeighbour].setImageBitmap(image);
                            imageViews[0][1].setImageBitmap(null);

                            int idx = row > 0 ? col+(4*row) : col;
                            int idx2 = rowNeighbour > 0 ? colNeighbour+(4*rowNeighbour) : colNeighbour;

                            temp = index.get(idx);
                            index.set(idx, index.get(idx2));
                            index.set(idx2, temp);

                            bmpTemp = bitmap.get(idx);
                            bitmap.set(idx, bitmap.get(idx2));
                            bitmap.set(idx2, bmpTemp);
                        }
                    }
                }

                check();
            }
        });

        imageViews[0][2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int row=0; int rowNeighbour;
                int col=2; int colNeighbour;

                int temp;
                Bitmap bmpTemp;
                for (int k=0; k<GRID_NO; k++) {
                    rowNeighbour = row + rowNbr[k];
                    colNeighbour = col + colNbr[k];

                    if (isSafe(rowNeighbour, colNeighbour)) {
                        Bitmap bmp = null;

                        try {
                            bmp = ((BitmapDrawable) imageViews[rowNeighbour][colNeighbour].getDrawable()).getBitmap();
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }

                        if (bmp == null) {
                            Bitmap image = ((BitmapDrawable) imageViews[0][2].getDrawable()).getBitmap();
                            imageViews[rowNeighbour][colNeighbour].setImageBitmap(image);
                            imageViews[0][2].setImageBitmap(null);

                            int idx = row > 0 ? col+(4*row) : col;
                            int idx2 = rowNeighbour > 0 ? colNeighbour+(4*rowNeighbour) : colNeighbour;

                            temp = index.get(idx);
                            index.set(idx, index.get(idx2));
                            index.set(idx2, temp);

                            bmpTemp = bitmap.get(idx);
                            bitmap.set(idx, bitmap.get(idx2));
                            bitmap.set(idx2, bmpTemp);
                        }
                    }
                }

                check();
            }
        });

        imageViews[0][3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int row=0; int rowNeighbour;
                int col=3; int colNeighbour;

                int temp;
                Bitmap bmpTemp;
                for (int k=0; k<GRID_NO; k++) {
                    rowNeighbour = row + rowNbr[k];
                    colNeighbour = col + colNbr[k];

                    if (isSafe(rowNeighbour, colNeighbour)) {
                        Bitmap bmp = null;

                        try {
                            bmp = ((BitmapDrawable) imageViews[rowNeighbour][colNeighbour].getDrawable()).getBitmap();
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }

                        if (bmp == null) {
                            Bitmap image = ((BitmapDrawable) imageViews[0][3].getDrawable()).getBitmap();
                            imageViews[rowNeighbour][colNeighbour].setImageBitmap(image);
                            imageViews[0][3].setImageBitmap(null);

                            int idx = row > 0 ? col+(4*row) : col;
                            int idx2 = rowNeighbour > 0 ? colNeighbour+(4*rowNeighbour) : colNeighbour;

                            temp = index.get(idx);
                            index.set(idx, index.get(idx2));
                            index.set(idx2, temp);

                            bmpTemp = bitmap.get(idx);
                            bitmap.set(idx, bitmap.get(idx2));
                            bitmap.set(idx2, bmpTemp);
                        }
                    }
                }

                check();
            }
        });

        imageViews[1][0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int row=1; int rowNeighbour;
                int col=0; int colNeighbour;

                int temp;
                Bitmap bmpTemp;
                for (int k=0; k<GRID_NO; k++) {
                    rowNeighbour = row + rowNbr[k];
                    colNeighbour = col + colNbr[k];

                    if (isSafe(rowNeighbour, colNeighbour)) {
                        Bitmap bmp = null;

                        try {
                            bmp = ((BitmapDrawable) imageViews[rowNeighbour][colNeighbour].getDrawable()).getBitmap();
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }

                        if (bmp == null) {
                            Bitmap image = ((BitmapDrawable) imageViews[1][0].getDrawable()).getBitmap();
                            imageViews[rowNeighbour][colNeighbour].setImageBitmap(image);
                            imageViews[1][0].setImageBitmap(null);

                            int idx = row > 0 ? col+(4*row) : col;
                            int idx2 = rowNeighbour > 0 ? colNeighbour+(4*rowNeighbour) : colNeighbour;

                            temp = index.get(idx);
                            index.set(idx, index.get(idx2));
                            index.set(idx2, temp);

                            bmpTemp = bitmap.get(idx);
                            bitmap.set(idx, bitmap.get(idx2));
                            bitmap.set(idx2, bmpTemp);
                        }
                    }
                }

                check();
            }
        });

        imageViews[1][1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int row=1; int rowNeighbour;
                int col=1; int colNeighbour;

                int temp;
                Bitmap bmpTemp;
                for (int k=0; k<GRID_NO; k++) {
                    rowNeighbour = row + rowNbr[k];
                    colNeighbour = col + colNbr[k];

                    if (isSafe(rowNeighbour, colNeighbour)) {
                        Bitmap bmp = null;

                        try {
                            bmp = ((BitmapDrawable) imageViews[rowNeighbour][colNeighbour].getDrawable()).getBitmap();
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }

                        if (bmp == null) {
                            Bitmap image = ((BitmapDrawable) imageViews[1][1].getDrawable()).getBitmap();
                            imageViews[rowNeighbour][colNeighbour].setImageBitmap(image);
                            imageViews[1][1].setImageBitmap(null);

                            int idx = row > 0 ? col+(4*row) : col;
                            int idx2 = rowNeighbour > 0 ? colNeighbour+(4*rowNeighbour) : colNeighbour;

                            temp = index.get(idx);
                            index.set(idx, index.get(idx2));
                            index.set(idx2, temp);

                            bmpTemp = bitmap.get(idx);
                            bitmap.set(idx, bitmap.get(idx2));
                            bitmap.set(idx2, bmpTemp);
                        }
                    }
                }

                check();
            }
        });

        imageViews[1][2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int row=1; int rowNeighbour;
                int col=2; int colNeighbour;

                int temp;
                Bitmap bmpTemp;
                for (int k=0; k<GRID_NO; k++) {
                    rowNeighbour = row + rowNbr[k];
                    colNeighbour = col + colNbr[k];

                    if (isSafe(rowNeighbour, colNeighbour)) {
                        Bitmap bmp = null;

                        try {
                            bmp = ((BitmapDrawable) imageViews[rowNeighbour][colNeighbour].getDrawable()).getBitmap();
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }

                        if (bmp == null) {
                            Bitmap image = ((BitmapDrawable) imageViews[1][2].getDrawable()).getBitmap();
                            imageViews[rowNeighbour][colNeighbour].setImageBitmap(image);
                            imageViews[1][2].setImageBitmap(null);

                            int idx = row > 0 ? col+(4*row) : col;
                            int idx2 = rowNeighbour > 0 ? colNeighbour+(4*rowNeighbour) : colNeighbour;

                            temp = index.get(idx);
                            index.set(idx, index.get(idx2));
                            index.set(idx2, temp);

                            bmpTemp = bitmap.get(idx);
                            bitmap.set(idx, bitmap.get(idx2));
                            bitmap.set(idx2, bmpTemp);
                        }
                    }
                }

                check();
            }
        });

        imageViews[1][3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int row=1; int rowNeighbour;
                int col=3; int colNeighbour;

                int temp;
                Bitmap bmpTemp;
                for (int k=0; k<GRID_NO; k++) {
                    rowNeighbour = row + rowNbr[k];
                    colNeighbour = col + colNbr[k];

                    if (isSafe(rowNeighbour, colNeighbour)) {
                        Bitmap bmp = null;

                        try {
                            bmp = ((BitmapDrawable) imageViews[rowNeighbour][colNeighbour].getDrawable()).getBitmap();
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }

                        if (bmp == null) {
                            Bitmap image = ((BitmapDrawable) imageViews[1][3].getDrawable()).getBitmap();
                            imageViews[rowNeighbour][colNeighbour].setImageBitmap(image);
                            imageViews[1][3].setImageBitmap(null);

                            int idx = row > 0 ? col+(4*row) : col;
                            int idx2 = rowNeighbour > 0 ? colNeighbour+(4*rowNeighbour) : colNeighbour;

                            temp = index.get(idx);
                            index.set(idx, index.get(idx2));
                            index.set(idx2, temp);

                            bmpTemp = bitmap.get(idx);
                            bitmap.set(idx, bitmap.get(idx2));
                            bitmap.set(idx2, bmpTemp);
                        }
                    }
                }

                check();
            }
        });

        imageViews[2][0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int row=2; int rowNeighbour;
                int col=0; int colNeighbour;

                int temp;
                Bitmap bmpTemp;
                for (int k=0; k<GRID_NO; k++) {
                    rowNeighbour = row + rowNbr[k];
                    colNeighbour = col + colNbr[k];

                    if (isSafe(rowNeighbour, colNeighbour)) {
                        Bitmap bmp = null;

                        try {
                            bmp = ((BitmapDrawable) imageViews[rowNeighbour][colNeighbour].getDrawable()).getBitmap();
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }

                        if (bmp == null) {
                            Bitmap image = ((BitmapDrawable) imageViews[2][0].getDrawable()).getBitmap();
                            imageViews[rowNeighbour][colNeighbour].setImageBitmap(image);
                            imageViews[2][0].setImageBitmap(null);

                            int idx = row > 0 ? col+(4*row) : col;
                            int idx2 = rowNeighbour > 0 ? colNeighbour+(4*rowNeighbour) : colNeighbour;

                            temp = index.get(idx);
                            index.set(idx, index.get(idx2));
                            index.set(idx2, temp);

                            bmpTemp = bitmap.get(idx);
                            bitmap.set(idx, bitmap.get(idx2));
                            bitmap.set(idx2, bmpTemp);
                        }
                    }
                }

                check();
            }
        });

        imageViews[2][1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int row=2; int rowNeighbour;
                int col=1; int colNeighbour;

                int temp;
                Bitmap bmpTemp;
                for (int k=0; k<GRID_NO; k++) {
                    rowNeighbour = row + rowNbr[k];
                    colNeighbour = col + colNbr[k];

                    if (isSafe(rowNeighbour, colNeighbour)) {
                        Bitmap bmp = null;

                        try {
                            bmp = ((BitmapDrawable) imageViews[rowNeighbour][colNeighbour].getDrawable()).getBitmap();
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }

                        if (bmp == null) {
                            Bitmap image = ((BitmapDrawable) imageViews[2][1].getDrawable()).getBitmap();
                            imageViews[rowNeighbour][colNeighbour].setImageBitmap(image);
                            imageViews[2][1].setImageBitmap(null);

                            int idx = row > 0 ? col+(4*row) : col;
                            int idx2 = rowNeighbour > 0 ? colNeighbour+(4*rowNeighbour) : colNeighbour;

                            temp = index.get(idx);
                            index.set(idx, index.get(idx2));
                            index.set(idx2, temp);

                            bmpTemp = bitmap.get(idx);
                            bitmap.set(idx, bitmap.get(idx2));
                            bitmap.set(idx2, bmpTemp);
                        }
                    }
                }

                check();
            }
        });

        imageViews[2][2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int row=2; int rowNeighbour;
                int col=2; int colNeighbour;

                int temp;
                Bitmap bmpTemp;
                for (int k=0; k<GRID_NO; k++) {
                    rowNeighbour = row + rowNbr[k];
                    colNeighbour = col + colNbr[k];

                    if (isSafe(rowNeighbour, colNeighbour)) {
                        Bitmap bmp = null;

                        try {
                            bmp = ((BitmapDrawable) imageViews[rowNeighbour][colNeighbour].getDrawable()).getBitmap();
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }

                        if (bmp == null) {
                            Bitmap image = ((BitmapDrawable) imageViews[2][2].getDrawable()).getBitmap();
                            imageViews[rowNeighbour][colNeighbour].setImageBitmap(image);
                            imageViews[2][2].setImageBitmap(null);

                            int idx = row > 0 ? col+(4*row) : col;
                            int idx2 = rowNeighbour > 0 ? colNeighbour+(4*rowNeighbour) : colNeighbour;

                            temp = index.get(idx);
                            index.set(idx, index.get(idx2));
                            index.set(idx2, temp);

                            bmpTemp = bitmap.get(idx);
                            bitmap.set(idx, bitmap.get(idx2));
                            bitmap.set(idx2, bmpTemp);
                        }
                    }
                }

                check();
            }
        });

        imageViews[2][3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int row=2; int rowNeighbour;
                int col=3; int colNeighbour;

                int temp;
                Bitmap bmpTemp;
                for (int k=0; k<GRID_NO; k++) {
                    rowNeighbour = row + rowNbr[k];
                    colNeighbour = col + colNbr[k];

                    if (isSafe(rowNeighbour, colNeighbour)) {
                        Bitmap bmp = null;

                        try {
                            bmp = ((BitmapDrawable) imageViews[rowNeighbour][colNeighbour].getDrawable()).getBitmap();
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }

                        if (bmp == null) {
                            Bitmap image = ((BitmapDrawable) imageViews[2][3].getDrawable()).getBitmap();
                            imageViews[rowNeighbour][colNeighbour].setImageBitmap(image);
                            imageViews[2][3].setImageBitmap(null);

                            int idx = row > 0 ? col+(4*row) : col;
                            int idx2 = rowNeighbour > 0 ? colNeighbour+(4*rowNeighbour) : colNeighbour;

                            temp = index.get(idx);
                            index.set(idx, index.get(idx2));
                            index.set(idx2, temp);

                            bmpTemp = bitmap.get(idx);
                            bitmap.set(idx, bitmap.get(idx2));
                            bitmap.set(idx2, bmpTemp);
                        }
                    }
                }

                check();
            }
        });

        imageViews[3][0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int row=3; int rowNeighbour;
                int col=0; int colNeighbour;

                int temp;
                Bitmap bmpTemp;
                for (int k=0; k<GRID_NO; k++) {
                    rowNeighbour = row + rowNbr[k];
                    colNeighbour = col + colNbr[k];

                    if (isSafe(rowNeighbour, colNeighbour)) {
                        Bitmap bmp = null;

                        try {
                            bmp = ((BitmapDrawable) imageViews[rowNeighbour][colNeighbour].getDrawable()).getBitmap();
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }

                        if (bmp == null) {
                            Bitmap image = ((BitmapDrawable) imageViews[3][0].getDrawable()).getBitmap();
                            imageViews[rowNeighbour][colNeighbour].setImageBitmap(image);
                            imageViews[3][0].setImageBitmap(null);

                            int idx = row > 0 ? col+(4*row) : col;
                            int idx2 = rowNeighbour > 0 ? colNeighbour+(4*rowNeighbour) : colNeighbour;

                            temp = index.get(idx);
                            index.set(idx, index.get(idx2));
                            index.set(idx2, temp);

                            bmpTemp = bitmap.get(idx);
                            bitmap.set(idx, bitmap.get(idx2));
                            bitmap.set(idx2, bmpTemp);
                        }
                    }
                }

                check();
            }
        });

        imageViews[3][1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int row=3; int rowNeighbour;
                int col=1; int colNeighbour;

                int temp;
                Bitmap bmpTemp;
                for (int k=0; k<GRID_NO; k++) {
                    rowNeighbour = row + rowNbr[k];
                    colNeighbour = col + colNbr[k];

                    if (isSafe(rowNeighbour, colNeighbour)) {
                        Bitmap bmp = null;

                        try {
                            bmp = ((BitmapDrawable) imageViews[rowNeighbour][colNeighbour].getDrawable()).getBitmap();
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }

                        if (bmp == null) {
                            Bitmap image = ((BitmapDrawable) imageViews[3][1].getDrawable()).getBitmap();
                            imageViews[rowNeighbour][colNeighbour].setImageBitmap(image);
                            imageViews[3][1].setImageBitmap(null);

                            int idx = row > 0 ? col+(4*row) : col;
                            int idx2 = rowNeighbour > 0 ? colNeighbour+(4*rowNeighbour) : colNeighbour;

                            temp = index.get(idx);
                            index.set(idx, index.get(idx2));
                            index.set(idx2, temp);

                            bmpTemp = bitmap.get(idx);
                            bitmap.set(idx, bitmap.get(idx2));
                            bitmap.set(idx2, bmpTemp);
                        }
                    }
                }

                check();
            }
        });

        imageViews[3][2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int row=3; int rowNeighbour;
                int col=2; int colNeighbour;

                int temp;
                Bitmap bmpTemp;
                for (int k=0; k<GRID_NO; k++) {
                    rowNeighbour = row + rowNbr[k];
                    colNeighbour = col + colNbr[k];

                    if (isSafe(rowNeighbour, colNeighbour)) {
                        Bitmap bmp = null;

                        try {
                            bmp = ((BitmapDrawable) imageViews[rowNeighbour][colNeighbour].getDrawable()).getBitmap();
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }

                        if (bmp == null) {
                            Bitmap image = ((BitmapDrawable) imageViews[3][2].getDrawable()).getBitmap();
                            imageViews[rowNeighbour][colNeighbour].setImageBitmap(image);
                            imageViews[3][2].setImageBitmap(null);

                            int idx = row > 0 ? col+(4*row) : col;
                            int idx2 = rowNeighbour > 0 ? colNeighbour+(4*rowNeighbour) : colNeighbour;

                            temp = index.get(idx);
                            index.set(idx, index.get(idx2));
                            index.set(idx2, temp);

                            bmpTemp = bitmap.get(idx);
                            bitmap.set(idx, bitmap.get(idx2));
                            bitmap.set(idx2, bmpTemp);
                        }
                    }
                }

                check();
            }
        });

        imageViews[3][3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int row=3; int rowNeighbour;
                int col=3; int colNeighbour;

                int temp;
                Bitmap bmpTemp;
                for (int k=0; k<GRID_NO; k++) {
                    rowNeighbour = row + rowNbr[k];
                    colNeighbour = col + colNbr[k];

                    if (isSafe(rowNeighbour, colNeighbour)) {
                        Bitmap bmp = null;

                        try {
                            bmp = ((BitmapDrawable) imageViews[rowNeighbour][colNeighbour].getDrawable()).getBitmap();
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }

                        if (bmp == null) {
                            Bitmap image = ((BitmapDrawable) imageViews[3][3].getDrawable()).getBitmap();
                            imageViews[rowNeighbour][colNeighbour].setImageBitmap(image);
                            imageViews[3][3].setImageBitmap(null);

                            int idx = row > 0 ? col+(4*row) : col;
                            int idx2 = rowNeighbour > 0 ? colNeighbour+(4*rowNeighbour) : colNeighbour;

                            temp = index.get(idx);
                            index.set(idx, index.get(idx2));
                            index.set(idx2, temp);

                            bmpTemp = bitmap.get(idx);
                            bitmap.set(idx, bitmap.get(idx2));
                            bitmap.set(idx2, bmpTemp);
                        }
                    }
                }

                check();
            }
        });
    }

    private boolean isSafe(int row, int col) {
        return (row >=0 && row < GRID_NO && col >= 0 && col < GRID_NO);
    }

    private void check() {
        boolean win = true;

        String tes = "";
        for (int i=0; i<index.size(); i++) {
            tes += index.get(i) + " ";
        }
        Log.d("TES", tes);

        for (int i=0; i<index.size(); i++) {
            tes += index.get(i) + " ";

            if (!index.get(i).equals(i)) {
                win = false;
                break;
            }
        }

        if (win) {
            Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.congratulation);
            dialog.setCancelable(false);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

            dialog.show();
        }
    }

}