package efactory.tools;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.fasterxml.jackson.databind.node.ObjectNode;
import j2html.tags.ContainerTag;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;

import static j2html.TagCreator.*;

public class GitlabJiraLinker {

    static ObjectMapper mapper = new ObjectMapper();
    static String gitlabIssuesUrl = null;
    static String gitlabAuthHeader = null;
    static String jiraAuthHeader = null;
    static String jiraUserPass = null;
    static String jiraSearchApiUrl = null;
    static String jiraIssuesBrowseUrl = null;
    static String containerString = "#--#";

    //Fetch all Jira issues via Jira API
    public static ArrayList getJiraIssues(String uri) {
        ArrayList<JiraIssue> jiraIssues = new ArrayList<JiraIssue>();
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(uri);
        httpGet.setHeader("Authorization", jiraAuthHeader);
        CloseableHttpResponse response = null;

        try {
            response = client.execute(httpGet);
            HttpEntity entity = response.getEntity();
            String content = EntityUtils.toString(entity);

            ObjectNode node = mapper.readValue(content, ObjectNode.class);
            String issuesOnly = null;

            if (node.has("issues")) {
                JiraIssue[] jiraIssuesArray = mapper.treeToValue(node.get("issues"), JiraIssue[].class);
                jiraIssues.addAll(Arrays.asList(jiraIssuesArray));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jiraIssues;
    }

    //recursively (because of pagination) fetch all Gitlab issues via Gitlab API
    public static ArrayList recursiveGetGitlabIssues(String uri, ArrayList result) {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(uri);
        httpGet.setHeader("Authorization", gitlabAuthHeader);
        CloseableHttpResponse response = null;

        try {
            response = client.execute(httpGet);
            HttpEntity entity = response.getEntity();
            String content = EntityUtils.toString(entity);
            result.addAll(Arrays.asList(mapper.readValue(content, GitlabIssue[].class)));
            Header[] linkHeader = response.getHeaders("Link");
            String[] linkHeaderSplit = linkHeader[0].toString().split(",");
            for (String link : linkHeaderSplit) {
                if (link.contains("rel=\"next\"")) {
                    int i = link.indexOf("<", 0);
                    int j = link.indexOf(">", i);
                    String address = link.substring(i + 1, j);
                    recursiveGetGitlabIssues(address, result);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void main(String args[]) throws IOException {
        //read properties from parameters
        gitlabIssuesUrl = System.getProperty("gitlab.issues-url");
        gitlabAuthHeader = "Bearer " + System.getProperty("gitlab.access-token");
        jiraSearchApiUrl = System.getProperty("jira.search-api-url");
        jiraIssuesBrowseUrl = System.getProperty("jira.issues-browse-url");
        jiraUserPass = System.getProperty("jira.username") + ":" + System.getProperty("jira.password");
        jiraAuthHeader = "Basic " + Base64.getEncoder().encodeToString(jiraUserPass.getBytes());

        //fetch all Gitlab issues
        ArrayList<GitlabIssue> gitlabIssues = new ArrayList<GitlabIssue>();
        recursiveGetGitlabIssues(gitlabIssuesUrl, gitlabIssues);

        //fetch all Jira issues
        ArrayList<JiraIssue> jiraIssues = getJiraIssues(jiraSearchApiUrl);
        HashMap<String, JiraIssue> jiraIssuesByKey = new HashMap<>();

        //create HashMap to search Jira issues by its keys
        for (JiraIssue jiraIssue : jiraIssues) {
            jiraIssuesByKey.put(jiraIssue.getKey(), jiraIssue);
        }


        for (GitlabIssue gitLabIssue : gitlabIssues) {

            //check for linked Jira issues in description
            String description = gitLabIssue.getDescription();
            if (description != null && description.contains(containerString)) {
                int i = description.indexOf(containerString, 0);
                int j = description.indexOf(containerString, i + containerString.length());

                //multiple Jira issues need to be comma separated
                String[] linkedJiraIssues = description.substring(i + containerString.length(), j).split(",");
                for (String jiraIssueKey : linkedJiraIssues) {
                    if (jiraIssueKey != null && jiraIssueKey.contains("EF-")) {
                        jiraIssuesByKey.get(jiraIssueKey).getLinkedGitlabIssues().add(gitLabIssue);
                    }
                }
            }

        }

        //write issues to html
        ContainerTag page = html(
                head(
                        title("Jira issues overview")
                ),
                body(
                        div(attrs("#jiraIssues"),
                                each(jiraIssues, jiraIssue ->
                                        div(attrs(".jiraIssue"),
                                                h3(jiraIssue.getKey() + " " + jiraIssue.getFields().getSummary()),
                                                p("Status: " + jiraIssue.getFields().getStatus().getName()),
                                                a(jiraIssuesBrowseUrl + jiraIssue.getKey()).withHref(jiraIssuesBrowseUrl + jiraIssue.getKey()),
                                                h4("Linked Gitlab issues:"),
                                                ul(each(jiraIssue.getLinkedGitlabIssues(), gitlabIssue -> li(attrs(".gitlabIssue"),
                                                        h5(gitlabIssue.getTitle()),
                                                        p("State: " + gitlabIssue.getState()),
                                                        a(gitlabIssue.getWebUrl()).withHref(gitlabIssue.getWebUrl())
                                                )))
                                        ).withStyle("border: 1px solid black"))
                        )
                )
        );

        Appendable writer = new FileWriter("rendered.html");
        page.render(writer);
        ((FileWriter) writer).close();

    }

}