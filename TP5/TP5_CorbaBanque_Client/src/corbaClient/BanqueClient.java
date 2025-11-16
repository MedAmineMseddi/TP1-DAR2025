package corbaClient;



import org.omg.CORBA.ORB;
import org.omg.CosNaming.*;
import org.omg.CosNaming.NamingContextPackage.*;

import corbaBanque.IBanqueRemote;
import corbaBanque.IBanqueRemoteHelper;
import corbaBanque.Compte;

public class BanqueClient {
    public static void main(String[] args) {
        try {
            // Initialize ORB
            ORB orb = ORB.init(args, null);

            // Get the root naming context
            org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
            NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

            // Resolve the object reference in naming
            String name = "Banque";
            org.omg.CORBA.Object obj = ncRef.resolve_str(name);

            IBanqueRemote banque = IBanqueRemoteHelper.narrow(obj);

            System.out.println("Client connected to Banque server!");

            // Create a new account
            Compte c1 = new Compte(0, 100.0f);
            banque.creerCompte(c1);
            System.out.println("Created account: code=" + c1.code + " solde=" + c1.solde);

            // Get all accounts
            Compte[] list = banque.getComptes();
            System.out.println("\nList of accounts:");
            for (Compte c : list) {
                System.out.println(" - code=" + c.code + ", solde=" + c.solde);
            }

            // Test operations on first account
            if (list.length > 0) {
                int code = list[0].code;

                banque.verser(50.0f, code);
                banque.retirer(20.0f, code);

                Compte upd = banque.getCompte(code);
                System.out.println("\nAfter operations:");
                System.out.println("Account " + upd.code + " solde=" + upd.solde);
            }

            // Conversion test
            double dt = banque.conversion(10.0f);
            System.out.println("\n10 EUR = " + dt + " DT");

        } catch (Exception e) {
            System.out.println("ERROR : " + e);
            e.printStackTrace(System.out);
        }
    }
}
