package com.test2;

import java.util.Collection;
import java.util.Iterator;

import com.xuggle.xuggler.ICodec;
import com.xuggle.xuggler.IContainer;
import com.xuggle.xuggler.IContainerFormat;
import com.xuggle.xuggler.IPacket;
import com.xuggle.xuggler.IPixelFormat;
import com.xuggle.xuggler.IRational;
import com.xuggle.xuggler.IStream;
import com.xuggle.xuggler.IStreamCoder;
import com.xuggle.xuggler.IVideoPicture;

public class RTMPWriter {
	    // private static String url = "";
	    private String url = "rtsp://admin:admin123@192.168.124.69:37777/cam/realmonitor?channel=0&subtype=0";
	    private String appName = "";
	    private int height = 0;
	    private int width = 0;
	    private IStreamCoder coder = null;
	    private IContainer container = null;
	    private Boolean isfinish = false;
	 
	    //rtmp writer中需要获取rtsp流图像的宽和高，帧频作为编码参数
	    //rtmp服务器的地址格式一般为：ip+端口+地址+应用名称
	    @SuppressWarnings("deprecation")
	    public RTMPWriter(int height, int width, double frameRate, String appName) {
	        container = IContainer.make();
	        IContainerFormat containerFormat = IContainerFormat.make();
	        containerFormat.setOutputFormat("flv", url + appName, null);
	        // set the buffer length xuggle will suggest to ffmpeg for reading inputs
	        container.setInputBufferLength(0);
	        int retVal = container.open(url + appName, IContainer.Type.WRITE,
	                containerFormat);
	        if (retVal < 0) {
	            System.err.println("Could not open output container for live stream");
	        } else {
	            System.out.println("hava opened server " + url + appName + " for write!");
	        }
	        IStream stream = container.addNewStream(0);
	        coder = stream.getStreamCoder();
	        ICodec codec = ICodec.findEncodingCodec(ICodec.ID.CODEC_ID_H264);
	        if (codec == null) {
	        	System.out.println("cannot find h264 encoding codec!");
	            Collection<ICodec> icodec_collections = ICodec.getInstalledCodecs();
	            Iterator<ICodec> iterator = icodec_collections.iterator();
	            while (iterator.hasNext()) {
	                ICodec icodec = iterator.next();
	                System.out.println("Your system supports codec:" + icodec.getName());
	            }
	        } else {
	            coder.setCodec(codec);
	        }
	        coder.setNumPicturesInGroupOfPictures(5);
	        coder.setBitRate(256000);
	        coder.setPixelType(IPixelFormat.Type.YUV420P);
	        if (width == 0 || height == 0) {
	        	System.err.println("cannot get the real size of the stream needed to read");
	        }
	        coder.setHeight(height);
	        coder.setWidth(width);
	        coder.setFlag(IStreamCoder.Flags.FLAG_QSCALE, true);
	        coder.setGlobalQuality(0);
	        IRational rationalFrameRate = IRational.make(frameRate);
	        coder.setFrameRate(rationalFrameRate);
	        coder.setTimeBase(IRational.make(rationalFrameRate.getDenominator(),
	                rationalFrameRate.getNumerator()));
	        coder.open();
	        System.out.println("[ENCODER] address: " + url + appName + 
	                "\n video size is " + width + "x" + height 
	                + " and framerate is " + frameRate);
	        if (container.writeHeader() < 0) 
	          throw new RuntimeException("cannot write header");
	    }
	 
	    public void setFinish() {
	        this.isfinish = true;
	    }
	 
	    public void write(long frameNr, IVideoPicture picture) {
	        IPacket packet = IPacket.make();
	        if (frameNr == 0) {
	            // make first frame keyframe
	            picture.setKeyFrame(true);
	        }
	        picture.setQuality(0);
	        coder.encodeVideo(packet, picture, 0);
	        picture.delete();
	 
	        if (packet.isComplete()) {
	            if (container.writePacket(packet) < 0) {
	                throw new RuntimeException("cannot write packet");
	            } else {
	                System.out.println("[ENCODER] writing packet of size " + packet.getSize());
	            }  
	        }
	        if (isfinish) {
	            if (container.writeTrailer() < 0)
	                throw new RuntimeException("cannot write Trailer");
	            coder.close();
	            container.close();
	        } 
	    }
}
