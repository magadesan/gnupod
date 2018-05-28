import java.util.Hashtable;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;

class MediaFile {
	String title, album, path;
}
public class IPod {
   static Document doc;
   static Hashtable<String,MediaFile> myTable= new Hashtable<String, MediaFile>();

   static MediaFile getFileFromId(String id) {
	return (MediaFile) myTable.get(id);	
   }
   public static void main(String argv[]) {
 
      try {
         File inputFile = new File("../iPod_Control/.gnupod/GNUtunesDB.xml");
         DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
         DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
         doc = dBuilder.parse(inputFile);
         doc.getDocumentElement().normalize();

	//Load the Media Files
         NodeList nListFile = doc.getElementsByTagName("files");
         for (int temp = 0; temp < nListFile.getLength(); temp++) {
            Node nNodeFile = nListFile.item(temp);

            if (nNodeFile.getNodeType() == Node.ELEMENT_NODE) {
               Element eElement = (Element) nNodeFile;
	       NodeList fileNameList = eElement.getElementsByTagName("file");
               for (int count = 0; count < fileNameList.getLength(); count++) {
                  Node nodeFile = fileNameList.item(count);

                  if (nodeFile.getNodeType() == nodeFile.ELEMENT_NODE) {
                     Element file = (Element) nodeFile;
                     MediaFile mf = new MediaFile();
		     mf.title = file.getAttribute("title");
		     mf.album = file.getAttribute("album"); 
		     mf.path = file.getAttribute("path");
		     String fileId = file.getAttribute("id");
		     myTable.put(fileId,mf);
                  }
               }
            }
         }
	//Get PlayLists and te IDs
         NodeList nList = doc.getElementsByTagName("playlist");
         for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);
            
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
               Element eElement = (Element) nNode;
               System.out.println("mkdir \"" + eElement.getAttribute("name")+"\"");
               NodeList nameList = eElement.getElementsByTagName("add");
               for (int count = 0; count < nameList.getLength(); count++) {
                  Node nodeFile = nameList.item(count);
                  
                  if (nodeFile.getNodeType() == nodeFile.ELEMENT_NODE) {
                     Element id = (Element) nodeFile;
			MediaFile mfTmp =getFileFromId(id.getAttribute("id"));
			String[] fileSplit = mfTmp.path.split(":");
			String fileDotSplit = mfTmp.path.split("\\.")[1];
                     System.out.println("copy \"..\\iPod_Control\\" + fileSplit[2]+"\\"+fileSplit[3]+"\\"+fileSplit[4]+"\" \""+ eElement.getAttribute("name")+"\\"+mfTmp.title+"."+fileDotSplit+"\"");
                  }
               }
            }
         }
      } catch (Exception e) {
         e.printStackTrace();
      }
   }
}
