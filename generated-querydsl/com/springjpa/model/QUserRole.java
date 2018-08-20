package com.springjpa.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUserRole is a Querydsl query type for UserRole
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QUserRole extends EntityPathBase<UserRole> {

    private static final long serialVersionUID = -763259085L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUserRole userRole = new QUserRole("userRole");

    public final QAppRole appRole;

    public final QAppUser appUser;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public QUserRole(String variable) {
        this(UserRole.class, forVariable(variable), INITS);
    }

    public QUserRole(Path<? extends UserRole> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUserRole(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUserRole(PathMetadata metadata, PathInits inits) {
        this(UserRole.class, metadata, inits);
    }

    public QUserRole(Class<? extends UserRole> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.appRole = inits.isInitialized("appRole") ? new QAppRole(forProperty("appRole")) : null;
        this.appUser = inits.isInitialized("appUser") ? new QAppUser(forProperty("appUser")) : null;
    }

}

