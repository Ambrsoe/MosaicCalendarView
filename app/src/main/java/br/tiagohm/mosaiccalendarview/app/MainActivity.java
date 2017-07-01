package br.tiagohm.mosaiccalendarview.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import br.tiagohm.mosaiccalendarview.MosaicCalendarView;
import br.tiagohm.mosaiccalendarview.MosaicView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MosaicCalendarView mosaicCalendarView = (MosaicCalendarView) findViewById(R.id.calendar);
        mosaicCalendarView.setAdapter(new DateAdapter());
        mosaicCalendarView.setDateHeight(40);
        mosaicCalendarView.setDateWidth(40);
        mosaicCalendarView.setDateSpace(2);
        mosaicCalendarView.setWeekTextColor(0xFF767676);
        mosaicCalendarView.setMonthTextColor(0xFF767676);
        mosaicCalendarView.setOnDateListener(new MosaicView.OnDateListener() {
            @Override
            public void onDateClick(Calendar calendar) {
                Toast.makeText(MainActivity.this, calendar.getTime().toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private static class DateAdapter extends MosaicCalendarView.Adapter {

        private Calendar start, end, now;

        public DateAdapter() {
            now = Calendar.getInstance();
            start = Calendar.getInstance();
            end = Calendar.getInstance();
            end.add(Calendar.MONTH, 12);
        }

        @Override
        public Calendar getStartDate() {
            return start;
        }

        @Override
        public Calendar getEndDate() {
            return end;
        }

        @Override
        public int getColor(Calendar calendar) {
            final int val = (int) (Math.random() * 100);
            final int[] colors = new int[]{0xFFEEEEEE, 0xFFC6E48B, 0xFF7BC96F, 0xFF239A3B, 0xFF196127};
            return colors[val % colors.length];
        }

        @Override
        public String getWeekName(int position) {
            String[] semanas = new String[]
                    {"Dom", "Seg", "Ter", "Qua", "Qui", "Sex", "Sab"};
            return semanas[position];
        }

        @Override
        public String getMonthName(int position) {
            String[] meses = new String[]
                    {"Jan", "Fev", "Mar", "Abr", "Mai", "Jun", "Jul", "Ago", "Set", "Out", "Nov", "Dez"};
            return meses[position];
        }
    }
}
