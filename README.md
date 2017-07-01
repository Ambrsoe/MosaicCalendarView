# MosaicCalendarView

[![](https://jitpack.io/v/tiagohm/MosaicCalendarView.svg)](https://jitpack.io/#tiagohm/MosaicCalendarView)

![](https://github.com/tiagohm/MosaicCalendarView/blob/master/1.png?raw=true)

> Usufrua de um calendário que se parece com o Visualizador de Contribuições do perfil do seu GitHub.

## 1. Install

Add it in your root build.gradle at the end of repositories:
```groovy
allprojects {
  repositories {
    ...
    maven { url 'https://jitpack.io' }
  }
}
```
Add the dependency:
```groovy
dependencies {
	   compile 'com.github.tiagohm:MosaicCalendarView:LATEST-VERSION'
}
```

## 2. Use

Basic:

```xml
<br.tiagohm.mosaiccalendarview.MosaicCalendarView
        android:id="@+id/calendar"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:padding="5dp" />
```

```java
MosaicCalendarView mosaicCalendarView = (MosaicCalendarView) findViewById(R.id.calendar);
mosaicCalendarView.setAdapter(new MyAdapter());
mosaicCalendarView.setDateHeight(40);
mosaicCalendarView.setDateWidth(40);
mosaicCalendarView.setDateSpace(2);
mosaicCalendarView.setWeekTextColor(0xFF767676);
mosaicCalendarView.setMonthTextColor(0xFF767676);        
```

Listeners:

```java
mosaicCalendarView.setOnDateListener(new MosaicView.OnDateListener() {
    @Override
    public void onDateClick(Calendar calendar) {
        Toast.makeText(MainActivity.this, calendar.getTime().toString(), Toast.LENGTH_LONG).show();
    }
});
```

Adapter:

```java
private static class MyAdapter extends MosaicCalendarView.Adapter {

        private Calendar start, end;

        public DateAdapter() {
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
```

## 3. License

```
Copyright 2017 Tiago Melo

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
