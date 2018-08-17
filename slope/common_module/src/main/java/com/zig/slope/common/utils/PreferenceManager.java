package com.zig.slope.common.utils;

//import com.ider.background.ConfigService;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


public class PreferenceManager {
	Context context;
	private static SharedPreferences preferences;
	private static PreferenceManager manager;
	Editor editor;
	
	public static PreferenceManager getInstance(Context context) {
		if(manager == null) {
			manager = new PreferenceManager(context);
		}
		return manager;
	}

	private PreferenceManager(Context context) {
		this.context = context;
		preferences = context.getSharedPreferences("fla", Context.MODE_PRIVATE);
		editor = preferences.edit();
	}

	public  void putString(String tag, String pkgName) {
		if (preferences.getString(tag, null) != null) {
			editor.remove(tag);
			editor.commit();
		}
		editor.putString(tag, pkgName);
		editor.commit();
	}

	public void delete(String tag) {
		editor.remove(tag);
		editor.commit();
	}
	public synchronized String getPackage(String tag) {
		String packageName = preferences.getString(tag, null);
		return packageName;
	}
	public boolean isFirstRun() {
		return preferences.getBoolean("first_run", true);
	}

	public void setFirstRun(boolean firstRun) {
		editor.putBoolean("first_run", firstRun);
		editor.commit();
	}
	public void putBoolean(String key, boolean b) {
		editor.putBoolean(key, b);
		editor.commit();
	}
	public boolean cacheDeleted() {
		return preferences.getBoolean("cache_removed", false);
	}
	public void setCacheDeleted() {
		editor.putBoolean("cache_removed", true);
		editor.commit();
	}
	public String getManuCity() {
		return preferences.getString("city", null);
	}
	public void setManuCity(String city) {
		if(getManuCity() != null) {
			removeCity();
		}
		editor.putString("city", city);
		editor.commit();
	}
	public void removeCity() {
		editor.remove("city");
		editor.commit();
	}

	public void setBootPackage(String pkg) {
		editor.putString("boot_package", pkg);
		editor.commit();
	}
	public void removeBootPackage() {
		editor.remove("boot_package");
		editor.commit();
	}
	public String getBootPackage() {
		return preferences.getString("boot_package", null);
	}
	
	public int getBootCount() {
		return preferences.getInt("boot_count", 0);
	}
	public void updateBootCount() {
		int count = getBootCount();
		editor.putInt("boot_count", count + 1);
		editor.commit();
	}





}
	
	


