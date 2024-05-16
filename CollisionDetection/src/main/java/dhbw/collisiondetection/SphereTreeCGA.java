package dhbw.collisiondetection;

import de.orat.math.cga.api.CGAMultivector;
import de.orat.math.cga.api.CGARotor;
import de.orat.math.cga.api.CGAEuclideanBivector;
import de.orat.math.cga.api.CGAEuclideanVector;
import de.orat.math.cga.api.CGALineIPNS;
import de.orat.math.cga.api.CGARoundPointIPNS;
import de.orat.math.cga.api.CGASphereIPNS;
import de.orat.math.cga.api.CGASphereOPNS;
import de.orat.math.cga.api.CGALineOPNS;
import de.orat.math.cga.api.CGAMotor;
import de.orat.math.cga.api.CGAPointPairIPNS;
import de.orat.math.cga.api.CGATranslator;
import de.orat.math.view.euclidview3d.GeometryViewCGA;
import static java.lang.Math.PI;
import org.jzy3d.colors.Color;
import dhbw.collisiondetection.SphereTree;
import de.orat.math.cga.spi.iCGAMultivector;
import java.util.ArrayList;
import java.util.List;
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
    double[] theta_rad = new double[]{0d, 0, 0d, 0d, 0d, 0d, 0d};
    
    double r[] = new double[]{90, 90, 90, 65, 70, 70, 50};
          
    public void addtoGeomeryView (GeometryViewCGA gv){
        List<CGAMultivector> Kugeln = new ArrayList();
        
        CGAMultivector einf = CGAMultivector.createInf(1);        
        CGAMultivector e0 = CGAMultivector.createOrigin(1);
        CGARoundPointIPNS MS0 = new CGARoundPointIPNS(e0);
        MS0 = MS0.normalize();
        //CGASphereIPNS S0 = new CGASphereIPNS(MS0, 90);   
          
        CGAMultivector SphereCenterCGA = MS0;
        Kugeln.add(MS0);
                
        for(int i = 1; i < 7; i++){
            
            //Translationsvektor aus den DH-Parametern:
            CGAMultivector tx = CGAMultivector.createEx(a_m[i]);
            CGAMultivector tz = CGAMultivector.createEz(d_m[i]);
            CGATranslator t = new CGATranslator(tx.add(tz));
                                   
            //Rotor um die X-Achse   
            Point3d ur = new Point3d(0,0,0);
            Point3d Px = new Point3d(1,0,0);
            CGARoundPointIPNS x = new CGARoundPointIPNS(Px);
            System.out.println(x);
            //CGAMultivector xAxis = e0.commutation(x.commutation(einf));
            
            //CGAPointPairIPNS pp = new CGAPointPairIPNS(x,MS0);
            //CGALineIPNS xAe = new CGALineIPNS(pp);
            CGALineOPNS xAe = new CGALineOPNS(ur, Px);
            xAe.dual();
            
            //xAxis = xAxis.normalize();
            System.out.println(xAe);
            
            CGAEuclideanBivector xAx = new CGAEuclideanBivector(xAe);
            xAx.normalize();
            CGARotor R = new CGARotor(xAx, alpha_rad[i]);
            System.out.println(R);
                
            //Transformation
            CGAMotor M = new CGAMotor(R, t);
            System.out.println(M);
            
            CGAMultivector test = (M.ip(Kugeln.get(i-1).ip(M.reverse())));
            System.out.println(test);
                       
            Kugeln.add(test);
            System.out.println("Das sind die Kugelmittelpunkte: "+Kugeln);
            
            
            Point3d SphereCenter = SphereCenterCGA.extractE3ToPoint3d();
            gv.addSphere(SphereCenter, r[i], translucent, "");
                 
        }
    }
        
        /*
        List<Point3d> Kugeln = new ArrayList();
        
        Point3d ursprung = new Point3d(0,0, 0);
        CGASphereOPNS S = new CGASphereOPNS(ursprung, r[0]);
        Point3d pp = S.extractE3ToPoint3d();
        Kugeln.add(ursprung);
       
        Point3d p1 = new Point3d(0,0, 162.5);
        CGASphereOPNS S1 = new CGASphereOPNS(p1, r[1]);
        Point3d pp1 = S1.extractE3ToPoint3d();
        Kugeln.add(p1);
        
        Point3d p2 = new Point3d(-425,0, 162.5);
        CGASphereOPNS S2 = new CGASphereOPNS(p2, r[2]);
        Point3d pp2 = S2.extractE3ToPoint3d();
        Kugeln.add(p2);
        
        Point3d p3 = new Point3d(-817.2,0, 162.5);
        CGASphereOPNS S3 = new CGASphereOPNS(p3, r[3]);
        Point3d pp3 = S3.extractE3ToPoint3d();
        Kugeln.add(p3);
        
        Point3d p4 = new Point3d(-817.2,-133.3, 162.5);
        CGASphereOPNS S4 = new CGASphereOPNS(p4, r[4]);
        Point3d pp4 = S4.extractE3ToPoint3d();
        Kugeln.add(p4);
        
        Point3d p5 = new Point3d(-817.2,-133.3, 62.8);
        CGASphereOPNS S5 = new CGASphereOPNS(p5, r[5]);
        Point3d pp5 = S5.extractE3ToPoint3d();
        Kugeln.add(p5);
        
        Point3d p6 = new Point3d(-817.2,-232.9, 62.8);
        CGASphereOPNS S6 = new CGASphereOPNS(p6, r[6]);
        Point3d pp6 = S6.extractE3ToPoint3d();
        Kugeln.add(p6);
    }
       /* 
    public void spherehierarchie(){
        SphereTree spheretree = new SphereTree();
        List<Point3d> SphereCenter = spheretree.rSphere();
        List<CGASphereOPNS> ApproximierteKugeln = new ArrayList();
        
        double newr = r[14];
        for(int j = 14; j > 0; j--){
            newr = newr + r[j]/*+ länge vom link*/;
           
    /*        CGASphereOPNS M = new CGASphereOPNS(SphereCenter.get(j), newr);
            
            
            ApproximierteKugeln.add(M);
        }
        System.out.println(ApproximierteKugeln);
    }    
        //Distanzberechnung
                
        Vector3d xAchse = new Vector3d(1,0, 0);
        Vector3d yAchse = new Vector3d(0,1, 0);
        Vector3d zAchse = new Vector3d(0,0, 1);
        
        CGAMultivector e0 = CGAMultivector.createOrigin(1);
        CGASphereIPNS S0 = new CGASphereIPNS(e0);
        Point3d s = S0.extractE3ToPoint3d();  
        gv.addSphere(s, r[0], translucent, "");
            
        
        //CGASphereOPNS S = new CGASphereOPNS(e0);
        
        for(int i = 1; i < 7; i++){
            
            //Translationsvektor aus den DH-Parametern:
            Vector3d p = new Vector3d(a_m[i],0,d_m[i]);
            CGATranslator t = new CGATranslator(p);
            
                        
            //Rotationsmatrix um die X-Achse
            //CGAPlaneOPNS xebene = new CGAPlaneOPNS(ursprung,xAchse,yAchse);
            //CGAPlaneOPNS yebene = new CGAPlaneOPNS(ursprung,yAchse,zAchse);
            //CGAPlaneOPNS zebene = new CGAPlaneOPNS(ursprung,xAchse,zAchse);
            
            CGALineOPNS xA = new CGALineOPNS(ursprung,xAchse);
            CGAEuclideanBivector rr = new CGAEuclideanBivector(xA);
            CGARotor R = new CGARotor(rr, alpha_rad[i]);
               
            //Transformation
            
            CGAMotor M = new CGAMotor(R, t);
            System.out.println(M);
            CGAMotor MR;
            
            CGASphereOPNS S = new CGASphereOPNS(ursprung, r[i]);
     
        }
        */
        
    
    
}
