package com.voronov.api.ImageEngine;

import org.junit.Test;

import java.awt.*;
import java.io.IOException;

import java.net.URL;



public class ImageEngineTest {

    @Test
    public void main() throws IOException {

        ImageEngine img = new ImageEngine(new URL("https://pp.userapi.com/c846322/v846322871/14843e/2_r6riU4V7M.jpg"));

        img.setPositionText();

        img.addTextToImage("МОЙ ПЕРВЫЙ ТЕКСТ ИЗ ТЕСТА", 0, 1, img.CENTER_TEXT_MODE, "Arial", Font.ITALIC, img.SIZE_FONT_AUTO, Color.WHITE);

        img.saveAs("E:\\Maksim\\programming\\Java\\project\\ImageEngine\\src\\main\\resources\\Image\\sourceOut.png");

    }
}