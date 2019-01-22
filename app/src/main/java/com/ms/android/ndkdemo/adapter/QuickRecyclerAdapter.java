package com.ms.android.ndkdemo.adapter;

import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wnw on 16-5-20.
 * 加载更多的RecyclerView适配器
 */
public abstract class QuickRecyclerAdapter<T> extends RecyclerView.Adapter<BaseViewHolder> {

    public static final int TYPE_EMPTY_VIEW = Integer.MAX_VALUE >> 1;//说明是空的EmptyView
    public static final int TYPE_HEADER = Integer.MAX_VALUE >> 2;  //说明是带有Header的
    public static final int TYPE_FOOTER = Integer.MIN_VALUE >> 3;  //说明是带有Footer的
    public static final int TYPE_NORMAL = Integer.MAX_VALUE >> 4;  //说明是不带有header和footer的

    //获取从Activity中传递过来每个item的数据集合
    protected List<T> mDatas = new ArrayList<>();
    private int resId;
    private MultiItemRecyclerTypeSupport<T> multiItemTypeSupport;
    protected int itemPosition;

    //HeaderView, FooterView
    private View mHeaderView;
    private View mFooterView;
    private View mEmptyView;
    private View resView;

    public void setMultiItemTypeSupport(MultiItemRecyclerTypeSupport<T> multiItemTypeSupport) {
        this.multiItemTypeSupport = multiItemTypeSupport;
    }

    //构造函数
    public QuickRecyclerAdapter(int resId, List<T> list) {
        mDatas.clear();
        if (list!=null){
            this.mDatas.addAll(list);
        }
        this.resId = resId;
    }

    public QuickRecyclerAdapter(int resId) {
        this(resId, null);
    }

    public QuickRecyclerAdapter(View resView) {
        this.resView = resView;
    }

