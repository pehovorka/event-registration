package com.cscu9yw.eventregistrationbackend.model;

public class RandomUserResponse {
    private Object _links;
    private User user;

    public RandomUserResponse(Object _links, User user) {
        this._links = _links;
        this.user = user;
    }

    protected RandomUserResponse() {
    }

    public Object get_links() {
        return _links;
    }

    public void set_links(Object _links) {
        this._links = _links;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "RandomUserResponse{" +
                "_links='" + _links + '\'' +
                ", user=" + user +
                '}';
    }
}
