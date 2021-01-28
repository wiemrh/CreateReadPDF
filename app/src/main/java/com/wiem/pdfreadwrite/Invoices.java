package com.wiem.pdfreadwrite;

class Invoices {
    String désignation ="" ;
    Double mTVA ;
    Double montantHT ;
    Double prixUHT ;
    Double quantite ;
    Integer référence ;

    public Invoices(String désignation, Double mTVA, Double montantHT, Double prixUHT, Double quantite, Integer référence) {
        this.désignation = désignation;
        this.mTVA = mTVA;
        this.montantHT = montantHT;
        this.prixUHT = prixUHT;
        this.quantite = quantite;
        this.référence = référence;
    }

    public String getDésignation() {
        return désignation;
    }

    public void setDésignation(String désignation) {
        this.désignation = désignation;
    }

    public Double getmTVA() {
        return mTVA;
    }

    public void setmTVA(Double mTVA) {
        this.mTVA = mTVA;
    }

    public Double getMontantHT() {
        return montantHT;
    }

    public void setMontantHT(Double montantHT) {
        this.montantHT = montantHT;
    }

    public Double getPrixUHT() {
        return prixUHT;
    }

    public void setPrixUHT(Double prixUHT) {
        this.prixUHT = prixUHT;
    }

    public Double getQuantite() {
        return quantite;
    }

    public void setQuantite(Double quantite) {
        this.quantite = quantite;
    }

    public Integer getRéférence() {
        return référence;
    }

    public void setRéférence(Integer référence) {
        this.référence = référence;
    }

    @Override
    public String toString() {
        return "Invoices{" +
                "désignation='" + désignation + '\'' +
                ", mTVA=" + mTVA +
                ", montantHT=" + montantHT +
                ", prixUHT=" + prixUHT +
                ", quantite=" + quantite +
                ", référence=" + référence +
                '}';
    }
}
