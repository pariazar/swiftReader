
package com.pariazar.swiftReader;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import io.github.tonnyl.whatsnew.WhatsNew;
import io.github.tonnyl.whatsnew.item.WhatsNewItem;

public class Utils {

    public static boolean tempBool = false;

    static void showLog(AppCompatActivity context) {
        WhatsNew log = WhatsNew.newInstance(
                new WhatsNewItem("File Manager", "Enable on start of the app", R.drawable.star_icon),
                new WhatsNewItem("Zoom", "Changed from 3x to 5x", R.drawable.thumbs_icon)
                );
        log.setTitleColor(ContextCompat.getColor(context, R.color.colorAccent));
        log.setTitleText(context.getResources().getString(R.string.appChangelog));
        log.setButtonText(context.getResources().getString(R.string.buttonLog));
        log.setButtonBackground(ContextCompat.getColor(context, R.color.colorPrimary));
        log.setButtonTextColor(ContextCompat.getColor(context, R.color.colorAccent));
        log.setItemTitleColor(ContextCompat.getColor(context, R.color.colorAccent));
        log.setItemContentColor(Color.parseColor("#808080"));

        log.show(context.getSupportFragmentManager(), "Log");
    }

    public static String getAndroidVersion() {
        String release = Build.VERSION.RELEASE;
        int sdkVersion = Build.VERSION.SDK_INT;
        return "Android SDK: " + sdkVersion + " (" + release + ")";
    }

    static Intent emailIntent(String emailAddress, String subject, String text, String title) {
        Intent email = new Intent(Intent.ACTION_SEND);
        email.setType("text/email");
        email.putExtra(Intent.EXTRA_EMAIL, new String[]{emailAddress});
        email.putExtra(Intent.EXTRA_SUBJECT, subject);
        email.putExtra(Intent.EXTRA_TEXT, text);
        return Intent.createChooser(email, title);
    }

    static Intent emailIntent(String subject, String text, String title, Uri filePath) {
        Intent email = new Intent(Intent.ACTION_SEND);
        email.setType("text/email");
        email.putExtra(Intent.EXTRA_SUBJECT, subject);
        email.putExtra(Intent.EXTRA_TEXT, text);
        email.putExtra(Intent.EXTRA_STREAM, filePath);
        return Intent.createChooser(email, title);
    }

    static Intent linkIntent(String url) {
        Intent link = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        return link;
    }

    static Intent navIntent(Context context, Class activity) {
        Intent navigate = new Intent(context, activity);
        return navigate;
    }

    static String getAppVersion() {
        return BuildConfig.VERSION_NAME;
    }

    static void readFromInputStreamToOutputStream (InputStream inputStream, OutputStream outputStream) throws IOException {
        byte[] buffer = new byte[8 * 1024];
        int bytesRead = inputStream.read(buffer);
        while (bytesRead > -1) {
            outputStream.write(buffer, 0, bytesRead);
            bytesRead = inputStream.read(buffer);
        }

        outputStream.flush();
        outputStream.close();
    }

    static File createFileFromInputStream (File cacheDir, String fileName, InputStream inputStream) throws IOException {
        File file = File.createTempFile(fileName, null, cacheDir);
        OutputStream outputStream = new FileOutputStream(file);
        Utils.readFromInputStreamToOutputStream(inputStream, outputStream);
        return file;
    }
}
