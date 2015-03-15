package oskaro.synesthesia.oskar.leonad.franssom.synesthesiaconverter;


import java.util.Random;

/**
 * A classed used for drawing different figures on a canvas.
 */
public class CanvasFigure {

    private int type;
    private int offSetMin;
    private int offSetMax;
    private int sizeY;
    private int sizeX;
    private float strokeWidth;

    public CanvasFigure(float strokeWidth, int type, int sizeY, int sizeX, int offSetX, int offSetY) {
        if(setType(type)){
            this.offSetMin = offSetX;
            this.offSetMax = offSetY;
            this.sizeY = sizeY;
            this.sizeX = sizeX;
            this.strokeWidth = strokeWidth;
        }else{
            throw new IllegalArgumentException();
        }
    }


    public float[] drawFigureAt(int posX, int posY) {
        if (type == 2) {
            return getTwo(posX, posY);
        } else if (type == 5) {
            return getFive(posX, posY);
        }
        return null;
    }

    private float[] getTwo(int x, int y) {
        //Give the element an offsett
        int randomOffSetX = new Random().nextInt(offSetMin);
        int randomOffSetY = new Random().nextInt(offSetMax);
        x += randomOffSetX;
        y += randomOffSetY;

        float deleteCrocodileGap = strokeWidth/2;

        float stopX = x + sizeX;
        float stopY = y + sizeY;



        float[] aTwo = new float[]{
                x-deleteCrocodileGap, y, stopX+deleteCrocodileGap, y, //x1
                x, stopY + sizeY, x, stopY, //y2
                x-deleteCrocodileGap, stopY, stopX+deleteCrocodileGap, stopY, //x2
                stopX, stopY, stopX, y, //y1
                stopX+deleteCrocodileGap, stopY + sizeY, stopX - sizeX-deleteCrocodileGap, stopY + sizeY //x3
        };
        return aTwo;
    }

    private float[] getFive(int x, int y) {
        //Give the element an offsett of max 50x and 50y
        int randomOffSetX = new Random().nextInt(offSetMin);
        int randomOffSetY = new Random().nextInt(offSetMax);
        x += randomOffSetX;
        y += randomOffSetY;

        float deleteCrocodileGap = strokeWidth/2;

        float stopX = x + sizeX;
        float stopY = y + sizeY;

        float[] aFive = new float[]{
                x-deleteCrocodileGap, y, stopX+deleteCrocodileGap, y,
                x, y, x, stopY,
                x-deleteCrocodileGap, stopY, stopX+deleteCrocodileGap, stopY,
                stopX, stopY, stopX, stopY + sizeY,
                stopX+deleteCrocodileGap, stopY + sizeY, stopX - sizeX-deleteCrocodileGap, stopY + sizeY
        };

        return aFive;
    }

    //Only set the type if it equals a defined value.
    public boolean setType(int type) {
        if(type == 2 || type == 5){
            this.type = type;
            return true;
        }else
            return false;

    }

}
