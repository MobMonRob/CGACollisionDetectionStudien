package dhbw.collisiondetection;

import de.orat.math.view.euclidview3d.GeometryView3d;
import static java.lang.Math.PI;
import java.util.ArrayList;
import org.jogamp.vecmath.Point3d;
import org.jogamp.vecmath.Vector3d;
import org.jzy3d.colors.Color;
import org.jogamp.vecmath.*;

/**
 *
 * @author erika
 */
public class SphereTree {
      
    Color translucent = new Color(255,255,255,0.3f);
      
    double[] d_m = new double[]{0d, 162.5, 0d, 0d, 133.3, 99.7, 99.6};
    double[] a_m = new double[]{0d, 0d,  -425, -392.2, 0d, 0d, 0d};
    double[] alpha_rad = new double[]{0d, PI/2d, 0d, 0d, PI/2d, -PI/2d, 0d};
    double[] theta_rad = new double[]{0, 0, 0, 0, 0, 0, 0};
    
    double r[] = new double[]{80, 90, 90, 63, 65, 90, 90, 65, 65, 65, 65, 70, 70, 50};
    ArrayList<Point3d> SphereCenter = new ArrayList<>();
    
    public void addtoGeometryView3d (GeometryView3d gv){
        
        Matrix4d Tbefore = new Matrix4d(1,0,0,0,0,1,0,0,0,0,1,0,0,0,0,1);
        
        for(int i = 0; i < 7; i++){
            
            //Translationsvektor aus den DH-Parametern:
            Vector3d t = new Vector3d();
            t.setZ(d_m[i]);
            t.setX(a_m[i]);
            
            //Rotationsmatrix um die X-Achse
            Matrix3d RotMatrix = new Matrix3d();
            RotMatrix.rotX(alpha_rad[i]); 
            
            //Rotationsmatrix um die Z-Achse
            Matrix3d RotZMatrix = new Matrix3d();
            if(theta_rad[i] != 0){
                RotZMatrix.rotZ(theta_rad[i]);
                RotMatrix.mul(RotMatrix, RotZMatrix);
            }
                      
            //Transformationsmatrix
            Matrix4d T = new Matrix4d(RotMatrix,t,1);
            T.mul(Tbefore,T);
            Tbefore.set(T);
            
            //Ortsvektor
            Point3d v = new Point3d(0,0,0);
            
            //Multiplikation
            T.transform(v);

            SphereCenter.add(v);
            
            if(i == 1){
                //Kugeln für Link upper_arm
                double zUpper_arm = 140;
                double[] xUpper_arm = new double[]{0, -125, -295, -425};
        
                for(int j=0;j<4;j++){
                    Point3d SphereCenterL = new Point3d(xUpper_arm[j],0, zUpper_arm);
                    T.transform(SphereCenterL,SphereCenterL);
                    SphereCenter.add(SphereCenterL);
                }
            }
        
            if(i == 2){
                //Kugeln für Link forearm
                double[] xForearm = new double[]{-100, -200, -300};
        
                for(int j=0;j<3;j++){
                    Point3d SphereCenterL = new Point3d(xForearm[j], 0, 0);
                    T.transform(SphereCenterL,SphereCenterL);
                    SphereCenter.add(SphereCenterL);
                }    
            }
        }
        
        for(int j=0;j<14;j++){
            gv.addSphere(SphereCenter.get(j), r[j], translucent, "");
        }
    }        
    
    public void distance(){
        for(int i = 0; i < 14; i++){
            for(int j = 0; j < 14; j++){
                if(j == i || j == (i-1)){
                    break;
                }
                double dx = Math.abs(SphereCenter.get(j).x-SphereCenter.get(i).x);
                double dy = Math.abs(SphereCenter.get(j).y-SphereCenter.get(i).y);
                double dz = Math.abs(SphereCenter.get(j).z-SphereCenter.get(i).z);
                
                Vector3d d = new Vector3d(dx,dy,dz);
            
                double distance;
                distance = d.length() - (r[j] + r[i]); 
                System.out.println("Der Abstand zwischen S" + j + " und S" + i + " beträgt " + distance);
            
                if(0 > distance){
                    System.out.println("Kollision findet statt!\n");
                }
            }
        }
    }
}
