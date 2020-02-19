package efactory.tools;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.*;

import com.fasterxml.jackson.databind.ObjectMapper;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class GitlabIssue {

    static ObjectMapper mapper = new ObjectMapper();

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String title;
    @JsonProperty(value = "description", access = JsonProperty.Access.WRITE_ONLY)
    private String description;
    @JsonProperty(value = "state", access = JsonProperty.Access.WRITE_ONLY)
    private String state;
    @JsonProperty(value = "web-url", access = JsonProperty.Access.WRITE_ONLY)
    private String webUrl;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("status")
    public String getStatus() {
        return getState();
    }

    @JsonProperty("summary")
    public String getSummary() {
        String hyperlink = "<a href=" + getWebUrl() + ">" + getTitle() + "</a>";
        return hyperlink;
    }

    @JsonProperty("tracker")
    public String getTracker() {
        return "Gitlab";
    }

    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    @JsonProperty("title")
    public void setTitle(String title) {
        this.title = title;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

    @JsonProperty("state")
    public String getState() {
        return state;
    }

    @JsonProperty("state")
    public void setState(String state) {
        this.state = state;
    }

    @JsonProperty("web_url")
    public String getWebUrl() {
        return webUrl;
    }

    @JsonProperty("web_url")
    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    @JsonProperty("issuetype")
    public String getIssueType() {

        return "Gitlab";
    }

    //@JsonAnyGetter
    @JsonIgnore
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    //@JsonAnySetter
    @JsonIgnore
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public String toString() {

        return this.getTitle() + " (" + this.getWebUrl() + ")";
    }
}