package com.techhive.Technology.DTO;

public class ChatMessageDTO {
    private String sender;
    private String content;
    private String time;

    public String getSender() { return sender; }
    public void setSender(String sender) { this.sender = sender; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }
}