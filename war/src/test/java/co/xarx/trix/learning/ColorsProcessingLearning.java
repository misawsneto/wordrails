package co.xarx.trix.learning;

import co.xarx.trix.util.Tinycolor;

import java.util.HashMap;
import java.util.Map;

public class ColorsProcessingLearning {

	public static void main(String[] agrs){
		Tinycolor color = new Tinycolor("#272785");

		Map testPallete = new HashMap();

		testPallete.put("50", "#c7c7ee");
		testPallete.put("100", "#8c8cdd");
		testPallete.put("200", "#6060d0");
		testPallete.put("300", "#3535b4");
		testPallete.put("400", "#2e2e9d");
		testPallete.put("500", "#272785");
		testPallete.put("600", "#20206d");
		testPallete.put("700", "#191956");
		testPallete.put("800", "#12123e");
		testPallete.put("900", "#0b0b26");
		testPallete.put("A100", "#c7c7ee");
		testPallete.put("A200", "#8c8cdd");
		testPallete.put("A400", "#2e2e9d");
		testPallete.put("A700", "#191956");

		Map<String, String> pallete = new HashMap<>();
		pallete.put("500", color.toHexString());
		pallete.put("50", color.lighten(52).toHexString());
		pallete.put("100", color.lighten(37).toHexString());
		pallete.put("200", color.lighten(26).toHexString());
		pallete.put("300", color.lighten(12).toHexString());
		pallete.put("400", color.lighten(6).toHexString());
		pallete.put("600", color.darken(6).toHexString());
		pallete.put("700", color.darken(12).toHexString());
		pallete.put("800", color.darken(18).toHexString());
		pallete.put("900", color.darken(24).toHexString());
		pallete.put("A100", color.lighten(52).toHexString());
		pallete.put("A200", color.lighten(37).toHexString());
		pallete.put("A400", color.lighten(6).toHexString());
		pallete.put("A700", color.darken(12).toHexString());

		System.out.printf(pallete.toString());

		System.out.println(String.valueOf(pallete.equals(testPallete)));

		for(String k: pallete.keySet()){
			System.out.println(k + " " + pallete.get(k) + " " + testPallete.get(k));
		}
	}
}
