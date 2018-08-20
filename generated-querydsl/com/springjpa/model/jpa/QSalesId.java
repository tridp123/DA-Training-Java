package com.springjpa.model.jpa;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QSalesId is a Querydsl query type for SalesId
 */
@Generated("com.querydsl.codegen.EmbeddableSerializer")
public class QSalesId extends BeanPath<SalesId> {

    private static final long serialVersionUID = 1688404642L;

    public static final QSalesId salesId = new QSalesId("salesId");

    public final ComparablePath<java.util.UUID> locationId = createComparable("locationId", java.util.UUID.class);

    public final ComparablePath<java.util.UUID> productId = createComparable("productId", java.util.UUID.class);

    public final ComparablePath<java.util.UUID> timeId = createComparable("timeId", java.util.UUID.class);

    public QSalesId(String variable) {
        super(SalesId.class, forVariable(variable));
    }

    public QSalesId(Path<? extends SalesId> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSalesId(PathMetadata metadata) {
        super(SalesId.class, metadata);
    }

}

