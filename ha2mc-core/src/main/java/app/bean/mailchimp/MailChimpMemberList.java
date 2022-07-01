package app.bean.mailchimp;

import java.util.List;

public class MailChimpMemberList {

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


    @Override
    public String toString() {
        return "MailChimpMemberList{" +
                "members=" + members +
                ", totalItems=" + totalItems +
                '}';
    }
}
