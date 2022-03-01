package com.example.ipro497_group_i.ui.checkinout;

import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;

import static android.graphics.ImageFormat.YUV_420_888;
import static android.graphics.ImageFormat.YUV_422_888;
import static android.graphics.ImageFormat.YUV_444_888;

import android.widget.Toast;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.PlanarYUVLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.multi.qrcode.QRCodeMultiReader;

import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

public class QRScanner implements ImageAnalysis.Analyzer {

    private QRListener listener;

    public QRScanner(QRListener listener) {
        this.listener = listener;
    }

    @Override
    public void analyze(@NotNull ImageProxy img) {
        if (img.getFormat() == YUV_420_888 || img.getFormat() == YUV_422_888 || img.getFormat() == YUV_444_888) {
            ByteBuffer byteBuffer = img.getPlanes()[0].getBuffer();
            byte[] imageData = new byte[byteBuffer.capacity()];
            byteBuffer.get(imageData);

            PlanarYUVLuminanceSource source = new PlanarYUVLuminanceSource(
                    imageData,
                    img.getWidth(), img.getHeight(),
                    0, 0,
                    img.getWidth(), img.getHeight(),
                    false
            );

            BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(source));

            try {
                Result result = new QRCodeMultiReader().decode(binaryBitmap);
                System.out.println(result.getText());
                listener.onQRCodeFound(result.getText());
            } catch (FormatException | ChecksumException | NotFoundException e) {
                listener.qrCodeNotFound();
            }
        }

        img.close();
    }
}
