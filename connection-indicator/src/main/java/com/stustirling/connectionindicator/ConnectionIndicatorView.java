package com.stustirling.connectionindicator;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

public class ConnectionIndicatorView extends View {

    private static final int DEFAULT_ANIM_INTERVAL = 500;

    public enum AnimationType{INCREMENTAL,FLASH}
    private AnimationType currentAnimationType;

    private static final int MIN_BAR_COUNT = 3;

    private boolean isSearching;
    private int currentSearchStage;

    private int currentConnectionLevel;
    private int[] connectionLevelBarAmounts;

    private long searchAnimIntervalMillis;

    private Paint emptyConnectionPaint;
    private Paint searchingGPSBarPaint;
    private Paint connectionLevelOnePaint;
    private Paint connectionLevelTwoPaint;
    private Paint connectionLevelThreePaint;
    private Paint connectionLevelFourPaint;
    private Paint connectionLevelFivePaint;
    private Paint connectionLevelSixPaint;

    private ColorStateList colorEmpty;
    private ColorStateList colorSolid;
    private ColorStateList colorConnectionLevelOne;
    private ColorStateList colorConnectionLevelTwo;
    private ColorStateList colorConnectionLevelThree;
    private ColorStateList colorConnectionLevelFour;
    private ColorStateList colorConnectionLevelFive;
    private ColorStateList colorConnectionLevelSix;

    private int barCount;
    private int connectionLevels;

    private float barWidth;
    private float barSpacing;

    private int availableHeight;


    public ConnectionIndicatorView(Context context) {
        super(context);
        init(null);
    }

