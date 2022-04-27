package com.tambor.orm.utils;

import android.graphics.drawable.Drawable;

import java.io.InputStream;
import java.net.URL;


public class Imagem {
    public Drawable loadImageFromWeb(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            return d;
        } catch (Exception e) {
            System.out.println("Exc=" + e);
            return null;
        }
    }
}
