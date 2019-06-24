package com.hzp.nzhbj.fragment;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.hzp.nzhbj.R;
import com.hzp.nzhbj.activity.HomeActivity;
import com.hzp.nzhbj.base.BaseFragment;

import java.util.List;

/**
 * created by hzp on 2019/6/17 19:39
 * 作者：codehan
 * 描述：菜单页的fragment
 */
public class MenuFragment extends BaseFragment{

    private View view;
    private ListView mMenufragmentLvListview;
    private List<String> list;
    private Myadapter myadapter;
    /**保存被点击的条目的索引**/
    private int currentPostion;

    @Override
    public View initView() {
        /*TextView textView = new TextView( activity );
        textView.setText( "我是菜单页的fragment" );
        return textView;*/
        view = View.inflate( activity, R.layout.menufragment, null );
        return view;
    }

    @Override
    protected void initData() {
        mMenufragmentLvListview = (ListView) view.findViewById(R.id.menufragment_lv_listview);

    }

    public void initList(List<String> list){
        this.list=list;

        currentPostion =0;//保证每次初始化显示数据的时候，都是从0开始，保证每次都是0索引条目为选中样式

        //接受到传递过来的数据，通过listview展示数据
        if(myadapter==null) {
            myadapter = new Myadapter();
            mMenufragmentLvListview.setAdapter( myadapter );
        }else {
            myadapter.notifyDataSetChanged();
        }

        //设置listview的条目点击事件，实现点击条目切换条目样式的操作
        mMenufragmentLvListview.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //切换点击条目的样式
                currentPostion=position;
                myadapter.notifyDataSetChanged();//会调用adapter的getcount和getView方法

                //切换完条目的样式，将侧拉菜单关闭
                slidingMenu.toggle();//如果侧拉菜单是关闭，执行打开，如果是打开的，执行关闭
                //切换新闻中心中显示的界面
                ((HomeActivity)activity).getHomeFragment().getNewsCenterPager().switchPage( position );
            }
        } );
    }

    /**
     *listview的adapter
     */
    private class Myadapter extends BaseAdapter{
        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get( position );
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            ViewHolder viewHolder;
            if(convertView==null){
                view=View.inflate( activity,R.layout.menu_listview_item,null );
                viewHolder = new ViewHolder();
                viewHolder.mArrow=(ImageView)view.findViewById( R.id.item_iv_arrow );
                viewHolder.mTitle=(TextView)view.findViewById( R.id.item_tv_title );
                view.setTag( viewHolder );
            }else {
                view=convertView;
                viewHolder=(ViewHolder)view.getTag();
            }
            viewHolder.mTitle.setText( list.get( position ) );
            if(currentPostion==position){
                viewHolder.mArrow.setImageResource( R.drawable.menu_arr_select );
                viewHolder.mTitle.setTextColor( Color.RED );
            }else {
                viewHolder.mArrow.setImageResource( R.drawable.menu_arr_normal );
                viewHolder.mTitle.setTextColor( Color.WHITE );
            }
            return view;
        }
    }
    static class ViewHolder{
        TextView mTitle;
        ImageView mArrow;

    }
}
