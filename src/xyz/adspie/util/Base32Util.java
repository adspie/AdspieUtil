package xyz.adspie.util;

import java.nio.charset.Charset;

public class Base32Util
{
	public static byte[] b32 = { 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86,
			87, 88, 89, 90, 50, 51, 52, 53, 54, 55 };

	private static byte[] rB32 = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 26, 27, 28, 29, 30, 31, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25 };

	private static final Charset c = Charset.forName("ASCII");

	public static String encode(byte[] data)
	{
		if (data == null || data.length < 1) return null;
		int l = data.length, i = l / 5, j = 0;
		byte d1;
		int d2;
		if (l % 5 != 0) i++;
		byte[] result = new byte[i * 8];
		for (i = 0, l -= 5; i <= l;)
		{
			d1 = data[i++];
			d2 = (data[i++] & 0xff) << 24 | (data[i++] & 0xff) << 16 | (data[i++] & 0xff) << 8 | data[i++] & 0xff;
			result[j++] = b32[d1 >>> 3];
			result[j++] = b32[((d1 & 0x7) << 2) | (d2 >>> 30)];
			result[j++] = b32[0x1f & d2 >>> 25];
			result[j++] = b32[0x1f & d2 >>> 20];
			result[j++] = b32[0x1f & d2 >>> 15];
			result[j++] = b32[0x1f & d2 >>> 10];
			result[j++] = b32[0x1f & d2 >>> 5];
			result[j++] = b32[0x1f & d2];
		}
		if ((l -= i - 5) > 0)
		{
			d1 = data[i++];
			d2 = (l < 2 ? 0 : ((data[i] & 0xff) << 24)) | (l < 3 ? 0 : ((data[i] & 0xff) << 16))
					| (l < 4 ? 0 : ((data[i] & 0xff) << 8));
			result[j++] = b32[d1 >>> 3];
			result[j++] = b32[((d1 & 0x7) << 2) | (d2 >>> 30)];
			result[j++] = l < 2 ? 61 : b32[0x1f & d2 >>> 25];
			result[j++] = l < 2 ? 61 : b32[0x1f & d2 >>> 20];

			result[j++] = l < 3 ? 61 : b32[0x1f & d2 >>> 15];
			result[j++] = l < 4 ? 61 : b32[0x1f & d2 >>> 10];
			result[j++] = l < 4 ? 61 : b32[0x1f & d2 >>> 5];
			result[j++] = 61;
		}
		return new String(result, c);
	}

	public static byte[] decode(String dataStr)
	{
		byte[] data = dataStr.getBytes(c);
		if (data == null || data.length < 8 || data.length % 8 != 0) return null;
		int l = data.length - 8, i = data.length / 8 * 5, j = 0;
		byte d1;
		int d2;

		if (data[l + 2] == 61) i--;
		if (data[l + 4] == 61) i--;
		if (data[l + 5] == 61) i--;
		if (data[l + 7] == 61) i--;
		byte[] result = new byte[i];
		for (i = 0, l = result.length - 5; j <= l;)
		{
			d1 = (byte) (rB32[data[i++]] << 3 | rB32[data[i]] >> 2);
			d2 = (rB32[data[i++]] & 0x3) << 30 | rB32[data[i++]] << 25 | rB32[data[i++]] << 20 | rB32[data[i++]] << 15
					| rB32[data[i++]] << 10 | rB32[data[i++]] << 5 | rB32[data[i++]];
			result[j++] = d1;
			result[j++] = (byte) (0xff & d2 >>> 24);
			result[j++] = (byte) (0xff & d2 >>> 16);
			result[j++] = (byte) (0xff & d2 >>> 8);
			result[j++] = (byte) (0xff & d2);
		}
		if ((l -= j - 5) > 0)
		{
			result[j++] = (byte) ((rB32[data[i++]] << 3) | (rB32[data[i]] >> 2));
			if (l > 1) result[j++] = (byte) ((rB32[data[i++]] & 0x3) << 6 | rB32[data[i++]] << 1 | rB32[data[i]] >>> 4);
			if (l > 2) result[j++] = (byte) (((rB32[data[i++]] & 0xf) << 4) | rB32[data[i]] >>> 1);
			if (l > 3) result[j] = (byte) (((rB32[data[i++]] & 0x1) << 7) | rB32[data[i++]] << 2 | rB32[data[i]] >>> 3);
		}
		return result;
	}
}
