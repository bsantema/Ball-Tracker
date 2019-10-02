package edu.iastate.bsantema.utils;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.IOException;

import org.opencv.core.Mat;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

public final class Utils
{
	public static void adjustRight() throws IOException
	{
		Runtime runtime = Runtime.getRuntime();
		String[] script = new String[] {"C:\\Program Files\\AutoHotkey\\AutoHotKey.exe", "C:\\Users\\txcyu\\Desktop\\AdjustRight.ahk"};
		Process p = runtime.exec(script);
		try
        {
            p.waitFor();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
	}
	
	public static void continueLeft() throws IOException
	{
		Runtime runtime = Runtime.getRuntime();
		String[] script = new String[] {"C:\\Program Files\\AutoHotkey\\AutoHotKey.exe", "C:\\Users\\txcyu\\Desktop\\ContinueLeft.ahk"};
		Process p = runtime.exec(script);
		try
        {
            p.waitFor();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
	}
	
	public static void haltAHK() throws IOException
	{
		Runtime runtime = Runtime.getRuntime();
		String[] script = new String[] {"C:\\Program Files\\AutoHotkey\\AutoHotKey.exe", "C:\\Users\\txcyu\\Desktop\\Halt.ahk"};
		Process p = runtime.exec(script);
		try
        {
            p.waitFor();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
	}
	
	public static void continueStraight() throws IOException
	{
		Runtime runtime = Runtime.getRuntime();
		String[] script = new String[] {"C:\\Program Files\\AutoHotkey\\AutoHotKey.exe", "C:\\Users\\txcyu\\Desktop\\ContinueStraight.ahk"};
		Process p = runtime.exec(script);
		try
        {
            p.waitFor();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
	}
	
	
	
	
	/**
	 * Scan a one-channel mat for any non-zero elements in the right half
	 */
	public static boolean scanRight(Mat m, int scanWidth)
	{
		boolean hasElements = false;
		for(int i = 0; i<m.rows(); i+=scanWidth)
			for(int j = m.cols()/2; j<m.cols(); j+=scanWidth)
				if(m.get(i, j)[0] > 0)
					hasElements = true;
		return hasElements;
	}
	
	public static boolean scanMid(Mat m, int scanWidth)
	{
		boolean hasElements = false;
		for(int i = 0; i<m.rows(); i+=scanWidth)
				if(m.get(i, m.cols()/2)[0] > 0)
					hasElements = true;
		return hasElements;
	}
	
	
	
	/**
	 * Convert a Mat object (OpenCV) in the corresponding Image for JavaFX
	 *
	 * @param frame
	 *            the {@link Mat} representing the current frame
	 * @return the {@link Image} to show
	 */
	public static Image mat2Image(Mat frame)
	{
		try
		{
			return SwingFXUtils.toFXImage(matToBufferedImage(frame), null);
		}
		catch (Exception e)
		{
			System.err.println("Cannot convert the Mat obejct: " + e);
			return null;
		}
	}
	
	/**
	 * Generic method for putting element running on a non-JavaFX thread on the
	 * JavaFX thread, to properly update the UI
	 * 
	 * @param property
	 *            a {@link ObjectProperty}
	 * @param value
	 *            the value to set for the given {@link ObjectProperty}
	 */
	public static <T> void onFXThread(final ObjectProperty<T> property, final T value)
	{
		Platform.runLater(() -> {
			property.set(value);
		});
	}
	
	/**
	 * Support for the {@link mat2image()} method
	 * 
	 * @param original
	 *            the {@link Mat} object in BGR or grayscale
	 * @return the corresponding {@link BufferedImage}
	 */
	private static BufferedImage matToBufferedImage(Mat original)
	{
		// init
		BufferedImage image = null;
		int width = original.width(), height = original.height(), channels = original.channels();
		byte[] sourcePixels = new byte[width * height * channels];
		original.get(0, 0, sourcePixels);
		
		if (original.channels() > 1)
		{
			image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
		}
		else
		{
			image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
		}
		final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
		System.arraycopy(sourcePixels, 0, targetPixels, 0, sourcePixels.length);
		
		return image;
	}
}
