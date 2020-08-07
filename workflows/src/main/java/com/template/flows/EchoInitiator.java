package com.template.flows;

import co.paralleluniverse.fibers.Suspendable;
import net.corda.core.flows.FlowException;
import net.corda.core.flows.FlowLogic;
import net.corda.core.flows.InitiatingFlow;
import net.corda.core.flows.StartableByRPC;
import net.corda.core.identity.Party;
import net.corda.core.node.services.IdentityService;
import net.corda.core.utilities.ProgressTracker;
import net.corda.core.flows.*;
import java.util.Set;

// ******************
// * Initiator flow *
// ******************
@InitiatingFlow
@StartableByRPC
public class EchoInitiator extends FlowLogic<String> {
    private final String message;
    private final String counterParty;

    private final ProgressTracker progressTracker = new ProgressTracker();
    public EchoInitiator(String message, String counterParty ) {
        this.message = message;
        this.counterParty = counterParty;
    }
    @Override
    public ProgressTracker getProgressTracker() {
        return progressTracker;
    }

    @Suspendable
    @Override
    public String call() throws FlowException {
        // Initiator flow logic goes here.
        final IdentityService idService = getServiceHub().getIdentityService();
        final Set <Party>  matches = idService.partiesFromName(counterParty,true);
        if ( matches.isEmpty()) {
            System.err.println("Target string doesn't match any nodes on the network.");
        }else if (matches.size() > 1) {
            System.err.println("Target string matches multiple nodes on the network.");
        }
        else {
            final Party otherParty = matches.iterator().next();
            FlowSession otherPartySession = initiateFlow(otherParty);
            System.out.println("Message sent to the party: " + otherParty.getName().getOrganisation());
            String returnMsg = otherPartySession.sendAndReceive(String.class, message).unwrap(msg -> msg);
            return returnMsg;
        }
        return null;
    }


}
