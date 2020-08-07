package com.template.flows;

import co.paralleluniverse.fibers.Suspendable;
import net.corda.core.flows.FlowException;
import net.corda.core.flows.FlowLogic;
import net.corda.core.flows.FlowSession;
import net.corda.core.flows.InitiatedBy;


// ******************
// * Responder flow *
// ******************
@InitiatedBy(EchoInitiator.class)
public class EchoResponder extends FlowLogic<Void> {
    private FlowSession counterpartySession;

    public EchoResponder(FlowSession counterpartySession) {
        this.counterpartySession = counterpartySession;
    }

    @Suspendable
    @Override
    public Void call() throws FlowException {
        // Responder flow logic goes here.
        String request = counterpartySession.receive(String.class).unwrap(msg -> msg);
        System.out.println("Counter party: " + counterpartySession.getCounterparty().getName().getOrganisation());
        System.out.println("Received message: " + request);
        String reverseMsg = reverseMessage(request);
        System.out.println("Reverse message: " + reverseMsg);
        counterpartySession.send (reverseMsg);
        return null;

    }
    protected String reverseMessage(String msg) {
        String reverseString = new StringBuilder(msg).reverse().toString();
        return reverseString;
    }
}
