package com.rwin.tag.util.test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;

import com.rwin.tag.datastore.ArtPiece;
import com.rwin.tag.datastore.DataStore;
import com.rwin.tag.datastore.Marker;
import com.rwin.tag.datastore.User;

public class TestLoader {

    public static void CreateData() {
        Random rnd = new Random();

        ArrayList<User> list = new ArrayList<User>();

        for (int i = 0; i < 11; i++) {
            try {
                BufferedImage img = ImageIO.read(new File(
                        "/Users/erwinj/Downloads/tags/png/" + i + ".png"));
                ArtPiece a = ArtPiece.getTag(img);
                DataStore.getInstance().addArtPiece(a);
                User u = new User("rwin" + i, "foo!", a);
                DataStore.getInstance().addUser(u);
                list.add(u);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        int x = 41918;
        int y = 101318;

        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                User u = list.get(rnd.nextInt(list.size()));
                Marker t = new Marker(x + i, y + j, u.tag, u.name);
                DataStore.getInstance().addMarker(t);
            }
        }

    }
}
