package com.yanshao.yanrecyclerview.widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yanshao.yanrecyclerview.R;

/**
 * Created  on 2016/10/24 0024.
 * Wang丶Yan
 */

public class YanRecyclerView extends LinearLayout {
    public static final int STAR_PULL_REFRESH = 0;//默认啦啦刷新
    public static final int STAR_RELESE_REFRESH = 1;//松开刷新
    public static final int STAR_REFRESING = 2;//刷新中...
    public static final int STAR_DOWN_REFRESH = 0;//默认上啦加载更多
    public static final int STAR_DOWN_RELESE_REFRESH = 1;//松开加载
    public static final int STAR_LOADING = 2;//加载中...
    private int mCurrState = STAR_PULL_REFRESH;//记录当前下拉的状态
    private int mCurrState2 = STAR_DOWN_REFRESH;//记录当前上啦的状态
    Context mcontext;
    int height;
    RelativeLayout layout;
    RelativeLayout Headview, FooterView;
    RecyclerView recyclerView;
    int StarY;
    int headerHeigh, footerHeigh;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;
    boolean isOpenLoad = false, isLoadingMore;
    TextView headText, footText;
    ProgressBar headProgress, footProgress;
    ImageView headImage, footImage;

    RotateAnimation rotateUp, rotateDown;

    public YanRecyclerView(Context context) {
        super(context, null);
    }

    public YanRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        View view = LayoutInflater.from(context).inflate(R.layout.yan_recyclerview, this, true);
        WindowManager wm = (WindowManager) getContext()
                .getSystemService(Context.WINDOW_SERVICE);

        int width = wm.getDefaultDisplay().getWidth();
        height = wm.getDefaultDisplay().getHeight();
        initAnim();

        layout = (RelativeLayout) view.findViewById(R.id.layout);
        //   layout.setOnTouchListener(touch);
        Headview = (RelativeLayout) view.findViewById(R.id.headview);
        headImage = (ImageView) view.findViewById(R.id.headImage);
        headProgress = (ProgressBar) view.findViewById(R.id.headprogress);
        headText = (TextView) view.findViewById(R.id.headerText);


        FooterView = (RelativeLayout) view.findViewById(R.id.footerview);
        footImage = (ImageView) view.findViewById(R.id.footImage);
        footProgress = (ProgressBar) view.findViewById(R.id.footprogress);
        footText = (TextView) view.findViewById(R.id.footText);


        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setOnTouchListener(recyclerViewtouch);

