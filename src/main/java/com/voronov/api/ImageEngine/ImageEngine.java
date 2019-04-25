package com.voronov.api.ImageEngine;

import com.voronov.api.ImageEngine.core.Image;

import java.awt.*;
import java.io.IOException;
import java.net.URL;

public class ImageEngine implements  ImageEngineAPI {
    private Image image;

    public static String LEFT_TEXT_MODE;
    public static  String RIGHT_TEXT_MODE;
    public static  String CENTER_TEXT_MODE ;
    public static  String WIDTH_TEXT_MODE ;
    public static  int SIZE_FONT_AUTO ;

    private void initStaticVariable() {
        LEFT_TEXT_MODE=image.LEFT_TEXT_MODE;
        RIGHT_TEXT_MODE=image.RIGHT_TEXT_MODE;
        CENTER_TEXT_MODE=image.CENTER_TEXT_MODE;
        WIDTH_TEXT_MODE=image.WIDTH_TEXT_MODE;
        SIZE_FONT_AUTO=image.SIZE_FONT_AUTO;
    }

    public ImageEngine(URL url) throws IOException {
        image=new Image(url);
        initStaticVariable();
    }

    public ImageEngine() {
    }

    public void addTextToImage(String text, int zoneW, float alpha, String mode, String font, int type, int size, Color color) {
       image.addTextToImage(text, zoneW, alpha, mode, font,type,size,color);
    }

    public void setPositionText() {
        image.setPositionText();
    }

    public void saveAs(String fileName) {
        image.saveAs(fileName);
    }
}
