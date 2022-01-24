package ca.uwaterloo.cs349.pdfreader;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.*;
import android.os.Build;
import android.util.Log;
import android.util.Pair;
import android.view.MotionEvent;
import android.widget.ImageView;
import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

@SuppressLint("AppCompatCustomView")
public class PDFimage extends ImageView {

    final String LOGNAME = "pdf_image";

    HashMap<Integer, ArrayList<Path>> pagePaths = new HashMap<Integer, ArrayList<Path>>();
    HashMap<Integer, ArrayList<Paint>> pagePaintBrushes = new HashMap<Integer, ArrayList<Paint>>();

    // drawing path
    Path path = null;
    ArrayList<Path> paths = new ArrayList<Path>();

    // image to display
    Bitmap bitmap;

    // Drawing paint
    Paint paint = new Paint(Color.BLUE);
    ArrayList<Paint> paintBrushes = new ArrayList<Paint>();

    // Tools
    enum Tool {PEN, HIGHLIGHTER, ERASER, UNDO, REDO};
    Tool currentTool = Tool.PEN;

    int currentPage = 0;

    Tool lastOp;
    Stack<Pair<Path, Paint>> undoStack = new Stack<Pair<Path, Paint>>();
    int undoNumber = 0;

    // constructor
    public PDFimage(Context context) {
        super(context);
    }

    public HashMap<Integer, ArrayList<Path>> getPagePaths() {
        return pagePaths;
    }

    public void setPagePaths(HashMap<Integer, ArrayList<Path>> pagePaths) {
        this.pagePaths = pagePaths;
    }

