package com.pariazar.swiftReader;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.itextpdf.text.pdf.PdfReader;
import com.github.chrisbanes.photoview.PhotoView;
import com.shockwave.pdfium.PdfDocument;
import com.shockwave.pdfium.PdfiumCore;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;

public class Translate extends AppCompatActivity {
    private File file;
    private Uri address_book;
    private int page_number;
    private PhotoView pagePhoto;
    private TextView results;
    private Bitmap page_bitmap;
    private int MAX = 1000;

    private WebView webviewTrans;
    ProgressDialog progressDialog;
    private int noPages;
    private String res;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate);
        progressDialog = new ProgressDialog(this);


            /*    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Copied", etText.getText().toString());
                clipboard.setPrimaryClip(clip);

*/

        try{
            Bundle extras = getIntent().getExtras();
            if (extras != null && extras.containsKey("bookAddress")) {
                address_book = Uri.parse(extras.getString("bookAddress"));
            }
            //address_book = Uri.parse(getIntent().getStringExtra("bookAddress"));
            page_number = getIntent().getIntExtra("page_no",1);
            Log.d("u8745523",address_book.toString());
            Log.d("u8745523", String.valueOf(page_number));



            //file = new File(filePath);
        }catch (Exception e){

        }
        pagePhoto = findViewById(R.id.page);
        results = findViewById(R.id.result);
        webviewTrans = findViewById(R.id.webviewTrans);

        String mypath = FileUtils.getPath(this,MainActivity.uri);

        file = new File(mypath);
        Log.d("u8745523", mypath);
        Log.d("u8745523", String.valueOf(file.getAbsolutePath()));


        translateNow();





        PdfReader document = null;
        try {
            document = new PdfReader(new FileInputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
        noPages = document.getNumberOfPages();
    }

    private void translateNow() {
        page_bitmap = getBitmap(file,page_number);
        pagePhoto.setImageBitmap(page_bitmap);
        //results.setText("Ready to change");
        res = PDF_Tools.getTextOfPage(file.getAbsolutePath(),page_number);

        String first_res = res;
        StringBuilder finalResult = new StringBuilder();
        char[] str = first_res.toCharArray();

        str = replaceSpaces(str);

        for (int i = 0; i < str.length; i++){
            finalResult.append(str[i]);
        }

        String str1 = first_res.trim();

        // Replace All space (unicode is \\s) to %20
        str1 = str1.replaceAll("\\s", "%20");

        webviewTrans.getSettings().setJavaScriptEnabled(true);
        setUpWebViewDefaults(webviewTrans);
        final String translate_address = "https://translate.google.com/#view=home&op=translate&sl=auto&tl=fa&text="+str1;
        webviewTrans.loadUrl(translate_address);

        webviewTrans.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onPermissionRequest(final PermissionRequest request) {
                ///Log.d(TAG, "onPermissionRequest");
                Translate.this.runOnUiThread(new Runnable() {
                    @TargetApi(Build.VERSION_CODES.ECLAIR_MR1)
                    @Override
                    public void run() {
                        if(request.getOrigin().toString().equals(translate_address)) {
                            request.grant(request.getResources());
                        } else {
                            request.deny();
                        }
                    }
                });
            }

        });
        //"https://translate.google.com/#view=home&op=translate&sl=auto&tl=fa&text="+res.toString()*/
    }


    private char[] replaceSpaces(char[] str)
    {

        // count spaces and find current length
        int space_count = 0, i = 0;
        for (i = 0; i < str.length; i++)
            if (str[i] == ' ')
                space_count++;

        // count spaces and find current length
        while (str[i - 1] == ' ')
        {
            space_count--;
            i--;
        }

        // Find new length.
        int new_length = i + space_count * 2;

        // New length must be smaller than length
        // of string provided.
        if (new_length > MAX)
            return str;

        // Start filling character from end
        int index = new_length - 1;

        char[] old_str = str;
        str = new char[new_length];

        // Fill rest of the string from end
        for (int j = i - 1; j >= 0; j--)
        {

            // inserts %20 in place of space
            if (old_str[j] == ' ')
            {
                str[index] = '0';
                str[index - 1] = '2';
                str[index - 2] = '%';
                index = index - 3;
            }

            else
            {
                str[index] = old_str[j];
                index--;
            }
        }
        return str;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setUpWebViewDefaults(WebView webView) {
        WebSettings settings = webView.getSettings();

        // Enable Javascript
        settings.setJavaScriptEnabled(true);

        // Use WideViewport and Zoom out if there is no viewport defined
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);

        // Enable pinch to zoom without the zoom buttons
        settings.setBuiltInZoomControls(true);

        // Allow use of Local Storage
        settings.setDomStorageEnabled(true);

        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
            // Hide the zoom controls for HONEYCOMB+
            settings.setDisplayZoomControls(false);
        }

        // Enable remote debugging via chrome://inspect
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }

        webView.setWebViewClient(new WebViewClient());

        // AppRTC requires third party cookies to work
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptThirdPartyCookies(webviewTrans, true);
    }

    public void nextPageSelect(View view) {
        webviewTrans.stopLoading();
        page_number++;
        try{
            if(page_number>noPages){
                page_number--;
                Toast.makeText(this, "آخرین صفحه!", Toast.LENGTH_SHORT).show();
            }
            else {
                translateNow();
            }
        }
        catch (Exception e){
            Toast.makeText(this, "آخرین صفحه!", Toast.LENGTH_SHORT).show();
        }
    }

    public void backPageSelect(View view) {
        webviewTrans.stopLoading();
        page_number--;
        if(page_number<0){
            page_number++;
            Toast.makeText(this, "اولین صفحه!", Toast.LENGTH_SHORT).show();
        }
        else {
            translateNow();
        }
    }

    class TestAsync extends AsyncTask<Void, Integer, String> {
        String TAG = getClass().getSimpleName();

        protected void onPreExecute() {
            super.onPreExecute();
            Log.d(TAG + " PreExceute","On pre Exceute......");
        }

        protected String doInBackground(Void...arg0) {
            Log.d(TAG + " DoINBackGround", "On doInBackground...");

            for (int i=0; i<10; i++){
                Integer in = new Integer(i);
                publishProgress(i);
            }
            return "You are at PostExecute";
        }

        protected void onProgressUpdate(Integer...a) {
            super.onProgressUpdate(a);
            Log.d(TAG + " onProgressUpdate", "You are in progress update ... " + a[0]);
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d(TAG + " onPostExecute", "" + result);
        }
    }

    public Bitmap getBitmap(File file, int pageNum){
        PdfiumCore pdfiumCore = new PdfiumCore(Translate.this);
        try {
            PdfDocument pdfDocument = pdfiumCore.newDocument(openFile(file));
            pdfiumCore.openPage(pdfDocument, pageNum);

            int width = pdfiumCore.getPageWidthPoint(pdfDocument, pageNum);
            int height = pdfiumCore.getPageHeightPoint(pdfDocument, pageNum);


            // ARGB_8888 - best quality, high memory usage, higher possibility of OutOfMemoryError
            // RGB_565 - little worse quality, twice less memory usage
            Bitmap bitmap = Bitmap.createBitmap(width , height ,
                    Bitmap.Config.RGB_565);
            pdfiumCore.renderPageBitmap(pdfDocument, bitmap, pageNum, 0, 0,
                    width, height);
            //if you need to render annotations and form fields, you can use
            //the same method above adding 'true' as last param

            pdfiumCore.closeDocument(pdfDocument); // important!
            return bitmap;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static ParcelFileDescriptor openFile(File file) {
        ParcelFileDescriptor descriptor;
        try {
            descriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return descriptor;
    }


}