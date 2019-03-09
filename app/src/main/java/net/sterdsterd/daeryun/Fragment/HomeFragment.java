package net.sterdsterd.daeryun.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.sterdsterd.daeryun.Activity.MealActivity;
import net.sterdsterd.daeryun.Meal.MealTool;
import net.sterdsterd.daeryun.Meal.ProcessTask;
import net.sterdsterd.daeryun.R;
import net.sterdsterd.daeryun.Meal.Tools;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;

public class HomeFragment extends Fragment {


    public HomeFragment() {
        // Required empty public constructor
    }


    Calendar mCalendar;
    int YEAR, MONTH, DAY;
    int DAY_OF_WEEK;

    String TEMP, STAT, ICON;

    BapDownloadTask mProcessTask;
    ProgressDialog mDialog;

    ImageView wicon;

    CardView mealCard;

    public TextView lunch, dinner, date, temp;
    public LinearLayout dinsection, cardbg;


    ArrayList<String> datel = new ArrayList<>();
    ArrayList<String> todayl = new ArrayList<>();
    ArrayList<String> lunchl = new ArrayList<>();
    ArrayList<String> dinnerl = new ArrayList<>();
    int colour[] = new int[]{ R.drawable.bg_01, R.drawable.bg_02, R.drawable.bg_03, R.drawable.bg_04, R.drawable.bg_05};

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        lunch = getView().findViewById(R.id.lunch);
        dinner = getView().findViewById(R.id.dinner);
        date = getView().findViewById(R.id.date);
        temp = getView().findViewById(R.id.temp);
        dinsection = getView().findViewById(R.id.dinsection);
        cardbg = getView().findViewById(R.id.colour);

        wicon = getView().findViewById(R.id.wicon);

        getCalendarInstance(true);
        int dayNum = mCalendar.get(Calendar.DAY_OF_WEEK);

        cardbg.setBackgroundResource(getActivity().getApplicationContext().getResources().
                getIdentifier("drawable/bg_0" + (dayNum - 1), null, getActivity().getApplicationContext().getPackageName()));

        getBapList(true);

        mealCard = getView().findViewById(R.id.mealCard);
        mealCard.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MealActivity.class);
                intent.putExtra("date", datel);
                intent.putExtra("today", todayl);
                intent.putExtra("lunch", lunchl);
                intent.putExtra("dinner", dinnerl);
                startActivity(intent);
            }
        });


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String sURL = "http://api.openweathermap.org/data/2.5/weather?appid=0589e9af30d1baedc9159fdf408a019a&lat=35.8515848&lon=128.6540544&lang=kr";
                    URL url = new URL(sURL);
                    URLConnection request = url.openConnection();
                    request.connect();

                    JsonParser jp = new JsonParser();
                    JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent()));
                    JsonObject rootobj = root.getAsJsonObject();
                    TEMP = Double.toString(Math.round(rootobj.get("main").getAsJsonObject().get("temp").getAsDouble() - 273.15)) + "°\t";
                    STAT = rootobj.get("weather").getAsJsonArray().get(0).getAsJsonObject().get("description").getAsString();
                    ICON = rootobj.get("weather").getAsJsonArray().get(0).getAsJsonObject().get("icon").getAsString();
                    STAT = STAT.substring(0, 1).toUpperCase() + STAT.substring(1);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            date.setText(STAT);
                            temp.setText(TEMP);
                            wicon.setImageResource(getActivity().getApplicationContext().getResources().
                                    getIdentifier("drawable/w" + ICON, null, getActivity().getApplicationContext().getPackageName()));
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_home, container, false);

        return v;
    }

    private void getCalendarInstance(boolean getInstance) {
        if (getInstance || (mCalendar == null))
            mCalendar = Calendar.getInstance();
        YEAR = mCalendar.get(Calendar.YEAR);
        MONTH = mCalendar.get(Calendar.MONTH);
        DAY = mCalendar.get(Calendar.DAY_OF_MONTH);
        DAY_OF_WEEK = mCalendar.get(Calendar.DAY_OF_WEEK);
    }

    private void getBapList(boolean isUpdate) {
        boolean isNetwork = Tools.isOnline(getActivity());

        getCalendarInstance(false);

        final Calendar mToday = Calendar.getInstance();
        final int TodayYear = mToday.get(Calendar.YEAR);
        final int TodayMonth = mToday.get(Calendar.MONTH);
        final int TodayDay = mToday.get(Calendar.DAY_OF_MONTH);

        // 이번주 월요일 날짜를 가져온다
        mCalendar.add(Calendar.DATE, 2 - DAY_OF_WEEK);

        for (int i = 0; i < 5; i++) {
            int year = mCalendar.get(Calendar.YEAR);
            int month = mCalendar.get(Calendar.MONTH);
            int day = mCalendar.get(Calendar.DAY_OF_MONTH);

            MealTool.restoreBapDateClass mData =
                    MealTool.restoreBapData(getActivity(), year, month, day);

            if (mData.isBlankDay) {
                if (isUpdate && isNetwork) {
                    mDialog = new ProgressDialog(getActivity());
                    mDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    mDialog.setMax(100);
                    mDialog.setTitle("불러오는 중");
                    mDialog.setCancelable(false);
                    mDialog.show();

                    mProcessTask = new BapDownloadTask(getActivity());
                    mProcessTask.execute(year, month, day);
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.Theme_AppCompat_Dialog_Alert);
                    builder.setTitle("네트워크 연결 안됨");
                    builder.setMessage("재연결");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.show();
                }

                return;
            }

            // if day equals today
            if ((year == TodayYear) && (month == TodayMonth) && (day == TodayDay)) {
                lunch.setText(replaceString(mData.Lunch));
                if(replaceString(mData.Dinner).equals("")) {
                    dinsection.setVisibility(View.GONE);
                } else dinner.setText(replaceString(mData.Dinner));

                datel.add(mData.Calender);
                todayl.add(mData.DayOfTheWeek);
                lunchl.add(replaceString(mData.Lunch));
                dinnerl.add(replaceString(mData.Dinner));
                //mAdapter.addItem(mData.Calender, mData.DayOfTheWeek, mData.Lunch, mData.Dinner, true);
            } else {
                datel.add(mData.Calender);
                todayl.add(mData.DayOfTheWeek);
                lunchl.add(replaceString(mData.Lunch));
                dinnerl.add(replaceString(mData.Dinner));
                //mAdapter.addItem(mData.Calender, mData.DayOfTheWeek, mData.Lunch, mData.Dinner, false);
            }

            mCalendar.add(Calendar.DATE, 1);
        }

        mCalendar.set(YEAR, MONTH, DAY);
    }

    public static String replaceString(String mString) {
        String[] mTrash = {"11.", "12.", "13.", "14.", "15.", "16.", "17.", "18.", "9.", "10.",
                "1.", "2.", "3.", "4.", "5.", "6.", "7.", "8."};
        for (String e : mTrash) {
            mString = mString.replace(e, "");
        }
        return mString.trim();
    }

    public class BapDownloadTask extends ProcessTask {
        public BapDownloadTask(Context mContext) {
            super(mContext);
        }

        @Override
        public void onPreDownload() {

        }

        @Override
        public void onUpdate(int progress) {
            mDialog.setProgress(progress);
        }

        @Override
        public void onFinish(long result) {
            if (mDialog != null)
                mDialog.dismiss();

            if (result == -1) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.Theme_AppCompat_Dialog_Alert);
                builder.setTitle("오류");
                builder.setMessage("오류");
                builder.setPositiveButton(android.R.string.ok, null);
                builder.show();

                return;
            }

            getBapList(false);

        }
    }

}