package com.voronov.api.ImageEngine.core;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Image {
    public static final String LEFT_TEXT_MODE = "left-text-mode";
    public static final String RIGHT_TEXT_MODE = "right-text-mode";
    public static final String CENTER_TEXT_MODE = "center-text-mode";
    public static final String WIDTH_TEXT_MODE = "width-text-mode";

    public static final int SIZE_FONT_AUTO = -1;

    private BufferedImage bufferedImage;
    private String fileName;
    private int heightImage;
    private int widthImage;
    private String format;
    private int positionTextX;
    private int positionTextY;


    private void setSubstrate(int x, int y, int width, int height) {
        Graphics2D graphics = bufferedImage.createGraphics();

        graphics.setPaint(
                new Color(0.0f, 0.0f, 0.0f, 0.6f));
        graphics.fillRect(x, y, width, height);

    }

    public Image(File imageFile) {
        try {
            SimpleImageInfo imageInfo = new SimpleImageInfo(imageFile);
            this.format = imageInfo.getMimeType();
            heightImage = imageInfo.getHeight();
            widthImage = imageInfo.getWidth();
            bufferedImage = ImageIO.read(imageFile);
            fileName = imageFile.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
            bufferedImage = null;
            imageFile = null;
        }
    }

    public Image(String imageFilePath) {
        this(new File(imageFilePath));
    }

    public Image(URL url) throws IOException {
        SimpleImageInfo imageInfo = new SimpleImageInfo(url.openStream());
        format = imageInfo.getMimeType();
        heightImage = imageInfo.getHeight();
        widthImage = imageInfo.getWidth();
        bufferedImage = ImageIO.read(url);
        fileName = "source";
    }

    public BufferedImage getAsBufferedImage() {
        return bufferedImage;
    }

    public void saveAs(String fileName) {
        saveImage(new File(fileName));
        this.fileName = fileName;
    }

    public void save() {
        saveImage(new File(fileName));
    }

    private void saveImage(File file) {
        try {
            ImageIO.write(bufferedImage, getFileType(file), file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getFileType(File file) {
        String fileName = file.getName();
        int idx = fileName.lastIndexOf(".");
        if (idx == -1) {
            throw new RuntimeException("Invalid file name");
        }

        return fileName.substring(idx + 1);
    }

    public void setPositionText() {
        int x = (int) Math.round(this.getWidth() * 0.5);
        int y = (int) Math.round(this.getHeight() * 0.35);
        this.positionTextX = x;
        this.positionTextY = y;
    }

    public void addTextToImage(String text,
                               int zoneW, float alpha,
                               String mode, String font,
                               int type, int size, Color color) {

        int topX = this.positionTextX;
        int topY = this.positionTextY;
        this.addTextToImage(text, topX, topY, zoneW, alpha, mode, font, type, size, color);
    }


    public void addTextToImage(String text,
                               int topX, int topY,
                               int zoneW, float alpha,
                               String mode, String font,
                               int type, int size, Color color) {



        Graphics2D g = bufferedImage.createGraphics();
        g.setColor(Color.BLACK);
        g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);

        int countSymbol=50;
        while (true) {//цикл, чтобы количество сиволов в строке убавлял на 10
            if (countSymbol<=0) throw  new Error("quantity symbol  str = null, text the the bigest");
            Font f = Font.decode(font);//передаем по ссылке этот параметр, по этому он тоже меняется в функции getRectangleNeedSize
            java.util.List<String> strings = arrText(text, countSymbol);
            String maxStr = getMaxString(strings);
            Rectangle2D r2d = g.getFontMetrics(f).getStringBounds(maxStr, g);
            while (true) {
                int sizeFont = f.getSize();
                if (r2d.getWidth() < widthImage) sizeFont++;
                else sizeFont--;

                f = f.deriveFont((float) sizeFont);
                r2d = g.getFontMetrics(f).getStringBounds(maxStr, g);
                int deff = (int) (widthImage - r2d.getWidth());
                if (deff >= (int) (widthImage * 0.05) && deff < (int) (widthImage * 0.1)) break;

                if (deff<0) throw new Error("Skip if into method addTextToImage");


            }

            int x = (int) (topX - r2d.getWidth() / 2 - r2d.getWidth() * 0.1);
            int y = (int) (topY - r2d.getHeight() * 0.4);
            int width = (int) (r2d.getWidth() + r2d.getWidth() * 0.2);
            int height = (int) (r2d.getHeight() * strings.size() + r2d.getHeight() * 0.2);

            if (height > (int) (heightImage * 0.63)) {
                countSymbol-=10;
                continue;
            }
            setSubstrate(x, y, width, height);

            for (int i=0; i<strings.size(); i++) {
                addTextLineToImage(strings.get(i),
                        topX, (int) r2d.getHeight() + topY + i * (int) r2d.getHeight(),
                        zoneW, i == (strings.size() - 1),
                        alpha, mode, font, type, f.getSize(), color);
            }

            break;
        }

    }
    private java.util.List<String> arrText (String in, int countSymbol){
        java.util.List outList=new ArrayList();

        String[] inArr = in.split(" ");
        String elArr="";
        for (String s : inArr) {
            elArr+=s+" ";
            if (elArr.length()>=countSymbol) {
                elArr.trim();
                outList.add(elArr);
                outList.add("\n");
                elArr="";
            }
        }
        if (elArr.length()!=0){
            outList.add(elArr);
            outList.add("\n");
        }

        return outList;
    }
    private  String getMaxString(java.util.List<String> strings){
        String maxStr=strings.get(0);
        for (String string: strings)
            if (string.length()>maxStr.length())maxStr=string;
        return maxStr;
    }

    public void adTextToImage(String text,
                              int topX, int topY,
                              int zoneW, float alpha,
                              String mode, String font,
                              int type, int size, Color color) {
        Graphics2D g = bufferedImage.createGraphics();
        g.setColor(Color.BLACK);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        List lines = new ArrayList();
        int lineHeight = 0;


        if (size != SIZE_FONT_AUTO) {
            g.setFont(new Font(font, type, size));

            final FontMetrics fontMetrics = g.getFontMetrics();
            g.dispose();
            lineHeight = fontMetrics.getHeight();

            String[] words = text.split(" ");
            String line = "";
            for (int i = 0; i < words.length; i++) {
                if (fontMetrics.stringWidth(line + words[i]) > zoneW || words[i].indexOf("\n") != -1) {
                    lines.add(line);
                    line = "";
                }

                line += words[i] + " ";
            }

            lines.add(line);
        } else {
            String[] line = text.split("\n");
            int numLongestStr = 0;
            int largestString = line[0].length();
            for (int i = 1; i < line.length; i++) {
                if (line[i].length() > largestString) {
                    largestString = line[i].length();
                    numLongestStr = i;
                }
            }
            size = 9;
            int sizeStr;
            while (true) {
                System.out.println("keeek");
                g.setFont(new Font(font, type, size));
                FontMetrics fontMetrics = g.getFontMetrics();
                g.dispose();
                sizeStr = fontMetrics.stringWidth(line[numLongestStr]);
                if (sizeStr >= widthImage - (int) Math.round(widthImage * 0.2)) {
                    size -= 1;
                    g.setFont(new Font(font, type, size));
                    lineHeight = fontMetrics.getHeight();
                    lines = Arrays.asList(line);
                    break;
                } else size += 1;

            }
            int a=(int) Math.round(widthImage * 0.08);
            int b=topY - (int) Math.round(topY * 0.08);
            int c=sizeStr + (int) Math.round(sizeStr * 0.03);
            int d=lineHeight * (line.length + 1);
            setSubstrate(a, b, c, d);
        }


        for (int i = 0; i < lines.size(); i++) {
            addTextLineToImage((String) lines.get(i),
                    topX, lineHeight + topY + i * lineHeight,
                    zoneW, i == (lines.size() - 1),
                    alpha, mode, font, type, size, color);
            System.out.println("kek");
        }
    }

    private void addTextLineToImage(String text,
                                    int topX, int topY,
                                    int zoneW, boolean isLastLine,
                                    float alpha, String mode,
                                    String font, int type,
                                    int size, Color color) {
        String[] words = text.trim().split(" ");

        if (words.length == 0) {
            return;
        } else if (words.length == 1) {
            addTextToImage(text, topX, topY, alpha, font, type, size, color);
        } else {
            Graphics2D g = bufferedImage.createGraphics();
            g.setColor(Color.BLACK);
            g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g.setFont(new Font(font, type, size));

            final FontMetrics fontMetrics = g.getFontMetrics();
            g.dispose();

            if (mode == LEFT_TEXT_MODE) {
                addTextToImage(text, topX, topY, alpha, font, type, size, color);
            } else if (mode == CENTER_TEXT_MODE) {
                topX += (int) (zoneW - fontMetrics.stringWidth(text)) / 2;
                addTextToImage(text, topX, topY, alpha, font, type, size, color);
            } else if (mode == RIGHT_TEXT_MODE) {
                topX += zoneW - fontMetrics.stringWidth(text);
                addTextToImage(text, topX, topY, alpha, font, type, size, color);
            } else {
                int totalWordsWidth = 0;
                for (int i = 0; i < words.length; i++) {
                    totalWordsWidth += fontMetrics.stringWidth(words[i]);
                }

                int delta = Math.round((zoneW - totalWordsWidth) / (words.length - 1));
                int offset = 0;

                if (isLastLine) {
                    delta = Math.min(delta, 10);
                }

                for (int i = 0; i < words.length; i++) {
                    addTextToImage(words[i], topX + offset, topY, alpha, font, type, size, color);

                    offset += fontMetrics.stringWidth(words[i]) + delta;
                }
            }
        }
    }

    private void addTextToImage(String text,
                                int topX, int topY,
                                float alpha,
                                String font, int type,
                                int size, Color color) {
        Graphics2D g = bufferedImage.createGraphics();
        g.setColor(color);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha)); // 0.5 = 50% transparency
        g.setFont(new Font(font, type, size));

        g.drawString(text, topX, topY);
        g.dispose();

    }

    public int getWidth() {
        return widthImage;
    }

    public int getHeight() {
        return heightImage;
    }

    public void setWaterMark(String puth) {
        try {

//            ImageIO.read(new File(puth));
            BufferedImage bufferedImageWaterMark = resizeImage(ImageIO.read(new File(puth)), 200, 200);
            Graphics2D g = bufferedImage.createGraphics();
            g.drawImage(bufferedImageWaterMark, 0, 0, null);
            g.dispose();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private BufferedImage resizeImage(final java.awt.Image image, int width, int height) {
        int targetw = 0;
        int targeth = 75;

        if (width > height) targetw = 112;
        else targetw = 50;

        do {
            if (width > targetw) {
                width /= 2;
                if (width < targetw) width = targetw;
            }

            if (height > targeth) {
                height /= 2;
                if (height < targeth) height = targeth;
            }
        } while (width != targetw || height != targeth);

        final BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        final Graphics2D graphics2D = bufferedImage.createGraphics();
        graphics2D.setComposite(AlphaComposite.Src);
        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.drawImage(image, 0, 0, width, height, null);
        graphics2D.dispose();

        return bufferedImage;
    }

}
