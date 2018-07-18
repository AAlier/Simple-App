/*
 * This is the source code of Telegram for Android v. 3.x.x.
 * It is licensed under GNU GPL v. 2 or later.
 * You should have received a copy of the license in this archive (see LICENSE).
 *
 * Copyright Nikolai Kudashov, 2013-2017.
 */

package neobis.o.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import neobis.o.BuildConfig;
import neobis.o.StartApplication;

public class FileLog {
    private static volatile FileLog Instance = null;
    private OutputStreamWriter streamWriter = null;
    private File currentFile = null;
    private File networkFile = null;

    public FileLog() {
        if (!BuildConfig.DEBUG) {
            return;
        }
        try {
            File sdCard = StartApplication.INSTANCE.getExternalFilesDir(null);
            if (sdCard == null) {
                return;
            }
            File dir = new File(sdCard.getAbsolutePath() + "/logs");
            dir.mkdirs();
            currentFile = new File(dir, System.currentTimeMillis() + ".txt");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            currentFile.createNewFile();
            FileOutputStream stream = new FileOutputStream(currentFile);
            streamWriter = new OutputStreamWriter(stream);
            streamWriter.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static FileLog getInstance() {
        FileLog localInstance = Instance;
        if (localInstance == null) {
            synchronized (FileLog.class) {
                localInstance = Instance;
                if (localInstance == null) {
                    Instance = localInstance = new FileLog();
                }
            }
        }
        return localInstance;
    }

    public static String getNetworkLogPath() {
        if (!BuildConfig.DEBUG) {
            return "";
        }
        try {
            File sdCard = StartApplication.INSTANCE.getExternalFilesDir(null);
            if (sdCard == null) {
                return "";
            }
            File dir = new File(sdCard.getAbsolutePath() + "/logs");
            dir.mkdirs();
            getInstance().networkFile = new File(dir, System.currentTimeMillis() + "_net.txt");
            return getInstance().networkFile.getAbsolutePath();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void e(final String message, final Throwable exception) {
        if (!BuildConfig.DEBUG) {
            return;
        }
        Log.e("tmessages", message, exception);
    }

    public static void e(final String message) {
        if (!BuildConfig.DEBUG) {
            return;
        }
        Log.e("tmessages", message);
    }

    public static void e(final Throwable e) {
        if (BuildConfig.DEBUG) {
            e.printStackTrace();
        }
    }

    public static void cleanupLogs() {
        File sdCard = StartApplication.INSTANCE.getExternalFilesDir(null);
        if (sdCard == null) {
            return;
        }
        File dir = new File(sdCard.getAbsolutePath() + "/logs");
        File[] files = dir.listFiles();
        if (files != null) {
            for (int a = 0; a < files.length; a++) {
                File file = files[a];
                if (getInstance().currentFile != null && file.getAbsolutePath().equals(getInstance().currentFile.getAbsolutePath())) {
                    continue;
                }
                if (getInstance().networkFile != null && file.getAbsolutePath().equals(getInstance().networkFile.getAbsolutePath())) {
                    continue;
                }
                file.delete();
            }
        }
    }

    public static void showError(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}
