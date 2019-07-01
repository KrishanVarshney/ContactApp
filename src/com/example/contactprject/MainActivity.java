package com.example.contactprject;





import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity 
{
	Button b1;
	EditText t1,t2;
	TextView t;

    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        database();
        
        b1=(Button)findViewById(R.id.main_Button1);
        t1=(EditText)findViewById(R.id.main_editText1);
        t2=(EditText)findViewById(R.id.main_editText2);
        t=(TextView)findViewById(R.id.main_textView3);
        t.setVisibility(View.INVISIBLE);
        //login
        b1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) 
			{
				try
				{
					String username,password;
					username=t1.getText().toString().trim();
					password=t2.getText().toString().trim();
					
					SQLiteDatabase db=openOrCreateDatabase("mydatabase",MODE_PRIVATE,null);
					String sql="select  *  from login where username='"+username+"' and password='"+password+"'";
					Cursor c= db.rawQuery(sql, null);
					int count=c.getCount();
					c.close();
					db.close();
					if(count>0)
					{
						Intent i1=new Intent(MainActivity.this,contact.class);
						startActivity(i1);
						
					}
					else
					{
						t.setVisibility(View.VISIBLE);
						t.setText("Either username or password is incorrect");
					}
				}
				catch (Exception e)
				{
					Toast.makeText(MainActivity.this, ""+e, Toast.LENGTH_LONG).show();
				}
				
			}
		});

        
    }
    private void database()
    {
    	try
		{
			SQLiteDatabase db=openOrCreateDatabase("mydatabase",MODE_PRIVATE,null);
			db.execSQL("CREATE TABLE IF NOT EXISTS login(username varchar(100),password varchar(100));");
			db.execSQL("CREATE TABLE IF NOT EXISTS contact(contactid integer,name varchar(250),number varchar(50),photo blob);");
			
			
			Cursor c=db.rawQuery("select * from login", null);
			
			
			if(c.getCount()>0)
			{
				String str="";
				for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext())
				{
					str=str+c.getString(c.getColumnIndex("username"))+"\n";
					str=str+c.getString(c.getColumnIndex("password"))+"\n";
				}
				
			}
			else
			{
				db.execSQL("insert into login values('admin','admin');");
				
			}
			c.close();
			
			db.close();
		}
		catch(Exception e)
		{
			Toast.makeText(MainActivity.this, ""+e, Toast.LENGTH_LONG).show();
		}
    }

}
