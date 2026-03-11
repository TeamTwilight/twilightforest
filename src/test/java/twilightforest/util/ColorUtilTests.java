package twilightforest.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ColorUtilTests {

	@Test
	public void rgbToHSV() {
		float[] result = ColorUtil.rgbToHSV(0x77, 0xAA, 0xCC);

		assertNotNull(result);
		assertEquals(56, (int) (result[0] * 100));
		assertEquals(41, (int) (result[1] * 100));
		assertEquals(80, (int) (result[2] * 100));
	}

	@Test
	public void rgbToHSVWhite() {
		float[] result = ColorUtil.rgbToHSV(0xFF, 0xFF, 0xFF);

		assertNotNull(result);
		assertEquals(0, (int) (result[0] * 100));
		assertEquals(0, (int) (result[1] * 100));
		assertEquals(100, (int) (result[2] * 100));
	}

	@Test
	public void rgbToHSVBlack() {
		float[] result = ColorUtil.rgbToHSV(0x00, 0x00, 0x00);

		assertNotNull(result);
		assertEquals(0, (int) (result[0] * 100));
		assertEquals(0, (int) (result[1] * 100));
		assertEquals(0, (int) (result[2] * 100));
	}

	@Test
	public void rgbToHSVRed() {
		float[] result = ColorUtil.rgbToHSV(0xFF, 0x00, 0x00);

		assertNotNull(result);
		assertEquals(0, (int) (result[0] * 100));
		assertEquals(100, (int) (result[1] * 100));
		assertEquals(100, (int) (result[2] * 100));
	}

	@Test
	public void rgbToHSVGreen() {
		float[] result = ColorUtil.rgbToHSV(0x00, 0xFF, 0x00);

		assertNotNull(result);
		assertEquals(33, (int) (result[0] * 100));
		assertEquals(100, (int) (result[1] * 100));
		assertEquals(100, (int) (result[2] * 100));
	}

	@Test
	public void rgbToHSVBlue() {
		float[] result = ColorUtil.rgbToHSV(0x00, 0x00, 0xFF);

		assertNotNull(result);
		assertEquals(66, (int) (result[0] * 100));
		assertEquals(100, (int) (result[1] * 100));
		assertEquals(100, (int) (result[2] * 100));
	}

	@Test
	public void hsvToRGB() {
		int result = ColorUtil.hsvToRGB(204F / 360F, 0.417F, 0.80F);

		assertEquals(0x77AACC, result);
	}

	@Test
	public void hsvToRGBWhite() {
		int result = ColorUtil.hsvToRGB(0F, 0F, 1F);

		assertEquals(0xFFFFFF, result);
	}

	@Test
	public void hsvToRGBBlack() {
		int result = ColorUtil.hsvToRGB(0F, 0F, 0F);

		assertEquals(0x000000, result);
	}

	@Test
	public void hsvToRGBRed() {
		int result = ColorUtil.hsvToRGB(0F, 1F, 1F);

		assertEquals(0xFF0000, result);
	}

	@Test
	public void hsvToRGBGreen() {
		int result = ColorUtil.hsvToRGB(120F / 360F, 1F, 1F);

		assertEquals(0x00FF00, result);
	}

	@Test
	public void hsvToRGBBlue() {
		int result = ColorUtil.hsvToRGB(240F / 360F, 1F, 1F);

		assertEquals(0x0000FF, result);
	}

	@Test
	public void argbToABGR() {
		int result = ColorUtil.argbToABGR(0x11223344);

		assertEquals(0x11443322, result);
	}

}
