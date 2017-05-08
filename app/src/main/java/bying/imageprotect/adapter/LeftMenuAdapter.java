package bying.imageprotect.adapter;

import android.content.Context;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bying.imageprotect.R;

/**
 * Created by Wu_bying on 2017/5/7.
 */

public class LeftMenuAdapter {
    private List<Map<String,Object>> leftMenus = new ArrayList<Map<String,Object>>();
    private String[] names={"主页","上传","下载","搜索","好友","设置"};
    private int[] imgIds={
            R.drawable.iconfont_home,
            R.drawable.iconfont_share,
            R.drawable.iconfont_download,
            R.drawable.iconfont_search,
            R.drawable.iconfont_friends,
            R.drawable.iconfont_set
    };
    public SimpleAdapter getAdapter(Context context){
        initData();
        return new SimpleAdapter(context,leftMenus,R.layout.simple_item,new String[]{"name","icon"},new int[]{R.id.title,R.id.img,});
    }
    private void initData(){
        for(int i=0;i<names.length;i++){
            Map<String,Object> listItem = new HashMap<String,Object>();
            listItem.put("name",names[i]);
            listItem.put("icon",imgIds[i]);
            leftMenus.add(listItem);
        }
    }
}
