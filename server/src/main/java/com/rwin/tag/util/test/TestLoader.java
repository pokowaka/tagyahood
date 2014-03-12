package com.rwin.tag.util.test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;

import com.rwin.tag.datamodel.ArtPiece;
import com.rwin.tag.datamodel.Crew;
import com.rwin.tag.datamodel.Marker;
import com.rwin.tag.datamodel.User;
import com.rwin.tag.datastore.DataStore;
import com.rwin.tag.util.NetworkUtils;

public class TestLoader {

    public static void CreateData() {
        Random rnd = new Random();

        ArrayList<User> list = new ArrayList<User>();
        String host = NetworkUtils.getPublicIpAddress();
        int alpha = 0x80;
        Crew[] crew = new Crew[] { new Crew("Cookie"), new Crew("RockSteady") };
        for (int i = 0; i < 11; i++) {
            try {
                BufferedImage img = ImageIO.read(new File(
                        "/Users/erwinj/Downloads/tags/png/" + i + ".png"));
                ArtPiece a = ArtPiece.getTag(img);
                a.url = "http://" + host + ":8080/v1/art/" + a.id;
                int red = rnd.nextInt(255);
                int green = rnd.nextInt(255);
                int blue = rnd.nextInt(255);
                DataStore.getInstance().addArtPiece(a);
                User u = new User("rwin" + i, "foo!", crew[i % 2], a);
                u.color = (alpha << 24) | (red << 16) | (green << 8) | blue;
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
                Marker t = new Marker(u);
                t.setX(18, x + i);
                t.setY(18, y + j);
                DataStore.getInstance().addMarker(t);
            }
        }
    }
}
