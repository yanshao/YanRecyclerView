package com.yanshao.yanrecyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created  on 2016/10/21 0021.
 * Wang丶Yan
 */

public class TestAdapter extends RecyclerView.Adapter<MyViewHolder> {

private LayoutInflater mInflater;

private Context mcontext;

private List<String> data;

public interface OnItemClickListener {
    void OnItemClick(View v, int position);

    void OnItemLongClick(View v, int position);
}

    private RecyclerViewAdapter.OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(RecyclerViewAdapter.OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }


    private List<Integer> mHeight = new ArrayList<>();

    public TestAdapter(Context context, List<String> dtaa) {
        this.mInflater = LayoutInflater.from(context);
        this.mcontext = context;
        this.data = dtaa;
        for (int i = 0; i < data.size(); i++) {
            mHeight.add((int) (100 + Math.random() * 300));
        }
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = mInflater.inflate(R.layout.item, parent, false);

       MyViewHolder myViewHolder = new MyViewHolder(view);

        return myViewHolder;
    }

    //bangding
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
        lp.height = mHeight.get(position);
       holder.itemView.setLayoutParams(lp);
        holder.text.setText(data.get(position));

        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int  layoutPosition=holder.getLayoutPosition();
                    mOnItemClickListener.OnItemClick(holder.itemView, layoutPosition);
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    int  layoutPosition=holder.getLayoutPosition();
                    mOnItemClickListener.OnItemLongClick(holder.itemView, layoutPosition);
                    return false;
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }



    public void   AddData(int po){
        data.add(po,"我是刷新的数据");

        Log.e("yanshao","data=="+data);
        mHeight.clear();
        for (int i = 0; i < data.size(); i++) {
            mHeight.add((int) (100 + Math.random() * 300));
        }
        notifyItemInserted(po);
    }

    public void   aAddData(int po){
        data.add(po,"我是加载更多的数据");

        Log.e("yanshao","data=="+data);
        mHeight.clear();
        for (int i = 0; i < data.size(); i++) {
            mHeight.add((int) (100 + Math.random() * 300));

        }
        notifyItemInserted(po);
    }



    public void   DeleteData(int po){
        data.remove(po);
        notifyItemRemoved(po);
    }
}

class MyViewHolder extends RecyclerView.ViewHolder {

    TextView text;

    public MyViewHolder(View itemView) {
        super(itemView);


        text = (TextView) itemView.findViewById(R.id.text);
    }
}
