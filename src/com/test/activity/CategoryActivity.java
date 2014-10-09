package com.test.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.tanxiaoqiang.R;
import com.test.adapter.CVCategoryListAdapter;
import com.test.datastructure.Category;
import com.test.datastructure.CategoryInfo;

/** ����ҳ�� */
public class CategoryActivity extends Activity{
	private CVCategoryListAdapter mCategoryListAdapter;
	private ListView mListView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initPage();
	}
	
	/** ��ʼ��ҳ�� */
	private void initPage(){
		setContentView(R.layout.activity_category);
		
		mListView=(ListView) findViewById(R.id.lv);
		mCategoryListAdapter=new CVCategoryListAdapter(this); 
		mListView.setAdapter(mCategoryListAdapter);
		
		//ģ������
		List<Category> list=new ArrayList<Category>();
		
		for(int i=0;i<9;i++){
			Category category=new Category();
			category.name=i+"Ʒ��";
			
			List<CategoryInfo> list1=new ArrayList<CategoryInfo>();
			
			for(int j=0;j<15;j++){
				CategoryInfo categoryInfo=new CategoryInfo();
				categoryInfo.name=i+"Ʒ��"+j;
				list1.add(categoryInfo);
			}
			
			category.list=list1;
			list.add(category);
		}
		
		mCategoryListAdapter.setData(list);
		mCategoryListAdapter.notifyDataSetChanged();
	}
}
