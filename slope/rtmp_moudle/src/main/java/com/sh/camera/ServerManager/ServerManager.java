/*  car eye 车辆管理平台
 * car-eye管理平台   www.car-eye.cn
 * car-eye开源网址:  https://github.com/Car-eye-team
 * Copyright
 */
package com.sh.camera.ServerManager;

import android.content.SharedPreferences;
import com.sh.camera.util.Constants;

import com.sh.camera.SetActivity;
import com.sh.camera.service.MainService;


/**    
 *     
 * 项目名称：SH-camera   
 * 类名称：SH-camera   
 * 类描述：摄像头工具类    
 * 创建人：Administrator    
 * 创建时间：2016年10月12日 下午2:35:47    
 * 修改人：Administrator    
 * 修改时间：2016年10月12日 下午2:35:47    
 * 修改备注：    
 * @version 1.0  
 *     
 */
public class ServerManager {

	private static final String tag = "ServerManger.";
	private static ServerManager instance;
	private static SharedPreferences sp;
	SharedPreferences.Editor sped;
	/**
	 * 获取实例
	 * @return
	 */
	public static ServerManager getInstance() {
		if (instance == null) {
			sp = MainService.getInstance().getSharedPreferences("fcoltest", MainService.getInstance().MODE_PRIVATE);
			instance = new ServerManager();
		}
		return instance;
	}
	public String getIp() {
		String ip = sp.getString(Constants.ip,Constants.SERVER_IP);
		return ip;
	}

	public String getAddport() {
		String addport = sp.getString(Constants.addPort,Constants.SERVER_ADDPORT);
		return addport;
	}

	public String getPort() {
		String port = sp.getString(Constants.port,Constants.SERVER_PORT);
		return port;
	}

	public String getStreamname() {
		String streamname = sp.getString(Constants.name,Constants.STREAM_NAME);
		return streamname;
	}
	public int getMode(){
		int mode = sp.getInt(Constants.mode, Constants.MODE);
		return mode;
	}
	public String getURL() {
		String URL = sp.getString(Constants.URL,Constants.Default_URL);
		return URL;
	}

	public String getapp() {
		String APP = sp.getString(Constants.application,Constants.RTMP_APP);
		return APP;
	}

	public int getFramerate(){
		int framerate = Integer.parseInt(sp.getString(Constants.fps,String.valueOf(Constants.FRAMERATE)));
		return framerate;
	}

}
