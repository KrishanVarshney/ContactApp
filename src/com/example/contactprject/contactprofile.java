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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class contactprofile extends Activity
{
	ImageButton b1,b2,b4;
	ImageView iv1,pi;
	Button b3;
	TextView t1,t2;
	byte b[];
	Bitmap bm=null;
	String id="";
	String name="";
	String number="";
	String new_name="";
	String new_number="";
	
	
	protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contactprofile);
        
        
        
        b1=(ImageButton)findViewById(R.id.profile_imageButton1);//back
        b2=(ImageButton)findViewById(R.id.profile_imageButton2);//edit
        b3=(Button)findViewById(R.id.profile_button1);//delete
        b4=(ImageButton)findViewById(R.id.profile_imageButton3);//call
        t1=(TextView)findViewById(R.id.profile_textView2);//name
        t2=(TextView)findViewById(R.id.profile_textView3);//number
        iv1=(ImageView)findViewById(R.id.profile_imageView1);//photo
        
        
        setingprofile();
        
        
        
        //back
        b1.setOnClickListener(new OnClickListener() 
        {
			
			@Override
			public void onClick(View arg0) 
			{
				Intent i1=new Intent(contactprofile.this,contact.class);
				startActivity(i1);
			}
		});
        
        //edit
        b2.setOnClickListener(new OnClickListener()
        {
			
			@Override
			public void onClick(View arg0) 
			{
				LayoutInflater li=LayoutInflater.from(contactprofile.this);
				View v1=li.inflate(R.layout.editcontactinputdialoge, null);
				
				AlertDialog.Builder adb=new AlertDialog.Builder(contactprofile.this);
				adb.setView(v1);
				adb.setCancelable(false);
				
				final EditText et1=(EditText)v1.findViewById(R.id.edit_editText1);
				final EditText et2=(EditText)v1.findViewById(R.id.edit_editText2);
				final Button cb=(Button)v1.findViewById(R.id.edit_button1);
				final Button gb=(Button)v1.findViewById(R.id.edit_button2);
				pi=(ImageView)v1.findViewById(R.id.edit_imageView1);
				pi.setImageBitmap(bm);
				
				
				et1.setText(name);
				et2.setText(number);
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
				 
				adb.setPositiveButton("Update Contact", new DialogInterface.OnClickListener() 
				{
					
					@Override
					public void onClick(DialogInterface arg0, int arg1) 
					{
						new_name= et1.getText().toString().trim();
						 new_number= et2.getText().toString().trim();
						 convertimage();
						 getcontactid();
							
						/*if(new_name.equals(name))
						{
							try
							{
								SQLiteDatabase db=openOrCreateDatabase("mydatabase", MODE_PRIVATE, null);
								String sql="UPDATE contact SET name='"+new_name+"' WHERE number='"+number+"';";
								db.execSQL(sql);
								ContentValues cv=new ContentValues();
								cv.put("photo", b);
								db.update("contact", cv, "number="+number, null);
								db.close();
								t1.setText(new_name);
								iv1.setImageBitmap(bm);
							
								Toast.makeText(contactprofile.this, "Contact updated Successfully", Toast.LENGTH_LONG).show();
								
							}
							catch (Exception e)
							{
								Toast.makeText(contactprofile.this, ""+e, Toast.LENGTH_LONG).show();
							}
						
						}*/
						 if(id!=null)
						{
							try
							{
								
								SQLiteDatabase db=openOrCreateDatabase("mydatabase", MODE_PRIVATE, null);
								String sql="UPDATE contact SET number='"+new_number+"',name='"+new_name+"'  WHERE contactid='"+id+"';";
								db.execSQL(sql);
								ContentValues cv=new ContentValues();
								cv.put("photo", b);
								db.update("contact", cv, "contactid='"+id+"'", null);
								cv.clear();
								db.close();
								t1.setText(new_name);
								t2.setText(new_number);
								 
								Toast.makeText(contactprofile.this, "Contact updated Successfully", Toast.LENGTH_LONG).show();
								
							}
							catch (Exception e)
							{
								Toast.makeText(contactprofile.this, ""+e, Toast.LENGTH_LONG).show();
							}
						
						}
						else
						{
							//Toast.makeText(contactprofile.this, ""+new_name+" - "+new_number, Toast.LENGTH_LONG).show();
							Toast.makeText(contactprofile.this, "invalid edit", Toast.LENGTH_LONG).show();
							
						}
						 
			
					}
				});
				adb.setNegativeButton("cancel", new DialogInterface.OnClickListener() 
				{
					
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
        
        name=t1.getText().toString().trim();
        number=t2.getText().toString().trim();
        
        //deleting contact
        b3.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) 
			{
				AlertDialog.Builder adb=new AlertDialog.Builder(contactprofile.this);
				adb.setMessage("are you shure ");
				adb.setPositiveButton("ok", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						
						try
						{
							SQLiteDatabase db=openOrCreateDatabase("mydatabase", MODE_PRIVATE, null);
							String sql="DELETE FROM contact WHERE name='"+name+"';";
							db.execSQL(sql);
							db.close();
							Toast.makeText(contactprofile.this, "Contact DELETED Successfully", Toast.LENGTH_LONG).show();
							Intent i=new Intent(contactprofile.this,contact.class);
							startActivity(i);
							
						}
						catch(Exception e)
						{
							Toast.makeText(contactprofile.this, ""+e, Toast.LENGTH_LONG).show();
						}
						
					}
				});
				adb.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						
						arg0.cancel();
						
					}
				});
				adb.setCancelable(false);
				adb.create();
				adb.show();
				
				
				
			}
		});
        
        // calling
        b4.setOnClickListener(new OnClickListener()
        {
			
			@Override
			public void onClick(View arg0) 
			{
				Intent ii=new Intent(Intent.ACTION_CALL , Uri.parse("tel:"+number));
				try
				{
					startActivity(ii);
				}
				catch(Exception e)
				{
					Toast.makeText(contactprofile.this, ""+e, Toast.LENGTH_LONG).show();
				}
				
			}
		});
        
        
        
    }
	protected void getcontactid()
	{
		SQLiteDatabase db=openOrCreateDatabase("mydatabase", MODE_PRIVATE, null);
		String sql="SELECT * FROM contact WHERE name='"+name+"';";
		Cursor c=db.rawQuery(sql, null);
		int n=c.getCount();
		c.moveToFirst();
		if(n>0)
		{
			id=c.getString(c.getColumnIndex("contactid"));
		}
		c.close();
		db.close();
	}
	
	protected void convertimage()
	{
		ByteArrayOutputStream baos= new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.JPEG,100, baos);
		b=baos.toByteArray();
	}
	protected void onActivityResult(int requestCode, int resultCode, Intent i1) 
	{
		super.onActivityResult(requestCode, resultCode, i1);
		if(resultCode==RESULT_OK && i1!=null)
		{
			if (requestCode==1)
			{
				bm=(Bitmap) i1.getExtras().get("data"); 
				pi.setImageBitmap(bm);
				iv1.setImageBitmap(bm);
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
				iv1.setImageBitmap(bm);
				c.close();
			}
		}

	}
	protected void setingprofile()
	{
		name=getIntent().getExtras().getString("x");
        t1.setText(name);
        try
        {
        	SQLiteDatabase db=openOrCreateDatabase("mydatabase", MODE_PRIVATE, null);
        	String sql="SELECT * FROM contact WHERE name='"+name+"';";
        	Cursor c =db.rawQuery(sql, null);
        	if(c.getCount()>0)
        	{
        		c.moveToFirst();
            	number=c.getString(c.getColumnIndex("number"));
            	byte b[]=c.getBlob(c.getColumnIndex("photo"));
            	c.close();
            	db.close();
            	
            	
            	bm=BitmapFactory.decodeByteArray(b, 0, b.length);
    			iv1.setImageBitmap(bm);
            	
            	t2.setText(number);
        		
        	}
        	else
			{
				Toast.makeText(contactprofile.this, "Invalid", Toast.LENGTH_LONG).show();
			}

        	
        }
        catch(Exception e)
        {
        	Toast.makeText(contactprofile.this, ""+e, Toast.LENGTH_LONG).show();
        }
		
	}

}