    @Override
    public void onViewAttachedToWindow(BaseViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
        if (lp != null
                && lp instanceof StaggeredGridLayoutManager.LayoutParams
                && holder.getLayoutPosition() == 0) {
            StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
            p.setFullSpan(true);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return getItemViewType(position) == TYPE_HEADER
                            ? gridManager.getSpanCount() : 1;
                }
            });
        }
    }

    //HeaderView和FooterView的get和set函数

    public View getHeaderView() {
        return mHeaderView;
    }

    public void setHeaderView(View headerView) {
        mHeaderView = headerView;
        notifyItemInserted(0);
    }

    public View getFooterView() {
        return mFooterView;
    }

    public void setFooterView(View footerView) {
        mFooterView = footerView;
        notifyItemInserted(getItemCount() - 1);
    }

    public void setEmptyView(View emptyView) {
        mEmptyView = emptyView;
        notifyDataSetChanged();
    }

    /**
     * 重写这个方法，很重要，是加入Header和Footer的关键，我们通过判断item的类型，从而绑定不同的view
     */
    @Override
    public int getItemViewType(int position) {
        if (position == 0 && mHeaderView != null) {
            //第一个item应该加载Header
            return TYPE_HEADER;
        }
        if (position == getItemCount() - 1 && mFooterView != null) {
            //最后一个,应该加载Footer
            return TYPE_FOOTER;
        }

        return mDatas.isEmpty() ? TYPE_EMPTY_VIEW : multiItemTypeSupport == null ? TYPE_NORMAL :
                multiItemTypeSupport.getMultiItemViewType(position);
    }

    //创建View，如果是HeaderView或者是FooterView，直接在Holder中返回
    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mHeaderView != null && viewType == TYPE_HEADER) {
            return new BaseViewHolder(mHeaderView) {
                @Override
                public String toString() {
                    return super.toString();
                }
            };
        }
        if (mFooterView != null && viewType == TYPE_FOOTER) {
            return new BaseViewHolder(mFooterView) {
                @Override
                public String toString() {
                    return super.toString();
                }
            };
        }
        if (viewType == TYPE_EMPTY_VIEW) {
            if (mEmptyView==null) {
                mEmptyView = new TextView(parent.getContext());
               // mEmptyView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, AutoUtils.getPercentHeightSize(180)));
            }
            return new BaseViewHolder(mEmptyView) {
                @Override
                public String toString() {
                    return super.toString();
                }
            };
        }
        if (multiItemTypeSupport != null) {
            return new BaseViewHolder(multiItemTypeSupport.getLayoutView(viewType)) {
                @Override
                public String toString() {
                    return super.toString();
                }
            };
        } else {
            View layout = resView;
            if (layout == null) {
                layout = LayoutInflater.from(parent.getContext()).inflate(resId, parent, false);
            }
            return new BaseViewHolder(layout) {
                @Override
                public String toString() {
                    return super.toString();
                }
            };
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    //绑定View，这里是根据返回的这个position的类型，从而进行绑定的， HeaderView和FooterView, 就不同绑定了
    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        int itemViewType = getItemViewType(position);
        int realPosition = getRealPosition(position);
        switch (itemViewType) {
            case TYPE_FOOTER:
                onBindViewFooter(holder, position);
                break;
            case TYPE_HEADER:
                onBindViewHeader(holder, position);
                break;
            case TYPE_NORMAL:
                onBindView(holder, realPosition, mDatas.get(realPosition));
                break;
            case TYPE_EMPTY_VIEW:
                onBindEmptyView(holder, position);
            default:
                if (multiItemTypeSupport != null) {
                    multiItemTypeSupport.onBindViewMultiSupport(holder.itemView, itemViewType, realPosition, mDatas.get(realPosition));
                }
        }
    }

    protected abstract void onBindView(BaseViewHolder holder, int position, T t);

    private void onBindEmptyView(BaseViewHolder holder, int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //eat click listener
            }
        });
    }

    private int getRealPosition(int position) {
        return mHeaderView == null ? position :
                position - 1;
    }

    protected void onBindViewHeader(BaseViewHolder holder, int position) {

    }

    protected void onBindViewFooter(BaseViewHolder holder, int position) {

    }

    //返回View中Item的个数，这个时候，总的个数应该是ListView中Item的个数加上HeaderView和FooterView
    @Override
    public int getItemCount() {
        int itemCount = mDatas.size();
        //如果集合为空,添加emptyView的个数
        if (itemCount == 0) itemCount++;
        if (mHeaderView != null) itemCount++;
        if (mFooterView != null) itemCount++;
        return itemCount;
    }

    public List<T> getData() {
        return mDatas;
    }

    public void setDataList(List<T> dataList) {
        if (dataList!=null){
            this.mDatas.clear();
            this.mDatas.addAll(dataList);
            notifyDataSetChanged();
        }
    }

    public void add(int position, T elem) {
        mDatas.add(position, elem);
        notifyItemInserted(position);
    }

    public void add(T elem) {
        mDatas.add(elem);
        notifyItemInserted(mDatas.size());
    }

    public void addAll(List<T> addList) {
        int lastIndex = mDatas.size();
        mDatas.addAll(addList);
        notifyItemRangeChanged(lastIndex, mDatas.size());
    }

    public void setselected(int itemPosition){
        this.itemPosition=itemPosition;
    }

    public int getAgoPosition() {
        return itemPosition;
    }

    /**
     * Clear data list
     */
    public void clear() {
        mDatas.clear();
        notifyDataSetChanged();
    }

    public void set(T oldElem, T newElem) {
        set(mDatas.indexOf(oldElem), newElem);
    }

    public void set(int index, T elem) {
        mDatas.set(index, elem);
        notifyDataSetChanged();
    }


    /**
     * Created by longShun on 2017/2/7.
     * desc
     */
    public static class SpaceItemDecoration extends RecyclerView.ItemDecoration {

        private int space;

        public SpaceItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

            if(parent.getChildPosition(view) != -1)
                outRect.bottom = space;
        }
    }
}