package corbaServer;

import org.omg.CORBA.ORB;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;

import corbaBanque.IBanqueRemote;
import corbaBanque.IBanqueRemoteHelper;
import service.BanqueImpl;

import org.omg.CosNaming.*;
import org.omg.CosNaming.NamingContextPackage.*;

public class BanqueServer {
    public static void main(String[] args) {
        try {
            // Initialize the ORB
            ORB orb = ORB.init(args, null);

            // Get reference to RootPOA and activate the POAManager
            POA rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
            rootpoa.the_POAManager().activate();

            // Create servant and register it with the ORB
            BanqueImpl banqueImpl = new BanqueImpl();
            org.omg.CORBA.Object ref = rootpoa.servant_to_reference(banqueImpl);
            IBanqueRemote href = IBanqueRemoteHelper.narrow(ref);

            // Get the naming context
            org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
            NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

            // Bind the Object Reference in Naming
            String name = "Banque";
            NameComponent path[] = ncRef.to_name(name);
            ncRef.rebind(path, href);

            System.out.println("BanqueServer ready and waiting ...");

            // Wait for invocations from clients
            orb.run();
        } catch (Exception e) {
            System.err.println("ERROR : " + e);
            e.printStackTrace(System.out);
        }
        System.out.println("BanqueServer Exiting ...");
    }
}
