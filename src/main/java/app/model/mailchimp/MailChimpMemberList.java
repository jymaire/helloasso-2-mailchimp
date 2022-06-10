package app.model.mailchimp;

import java.util.List;

public class MailChimpMemberList {

    // tmp debug, virer list
    private List<MailChimpList> lists;
    private List<MailChimpMember> members;
    private int totalItems;

    public List<MailChimpMember> getMembers() {
        return members;
    }

    public void setMembers(List<MailChimpMember> members) {
        this.members = members;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }

    public List<MailChimpList> getLists() {
        return lists;
    }

    public void setLists(List<MailChimpList> lists) {
        this.lists = lists;
    }

    @Override
    public String toString() {
        return "MailChimpMemberList{" +
                "lst" + lists +
                "members=" + members +
                ", totalItems=" + totalItems +
                '}';
    }
}
