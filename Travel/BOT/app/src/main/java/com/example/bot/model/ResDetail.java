
package com.example.bot.model;

import com.google.gson.annotations.Expose;

//Detail을 담는 Response클래스
@SuppressWarnings("unused")
public class ResDetail {

    @Expose
    private BodyDetail body;
    @Expose
    private Header header;
    @Expose
    private ResDetail response;

    public BodyDetail getBody() {
        return body;
    }

    public void setBody(BodyDetail body) {
        this.body = body;
    }

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public ResDetail getResponse() {
        return response;
    }

    public void setResponse(ResDetail response) {
        this.response = response;
    }

}
