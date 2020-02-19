package efactory.tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class JiraIssue {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public class Fields {

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonIgnoreProperties(ignoreUnknown = true)
        public class Status {
            @JsonInclude(JsonInclude.Include.NON_NULL)
            @JsonProperty("name")
            private String name;
            @JsonProperty("id")
            private String id;
            @JsonIgnore
            private Map<String, Object> additionalProperties = new HashMap<String, Object>();

            @JsonProperty("name")
            public String getName() {
                return name;
            }

            @JsonProperty("name")
            public void setName(String name) {
                this.name = name;
            }

            @JsonProperty("id")
            public String getId() {
                return id;
            }

            @JsonProperty("id")
            public void setId(String id) {
                this.id = id;
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
        }

        @JsonProperty("status")
        private Status status;
        @JsonProperty("summary")
        private String summary;
        @JsonIgnore
        private Map<String, Object> additionalProperties = new HashMap<String, Object>();

        @JsonProperty("status")
        public Status getStatus() {
            return status;
        }

        @JsonProperty("status")
        public void setStatus(Status status) {
            this.status = status;
        }

        @JsonProperty("summary")
        public String getSummary() {
            return summary;
        }

        @JsonProperty("summary")
        public void setSummary(String summary) {
            this.summary = summary;
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

    }

    @JsonProperty(value = "id", access = JsonProperty.Access.WRITE_ONLY)
    private String id;
    @JsonProperty(value = "key", access = JsonProperty.Access.WRITE_ONLY)
    private String key;
    @JsonProperty(value = "fields", access = JsonProperty.Access.WRITE_ONLY)
    private Fields fields;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonIgnore
    public String getWebUrl (){
        return System.getenv("JIRA_ISSUE_BROWSE_URL")+getKey();
    }

    @JsonProperty("__children")
    public ArrayList<GitlabIssue> getLinkedGitlabIssues() {
        return linkedGitlabIssues;
    }

    @JsonProperty("__children")
    public void setLinkedGitlabIssues(ArrayList<GitlabIssue> linkedGitlabIssues) {
        this.linkedGitlabIssues = linkedGitlabIssues;
    }

    @JsonProperty("__children")
    private ArrayList<GitlabIssue> linkedGitlabIssues = new ArrayList<GitlabIssue>();

    @JsonIgnore
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("key")
    public String getKey() {
        return key;
    }

    @JsonProperty("key")
    public void setKey(String key) {
        this.key = key;
    }

    @JsonProperty("fields")
    public Fields getFields() {
        return fields;
    }

    @JsonProperty("fields")
    public void setFields(Fields fields) {
        this.fields = fields;
    }

    @JsonProperty("status")
    public String getStatus() {
        return getFields().getStatus().getName();
    }

    @JsonProperty("summary")
    public String getSummary() {
        String hyperlink = "<a href=" + getWebUrl() + ">" + getFields().getSummary() + "</a>";
        return hyperlink;
    }

    @JsonProperty("tracker")
    public String getTracker() {
        return "Jira";
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
        return (this.getKey() + " " + this.getFields().getSummary());
    }
}