        Headview.measure(0, 0);
        headerHeigh = Headview.getMeasuredHeight();
        FooterView.measure(0, 0);
        footerHeigh = FooterView.getMeasuredHeight();
    }

    public YanRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mcontext = context;


    }


    OnTouchListener recyclerViewtouch = new OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            //   Log.e("yanshao","recyclerView=="+ adapter.getItemCount()+",heigh="+recyclerView.getHeight()+"   "+layoutManager.getHeight());

            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    Log.e("000000", "DOWN==" + motionEvent.getRawY());
                    StarY = (int) motionEvent.getRawY();

                    break;
                case MotionEvent.ACTION_UP:
                    Log.e("000000", "UP==" + motionEvent.getRawY());
                    StarY = 0;
                 /*   if(mCurrState2==STAR_RELESE_REFRESH){
                        mCurrState2=STAR_LOADING;
                        getRefreshDownstate();
                        FooterView.setPadding(0,0,0,0);
                    }else if (mCurrState2==STAR_DOWN_REFRESH){
                        FooterView.setPadding(0,0,0,-footerHeigh);
                    }*/
                    if (isLoadingMore) {
                        if (mCurrState2 == STAR_RELESE_REFRESH) {
                            mCurrState2 = STAR_LOADING;
                            getRefreshDownstate();
                            layout.setPadding(0, 0, 0, 0);
                        } else if (mCurrState2 == STAR_DOWN_REFRESH) {
                            layout.setPadding(0, 0, 0, -footerHeigh);
                        }
                        //isLoadingMore=false;
                    } else {
                        if (mCurrState == STAR_RELESE_REFRESH) {
                            mCurrState = STAR_REFRESING;
                            ReFreshState();
                            layout.setPadding(0, 0, 0, 0);
                        } else if (mCurrState == STAR_PULL_REFRESH) {
                            layout.setPadding(0, -headerHeigh, 0, 0);
                        }
                    }
                    break;
                case MotionEvent.ACTION_MOVE:

                    if (StarY == 0 || StarY == -1) {
                        StarY = (int) motionEvent.getRawY();
                    }
                    int endY = (int) motionEvent.getRawY();
                    int des = Math.abs(endY - StarY);
                    Log.e("yanshao", "StarY=" + StarY);
                    Log.e("000000", "MOVE==" + motionEvent.getRawY());
                    Log.e("yanshao", "des==" + des);
                    if (mCurrState != STAR_LOADING || mCurrState2 != STAR_LOADING) {
                        if (des > 50 && !recyclerView.canScrollVertically(-1) && endY - StarY > 50) {

                            isLoadingMore = false;
                            Headview.setVisibility(View.VISIBLE);
                            int padding = des - headerHeigh;
                            layout.setPadding(0, padding, 0, 0);
                            recyclerView.scrollToPosition(0);
                            if (padding > 0 && mCurrState == STAR_PULL_REFRESH) {
                                mCurrState = STAR_RELESE_REFRESH;
                                ReFreshState();
                            } else if (padding < 0 && mCurrState != STAR_PULL_REFRESH) {
                                mCurrState = STAR_PULL_REFRESH;
                                ReFreshState();
                            }

                        } else if (des > 50 && !recyclerView.canScrollVertically(1) && endY - StarY < -50) {
                            if (isOpenLoad) {

                                isLoadingMore = true;
                                FooterView.setVisibility(View.VISIBLE);
                                int padding = des - footerHeigh;
                                layout.setPadding(0, 0, 0, padding);
                                recyclerView.scrollToPosition(adapter.getItemCount() - 1);
                                // recyclerView.smoothScrollBy(0,height-(des*2)-100);
                                //
                                if (padding > 0 && mCurrState2 == STAR_DOWN_REFRESH) {
                                    mCurrState2 = STAR_DOWN_RELESE_REFRESH;
                                    getRefreshDownstate();
                                } else if (padding < 0 && mCurrState2 != STAR_DOWN_REFRESH) {
                                    mCurrState2 = STAR_LOADING;
                                    getRefreshDownstate();
                                }
                            }
                        }
                    }
                    break;

            }
            return false;
        }
    };

    public void setAdapter(RecyclerView.Adapter adpater) {
        this.adapter = adpater;
        recyclerView.setAdapter(adpater);
        // recyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);


    }

    public void setLayoutManager(RecyclerView.LayoutManager layout) {
        layoutManager = layout;
        recyclerView.setLayoutManager(layout);

    }

    public void setItemAnimator(RecyclerView.ItemAnimator animator) {
        recyclerView.setItemAnimator(animator);

    }


    /**
     * 是否开启上啦加载更多  默认关闭
     *
     * @param isopen
     */
    public void setIsopenLoad(boolean isopen) {
        isOpenLoad = isopen;
    }

    /**
     * 刷新头部的状态
     */
    private void ReFreshState() {
        switch (mCurrState) {
            case STAR_PULL_REFRESH:
                headText.setText("下拉刷新");
                headImage.setVisibility(View.VISIBLE);
                headProgress.setVisibility(View.GONE);
                headImage.startAnimation(rotateUp);
                break;
            case STAR_REFRESING:
                headText.setText("正在刷新中...");
                headImage.setVisibility(View.GONE);
                headProgress.setVisibility(View.VISIBLE);
                headImage.clearAnimation();
                if (onRefreshListener != null) {
                    onRefreshListener.refresh();
                }
                break;
            case STAR_RELESE_REFRESH:
                headText.setText("松开刷新");
                headImage.setVisibility(View.VISIBLE);
                headProgress.setVisibility(View.GONE);
                headImage.startAnimation(rotateDown);

                break;


        }
    }

    /**
     * 刷新底部
     */
    private void getRefreshDownstate() {
        switch (mCurrState2) {
            case STAR_DOWN_REFRESH:
                footText.setText("上啦加载更多");
                footImage.setVisibility(View.VISIBLE);
                footProgress.setVisibility(View.GONE);
                footImage.startAnimation(rotateUp);
                break;
            case STAR_LOADING:
                footText.setText("正在加载中...");
                footImage.setVisibility(View.GONE);
                footProgress.setVisibility(View.VISIBLE);
                footImage.clearAnimation();
                if (onRefreshListener != null) {
                    onRefreshListener.loadingMore();
                }
                break;

            case STAR_DOWN_RELESE_REFRESH:
                footText.setText("松开加载更多...");
                footImage.setVisibility(View.VISIBLE);
                footProgress.setVisibility(View.GONE);
                footImage.startAnimation(rotateDown);

                break;

        }
    }

    private void initAnim() {
        //箭头朝上
        rotateUp = new RotateAnimation(0, -180, RotateAnimation.RELATIVE_TO_SELF,
                0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        rotateUp.setDuration(500);
        rotateUp.setFillAfter(true);
        //箭头朝xia
        rotateDown = new RotateAnimation(-180, 0, RotateAnimation.RELATIVE_TO_SELF,
                0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        rotateDown.setDuration(500);
        rotateDown.setFillAfter(true);

    }

    /**
     * 刷新or加载完成
     */
    public void setOnRefreshComplete() {

        if (isLoadingMore) {
            mCurrState2 = STAR_DOWN_REFRESH;
            footText.setText("上啦加载更多");
            footImage.setVisibility(View.GONE);
            footProgress.setVisibility(View.GONE);
            layout.setPadding(0, 0, 0, 0);
            isLoadingMore = false;
            FooterView.setVisibility(View.GONE);
            recyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);


        } else {
            mCurrState = STAR_PULL_REFRESH;
            headText.setText("下啦刷新");
            headImage.setVisibility(View.GONE);
            headProgress.setVisibility(View.GONE);
            layout.setPadding(0, 0, 0, 0);
            Headview.setVisibility(View.GONE);
            recyclerView.smoothScrollToPosition(0);
        }
    }

    private onRefreshListener onRefreshListener;

    public void setonRefreshListener(onRefreshListener listener) {
        this.onRefreshListener = listener;
    }


    public interface onRefreshListener {
        void refresh();

        void loadingMore();
    }

}