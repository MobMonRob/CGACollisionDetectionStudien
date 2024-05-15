package dhbw.collisiondetection;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import java.util.ArrayList;

/**
 *
 * @author erika
 */
public class DomParser {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SAXException, IOException, ParserConfigurationException {
        File xmlFile = new File("C:\\Users\\erika\\Documents\\DHBW\\Studienarbeit\\CGA\\ur5e.urdf");

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = factory.newDocumentBuilder();
        Document doc = dBuilder.parse(xmlFile);

        doc.getDocumentElement().normalize();

        System.out.println("Root element: " + doc.getDocumentElement().getNodeName());

        NodeList jointTags = doc.getElementsByTagName("joint");
        NodeList linkTags = doc.getElementsByTagName("link");
        
        ArrayList<Double> d_m = new ArrayList<>();
        ArrayList<Double> a_m = new ArrayList<>();
        ArrayList<Double> alpha_rad = new ArrayList<>();
        
        for(int i = 0; i < linkTags.getLength(); i++){
            /*
            Node nNode = linkTags.item(i);
            
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                
                System.out.println("\nCurrent Element: ");
                
                Element elem = (Element) nNode;
                String name = elem.getAttribute("name");
                //String temp = elem.getAttributes().getNamedItem("name").getNodeValue();
                System.out.printf("link name: %s%n", name);
                
                if(elem.getElementsByTagName("inertial").item(0) != null){
                    Node node1 = elem.getElementsByTagName("inertial").item(0);
                    Element elInertial = (Element) node1;
                
                    Node node2 = elInertial.getElementsByTagName("mass").item(0);
                    Element elmass = (Element) node2;
                    String mass = elmass.getAttribute("value");
                    System.out.printf("mass value: %s%n", mass);
                    
                    Node node3 = elInertial.getElementsByTagName("origin").item(0);
                    Element elOrigin = (Element) node3;
                    String originrpy = elOrigin.getAttribute("rpy");
                    String originxyz = elOrigin.getAttribute("xyz");
                    System.out.printf("origin rpy: %s xyz: %s%n", originrpy, originxyz);
                    
                    Node node4 = elInertial.getElementsByTagName("inertia").item(0);
                    Element elinertia = (Element) node4;
                    String inertia = elinertia.getAttribute("ixx");
                    System.out.printf("inertia: %s%n", inertia);
                }
            }*/
        }

        for (int j = 0; j < (jointTags.getLength() - 9); j++) { 

            Node nNode = jointTags.item(j);

            System.out.println("\nCurrent Element: " + nNode.getNodeName());
            
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                Element elem = (Element) nNode;
                String name = elem.getAttribute("name");
                System.out.printf("joint name: %s%n", name);
                
                Node node1 = elem.getElementsByTagName("parent").item(0);
                Element elParent = (Element) node1;
                String parent = elParent.getAttribute("link");
                System.out.printf("parent: %s%n", parent);
                
                Node node2 = elem.getElementsByTagName("child").item(0);
                Element elChild = (Element) node2;
                String child = elChild.getAttribute("link");
                System.out.printf("child: %s%n", child);
                
                Node node3 = elem.getElementsByTagName("origin").item(0);
                Element elOrigin = (Element) node3;
                String originrpy = elOrigin.getAttribute("rpy");
                String originxyz = elOrigin.getAttribute("xyz");
                
                String[] split = originxyz.split(" ");
                double a = Double.parseDouble(split[1]);
                a_m.add(a);
                double d = Double.parseDouble(split[2]);
                d_m.add(d);
                
                String[] splitrpy = originrpy.split(" ");
                double alpha = Double.parseDouble(splitrpy[1]);
                alpha_rad.add(alpha);
                
                System.out.printf("origin rpy: %s xyz: %s%n", originrpy, originxyz);
                
                if (elem.getElementsByTagName("axis").item(0)!= null){
                    Node node4 = elem.getElementsByTagName("axis").item(0);
                    Element elAxis = (Element) node4;
                    String axis = elAxis.getAttribute("xyz");
                    System.out.printf("axis: %s%n", axis);
                }
            }
        } 
        System.out.println("\nArrayList d_m: " + d_m);
        System.out.println("ArrayList a_m: " + a_m);
        System.out.println("ArrayList alpha_rad: " + alpha_rad);
    }
    
}
