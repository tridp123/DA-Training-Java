package com.springjpa.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QAppRole is a Querydsl query type for AppRole
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QAppRole extends EntityPathBase<AppRole> {

    private static final long serialVersionUID = 1545277349L;

    public static final QAppRole appRole = new QAppRole("appRole");

    public final NumberPath<Long> roleId = createNumber("roleId", Long.class);

    public final StringPath roleName = createString("roleName");

    public QAppRole(String variable) {
        super(AppRole.class, forVariable(variable));
    }

    public QAppRole(Path<? extends AppRole> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAppRole(PathMetadata metadata) {
        super(AppRole.class, metadata);
    }

}