    // capture touch events (down/move/up) to create a path
    // and use that to create a stroke that we can draw
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //Log.d(LOGNAME, "Action down");
                path = new Path();
                path.moveTo(event.getX(), event.getY());
                innitTool();
                if (currentTool != Tool.ERASER) {
                    paintBrushes.add(paint);
                    paths.add(path);
                }
                eraseDrawingClick(event.getX(), event.getY());
                break;
            case MotionEvent.ACTION_MOVE:
                //Log.d(LOGNAME, "Action move");
                path.lineTo(event.getX(), event.getY());
                eraseDrawingDrag();
                break;
            case MotionEvent.ACTION_UP:
                //Log.d(LOGNAME, "Action up");
                //paths.add(path);
                break;
        }
        return true;
    }

    // set image as background
    public void setImage(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    // set brush characteristics
    // e.g. color, thickness, alpha
    public void setBrush(Paint paint) {
        this.paint = paint;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // draw background
        if (bitmap != null) {
            this.setImageBitmap(bitmap);
        }
        // draw lines over it
        for (int i = 0; i < paths.size(); i++) {
            canvas.drawPath(paths.get(i), paintBrushes.get(i));
        }
        super.onDraw(canvas);
    }

    public void setCurrentTool(Tool tool) {
        this.currentTool = tool;
    }

    public void innitTool() {
        if (currentTool == Tool.PEN) {
            lastOp = Tool.PEN;
            innitPen();
        } else if (currentTool == Tool.HIGHLIGHTER) {
            lastOp = Tool.HIGHLIGHTER;
            innitHighlighter();
        } else if (currentTool == Tool.ERASER) {
            lastOp = Tool.ERASER;
            innitErase();
        }
    }

    public void innitPen() {
        paint = new Paint();
        paint.setStrokeWidth(5);
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.STROKE);
    }

    public void innitErase() {
        paint = new Paint();
        paint.setStrokeWidth(1);
        paint.setColor(Color.TRANSPARENT);
        paint.setStyle(Paint.Style.STROKE);
    }

    public void innitHighlighter() {
        paint = new Paint();
        int c = Color.YELLOW;
        paint.setStrokeWidth(25);
        paint.setColor(c);
        paint.setAlpha(200);
        paint.setStyle(Paint.Style.STROKE);
    }

    public void eraseDrawingClick(float x, float y) {
        if (currentTool == Tool.ERASER) {
            for (int i = 0; i < paths.size(); i++) {
                Path tempPath = new Path(); // Create temp Path
                tempPath.moveTo(x,y); // Move cursor to point
                RectF rectangle = new RectF(x-1, y-1, x+1, y+1); // create rectangle with size 2xp
                tempPath.addRect(rectangle, Path.Direction.CW); // add rect to temp path
                tempPath.op(paths.get(i), Path.Op.INTERSECT); // get difference with our PathToCheck
                if (!tempPath.isEmpty()) // if out path cover temp path we get empty path in result
                {
                    Path undoPath = new Path(paths.get(i));
                    Paint undoPaint = new Paint(paintBrushes.get(i));
                    Pair<Path, Paint> undoDrawing = new Pair<>(undoPath, undoPaint);
                    undoStack.push(undoDrawing);
                    paths.get(i).reset();
                }
            }
        }
    }

    public void eraseDrawingDrag() {
        if (currentTool == Tool.ERASER) {
            for (int i = 0; i < paths.size(); i++) {
                Region region1 = new Region();
                Region region2 = new Region();
                Region clip = new Region(0, 0, this.getWidth(), this.getHeight());
                region1.setPath(paths.get(i), clip);
                region2.setPath(path, clip);
                if (region1.op(region2, Region.Op.INTERSECT))
                {
                    Path undoPath = new Path(paths.get(i));
                    Paint undoPaint = new Paint(paintBrushes.get(i));
                    Pair<Path, Paint> undoDrawing = new Pair<>(undoPath, undoPaint);
                    undoStack.push(undoDrawing);
                    paths.get(i).reset();
                }
            }
        }
    }

    public void setPageDrawings(int page) {
        pagePaths.put(this.currentPage, paths);
        pagePaintBrushes.put(this.currentPage, paintBrushes);
        this.currentPage = page;
        paths = pagePaths.get(page);
        paintBrushes = pagePaintBrushes.get(page);
        if (paths == null) paths = new ArrayList<Path>();
        if (paintBrushes == null) paintBrushes = new ArrayList<Paint>();
        undoStack = new Stack<>();
    }

    public void undoDrawingTemp() {
        undoNumber++;
    }

    public void redoDrawingTemp() {
        if (undoNumber > 0) {
            undoNumber--;
        }
    }


    public void undoDrawing() {
        if (lastOp == Tool.PEN || lastOp == Tool.HIGHLIGHTER) {
            int lastIndex = paths.size() - 1;
            if (lastIndex >= 0) {
                Pair<Path, Paint> undoDrawing = new Pair<Path, Paint>(paths.get(lastIndex), paintBrushes.get(lastIndex));
                undoStack.add(undoDrawing);
                paths.remove(lastIndex);
                paintBrushes.remove(lastIndex);
            }
        } else if (lastOp == Tool.ERASER) {
            if (!undoStack.isEmpty()) {
                Pair<Path, Paint> redoDrawing = undoStack.pop();
                paths.add(redoDrawing.first);
                paintBrushes.add(redoDrawing.second);
            }
        } else if (lastOp == Tool.REDO) {
            int lastIndex = paths.size() - 1;
            if (lastIndex >= 0) {
                Pair<Path, Paint> undoDrawing = new Pair<Path, Paint>(paths.get(lastIndex), paintBrushes.get(lastIndex));
                undoStack.add(undoDrawing);
                paths.remove(lastIndex);
                paintBrushes.remove(lastIndex);
            }
        }
    }

    public void redoDrawing() {
        if (!undoStack.isEmpty()) {
            Pair<Path, Paint> redoDrawing = undoStack.pop();
            paths.add(redoDrawing.first);
            paintBrushes.add(redoDrawing.second);
        }
        lastOp = Tool.REDO;
    }
}
