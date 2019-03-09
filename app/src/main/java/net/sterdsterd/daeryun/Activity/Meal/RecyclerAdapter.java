package net.sterdsterd.daeryun.Activity.Meal;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import net.sterdsterd.daeryun.R;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ItemViewHolder> {
    ArrayList<RecyclerItem> mItems;

    public RecyclerAdapter(ArrayList<RecyclerItem> items){
        mItems = items;
    }


    // 새로운 뷰 홀더 생성
    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_meal, parent,false);
        return new ItemViewHolder(view);
    }


    // View 의 내용을 해당 포지션의 데이터로 바꿉니다.
    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        holder.date.setText(mItems.get(position).getDate());
        holder.today.setText(mItems.get(position).getToday());
        holder.lunch.setText(mItems.get(position).getLunch());
        holder.dinner.setText(mItems.get(position).getDinner());
        holder.cardbg.setBackgroundResource(mItems.get(position).getColour());
        if ((mItems.get(position).getDinner()).equals("")) holder.dinnersection.setVisibility(View.GONE);
    }

    // 데이터 셋의 크기를 리턴해줍니다.
    @Override
    public int getItemCount() {
        return mItems.size();
    }

    // 커스텀 뷰홀더
// item layout 에 존재하는 위젯들을 바인딩합니다.
    class ItemViewHolder extends RecyclerView.ViewHolder{
        private TextView date, today, lunch, dinner;
        LinearLayout dinnersection, cardbg;
        public ItemViewHolder(View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.date);
            today = itemView.findViewById(R.id.today);
            lunch = itemView.findViewById(R.id.lunch);
            dinner = itemView.findViewById(R.id.dinner);
            dinnersection = itemView.findViewById(R.id.dinsection);
            cardbg = itemView.findViewById(R.id.cardbg);
        }
    }
}