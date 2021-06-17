package xyz.adspie.util;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class Authenticator
{
	private final Mac mac;

	private int offsetSize = 3;

	private final String key;

	public Authenticator(String key)
	{
		this.key = key;
		try
		{
			SecretKeySpec signKey = new SecretKeySpec(Base32Util.decode(key), "HmacSHA1");
			mac = Mac.getInstance("HmacSHA1");
			mac.init(signKey);
		}
		catch (Exception ex)
		{
			throw new RuntimeException("密码验证器初始化失败！");
		}
	}

	public Authenticator(String key, int offsetSize)
	{
		this(key);
		setOffsetSize(offsetSize);
	}

	public void setOffsetSize(int offsetSize)
	{
		if (offsetSize > 1 && offsetSize < 17) this.offsetSize = offsetSize;
	}

	public boolean verify(String code)
	{
		return verify(Long.valueOf(code));
	}

	public boolean verify(long code)
	{
		long t = (System.currentTimeMillis() / 1000L) / 30L;
		for (int i = -offsetSize; i <= offsetSize; i++)
		{
			byte[] data = new byte[8];
			long value = t + i;
			for (int j = 8; j-- > 0; value >>>= 8)
				data[j] = (byte) value;
			byte[] hash = mac.doFinal(data);
			int offset = hash[19] & 0xF;
			long truncatedHash = 0;
			for (int j = 0; j < 4; ++j)
			{
				truncatedHash <<= 8;
				truncatedHash |= (hash[offset + j] & 0xFF);
			}
			truncatedHash &= 0x7FFFFFFF;
			truncatedHash %= 1000000;
			if (truncatedHash == code) return true;
		}
		return false;
	}

	public String getKey()
	{
		return key;
	}

	public static Authenticator newInstance()
	{
		return new Authenticator(generateSecretKey());
	}

	public static String generateSecretKey()
	{
		try
		{
			return Base32Util.encode(SecureRandom.getInstance("SHA1PRNG").generateSeed(10));
		}
		catch (NoSuchAlgorithmException ex)
		{
			throw new RuntimeException(ex);
		}
	}
}