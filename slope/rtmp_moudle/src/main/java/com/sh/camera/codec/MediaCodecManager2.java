/*  car eye 车辆管理平台 
 * car-eye管理平台   www.car-eye.cn
 * car-eye开源网址:  https://github.com/Car-eye-team
 * Copyright
 */
package com.sh.camera.codec;

import android.content.Context;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.media.MediaCodec;

import com.sh.camera.LiveActivity;
import com.sh.camera.audio.AudioStream;
import com.sh.camera.util.CameraFileUtil;
import com.sh.camera.util.Constants;

import org.push.hw.EncoderDebugger;
import org.push.hw.NV21Convertor;

import java.io.IOException;

/**
 *     
 * 项目名称：SH_CAMERA    
 * 类名称：MediaCodecManager    
 * 类描述：    
 * 创建人：Administrator    
 * 创建时间：2016年10月18日 上午11:56:29    
 * 修改人：Administrator    
 * 修改时间：2016年10月18日 上午11:56:29    
 * 修改备注：    
 * @version 1.0  
 *     
 */
public class MediaCodecManager2 {

	private static final String TAG = "MediaCodecManager";
	private static MediaCodecManager2 instance;
	public static NV21Convertor mConvertor;
	//解码
	private int previewFormat;
	public static MediaCodec[] mMediaCodec;
	public static boolean TakePicture = false;	
	private static boolean sw_codec	= false;
	private VideoConsumer[] mVC={null, null, null, null};	
	private static boolean RECODER_PUSHING	= true;
	public static int CAMERA_OPER_MODE = 1;
	private EncoderDebugger debugger;
	private AudioStream audioStream;
	private Context context;
	
	public static MediaCodecManager2 getInstance(Context context) {
		if (instance == null) {
			//mMediaCodec = new MediaCodec[Constants.MAX_NUM_OF_CAMERAS];			
			
			instance = new MediaCodecManager2(context);
		}
		return instance;
	}

	public MediaCodecManager2(Context context) {
		this.context = context;
	}

	/**
	 * 释放解码器资源
	 */
 public   void PrepareTakePicture()
 {
	 TakePicture = true;
 }
	
 
 public void StartUpload(int index, Camera camera)
 {   

	 debugger = EncoderDebugger.debug(context, Constants.UPLOAD_VIDEO_WIDTH, Constants.UPLOAD_VIDEO_HEIGHT);
	 previewFormat = sw_codec ? ImageFormat.YV12 : debugger.getNV21Convertor().getPlanar() ? ImageFormat.YV12 : ImageFormat.NV21;
     mVC[index] = new HWConsumer(context, LiveActivity.mPusher,index);
	 try {
		mVC[index].onVideoStart(Constants.UPLOAD_VIDEO_WIDTH, Constants.UPLOAD_VIDEO_HEIGHT);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	 if(Constants.AudioRecord)
	 {		 
		  audioStream = new AudioStream(LiveActivity.mPusher, null, index);
          audioStream.startRecord();
	 }	 
	
 } 
 
 public void StopUpload(int index)
 {
	 if(mVC[index]!= null)
	 {
		 mVC[index].onVideoStop();
		 mVC[index] =null;
		 
	 }	
	 if (audioStream != null) {
         audioStream.stop();
         audioStream = null;
     }
	 
 }

public void onPreviewFrameUpload(byte[] data,int index,Camera camera){			
	 if (data == null ) {
		 camera.addCallbackBuffer(data);
         return;
     }	
     Camera.Size previewSize = camera.getParameters().getPreviewSize();
     if (data.length != Constants.UPLOAD_VIDEO_HEIGHT * Constants.UPLOAD_VIDEO_WIDTH * 3 / 2) {
    	 camera.addCallbackBuffer(data);
    	 return;
     }   
     if(TakePicture )
     {    	
    	TakePicture = false;
		 CameraFileUtil.saveJpeg_snap(
		 			context,
					 index,
					 data,
					 Constants.UPLOAD_VIDEO_WIDTH,
					 Constants.UPLOAD_VIDEO_HEIGHT);
	 }
     if(mVC[index]!= null)
     {
      	 mVC[index].onVideo(data, previewFormat);
     }else
     {   	
    	 camera.setPreviewCallback(null);    
     }
     camera.addCallbackBuffer(data);      
 }

}
