package com.nader.intelligent.util.http;

import rx.functions.Func1;

/**
 *
 * 剥离 最终数据
 * Created by zhouwei on 16/11/10.
 */

public class PayLoad<T> implements Func1<BaseResponse<T>,T> {
    @Override
    public T call(BaseResponse<T> tBaseResponse) {//获取数据失败时，包装一个Fault 抛给上层处理错误
        if(!tBaseResponse.isSuccess()){
            throw new Fault(tBaseResponse.code, (String) tBaseResponse.message);
        }
        return tBaseResponse.data;
    }
}
