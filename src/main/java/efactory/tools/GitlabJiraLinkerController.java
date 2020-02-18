package efactory.tools;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
public class GitlabJiraLinkerController {

    @RequestMapping(method = RequestMethod.GET, path = "/links")
    @ResponseBody
    public ArrayList<JiraIssue> getLinks() {
        return new GitlabJiraLinker().getJiraIssues();
    }
}
