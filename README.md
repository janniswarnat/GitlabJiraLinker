# eFactory Issue Linking

Tool to display Jira issues and their linked Gitlab issues.

## Issue linking

Enter a comma separated list of Jira issue keys between two strings `§§--§§` anywhere in the description of a Gitlab issue. Example:

```bash
§§--§§EF-69,EF-70§§--§§ 
```

## Run

The API is a Spring boot application and can be started with

```bash
docker-compose up
```

This will expose port 8080. To get the list of Jira issues, use Postman to call endpoint

```
http://localhost:8080/links
```

You need to provide the following query parameters:

* `gitlabIssuesUrl`: The Gitlab API call to retrieve the relevant Gitlab issues, e.g. https://gitlab.fit.fraunhofer.de/api/v4/issues?per_page=100. The pages will be retrieved recursively.
* `gitlabAccessToken`: Generate an access token for Gitlab access at https://gitlab.fit.fraunhofer.de/profile/personal_access_tokens.
* `jiraUsername`: You may have to encode special characters (see https://en.wikipedia.org/wiki/Percent-encoding).
* `jiraPassword`: You may have to encode special characters (see https://en.wikipedia.org/wiki/Percent-encoding).
* `jiraSearchApiUrl`: The Jira API call to retrieve the relevant Jira issues, e.g. http://jira.fit.fraunhofer.de/rest/api/latest/search?jql=project=EF+order+by+key&maxResults=-1. In Postman you have to encode the + sign, e.g. http://jira.fit.fraunhofer.de/rest/api/latest/search?jql=project=EF%2Border%2Bby%2Bkey&maxResults=-1.
* `jiraIssuesBrowseUrl`: The base URL by which to browse the Jira issues via their keys, e.g. https://jira.fit.fraunhofer.de/jira/browse/
* `containerString`: The string to put the links to Jira issues between in Gitlab descriptions, currently `§§--§§` 