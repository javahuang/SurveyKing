package cn.surveyking.server.core.uitls;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import lombok.SneakyThrows;
import net.coobird.thumbnailator.Thumbnails;

import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;

/**
 * @author javahuang
 * @date 2022/5/25
 */
public class BarcodeReader {

	@SneakyThrows
	public static String readBarcode(InputStream inputStream) {

		Map<DecodeHintType, Object> hintMap = new EnumMap<DecodeHintType, Object>(DecodeHintType.class);
		hintMap.put(DecodeHintType.CHARACTER_SET, "utf-8");
		hintMap.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
		hintMap.put(DecodeHintType.POSSIBLE_FORMATS, EnumSet.allOf(BarcodeFormat.class));

		Map<DecodeHintType, Object> hintMap_pure = new EnumMap<DecodeHintType, Object>(DecodeHintType.class);
		hintMap_pure.put(DecodeHintType.CHARACTER_SET, "utf-8");
		hintMap_pure.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
		hintMap_pure.put(DecodeHintType.PURE_BARCODE, Boolean.TRUE);
		return readBarcode(inputStream, hintMap, hintMap_pure);
	}

	/**
	 * @param inputStream
	 * @param hintMap
	 * @return Qr Code value
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws NotFoundException
	 */
	public static String readBarcode(InputStream inputStream, Map hintMap, Map hintMap_pure)
			throws FileNotFoundException, IOException, NotFoundException {
		BufferedImage image = Thumbnails.of(inputStream).size(1280, 960).asBufferedImage();
		BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(image)));
		Result qrCodeResult = new MultiFormatReader().decode(binaryBitmap, hintMap);
		if (qrCodeResult == null) {
			qrCodeResult = new MultiFormatReader().decode(binaryBitmap, hintMap_pure);
		}
		return qrCodeResult.getText();

	}

}
