import java.io.File;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

public class Reasoning {
    
   public static record Block(String name, String xmi) {
   }
    
   public static record Port(String name, String xmi, String xmiOfOwner, 
         String reusesProperty) {
   }

   public static record Association(String name, String xmi, String sourceName, 
         String destinationName, String xmiOfSource, String xmiOfDestination) {
   }

   public static record ClassifierRole(String name, String xmi, String type, 
         String xmiOfOwner, String propertyType, String reusesProperty) {
   }
   
   public static record Function(String name, String xmi, String xmiOfOwner) {
   }
   
   public static record Error(String type, String portName, String xmiOfPort, 
         String blockName, String xmiOfBlock) {
   }

   public static void main(String[] args) {

      try {
         
         // Sets up the file
         //File inputFile = new File("C:\\Users\\summe\\Functional Reasoning\\ActiveStandby_NLP.xml");
         //File inputFile = new File("C:\\Users\\summe\\Functional Reasoning\\CoffeeMaker_NLP.xml");
         //File inputFile = new File("C:\\Users\\summe\\Functional Reasoning\\FGS_NLP.xml");
         File inputFile = new File("C:\\Users\\summe\\Functional Reasoning\\HairDryer_NLP.xml");
         DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
         DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
         Document doc = dBuilder.parse(inputFile);
         doc.getDocumentElement().normalize();
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
                     
                  // Determines if the port has a reuses property
                  String reusesProperty;
                  if (taggedValueList.getLength() >= 5) {
                     Node taggedValueNode4 = taggedValueList.item(4);
                     Element taggedValueElement4 = (Element) taggedValueNode4;
                     reusesProperty = taggedValueElement4.getAttribute("value");
                  } else {
                     reusesProperty = "";
                  }
                  allPorts.add(new Port (classElement.getAttribute("name"), 
                        classElement.getAttribute("xmi.id"),
                        taggedValueElement2.getAttribute("value"), 
                        reusesProperty));
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
         
         // Creates an ArrayList of records to hold all the classifier roles
         List<ClassifierRole> allRoles = new ArrayList<ClassifierRole>();
         
         NodeList roleList = namespaceElement.getElementsByTagName("UML:ClassifierRole");
         
         // Iterates over each UML:ClassifierRole
         for (int i = 0; i < roleList.getLength(); i++) {
            
            Node roleNode = roleList.item(i);
            Element roleElement = (Element) roleNode;
            
            NodeList taggedValueList = roleElement.getElementsByTagName("UML:TaggedValue");
            
            Node taggedValueNode0 = taggedValueList.item(0);
            Element taggedValueElement0 = (Element) taggedValueNode0;
            Node taggedValueNode2 = taggedValueList.item(2);
            Element taggedValueElement2 = (Element) taggedValueNode2;
            Node taggedValueNode4 = taggedValueList.item(4);
            Element taggedValueElement4 = (Element) taggedValueNode4;
            
            // Determines if the classifier role has a reuses property
            String reusesProperty;
            if (taggedValueList.getLength() >= 6) {
               Node taggedValueNode5 = taggedValueList.item(5);
               Element taggedValueElement5 = (Element) taggedValueNode5;
               reusesProperty = taggedValueElement5.getAttribute("value");
            } else {
               reusesProperty = "";
            }
            
            // Adds a new classifier role to the list of all classifier roles
            allRoles.add(new ClassifierRole (roleElement.getAttribute("name"), 
                  roleElement.getAttribute("xmi.id"),
                  taggedValueElement0.getAttribute("value"),
                  taggedValueElement2.getAttribute("value"),
                  taggedValueElement4.getAttribute("value"),
                  reusesProperty));
            
         }
         
         // Creates an ArrayList of records to hold all the functions
         List<Function> allFunctions = new ArrayList<Function>();
         
         NodeList functionList = doc.getElementsByTagName("UML:TaggedValue");
         
         // Iterates over each UML:TaggedValue
         for (int i = 0; i < functionList.getLength(); i++) {
            
            Node functionNode = functionList.item(i);
            Element functionElement = (Element) functionNode;
            
            // Verifies the tag is a function
            String tag = functionElement.getAttribute("tag");
            if (tag.length() > 4) {
               if (tag.substring(0,4).equals("Func")) {
                  
                  // Adds a new function to the list of all functions
                  allFunctions.add(new Function (functionElement.getAttribute("value").trim(),
                        functionElement.getAttribute("xmi.id"),
                        functionElement.getAttribute("modelElement")));
               }
            }
         }     

         // Tests by printing the first element of each list
         /*System.out.println(allBlocks.get(0));
         System.out.println();
         System.out.println(allPorts.get(0));
         System.out.println();
         System.out.println(allAssociations.get(0));
         System.out.println();
         System.out.println(allRoles.get(0));
         System.out.println();
         System.out.println(allFunctions.get(0));*/
         

         // Begins validating the dangling node inspection
         
         // Creates an ArrayList to hold the complete list of errors and their types
         List<Error> allErrors = new ArrayList<Error>();
         
         // Creates two HashMaps to separately define the sources and destinations of each association
         HashMap<String, String> sources = new HashMap<String, String>();
         HashMap<String, String> destinations = new HashMap<String, String>();
         
         // Fills the maps with unique associations but not necessarily unique ports 
         for (Association association : allAssociations) {
           sources.put(association.xmi(), association.xmiOfSource());
           destinations.put(association.xmi(), association.xmiOfDestination());
         }
   
         // Validates that each port within an IBD is either the source or destination of an association
         for (Port port : allPorts) {
            if (!(port.reusesProperty().equals(""))) {
               if (!(sources.containsValue(port.xmi()) || destinations.containsValue(port.xmi()))) {
                  String ownerName = "";
                  // Finds the name of the owner's block
                  for (ClassifierRole role : allRoles) {
                     if (role.xmi().equals(port.xmiOfOwner())) {
                        ownerName = role.name();
                     }
                  }
                  allErrors.add(new Error ("Dangling Node", port.name(), port.xmi(), 
                        ownerName, port.xmiOfOwner()));
               }     
            }
         }

         // Prints the type of each error caught and its location
         for (Error error : allErrors) {
            System.out.printf ("%s%n%n", error);
         }
         System.out.println(allPorts.size());
         System.out.println(allErrors.size());
         

      } catch (Exception e) {
         e.printStackTrace();
      }
   }
   
   /*public static String getOwnerName (final String xmiOfOwner, final List list) {
      String ownerName = "";
      for (int i = 0; i < list.size(); i++) {
         ClassifierRole role = list.get(i);
         if (role.xmi().equals(xmiOfOwner)) {
            ownerName = role.name();
            return ownerName;
         }
      }
      return ownerName;
   }*/
}
