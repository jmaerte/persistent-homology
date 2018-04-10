float[] x;
float[] y;
double scale = 100;

void setup() {
  String[] lines = loadStrings("C:/Users/Julian/IdeaProjects/persistent_homology/output/2018.04.08 - 12 57 35/pointset.txt");
  x = new float[lines.length];
  y = new float[lines.length];
  for(int i = 0; i < lines.length; i++) {
    String[] entries = lines[i].split(" ");
    x[i] = Float.valueOf(entries[0]);
    y[i] = Float.valueOf(entries[1]);
  }
  size(400,400);
}

void draw() {
  background(255);
  translate(width/2, height/2);
  line(0, -width/2, 0, width/2);
  line(-height/2, 0, height/2, 0);
  for(int i = 0; i < x.length; i++) {
    point(40 * x[i], 40 * y[i]);
  }
}