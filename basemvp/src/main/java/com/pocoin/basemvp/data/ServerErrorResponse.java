package com.pocoin.basemvp.data;

import io.realm.internal.Keep;

/**
 * Created by Administrator on 2016/11/11.
 */
@Keep
public class ServerErrorResponse {

    private String code;
    private String description;
    private String semantic;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSemantic() {
        return semantic;
    }

    public void setSemantic(String semantic) {
        this.semantic = semantic;
    }
}
