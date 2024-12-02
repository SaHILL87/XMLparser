package xmlparser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

public class Preprocessing{

    String input;
    String output;
    public Preprocessing(String input, String output){
        this.input = input;
        this.output = output;
    }

    public void converSelfClosingTags(){
        try {
            BufferedReader br = new BufferedReader(new FileReader(input));
            BufferedWriter bw = new BufferedWriter(new FileWriter(output));

            String line;
            while((line=br.readLine()) != null){
                String modifiedLine = convertSelfClosingTagInALine(line);
                bw.write(modifiedLine + "\n");
            }
            br.close();
            bw.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String convertSelfClosingTagInALine(String line) {
        StringBuilder outputLine = new StringBuilder();
        int index = 0; 
        int previousIndexToCopyFrom = 0;
    
        while ((index = line.indexOf("<", index)) != -1) {
            int startIndex = index;
            int endIndex = line.indexOf(">", index + 1);
    
            if (endIndex == -1) {
                throw new RuntimeException("Invalid XML: Missing '>' for a tag.");
            }
    
            String tag = line.substring(startIndex, endIndex + 1);
    
            if (isSelfClosingTag(tag)) {
                String tagContent = tag.substring(1, tag.length() - 2).trim();
                int tagNameEndIndex = tagContent.indexOf(' ') == -1 ? tagContent.length() : tagContent.indexOf(' ');
                String tagName = tagContent.substring(0, tagNameEndIndex);
    
                String startTag = "<" + tagName + ">";
                String endTag = "</" + tagName + ">";
                String replaceTag = startTag + endTag;
    
                outputLine.append(line.substring(previousIndexToCopyFrom, startIndex));
                outputLine.append(replaceTag);
                previousIndexToCopyFrom = endIndex + 1; // Update for next iteration
            }
    
            index = endIndex + 1;
        }
    
        outputLine.append(line.substring(previousIndexToCopyFrom));
    
        return outputLine.toString();
    }
    

    public boolean isSelfClosingTag(String tag){
        return tag.endsWith("/>");
    }
}
