package dhbw.collisiondetection;

import de.orat.math.cga.api.CGAMultivector;
import de.orat.math.view.euclidview3d.GeometryView3d;
import static java.lang.Math.PI;
import org.jzy3d.colors.Color;
import static de.orat.math.cga.api.CGASphereIPNS.*;
import de.orat.math.cga.spi.iCGAMultivector;
import org.jogamp.vecmath.*;

/**
 *
 * @author erika
 */
public class SphereTreeCGA {
    Color translucent = new Color(255,255,255,0.3f);
      
    double[] d_m = new double[]{0d, 162.5, 0d, 0d, 133.3, 99.7, 99.6};
    double[] a_m = new double[]{0d, 0d,  -425, -392.2, 0d, 0d, 0d};
    double[] alpha_rad = new double[]{0d, PI/2d, 0d, 0d, PI/2d, -PI/2d, 0d};
          
    public void addtoGeomeryView (GeometryView3d gv){
        CGAMultivector e0 =  CGAMultivector.createOrigin(PI);
        CGAMultivector einf =  CGAMultivector.createInf(PI);
        CGAMultivector e1 =  CGAMultivector.createEx(PI);
        CGAMultivector e2 =  CGAMultivector.createEx(PI);
        CGAMultivector e3 =  CGAMultivector.createEx(PI);
        Tuple3d t = new Tupel3d(e1,e2,e3);
        //CGAEuclideanVector k = CGAEuclideanVector(t);
        CGARoundPointIPNS S = new CGARoundPointIPNS(e0);
        CGASphereIPNS S0 = new CGASphereIPNS( S ,1);
       
    }
    
}
