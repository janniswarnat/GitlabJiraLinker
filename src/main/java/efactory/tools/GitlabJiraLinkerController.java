package efactory.tools;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
public class GitlabJiraLinkerController {

    @RequestMapping(method = RequestMethod.GET, path = "/links")
    @ResponseBody
    public ArrayList<JiraIssue> getLinks(@RequestParam("gitlabIssuesUrl") String gitlabIssuesUrl,
                                         @RequestParam("gitlabAccessToken") String gitlabAccessToken,
                                         @RequestParam("jiraUsername") String jiraUsername,
                                         @RequestParam("jiraPassword") String jiraPassword,
                                         @RequestParam("jiraSearchApiUrl") String jiraSearchApiUrl,
                                         @RequestParam("jiraIssuesBrowseUrl") String jiraIssuesBrowseUrl,
                                         @RequestParam("containerString") String containerString
    ) {
        return new GitlabJiraLinker(gitlabIssuesUrl,gitlabAccessToken,jiraUsername,jiraPassword,jiraSearchApiUrl,jiraIssuesBrowseUrl,containerString).getJiraIssues();
    }
}
