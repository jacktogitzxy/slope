/*  car eye 车辆管理平台 
 * car-eye管理平台   www.car-eye.cn
 * car-eye开源网址:  https://github.com/Car-eye-team
 * Copyright
 */
package org.push.push;



import java.nio.ByteBuffer;
import com.sh.camera.util.Constants;

import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.content.Context;
public class Pusher {
	private Context context;
	static {
		System.loadLibrary("rtmp");
	}

	public Pusher(Context context) {
		this.context = context;
	}

	/**
	 * 初始化
	 * @param serverIP   流媒体服务器IP
	 * @param serverPort 流媒体服务器端口
	 * @param streamName 流媒体文件名
	 * @param fps
	 * @param format
	 * @return
	 */
	private ByteBuffer directbuffer;
	
	private Handler handle =null;	
	public native int    CarEyeInitNetWorkRTMP(Context context,String key,String serverIP, String serverPort, String streamName, int videoformat, int fps,int audioformat, int audiochannel, int audiosamplerate);
	public native int    CarEyePusherIsReadyRTMP(int channel);
	public native long   CarEyeSendBufferRTMP(long time, byte[] data, int lenth, int type, int channel);	
	public native int    CarEyeStopNativeFileRTMP(int channel);	
	public native int    CarEyeStartNativeFileRTMPEX(Context context, String key, String serverIP, String serverPort, String streamName,  String fileName,int start, int end);

	// result： 0 文件传输结束  , 传输出错
	
	public void  CarEyeCallBack(int channel, int Result){
		Log.e("puser", "exit send file!");	
		if(handle != null){
			handle.sendMessage(handle.obtainMessage(1006));
		}else{
		}		
		Intent intent = new Intent("com.dss.camera.ACTION_END_VIDEO_PLAYBACK");
		intent.putExtra("EXTRA_ID", channel);
		context.sendBroadcast(intent);
	}	
	/**
	 * 发送H264编码格式
	 * @param data
	 * @param timestamp
	 * @param type
	 * @param index
	 * @return
	 */

	/**
	 * 停止发送
	 * @param index
	 */
	public native void  CarEyeStopPushNetRTMP(int index);   
	
	public  long SendBuffer_org(final byte[] data,final int length, final long timestamp, final int type, final int index)
	{
		long ret;
		//Log.e("puser", "timestamp:"+timestamp+"length:"+length);	
		ret =  CarEyeSendBufferRTMP(timestamp, data,length,type,index);		
		return ret;
		
	}	
	public  void stopPush(final int  index)
	{
		new Thread(new Runnable() {
			@Override
			public void run() {
				 CarEyeStopPushNetRTMP(index);
			}
		}).start();
	}
	/**
	 * 停止所有
	 */
	public void stop() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				stopPush(0);
				stopPush(1);
				stopPush(2);
				stopPush(3);
			}
		}).start();
	}	

	public int  startfilestream( final String serverIP, final String serverPort, final String streamName, final String fileName,final int splaysec,final int eplaysec,final Handler handler){

		//StartNativeFileRTSP(serverIP,serverPort,streamName, fileName);
		handle = handler;
		int channel =  CarEyeStartNativeFileRTMPEX(context, Constants.Key,serverIP,serverPort,streamName, fileName,splaysec,eplaysec);
		return channel;				
				
	}
	public void startfilestream(final String serverIP, final String serverPort, final String streamName, final String filePath){

		CarEyeStartNativeFileRTMPEX(context,Constants.Key,serverIP,serverPort,streamName,filePath,0,0);
		Log.e("puser", "exit send file!");
		//CameraUtil.stopVideoFileStream();
	}
	
}

