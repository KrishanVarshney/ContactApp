package com.example.contactprject;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class contact_list_adapter extends ArrayAdapter<contact_list> 
{
	Context context;
	int layoutid;
	contact_list[] data=null;
	

	public contact_list_adapter(Context context, int layoutid,contact_list[] data) {
		super(context, layoutid, data);
		this.context=context;
		this.layoutid=layoutid;
		this.data=data;
		
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		View row=convertView;
		contectholder holder=null;
		if(row==null)
		{
			LayoutInflater i=((Activity)context).getLayoutInflater();
			row=i.inflate(layoutid, parent,false);
			
			holder=new contectholder();
			holder.imgicon=(ImageView)row.findViewById(R.id.imageView1);
			holder.txttitle=(TextView)row.findViewById(R.id.textView1);
			
			row.setTag(holder);
		}
		else
		{
			holder=(contectholder)row.getTag();
		}
		contact_list cl=data[position];
		holder.txttitle.setText(cl.title);
		holder.imgicon.setImageBitmap(cl.icon);
		
		
		return row;
	}
	static class contectholder
	{
		ImageView imgicon;
		TextView txttitle;
		
	}

}
