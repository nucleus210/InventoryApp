package com.example.nucle.inventoryapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class Utils {
    private static final String TAG = "Utils";
    private static final int REQUIRED_IMAGE_WIDTH = 333;
    private static final int REQUIRED_IMAGE_HEIGHT = 222;

    /**
     * File name generator. Method is used when Image is upload from user to Server.
     */
    public static String fileNameGenerator() {
        @SuppressLint("SimpleDateFormat") String timeStamp =
                new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return "img" + timeStamp + ".jpg";
    }

    /**
     * File name generator. Method is used when Image is upload from user to Server.
     * @param number generated from loop
     */
    static String initNameGenerator(int number) {
        @SuppressLint("SimpleDateFormat") String timeStamp =
                new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return "img" + timeStamp + number + ".jpg";
    }

    /**
     * Save image to internal Storage Directory from bitmap.
     *
     * @param context  get default context
     * @param bmp      Bitmap object
     * @param filename Filename for new file
     */
    public static Uri saveBitMap(Context context, Bitmap bmp, String filename) throws IOException {

        FileOutputStream fo;
        File file = new File(context.getFilesDir(), filename);

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();

        try {
            fo = context.openFileOutput(filename, Context.MODE_PRIVATE);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            file.createNewFile();
            fo.write(bytes.toByteArray());
        } catch (NullPointerException w) {
            Log.d(TAG, "NuLL", w);
        }
        // Parse the gallery image url to uri
        Uri savedImageURI = Uri.parse(file.getAbsolutePath());

        try {
            MediaScannerConnection.scanFile(context,
                    new String[]{file.toString()}, null,
                    (path, uri) -> {
                        Log.i("ExternalStorage", "Scanned " + path + ":");
                        Log.i("ExternalStorage", "-> uri=" + uri);
                    });
        } catch (NullPointerException w) {
            Log.d("Media Scanner: NuLL", String.valueOf(w));
        }
        return savedImageURI;
    }

    /**
     * Convert and resize our image to 400dp for faster uploading our images to DB
     *
     * @param selectedImage Uri of selected image
     */
    public static Bitmap decodeUri(Context context, Uri selectedImage) {

        try {
            // Decode image size
            BitmapFactory.Options option = new BitmapFactory.Options();
            option.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(Objects
                    .requireNonNull(context)
                    .getContentResolver().openInputStream(selectedImage), null, option);
            // Scale down with required size
            int width_tmp = option.outWidth, height_tmp = option.outHeight;
            int scale = 1;
            while (width_tmp
                    / 2 >= REQUIRED_IMAGE_WIDTH
                    && height_tmp
                    / 2 >= REQUIRED_IMAGE_HEIGHT) {
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
            }
            // Decode with inSampleSize
            BitmapFactory.Options optionSec = new BitmapFactory.Options();
            optionSec.inSampleSize = scale;
            return BitmapFactory.decodeStream(context.getContentResolver()
                    .openInputStream(selectedImage), null, optionSec);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Calculate Image size
     *
     * @param options   Bitmap options
     * @param reqWidth  Image Width
     * @param reqHeight Image Height
     * @return inSampleSize int Size
     */
    private static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    /**
     * Save image to internal Storage Directory from bitmap.
     *
     * @param res       resource object
     * @param resId     Bitmap resource object
     * @param reqWidth  required width size
     * @param reqHeight required Height size
     */
    @SuppressWarnings("SameParameterValue")
    static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                  int reqWidth,  int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    /**
     * Generate random Double number. Used for first time run to add data to database
     *
     * @param min min value
     * @param max max value
     */
    static double generateRandomDouble(double min, double max) {
        return ThreadLocalRandom.current().nextDouble(min, max);
    }

    /**
     * Generate random Int number. Used for first time run to add data to database
     *
     */
    static int generateRandomInt() {
        return ThreadLocalRandom.current().nextInt(100);
    }
}
