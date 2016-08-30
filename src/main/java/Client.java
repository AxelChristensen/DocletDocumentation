

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import sun.misc.BASE64Encoder;

public class Client
{
public static final String CONFLUENCE_ACCOUNT = "aptest";
public static final String CONFLUENCE_PASSWORD = "Idexx123!";
    private static final RestTemplate REST = new RestTemplate();
public static void main(String[] args)
{
RestTemplate rest = new RestTemplate();
    String uri = getUriComponentsBuilder()
            .queryParam("title", "Axels Test Page")
            .build().toUriString();
ResponseEntity exchange = REST.exchange(uri, HttpMethod.GET, new HttpEntity(getAuthHeaders()), String.class);
System.out.println(" "  + exchange.getBody());
  ConfluenceSearchResults cp= findPageByName("Axels Test Page");
//    updatePageContent("Axels Test Page","Now new!","Something else");
}



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

    private static ConfluenceSearchResults findPageByName(String name)
    {ResponseEntity<ConfluenceSearchResults> exchange=null;
        try
        {
            String uri = getUriComponentsBuilder()
                    .queryParam("title", name)
                    .build().toUriString();
            HttpEntity ee=new HttpEntity<>(getAuthHeaders());
            exchange = REST.exchange(uri, HttpMethod.GET, ee, ConfluenceSearchResults.class);
            //   Assert.isTrue(exchange.getStatusCode().series() == HttpStatus.Series.SUCCESSFUL, "Can't execute search");
            System.out.println("GOT IT");
        }
        catch (Exception ex){ex.printStackTrace();}


        return exchange.getBody();
    }


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


private static HttpHeaders getAuthHeaders()
{
HttpHeaders httpHeaders = new HttpHeaders();
httpHeaders.set("Authorization", "Basic " + new BASE64Encoder().encode((CONFLUENCE_ACCOUNT + ":" + CONFLUENCE_PASSWORD).getBytes()));
return httpHeaders;
}


    private static UriComponentsBuilder getUriComponentsBuilder()
    {
        return UriComponentsBuilder
                .fromHttpUrl("https://wiki.idexx.com/rest/api/content")
                .queryParam("expand", "version,space,body.storage,ancestors");
    }
}