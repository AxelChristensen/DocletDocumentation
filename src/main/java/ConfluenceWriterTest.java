


import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.util.Assert;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import sun.applet.AppletClassLoader;
import sun.misc.BASE64Encoder;
import org.springframework.util.MultiValueMap;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
//import com.idexx.labstation.confluence.domain.ConfluencePage;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;


//import static com.idexx.labstation.confluence.ConfluenceClient.findUniquePageByName;
//import static com.idexx.labstation.confluence.ConfluenceClient.updatePageContent;
/**
 * See
 * https://developer.atlassian.com/confdev/confluence-rest-api/confluence-rest-api-examples
 * https://docs.atlassian.com/atlassian-confluence/REST/latest/
 */
public class ConfluenceWriterTest
{
    protected static Element pageRootElement;
    protected static Properties properties;
    public static void main(String[] Args){
        System.out.println("Hello");
       loadProperties("confluence.properties");



        pageRootElement = new Element(Tag.valueOf("root"), "");
        // Append table of content
        pageRootElement.appendElement("ac:structured-macro").attr("ac:name", "toc");
        ConfluencePage cp= findUniquePageByName("Axels Test Page");
        System.out.println("HAVE " + cp.title + " " + cp);
        updatePageContent("Axels Test Page","Now new!","Something else");

    }
    protected static void loadProperties(String name)
    {
        properties = new Properties();
        try
        {
            URL o= ConfluenceWriterTest.class.getClassLoader().getResource(name);
            System.out.println("O " + o);
            properties.load(ConfluenceWriterTest.class.getClassLoader().getResourceAsStream(name));
        }
        catch (IOException e)
        {

        }
    }
    private static final RestTemplate REST = new RestTemplate();

    public static ConfluencePage findUniquePageByName(String name)
    {
        ConfluenceSearchResults searchResults = findPageByName(name);
        // Assert.isTrue(searchResults.size == 1, "Unique page expected");
        return searchResults.results.get(0);
    }

    public static boolean pageExists(String name)
    {
        return findPageByName(name).size > 0;
    }

    private static ConfluenceSearchResults findPageByName(String name)
    {ResponseEntity<ConfluenceSearchResults> exchange=null;
        try
        {
            String uri = getUriComponentsBuilder()
                    .queryParam("title", name)
                    .build().toUriString();

            exchange = REST.exchange(uri, HttpMethod.GET, new HttpEntity<>(getAuthHeaders()), ConfluenceSearchResults.class);
            //   Assert.isTrue(exchange.getStatusCode().series() == HttpStatus.Series.SUCCESSFUL, "Can't execute search");
            System.out.println("GOT IT");
        }
        catch (Exception ex){}


        return exchange.getBody();
    }

    public static ConfluencePage createChildPage(String parentPageTitle, String title, String content)
    {
        String uri = getUriComponentsBuilder().build().toUriString();

        ConfluencePage parentPage = findUniquePageByName(parentPageTitle);
        ConfluencePage newPage = cloneForCreate(parentPage);

        newPage.title = title;
        newPage.body.storage.value = content;

        ResponseEntity<ConfluencePage> exchange = REST.exchange(uri, HttpMethod.POST, new HttpEntity<>(newPage, getAuthHeaders()), ConfluencePage.class);
        //  Assert.isTrue(exchange.getStatusCode().series() == HttpStatus.Series.SUCCESSFUL, "Can't create child confluence page");

        return exchange.getBody();
    }

    public static ConfluencePage updatePageContent(String title, String newTitle, String newContent)
    {
        ConfluencePage initialPage = findUniquePageByName(title);

        String uri = getUriComponentsBuilder()
                .pathSegment(String.valueOf(initialPage.id))
                .build().toUriString();

        ConfluencePage newPage = cloneForUpdate(initialPage);

        newPage.title = newTitle;
        newPage.body.storage.value = newContent;
        newPage.version.number += 1;

        ResponseEntity<ConfluencePage> exchange = REST.exchange(uri, HttpMethod.PUT, new HttpEntity<>(newPage, getAuthHeaders()), ConfluencePage.class);
        //   Assert.isTrue(exchange.getStatusCode().series() == HttpStatus.Series.SUCCESSFUL, "Can't update confluence page");

        return exchange.getBody();
    }

    private static UriComponentsBuilder getUriComponentsBuilder()
    {
        return UriComponentsBuilder
                .fromHttpUrl("https://wiki.idexx.com/rest/api/content")
                .queryParam("expand", "version,space,body.storage,ancestors");
    }

    private static HttpHeaders getAuthHeaders()
    {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "Basic " + new BASE64Encoder().encode("anton-pacuk@idexx.com:hVqish3xHawn262tYRRC".getBytes()));
        return httpHeaders;
    }

    //    private static MultiValueMap getAuthHeaders()
//    {
//        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.set("Authorization", "Basic " + new BASE64Encoder().encode("anton-pacuk@idexx.com:hVqish3xHawn262tYRRC".getBytes()));
//        return httpHeaders;
//    }
    //    https://developer.atlassian.com/confdev/confluence-rest-api/confluence-rest-api-examples#ConfluenceRESTAPIExamples-Updateapage
//    http://localhost:8080/confluence/rest/api/content/3604482
//    {
//        "type": "page",
//        "title": "new page",
//        "ancestors":
//            [
//                {
//                    "id": 456
//                }
//            ],
//        "space":
//            {
//                "key": "TST"
//            },
//        "body":
//            {
//            "storage":
//                {
//                    "value": "<p>This is a new page</p>",
//                    "representation": "storage"
//                }
//            }
//    }
    private static ConfluencePage cloneForUpdate(ConfluencePage page)
    {
        ConfluencePage clone = new ConfluencePage();
        clone.id = page.id;
        clone.type = page.type;
        clone.title = page.title;
        clone.space = page.space;
        clone.body = page.body;
        clone.version = page.version;
        return clone;
    }

    //    https://developer.atlassian.com/confdev/confluence-rest-api/confluence-rest-api-examples#ConfluenceRESTAPIExamples-Createanewpageasachildofanotherpage
//    http://localhost:8080/confluence/rest/api/content/
//    {
//        "type":"page",
//        "title":"new page",
//        "ancestors":
//            [
//                {
//                    "id":456
//                }
//            ],
//        "space":
//        {
//            "key":"TST"
//        },
//        "body":
//            {
//                "storage":
//                {
//                    "value":"<p>This is a new page</p>",
//                    "representation":"storage"
//                }
//            }
//    }
    private static ConfluencePage cloneForCreate(ConfluencePage page)
    {
        ConfluencePage clone = new ConfluencePage();
        clone.type = page.type;
        clone.space = page.space;
        clone.ancestors = new ArrayList<>();
        clone.ancestors.add(page);
        clone.body = new ConfluencePageBody();
        clone.body.storage = new ConfluencePageBodyStorage();
        clone.body.storage.representation = "storage";
        return clone;
    }
}




