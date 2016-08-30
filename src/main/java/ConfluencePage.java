import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConfluencePage
{
    public int id;
    public String type;
    public String title;
    public ConfluencePageVersion version;
    public ConfluencePageBody body;
    public ConfluenceSpace space;
    public List<ConfluencePage> ancestors;
}

