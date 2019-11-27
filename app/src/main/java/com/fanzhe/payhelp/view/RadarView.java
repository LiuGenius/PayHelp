package com.fanzhe.payhelp.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.IntDef;

import com.fanzhe.payhelp.R;

import java.util.ArrayList;

public class RadarView extends FrameLayout {

    private Context mContext;
    private int viewSize = 800;
    private Paint mPaintLine;
    private Paint mPaintCircle;
    private Paint mPaintSector;
    public boolean isstart = false;
    private ScanThread mThread;
    private Paint mPaintPoint;
    //旋转效果起始角度
    private int start = 0;

    private ArrayList<Integer> point_x;
    private ArrayList<Integer> point_y;

    private Shader mShader;

    private Matrix matrix;

    public final static int CLOCK_WISE=1;
    public final static int ANTI_CLOCK_WISE=-1;

    Bitmap bm;

    @IntDef({ CLOCK_WISE, ANTI_CLOCK_WISE })
    public @interface RADAR_DIRECTION {

    }
    //默认为顺时针呢
    private final static int DEFAULT_DIERCTION = CLOCK_WISE;

    //设定雷达扫描方向
    private int direction = DEFAULT_DIERCTION;

    private boolean threadRunning = true;

    public RadarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initPaint();
    }

    public RadarView(Context context) {
        super(context);
        mContext = context;
        initPaint();

    }

    private void initPaint() {
        setBackgroundColor(Color.TRANSPARENT);

        //宽度=5，抗锯齿，描边效果的白色画笔
        mPaintLine = new Paint();
        mPaintLine.setStrokeWidth(3);
        mPaintLine.setAntiAlias(true);
        mPaintLine.setStyle(Paint.Style.STROKE);
        mPaintLine.setColor(Color.WHITE);

        //宽度=5，抗锯齿，描边效果的浅绿色画笔
        mPaintCircle = new Paint();
        mPaintCircle.setStrokeWidth(3);
        mPaintCircle.setAntiAlias(true);
        mPaintCircle.setStyle(Paint.Style.FILL);
        mPaintCircle.setColor(0x99000000);

        //暗绿色的画笔
        mPaintSector = new Paint();
        mPaintSector.setColor(0x9D32B5E7);
        mPaintSector.setAntiAlias(true);
        mShader = new SweepGradient(viewSize / 2, viewSize / 2, Color.TRANSPARENT, Color.parseColor("#32B5E7"));
        mPaintSector.setShader(mShader);

        //白色实心画笔
        mPaintPoint=new Paint();
        mPaintPoint.setColor(Color.WHITE);
        mPaintPoint.setStyle(Paint.Style.FILL_AND_STROKE);

        point_x = new ArrayList<>();
        point_y = new ArrayList<>();

        bm = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_redar);

        bm = warmthFilter(bm,0,0);
    }

    public void setViewSize(int size) {
        this.viewSize = size;
        setMeasuredDimension(viewSize, viewSize);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(viewSize, viewSize);
    }

    public void start() {
        mThread = new ScanThread(this);
        mThread.setName("radar");
        mThread.start();
        threadRunning = true;
        isstart = true;
    }

    public void stop() {
        if (isstart) {
            threadRunning = false;
            isstart = false;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawCircle(viewSize / 2, viewSize / 2, viewSize / 2, mPaintCircle);

        canvas.drawCircle(viewSize / 2, viewSize / 2, viewSize / 16 * 8, mPaintLine);
        canvas.drawCircle(viewSize / 2, viewSize / 2, viewSize / 16 * 7, mPaintLine);
        canvas.drawCircle(viewSize / 2, viewSize / 2, viewSize / 16 * 6, mPaintLine);
        canvas.drawCircle(viewSize / 2, viewSize / 2, viewSize / 16 * 5, mPaintLine);
        canvas.drawCircle(viewSize / 2, viewSize / 2, viewSize / 16 * 4, mPaintLine);
        canvas.drawCircle(viewSize / 2, viewSize / 2, viewSize / 16 * 3, mPaintLine);
        canvas.drawCircle(viewSize / 2, viewSize / 2, viewSize / 16 * 2, mPaintLine);
        canvas.drawCircle(viewSize / 2, viewSize / 2, viewSize / 16, mPaintLine);
        //绘制两条十字线
        canvas.drawLine(viewSize / 2, 0, viewSize / 2, viewSize, mPaintLine);
        canvas.drawLine(0, viewSize / 2, viewSize, viewSize / 2, mPaintLine);

        if (start % 50 == 0 && start > 50) {
            point_x.add(0,(int) (300 * (2 * Math.random() - 1)));
            point_y.add(0,(int) (300 * (2 * Math.random() - 1)));
            point_x.add(0,(int) (300 * (2 * Math.random() - 1)));
            point_y.add(0,(int) (300 * (2 * Math.random() - 1)));
        }



        for (int i = 0; i < (point_x.size() > 15 ? 15 : point_x.size()); i++) {
            canvas.drawCircle(viewSize / 2 + point_x.get(i), viewSize / 2 + point_y.get(i), 10, mPaintPoint);
//            canvas.drawBitmap(bm,viewSize / 2 + point_x.get(i),viewSize / 2 + point_y.get(i),mPaintPoint);
        }


//        if (start % 1000 == 0) {
////            mPaintPoint.setColor(Color.rgb(new Random().nextInt(255),new Random().nextInt(255),new Random().nextInt(255)));
//            start = 0;
//            point_x = UtilsHelper.Getrandomarray(15, 300);
//            point_y = UtilsHelper.Getrandomarray(15, 300);
//        }
//
//        //这里在雷达扫描过制定圆周度数后，将随机绘制一些白点，模拟搜索结果
//        if (start > 100) {
//            for (int i = 0; i < 2; i++) {
//                canvas.drawCircle(viewSize / 2 + point_x[i], viewSize / 2 + point_y[i], 10, mPaintPoint);
//            }
//        }
//        if (start > 200) {
//            for (int i = 2; i < 5; i++) {
//                canvas.drawCircle(viewSize / 2 + point_x[i], viewSize / 2 + point_y[i], 10, mPaintPoint);
//            }
//        }
//        if (start > 300) {
//            for (int i = 5; i < 9; i++) {
//                canvas.drawCircle(viewSize / 2 + point_x[i], viewSize / 2 + point_y[i], 10, mPaintPoint);
//            }
//        }
//        if (start > 500) {
//            for (int i = 9; i < 11; i++) {
//                canvas.drawCircle(viewSize / 2 + point_x[i], viewSize / 2 + point_y[i], 10, mPaintPoint);
//            }
//        }
//        if (start > 800) {
//            for (int i = 11; i < point_x.length; i++) {
//                canvas.drawCircle(viewSize / 2 + point_x[i], viewSize / 2 + point_y[i], 10, mPaintPoint);
//            }
//        }

        //根据matrix中设定角度，不断绘制shader,呈现出一种扇形扫描效果
        canvas.concat(matrix);
        canvas.drawCircle(viewSize / 2, viewSize / 2, viewSize / 2, mPaintSector);
        super.onDraw(canvas);
    }

    public void setDirection(@RADAR_DIRECTION int direction) {
        if (direction != CLOCK_WISE && direction != ANTI_CLOCK_WISE) {
            throw new IllegalArgumentException("Use @RADAR_DIRECTION constants only!");
        }
        this.direction = direction;
    }

    protected class ScanThread extends Thread {

        private RadarView view;

        public ScanThread(RadarView view) {
            this.view = view;
        }

        @Override
        public void run() {
            while (threadRunning) {
                if (isstart) {
                    view.post(() -> {
                        start = start + 1;
                        matrix = new Matrix();
                        //设定旋转角度,制定进行转转操作的圆心
//                            matrix.postRotate(start, viewSize / 2, viewSize / 2);
//                            matrix.setRotate(start,viewSize/2,viewSize/2);
                        matrix.preRotate(direction * start,viewSize / 2,viewSize / 2);
                        view.invalidate();
                    });
                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 暖意特效
     *
     * @param bmp
     *            原图片
     * @param centerX
     *            光源横坐标
     * @param centerY
     *            光源纵坐标
     * @return 暖意特效图片
     */
    public static Bitmap warmthFilter(Bitmap bmp, int centerX, int centerY) {
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        int dst[] = new int[width * height];
        bmp.getPixels(dst, 0, width, 0, 0, width, height);

        int ratio = width > height ? height * 32768 / width : width * 32768 / height;
        int cx = width >> 1;
        int cy = height >> 1;
        int max = cx * cx + cy * cy;
        int min = (int) (max * (1 - 0.8f));
        int diff = max - min;

        int ri, gi, bi;
        int dx, dy, distSq, v;

        int R, G, B;

        int value;
        int pos, pixColor;
        int newR, newG, newB;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                pos = y * width + x;
                pixColor = dst[pos];
                R = Color.red(pixColor);
                G = Color.green(pixColor);
                B = Color.blue(pixColor);

                value = R < 128 ? R : 256 - R;
                newR = (value * value * value) / 64 / 256;
                newR = (R < 128 ? newR : 255 - newR);

                value = G < 128 ? G : 256 - G;
                newG = (value * value) / 128;
                newG = (G < 128 ? newG : 255 - newG);

                newB = B / 2 + 0x25;

                // ==========边缘黑暗==============//
                dx = cx - x;
                dy = cy - y;
                if (width > height)
                    dx = (dx * ratio) >> 15;
                else
                    dy = (dy * ratio) >> 15;

                distSq = dx * dx + dy * dy;
                if (distSq > min) {
                    v = ((max - distSq) << 8) / diff;
                    v *= v;

                    ri = newR * v >> 16;
                    gi = newG * v >> 16;
                    bi = newB * v >> 16;

                    newR = ri > 255 ? 255 : (ri < 0 ? 0 : ri);
                    newG = gi > 255 ? 255 : (gi < 0 ? 0 : gi);
                    newB = bi > 255 ? 255 : (bi < 0 ? 0 : bi);
                }
                // ==========边缘黑暗end==============//

                dst[pos] = Color.rgb(newR, newG, newB);
            }
        }

        Bitmap acrossFlushBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        acrossFlushBitmap.setPixels(dst, 0, width, 0, 0, width, height);
        return acrossFlushBitmap;
    }
}
