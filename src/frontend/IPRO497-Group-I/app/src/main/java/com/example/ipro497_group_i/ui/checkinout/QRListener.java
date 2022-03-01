package com.example.ipro497_group_i.ui.checkinout;

public interface QRListener {
    void onQRCodeFound(String qrc);
    void qrCodeNotFound();
}
