package efactory.tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "key",
        "fields"
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class JiraIssue {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonPropertyOrder({
            "status",
            "summary"
    })
    @JsonIgnoreProperties(ignoreUnknown = true)
    public class Fields {

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonPropertyOrder({
                "name",
                "id"
        })
        @JsonIgnoreProperties(ignoreUnknown = true)
        public class Status {
            @JsonInclude(JsonInclude.Include.NON_NULL)
            @JsonPropertyOrder({
                    "id",
                    "name"
            })

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

    @JsonProperty("id")
    private String id;
    @JsonProperty("key")
    private String key;
    @JsonProperty("fields")
    private Fields fields;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public ArrayList<GitlabIssue> getLinkedGitlabIssues() {
        return linkedGitlabIssues;
    }

    public void setLinkedGitlabIssues(ArrayList<GitlabIssue> linkedGitlabIssues) {
        this.linkedGitlabIssues = linkedGitlabIssues;
    }

    @JsonProperty("linked-gitlab-issues")
    private ArrayList<GitlabIssue> linkedGitlabIssues = new ArrayList<GitlabIssue>();

    @JsonProperty("id")
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

