package com.projectrixor.rixor.scrimmage.utils;

import com.projectrixor.rixor.scrimmage.Scrimmage;
import org.bukkit.plugin.PluginDescriptionFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

/**
 * @author MasterEjay
 */
public class UpdateUtil {

	static String version;
	static String plugin_name;

	public static boolean checkForUpdate(String new_version_adress, String new_plugin_adress, PluginDescriptionFile pdFile)
	{
		plugin_name = pdFile.getName();

		Scrimmage.getInstance().getLogger().info("["+plugin_name+"] Searching for update...");

		try
		{
			URL url1 = new URL(new_version_adress);
			URLConnection connection = url1.openConnection();

			ByteArrayOutputStream result1 = new ByteArrayOutputStream();
			java.io.InputStream input1 = connection.getInputStream();
			byte[] buffer = new byte[1000];
			int amount = 0;

			while(amount != -1)
			{

				result1.write(buffer, 0, amount);
				amount = input1.read(buffer);

				version = result1.toString();
			}

			String Version_String_Temp = version;
			Double version_double = Double.parseDouble(Version_String_Temp);

			String version_now = pdFile.getVersion();
			Double version_now_double = Double.parseDouble(version_now);

			if(version_now_double < version_double && version_double > version_now_double)
			{
				Scrimmage.getInstance().getLogger().info("["+plugin_name+"] Update found!");
				Scrimmage.getInstance().getLogger().info("["+plugin_name+"] Updating...");

				URL url2 = new URL(new_plugin_adress);
				ReadableByteChannel rbc1 = Channels.newChannel(url2.openStream());
				//File updateFolder = new File("plugins/update/");
				//updateFolder.mkdir();
				FileOutputStream fos1 = new FileOutputStream("plugins/" + plugin_name + ".jar");
				fos1.getChannel().transferFrom(rbc1, 0, 1 << 24);

				Scrimmage.getInstance().getLogger().info("["+plugin_name+"] Updated sucessfully!");
				return true;
			}
			else {
				Scrimmage.getInstance().getLogger().info("["+plugin_name+"] No update found!");
				return false;
			}
		}
		catch (MalformedURLException | FileNotFoundException e)
		{
			//e.printStackTrace();
		}catch (IOException e)
		{
			Scrimmage.getInstance().getLogger().severe("["+plugin_name+"] Unable to find Version-File!");
			Scrimmage.getInstance().getLogger().severe("["+plugin_name+"] Unable to load update!");
			return false;
			//e.printStackTrace();
		}
		return false;
	}
}
