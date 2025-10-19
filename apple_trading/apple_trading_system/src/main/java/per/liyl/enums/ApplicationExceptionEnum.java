package per.liyl.enums;

import per.liyl.exception.ApplicationException;

public enum ApplicationExceptionEnum {

    SUCCESS("000000", "成功！"),

    ORDER_ID_ERROR("100001", "订单ID不能为空！"),
    ORDER_UNIT_ERROR("100002", "计量单位不合规！"),
    ORDER_EXIST_ERROR("100003", "订单已存在！"),
    ORDER_NOT_EXIST_ERROR("100004", "订单不存在！"),
    ORDER_ADD_ERROR("100005", "订单新增失败！"),
    ORDER_UPDATE_ERROR("100006", "订单更新失败！"),

    SYSTEM_ERROR("999999", "系统异常！"),
    ;

    ApplicationExceptionEnum(String errorCode, String errorMsg){
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    private String errorCode;

    private String errorMsg;

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public ApplicationException exception(){
        return new ApplicationException(errorCode, errorMsg);
    }

}
