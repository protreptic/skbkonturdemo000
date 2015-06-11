package skbconturdemo;

import org.junit.Test;

public class SKBKonturDemoTest {
	
	public static final int RUNTIME_LIMIT = 1000 * 10; // Тест на время выполнения
	
	@Test(timeout = RUNTIME_LIMIT) 
	public void runtimeLimitTest() {
		SKBKonturDemo.main(new String[] { "test.in" }); 
	}
	
}
