package service;


import java.util.HashMap;
import java.util.Map;
import java.util.Collection;

import corbaBanque.Compte;
import corbaBanque.IBanqueRemotePOA;

public class BanqueImpl extends IBanqueRemotePOA {

    private Map<Integer, Compte> accounts = new HashMap<>();
    private int nextCode = 1;

    public BanqueImpl() {}

    @Override
    public synchronized void creerCompte(Compte cpte) {
        int code = cpte.code;
        if (code == 0) {
            code = nextCode++;
            cpte.code = code;
        }
        accounts.put(code, cpte);
        System.out.println("Compte créé: code=" + code + " solde=" + cpte.solde);
    }

    @Override
    public synchronized void verser(float mt, int code) {
        Compte c = accounts.get(code);
        if (c == null) {
            System.out.println("verser: compte introuvable: " + code);
            return;
        }
        c.solde += mt;
        System.out.println("verser +" + mt + " sur " + code);
    }

    @Override
    public synchronized void retirer(float mt, int code) {
        Compte c = accounts.get(code);
        if (c == null) {
            System.out.println("retirer: compte introuvable: " + code);
            return;
        }
        if (c.solde < mt) {
            System.out.println("Fond insuffisant");
            return;
        }
        c.solde -= mt;
        System.out.println("retirer -" + mt + " sur " + code);
    }

    @Override
    public synchronized Compte getCompte(int code) {
        Compte c = accounts.get(code);
        if (c == null) {
            return new Compte(0, 0f);  // compte vide
        }
        return new Compte(c.code, c.solde);
    }

    @Override
    public synchronized Compte[] getComptes() {
        Collection<Compte> values = accounts.values();
        return values.toArray(new Compte[values.size()]);
    }

    @Override
    public double conversion(float mt) {
        return mt * 3.3;  // taux arbitraire
    }
}
