package com.pocoin.digimage.base.realm;

import io.realm.RealmSchema;

/**
 * Created by Sen on 2017/11/27.
 */

public interface MigrationApi {
    /**
     * @param schema 迁移操作对象
     * @return 最大支持版本
     */
    long migrationVersion(RealmSchema schema) throws Throwable;
}
