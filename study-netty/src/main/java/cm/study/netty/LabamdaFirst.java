package cm.study.netty;

import java.util.Arrays;
import java.util.List;

public class LabamdaFirst {

	public static void main(String[] args) {
		List<String> features = Arrays.asList("aaa", "bbbb", "ccccc");
		features.forEach(n -> System.out.println(n));
	}
}
