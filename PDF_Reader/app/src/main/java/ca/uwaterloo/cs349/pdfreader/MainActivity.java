package ca.uwaterloo.cs349.pdfreader;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

// PDF sample code from
// https://medium.com/@chahat.jain0/rendering-a-pdf-document-in-android-activity-fragment-using-pdfrenderer-442462cb8f9a
// Issues about cache etc. are not at all obvious from documentation, so read this carefully.

public class MainActivity extends AppCompatActivity {

    final String LOGNAME = "pdf_viewer";
    final String FILENAME = "shannon1948.pdf";
    final int FILERESID = R.raw.shannon1948;

    // manage the pages of the PDF, see below
    PdfRenderer pdfRenderer;
    private ParcelFileDescriptor parcelFileDescriptor;
    private PdfRenderer.Page currentPage;

    // custom ImageView class that captures strokes and draws them over the image
    PDFimage pageImage;

    int totalPages = 0;
    int currentPageIndex = 0;

    TextView tv1;
    String totalPagesStr;
    String currentPageNumber;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(LOGNAME, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(FILENAME);
            //actionBar.setDisplayShowTitleEnabled(false);
        }

        LinearLayout layout = findViewById(R.id.pdfLayout);
        pageImage = new PDFimage(this);
        layout.addView(pageImage);
        layout.setEnabled(true);
        pageImage.setMinimumWidth(1000);
        pageImage.setMinimumHeight(2000);

        // open page 0 of the PDF
        // it will be displayed as an image in the pageImage (above)
        try {
            openRenderer(this);
            showPage(currentPageIndex);
        } catch (IOException exception) {
            Log.d(LOGNAME, "Error opening PDF");
        }

        totalPages = pdfRenderer.getPageCount();
        tv1 = findViewById(R.id.page_number);
        totalPagesStr = Integer.toString(totalPages);
        currentPageNumber = Integer.toString(currentPageIndex + 1);
        tv1.setText("Page " + currentPageNumber + "/" + totalPagesStr);
    }


    @Override
    protected void onStart() {
        Log.d(LOGNAME, "onStart");
        super.onStart();
        try {
            openRenderer(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.pdfedittools, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.pen:
                pageImage.setCurrentTool(PDFimage.Tool.PEN);
                Toast.makeText(this, "Pen", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.highlighter:
                pageImage.setCurrentTool(PDFimage.Tool.HIGHLIGHTER);
                Toast.makeText(this, "Highlighter", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.eraser:
                pageImage.setCurrentTool(PDFimage.Tool.ERASER);
                Toast.makeText(this, "Eraser", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.next:
                if (currentPageIndex < totalPages - 1) currentPageIndex++;
                showPage(currentPageIndex);
                currentPageNumber = Integer.toString(currentPageIndex + 1);
                tv1.setText("Page " + currentPageNumber + "/" + totalPagesStr);
                Toast.makeText(this, "Next", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.previous:
                if (currentPageIndex > 0) currentPageIndex--;
                showPage(currentPageIndex);
                currentPageNumber = Integer.toString(currentPageIndex + 1);
                tv1.setText("Page " + currentPageNumber + "/" + totalPagesStr);
                Toast.makeText(this, "Previous", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.undo:
                pageImage.undoDrawing();
                Toast.makeText(this, "Undo", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.redo:
                pageImage.redoDrawing();
                Toast.makeText(this, "Redo", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        Log.d(LOGNAME, "onBackPressed");
        finish();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onStop() {
        Log.d(LOGNAME, "onStop");
        super.onStop();
        try {
            closeRenderer();
        } catch (IOException ex) {
            Log.d(LOGNAME, "Unable to close PDF renderer");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void openRenderer(Context context) throws IOException {
        // In this sample, we read a PDF from the assets directory.
        File file = new File(context.getCacheDir(), FILENAME);
        if (!file.exists()) {
            // pdfRenderer cannot handle the resource directly,
            // so extract it into the local cache directory.
            InputStream asset = this.getResources().openRawResource(FILERESID);
            FileOutputStream output = new FileOutputStream(file);
            final byte[] buffer = new byte[1024];
            int size;
            while ((size = asset.read(buffer)) != -1) {
                output.write(buffer, 0, size);
            }
            asset.close();
            output.close();
        }
        parcelFileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY);

        // capture PDF data
        // all this just to get a handle to the actual PDF representation
        if (parcelFileDescriptor != null) {
            pdfRenderer = new PdfRenderer(parcelFileDescriptor);
        }
    }

    // do this before you quit!
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void closeRenderer() throws IOException {
        if (null != currentPage) {
            currentPage.close();
            currentPage = null;
        }
        pdfRenderer.close();
        parcelFileDescriptor.close();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void showPage(int index) {
        if (pdfRenderer.getPageCount() <= index) {
            return;
        }
        // Close the current page before opening another one.
        if (null != currentPage) {
            currentPage.close();
            currentPage = null;
        }
        // Use `openPage` to open a specific page in PDF.
        currentPage = pdfRenderer.openPage(index);
        // Important: the destination bitmap must be ARGB (not RGB).
        Bitmap bitmap = Bitmap.createBitmap(currentPage.getWidth(), currentPage.getHeight(), Bitmap.Config.ARGB_8888);

        // Here, we render the page onto the Bitmap.
        // To render a portion of the page, use the second and third parameter. Pass nulls to get the default result.
        // Pass either RENDER_MODE_FOR_DISPLAY or RENDER_MODE_FOR_PRINT for the last parameter.
        currentPage.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);

        // Display the page
        pageImage.setImage(bitmap);
        pageImage.setPageDrawings(currentPageIndex);
    }
}
