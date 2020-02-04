package efactory.tools;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "self",
        "key",
        "fields"
})
public class JiraIssue {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonPropertyOrder({
            "created",
            "status",
            "summary"
    })
    public class Fields {

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonPropertyOrder({
                "self",
                "description",
                "iconUrl",
                "name",
                "id",
                "statusCategory"
        })
        public class Status {
            @JsonInclude(JsonInclude.Include.NON_NULL)
            @JsonPropertyOrder({
                    "self",
                    "id",
                    "key",
                    "colorName",
                    "name"
            })
            public class StatusCategory {

                @JsonProperty("self")
                private String self;
                @JsonProperty("id")
                private Integer id;
                @JsonProperty("key")
                private String key;
                @JsonProperty("colorName")
                private String colorName;
                @JsonProperty("name")
                private String name;
                @JsonIgnore
                private Map<String, Object> additionalProperties = new HashMap<String, Object>();

                @JsonProperty("self")
                public String getSelf() {
                    return self;
                }

                @JsonProperty("self")
                public void setSelf(String self) {
                    this.self = self;
                }

                @JsonProperty("id")
                public Integer getId() {
                    return id;
                }

                @JsonProperty("id")
                public void setId(Integer id) {
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

                @JsonProperty("colorName")
                public String getColorName() {
                    return colorName;
                }

                @JsonProperty("colorName")
                public void setColorName(String colorName) {
                    this.colorName = colorName;
                }

                @JsonProperty("name")
                public String getName() {
                    return name;
                }

                @JsonProperty("name")
                public void setName(String name) {
                    this.name = name;
                }

                @JsonAnyGetter
                public Map<String, Object> getAdditionalProperties() {
                    return this.additionalProperties;
                }

                @JsonAnySetter
                public void setAdditionalProperty(String name, Object value) {
                    this.additionalProperties.put(name, value);
                }

            }

            @JsonProperty("self")
            private String self;
            @JsonProperty("description")
            private String description;
            @JsonProperty("iconUrl")
            private String iconUrl;
            @JsonProperty("name")
            private String name;
            @JsonProperty("id")
            private String id;
            @JsonProperty("statusCategory")
            private StatusCategory statusCategory;
            @JsonIgnore
            private Map<String, Object> additionalProperties = new HashMap<String, Object>();

            @JsonProperty("self")
            public String getSelf() {
                return self;
            }

            @JsonProperty("self")
            public void setSelf(String self) {
                this.self = self;
            }

            @JsonProperty("description")
            public String getDescription() {
                return description;
            }

            @JsonProperty("description")
            public void setDescription(String description) {
                this.description = description;
            }

            @JsonProperty("iconUrl")
            public String getIconUrl() {
                return iconUrl;
            }

            @JsonProperty("iconUrl")
            public void setIconUrl(String iconUrl) {
                this.iconUrl = iconUrl;
            }

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

            @JsonProperty("statusCategory")
            public StatusCategory getStatusCategory() {
                return statusCategory;
            }

            @JsonProperty("statusCategory")
            public void setStatusCategory(StatusCategory statusCategory) {
                this.statusCategory = statusCategory;
            }

            @JsonAnyGetter
            public Map<String, Object> getAdditionalProperties() {
                return this.additionalProperties;
            }

            @JsonAnySetter
            public void setAdditionalProperty(String name, Object value) {
                this.additionalProperties.put(name, value);
            }

        }


        @JsonProperty("created")
        private String created;
        @JsonProperty("status")
        private Status status;
        @JsonProperty("summary")
        private String summary;
        @JsonIgnore
        private Map<String, Object> additionalProperties = new HashMap<String, Object>();

        @JsonProperty("created")
        public String getCreated() {
            return created;
        }

        @JsonProperty("created")
        public void setCreated(String created) {
            this.created = created;
        }

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

        @JsonAnyGetter
        public Map<String, Object> getAdditionalProperties() {
            return this.additionalProperties;
        }

        @JsonAnySetter
        public void setAdditionalProperty(String name, Object value) {
            this.additionalProperties.put(name, value);
        }

    }

    @JsonProperty("id")
    private String id;
    @JsonProperty("self")
    private String self;
    @JsonProperty("key")
    private String key;
    @JsonProperty("fields")
    private Fields fields;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("self")
    public String getSelf() {
        return self;
    }

    @JsonProperty("self")
    public void setSelf(String self) {
        this.self = self;
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

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }
}

