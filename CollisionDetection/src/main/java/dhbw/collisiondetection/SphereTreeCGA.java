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
import java.util.LinkedList;
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
    
    double r[] = new double[]{80, 90, 90, 65, 70, 70, 50};
    double rl[] = new double[]{90, 63, 65, 90, 65, 65, 65, 65};
          
    public void addtoGeomeryView (GeometryViewCGA gv){
        
        List<CGAMultivector> Kugeln = new ArrayList();
        LinkedList<CGAMultivector> KugelnLink = new LinkedList();       
        CGAMultivector e0 = CGAMultivector.createOrigin(1);
        CGARoundPointIPNS MS0 = new CGARoundPointIPNS(e0);
        Kugeln.add(MS0);
                        
        for(int i = 1; i < 7; i++){
            
            //Translator aus den DH-Parametern:
            Vector3d t = new Vector3d();
            t.setZ(d_m[i]);
            t.setX(a_m[i]);
            CGATranslator T = new CGATranslator(t);
                                                           
            //Rotor um die X-Achse   
            Point3d x = new Point3d(1,0,0);
            CGARoundPointIPNS Px = new CGARoundPointIPNS(x);
            CGALineOPNS xAe = new CGALineOPNS(MS0, Px);
            CGALineIPNS xAxis = xAe.dual();
            CGAEuclideanBivector xA = new CGAEuclideanBivector(xAxis);
            xA.normalize();
            CGARotor Rx = new CGARotor(xA, alpha_rad[0]);
            //System.out.println(Rx);
            
            //Rotor um die z-Achse   
            Point3d z = new Point3d(0,0,1);
            CGARoundPointIPNS Pz = new CGARoundPointIPNS(z);
            CGALineOPNS zAe = new CGALineOPNS(MS0, Pz);
            CGALineIPNS zAxis = zAe.dual();
            CGAEuclideanBivector zA = new CGAEuclideanBivector(zAxis);
            zA.normalize();
            CGARotor Rz = new CGARotor(zA, theta_rad[i]);
            
                                        
            //Transformation
            
            CGAMultivector test = Rx.transform(T.transform(Kugeln.get(i-1)));
            
            Kugeln.add(test);
                       
            
            
            if(i == 1){
                //Kugeln für Link upper_arm
                double[] zUpper_arm = new double[]{140,0,0,0};
                double[] xUpper_arm = new double[]{0, -125, -295, -425};
                KugelnLink.add(Kugeln.get(1));
                
                for(int j=0;j<4;j++){
                    Vector3d SphereCenterL = new Vector3d(xUpper_arm[j],0, zUpper_arm[j]);
                    CGATranslator TL = new CGATranslator(SphereCenterL);
                    
                    CGAMultivector k = TL.transform(KugelnLink.getLast());
                    KugelnLink.add(k);
                }
            }
            
            if(i == 2){
                //Kugeln für Link forearm
                double[] xForearm = new double[]{-100, -100, -100};
                KugelnLink.add(Kugeln.get(2));
        
                for(int j=0;j<3;j++){
                    Vector3d SphereCenterL = new Vector3d(xForearm[j],0, 0);
                    CGATranslator TL = new CGATranslator(SphereCenterL);
                    
                    CGAMultivector k = TL.transform(KugelnLink.get(j));
                    
                    KugelnLink.add(k);
                    
                }    
            }
        }
                
        
        for(int i = 0; i < 7; i++){
            Point3d SphereCenter = Kugeln.get(i).extractE3ToPoint3d();
            gv.addSphere(SphereCenter, r[i], translucent, "");
            Point3d SphereCenterL = KugelnLink.get(i).extractE3ToPoint3d();
            gv.addSphere(SphereCenterL, 70, translucent, "");
            
        }
        
           
    }
    
    //Hierarchieaufbau mit vorgefertigter Liste, da die Translation der Kugeln nicht funktioniert 
    public void spherehierarchie(/*hier müssten die Listen eigentlich übergeben werden*/){
        
        List<CGAMultivector> SphereCenter = new ArrayList();
        List<CGAMultivector> KugelnLink = new ArrayList();
                
        Point3d ursprung = new Point3d(0,0, 0);
        CGARoundPointIPNS S0 = new CGARoundPointIPNS(ursprung);
        SphereCenter.add(S0);
       
        Point3d p1 = new Point3d(0,0, 162.5);
        CGARoundPointIPNS S1 = new CGARoundPointIPNS(p1);
        SphereCenter.add(S1);
        
        Point3d l1 = new Point3d(0,140, 162.5);
        CGARoundPointIPNS L1 = new CGARoundPointIPNS(l1);
        KugelnLink.add(L1);
        
        Point3d l2 = new Point3d(-125,140, 162.5);
        CGARoundPointIPNS L2 = new CGARoundPointIPNS(l2);
        KugelnLink.add(L2);
        
        Point3d l3 = new Point3d(-295,140, 162.5);
        CGARoundPointIPNS L3 = new CGARoundPointIPNS(l3);
        KugelnLink.add(L3);
        
        Point3d l4 = new Point3d(-425,140, 162.5);
        CGARoundPointIPNS L4 = new CGARoundPointIPNS(l4);
        KugelnLink.add(L4);
        
        Point3d p2 = new Point3d(-425,0, 162.5);
        CGARoundPointIPNS S2 = new CGARoundPointIPNS(p2);
        SphereCenter.add(S2);
        
        Point3d l5 = new Point3d(-425,140, 162.5);
        CGARoundPointIPNS L5 = new CGARoundPointIPNS(l5);
        KugelnLink.add(L5);
        
        Point3d l6 = new Point3d(-425,140, 162.5);
        CGARoundPointIPNS L6 = new CGARoundPointIPNS(l6);
        KugelnLink.add(L6);
        
        Point3d l7 = new Point3d(-425,140, 162.5);
        CGARoundPointIPNS L7 = new CGARoundPointIPNS(l7);
        KugelnLink.add(L7);
        
        Point3d p3 = new Point3d(-817.2,0, 162.5);
        CGARoundPointIPNS S3 = new CGARoundPointIPNS(p3);
        SphereCenter.add(S3);
        
        Point3d p4 = new Point3d(-817.2,-133.3, 162.5);
        CGARoundPointIPNS S4 = new CGARoundPointIPNS(p4);
        SphereCenter.add(S4);
        
        Point3d p5 = new Point3d(-817.2,-133.3, 62.8);
        CGARoundPointIPNS S5 = new CGARoundPointIPNS(p5);
        SphereCenter.add(S5);
        
        Point3d p6 = new Point3d(-817.2,-232.9, 62.8);
        CGARoundPointIPNS S6 = new CGARoundPointIPNS(p6);
        SphereCenter.add(S6);
        
        //hier beginnt der Code ohne Workaround
          
        //Umwandlung der Multivektoren in Kugeln:
        List<CGAMultivector> Sphere = new ArrayList();
        for(int j = 0; j <7; j++){
            CGARoundPointIPNS s = new CGARoundPointIPNS(SphereCenter.get(j));
            s = s.normalize();
            CGASphereIPNS S = new CGASphereIPNS(s, r[j]);
            Sphere.add(S);
        } 
        
        List<CGAMultivector> SphereLink = new ArrayList();
        for(int j = 0; j <7; j++){
            CGARoundPointIPNS sl = new CGARoundPointIPNS(KugelnLink.get(j));
            sl = sl.normalize();
            CGASphereIPNS SL = new CGASphereIPNS(sl, rl[j]);
            SphereLink.add(SL);
        }
       
        List<CGAMultivector> BewegungsraumKugel = new ArrayList();
    
        CGARoundPointIPNS m4 = new CGARoundPointIPNS(SphereCenter.get(4));
        m4 = m4.normalize();
        CGASphereIPNS M4 = new CGASphereIPNS(m4, 145);
        BewegungsraumKugel.add(M4);
        
        CGARoundPointIPNS m5 = new CGARoundPointIPNS(SphereCenter.get(5));
        m5 = m5.normalize();
        CGASphereIPNS M5 = new CGASphereIPNS(m5, 110);
        BewegungsraumKugel.add(M5);
        
        //Kollisionsberechnung der kollisionsanfälligen Kugeln in der Baumhierarchie
        for(int j = 0; j <3; j++){
            distance(Sphere.get(j),Sphere.get(j+3));
            if(j==0){
                for(int i = 0; i <3; i++){
                    distance(Sphere.get(j),SphereLink.get(i+4));
                }
                for(int i = 0; i <3; i++){
                    if(distance(Sphere.get(j),BewegungsraumKugel.get(0))!= 0){
                        distance(Sphere.get(j),Sphere.get(j+3));
                    }
                }
            }
            if(j==1){
                for(int i = 0; i <3; i++){
                    if(distance(Sphere.get(j),BewegungsraumKugel.get(1))!= 0){
                        distance(Sphere.get(j),Sphere.get(5));
                        distance(Sphere.get(j),Sphere.get(6));
                    }
                }
            }
        }
        for(int j = 0; j <4; j++){
            distance(SphereLink.get(j),Sphere.get(5));
        }
        for(int j = 4; j <7; j++){
            distance(SphereLink.get(j),Sphere.get(6));
        }
    } 
    
    //Multivektoren müssen als Kugeln vorliegen
    public double distance(CGAMultivector M1, CGAMultivector M2){
        Point3d origin = new Point3d(0,0,0);        
        CGAMultivector M12 = M1.ip(M2);
        Point3d P = M12.extractE3ToPoint3d();
        double d = P.distance(origin);
        //System.out.println("Der Abstand zwischen S" + M1 + " und S" + M2 + " beträgt " + d);
        if(d < 0){
            //System.out.println("Kollision findet statt!\n");
        }      
        return d;               
    }
        
}
