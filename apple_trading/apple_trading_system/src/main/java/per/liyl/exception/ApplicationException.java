package per.liyl.exception;

import static per.liyl.enums.ApplicationExceptionEnum.SYSTEM_ERROR;

public class ApplicationException extends RuntimeException{

    private String errorCode;

    private String errorMsg;

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public ApplicationException(){
        this.errorCode = SYSTEM_ERROR.getErrorCode();
        this.errorMsg = SYSTEM_ERROR.getErrorMsg();
    }

    public ApplicationException(String errorCode, String errorMsg){
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }
}
