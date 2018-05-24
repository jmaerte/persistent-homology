package com.jmaerte.visualization;

import com.jmaerte.data_struc.complex.Filtration;
import com.jmaerte.data_struc.complex.Tree;
import com.jmaerte.data_struc.point_set.Euclidean;
import com.jmaerte.data_struc.point_set.PointSet;
import com.jmaerte.util.log.Logger;
import com.jmaerte.util.vector.Vector2D;
import processing.core.PApplet;
import processing.core.PGraphics;

import java.text.DecimalFormat;

public class Visualization extends PApplet {

    private static DecimalFormat df = new DecimalFormat("#.##");
    public static double epsilon = 0, delta = -1;
    public static Vector2D<Integer, Integer> dimension = new Vector2D<>(500,500);
    public static Filtration f = null;
    public static PointSet<Euclidean> S;

    private double curr;
    private int last = 1;
    private float scaleX, scaleY;
    private PGraphics frame;
    private boolean pause;
    private boolean save;

    public void settings() {
        if(S.get(0).vector.length == 2) {
            size(dimension.getFirst(), dimension.getSecond());
            Tree t = f.get(0);
            double maxX = -1;
            double maxY = -1;
            for(int i = 0; i < f.vertexSize(); i++) {
                double[] v = S.get(t.getChild(i).node()).vector;
                if(Math.abs(v[0]) > maxX) maxX = Math.abs(v[0]);
                if(Math.abs(v[1]) > maxY) maxY = Math.abs(v[1]);
            }
            scaleX = (width - 50) / (float)maxX;
            scaleY = (height - 50) / (float)maxY;
        }else {
            size(dimension.getFirst(), dimension.getSecond(), P3D);
        }
    }

    public void setup() {
        curr = epsilon;
        if(S.get(0).vector.length == 2) {
            frame = createGraphics(2000, 2000);
            background(255);
            fill(0);
        }else {
            background(0);
        }
    }

    public void keyPressed() {
        if(key == ' ') {
            pause = !pause;
        }else if(key == 's') {
            pause = true;
            save = true;
        }else if(key == LEFT) {
            setup();
        }
    }

    public void draw() {
        if(S.get(0).vector.length == 2) {
            background(255);
            frame.beginDraw();
            frame.fill(0);
            frame.translate(width/2, height/2);
        }else {
            frame = super.g;
        }
        int i = last;
        Tree t;
        while(i < f.size() && (t = f.get(i)).val() < curr) {
            int[] vertex = new int[t.depth()];
            if(vertex.length <= 0) continue;
            int j = 0;
            while(t.depth() != 0) {
                vertex[j++] = t.node();
                t = t.getParent();
            }
            if(vertex.length == 1) {
                frame.fill(255, 0, 0);
                frame.ellipse((float)S.get(vertex[0]).vector[0] * scaleX / 2, (float)S.get(vertex[0]).vector[1] * scaleY / 2, 2, 2);
            }else {
                switch(vertex.length) {
                    case 2:
                        frame.stroke(0);
                        frame.strokeWeight(1);
                        frame.fill(0);
                        break;
                    case 3:
                        frame.strokeWeight(0);
                        frame.fill(0, 255, 0, 50);
                        break;
                    case 4:
                        frame.strokeWeight(0);
                        frame.fill(0, 0, 255, 50);
                        break;
                }
                frame.beginShape();
                for(j = 0; j < vertex.length; j++) {
                    double[] v = S.get(vertex[j]).vector;
                    if(S.get(0).vector.length == 2) {
                        frame.vertex((float)v[0] * scaleX / 2, (float)v[1] * scaleY / 2);
                    }else {
                        frame.vertex((float)v[0], (float)v[1], (float)v[2]);
                    }
                }
                frame.endShape();
            }
            i++;
        }
        if(S.get(0).vector.length == 2) {
            frame.endDraw();
        }
        last = i;
        image(frame, 0, 0);
        if(save) {
            saveFrame("output/" + Logger.dateFormat.format(Logger.date) + "/complex_eps-" + curr + ".jpg");
        }
        text("eps:" + df.format(curr), 10f, 10f);

        if(!pause) {
            curr = curr + 0.007;
        }
    }
}
