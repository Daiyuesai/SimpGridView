package com.xiaozhanxiang.simplegridview.callback;

import java.util.HashMap;

/**
 * author: dai
 * date:2018/11/21
 */
public interface PermissionResultListener {
    /**
     *
     * @param permission
     * @param isAllAgree  请求的权限是否已经全部同意
     */
    void permissionResult(HashMap<String, Boolean> permission, boolean isAllAgree);

}
