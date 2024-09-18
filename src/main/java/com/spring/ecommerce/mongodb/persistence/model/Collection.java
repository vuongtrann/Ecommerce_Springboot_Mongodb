package com.spring.ecommerce.mongodb.persistence.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.joda.time.DateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "collection")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)

public class Collection {
    @Id
    @JsonProperty("id")
    private String id;
    @JsonProperty("collectionName")
    private String collectionName;

    @JsonProperty("createAt")
    @JsonIgnore
    LocalDateTime createdAt;
    @JsonProperty("updatedAt")
    @JsonIgnore
    LocalDateTime updatedAt;
}
