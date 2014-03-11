package com.projectrixor.rixor.scrimmage.utils;


import org.bukkit.ChatColor;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class DownloadMapUtil{

	public static boolean checkForMap(String mapname){
		String parsedMapName = mapname.replace(" ", "%20").replace("/", "");
		String returnValue;
		try
		{
			URL url1 = new URL("http://update.masterejay.us/maps/" + parsedMapName);
			URLConnection connection = url1.openConnection();

			ByteArrayOutputStream result1 = new ByteArrayOutputStream();
			java.io.InputStream input1 = connection.getInputStream();
			byte[] buffer = new byte[1000];
			int amount = 0;

			while(amount != -1)
			{

				result1.write(buffer, 0, amount);
				amount = input1.read(buffer);

				returnValue = result1.toString();
				if (returnValue.contains("Not Found")){
				   return false;
				}
				else {
					return true;
				}
			}
		}
		catch (MalformedURLException|FileNotFoundException e)
		{
			//e.printStackTrace();
			return false;
		}catch (IOException e)
		{
			return false;
			//e.printStackTrace();
		}
		return false;
	}
}
