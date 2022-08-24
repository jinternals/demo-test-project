package com.jinternals.demo.domain;

import com.jinternals.demo.validators.group.OnCreate;
import com.jinternals.demo.validators.group.OnUpdate;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.couchbase.core.index.CompositeQueryIndex;
import org.springframework.data.couchbase.core.index.QueryIndexed;
import org.springframework.data.couchbase.core.mapping.Document;
import org.springframework.data.couchbase.core.mapping.Field;
import org.springframework.stereotype.Indexed;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

@Builder
@Data
@Document
@Indexed
//Keyspace index
@CompositeQueryIndex(
        fields = {
                "_class"
        })
@EqualsAndHashCode(of = {"id"})
@ToString
public class Product {

    @Id
    @Null(groups = OnCreate.class)
    @NotEmpty(message = "Empty or null id is not allowed", groups = OnUpdate.class )
    private String id;

    @Field
    @NotEmpty(message = "Empty or null name is not allowed")
    private String name;
    @Field
    private String description;
    @Field
    @QueryIndexed
    @NotNull(message = "null Empty is not allowed")
    private ProductType type;
}
