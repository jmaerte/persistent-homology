package com.jmaerte.visualization;

import com.jmaerte.data_struc.complex.Filtration;
import com.jmaerte.data_struc.complex.Tree;
import com.jmaerte.data_struc.point_set.Euclidean;
import com.jmaerte.data_struc.point_set.PointSet;
import com.jmaerte.util.log.Logger;
import com.jmaerte.util.vector.Vector2D;
import peasy.PeasyCam;
import processing.core.PApplet;
import processing.core.PGraphics;

import java.text.DecimalFormat;

public class Visualization extends PApplet {

    private static DecimalFormat df = new DecimalFormat("#.##");
    public static double epsilon = 0, delta = -1;
    public static int dimension = 500;
    public static Filtration f = null;
    public static PointSet<Euclidean> S;
    public static boolean balls;

    private double curr;
    private int last = 1;
    private float scale;
    private PGraphics frame;
    private boolean pause = true;
    private boolean save;
    private PeasyCam pc;

    public void settings() {
        if(S.get(0).vector.length == 2) {
            size(dimension, dimension);
            Tree t = f.get(0);
            double maxX = -1;
            double maxY = -1;
            for(int i = 0; i < f.vertexSize(); i++) {
                double[] v = S.get(t.getChild(i).node()).vector;
                if(Math.abs(v[0]) > maxX) maxX = Math.abs(v[0]);
                if(Math.abs(v[1]) > maxY) maxY = Math.abs(v[1]);
            }
            double max = Math.max(maxX, maxY);
            scale = (dimension - 50) / (float)max;
            scale /= 3;
        }else {
            size(dimension, dimension, P3D);
        }
    }

    public void setup() {
        curr = epsilon;
        if(S.get(0).vector.length == 2) {
            frame = createGraphics(2000, 2000);
            frame.beginDraw();
            frame.endDraw();
            background(255);
            fill(0);
        }else {
            pc = new PeasyCam(this, 0, 0, 0, 100);
            pc.setMinimumDistance(0);
            pc.setMaximumDistance(500);
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
        int i;
        if(S.get(0).vector.length == 2) {
            frame.beginDraw();
            frame.fill(0);
            frame.translate(width/2, height/2);
            translate(width/2, height/2);
            i = last;
        }else {
            frame = super.g;
            i = 1;
        }
        background(255);
        Tree t;
        while(i < f.size() && (t = f.get(i)).val() <= curr) {
            int[] vertex = new int[t.depth()];
            if(vertex.length <= 0) continue;
            int j = 0;
            while(t.depth() != 0) {
                vertex[j++] = t.node();
                t = t.getParent();
            }
            if(vertex.length == 1) {
                if(S.get(0).vector.length == 2) {
                    frame.stroke(0);
                    frame.strokeWeight(1);
                    frame.point((float)S.get(vertex[0]).vector[0] * scale / 2, (float)S.get(vertex[0]).vector[1] * scale / 2);
                }else {
                    frame.point((float)S.get(vertex[0]).vector[0], (float)S.get(vertex[0]).vector[1], (float)S.get(vertex[0]).vector[2]);
                }
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
                if(vertex.length > 2) {
                    frame.beginShape();
                    for(j = 0; j < vertex.length; j++) {
                        double[] v = S.get(vertex[j]).vector;
                        if(S.get(0).vector.length == 2) {
                            frame.vertex((float)v[0] * scale / 2, (float)v[1] * scale / 2);
                        }else {
                            frame.vertex((float)v[0], (float)v[1], (float)v[2]);
                        }
                    }
                    frame.endShape();
                }else {
                    if(S.get(0).vector.length == 2) {
                        frame.line((float)S.get(vertex[0]).vector[0] * scale / 2, (float)S.get(vertex[0]).vector[1] * scale / 2,
                                (float)S.get(vertex[1]).vector[0] * scale / 2, (float)S.get(vertex[1]).vector[1] * scale / 2);
                    }else {
                        frame.line((float)S.get(vertex[0]).vector[0], (float)S.get(vertex[0]).vector[1], (float)S.get(vertex[0]).vector[2],
                                (float)S.get(vertex[1]).vector[0], (float)S.get(vertex[1]).vector[1], (float)S.get(vertex[1]).vector[2]);
                    }
                }

            }
            i++;
        }
        if(balls) {
            for(int l = 0; l < f.vertexSize(); l++) {
                fill(255, 0, 0, 15);
                stroke(0);
                strokeWeight(1);
                ellipse((float)S.get(l).vector[0] * scale / 2, (float)S.get(l).vector[1] * scale / 2,scale * (float)curr,scale * (float)curr);
            }
        }
        if(S.get(0).vector.length == 2) {
            frame.endDraw();
            image(frame, -width/2, -height/2);
            fill(0);
            text("eps:" + df.format(curr), 10f - width/2, 10f - height/2);
        }else {
            pc.beginHUD();
            fill(0);
            text("eps: " + curr, 10, 10);
            pc.endHUD();
        }
        last = i;
        if(save) {
            saveFrame("output/" + Logger.dateFormat.format(Logger.date) + "/complex_eps-" + curr + ".jpg");
            save = false;
        }
        if(!pause) {
            curr = curr + 0.007;
        }
    }
}
