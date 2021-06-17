package test;

import org.junit.Test;

import xyz.adspie.util.Authenticator;

public class MyTest
{
	@Test
	public void test1()
	{
		String key = Authenticator.generateSecretKey();
		System.out.println(key);
	}
	
	@Test
	public void test2() {
		Authenticator a=new Authenticator("BPG4UI5HPXPZ67AC");
		System.out.println(a.verify(537030));
	}
}
