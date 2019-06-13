package com.example.firebasechatfinalmodule.model;

public class MessageBody {

    private  String from;
    private String message;
    private String time;
    private String seen;
    private String type;
    private  String message_ID;

    public String getMessage_ID() {
        return message_ID;
    }

    public void setMessage_ID(String message_ID) {
        this.message_ID = message_ID;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSeen() {
        return seen;
    }

    public void setSeen(String seen) {
        this.seen = seen;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
