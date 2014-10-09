package com.test.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.test.R;
import com.test.datastructure.Category;
import com.test.datastructure.CategoryInfo;

/** 品类列表适配器 */
public class CVCategoryListAdapter extends BaseAdapter {
	private List<Category> mList;
	private Activity mContext;
	private int row = -1;// 行
	private int column = -1;// 列

	private static final int LEFT = 0;// 左列
	private static final int CENTER = 1;// 中列
	private static final int RIGHT = 2;// 右列
	private static final int COLUMN = 3;// 列数
	
	int mapW;// 动画图片的宽度
	int offset;// 动画图片的偏移量
	int one,two;

	/** 设置数据源 */
	public void setData(List<Category> list) {
		mList = list;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	public CVCategoryListAdapter(Context context) {
		mContext = (Activity)context;
		
		mapW = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.cursor).getWidth();// 获取图片宽度
		DisplayMetrics dm = new DisplayMetrics();
		mContext.getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenW = dm.widthPixels;// 获取分辨率宽度
		offset = (screenW / COLUMN - mapW) / 2;// 计算偏移量
		one = offset * 2 + mapW;// 页卡1 -> 页卡2 偏移量
		two = one * 2;// 页卡1 -> 页卡3 偏移量
	}

	public Category getCategory(int position, int column) {
		int index = position * COLUMN + column;

		if (mList != null && index < mList.size() && index >= 0) {
			return mList.get(index);
		}

		return null;
	}

	@Override
	public int getCount() {

		if (mList != null) {
			int size = mList.size();

			if (size % COLUMN == 0) {
				return size / COLUMN;
			} else {
				return size / COLUMN + 1;
			}

		}

		return 0;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View view, ViewGroup parent) {
		ViewHolder viewHolder = null;

		if (view == null) {
			viewHolder = new ViewHolder();
			view = View.inflate(mContext, R.layout.item_category_list, null);
			viewHolder.leftLayout = (LinearLayout)view.findViewById(R.id.ly_left);
			viewHolder.leftTextView = (TextView) view.findViewById(R.id.tv_left);
			viewHolder.centerLayout = (LinearLayout)view.findViewById(R.id.ly_center);
			viewHolder.centerTextView = (TextView) view.findViewById(R.id.tv_center);
			viewHolder.rightLayout = (LinearLayout)view.findViewById(R.id.ly_right);
			viewHolder.rightTextView = (TextView) view.findViewById(R.id.tv_right);
			viewHolder.arrowImageView = (ImageView) view.findViewById(R.id.iv_arrow);
			viewHolder.categoryLinearLayout = (LinearLayout) view.findViewById(R.id.ll_category);
			view.setTag(viewHolder);
			
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}

		viewHolder.arrowImageView.setVisibility(View.GONE);
		Matrix matrix = new Matrix();
		matrix.postTranslate(offset, 0);
		viewHolder.arrowImageView.setImageMatrix(matrix); 
		
		viewHolder.categoryLinearLayout.setVisibility(View.GONE);

		for (int i = 0; i < COLUMN; i++) {
			Category category = getCategory(position, i);
			switch (i) {
			case LEFT:
				if (category != null) {
					viewHolder.leftTextView.setText(category.name);
				}
				break;
			case CENTER:
				if (category != null) {
					viewHolder.centerTextView.setText(category.name);
				}
				break;
			case RIGHT:
				if (category != null) {
					viewHolder.rightTextView.setText(category.name);
				}
				break;
			}
		}
		
		viewHolder.leftLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onSubItemClick(position, LEFT);
			}

		});
		
		viewHolder.centerLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onSubItemClick(position, CENTER);
			}

		});
		
		viewHolder.rightLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onSubItemClick(position, RIGHT);
			}
		});

		Category category = (Category) getCategory(position, getColumn());
		int row = getRow();
		if (row != -1 && row == position) {//判断是否当前行
			refreshView(viewHolder.arrowImageView, getColumn());
			if (category != null) {
				refreshCategory(viewHolder.categoryLinearLayout, category.list);
			}
		}

		return view;
	}

	private class ViewHolder {
		private TextView leftTextView;
		private TextView centerTextView;
		private TextView rightTextView;
		private ImageView arrowImageView;
		private LinearLayout categoryLinearLayout;
		private LinearLayout leftLayout;
		private LinearLayout centerLayout;
		private LinearLayout rightLayout;
	}

	/**
	 * row 点击行
	 * column 点击列
	 */
	public void onSubItemClick(int row, int column) {
		int selectedRow = getRow();//第几行
		int selectedColumn = getColumn();//第几行的第几个  列
		
		if(selectedRow==row){//判断是否当前行
			if(selectedColumn==column){//判断是否当前选中的item
				setRow(-1);
				setColumn(-1);
			}else{
				setColumn(column);
			}
		}else {
			setRow(row);
			setColumn(column);
		}
		notifyDataSetChanged();
	}
	
	/** 
	 * 刷新箭头
	 * i 当前行的  “列” 的下标
	 **/
	public void refreshView(final ImageView view, int i)
	{
		view.setVisibility(View.VISIBLE);
			int moving = 0;
			switch (i) {
			case 0:
				moving = offset;
				break;
			case 1:
				moving = one+offset;
				break;
			case 2:
				moving = two+offset;
				break;	
			}
			Matrix matrix = new Matrix();
			matrix.postTranslate(moving, 0);
			view.setImageMatrix(matrix); 
	}
	
	/** 刷新下拉框 */
	private void refreshCategory(LinearLayout categoryLinearLayout,
			List<CategoryInfo> list) {
		categoryLinearLayout.removeAllViews();

		if (list != null) {
			int size = list.size();
			LinearLayout linearLayout = null;
			LayoutParams layoutParams = new LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1);

			for (int i = 0; i < size; i++) {

				if (i % COLUMN == 0) {
					linearLayout = new LinearLayout(mContext);
					linearLayout.setOrientation(LinearLayout.HORIZONTAL);
					categoryLinearLayout.addView(linearLayout);
				}

				TextView textView = new TextView(mContext);
				textView.setGravity(Gravity.CENTER);
				textView.setLayoutParams(layoutParams);

				CategoryInfo categoryInfo = list.get(i);

				if (categoryInfo != null) {
					textView.setText(categoryInfo.name);
				}

				linearLayout.addView(textView);
			}
			categoryLinearLayout.setVisibility(View.VISIBLE);
		}
	}

}
