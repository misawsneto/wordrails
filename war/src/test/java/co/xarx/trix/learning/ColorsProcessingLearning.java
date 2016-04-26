package co.xarx.trix.learning;

import co.xarx.trix.util.Tinycolor;

public class ColorsProcessingLearning {

	public static void main(String[] agrs){
		Tinycolor color = new Tinycolor("#960064");
		System.out.println(color.getRGB());
		System.out.println(color.lighten(20).toString());
		System.out.println(color.darken(20).toString());
	}
}
