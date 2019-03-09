package net.sterdsterd.daeryun.Activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import net.sterdsterd.daeryun.Activity.Meal.RecyclerAdapter;
import net.sterdsterd.daeryun.Activity.Meal.RecyclerItem;
import net.sterdsterd.daeryun.R;

import java.util.ArrayList;

public class MealActivity extends AppCompatActivity {


    ArrayList<String> datel = new ArrayList<>();
    ArrayList<String> todayl = new ArrayList<>();
    ArrayList<String> lunchl = new ArrayList<>();
    ArrayList<String> dinnerl = new ArrayList<>();


    private RecyclerView.Adapter adapter;
    RecyclerView recyclerView;
    private ArrayList<RecyclerItem> mItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        recyclerView = findViewById(R.id.rv);

        Intent intent = getIntent();
        datel = intent.getExtras().getStringArrayList("date");
        todayl = intent.getExtras().getStringArrayList("today");
        lunchl = intent.getExtras().getStringArrayList("lunch");
        dinnerl = intent.getExtras().getStringArrayList("dinner");
        setRecyclerView();

    }

    private void setRecyclerView(){
// 각 Item 들이 RecyclerView 의 전체 크기를 변경하지 않는 다면
// setHasFixedSize() 함수를 사용해서 성능을 개선할 수 있습니다.
// 변경될 가능성이 있다면 false 로 , 없다면 true를 설정해주세요.
        recyclerView.setHasFixedSize(true);

// RecyclerView에 Adapter를 설정해줍니다.
        adapter = new RecyclerAdapter(mItems);
        recyclerView.setAdapter(adapter);

// 다양한 LayoutManager 가 있습니다. 원하시는 방법을 선택해주세요.
// 지그재그형의 그리드 형식
//mainBinding.recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
// 그리드 형식
//mainBinding.recyclerView.setLayoutManager(new GridLayoutManager(this,4));
// 가로 또는 세로 스크롤 목록 형식
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        setData();
    }

    private void setData(){
        mItems.clear();
// RecyclerView 에 들어갈 데이터를 추가합니다.
        int colour[] = new int[]{ R.drawable.bg_01, R.drawable.bg_02, R.drawable.bg_03, R.drawable.bg_04, R.drawable.bg_05};
        for (int i = 0; i < datel.size(); i++) {
            mItems.add(new RecyclerItem(datel.get(i), todayl.get(i), lunchl.get(i), dinnerl.get(i), colour[i]));
        }
// 데이터 추가가 완료되었으면 notifyDataSetChanged() 메서드를 호출해 데이터 변경 체크를 실행합니다.
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


}
