package com.jmaerte.util.input.commands.collection;

import com.jmaerte.data_struc.complex.Filtration;
import com.jmaerte.persistence.Persistence;
import com.jmaerte.util.input.Input;
import com.jmaerte.util.input.Modifier;
import com.jmaerte.util.input.Register;
import com.jmaerte.util.input.commands.Command;
import com.jmaerte.util.vector.Vector2D;

public class Plot extends Command {

    public String call(String[] params) {
        try {
            Vector2D<Object, Class> v = Register.get(params[0]);
            if(v.getSecond().getSimpleName().equals("Filtration")) {
                Filtration f = (Filtration) v.getFirst();
                f.draw(0, f.get(f.size() - 1).val() + 1, 1000, Input.is(Modifier.BALLS));
                return "Plotted the Filtration";
            }else if(v.getSecond().getSimpleName().equals("Persistence")) {
                Persistence p = (Persistence) v.getFirst();
                String stK = Input.options.get("k");
                String stL = Input.options.get("l");
                int k;
                int l;
                if(stK == null) k = 0;
                else k = Integer.valueOf(stK);
                if(stL == null) l = p.dimension() - 1;
                else l = Integer.valueOf(stL);
                System.out.println(p.toBarcodePlot(k, l, false, false));
                return "Successfully calculated the barcode plot of " + params[0] + ":\n" + p.toBarcodePlot(k, l, false, false);
            }
        }catch(Exception e) {
            return "Fatal Error: " + e.getMessage();
        }finally {
            return "Couldn't determine what to draw. Probably " + params[0] + " is not a known object or either " + Input.options.get("k") + " or " + Input.options.get("k") + " is not a number.";
        }
    }

    protected String description() {
        return "Plots a given plotable Object [P], which is either one of the Type Filtration or Persistence. If you put in" +
                " a Persistence object, you can specify a type of plot, i.e. \"barcode\" or \"diagram\" and a range -k, -l in which to plot." +
                "I.e. a barcode plot of H_k to H_l. If you put in a Filtration it only gets plotted if the underlying PointSet is embedded in a" +
                " 2- or 3-dimensional euclidean space. The balls modifier enables drawing the balls in case of the cech filtration.";
    }

    protected String[] params() {
        return new String[]{"P"};
    }

    protected String[] options() {
        return new String[]{"k", "l", "type"};
    }

    protected Modifier[] modifiers() {
        return new Modifier[]{Modifier.BALLS};
    }

    protected String command() {
        return "plot";
    }

    protected int positionCommand() {
        return 0;
    }

}
