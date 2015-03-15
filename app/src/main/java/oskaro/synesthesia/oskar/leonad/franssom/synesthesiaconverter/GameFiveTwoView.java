package oskaro.synesthesia.oskar.leonad.franssom.synesthesiaconverter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;


/*
    A view containing 45 elements in a 5x12 lineup, an element can be a 2 or a 5.
 */

public class GameFiveTwoView extends View {

    private int squareSizeY;
    private int squareSizeX;
    private Paint paint;
    public int nrOfTimesFive = 0;


    public GameFiveTwoView(Context context, AttributeSet attrs, int defStyle) {
        this(context);
    }

    public GameFiveTwoView(Context context, AttributeSet attrs) {
        this(context);
    }

    public GameFiveTwoView(Context context) {
        super(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
        int parentHeight = MeasureSpec.getSize(heightMeasureSpec) / 2;
        setMeasuredDimension(parentWidth, parentHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.WHITE);

        //Width 1400 height 1039

        Log.i("GameFigureView", "Width " + canvas.getWidth() + " height " + canvas.getHeight());

        squareSizeY = canvas.getHeight()/5;
        squareSizeX = canvas.getWidth()/12;

        paint = new Paint();

        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(getResources().getDisplayMetrics().density*2);


        List<CanvasFigure> figures = createFigures(50);

        drawFigures(canvas, figures);

    }

    private void drawFigures(Canvas canvas, List<CanvasFigure> figures){
        Random random = new Random();

        // 7 x points elements
        int figureNumber = 0;
        for (int y = 0; y < 5; y++) {
            int[] skipSquare = new int[]{random.nextInt(7), random.nextInt(4)+7};
            for (int x = 0; x < 12; x++) {
                if(x == skipSquare[0] || x == skipSquare[1]){
                }else{
                    canvas.drawLines(figures.get(figureNumber).drawFigureAt(squareSizeX * x + 10, squareSizeY * y + 10), paint);
                    figureNumber++;
                }
            }
        }

    }

    private List<CanvasFigure> createFigures(int numbers ) {

        // squareSizeY and squareSizeX determines the figure sizes
        int figSizeX = squareSizeX/3;
        int figSizeY = squareSizeY/5;

        //between 8 and 13 5s
        nrOfTimesFive = new Random().nextInt(6) + 8;

        List<Integer> figType = new ArrayList<>();

        for (int x = 0; x < nrOfTimesFive; x++) {
            figType.add(5);
        }

        for(int x = nrOfTimesFive; x < numbers; x++){
            figType.add(2);
        }
        Collections.shuffle(figType);

        List<CanvasFigure> figures = new ArrayList<CanvasFigure>();

        for (int x = 0; x < numbers; x++) {
            figures.add(new CanvasFigure(paint.getStrokeWidth(), figType.get(x), figSizeY, figSizeX, squareSizeX/2, squareSizeY/2));
        }
//40,40,50,115
        return figures;

    }


}
