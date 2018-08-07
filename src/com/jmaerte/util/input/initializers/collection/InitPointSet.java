package com.jmaerte.util.input.initializers.collection;

import com.jmaerte.data_struc.point_set.Metadata;
import com.jmaerte.data_struc.point_set.PointSet;
import com.jmaerte.data_struc.point_set.PointSetUtils;
import com.jmaerte.util.calc.Function;
import com.jmaerte.util.input.FileIO;
import com.jmaerte.util.input.Input;
import com.jmaerte.util.input.Modifier;
import com.jmaerte.util.input.Writer;
import com.jmaerte.util.input.initializers.Initializer;
import com.jmaerte.util.log.Logger;
import com.jmaerte.util.vector.Vector2D;
import com.jmaerte.util.vector.Vector3D;

public class InitPointSet extends Initializer {

    public Vector3D<String, Object, Class> init(String[] params) {
        if(Input.is(Modifier.MAPPING)) {
            Function<double[], double[]> mapping = null;
            double[] range = null;
            String description = "";
            int size = Integer.valueOf(params[1]);
            switch(params[0]) {
                case "Torus":
                    mapping = PointSetUtils.torusChart(5, 10);
                    range = new double[]{0, 2*Math.PI, 0, 2 * Math.PI};
                    description = "A " + size + " points sized sample of the Torus embedded in R^3 with parameters r = 5, R = 10.";
                    break;
                case "Swiss":
                    mapping = PointSetUtils.swissRollMapping();
                    range = new double[]{0, 16, 0, 16};
                    description = "A " + size + " points sized sample of the Swiss roll embedded in R^3.";
                    break;
                case "Klein3":
                    mapping = PointSetUtils.kleinBottle3Chart(3,4,2);
                    range = new double[]{0, 2*Math.PI, 0, 2 * Math.PI};
                    description = "A " + size + " points sized sample of the Klein bottle immersed in R^3 with parameters a = 3, b = 4, c = 2.";
                    break;
                case "Klein4":
                    mapping = PointSetUtils.kleinBottle4Chart(3,4,2);
                    range = new double[]{0, 2*Math.PI, 0, 2*Math.PI};
                    description = "A " + size + " points sized sample of the Torus embedded in R^4 with parameters a = 3, b = 4, c = 2.";
                    break;
            }
            return new Vector3D<>(description, PointSetUtils.getFromMapping(size, range, mapping), PointSet.class);
        }else if(Input.is(Modifier.CSV)) {
            String path;
            if(Input.is(Modifier.HOME)) {
                path = Input.HOME.getAbsolutePath() + params[0];
            }else {
                path = params[0];
            }
            PointSet S = null;
            char delimiter = ',';
            char next = '\n';
            char text = '\"';
            if(Input.options.get("delim") != null) {
                delimiter = Input.options.get("delim").charAt(0);
                if(Input.options.get("next").equals("\\n") || Input.options.get("next").equals("\\r\\n")) next = '\n';
            }
            if(Input.options.get("next") != null) {
                next = Input.options.get("next").charAt(0);
                if(Input.options.get("next").equals("\\n") || Input.options.get("next").equals("\\r\\n")) next = '\n';
            }
            if(Input.options.get("text") != null) {
                text = Input.options.get("text").charAt(0);
            }
            final String delim = ""+delimiter;
            final String ne = ""+next;

            String description = "";
            switch(params[1]) {
                case "euclidean":
                    S = FileIO.fromCSV(path, Double::valueOf,
                        list -> list.stream().mapToDouble(d -> d).toArray(), next, delimiter, text,
                        d -> Metadata.getEuclidean(d.length), d -> Writer.DoubleArray(""+delim, ""+ne));
                    description = "An euclidean PointSet of size " + S.size() + " load from " + path + ".";
                    break;
                case "levenshtein":
                    S = FileIO.fromCSV(path, x -> x, x -> x.get(0),
                            next, delimiter, text,
                            d -> Metadata.Levehnshtein, d -> Writer.String());
                    description = "An String-PointSet with Levenshtein-Metric of size " + S.size() + " load from " + path + ".";
                    break;
                default:
                    Logger.warn("Unknown option " + params[1] + ".");
            }
            return new Vector3D<>(description, S, PointSet.class);
        }
        return null;
    }

    public String description() {
        return "Initialize a point set from a pre-defined mapping or a csv-file.";
    }

    public String[] params() {
        return new String[]{"type"};
    }

    public String[] options() {
        return new String[]{"delim", "next", "text"};
    }

    public Modifier[] modifiers() {
        return new Modifier[]{Modifier.HOME, Modifier.CSV, Modifier.MAPPING};
    }

    public String command() {
        return "PointSet";
    }
}
