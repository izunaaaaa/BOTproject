
package com.example.bot.model;

import com.google.gson.annotations.Expose;

//List를 담는 Response클래스
@SuppressWarnings("unused")
public class ResList {

    @Expose
    private BodyList body;
    @Expose
    private Header header;
    @Expose
    private ResList response;

    public BodyList getBody() {
        return body;
    }

    public void setBody(BodyList body) {
        this.body = body;
    }

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public ResList getResponse() {
        return response;
    }

    public void setResponse(ResList response) {
        this.response = response;
    }

}
