package is.yranac.canary.model.membership;

import is.yranac.canary.model.contact.Contact;
import is.yranac.canary.model.customer.Customer;
import is.yranac.canary.model.invitation.Invitation;

/**
 * Created by cumisnic on 8/22/14.
 */
public class Member {

    public static final int MEMBER         = 0;
    public static final int INVITATION     = 2;
    public static final int ADD_NEW_MEMBER = 3;



    public int memberType;


    public Customer   customer;
    public Contact    contact;
    public Invitation invitation;

    public Member(Customer customer) {
        memberType = MEMBER;
        this.customer = customer;
    }


    public Member(Invitation invitation) {
        memberType = INVITATION;
        this.invitation = invitation;
    }

    public Member(int memberType) {
        this.memberType = memberType;
    }

}
