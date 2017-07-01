package br.tiagohm.mosaiccalendarview;


import android.content.Context;
import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class MosaicView extends View {

    //Paint dos dias e do rótulo do meses.
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
    //Avisa que houve alguma alteração no Adapter.
    private final DataSetObserver mObserver = new DataSetObserver() {
        @Override
        public void onChanged() {
            invalidate();
        }
    };
    private final PointF startPoint = new PointF(), endPoint = new PointF();
    //Usado para desenhar os rótulos dos meses.
    private StaticLayout staticLayout;
    //Largura do dia.
    private int dateWidth;
    //Altura do dia.
    private int dateHeight;
    //Espaco entre os dias.
    private int dateSpace;
    //Cor do rótulo dos meses
    private int monthTextColor;
    //Adapter para preencher a cor dos dias e o intervalo de início e fim.
    private Adapter adapter;
    //Listeners
    private OnDateListener onDateListener;

    public MosaicView(Context context) {
        this(context, null);
    }

    public MosaicView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MosaicView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, @Nullable AttributeSet attrs) {
        if (isInEditMode()) {
            adapter = new Adapter() {
                Calendar startDate, endDate;

                @Override
                public Calendar getStartDate() {
                    return startDate;
                }

                @Override
                public Calendar getEndDate() {
                    return endDate;
                }

                @Override
                public int getColor(Calendar calendar) {
                    return Color.GREEN;
                }

                @Override
                public String getMonthName(int position) {
                    String[] meses = new String[]
                            {"Jan", "Fev", "Mar", "Abr", "Mai", "Jun", "Jul", "Ago", "Set", "Out", "Nov", "Dez"};
                    return meses[position];
                }

                public Adapter init() {
                    startDate = Calendar.getInstance();
                    endDate = Calendar.getInstance();
                    endDate.add(Calendar.MONTH, 3);
                    return this;
                }
            }.init();
        }

        setDateHeight(40);
        setDateWidth(40);
        setDateSpace(2);
        setMonthTextColor(Color.GRAY);
    }

    public void setOnDateListener(OnDateListener onDateListener) {
        this.onDateListener = onDateListener;
    }

    public int getDateWidth() {
        return dateWidth;
    }

    public void setDateWidth(int dateWidth) {
        if (dateWidth < 10)
            throw new RuntimeException("itemWidth must be greater or equal to 10");
        this.dateWidth = dateWidth;
        requestLayout();
    }

    public int getDateHeight() {
        return dateHeight;
    }

    public void setDateHeight(int dateHeight) {
        if (dateHeight < 10)
            throw new RuntimeException("itemHeight must be greater or equal to 10");
        this.dateHeight = dateHeight;
        textPaint.setTextSize(dateHeight);
        requestLayout();
    }

    public int getDateSpace() {
        return dateSpace;
    }

    public void setDateSpace(int dateSpace) {
        this.dateSpace = dateSpace;
        requestLayout();
    }

    public int getMonthTextColor() {
        return monthTextColor;
    }

    public void setMonthTextColor(int monthTextColor) {
        this.monthTextColor = monthTextColor;
        textPaint.setColor(monthTextColor);
        invalidate();
    }

    public Adapter getAdapter() {
        return adapter;
    }

    public void setAdapter(Adapter adapter) {
        if (this.adapter != null) {
            this.adapter.unregisterDataSetObserver(mObserver);
        }

        if (adapter == null) throw new RuntimeException("adapter is null");

        adapter.registerDataSetObserver(mObserver);

        this.adapter = adapter;
        this.adapter.notifyDataSetChanged();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //Calcula o tamanho necessário da view.
        final int measureWidth = ((getAdapter() == null ? 0 : getAdapter().getNumberOfDays() / 7) * (getDateWidth() + getDateSpace()) - getDateSpace())
                + getPaddingLeft() + getPaddingRight();
        final int measureHeight = (10 * (getDateHeight() + getDateSpace()) - getDateSpace())
                + getPaddingTop() + getPaddingBottom();

        final int width = MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.EXACTLY ?
                MeasureSpec.getSize(widthMeasureSpec) : measureWidth;
        final int height = MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.EXACTLY ?
                MeasureSpec.getSize(heightMeasureSpec) : measureHeight;

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (getAdapter() == null) return;

        //Desenhar nesta area delimitada.
        canvas.clipRect(getPaddingLeft(),
                getPaddingTop(),
                getRight() - getPaddingRight(),
                getBottom() - getPaddingBottom());
        //Quantidade de dias para exibir.
        int numberOfDays = getAdapter().getNumberOfDays();
        //Dia inicial.
        Calendar date = (Calendar) getAdapter().getStartDate().clone();
        date.add(Calendar.DATE, 1);

        if (numberOfDays > 0) {
            final int year = date.get(Calendar.YEAR);
            //Para cada dia.
            for (int week = 0; numberOfDays > 0; week++) {
                //Desenha uma coluna, 7 dias, representando a semana.
                for (int k = 0; k < 7 && numberOfDays > 0; k++, numberOfDays--) {
                    final int left = week * (getDateWidth() + getDateSpace()) + getPaddingLeft();
                    final int top = (k + 3) * (getDateHeight() + getDateSpace()) + getPaddingTop();
                    final int right = left + getDateWidth();
                    final int bottom = top + getDateHeight();
                    //Desenha o dia.
                    paint.setColor(getAdapter().getColor(date));
                    canvas.drawRect(left, top, right, bottom, paint);
                    if (date.get(Calendar.DATE) == 1) {
                        //Desenha os rótulos dos meses.
                        String text;
                        if (year != date.get(Calendar.YEAR) &&
                                date.get(Calendar.MONTH) == 0) {
                            text = String.format(Locale.ENGLISH, "%s/%d",
                                    getAdapter().getMonthName(date.get(Calendar.MONTH)),
                                    date.get(Calendar.YEAR) % 100);
                        } else {
                            text = String.format(Locale.ENGLISH, "%s",
                                    getAdapter().getMonthName(date.get(Calendar.MONTH)));
                        }
                        staticLayout = new StaticLayout(text, textPaint, (getDateWidth() + getDateSpace()) * 5, Layout.Alignment.ALIGN_NORMAL, 0, 0, false);
                        canvas.save();
                        canvas.translate(left, getDateHeight());
                        staticLayout.draw(canvas);
                        canvas.restore();
                    }
                    date.add(Calendar.DATE, 1);
                }
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final int action = event.getActionMasked();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                startPoint.x = event.getX();
                startPoint.y = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                endPoint.x = event.getX();
                endPoint.y = event.getY();
                final int offset = getOffsetAt(startPoint.x, startPoint.y);
                if (offset >= 0 &&
                        getAdapter() != null &&
                        offset < getAdapter().getNumberOfDays() &&
                        offset == getOffsetAt(endPoint.x, endPoint.y)) {
                    if (onDateListener != null) {
                        Calendar date = (Calendar) getAdapter().getStartDate().clone();
                        date.add(Calendar.DATE, offset);
                        onDateListener.onDateClick(date);
                    }
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
            case MotionEvent.ACTION_MOVE:
                break;
        }

        return true;
    }

    private int getOffsetAt(float x, float y) {
        final int week = (int) ((x - getPaddingLeft()) / (getDateWidth() + getDateSpace()));
        final int dayOfWeek = (int) ((y - getPaddingTop()) / (getDateHeight() + getDateSpace())) - 3;
        return week >= 0 && dayOfWeek >= 0 ? week * 7 + dayOfWeek : -1;
    }

    public interface OnDateListener {

        void onDateClick(Calendar calendar);
    }

    public static abstract class Adapter {

        private final DataSetObservable mDataSetObservable = new DataSetObservable();

        public void registerDataSetObserver(DataSetObserver observer) {
            mDataSetObservable.registerObserver(observer);
        }

        public void unregisterDataSetObserver(DataSetObserver observer) {
            mDataSetObservable.unregisterObserver(observer);
        }

        public abstract Calendar getStartDate();

        public abstract Calendar getEndDate();

        public abstract int getColor(Calendar calendar);

        public abstract String getMonthName(int position);

        public final int getNumberOfDays() {
            return (int) TimeUnit.DAYS.convert(
                    getEndDate().getTimeInMillis() - getStartDate().getTimeInMillis(), TimeUnit.MILLISECONDS);
        }

        public void notifyDataSetChanged() {
            mDataSetObservable.notifyChanged();
        }
    }
}
