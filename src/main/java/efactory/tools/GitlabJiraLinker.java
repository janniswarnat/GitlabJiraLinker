package efactory.tools;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

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

public class GitlabJiraLinker {

    static ObjectMapper mapper = new ObjectMapper();
    static String gitlabIssuesUrl = null;
    static String gitlabAuthHeader = null;
    static String jiraAuthHeader = null;
    static String jiraUserPass = null;
    static String jiraIssuesApiUrl = null;
    static String jiraIssuesBrowseUrl = null;
    static String containerString = "#--#";

    //fetch information about particular issue from Jira API
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

    //recursively (because of pagination) fetch all Gitlab issues via Gitlab API
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
        //read properties from parameters
        gitlabIssuesUrl = System.getProperty("gitlab.issues-url");
        gitlabAuthHeader = "Bearer " + System.getProperty("gitlab.access-token");
        jiraIssuesApiUrl = System.getProperty("jira.issues-api-url");
        jiraIssuesBrowseUrl = System.getProperty("jira.issues-browse-url");
        jiraUserPass = System.getProperty("jira.username") + ":" + System.getProperty("jira.password");
        jiraAuthHeader = "Basic " + Base64.getEncoder().encodeToString(jiraUserPass.getBytes());

        //fetch all Gitlab issues
        ArrayList<GitlabIssue> result = new ArrayList<GitlabIssue>();
        recursiveGet(gitlabIssuesUrl, result);

        // create workbook and column headers
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Issue_links");
        int rowCount = -1;
        Row row = sheet.createRow(++rowCount);
        int columnCount = -1;
        XSSFCellStyle boldStyle = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        boldStyle.setFont(font);
        Cell cell = row.createCell(++columnCount);
        cell.setCellValue("Gitlab issue web URL");
        cell.setCellStyle(boldStyle);
        cell = row.createCell(++columnCount);
        cell.setCellValue("Gitlab issue title");
        cell.setCellStyle(boldStyle);
        cell = row.createCell(++columnCount);
        cell.setCellValue("Gitlab issue state");
        cell.setCellStyle(boldStyle);
        cell = row.createCell(++columnCount);
        cell.setCellValue("Linked Jira issues");
        cell.setCellStyle(boldStyle);

        for (GitlabIssue issue : result) {

            //write Gitlab issue information to workbook
            String description = issue.getDescription();
            row = sheet.createRow(++rowCount);
            columnCount = -1;
            cell = row.createCell(++columnCount);
            cell.setCellValue(issue.getWebUrl());
            cell = row.createCell(++columnCount);
            cell.setCellValue(issue.getTitle());
            cell = row.createCell(++columnCount);
            cell.setCellValue(issue.getState());

            //check for linked Jira issues in description
            if (description != null && description.contains(containerString)) {
                int i = description.indexOf(containerString, 0);
                int j = description.indexOf(containerString, i + containerString.length());

                //multiple Jira issues need to be comma separated
                String[] jiraIssues = description.substring(i + containerString.length(), j).split(",");
                String linksCombined = "";
                for (String jiraIssueKey : jiraIssues) {
                    if(jiraIssueKey != null && jiraIssueKey.contains("EF-")) {
                        JiraIssue jiraIssue = getJiraIssue(jiraIssueKey.trim());
                        String printIssue = jiraIssue.getFields().getSummary() + " (" + jiraIssuesBrowseUrl + jiraIssue.getKey() + ")";
                        linksCombined += printIssue + ";";
                    }
                }
                //write linked Jira issues to Excel
                cell = row.createCell(++columnCount);
                cell.setCellValue(linksCombined);
            }
        }

        try (FileOutputStream outputStream = new FileOutputStream("Issue_links.xlsx")) {
            workbook.write(outputStream);
        }

    }
}
