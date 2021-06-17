package xyz.adspie.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

public class RWUtil
{
	public static final int DEFAULT_BUFFER_SIZE = 8192;

	public static final Charset U8 = Charset.forName("UTF-8");

	public static void simpleRW(InputStream is, OutputStream os) throws IOException
	{
		simpleRW(new byte[DEFAULT_BUFFER_SIZE], is, os);
	}

	public static void simpleRW(byte[] buf, InputStream is, OutputStream os) throws IOException
	{
		int i = is.read(buf);
		while (i > 0)
		{
			os.write(buf, 0, i);
			os.flush();
			i = is.read(buf);
		}
	}

	public static byte[] readData(InputStream is) throws IOException
	{
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream())
		{
			simpleRW(is, baos);
			return baos.toByteArray();
		}
	}

	public static String readString(InputStream is, Charset charset) throws IOException
	{
		return new String(readData(is), charset);
	}

	public static String readString(InputStream is, String chatsetName) throws IOException
	{
		return readString(is, Charset.forName(chatsetName));
	}

	public static String readUTF(InputStream is) throws IOException
	{
		return readString(is, U8);
	}

	public static String readAllText(String path) throws IOException
	{
		return readAllText(path, Charset.defaultCharset());
	}

	public static String readAllText(String path, String charsetName) throws IOException
	{
		return readAllText(path, Charset.forName(charsetName));
	}

	public static String readAllText(String path, Charset charset) throws IOException
	{
		return readAllText(new File(path), charset);
	}

	public static String readAllText(File f) throws IOException
	{
		return readAllText(f, Charset.defaultCharset());
	}

	public static String readAllText(File f, String charsetName) throws IOException
	{
		return readAllText(f, Charset.forName(charsetName));
	}

	public static String readAllText(File f, Charset charset) throws IOException
	{
		try (FileInputStream fis = new FileInputStream(f))
		{
			return readString(fis, charset);
		}
	}
}
