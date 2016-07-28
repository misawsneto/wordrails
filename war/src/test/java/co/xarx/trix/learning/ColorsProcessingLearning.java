package co.xarx.trix.learning;

import co.xarx.trix.util.Tinycolor;
import com.google.common.base.Joiner;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class ColorsProcessingLearning {

	public static void main(String[] agrs){
		Tinycolor color = new Tinycolor("#000000");

		Map<String, String> palette = new TreeMap<String, String>();
		palette.put("500", color.toHexString());
		palette.put("50", color.lighten(52).toHexString());
		palette.put("100", color.lighten(37).toHexString());
		palette.put("200", color.lighten(26).toHexString());
		palette.put("300", color.lighten(12).toHexString());
		palette.put("400", color.lighten(6).toHexString());
		palette.put("600", color.darken(6).toHexString());
		palette.put("700", color.darken(12).toHexString());
		palette.put("800", color.darken(18).toHexString());
		palette.put("900", color.darken(24).toHexString());
		palette.put("A100", color.lighten(52).toHexString());
		palette.put("A200", color.lighten(37).toHexString());
		palette.put("A400", color.lighten(6).toHexString());
		palette.put("A700", color.darken(12).toHexString());

		ArrayList<String> contrasts = new ArrayList<String>();

		for(Map.Entry<String, String> entry: palette.entrySet()){
			Tinycolor tc = new Tinycolor(entry.getValue());
			if(tc.isLight())
				contrasts.add(entry.getKey());
		}

		palette.put("contrastDarkColors", Joiner.on(" ").join(contrasts));
		palette.put("contrastDefaultColor", "light");

//		System.out.printf(palette.toString());

//		for(String k: palette.keySet()){
//			System.out.println(k + " " + palette.get(k));
//		}
	}
}
