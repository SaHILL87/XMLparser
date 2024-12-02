package xmlparser;

import java.io.File;

public class Xml {
    public static void main(String[] args) {
        try{
            String input = "Fault.xml";
            String tempFile = "temp" + String.valueOf(System.currentTimeMillis()) + ".tmp"; // Create a temporary file name based on current time
            new Preprocessing(input, tempFile).converSelfClosingTags(); 
            Parser parser = new Parser(tempFile); 
            if(parser.parseXMLFile()){
                System.out.println("XML file parsed successfully.");
            }

            File file = new File(tempFile);
            if(file.delete()){
                System.out.println("Temporary file deleted successfully.");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
