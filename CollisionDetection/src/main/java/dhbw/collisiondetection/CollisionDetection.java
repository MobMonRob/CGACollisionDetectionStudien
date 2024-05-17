package dhbw.collisiondetection;

import de.orat.math.view.euclidview3d.GeometryView3d;
import de.orat.math.view.euclidview3d.GeometryViewCGA;
import java.awt.Component;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.BoxLayout;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import org.jzy3d.analysis.AnalysisLauncher;
import org.jzy3d.plot3d.primitives.EuclidRobot;
import org.jzy3d.plot3d.rendering.canvas.CanvasNewtAwt;

/**
 *
 * @author erika
 */
public class CollisionDetection extends GeometryViewCGA{
    
    public static void main(String[] args) throws Exception{
                
        CollisionDetection gv = new CollisionDetection();
        AnalysisLauncher.open(gv);
        
        
        rotateRobotsCoordsystem();
        setRobotsDH();
    
        gv.setUpRobotMovementUIWithSlidersWithCollisionDetection();
          
        //lineare Algebra:
        SphereTree spheretree = new SphereTree();
        spheretree.addtoGeometryView3d(gv);
        spheretree.distance();
        
        //CGA:
        //SphereTreeCGA st = new SphereTreeCGA();
        //st.addtoGeomeryView(gv);
        //st.spherehierarchie();
        
    }
    
    protected void setUpRobotMovementUIWithSlidersWithCollisionDetection(){
        CanvasNewtAwt c = (CanvasNewtAwt) chart.getCanvas();
        Component comp = c.getComponent(0);
        c.remove(comp);
        JPanel p = new JPanel();
        p.add(comp);
        p.setLayout(new BoxLayout(p, 1));
        Map<EuclidRobot, List<JSlider>> result = new HashMap<>();
       
        for(int j = 0; j < robotList.size(); j++){
            List<JSlider> sliders = new ArrayList<JSlider>();
            result.put(robotList.get(j), sliders);
            int number = j + 1;
            String string = "Robot Number " + number;
            JLabel label = new JLabel(string);
            p.add(label);
            
            List<Double> theta = new ArrayList<>();
            
            double[] gelenke = new double [6];
            for(int i = 1; i < 7; i++){
                JSlider slider = new JSlider();
                slider.setMaximum(360);
                slider.setMinimum(0);
                slider.setVisible(true);
                //FIX
                //slider.setValue((int) robotList.get(0).getDHs().get(i).getTheta());
                slider.setValue((int) robotList.get(j).getDHs().get(i).getTheta());
                
                //double thetaValue = robotList.get(j).getDHs().get(i).getTheta();
                //System.out.println(thetaValue);
                
                final int ix = i;
                final int jx = j;
                GeometryView3d g = this;
               
                slider.addChangeListener((ChangeEvent e) -> {
                    JSlider source = (JSlider)e.getSource();
                    //updateing chess floor after seting the Theta Values does not lead to tearing
                    robotList.get(jx).setTheta(ix, source.getValue(), false);
                    //updateChessFloor(true, CHESS_FLOOR_WIDTH);
                   
                    gelenke[ix] = robotList.get(jx).getDHs().get(ix).getTheta();
                    theta.clear();
                    
                    for (int k = 1; k < 6; k++){
                        theta.add(gelenke[k]);
                        //System.out.println("t"+theta);
                        
                    }
                });                   
                p.add(slider);
                sliders.add(slider);           
            }
        }
        
        p.setVisible(true);
        c.add(p);
        robotSliders = result;
    }
}
