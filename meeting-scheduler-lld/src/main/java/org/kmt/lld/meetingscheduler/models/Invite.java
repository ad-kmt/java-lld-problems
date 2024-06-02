package org.kmt.lld.meetingscheduler.models;

import org.kmt.lld.meetingscheduler.models.enums.InviteResponse;

/**
 * Represents an invite to a meeting.
 */
public class Invite {
    private User participant;
    private InviteResponse inviteResponse;

    public Invite(User participant) {
        this.participant = participant;
        this.inviteResponse = InviteResponse.NO_RESPONSE;
    }

    public User getParticipant() {
        return participant;
    }

    public void setParticipant(User participant) {
        this.participant = participant;
    }

    public InviteResponse getInviteStatus() {
        return inviteResponse;
    }

    public void setInviteStatus(InviteResponse inviteResponse) {
        this.inviteResponse = inviteResponse;
    }

    @Override
    public String toString() {
        return "Invite{ " +
                " participant: " + participant.getName() +
                ", response: " + inviteResponse +
                " }";
    }
}
