package xyz.adspie.util;

import java.nio.charset.Charset;

public class Base64Util
{
	private static final byte[] b64 = { 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84,
			85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113,
			114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 43, 47 };

	private static final byte[] rB64 = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 62, 0, 0, 0, 63, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61,
			0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23,
			24, 25, 0, 0, 0, 0, 0, 0, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45,
			46, 47, 48, 49, 50, 51 };

	private static final Charset c = Charset.forName("ASCII");

	public static String encode(byte[] data)
	{
		if (data == null || data.length < 1) return null;
		int l = data.length, i = l / 3, j = 0, d;
		if (l % 3 != 0) i++;
		byte[] result = new byte[i * 4];
		for (i = 0, l -= 3; i <= l;)
		{
			d = (data[i++] & 0xff) << 16 | (data[i++] & 0xff) << 8 | data[i++] & 0xff;
			result[j++] = b64[0x3f & d >>> 18];
			result[j++] = b64[0x3f & d >>> 12];
			result[j++] = b64[0x3f & d >>> 6];
			result[j++] = b64[0x3f & d];
		}
		if ((l -= i - 3) > 0)
		{
			d = (data[i++] & 0xff) << 16 | (l == 1 ? 0 : ((data[i] & 0xff) << 8));
			result[j++] = b64[0x3f & d >>> 18];
			result[j++] = b64[0x3f & d >>> 12];
			result[j++] = l == 1 ? 61 : b64[0x3f & d >>> 6];
			result[j++] = 61;
		}
		return new String(result, c);
	}

	public static byte[] decode(String dataStr)
	{
		byte[] data = dataStr.getBytes(c);
		if (data == null || data.length < 4 || data.length % 4 != 0) return null;
		int l = data.length - 4, i = data.length / 4 * 3, j = 0, d;

		if (data[l + 2] == 61) i--;
		if (data[l + 3] == 61) i--;
		byte[] result = new byte[i];
		for (i = 0, l = result.length - 3; j <= l;)
		{
			d = rB64[data[i++]] << 18 | rB64[data[i++]] << 12 | rB64[data[i++]] << 6 | rB64[data[i++]];
			result[j++] = (byte) (0xff & d >>> 16);
			result[j++] = (byte) (0xff & d >>> 8);
			result[j++] = (byte) (0xff & d);
		}
		if ((l -= j - 3) > 0)
		{
			result[j] = (byte) ((rB64[data[i++]] << 2) | (rB64[data[i]] >> 4));
			if (l > 1) result[j + 1] = (byte) (((rB64[data[i++]] & 0xf) << 4) | ((rB64[data[i]] & 0x3c) >> 2));
		}
		return result;
	}
}
