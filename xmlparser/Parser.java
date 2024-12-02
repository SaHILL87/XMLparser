package xmlparser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Stack;

public class Parser {
    private String fileName;

    public Parser(String fileName) {
        this.fileName = fileName;
    }

    public boolean parseXMLFile() {
        try {
            Stack<String> tagStack = new Stack<>();
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            String line;
            int lineNumber = 1;
            boolean isValid = true;

            while ((line = br.readLine()) != null) {
                int startIndex = 0;

                if(line.startsWith("<?xml")) {
                    lineNumber++;
                    continue;
                }

                while ((startIndex = line.indexOf("<", startIndex)) != -1) {
                    int endIndex = line.indexOf(">", startIndex + 1);

                    if (endIndex == -1) {
                        System.out.println("Error in line " + lineNumber + ": Unclosed tag.");
                        isValid = false;
                        break;
                    }

                    String tagElement = line.substring(startIndex + 1, endIndex).trim();

                    if (tagElement.startsWith("!--")) {
                        int commentEndIndex = line.indexOf("-->", startIndex + 1);
                        if (commentEndIndex == -1) {
                            System.out.println("Error in line " + lineNumber + ": Unclosed comment.");
                            isValid = false;
                        }
                        startIndex = commentEndIndex + 3;
                        continue;
                    }

                    if (tagElement.endsWith("/")) {
                        startIndex = endIndex + 1;
                        continue;
                    }

                    boolean isClosingTag = tagElement.startsWith("/");
                    String tagName = isClosingTag ? tagElement.substring(1).trim() : tagElement.split("\\s+")[0];

                    if (isClosingTag) {
                        if (tagStack.isEmpty() || !tagStack.peek().equals(tagName)) {
                            System.out.println("Error in line " + lineNumber + ": Closing tag </" + tagName + "> does not match opening tag <" + (tagStack.isEmpty() ? "none" : tagStack.peek()) + ">.");
                            isValid = false;
                        } else {
                            tagStack.pop();
                        }
                    } else {
                        tagStack.push(tagName); 
                    }

                    startIndex = endIndex + 1;
                }

                lineNumber++;
            }

            br.close();

            if (!tagStack.isEmpty()) {
                System.out.println("Error: Unmatched opening tags: " + tagStack);
                isValid = false;
            }

            return isValid;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
