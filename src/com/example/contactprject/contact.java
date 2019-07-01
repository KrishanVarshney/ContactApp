package com.example.contactprject;

import java.io.ByteArrayOutputStream;



import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

public class contact extends Activity 
{
	ImageButton b1,b2;
	EditText t1;
	ListView list;
	String name[];
	ImageView pi;
	byte ba[];
	byte b[];
	Bitmap bm=null;
	
	contact_list data[]=null;
	
	
	protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact);
        b1=(ImageButton)findViewById(R.id.contact_imageButton1);//add
        b2=(ImageButton)findViewById(R.id.contact_imageButton2);//search
        t1=(EditText)findViewById(R.id.contact_editText1);//search bar
        list=(ListView)findViewById(R.id.contact_listView1);
        showinlist();
        
       
        
        
        //adding contact
        
       b1.setOnClickListener(new OnClickListener() 
       {
		
		@Override
		public void onClick(View arg0) 
		{
			LayoutInflater li=LayoutInflater.from(contact.this);
			View v1=li.inflate(R.layout.addcontactinputdialog, null);
			AlertDialog.Builder adb=new AlertDialog.Builder(contact.this);
			adb.setView(v1);
			adb.setCancelable(false);
			
			final EditText et1=(EditText)v1.findViewById(R.id.add_editText1);
			final EditText et2=(EditText)v1.findViewById(R.id.add_editText2);
			final Button cb = (Button)v1.findViewById(R.id.add_button1);
			final Button gb=(Button)v1.findViewById(R.id.add_button2);
			pi=(ImageView)v1.findViewById(R.id.add_imageView1);
			
			bm=BitmapFactory.decodeResource(getResources(), R.drawable.co);
			//pi.setImageBitmap(bm);
			
			//taking photo from camera
			cb.setOnClickListener(new OnClickListener() 
			{
				
				@Override
				public void onClick(View arg0) 
				{
					Intent i=new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
					startActivityForResult(i,1);
					
					
				}
			});
			
			//taking photo from galary
			gb.setOnClickListener(new OnClickListener() 
			{
				
				@Override
				public void onClick(View arg0) 
				{
					Intent i=new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
					startActivityForResult(i,2);
					
				}
			});
			
			
		
			
			adb.setPositiveButton("Save Contact", new DialogInterface.OnClickListener() 
			{
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) 
				{
					try
					{
						SQLiteDatabase db=openOrCreateDatabase("mydatabase", MODE_PRIVATE, null);
						
						int contactid=generateid();
						
						convertimage();
						
						String name=et1.getText().toString().trim();
						String number=et2.getText().toString().trim();
						
						ContentValues cv=new ContentValues();
						
							cv.put("contactid", contactid);
							cv.put("name", name);
							cv.put("number", number);
							cv.put("photo",b);
						
							
						db.insert("contact", null, cv);
						cv.clear();
						db.close();
						
						Toast.makeText(contact.this, "contect added succesfully", Toast.LENGTH_LONG).show();
						
						showinlist();
						
						
						
						
						
					}
					catch(Exception e)
					{
						Toast.makeText(contact.this, ""+e, Toast.LENGTH_LONG).show();
					}
					
				}
			});
			adb.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) 
				{
					arg0.cancel();
					
				}
			});
			adb.create();
			adb.show();
			
			
		}
	}); 
       
       
       //searching button
       
       b2.setOnClickListener(new OnClickListener() 
       {
		
		@Override
		public void onClick(View arg0) 
		{
			String search=t1.getText().toString().trim();
			
			LayoutInflater li=LayoutInflater.from(contact.this);
			View v1= li.inflate(R.layout.searchlist, null);
			
			AlertDialog.Builder adb=new AlertDialog.Builder(contact.this);
			adb.setView(v1);
			adb.setCancelable(true);
			
			final ListView l=(ListView)v1.findViewById(R.id.search_listView1);
			
			try
			{
				SQLiteDatabase db=openOrCreateDatabase("mydatabase", MODE_PRIVATE, null);
				
				
				String sql="SELECT * FROM contact WHERE name LIKE '%"+search+"%';";
				
				Cursor c=db.rawQuery(sql, null);
				
				int count =c.getCount();
				if(count>0)
				{
					String name []=new String[count];
					int i=0;
					 /*for(c.moveToFirst();!c.isAfterLast();c.moveToNext())
					 {
						 name[i]=c.getString(c.getColumnIndex("name"));
						 i++;
						 
					 }*/
					 c.moveToFirst();
						do
						{
							name[i]=c.getString(c.getColumnIndex("name"));
							 i++;
							
							
						}while(c.moveToNext());
					 ArrayAdapter<String> adapter=new ArrayAdapter<String>(contact.this, android.R.layout.simple_list_item_1,android.R.id.text1,name);
						l.setAdapter(adapter);
						l.setOnItemClickListener(new OnItemClickListener() 
						{

							@Override
							public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
									long arg3) 
							{
								//int itemPosition     = arg2;
								String a=(String)l.getItemAtPosition(arg2);
								
								Intent i1=new Intent(contact.this,contactprofile.class);
								i1.putExtra("x",a );
								startActivity(i1);
								
							}
						});
						c.close();
						db.close();
					
					 
				}
				 else
				 {
					 Toast.makeText(contact.this,"No contact found", Toast.LENGTH_LONG).show();
				 }
				
				
			}
			catch(Exception e)
			{
				Toast.makeText(contact.this, ""+e, Toast.LENGTH_LONG).show();
			}
			
			adb.create();
			adb.show();
			
			
			
			
			
			
		}
	});
       
    }
	
	
	//image activity calling
	
	protected void onActivityResult(int requestCode, int resultCode, Intent i1) 
	{
		super.onActivityResult(requestCode, resultCode, i1);
		if(resultCode==RESULT_OK && i1!=null)
		{
			if (requestCode==1)
			{
				bm=(Bitmap) i1.getExtras().get("data"); 
				pi.setImageBitmap(bm);
			}
			else if (requestCode==2)
			{
				Uri pickedimage=i1.getData();
				String filepath[]={MediaStore.Images.Media.DATA};
				Cursor c=getContentResolver().query(pickedimage, filepath, null, null, null);
				c.moveToFirst();
				String imagepath=c.getString(c.getColumnIndex(filepath[0]));
				bm=BitmapFactory.decodeFile(imagepath);
				pi.setImageBitmap(bm);
				c.close();
			}
			
		}
		
		
			
			
		

	}
	//image to byte
	protected void convertimage()
	{
		ByteArrayOutputStream baos= new ByteArrayOutputStream();
		
		bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		b=baos.toByteArray();
	}

	private int generateid()
	{
		int id=1;
		
		SQLiteDatabase db=openOrCreateDatabase("mydatabase", MODE_PRIVATE, null);
		
		String sql="SELECT * FROM contact;";
		Cursor c=db.rawQuery(sql, null);
		int n= c.getCount();
		id=id+n;
		
		c.close();
		db.close();
		
		return id;
		
	}
	private void showinlist()
	{
		try
		{
			SQLiteDatabase db=openOrCreateDatabase("mydatabase", MODE_PRIVATE, null);
			String sql="SELECT * FROM contact ORDER BY name;";
			Cursor c=db.rawQuery(sql, null);
			if(c!=null && c.getCount()>0)
			{
				int count= c.getCount();
				ba =new byte [count];
				String name[]=new String[count];
				Bitmap imageid[]=new Bitmap[count];
				data=new contact_list[count];
				int i=0;
				/*for(c.moveToFirst();!c.isAfterLast();c.moveToNext())
				{
					ba=c.getBlob(c.getColumnIndex("photo"));
					imageid[i]=BitmapFactory.decodeByteArray(ba, 0, ba.length);
					name[i]=c.getString(c.getColumnIndex("name"));
					
					data [i]=new contact_list(imageid[i],name[i]);
					i++;
					
				}*/
				c.moveToFirst();
				do
				{
					ba=c.getBlob(c.getColumnIndex("photo"));
					imageid[i]=BitmapFactory.decodeByteArray(ba, 0, ba.length);
					name[i]=c.getString(c.getColumnIndex("name"));
					
					data [i]=new contact_list(imageid[i],name[i]);
					i++;
					
					
				}while(c.moveToNext());
				
				contact_list_adapter cla= new contact_list_adapter(contact.this, R.layout.rawlayout,data);
				list.setAdapter(cla);
				list.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
							long arg3) 
					{
						
						//int itemPosition     = arg2;
						contact_list a=(contact_list)list.getItemAtPosition(arg2);
						//Toast.makeText(contact.this, ""+a.title, Toast.LENGTH_LONG).show();
						
						Intent i1=new Intent(contact.this,contactprofile.class);
						i1.putExtra("x",a.title);
						startActivity(i1);
						
					}
				});
			
			}
			c.close();
			db.close();
			
			
		}
		catch(Exception e)
		{
			Toast.makeText(contact.this, ""+e, Toast.LENGTH_LONG).show();
		}
		
		
	}
}
