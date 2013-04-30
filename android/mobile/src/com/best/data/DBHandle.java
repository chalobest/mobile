////////////////////////////////////////////////
//
// License: GPLv3
//
//
package com.best.data;

import java.util.List;
import java.util.ArrayList;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Hashtable;

import jsqlite.Callback;
import jsqlite.Exception;
import jsqlite.Stmt;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.util.Vector;
import android.content.Context;

import com.best.ui.Best;
import java.util.zip.GZIPInputStream;

public class DBHandle {

	private static final String TAG = DBHandle.class.getName();
	public static Context me = null;
	
	public static String _DB_NAME = "chalobest_5"; //NOTE: Inciment the number here (or name) to install the new DB"
	public static String _DATA_DIR = "data";
	public static String _DB_PATH = "data";
	public static int _DB_FILE_IDENTIFIER = com.best.ui.R.raw.chalobest;
	
	public static void init(Context cont)
	{me = cont;}
	
	
	public static String[][] execQuery(String query , int maxRows)
	{
		String[][] resultSet = new String[ maxRows ][];
		List<String[]> listResultSet = new ArrayList<String[]>();
		try 
		{
			if( maxRows > 0 )
			{
				jsqlite.Database db = new jsqlite.Database();
			
				db.open( _DB_PATH, jsqlite.Constants.SQLITE_OPEN_READONLY );		
				Stmt resHandle = db.prepare( query );
				int resCounter = 0;
				while ( resHandle.step() ) 
				{
					if( resCounter < maxRows )
					{
						int no_col = resHandle.column_count();
						
						resultSet[ resCounter ] = new String[ no_col ];
						
						for(int index=0;index<no_col;index++)
							resultSet[ resCounter ][ index ] = resHandle.column_string( index );
							
						listResultSet.add(resultSet[ resCounter ]);
						resCounter++;
					}	
				}
			}
		} catch (Exception e) {System.out.println("DBHandle Excep:"+e.getMessage()); e.printStackTrace();}
		
		return( listResultSet.toArray( new String[ listResultSet.size() ][] ) );
	}
	
	public static String[][] execQuery(String query)
	{
		Hashtable resultSet = new Hashtable();
		String[][] ret = null;
		int no_col = -1;
		try 
		{			
			jsqlite.Database db = new jsqlite.Database();
		
			db.open( _DB_PATH, jsqlite.Constants.SQLITE_OPEN_READONLY );		
			Stmt resHandle = db.prepare( query );
			int resCounter = 0;
			System.out.println("==db.prepare Complete===");
			while ( resHandle.step() ) 
			{
				no_col = resHandle.column_count();
				String[] row = new String[ no_col ];
				
				for(int index=0; index < no_col; index++ )
					row[ index ] = resHandle.column_string( index );
					
				resultSet.put( resCounter, row );	
				resCounter++;
			}	
			
			if( resultSet.size() > 0 && no_col > 0 )
			{
				ret = new String[ resultSet.size() ][ no_col ];
				for(int cnt = 0; cnt < resultSet.size(); cnt++ )
				{
					ret[ cnt ] = ( String[] )resultSet.get( cnt );
				}
			}	
		} catch (Exception e) { System.out.println("DBHandle Excep:"+e.getMessage()); e.printStackTrace();}
		return( ret );
	}
	public static void execInsDelQuery( String query )
	{
		try 
		{			
			jsqlite.Database db = new jsqlite.Database();
		
			db.open( _DB_PATH, jsqlite.Constants.SQLITE_OPEN_READWRITE );		
			Stmt resHandle = db.prepare( query );
			System.out.println("DBHANDLE:resHandle="+resHandle.toString());
			int no_col = resHandle.column_count();
			System.out.println("EXEC QUERY: "+no_col);
		}
		catch (Exception e) { System.out.println("DBHandle execInsDelQuery Excep:"+e.getMessage()); e.printStackTrace();}
	}
	public static void execInsertQuery( String query )
	{
	//the zip file is opened only to read i.e. input mode...so for insert we need to do tht as output mode line no: 183 and 184
		try 
		{			
			jsqlite.Database db = new jsqlite.Database();
		
			db.open( _DB_PATH, jsqlite.Constants.SQLITE_INSERT );		
			Stmt resHandle = db.prepare( query );
			String[] args = {"a","b"};
			//db.exec(query, null ,args );
			// System.out.println("DBHANDLE:resHandle="+resHandle.toString());
			// int no_col = resHandle.column_count();
			// System.out.println("EXEC QUERY: "+no_col);
		}
		catch (Exception e) { System.out.println("DBHandle execInsDelQuery Excep:"+e.getMessage()); e.printStackTrace();}
	}
	// public static void execInsDelQuery( String query )
	// {
		// try 
		// {			
			// jsqlite.Database db = new jsqlite.Database();
		
			// db.open( _DB_PATH, jsqlite.Constants.SQLITE_OPEN_READWRITE );		
			// Stmt resHandle = db.prepare( query );
			// System.out.println("DBHANDLE:resHandle="+resHandle.toString());
			// int no_col = resHandle.column_count();
			// System.out.println("EXEC QUERY: "+no_col);
		// }
		// catch (Exception e) { System.out.println("DBHandle execInsDelQuery Excep:"+e.getMessage()); e.printStackTrace();}
	// }
	
	public static boolean dbFileReady()
	{
		boolean Retvalue = false;
		try{
			//database folder object
			File DBFolder = new File( "//data//data//com.best.ui//databases//" );
			
			if( !DBFolder.exists() )
			{
				if( !DBFolder.mkdir() )
					return( false );
				System.out.println("DB FOLDER CREATED ");	
			}
			
			File DBFile = new File( DBFolder, _DB_NAME );
			
			System.out.println("DBFile DATAPATH: "+DBFile.getAbsolutePath());
			if( !DBFile.exists() )
			{
				System.out.println("DB FILE "+_DB_NAME+" does not exist");
				//remove old databases...
				File[] _oldFiles = DBFolder.listFiles();
				for( File _oldFile : _oldFiles )
				{
					_oldFile.delete();
					System.out.println("Deleted old file :"+_oldFile.getName());
				}
				
				Retvalue = DBFile.createNewFile();
				///////////
				InputStream dbin = me.getResources().openRawResource( _DB_FILE_IDENTIFIER );
				GZIPInputStream gzin = new GZIPInputStream( dbin );
				OutputStream os = new FileOutputStream( DBFile );
				//////////
				int r;
				byte []b = new byte[ 1024 ];
				
				while((r = gzin.read(b)) != -1)
					os.write(b, 0, r);
				dbin.close();
				os.close();
				
			}
			_DB_PATH = DBFile.getAbsolutePath();
			System.out.println( "DATABASE PATH:" + _DB_PATH );
			System.out.println("DATABASE SIZE:"+DBFile.length());
			return( true );
		}catch(java.lang.Exception e){ Best.log( " DBHandle ERR : " +e.getMessage() ); e.printStackTrace(); 
		System.out.println("RETURN VALUE:"+Retvalue);}
		return( false );
	}
}
