package efactory.tools;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;
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
import java.util.Properties;

public class GitlabJiraLinker {

    static ObjectMapper mapper = new ObjectMapper();
    static String gitlabIssuesUrl = null;
    static String gitlabAuthHeader = null;
    static String jiraAuthHeader = null;
    static String jiraUserPass = null;
    static String jiraIssuesApiUrl = null;
    static String jiraIssuesBrowseUrl = null;

    public static JiraIssue getJiraIssue(String id) {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(jiraIssuesApiUrl + id);
        httpGet.setHeader("Authorization", jiraAuthHeader);
        CloseableHttpResponse response = null;

        JiraIssue result = null;
        try {
            response = client.execute(httpGet);
            HttpEntity entity = response.getEntity();
            String content = EntityUtils.toString(entity);
            result = mapper.readValue(content, JiraIssue.class);
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

    public static ArrayList recursiveGet(String uri, ArrayList result) {
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
                    recursiveGet(address, result);
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
        gitlabIssuesUrl = System.getProperty("gitlab.issues-url");
        gitlabAuthHeader = "Bearer " + System.getProperty("gitlab.access-token");
        jiraIssuesApiUrl = System.getProperty("jira.issues-api-url");
        jiraIssuesBrowseUrl = System.getProperty("jira.issues-browse-url");
        jiraUserPass = System.getProperty("jira.username") + ":" + System.getProperty("jira.password");
        jiraAuthHeader = "Basic " + Base64.getEncoder().encodeToString(jiraUserPass.getBytes());
        ArrayList<GitlabIssue> result = new ArrayList<GitlabIssue>();
        recursiveGet(gitlabIssuesUrl, result);

        PdfDocument pdf = null;
        try {
            pdf = new PdfDocument(new PdfWriter("issue_links.pdf"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Document document = new Document(pdf, PageSize.A4.rotate());


        Table table = new Table(2);
        Cell cell = new Cell(1, 1)
                .setTextAlignment(TextAlignment.CENTER)
                .add(new Paragraph("Gitlab issues"));
        table.addCell(cell);
        cell = new Cell(1, 1)
                .add(new Paragraph("Linked Jira issues"))
                .setTextAlignment(TextAlignment.CENTER);
        table.addCell(cell);

        for (GitlabIssue issue : result) {

            String description = issue.getDescription();

            cell = new Cell(1, 1)
                    .add(new Paragraph(issue.toString()))
                    .setVerticalAlignment(VerticalAlignment.MIDDLE).setFont(PdfFontFactory.createFont(StandardFonts.COURIER)).setFontSize(8);

            table.addCell(cell);
            if (description != null && description.contains("###")) {
                System.out.println(issue.toString());

                int i = description.indexOf("###", 0);
                int j = description.indexOf("###", i + 3);
                String[] jiraIssues = description.substring(i + 3, j).split(",");


                System.out.println("Linked to Jira issues:");
                String linksCombined = "";
                for (String jiraIssueKey : jiraIssues) {
                    JiraIssue jiraIssue = getJiraIssue(jiraIssueKey);
                    String printIssue = jiraIssue.getFields().getSummary() + " (" + jiraIssuesBrowseUrl + jiraIssue.getKey() + ")";
                    System.out.println(printIssue);
                    linksCombined += printIssue + "\n";
                }
                cell = new Cell(1, 1)
                        .add(new Paragraph(linksCombined))
                        .setVerticalAlignment(VerticalAlignment.MIDDLE).setFont(PdfFontFactory.createFont(StandardFonts.COURIER)).setFontSize(8);
                table.addCell(cell);
            } else {
                cell = new Cell(1, 1)
                        .add(new Paragraph(""))
                        .setVerticalAlignment(VerticalAlignment.MIDDLE).setFont(PdfFontFactory.createFont(StandardFonts.COURIER)).setFontSize(8);
                table.addCell(cell);
            }
        }


        document.add(table);
        document.close();


    }
}
