package per.liyl.gateway.response;

import per.liyl.gateway.dtos.AppleOrderDTO;

import java.util.List;

public class AppleOrderResp extends PageResponse<AppleOrderDTO>{

    private List<AppleOrderDTO> contents;

    @Override
    public void setContents(List<AppleOrderDTO> contents) {
        this.contents = contents;
    }

    @Override
    public List<AppleOrderDTO> getContents() {
        return this.contents;
    }
}
