package br.tiagohm.mosaiccalendarview;


import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.Calendar;

public class MosaicCalendarView extends FrameLayout {

    //Rótulos do dia da semana.
    private TextView[] mDaysOfWeek = new TextView[8];
    //Mosaico que exibe o calendário.
    private MosaicView mMosaicView;
    //Cor do rótulo do dia da semana.
    private int weekTextColor;

    public MosaicCalendarView(Context context) {
        this(context, null);
    }

    public MosaicCalendarView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MosaicCalendarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, @Nullable AttributeSet attrs) {
        LayoutInflater inflater = LayoutInflater.from(context);
        addView(inflater.inflate(R.layout.view_calendar, this, false));
        //Dias da semana
        mDaysOfWeek[7] = (TextView) findViewById(R.id.dayOfWeek0);
        mDaysOfWeek[0] = (TextView) findViewById(R.id.dayOfWeek1);
        mDaysOfWeek[1] = (TextView) findViewById(R.id.dayOfWeek2);
        mDaysOfWeek[2] = (TextView) findViewById(R.id.dayOfWeek3);
        mDaysOfWeek[3] = (TextView) findViewById(R.id.dayOfWeek4);
        mDaysOfWeek[4] = (TextView) findViewById(R.id.dayOfWeek5);
        mDaysOfWeek[5] = (TextView) findViewById(R.id.dayOfWeek6);
        mDaysOfWeek[6] = (TextView) findViewById(R.id.dayOfWeek7);
        //Calendário
        mMosaicView = (MosaicView) findViewById(R.id.mosaicView);

        setWeekTextColor(Color.GRAY);
    }

    public int getWeekTextColor() {
        return weekTextColor;
    }

    public void setWeekTextColor(int weekTextColor) {
        this.weekTextColor = weekTextColor;
        popularOsDiasDaSemana();
    }

    public int getDateWidth() {
        return mMosaicView.getDateWidth();
    }

    public void setDateWidth(int dateWidth) {
        mMosaicView.setDateWidth(dateWidth);
    }

    public int getDateHeight() {
        return mMosaicView.getDateHeight();
    }

    public void setDateHeight(int dateHeight) {
        mMosaicView.setDateHeight(dateHeight);
        popularOsDiasDaSemana();
    }

    public int getDateSpace() {
        return mMosaicView.getDateSpace();
    }

    public void setDateSpace(int dateSpace) {
        mMosaicView.setDateSpace(dateSpace);
        popularOsDiasDaSemana();
    }

    public int getMonthTextColor() {
        return mMosaicView.getMonthTextColor();
    }

    public void setMonthTextColor(int monthTextColor) {
        mMosaicView.setMonthTextColor(monthTextColor);
    }

    public Adapter getAdapter() {
        return (Adapter) mMosaicView.getAdapter();
    }

    public void setAdapter(Adapter adapter) {
        mMosaicView.setAdapter(adapter);
        popularOsDiasDaSemana();
    }

    private void popularOsDiasDaSemana() {
        if (getAdapter() != null) {
            int dayOfWeek = getAdapter().getStartDate().get(Calendar.DAY_OF_WEEK);
            for (int i = 0; i <= 7; i++, dayOfWeek++) {
                //Texto do rótulo dos dias da semana.
                mDaysOfWeek[i].setTextSize(TypedValue.COMPLEX_UNIT_PX, mMosaicView.getDateHeight() * 0.7f);
                if (i < 7) {
                    //Altura do texto dos rótulo dos dias da semana.
                    mDaysOfWeek[i].setHeight(mMosaicView.getDateHeight() + mMosaicView.getDateSpace());
                    mDaysOfWeek[i].setText(getAdapter().getWeekName(dayOfWeek % 7));
                } else {
                    mDaysOfWeek[i].setHeight((mMosaicView.getDateHeight() + mMosaicView.getDateSpace()) * 3);
                }
                //Altura do texto dos rótulo dos dias da semana.
                mDaysOfWeek[i].setTextColor(getWeekTextColor());
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    public static abstract class Adapter extends MosaicView.Adapter {

        public abstract String getWeekName(int position);
    }
}