    public ConnectionIndicatorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init( getAttributesFromSet( context, attrs ));
    }

    public ConnectionIndicatorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init( getAttributesFromSet( context, attrs ));
    }

    private TypedArray getAttributesFromSet( Context context, AttributeSet attributeSet ) {
        return context.getTheme().obtainStyledAttributes(
                attributeSet,
                R.styleable.ConnectionIndicatorView,
                0,0);
    }

    private void init( TypedArray attributes ) {

        currentConnectionLevel = 0;

        if (attributes != null)
            extractAvailableAttributes(attributes);

        initPropertiesWithDefaultsIfNeeded();

        emptyConnectionPaint = new Paint();
        emptyConnectionPaint.setColor(colorEmpty.getDefaultColor());

        searchingGPSBarPaint = new Paint();
        searchingGPSBarPaint.setColor(colorSolid.getDefaultColor());

        connectionLevelOnePaint = new Paint();
        connectionLevelOnePaint.setColor(colorConnectionLevelOne.getDefaultColor());

        connectionLevelTwoPaint = new Paint();
        connectionLevelTwoPaint.setColor(colorConnectionLevelTwo.getDefaultColor());

        connectionLevelThreePaint = new Paint();
        connectionLevelThreePaint.setColor(colorConnectionLevelThree.getDefaultColor());

        connectionLevelFourPaint = new Paint();
        connectionLevelFourPaint.setColor(colorConnectionLevelFour.getDefaultColor());

        connectionLevelFivePaint = new Paint();
        connectionLevelFivePaint.setColor(colorConnectionLevelFive.getDefaultColor());

        connectionLevelSixPaint = new Paint();
        connectionLevelSixPaint.setColor(colorConnectionLevelSix.getDefaultColor());
    }

    private void extractAvailableAttributes(TypedArray attributes ) {
        try {
            colorEmpty = attributes.getColorStateList(R.styleable.ConnectionIndicatorView_emptyBarColor);
            colorSolid = attributes.getColorStateList(R.styleable.ConnectionIndicatorView_solidSearchBarColor);
            colorConnectionLevelOne=  attributes.getColorStateList(R.styleable.ConnectionIndicatorView_connectionLevelOneColor);
            colorConnectionLevelTwo =  attributes.getColorStateList(R.styleable.ConnectionIndicatorView_connectionLevelTwoColor);
            colorConnectionLevelThree =  attributes.getColorStateList(R.styleable.ConnectionIndicatorView_connectionLevelThreeColor);
            colorConnectionLevelFour =  attributes.getColorStateList(R.styleable.ConnectionIndicatorView_connectionLevelFourColor);
            colorConnectionLevelFive =  attributes.getColorStateList(R.styleable.ConnectionIndicatorView_connectionLevelFiveColor);
            colorConnectionLevelSix =  attributes.getColorStateList(R.styleable.ConnectionIndicatorView_connectionLevelSixColor);

            barCount = Math.max( MIN_BAR_COUNT, attributes.getInt(R.styleable.ConnectionIndicatorView_bars, MIN_BAR_COUNT));
            connectionLevels = attributes.getInt(R.styleable.ConnectionIndicatorView_connectionLevels, barCount );

            searchAnimIntervalMillis = attributes.getInt(R.styleable.ConnectionIndicatorView_animIntervalMillis, DEFAULT_ANIM_INTERVAL );
            currentAnimationType = getAnimationType( attributes.getInt( R.styleable.ConnectionIndicatorView_animType,0) );
        } finally {
            attributes.recycle();
        }
    }

    private AnimationType getAnimationType( int type ) {
        switch ( type ) {
            case 1: return AnimationType.FLASH;
            default: return AnimationType.INCREMENTAL;
        }
    }

    private void initPropertiesWithDefaultsIfNeeded() {
        if ( colorEmpty == null )
            colorEmpty = ContextCompat.getColorStateList(getContext(),R.color.defaultEmpty);

        if ( colorSolid == null )
            colorSolid = ContextCompat.getColorStateList(getContext(),R.color.defaultSolid);

        if ( colorConnectionLevelOne == null )
            colorConnectionLevelOne = colorSolid;

        if ( colorConnectionLevelTwo == null )
            colorConnectionLevelTwo = colorSolid;

        if ( colorConnectionLevelThree == null )
            colorConnectionLevelThree = colorSolid;

        if ( colorConnectionLevelFour == null )
            colorConnectionLevelFour = colorSolid;

        if ( colorConnectionLevelFive == null )
            colorConnectionLevelFive = colorSolid;

        if ( colorConnectionLevelSix == null )
            colorConnectionLevelSix = colorSolid;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        availableHeight = h;

        barSpacing = w /15.0f;

        barWidth = (w - (barSpacing*(barCount-1))) / barCount;

        connectionLevelBarAmounts = calculateConnectionLevelBarSpans( connectionLevels, barCount );
    }

    protected static int[] calculateConnectionLevelBarSpans( int levelCount, int barAmount ) {
        int[] levelBarAmounts = new int[levelCount];
        if ( barAmount % levelCount == 0 ) {
            for ( int i = 0; i < levelCount; i++ )
                levelBarAmounts[i] = barAmount/levelCount;
        } else {
            int remainder = barAmount % levelCount;
            int levelAmount = barAmount / levelCount;
            for ( int i = levelCount-1; i > -1; i-- ) {
                if  ( remainder > 0 ) {
                    levelBarAmounts[i] = levelAmount + 1;
                    remainder--;
                } else
                    levelBarAmounts[i] = levelAmount;
            }
        }
        return levelBarAmounts;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if ( isInEditMode() ) {
            drawEditMode(canvas);
        } else {
            drawBars( canvas );
        }
    }

    private void drawEditMode( Canvas canvas ) {

        for ( int i = 0; i < barCount; i++ ) {
            drawBar(i,canvas, emptyConnectionPaint);
        }

    }

    private void drawBars( Canvas canvas ) {
        if ( isSearching )
            drawSearchingBars( canvas );
        else
            drawConnectionBars( canvas );
    }

    private void drawSearchingBars( Canvas canvas ) {
        for ( int barPosition = 0; barPosition < barCount; barPosition++ ) {
            Paint barPaint = emptyConnectionPaint;
            if ( barPosition < currentSearchStage )
                barPaint = searchingGPSBarPaint;

            drawBar( barPosition, canvas, barPaint);
        }
    }

    private void drawConnectionBars( Canvas canvas ) {
        Paint reachedConnectionPaint = getPaintForConnectionLevel( currentConnectionLevel );

        if ( currentConnectionLevel == 0 ) {
            for ( int barPosition = 0; barPosition < barCount; barPosition++ )
                drawBar( barPosition, canvas, emptyConnectionPaint);
        } else {
            int drawnBarCount = 0;
            for ( int level = 0; level < connectionLevels; level++ ) {
                for ( int barPosition = drawnBarCount; barPosition < connectionLevelBarAmounts[level]+drawnBarCount; barPosition++ ) {
                    if ( level+1 <= currentConnectionLevel )
                        drawBar( barPosition, canvas, reachedConnectionPaint );
                    else
                        drawBar( barPosition, canvas, emptyConnectionPaint );
                }

                drawnBarCount += connectionLevelBarAmounts[level];
            }
        }
    }



    private Paint getPaintForConnectionLevel( int level ) {
        if ( (barCount % connectionLevels) == 0 ) {
            if ( level == 1 )
                return connectionLevelOnePaint;
            else if ( level== 2 )
                return connectionLevelTwoPaint;
            else if ( level == 3 )
                return connectionLevelThreePaint;
            else if ( level == 4 )
                return connectionLevelFourPaint;
            else if ( level == 5 )
                return connectionLevelFivePaint;
            else if ( level == 6 )
                return connectionLevelSixPaint;
            else if ( level > 6)
                throw new UnsupportedOperationException("Not implemented yet");
        }
        return searchingGPSBarPaint;
    }

    private void drawBar( int position, Canvas canvas, Paint paint ) {
        float leftSide = 0;
        float rightSide = barWidth;
        float top = 0;
        if ( position > 0 ) {
            leftSide = position * (barWidth + barSpacing );
            rightSide = leftSide + barWidth;
        }

        if ( position < barCount - 1 )
            top = availableHeight - ((availableHeight / barCount) * (position+1));

        canvas.drawRect(leftSide,top,rightSide,availableHeight,paint);
    }

    /**
     * Sets the indicator to be at the specified level. 0 is no bars, 1 is 1 bar and so on.
     *
     * @param connectionLevel - The amount of bars to show as solid.
     */
    public void displayConnectionLevel( int connectionLevel ) {
        setConnectionLevel( connectionLevel );
    }

    private void setConnectionLevel( int connectionLevel ) {
        currentConnectionLevel =connectionLevel;
        if ( isSearching )
            stopSearching();

        invalidate();
    }

    public void startSearching() {
        if ( !isSearching ) {
            currentConnectionLevel = 0;
            isSearching = true;
            currentSearchStage = 0;

            searchingHandler = new Handler();
            searchingHandler.postDelayed(searchingRunnable, searchAnimIntervalMillis);
        }
    }

    private Handler searchingHandler;
    private Runnable searchingRunnable = new Runnable() {
        @Override
        public void run() {

            if ( currentSearchStage == barCount )
                currentSearchStage = 0;
            else {
                if ( currentAnimationType == AnimationType.INCREMENTAL )
                    currentSearchStage++;
                else
                    currentSearchStage = barCount;
            }

            invalidate();
            searchingHandler.postDelayed(this, searchAnimIntervalMillis);
        }
    };

    public boolean isSearching() {
        return isSearching;
    }

    public void stopSearching() {
        if ( isSearching ) {
            isSearching = false;
            searchingHandler.removeCallbacks(searchingRunnable);
            searchingHandler = null;
        }
    }

    public int getBarCount() {
        return barCount;
    }



}
