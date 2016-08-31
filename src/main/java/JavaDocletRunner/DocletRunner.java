package JavaDocletRunner;
import com.sun.javadoc.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class DocletRunner {
    public static boolean start(RootDoc root) {
        ArrayList<String> alltags=new ArrayList<String>();
        String assertrequirementTag=  "assertrequirement";alltags.add(assertrequirementTag);
        String todoTag="todo";alltags.add(todoTag);
        String issueTag="issue";alltags.add(issueTag);
        String purposeTag="purpose";alltags.add(purposeTag);
        String worthnotingTag="worthnoting";alltags.add(worthnotingTag);
        String authorTag="author";alltags.add(authorTag);
        String dateTag="date";alltags.add(dateTag);
        String throwsTag="throws";alltags.add(throwsTag);
        String defunctTag="defunct";alltags.add(defunctTag);
        String paramTag="param";alltags.add(paramTag);
        String linkTag="link";alltags.add(linkTag);
        String coverageTag="coverage";alltags.add(coverageTag);
        writeContents(root.classes(), alltags);
        return true;
    }
    private static void writeContents(ClassDoc[] classes, ArrayList<String> alltags) {
        try {
            File file = new File("DocletDoc.txt");
            FileWriter fileWriter = new FileWriter(file);



        System.out.println("DONE");

        for (int i=0; i < classes.length; i++) {
            fileWriter.write("==++++++++++++==Class " + classes[i].toString()+"===" + classes[i].tags() +"====++++++++++++====\n");
            Tag[] classtag=classes[i].tags();
            for(int u=0;u<classtag.length;u++ ){
                System.out.println("      " + classtag[u].name() + ": "
                        + classtag[u].text());
            }
            MethodDoc[] methods = classes[i].methods();
            for (int j=0; j < methods.length; j++) {
                fileWriter.write("---++++++++++++-----Method:" + methods[j].name()+"----++++++++++++++----\n");
                for(String t:alltags) {
                    Tag[] tags = methods[j].tags(t);
                    if (tags.length > 0)
                    {
                        for (int k = 0; k < tags.length; k++)
                        {
                            fileWriter.write("      " + tags[k].name() + ": "
                                    + tags[k].text() + "\n");
                        }
                    }
                }
            }
        }
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
