package com.ob.common.sm3token.base;

public class ResponseResultHelper {

    public static ResponseResult returnResult(ResponseExecutor responseExecutor) {
        ResponseResult responseResult = new ResponseResult();
        try {
            responseExecutor.apply(responseResult);
        } catch (DataException e) {
            e.printStackTrace();
            responseResult.putException(e);
        } catch (Exception e) {
            String message = e.getMessage();
            DataException dataException = new DataException("-1", message);
            e.printStackTrace();
            responseResult.putException(dataException);
        }
        return responseResult;
    }

    public interface ResponseExecutor {
        void apply(ResponseResult responseResult) throws DataException, Exception;
    }

}
