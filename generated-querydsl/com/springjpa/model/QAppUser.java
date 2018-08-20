package com.springjpa.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QAppUser is a Querydsl query type for AppUser
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QAppUser extends EntityPathBase<AppUser> {

    private static final long serialVersionUID = 1545370362L;

    public static final QAppUser appUser = new QAppUser("appUser");

    public final BooleanPath enabled = createBoolean("enabled");

    public final StringPath encrytedPassword = createString("encrytedPassword");

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public final StringPath userName = createString("userName");

    public QAppUser(String variable) {
        super(AppUser.class, forVariable(variable));
    }

    public QAppUser(Path<? extends AppUser> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAppUser(PathMetadata metadata) {
        super(AppUser.class, metadata);
    }

}

