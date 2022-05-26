package cn.surveyking.server.core.uitls;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import lombok.SneakyThrows;

import javax.imageio.ImageIO;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author javahuang
 * @date 2022/5/25
 */
public class BarcodeReader {

	@SneakyThrows
	public static String readBarcode(InputStream inputStream) {
		Map<EncodeHintType, ErrorCorrectionLevel> hintMap = new HashMap<EncodeHintType, ErrorCorrectionLevel>();
		hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
		String charset = "UTF-8"; // or "ISO-8859-1"
		return readBarcode(inputStream, charset, hintMap);
	}

	/**
	 * @param inputStream
	 * @param charset
	 * @param hintMap
	 * @return Qr Code value
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws NotFoundException
	 */
	public static String readBarcode(InputStream inputStream, String charset, Map hintMap)
			throws FileNotFoundException, IOException, NotFoundException {
		BinaryBitmap binaryBitmap = new BinaryBitmap(
				new HybridBinarizer(new BufferedImageLuminanceSource(ImageIO.read(inputStream))));
		Result qrCodeResult = new MultiFormatReader().decode(binaryBitmap, hintMap);
		return qrCodeResult.getText();
	}

}
