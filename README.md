# eFactory Issue Linking

Tool to display Jira issues and their linked Gitlab issues.

## Prerequisites

Docker and docker-compose

## Issue linking

Enter a comma separated list of Jira issue keys between two strings `§§--§§` anywhere in the description of a Gitlab issue. Example:

```bash
§§--§§EF-69,EF-70§§--§§ 
```

## Configure gitlab-jira-linker API

You need to set the following environment variables in your docker-compose.yml file:

* `GITLAB_ISSUES_URL`: The Gitlab API call to retrieve the relevant Gitlab issues, e.g. https://gitlab.fit.fraunhofer.de/api/v4/issues?per_page=100. The pages will be retrieved recursively.
* `GITLAB_ACCESS_TOKEN`: Generate an access token for Gitlab access at https://gitlab.fit.fraunhofer.de/profile/personal_access_tokens.
* `JIRA_USERNAME`.
* `JIRA_PASSWORD`
* `JIRA_SEARCH_API_URL`: The Jira API call to retrieve the relevant Jira issues, e.g. http://jira.fit.fraunhofer.de/rest/api/latest/search?jql=project=EF+order+by+key&maxResults=-1.
* `JIRA_ISSUE_BROWSE_URL`: The base URL by which to browse the Jira issues via their keys, e.g. https://jira.fit.fraunhofer.de/jira/browse/
* `CONTAINER_STRING`: The string to put the links to Jira issues between in Gitlab descriptions, currently `§§--§§` 

## Run

The API is a Spring boot application and can be started with

```bash
docker-compose up -d gitlab-jira-linker
```

This will expose port 8080. To get the list of Jira issues as json, open http://localhost:8080/links in your browser.

Start nginx to serve the website defined in `index.html`

```bash
docker-compose up -d nginx
```

The website will be available at http://localhost:8081. It uses Handsontable (https://handsontable.com/) to convert the json served by the API to an HTML table.
