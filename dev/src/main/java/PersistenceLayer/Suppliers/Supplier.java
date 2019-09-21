package PersistenceLayer.Suppliers;

import javax.persistence.*;
import java.io.Serializable;


@Entity(name = "Supplier")
public class Supplier implements Serializable {

    @Id
    private int private_company_id;
    private String bank_account;
    private String payments_terms;
    private String name;
    private String address;

    public Supplier(int private_company_id, String bank_account, String payments_terms, String name, String address) {
        this.private_company_id = private_company_id;
        this.bank_account = bank_account;
        this.payments_terms = payments_terms;
        this.name = name;
        this.address = address;
    }


    public Supplier() {
    }


    //region getters & setters
    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public void setPrivate_company_id(int private_company_id) {
        this.private_company_id = private_company_id;
    }

    public void setBank_account(String back_account) {
        this.bank_account = back_account;
    }

    public void setPayments_terms(String payments_terms) {
        this.payments_terms = payments_terms;
    }

    public int getPrivate_company_id() {
        return private_company_id;
    }

    public String getBank_account() {
        return bank_account;
    }

    public String getPayments_terms() {
        return payments_terms;
    }


    //endregion


    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\nCompany id: " + private_company_id);
        stringBuilder.append("\nCompany name: " + name);
        stringBuilder.append("\nCompany address: " + address);
        stringBuilder.append("\nBank account: " + bank_account);
        stringBuilder.append("\nPayment terms: " + payments_terms);
        return stringBuilder.toString();
    }
}