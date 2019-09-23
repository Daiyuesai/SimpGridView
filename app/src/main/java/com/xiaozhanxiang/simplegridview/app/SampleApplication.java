package com.xiaozhanxiang.simplegridview.app;

import com.tencent.tinker.loader.app.TinkerApplication;
import com.tencent.tinker.loader.shareutil.ShareConstants;

/**
 * author: dai
 * date:2019/9/16
 */
public class SampleApplication extends TinkerApplication {



    public SampleApplication() {
        super(ShareConstants.TINKER_ENABLE_ALL, "com.xiaozhanxiang.simplegridview.app.SampleApplicationLike",  "com.tencent.tinker.loader.TinkerLoader", false);
    }
}
