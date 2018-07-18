package neobis.o.utils;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcel;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class FileUtils {
    private static String TAG = "FileUtils___";

    public static Uri getNormalizedUri(Context context, Uri uri) {
        if (uri != null && uri.toString().contains("content:"))
            return Uri.fromFile(FileUtils.getPath(context, uri, MediaStore.Images.Media.DATA));
        else return uri;
    }

    public static Spanned fromHtml(String html) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(html);
        }
    }

    private static File getPath(Context context, Uri uri, String column) {
        String[] columns = {column};
        Cursor cursor = context.getContentResolver().query(uri, columns, null, null, null);
        if (cursor == null) return null;
        int column_index = cursor.getColumnIndexOrThrow(column);
        cursor.moveToFirst();
        String path = cursor.getString(column_index);
        cursor.close();
        return new File(path);
    }

    public static String getImagePathFromInputStreamUri(Context context, Uri uri) {
        InputStream inputStream = null;
        String filePath = null;

        if (uri.getAuthority() != null) {
            try {
                inputStream = context.getContentResolver().openInputStream(uri); // context needed
                File photoFile = createTemporalFileFrom(context, inputStream);

                filePath = photoFile.getPath();

            } catch (FileNotFoundException e) {
                // log
            } catch (IOException e) {
                // log
            } finally {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return filePath;
    }

    public static <T> void writeCacheData(Context context, String filename, T data) {
        File cacheDir = new File(context.getCacheDir(), "alier.auca.neobis");
        if (!cacheDir.exists()) cacheDir.mkdir();
        try {
            File dir = new File(cacheDir, filename);
            if (!dir.exists()) return;
            FileOutputStream fos = new FileOutputStream(dir);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(data);
            oos.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static <T> T readCacheData(Context context, String filename) {
        File cacheDir = new File(context.getCacheDir(), "alier.auca.neobis");
        if (!cacheDir.exists()) return null;
        try {
            File dir = new File(cacheDir, filename);
            if (!dir.exists()) return null;
            FileInputStream fis = new FileInputStream(dir);
            ObjectInputStream ois = new ObjectInputStream(fis);
            fis.close();
            ois.close();
            return (T) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static File createTemporalFileFrom(Context context, InputStream inputStream) throws IOException {
        File targetFile = null;

        if (inputStream != null) {
            int read;
            byte[] buffer = new byte[8 * 1024];

            targetFile = createTemporalFile(context);
            OutputStream outputStream = new FileOutputStream(targetFile);

            while ((read = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, read);
            }
            outputStream.flush();

            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return targetFile;
    }

    private static File createTemporalFile(Context context) {
        return new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), java.util.Calendar.getInstance().getTimeInMillis() + ".jpg"); // context needed
    }

    public static Uri getCaptureImageOutputUri(Context context, String fileName) {
        Uri outputFileUri = null;
        File getImage = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (getImage != null) {
            outputFileUri = Uri.fromFile(new File(getImage.getPath(), fileName + ".jpeg"));
        }
        return outputFileUri;
    }

    public static Uri getPickImageResultUri(Context context, Intent data, String fileName) {
        boolean isCamera = true;
        if (data != null && data.getData() != null) {
            String action = data.getAction();
            isCamera = action != null && action.equals(MediaStore.ACTION_IMAGE_CAPTURE);
        }
        return isCamera || data.getData() == null
                ? getCaptureImageOutputUri(context, fileName) : data.getData();
    }

    public static String readFile(InputStream stream) {
        StringBuilder content = new StringBuilder();
        try {
            BufferedReader reader;
            reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }

        } catch (Exception ignored) {
        }
        return content.toString();

    }

    @SuppressLint("NewApi")
    public static String getImageFile(Context context, Uri uri) {
        try {
            final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
            if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
                if (isExternalStorageDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];
                    if ("primary".equalsIgnoreCase(type)) {
                        return Environment.getExternalStorageDirectory() + "/" + split[1];
                    }
                } else if (isDownloadsDocument(uri)) {
                    final String id = DocumentsContract.getDocumentId(uri);
                    final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                    return getDataColumn(context, contentUri, null, null);
                } else if (isMediaDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    Uri contentUri = null;
                    switch (type) {
                        case "image":
                            contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                            break;
                        case "video":
                            contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                            break;
                        case "audio":
                            contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                            break;
                    }

                    final String selection = "_id=?";
                    final String[] selectionArgs = new String[]{
                            split[1]
                    };

                    return getDataColumn(context, contentUri, selection, selectionArgs);
                }
            } else if ("content".equalsIgnoreCase(uri.getScheme())) {
                return getDataColumn(context, uri, null, null);
            } else if ("file".equalsIgnoreCase(uri.getScheme())) {
                return uri.getPath();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return uri.getPath();
    }

    private static String getDataColumn(Context context, Uri uri, String selection,
                                        String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }


    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static void setBackgroundDrawable(View view, int drawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackground(ContextCompat.getDrawable(view.getContext(), drawable));
        } else {
            view.setBackgroundDrawable(ContextCompat.getDrawable(view.getContext(), drawable));
        }
    }

    public static void setBackgroundDrawable(View view, GradientDrawable drawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackground(drawable);
        } else {
            view.setBackgroundDrawable(drawable);
        }
    }

    public static Map<String, Object> jsonToMap(JSONObject json) throws JSONException {
        Map<String, Object> retMap = new HashMap<String, Object>();

        if (json != JSONObject.NULL) {
            retMap = toMap(json);
        }
        return retMap;
    }

    public static Map<String, Object> toMap(JSONObject object) throws JSONException {
        Map<String, Object> map = new HashMap<String, Object>();

        Iterator<String> keysItr = object.keys();
        while (keysItr.hasNext()) {
            String key = keysItr.next();
            Object value = object.get(key);

            if (value instanceof JSONArray) {
                value = toList((JSONArray) value);
            } else if (value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            map.put(key, value);
        }
        return map;
    }

    public static List<Object> toList(JSONArray array) throws JSONException {
        List<Object> list = new ArrayList<Object>();
        for (int i = 0; i < array.length(); i++) {
            Object value = array.get(i);
            if (value instanceof JSONArray) {
                value = toList((JSONArray) value);
            } else if (value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            list.add(value);
        }
        return list;
    }

    public static Map<String, Object> getMapFromJson(String jsonString) {
        return new Gson().fromJson(jsonString, new TypeToken<HashMap<String, Object>>() {
        }.getType());
    }

    public static boolean saveBundle(Context context, Bundle bundle, int answerId) {
        Log.i("__________________", " " + answerId);
        File cacheDir = new File(context.getFilesDir(), "bilimkana");
        if (!cacheDir.exists()) cacheDir.mkdir();
        File dir = new File(cacheDir, String.valueOf(answerId + ".json"));
        if (!dir.exists()) try {
            dir.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        FileOutputStream fos = null;
        try {
            fos = context.openFileOutput(dir.getName(), Context.MODE_PRIVATE);
            Parcel p = Parcel.obtain(); //creating empty parcel object
            bundle.writeToParcel(p, 0); //saving bundle as parcel
            fos.write(p.marshall()); //writing parcel to file
        } catch (FileNotFoundException e) {
            FileLog.e(e);
            return false;
        } catch (IOException e) {
            FileLog.e(e);
            return false;
        } catch (IllegalArgumentException e) {
            FileLog.e(e);
            return false;
        }
        try {
            fos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static Bundle getSavedWeb(Context context, int answerId) {
        File cacheDir = new File(context.getFilesDir(), "bilimkana");
        if (!cacheDir.exists()) cacheDir.mkdir();
        File dir = new File(cacheDir, String.valueOf(answerId + ".json"));
        if (!dir.exists()) return null;
        Parcel parcel = Parcel.obtain();
        try {
            FileInputStream fis = context.openFileInput(dir.getName());
            byte[] array = new byte[(int) fis.getChannel().size()];
            fis.read(array, 0, array.length);
            fis.close();
            parcel.unmarshall(array, 0, array.length);
            parcel.setDataPosition(0);
            Bundle out = parcel.readBundle();
            out.putAll(out);
            return out;
        } catch (FileNotFoundException fnfe) {
            FileLog.e(fnfe);
        } catch (IOException ioe) {
            FileLog.e(ioe);
        } finally {
            parcel.recycle();
        }
        return null;
    }
}
