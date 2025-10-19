package per.liyl.gateway.response;

import per.liyl.enums.ApplicationExceptionEnum;

public class Response {

    private String respCode;

    private String respMsg;

    private long timestamp = System.currentTimeMillis();

    public Response() {
        this.respCode = ApplicationExceptionEnum.SUCCESS.getErrorCode();
        this.respMsg = ApplicationExceptionEnum.SUCCESS.getErrorMsg();
    }

    public Response(String code, String message) {
        this.respCode = code;
        this.respMsg = message;
    }

    public Response(ApplicationExceptionEnum applicationExceptionEnum) {
        this.respCode = applicationExceptionEnum.getErrorCode();
        this.respMsg = applicationExceptionEnum.getErrorMsg();
    }

    public static Response success(){
        return new Response(ApplicationExceptionEnum.SUCCESS);
    }

    public boolean isSuccess(){
        return ApplicationExceptionEnum.SUCCESS.getErrorCode().equals(this.respCode);
    }

    public static Response fail(){
        return new Response(ApplicationExceptionEnum.SUCCESS);
    }

    public String getRespCode() {
        return respCode;
    }

    public void setRespCode(String respCode) {
        this.respCode = respCode;
    }

    public String getRespMsg() {
        return respMsg;
    }

    public void setRespMsg(String respMsg) {
        this.respMsg = respMsg;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

}
