import java.io.File;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.util.List;
import java.util.ArrayList;

public class ExtractKnowledge {
    
   public static record Block(String name, String xmi) {
   }
    
   public static record Port(String name, String xmi, String xmiOfOwner) {
   }

   public static record Association(String name, String xmi, String sourceName, 
         String destinationName, String xmiOfSource, String xmiOfDestination) {
   }

   public static record ClassifierRole(String name, String xmi, String type, 
         String xmiOfOwner, String propertyType, String reusesProperty) {
   }

   public static void main(String[] args) {

      try {
         File inputFile = new File("C:\\Users\\summe\\Functional Reasoning\\CoffeeMaker.xml");
         DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
         DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
         Document doc = dBuilder.parse(inputFile);
         doc.getDocumentElement().normalize();
         System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
         NodeList nList = doc.getElementsByTagName("XMI.documentation");
         System.out.printf ("----------------------------%n%n");
            
         // Creates an ArrayList of records to hold all the blocks and another one for the ports
         List<Block> allBlocks = new ArrayList<Block>();
         List<Port> allPorts = new ArrayList<Port>();
            
         // Selects only the UML:Class blocks nested within the UML:Namespace.ownedElement block
         NodeList namespaceList = doc.getElementsByTagName("UML:Namespace.ownedElement");
         Node namespaceNode = namespaceList.item(0);
         Element namespaceElement = (Element) namespaceNode;
         NodeList classList = namespaceElement.getElementsByTagName("UML:Class");
            
         // Iterates over each UML:Class tag
         for (int i = 0; i < classList.getLength(); i++) {
               
            Node classNode = classList.item(i);
            Element classElement = (Element) classNode;
                
            NodeList taggedValueList = classElement.getElementsByTagName("UML:TaggedValue");
                
            // Ignores the first UML:Class tag
            if (!(taggedValueList.item(0) == null)) {
                  
               // Checks if the current UML:Class tag is a block, port, or if theres an error
               Node taggedValueNode0 = taggedValueList.item(0);
               Element taggedValueElement0 = (Element) taggedValueNode0;
               String type = taggedValueElement0.getAttribute("value");
               
               // Adds a new block or port to the appropriate list
               if (type.equals("Class")) {
                  allBlocks.add(new Block (classElement.getAttribute("name"), 
                        classElement.getAttribute("xmi.id")));
               } else if (type.equals("Port")) {
                  Node taggedValueNode2 = taggedValueList.item(2);
                  Element taggedValueElement2 = (Element) taggedValueNode2;
                  allPorts.add(new Port (classElement.getAttribute("name"), 
                        classElement.getAttribute("xmi.id"),
                        taggedValueElement2.getAttribute("value")));
               } else {
                  System.out.printf ("Invalid UML:Class tag at %s%n", 
                        classElement.getAttribute("name"));
               }
            }
         }
         
         // Creates an ArrayList of records to hold all the associations
         List<Association> allAssociations = new ArrayList<Association>();
         
         NodeList associationList = namespaceElement.getElementsByTagName("UML:Association");
         
         // Iterates over each UML:Association tag
         for (int i = 0; i < associationList.getLength(); i++) {
            
            Node associationNode = associationList.item(i);
            Element associationElement = (Element) associationNode;
            
            NodeList taggedValueList = associationElement.getElementsByTagName("UML:TaggedValue");
            Node taggedValueNode2 = taggedValueList.item(2);
            Element taggedValueElement2 = (Element) taggedValueNode2;
            Node taggedValueNode3 = taggedValueList.item(3);
            Element taggedValueElement3 = (Element) taggedValueNode3;
            
            NodeList aEndList = associationElement.getElementsByTagName("UML:AssociationEnd");
            Node aEndNode0 = aEndList.item(0);
            Element aEndElement0 = (Element) aEndNode0;
            Node aEndNode1 = aEndList.item(1);
            Element aEndElement1 = (Element) aEndNode1;
            
            // Adds a new association to the list of all associations
            allAssociations.add(new Association (associationElement.getAttribute("name"), 
                  associationElement.getAttribute("xmi.id"), 
                  taggedValueElement2.getAttribute("value"),
                  taggedValueElement3.getAttribute("value"),
                  aEndElement0.getAttribute("type"),
                  aEndElement1.getAttribute("type")));
         }
         
         // Creates an ArrayList of records to old all the associations
         List<ClassifierRole> allRoles = new ArrayList<ClassifierRole>();
         
         NodeList roleList = namespaceElement.getElementsByTagName("UML:ClassifierRole");
         
         // Iterates over each UML:ClassifierRole
         for (int i = 0; i < roleList.getLength(); i++) {
            
            
            
         }

         System.out.println(allBlocks.get(0));
         System.out.println(allPorts.get(0));
         System.out.println(allAssociations.get(0));

      } catch (Exception e) {
         e.printStackTrace();
      }
   }
}
