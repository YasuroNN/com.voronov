package com.voronov.api.ImageEngine;

import java.awt.*;

public interface ImageEngineAPI {
    public void addTextToImage (String text,
                                int zoneW, float alpha,
                                String mode, String font,
                                int type, int size, Color color);
    public void setPositionText();

    public void saveAs(String fileName);
}
